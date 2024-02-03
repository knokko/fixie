package fixie.physics

import fixie.*
import fixie.geometry.Geometry
import fixie.geometry.LineSegment
import fixie.geometry.Position
import java.util.*
import kotlin.ConcurrentModificationException

class Scene {

    // Duration of 1 physics step/tick, in milliseconds
    // TODO Maybe use fixed-point duration for this?
    private val stepDuration = 10L
    private var remainingTime = 0L

    private var updateThread: Thread? = null

    private val tiles = mutableListOf<Tile>()
    private val entities = mutableListOf<Entity>()

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

    private fun copyStateAfterUpdate() {
        synchronized(this) {
            var index = 0
            for (entity in entities) {
                entity.position.x = entityPositions[index].x
                entity.position.y = entityPositions[index].y
                entity.velocity.x = entityVelocities[index].x
                entity.velocity.y = entityVelocities[index].y
                index += 1
            }
            updateThread = null
        }
    }

    private val entityIntersection = Position.origin()
    private val tileIntersection = Position.origin()
    private val lastTileIntersection = Position.origin()

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
                lastTileIntersection.x = tileIntersection.x
                lastTileIntersection.y = tileIntersection.y
                intersectedTile = tile
            }
        }

        position.x += deltaX
        position.y += deltaY

        if (intersectedTile != null) {
            val normalX = (position.x - lastTileIntersection.x) / entity.properties.radius
            val normalY = (position.y - lastTileIntersection.y) / entity.properties.radius

            val bounceConstant = max(FixDisplacement.from(1.1),
                entity.properties.bounceFactor + intersectedTile.properties.bounceFactor + 1)
            val frictionConstant = 0.02 * entity.properties.frictionFactor * intersectedTile.properties.frictionFactor

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

    fun spawnEntity(properties: EntityProperties, x: Displacement, y: Displacement, vx: Displacement, vy: Displacement): UUID {
        if (updateThread != null && Thread.currentThread() != updateThread) throw ConcurrentModificationException()

        // TODO Postpone this to next update?
        val id = UUID.randomUUID()
        entities.add(Entity(id, properties, Position(x, y), Velocity(vx, vy)))
        return id
    }

    fun addTile(collider: LineSegment, properties: TileProperties) {
        if (updateThread != null && Thread.currentThread() != updateThread) throw ConcurrentModificationException()

        // TODO Forbid entity intersections?

        tiles.add(Tile(UUID.randomUUID(), collider, properties))
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
