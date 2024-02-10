package fixie.physics

import fixie.*
import fixie.geometry.Geometry
import fixie.geometry.LineSegment
import fixie.geometry.Position
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

class Scene {

    private val stepDuration = 10.milliseconds
    private var remainingTime = 0.milliseconds

    private var updateThread: Thread? = null

    private val tiles = mutableListOf<Tile>()
    private val entities = mutableListOf<Entity>()

    private val entitiesToSpawn = ConcurrentLinkedQueue<EntitySpawnRequest>()
    private val tilesToPlace = ConcurrentLinkedQueue<TilePlaceRequest>()

    private var entityPositions = Array(10) { Position.origin() }
    private var entityVelocities = Array(10) { Velocity.zero() }

    private fun copyStateBeforeUpdate() {
        synchronized(this) {
            if (updateThread != null) throw IllegalStateException("Already updating")

            if (entities.size > entityPositions.size) {
                entityPositions = Array(entities.size) { Position.origin() }
                entityVelocities = Array(entities.size) { Velocity.zero() }
            }
            var index = 0
            for (entity in entities) {
                entityPositions[index].x = entity.position.x
                entityPositions[index].y = entity.position.y
                entityVelocities[index].x = entity.velocity.x
                entityVelocities[index].y = entity.velocity.y
                index += 1
            }
        }
    }

    private val spawnIntersection = Position.origin()

    private fun canSpawn(x: Displacement, y: Displacement, properties: EntityProperties): Boolean {
        for (tile in tiles) {
            if (Geometry.distanceBetweenPointAndLineSegment(
                    x, y, tile.collider, spawnIntersection
            ) <= properties.radius) return false
        }

        var index = 0
        for (entity in entities) {
            val dx = x - entityPositions[index].x
            val dy = y - entityPositions[index].y
            val combinedRadius = properties.radius + entity.properties.radius
            if (dx * dx + dy * dy <= combinedRadius * combinedRadius) return false
            index += 1
        }

        for (entity in confirmedEntities) {
            val dx = x - entity.x
            val dy = y - entity.y
            val combinedRadius = properties.radius + entity.properties.radius
            if (dx * dx + dy * dy <= combinedRadius * combinedRadius) return false
        }

        return true
    }

    private val confirmedEntities = mutableListOf<EntitySpawnRequest>()

    private fun processEntitySpawnRequests() {
        do {
            val request = entitiesToSpawn.poll()
            if (request != null) {
                if (canSpawn(request.x, request.y, request.properties)) confirmedEntities.add(request)
                else request.processed = true
            }
        } while (request != null)
    }

    private fun canPlace(collider: LineSegment): Boolean {
        var index = 0
        for (entity in entities) {
            if (Geometry.distanceBetweenPointAndLineSegment(
                entityPositions[index].x, entityPositions[index].y, collider, tileIntersection
            ) <= entity.properties.radius) return false
            index += 1
        }

        for (entity in confirmedEntities) {
            if (Geometry.distanceBetweenPointAndLineSegment(
                entity.x, entity.y, collider, tileIntersection
            ) <= entity.properties.radius) return false
        }

        return true
    }

    private val confirmedTiles = mutableListOf<TilePlaceRequest>()

    private fun processTilePlaceRequests() {
        do {
            val request = tilesToPlace.poll()
            if (request != null) {
                if (canPlace(request.collider)) confirmedTiles.add(request)
                else request.processed = true
            }
        } while (request != null)
    }

