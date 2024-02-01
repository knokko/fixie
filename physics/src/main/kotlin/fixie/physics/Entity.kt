package fixie.physics

import fixie.geometry.Position
import java.util.*

class Entity(
    val id: UUID,
    val properties: EntityProperties,
    val position: Position,
    val velocity: Velocity
) {
    override fun equals(other: Any?) = other is Entity && other.id == this.id

    override fun hashCode() = id.hashCode()
}
