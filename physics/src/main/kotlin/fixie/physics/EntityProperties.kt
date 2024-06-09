package fixie.physics

import fixie.Displacement
import fixie.geometry.Position

class EntityProperties(
    val radius: Displacement,
    val bounceFactor: Float = 0f,
    val frictionFactor: Float = 1f,
    val updateFunction: ((Position, Velocity) -> Unit)? = null
)
