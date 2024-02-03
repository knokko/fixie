package fixie.physics

import fixie.FixDisplacement

class TileProperties(
    val bounceFactor: FixDisplacement = FixDisplacement.ZERO,
    val frictionFactor: FixDisplacement = FixDisplacement.ONE
)
