package fixie.physics

import fixie.Displacement
import fixie.FixDisplacement
import fixie.geometry.Position

class EntityProperties(
    val radius: Displacement,
    val bounceFactor: FixDisplacement = FixDisplacement.ZERO,
    val frictionFactor: FixDisplacement = FixDisplacement.ONE,
    val updateFunction: ((Position, Velocity) -> Unit)? = null
)
