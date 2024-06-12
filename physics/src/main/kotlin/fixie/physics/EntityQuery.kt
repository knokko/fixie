package fixie.physics

import fixie.degrees
import fixie.geometry.Position
import java.util.*

class EntityQuery {
    lateinit var id: UUID
    val position = Position.origin()
    val velocity = Velocity.zero()
    var angle = 0.degrees
    lateinit var properties: EntityProperties
}
