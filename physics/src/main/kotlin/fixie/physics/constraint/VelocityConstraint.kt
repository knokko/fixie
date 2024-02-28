package fixie.physics.constraint

import fixie.geometry.Position
import fixie.physics.Velocity

internal abstract class VelocityConstraint {

    abstract fun check(position: Position, velocity: Velocity)
}
