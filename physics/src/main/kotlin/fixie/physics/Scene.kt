package fixie.physics

import fixie.*
import fixie.geometry.Geometry
import fixie.geometry.LineSegment
import fixie.geometry.Position
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

private val LIMIT = 10.km

class Scene {

    private val stepDuration = 10.milliseconds
    private var remainingTime = 0.milliseconds

    private var updateThread: Thread? = null

    private val tiles = mutableListOf<Tile>()
    private val tileTree = TileTree(
            minX = -LIMIT,
            minY = -LIMIT,
            maxX = LIMIT,
            maxY = LIMIT
    )
    private val entities = mutableListOf<Entity>()
    private val entityClustering = EntityClustering()

    private val entitiesToSpawn = ConcurrentLinkedQueue<EntitySpawnRequest>()
    private val tilesToPlace = ConcurrentLinkedQueue<TilePlaceRequest>()

    private fun copyStateBeforeUpdate() {
        synchronized(this) {
            if (updateThread != null) throw IllegalStateException("Already updating")

            var index = 0
            for (entity in entities) {
                entity.wipPosition.x = entity.position.x
                entity.wipPosition.y = entity.position.y
                entity.wipVelocity.x = entity.velocity.x
                entity.wipVelocity.y = entity.velocity.y
                index += 1
            }
        }
    }

    private val spawnIntersection = Position.origin()

    private fun canSpawn(x: Displacement, y: Displacement, properties: EntityProperties): Boolean {
        val safeRadius = 2 * properties.radius
        queryTiles.clear()
        tileTree.query(x - safeRadius, y - safeRadius, x + safeRadius, y + safeRadius, queryTiles)
        for (tile in queryTiles) {
            if (Geometry.distanceBetweenPointAndLineSegment(
                    x, y, tile.collider, spawnIntersection
            ) <= properties.radius) return false
        }
        queryTiles.clear()

        for (entity in entities) {
            val dx = x - entity.position.x
            val dy = y - entity.position.y
            val combinedRadius = properties.radius + entity.properties.radius
            if (dx * dx + dy * dy <= combinedRadius * combinedRadius) return false
        }

        return true
    }

    private fun processEntitySpawnRequests() {
        do {
            val request = entitiesToSpawn.poll()
            if (request != null) {
                if (canSpawn(request.x, request.y, request.properties)) {
                    val entity = Entity(
                            properties = request.properties,
                            position = Position(request.x, request.y),
                            velocity = Velocity(request.velocityX, request.velocityY)
                    )
                    entities.add(entity)
                    request.id = entity.id
                }
                request.processed = true
            }
        } while (request != null)
    }

    private fun canPlace(collider: LineSegment): Boolean {
        var index = 0
        for (entity in entities) {
            if (Geometry.distanceBetweenPointAndLineSegment(
                entity.wipPosition.x, entity.wipPosition.y, collider, tileIntersection
            ) <= entity.properties.radius) return false
            index += 1
        }

        return true
    }

    private fun processTilePlaceRequests() {
        do {
            val request = tilesToPlace.poll()
            if (request != null) {
                if (canPlace(request.collider)) {
                    val tile = Tile(
                            collider = request.collider,
                            properties = request.properties
                    )
                    tileTree.insert(tile)
                    tiles.add(tile)
                    request.id = tile.id
                }
                request.processed = true
            }
        } while (request != null)
    }

    private fun processRequests() {
        synchronized(this) {
            processEntitySpawnRequests()
            processTilePlaceRequests()
        }
    }

    private fun copyStateAfterUpdate() {
        synchronized(this) {
            var index = 0
            for (entity in entities) {
                entity.position.x = entity.wipPosition.x
                entity.position.y = entity.wipPosition.y
                entity.velocity.x = entity.wipVelocity.x
                entity.velocity.y = entity.wipVelocity.y
                index += 1
            }

            if (entities.removeIf { abs(it.position.x) > LIMIT || abs(it.position.y) > LIMIT }) {
                println("destroyed an entity")
            }

            updateThread = null
        }
    }

