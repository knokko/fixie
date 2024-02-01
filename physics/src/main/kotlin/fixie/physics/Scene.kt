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

    private val tiles = mutableListOf<LineSegment>()
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
        velocity.y -= (9.8 * stepDuration).mm

        var didIntersect = false
        for (tile in tiles) {
            if (Geometry.sweepCircleToLineSegment(
                position.x, position.y, deltaX, deltaY, entity.properties.radius,
                tile.startX, tile.startY, tile.lengthX, tile.lengthY,
                entityIntersection, tileIntersection
            )) {
                deltaX = entityIntersection.x - position.x
                deltaY = entityIntersection.y - position.y
                lastTileIntersection.x = tileIntersection.x
                lastTileIntersection.y = tileIntersection.y
                didIntersect = true
            }
        }

        position.x += deltaX
        position.y += deltaY

        if (didIntersect) {
            val normalX = (position.x - lastTileIntersection.x) / entity.properties.radius
            val normalY = (position.y - lastTileIntersection.y) / entity.properties.radius

            val opposingFactor = (normalX * velocity.x + normalY * velocity.y) * entity.properties.bounceConstant
            val frictionFactor = (normalY * velocity.x - normalX * velocity.y) * entity.properties.frictionConstant

            velocity.x -= opposingFactor * normalX + frictionFactor * normalY * 0.015
            velocity.y -= opposingFactor * normalY - frictionFactor * normalX * 0.015
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

    fun addTile(tile: LineSegment) {
        if (updateThread != null && Thread.currentThread() != updateThread) throw ConcurrentModificationException()

        // TODO Forbid entity intersections?

        tiles.add(tile)
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
