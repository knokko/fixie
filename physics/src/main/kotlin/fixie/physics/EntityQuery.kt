package fixie.physics

import fixie.geometry.Position

class EntityQuery() {
    val position = Position.origin()
    val velocity = Velocity.zero()
    lateinit var properties: EntityProperties
}