    private val entityIntersection = Position.origin()
    private val tileIntersection = Position.origin()

    private class Intersection {
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

        fun validate() {
            val dx = otherX - myX
            val dy = otherY - myY
            if (dx * dx + dy * dy > radius * (radius * 1.1)) {
                println("invalid intersection at ($myX, $myY) and ($otherX, $otherY)")
                throw RuntimeException("distance between points is ($dx, $dy): ${sqrt(dx * dx + dy * dy)} but radius is $radius")
            }
        }
    }

    private val intersections = Array(5) { Intersection() }
    private val interestingTiles = mutableListOf<Tile>()
    private val queryTiles = mutableListOf<Tile>()
    private val interestingEntities = mutableListOf<Entity>()

    private fun updateEntity(entity: Entity) {
        var deltaX = entity.wipVelocity.x * stepDuration
        var deltaY = entity.wipVelocity.y * stepDuration
        val originalDelta = sqrt(deltaX * deltaX + deltaY * deltaY)

        val safeRadius = 2 * entity.properties.radius + 2 * originalDelta
        val safeMinX = entity.wipPosition.x - safeRadius
        val safeMinY = entity.wipPosition.y - safeRadius
        val safeMaxX = entity.wipPosition.x + safeRadius
        val safeMaxY = entity.wipPosition.y + safeRadius
        queryTiles.clear()
        interestingTiles.clear()
        tileTree.query(safeMinX, safeMinY, safeMaxX, safeMaxY, queryTiles)
        for (tile in queryTiles) {
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

        var numIntersections = 0
        for (tile in interestingTiles) {
            if (Geometry.sweepCircleToLineSegment(
                entity.wipPosition.x, entity.wipPosition.y, deltaX, deltaY, entity.properties.radius,
                tile.collider, entityIntersection, tileIntersection
            )) {
                deltaX = entityIntersection.x - entity.wipPosition.x
                deltaY = entityIntersection.y - entity.wipPosition.y

                val intersection = intersections[numIntersections % intersections.size]
                intersection.myX = entityIntersection.x
                intersection.myY = entityIntersection.y
                intersection.otherX = tileIntersection.x
                intersection.otherY = tileIntersection.y
                intersection.radius = entity.properties.radius
                intersection.delta = sqrt(deltaX * deltaX + deltaY * deltaY)
                intersection.bounce = tile.properties.bounceFactor
                intersection.friction = tile.properties.frictionFactor
                intersection.otherVelocity = null

                intersection.validate()

                numIntersections += 1
            }
        }

        for (other in interestingEntities) {
            if (Geometry.sweepCircleToCircle(
                entity.wipPosition.x, entity.wipPosition.y, entity.properties.radius, deltaX, deltaY,
                other.wipPosition.x, other.wipPosition.y, other.properties.radius, entityIntersection
            )) {
                deltaX = entityIntersection.x - entity.wipPosition.x
                deltaY = entityIntersection.y - entity.wipPosition.y

                val intersection = intersections[numIntersections % intersections.size]
                intersection.myX = entityIntersection.x
                intersection.myY = entityIntersection.y
                intersection.otherX = other.wipPosition.x
                intersection.otherY = other.wipPosition.y
                intersection.radius = entity.properties.radius + other.properties.radius
                intersection.delta = sqrt(deltaX * deltaX + deltaY * deltaY)
                intersection.bounce = other.properties.bounceFactor
                intersection.friction = other.properties.frictionFactor
                intersection.otherVelocity = other.wipVelocity
                intersection.otherRadius = other.properties.radius

                intersection.validate()

                numIntersections += 1
            }
        }

        moveSafely(entity, deltaX, deltaY)

        if (numIntersections > 5) numIntersections = 5

        var smallestIntersectionDelta = -1.m
        for (index in 0 until numIntersections) {
            val delta = intersections[index].delta
            if (smallestIntersectionDelta == -1.m || delta < smallestIntersectionDelta) smallestIntersectionDelta = delta
        }

        for (index in 0 until numIntersections) {
            val intersection = intersections[index]
            if (intersection.delta < smallestIntersectionDelta - 1.mm) continue

            val normalX = (intersection.myX - intersection.otherX) / intersection.radius
            val normalY = (intersection.myY - intersection.otherY) / intersection.radius

            if (normalX * normalX + normalY * normalY > 1.1) {
                throw RuntimeException("No no")
            }

            val bounceConstant = max(FixDisplacement.from(1.1),
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
        }

        if (numIntersections > 0 && originalDelta > 0.1.mm) {
            val finalDelta = sqrt(deltaX * deltaX + deltaY * deltaY)
            val remainingBudget = 1 - finalDelta / originalDelta
            deltaX = remainingBudget * entity.wipVelocity.x * stepDuration
            deltaY = remainingBudget * entity.wipVelocity.y * stepDuration

            for (tile in interestingTiles) {
                if (Geometry.sweepCircleToLineSegment(
                        entity.wipPosition.x, entity.wipPosition.y, deltaX, deltaY, entity.properties.radius,
                        tile.collider, entityIntersection, tileIntersection
                    )) {
                    deltaX = entityIntersection.x - entity.wipPosition.x
                    deltaY = entityIntersection.y - entity.wipPosition.y
                }
            }

            for (other in interestingEntities) {
                if (Geometry.sweepCircleToCircle(
                        entity.wipPosition.x, entity.wipPosition.y, entity.properties.radius, deltaX, deltaY,
                        other.wipPosition.x, other.wipPosition.y,
                        other.properties.radius, entityIntersection
                    )) {
                    deltaX = entityIntersection.x - entity.wipPosition.x
                    deltaY = entityIntersection.y - entity.wipPosition.y
                }
            }

            moveSafely(entity, deltaX, deltaY)
        }

        interestingTiles.clear()
        interestingEntities.clear()

        entity.wipVelocity.y -= 9.8.mps * stepDuration.toDouble(DurationUnit.SECONDS)
    }

    private fun moveSafely(entity: Entity, deltaX: Displacement, deltaY: Displacement) {
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

    private fun updateEntities() {
        for (entity in entities) {
            val vx = entity.wipVelocity.x * stepDuration
            val vy = entity.wipVelocity.y * stepDuration
            entityClustering.insert(entity, 1.1 * (entity.properties.radius + abs(vx) + abs(vy)))
        }

        for (entity in entities) {
            updateEntity(entity)
            entity.properties.updateFunction?.invoke(entity.wipPosition, entity.wipVelocity)
        }

        entityClustering.reset()
    }

    fun update(duration: Duration) {
        processRequests()
        remainingTime += duration

        while (remainingTime >= stepDuration) {
            copyStateBeforeUpdate()
            updateEntities()
            copyStateAfterUpdate()
            remainingTime -= stepDuration
        }
    }

    fun spawnEntity(request: EntitySpawnRequest) {
        entitiesToSpawn.add(request)
    }

    fun addTile(request: TilePlaceRequest) {
        tilesToPlace.add(request)
    }

    fun read(query: SceneQuery) {
        synchronized(this) {
            query.ensureCapacity(tiles.size, entities.size)
            query.numTiles = tiles.size
            query.numEntities = entities.size

            var index = 0
            for (tile in tiles) {
                query.tiles[index] = tile
                index += 1
            }

            index = 0
            for (entity in entities) {
                val qe = query.entities[index]
                qe.id = entity.id
                qe.properties = entity.properties
                qe.position.x = entity.position.x
                qe.position.y = entity.position.y
                qe.velocity.x = entity.velocity.x
                qe.velocity.y = entity.velocity.y
                index += 1
            }
        }
    }
}
