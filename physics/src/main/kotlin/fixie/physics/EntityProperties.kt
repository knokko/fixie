package fixie.physics

import fixie.Displacement
import fixie.FixDisplacement
import fixie.geometry.Position

class EntityProperties(
    val radius: Displacement,
    val bounceConstant: FixDisplacement,
    val frictionConstant: FixDisplacement,
    val updateFunction: ((Position, Velocity) -> Unit)? = null
)
