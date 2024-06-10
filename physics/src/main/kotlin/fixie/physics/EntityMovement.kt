package fixie.physics

import fixie.*
import fixie.geometry.Geometry
import fixie.geometry.Position
import java.util.*
import kotlin.math.*
import kotlin.time.Duration.Companion.seconds

internal class EntityMovement(
        private val tileTree: TileTree,
        private val entityClustering: EntityClustering
) {
    val intersections = GrowingBuffer.withMutableElements(5) { Intersection() }
    private val properIntersections = mutableListOf<Intersection>()

    private val interestingTiles = mutableListOf<Tile>()
    private val queryTiles = GrowingBuffer.withImmutableElements(10, DUMMY_TILE)

    private val interestingEntities = mutableListOf<Entity>()

    private val entityIntersection = Position.origin()
    private val tileIntersection = Position.origin()
    private val retryDestination = Position.origin()

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

    private fun computeCurrentVelocityY() = entity.wipVelocity.y - 4.9.mps2 * Scene.STEP_DURATION

    fun start(entity: Entity) {
        this.entity = entity
        deltaX = computeCurrentVelocityX() * Scene.STEP_DURATION
        deltaY = computeCurrentVelocityY() * Scene.STEP_DURATION
        originalDelta = sqrt(deltaX * deltaX + deltaY * deltaY)

        intersections.clear()
        properIntersections.clear()
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
            if (intersection.delta <= firstIntersection.delta + 1.mm) properIntersections.add(intersection)
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

        val opposingFactor = bounceConstant * (normalX * oldVelocityX + normalY * oldVelocityY)
        val frictionFactor = frictionConstant * (normalY * oldVelocityX - normalX * oldVelocityY)

        val impulseX = intersectionFactor * (opposingFactor * normalX + frictionFactor * normalY)
        if (opposingFactor > 1.kmps) {
            println("uh ooh: ${normalX * normalX + normalY * normalY}")
        }
        val impulseY = intersectionFactor * (opposingFactor * normalY - frictionFactor * normalX)

        entity.wipVelocity.x -= impulseX
        entity.wipVelocity.y -= impulseY

        val otherVelocity = intersection.otherVelocity
        if (otherVelocity != null) {
            val massFactor = (entity.properties.radius / intersection.otherRadius) * (entity.properties.radius / intersection.otherRadius)
            otherVelocity.x += massFactor * impulseX
            otherVelocity.y += massFactor * impulseY
        }
    }

    private fun retryTiles() {
        for (tile in interestingTiles) {
            if (Geometry.sweepCircleToLineSegment(
                            entity.wipPosition.x, entity.wipPosition.y,
                            retryDestination.x - entity.wipPosition.x,
                            retryDestination.y - entity.wipPosition.y, entity.properties.radius,
                            tile.collider, entityIntersection, tileIntersection
                    )) {
                retryDestination.x = entityIntersection.x
                retryDestination.y = entityIntersection.y
            }
        }
    }

    private fun retryEntities() {
        for (other in interestingEntities) {
            if (Geometry.sweepCircleToCircle(
                            entity.wipPosition.x, entity.wipPosition.y, entity.properties.radius,
                            retryDestination.x - entity.wipPosition.x, retryDestination.y - entity.wipPosition.y,
                            other.wipPosition.x, other.wipPosition.y, other.properties.radius, entityIntersection
                    )) {
                retryDestination.x = entityIntersection.x
                retryDestination.y = entityIntersection.y
            }
        }
    }

    private fun retryAngle(angle: Double, totalDelta: Displacement): Displacement {
        return retryDelta(cos(angle) * totalDelta, sin(angle) * totalDelta)
    }

    private fun retryDelta(dx: Displacement, dy: Displacement): Displacement {
        retryDestination.x = entity.wipPosition.x + dx
        retryDestination.y = entity.wipPosition.y + dy
        retryTiles()
        retryEntities()

        val distanceX = retryDestination.x - entity.wipPosition.x
        val distanceY = retryDestination.y - entity.wipPosition.y
        return sqrt(distanceX * distanceX + distanceY * distanceY)
    }

    private fun retryAngles(originalAngle: Double, totalDelta: Displacement) {
        if (retryDelta(deltaX, deltaY) > 0.m) return

        if (entity.stuckCounter > 0) return

        val angleIncrement = 0.01
        for (counter in 1 until 6) {
            if (retryAngle(originalAngle + angleIncrement * counter * counter, totalDelta) > 0.m) return
            if (retryAngle(originalAngle - angleIncrement * counter * counter, totalDelta) > 0.m) return
        }

        entity.stuckCounter = (5.seconds / Scene.STEP_DURATION).roundToLong()
    }

    fun retry() {
        val finalDelta = sqrt(deltaX * deltaX + deltaY * deltaY)
        remainingBudget -= finalDelta / originalDelta

        deltaX = remainingBudget * computeCurrentVelocityX() * Scene.STEP_DURATION
        deltaY = remainingBudget * computeCurrentVelocityY() * Scene.STEP_DURATION
        val totalDelta = sqrt(deltaX * deltaX + deltaY * deltaY)
        if (totalDelta < 0.1.mm) return

        val oldAngle = atan2(deltaY.value.toDouble(), deltaX.value.toDouble())

        retryAngles(oldAngle, totalDelta)

        deltaX = retryDestination.x - entity.wipPosition.x
        deltaY = retryDestination.y - entity.wipPosition.y

        intersections.clear()
        properIntersections.clear()

        moveSafely()
    }

    fun finish() {
        interestingTiles.clear()
        interestingEntities.clear()

        entity.wipVelocity.y -= 9.8.mps2 * Scene.STEP_DURATION
    }
}
