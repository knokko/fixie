package fixie.physics

import fixie.*
import fixie.geometry.Geometry
import fixie.geometry.LineSegment
import fixie.geometry.Position
import fixie.physics.constraint.MaxAccelerationConstraint
import fixie.physics.constraint.NotMovingConstraint
import fixie.physics.constraint.NotStuckConstraint
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

private val LIMIT = 10.km

class Scene {

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
    private val queryTiles = GrowingBuffer.withImmutableElements(50, DUMMY_TILE)

    private fun canSpawn(x: Displacement, y: Displacement, properties: EntityProperties): Boolean {
        val safeRadius = 2 * properties.radius
        tileTree.query(x - safeRadius, y - safeRadius, x + safeRadius, y + safeRadius, queryTiles)
        for (index in 0 until queryTiles.size) {
            if (Geometry.distanceBetweenPointAndLineSegment(
                    x, y, queryTiles[index].collider, spawnIntersection
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
                    entity.constraints.add(MaxAccelerationConstraint(400.milliseconds, 5.mps))
                    entity.constraints.add(NotMovingConstraint(200.milliseconds))
                    entity.constraints.add(NotStuckConstraint(0.2.mps, 1.seconds) { entity.stuckCounter = 0 })
                    entities.add(entity)
                    request.id = entity.id
                }
                request.processed = true
            }
        } while (request != null)
    }

    private val tileIntersection = Position.origin()

    private fun canPlace(collider: LineSegment): Boolean {
        for (entity in entities) {
            if (Geometry.distanceBetweenPointAndLineSegment(
                entity.position.x, entity.position.y, collider, tileIntersection
            ) <= entity.properties.radius) return false
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
                if (entity.stuckCounter > 0) entity.stuckCounter -= 1
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

    private val movement = EntityMovement(tileTree, entityClustering)

    private fun updateEntity(entity: Entity) {
        movement.start(entity)

        movement.determineInterestingTilesAndEntities()
        movement.determineTileIntersections()
        movement.determineEntityIntersections()

        movement.moveSafely()
        movement.processIntersections()

        if (movement.intersections.size > 0 && movement.originalDelta > 0.1.mm) movement.retry()

        movement.finish()
    }

    private fun updateEntities() {
        for (entity in entities) {
            val vx = entity.wipVelocity.x * STEP_DURATION
            val vy = entity.wipVelocity.y * STEP_DURATION
            entityClustering.insert(entity, 1.1 * (entity.properties.radius + abs(vx) + abs(vy)))

            for (constraint in entity.constraints) {
                constraint.check(entity.wipPosition, entity.wipVelocity)
            }
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

        while (remainingTime >= STEP_DURATION) {
            copyStateBeforeUpdate()
            updateEntities()
            copyStateAfterUpdate()
            remainingTime -= STEP_DURATION
        }
    }

    fun spawnEntity(request: EntitySpawnRequest) {
        entitiesToSpawn.add(request)
    }

    fun addTile(request: TilePlaceRequest) {
        tilesToPlace.add(request)
    }

    fun read(query: SceneQuery, minX: Displacement, minY: Displacement, maxX: Displacement, maxY: Displacement) {
        synchronized(this) {
            query.tiles.clear()
            tileTree.query(minX, minY, maxX, maxY, query.tiles)

            query.entities.clear()
            for (entity in entities) {
                val p = entity.position
                val r = entity.properties.radius
                if (p.x + r >= minX && p.y + r >= minY && p.x - r <= maxX && p.y - r <= maxY) {
                    val qe = query.entities.add()

                    qe.id = entity.id
                    qe.properties = entity.properties
                    qe.position.x = p.x
                    qe.position.y = p.y
                    qe.velocity.x = entity.velocity.x
                    qe.velocity.y = entity.velocity.y
                }
            }
        }
    }

    companion object {
        val STEP_DURATION = 10.milliseconds
    }
}
