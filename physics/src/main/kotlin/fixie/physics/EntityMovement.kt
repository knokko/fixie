package fixie.physics

import fixie.*
import fixie.geometry.Geometry
import fixie.geometry.Position
import java.util.*
import kotlin.math.*

internal class EntityMovement(
        private val tileTree: TileTree,
        private val entityClustering: EntityClustering
) {
    val intersections = GrowingBuffer.withMutableElements(5) { Intersection() }
    private val processedIntersections = mutableListOf<UUID>()
    private val properIntersections = mutableListOf<Intersection>()

    private val interestingTiles = mutableListOf<Tile>()
    private val queryTiles = GrowingBuffer.withImmutableElements(10, DUMMY_TILE)

    private val interestingEntities = mutableListOf<Entity>()

    private val entityIntersection = Position.origin()
    private val tileIntersection = Position.origin()

    private lateinit var entity: Entity

    private var deltaX = 0.m
    private var deltaY = 0.m
    var originalDelta = 0.m
    private var remainingBudget = 1.0

    class Intersection {
        var myX = 0.m
        var myY = 0.m
        var otherX = 0.m
        var otherY = 0.m
        var radius = 0.m
        var delta = 0.m
        var bounce = 0f
        var friction = 0f
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

    private fun computeCurrentVelocityX() = entity.wipVelocity.x

    private fun computeCurrentVelocityY(vy: Speed = entity.wipVelocity.y) = vy - 4.9.mps2 * Scene.STEP_DURATION

    fun start(entity: Entity) {
        this.entity = entity
        deltaX = computeCurrentVelocityX() * Scene.STEP_DURATION
        deltaY = computeCurrentVelocityY() * Scene.STEP_DURATION
        originalDelta = sqrt(deltaX * deltaX + deltaY * deltaY)

        intersections.clear()
        properIntersections.clear()
        processedIntersections.clear()
        remainingBudget = 1.0
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

        while (true) {
            val vx = entity.velocity.x * Scene.STEP_DURATION
            val vy = entity.velocity.y * Scene.STEP_DURATION
            val dx = entity.wipPosition.x + deltaX - entity.position.x
            val dy = entity.wipPosition.y + deltaY - entity.position.y
            if (sqrt(dx * dx + dy * dy) > sqrt(vx * vx + vy * vy) + 0.1 * entity.properties.radius) {
                deltaX /= 2
                deltaY /= 2
            } else break
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

    private fun processTileOrEntityIntersections(processTiles: Boolean) {
        val vx = computeCurrentVelocityX().toDouble(SpeedUnit.METERS_PER_SECOND)
        val vy = computeCurrentVelocityY().toDouble(SpeedUnit.METERS_PER_SECOND)
        val speed = sqrt(vx * vx + vy * vy)
        val directionX = vx / speed
        val directionY = vy / speed

        var totalIntersectionFactor = 0.0
        for (intersection in properIntersections) {
            if (intersection.otherVelocity == null == processTiles) {
                totalIntersectionFactor += determineIntersectionFactor(intersection, directionX, directionY)
            }
        }

        if (totalIntersectionFactor > 0.0) {
            val oldVelocityX = computeCurrentVelocityX()
            val oldVelocityY = computeCurrentVelocityY()
            for (intersection in properIntersections) {
                if (intersection.otherVelocity == null == processTiles) {
                    processIntersection(intersection, oldVelocityX, oldVelocityY, directionX, directionY, totalIntersectionFactor)
                }
            }
        }
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
            if (intersection.delta <= firstIntersection.delta + 1.mm && !processedIntersections.contains(intersection.otherID)) {
                properIntersections.add(intersection)
            }
        }

        processTileOrEntityIntersections(true)
        processTileOrEntityIntersections(false)
    }

    private fun determineIntersectionFactor(intersection: Intersection, directionX: Double, directionY: Double): Double {
        val normalX = (intersection.myX - intersection.otherX) / intersection.radius
        val normalY = (intersection.myY - intersection.otherY) / intersection.radius

        return determineIntersectionFactor(normalX, normalY, directionX, directionY)
    }

    private fun determineIntersectionFactor(
            normalX: Double, normalY: Double, directionX: Double, directionY: Double
    ): Double {
        return max(0.0, -directionX * normalX - directionY * normalY)
    }

    private fun processIntersection(
            intersection: Intersection, oldVelocityX: Speed, oldVelocityY: Speed,
            directionX: Double, directionY: Double, totalIntersectionFactor: Double
    ) {
        processedIntersections.add(intersection.otherID)

        val normalX = (intersection.myX - intersection.otherX) / intersection.radius
        val normalY = (intersection.myY - intersection.otherY) / intersection.radius

        if (normalX * normalX + normalY * normalY > 1.1) {
            throw RuntimeException("No no")
        }

        val intersectionFactor = determineIntersectionFactor(
                normalX, normalY, directionX, directionY
        ) / totalIntersectionFactor

        val bounceConstant = entity.properties.bounceFactor + intersection.bounce + 1
        val frictionConstant = 0.01 * entity.properties.frictionFactor * intersection.friction

        var otherVelocityX = 0.mps
        var otherVelocityY = 0.mps
        val otherVelocity = intersection.otherVelocity
        if (otherVelocity != null) {
            otherVelocityX = otherVelocity.x
            otherVelocityY = computeCurrentVelocityY(otherVelocity.y)
        }

        val relativeVelocityX = oldVelocityX - otherVelocityX
        val relativeVelocityY = oldVelocityY - otherVelocityY

        val opposingImpulse = bounceConstant * (normalX * oldVelocityX + normalY * oldVelocityY)
        val frictionImpulse = frictionConstant * (normalY * oldVelocityX - normalX * oldVelocityY)

        var impulseX = intersectionFactor * (opposingImpulse * normalX + frictionImpulse * normalY)
        if (opposingImpulse > 1.kmps) {
            println("uh ooh: ${normalX * normalX + normalY * normalY}")
        }
        var impulseY = intersectionFactor * (opposingImpulse * normalY - frictionImpulse * normalX)

        if (otherVelocity != null) {
            val massFactor = (entity.properties.radius / intersection.otherRadius) * (entity.properties.radius / intersection.otherRadius)

            val thresholdFactor = 2.0
            var dimmer = 1.0

            val relativeVelocity = sqrt(relativeVelocityX.value * relativeVelocityX.value + relativeVelocityY.value * relativeVelocityY.value)
            val impulse = sqrt(impulseX.value * impulseX.value + impulseY.value * impulseY.value)
            val push = massFactor * impulse / (relativeVelocity + 0.5.mps.value / massFactor)
            if (push > thresholdFactor) dimmer = push / thresholdFactor

            impulseX /= dimmer
            impulseY /= dimmer

            otherVelocity.x += massFactor * impulseX
            otherVelocity.y += massFactor * impulseY
        }

        entity.wipVelocity.x -= impulseX
        entity.wipVelocity.y -= impulseY
    }

    fun retry() {
        updateRetryBudget()
        retryStep()

        val oldBudget = remainingBudget
        if (oldBudget > 0.5) {
            updateRetryBudget()
            if (remainingBudget > 0.4) {
                createMargin(entity, interestingEntities, interestingTiles, 0.2.mm)
            }
            retryStep()
        }
    }

    private fun updateRetryBudget() {
        val finalDelta = sqrt(deltaX * deltaX + deltaY * deltaY)
        remainingBudget -= finalDelta / originalDelta

        deltaX = remainingBudget * computeCurrentVelocityX() * Scene.STEP_DURATION
        deltaY = remainingBudget * computeCurrentVelocityY() * Scene.STEP_DURATION
    }

    private fun retryStep() {
        val totalDelta = sqrt(deltaX * deltaX + deltaY * deltaY)
        if (totalDelta < 0.1.mm) return

        intersections.clear()
        properIntersections.clear()
        determineTileIntersections()
        determineEntityIntersections()
        moveSafely()
        processIntersections()
    }

    fun finish() {
        interestingTiles.clear()
        interestingEntities.clear()

        entity.wipVelocity.y -= 9.8.mps2 * Scene.STEP_DURATION
    }
}
