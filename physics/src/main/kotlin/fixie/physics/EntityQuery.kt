package fixie.physics

import fixie.geometry.Position
import java.util.*

class EntityQuery {
    lateinit var id: UUID
    val position = Position.origin()
    val velocity = Velocity.zero()
    lateinit var properties: EntityProperties
}
