package fixie.physics

import fixie.geometry.Position
import fixie.physics.constraint.MaxAccelerationConstraint
import fixie.physics.constraint.VelocityConstraint
import java.util.*
import kotlin.time.Duration.Companion.milliseconds

class Entity(
    val properties: EntityProperties,
    val position: Position,
    val velocity: Velocity
) {
    val id: UUID = UUID.randomUUID()

    internal val constraints = mutableListOf<VelocityConstraint>()
    
    internal val wipPosition = Position.origin()
    internal val wipVelocity = Velocity.zero()
    internal val clusteringLists = mutableListOf<MutableList<Entity>>()
    internal var isAlreadyPresent = false

    override fun equals(other: Any?) = other is Entity && other.id == this.id

    override fun hashCode() = id.hashCode()
}
