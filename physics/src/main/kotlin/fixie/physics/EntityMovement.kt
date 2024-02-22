package fixie.physics

import fixie.*
import fixie.geometry.Geometry
import fixie.geometry.Position
import java.util.*
import kotlin.time.Duration
import kotlin.time.DurationUnit

internal class EntityMovement(
        private val stepDuration: Duration,
        private val tileTree: TileTree,
        private val entityClustering: EntityClustering
) {
    val intersections = GrowingBuffer.withMutableElements(5) { Intersection() }
    private val processedIntersections = GrowingBuffer.withImmutableElements(5, UUID.randomUUID())

    private val interestingTiles = mutableListOf<Tile>()
    private val queryTiles = GrowingBuffer.withImmutableElements(10, DUMMY_TILE)

    private val interestingEntities = mutableListOf<Entity>()

    private val entityIntersection = Position.origin()
    private val tileIntersection = Position.origin()

    private lateinit var entity: Entity

    private var deltaX = 0.m
    private var deltaY = 0.m
    var originalDelta = 0.m
    private var remainingBudget = FixDisplacement.ONE

    class Intersection {
        var myX = 0.m
        var myY = 0.m
        var otherX = 0.m
        var otherY = 0.m
        var radius = 0.m
        var delta = 0.m
        var bounce = FixDisplacement.ZERO
        var friction = FixDisplacement.ZERO
        var otherVelocity: Velocity? = null
        var otherRadius = 0.m
        var otherID: UUID = UUID.randomUUID()

        fun validate() {
            val dx = otherX - myX
            val dy = otherY - myY
            if (dx * dx + dy * dy > radius * (radius * 1.1)) {
                println("invalid intersection at ($myX, $myY) and ($otherX, $otherY)")
                throw RuntimeException("distance between points is ($dx, $dy): ${sqrt(dx * dx + dy * dy)} but radius is $radius")
            }
        }
    }

    fun start(entity: Entity) {
        this.entity = entity
        deltaX = entity.wipVelocity.x * stepDuration
        deltaY = entity.wipVelocity.y * stepDuration
        originalDelta = sqrt(deltaX * deltaX + deltaY * deltaY)

        intersections.clear()
        processedIntersections.clear()
        remainingBudget = FixDisplacement.ONE
    }

    fun determineInterestingTilesAndEntities() {
        val safeRadius = 2 * entity.properties.radius + 2 * originalDelta
        val safeMinX = entity.wipPosition.x - safeRadius
        val safeMinY = entity.wipPosition.y - safeRadius
        val safeMaxX = entity.wipPosition.x + safeRadius
        val safeMaxY = entity.wipPosition.y + safeRadius
        queryTiles.clear()
        interestingTiles.clear()
        tileTree.query(safeMinX, safeMinY, safeMaxX, safeMaxY, queryTiles)
        for (index in 0 until queryTiles.size) {
            val tile = queryTiles[index]
            val endX = tile.collider.startX + tile.collider.lengthX
            val endY = tile.collider.startY + tile.collider.lengthY
            val minTileX = min(tile.collider.startX, endX)
            val minTileY = min(tile.collider.startY, endY)
            val maxTileX = max(tile.collider.startX, endX)
            val maxTileY = max(tile.collider.startY, endY)
            if (safeMinX <= maxTileX && safeMinY <= maxTileY && safeMaxX >= minTileX && safeMaxY >= minTileY) {
                if (Geometry.distanceBetweenPointAndLineSegment(
                                entity.wipPosition.x, entity.wipPosition.y, tile.collider, tileIntersection
                        ) < safeRadius) {
                    interestingTiles.add(tile)
                }
            }
        }
        queryTiles.clear()

        entityClustering.query(entity, interestingEntities)
    }

    fun determineTileIntersections() {
        for (tile in interestingTiles) {
            if (Geometry.sweepCircleToLineSegment(
                            entity.wipPosition.x, entity.wipPosition.y, deltaX, deltaY, entity.properties.radius,
                            tile.collider, entityIntersection, tileIntersection
                    )) {
                val dx = entityIntersection.x - entity.wipPosition.x
                val dy = entityIntersection.y - entity.wipPosition.y

                val intersection = intersections.add()
                intersection.myX = entityIntersection.x
                intersection.myY = entityIntersection.y
                intersection.otherX = tileIntersection.x
                intersection.otherY = tileIntersection.y
                intersection.radius = entity.properties.radius
                intersection.delta = sqrt(dx * dx + dy * dy)
                intersection.bounce = tile.properties.bounceFactor
                intersection.friction = tile.properties.frictionFactor
                intersection.otherVelocity = null
                intersection.otherID = tile.id

                intersection.validate()
            }
        }
    }

    fun determineEntityIntersections() {
        for (other in interestingEntities) {
            if (Geometry.sweepCircleToCircle(
                            entity.wipPosition.x, entity.wipPosition.y, entity.properties.radius, deltaX, deltaY,
                            other.wipPosition.x, other.wipPosition.y, other.properties.radius, entityIntersection
                    )) {
                val dx = entityIntersection.x - entity.wipPosition.x
                val dy = entityIntersection.y - entity.wipPosition.y

                val intersection = intersections.add()
                intersection.myX = entityIntersection.x
                intersection.myY = entityIntersection.y
                intersection.otherX = other.wipPosition.x
                intersection.otherY = other.wipPosition.y
                intersection.radius = entity.properties.radius + other.properties.radius
                intersection.delta = sqrt(dx * dx + dy * dy)
                intersection.bounce = other.properties.bounceFactor
                intersection.friction = other.properties.frictionFactor
                intersection.otherVelocity = other.wipVelocity
                intersection.otherRadius = other.properties.radius
                intersection.otherID = other.id

                intersection.validate()
            }
        }
    }

    fun moveSafely() {
        if (intersections.size > 0) {
            var firstIntersection = intersections[0]
            for (index in 1 until intersections.size) {
                val otherIntersection = intersections[index]
                if (otherIntersection.delta < firstIntersection.delta) firstIntersection = otherIntersection
            }

            deltaX = firstIntersection.myX - entity.wipPosition.x
            deltaY = firstIntersection.myY - entity.wipPosition.y
        }

        run {
            val vx = entity.velocity.x * stepDuration
            val vy = entity.velocity.y * stepDuration
            val dx = entity.wipPosition.x + deltaX - entity.position.x
            val dy = entity.wipPosition.y + deltaY - entity.position.y
            if (sqrt(dx * dx + dy * dy) > sqrt(vx * vx + vy * vy) + 0.1 * entity.properties.radius) {
                return
            }
        }

        for (other in interestingEntities) {
            val dx = entity.wipPosition.x + deltaX - other.wipPosition.x
            val dy = entity.wipPosition.y + deltaY - other.wipPosition.y
            if (sqrt(dx * dx + dy * dy) <= entity.properties.radius + other.properties.radius) {
                return
            }
        }

        for (tile in interestingTiles) {
            if (Geometry.distanceBetweenPointAndLineSegment(
                            entity.wipPosition.x + deltaX, entity.wipPosition.y + deltaY, tile.collider, tileIntersection
                    ) <= entity.properties.radius) {
                return
            }
        }

        entity.wipPosition.x += deltaX
        entity.wipPosition.y += deltaY
    }

    fun processIntersections() {
        if (intersections.size == 0) return

        var firstIntersection = intersections[0]
        for (index in 1 until intersections.size) {
            val otherIntersection = intersections[index]
            if (otherIntersection.delta < firstIntersection.delta) firstIntersection = otherIntersection
        }

        for (index in 0 until intersections.size) {
            val intersection = intersections[index]
            if (intersection.delta > firstIntersection.delta + 1.mm) continue
            if (processedIntersections.contains(intersection.otherID)) continue
            processIntersection(intersection)
        }
    }

    private fun processIntersection(intersection: Intersection) {
        val normalX = (intersection.myX - intersection.otherX) / intersection.radius
        val normalY = (intersection.myY - intersection.otherY) / intersection.radius

        if (normalX * normalX + normalY * normalY > 1.1) {
            throw RuntimeException("No no")
        }

        val bounceConstant = max(FixDisplacement.from(1.01),
                entity.properties.bounceFactor + intersection.bounce + 1)
        val frictionConstant = 0.02 * entity.properties.frictionFactor * intersection.friction

        val opposingFactor = bounceConstant * (normalX * entity.wipVelocity.x + normalY * entity.wipVelocity.y)
        val frictionFactor = frictionConstant * (normalY * entity.wipVelocity.x - normalX * entity.wipVelocity.y)

        val impulseX = opposingFactor * normalX + frictionFactor * normalY
        if (opposingFactor > 1.kmps) {
            println("uh ooh: ${normalX * normalX + normalY * normalY}")
        }
        val impulseY = opposingFactor * normalY - frictionFactor * normalX

        entity.wipVelocity.x -= impulseX
        entity.wipVelocity.y -= impulseY

        val otherVelocity = intersection.otherVelocity
        if (otherVelocity != null) {
            val massFactor = (entity.properties.radius / intersection.otherRadius) * (entity.properties.radius / intersection.otherRadius)
            otherVelocity.x += massFactor * impulseX
            otherVelocity.y += massFactor * impulseY
        }

        processedIntersections.add(intersection.otherID)
    }

    fun retry() {
        intersections.clear()

        val finalDelta = sqrt(deltaX * deltaX + deltaY * deltaY)
        remainingBudget -= finalDelta / originalDelta

        deltaX = remainingBudget * entity.wipVelocity.x * stepDuration
        deltaY = remainingBudget * entity.wipVelocity.y * stepDuration

        determineTileIntersections()
        determineEntityIntersections()

        moveSafely()
    }

    fun finish() {
        interestingTiles.clear()
        interestingEntities.clear()

        entity.wipVelocity.y -= 9.8.mps * stepDuration.toDouble(DurationUnit.SECONDS)
    }
}