    private fun copyStateAfterUpdate() {
        processEntitySpawnRequests()
        processTilePlaceRequests()

        synchronized(this) {
            var index = 0
            for (entity in entities) {
                entity.position.x = entityPositions[index].x
                entity.position.y = entityPositions[index].y
                entity.velocity.x = entityVelocities[index].x
                entity.velocity.y = entityVelocities[index].y
                index += 1
            }

            for (request in confirmedEntities) {
                val id = UUID.randomUUID()
                entities.add(Entity(
                    id = id,
                    properties = request.properties,
                    position = Position(request.x, request.y),
                    velocity = Velocity(request.velocityX, request.velocityY)
                ))
                request.id = id
                request.processed = true
            }
            confirmedEntities.clear()

            for (request in confirmedTiles) {
                val id = UUID.randomUUID()
                tiles.add(Tile(
                    id = id,
                    collider = request.collider,
                    properties = request.properties
                ))
                request.id = id
                request.processed = true
            }
            confirmedTiles.clear()

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
    }

    private val intersections = Array(5) { Intersection() }

    private fun updateEntity(entity: Entity, position: Position, velocity: Velocity) {
        entity.properties.updateFunction?.invoke(position, velocity)

        var deltaX = velocity.x * stepDuration
        var deltaY = velocity.y * stepDuration
        val originalDelta = sqrt(deltaX * deltaX + deltaY * deltaY)
        // TODO Acceleration?
        velocity.y -= 9.8.mps * stepDuration.toDouble(DurationUnit.SECONDS)

        var numIntersections = 0
        for (tile in tiles) {
            if (Geometry.sweepCircleToLineSegment(
                position.x, position.y, deltaX, deltaY, entity.properties.radius,
                tile.collider, entityIntersection, tileIntersection
            )) {
                deltaX = entityIntersection.x - position.x
                deltaY = entityIntersection.y - position.y

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

                numIntersections += 1
            }
        }

        var otherEntityIndex = 0
        for (other in entities) {
            val otherPosition = entityPositions[otherEntityIndex]
            if (other != entity && Geometry.sweepCircleToCircle(
                position.x, position.y, entity.properties.radius, deltaX, deltaY,
                otherPosition.x, otherPosition.y, other.properties.radius, entityIntersection
            )) {
                deltaX = entityIntersection.x - position.x
                deltaY = entityIntersection.y - position.y

                val intersection = intersections[numIntersections % intersections.size]
                intersection.myX = entityIntersection.x
                intersection.myY = entityIntersection.y
                intersection.otherX = otherPosition.x
                intersection.otherY = otherPosition.y
                intersection.radius = entity.properties.radius + other.properties.radius
                intersection.delta = sqrt(deltaX * deltaX + deltaY * deltaY)
                intersection.bounce = other.properties.bounceFactor
                intersection.friction = other.properties.frictionFactor
                intersection.otherVelocity = entityVelocities[otherEntityIndex]
                intersection.otherRadius = other.properties.radius

                numIntersections += 1
            }
            otherEntityIndex += 1
        }

        moveSafely(entity, position, deltaX, deltaY)

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

            val bounceConstant = max(FixDisplacement.from(1.1),
                entity.properties.bounceFactor + intersection.bounce + 1)
            val frictionConstant = 0.02 * entity.properties.frictionFactor * intersection.friction

            val opposingFactor = bounceConstant * (normalX * velocity.x + normalY * velocity.y)
            val frictionFactor = frictionConstant * (normalY * velocity.x - normalX * velocity.y)

            val impulseX = opposingFactor * normalX + frictionFactor * normalY
            val impulseY = opposingFactor * normalY - frictionFactor * normalX

            velocity.x -= impulseX
            velocity.y -= impulseY

            val otherVelocity = intersection.otherVelocity
            if (otherVelocity != null) {
                val massFactor = (entity.properties.radius / intersection.otherRadius) * (entity.properties.radius / intersection.otherRadius)
                otherVelocity.x += massFactor * impulseX
                otherVelocity.y += massFactor * impulseY
            }
        }

        if (numIntersections > 0) {
            val finalDelta = sqrt(deltaX * deltaX + deltaY * deltaY)
            val remainingBudget = 1 - finalDelta / originalDelta
            deltaX = remainingBudget * velocity.x * stepDuration
            deltaY = remainingBudget * velocity.y * stepDuration

            for (tile in tiles) {
                if (Geometry.sweepCircleToLineSegment(
                        position.x, position.y, deltaX, deltaY, entity.properties.radius,
                        tile.collider, entityIntersection, tileIntersection
                    )) {
                    deltaX = entityIntersection.x - position.x
                    deltaY = entityIntersection.y - position.y
                }
            }

            otherEntityIndex = 0
            for (other in entities) {
                if (other != entity && Geometry.sweepCircleToCircle(
                        position.x, position.y, entity.properties.radius, deltaX, deltaY,
                        entityPositions[otherEntityIndex].x, entityPositions[otherEntityIndex].y,
                        other.properties.radius, entityIntersection
                    )) {
                    deltaX = entityIntersection.x - position.x
                    deltaY = entityIntersection.y - position.y
                }
                otherEntityIndex += 1
            }

            moveSafely(entity, position, deltaX, deltaY)
        }
    }

    private fun moveSafely(entity: Entity, position: Position, deltaX: Displacement, deltaY: Displacement) {
        var otherEntityIndex = 0
        for (other in entities) {
            if (other != entity) {
                val dx = position.x + deltaX - entityPositions[otherEntityIndex].x
                val dy = position.y + deltaY - entityPositions[otherEntityIndex].y
                if (sqrt(dx * dx + dy * dy) <= entity.properties.radius + other.properties.radius) {
                    println("intervene entity")
                    return
                }
            }
            otherEntityIndex += 1
        }

        for (tile in tiles) {
            if (Geometry.distanceBetweenPointAndLineSegment(
                position.x + deltaX, position.y + deltaY, tile.collider, tileIntersection
            ) <= entity.properties.radius) {
                println("intervene tile")
                return
            }
        }

        position.x += deltaX
        position.y += deltaY
    }

    private fun updateEntities() {
        var index = 0
        for (entity in entities) {
            updateEntity(entity, entityPositions[index], entityVelocities[index])
            index += 1
        }
    }

    fun update(duration: Duration) {
        copyStateBeforeUpdate()
        remainingTime += duration

        while (remainingTime >= stepDuration) {
            updateEntities()
            remainingTime -= stepDuration
        }

        copyStateAfterUpdate()
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
