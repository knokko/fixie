package fixie.physics

import fixie.*
import fixie.geometry.Geometry
import fixie.geometry.LineSegment
import fixie.geometry.Position
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class Scene {

    // Duration of 1 physics step/tick, in milliseconds
    // TODO Maybe use fixed-point duration for this?
    private val stepDuration = 10L
    private var remainingTime = 0L

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

    private fun canSpawn(x: Displacement, y: Displacement, properties: EntityProperties): Boolean {
        for (tile in tiles) {
            if (Geometry.distanceBetweenPointAndLineSegment(
                    x, y, tile.collider, lastIntersection
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

            updateThread = null
        }
    }

    private val entityIntersection = Position.origin()
    private val tileIntersection = Position.origin()
    private val lastIntersection = Position.origin()

    private fun updateEntity(entity: Entity, position: Position, velocity: Velocity) {
        entity.properties.updateFunction?.invoke(position, velocity)
        // TODO Anti-stuck policy?

        var deltaX = velocity.x * stepDuration / 1000
        var deltaY = velocity.y * stepDuration / 1000
        val originalDelta = sqrt(deltaX * deltaX + deltaY * deltaY)
        velocity.y -= (9.8 * stepDuration).mm

        var intersectedTile: Tile? = null
        for (tile in tiles) {
            if (Geometry.sweepCircleToLineSegment(
                position.x, position.y, deltaX, deltaY, entity.properties.radius,
                tile.collider.startX, tile.collider.startY, tile.collider.lengthX, tile.collider.lengthY,
                entityIntersection, tileIntersection
            )) {
                deltaX = entityIntersection.x - position.x
                deltaY = entityIntersection.y - position.y
                lastIntersection.x = tileIntersection.x
                lastIntersection.y = tileIntersection.y
                intersectedTile = tile
            }
        }

        var intersectedEntity: Entity? = null
        var index = 0
        for (other in entities) {
            if (other != entity && Geometry.sweepCircleToCircle(
                position.x, position.y, entity.properties.radius, deltaX, deltaY,
                entityPositions[index].x, entityPositions[index].y, other.properties.radius, entityIntersection
            )) {
                intersectedTile = null
                deltaX = entityIntersection.x - position.x
                deltaY = entityIntersection.y - position.y
                lastIntersection.x = entityPositions[index].x
                lastIntersection.y = entityPositions[index].y
                intersectedEntity = other
            }
            index += 1
        }

        position.x += deltaX
        position.y += deltaY

        if (intersectedTile != null || intersectedEntity != null) {
            var effectiveRadius = entity.properties.radius
            if (intersectedEntity != null) effectiveRadius += intersectedEntity.properties.radius
            val normalX = (position.x - lastIntersection.x) / effectiveRadius
            val normalY = (position.y - lastIntersection.y) / effectiveRadius

            val otherBounce: FixDisplacement
            val otherFriction: FixDisplacement
            if (intersectedTile != null) {
                otherBounce = intersectedTile.properties.bounceFactor
                otherFriction = intersectedTile.properties.frictionFactor
            } else {
                otherBounce = intersectedEntity!!.properties.bounceFactor
                otherFriction = intersectedEntity.properties.frictionFactor
            }
            val bounceConstant = max(FixDisplacement.from(1.1),
                entity.properties.bounceFactor + otherBounce + 1)
            val frictionConstant = 0.02 * entity.properties.frictionFactor * otherFriction

            val opposingFactor = bounceConstant * (normalX * velocity.x + normalY * velocity.y)
            val frictionFactor = frictionConstant * (normalY * velocity.x - normalX * velocity.y)

            velocity.x -= opposingFactor * normalX + frictionFactor * normalY
            velocity.y -= opposingFactor * normalY - frictionFactor * normalX

            val finalDelta = sqrt(deltaX * deltaX + deltaY * deltaY)
            val remainingBudget = 1 - finalDelta / originalDelta
            deltaX = remainingBudget * velocity.x * stepDuration / 1000
            deltaY = remainingBudget * velocity.y * stepDuration / 1000

            for (tile in tiles) {
                if (Geometry.sweepCircleToLineSegment(
                        position.x, position.y, deltaX, deltaY, entity.properties.radius,
                        tile.collider.startX, tile.collider.startY, tile.collider.lengthX, tile.collider.lengthY,
                        entityIntersection, tileIntersection
                    )) {
                    deltaX = entityIntersection.x - position.x
                    deltaY = entityIntersection.y - position.y
                }
            }

            index = 0
            for (other in entities) {
                if (other != entity && Geometry.sweepCircleToCircle(
                        position.x, position.y, entity.properties.radius, deltaX, deltaY,
                        entityPositions[index].x, entityPositions[index].y, other.properties.radius, entityIntersection
                    )) {
                    deltaX = entityIntersection.x - position.x
                    deltaY = entityIntersection.y - position.y
                }
                index += 1
            }

            position.x += deltaX
            position.y += deltaY
        }
    }

    private fun updateEntities() {
        var index = 0
        for (entity in entities) {
            updateEntity(entity, entityPositions[index], entityVelocities[index])
            index += 1
        }
    }

    fun update(millis: Long) {
        copyStateBeforeUpdate()
        remainingTime += millis

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
