package fixie.physics

import fixie.geometry.Position
import java.util.*

class Entity(
    val properties: EntityProperties,
    val position: Position,
    val velocity: Velocity
) {
    val id: UUID = UUID.randomUUID()
    
    internal val wipPosition = Position.origin()
    internal val wipVelocity = Velocity.zero()
    internal val clusteringLists = mutableListOf<MutableList<Entity>>()
    internal var isAlreadyPresent = false

    override fun equals(other: Any?) = other is Entity && other.id == this.id

    override fun hashCode() = id.hashCode()
}
