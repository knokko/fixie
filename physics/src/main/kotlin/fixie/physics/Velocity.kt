package fixie.physics

import fixie.*

class Velocity(
    /** Velocity in the x-direction, per second */
    var x: Displacement,
    /** Velocity in the y-direction, per second */
    var y: Displacement
) {

    override fun toString() = "V($x/s, $y/s)"

    companion object {

        fun zero() = Velocity(0.m, 0.m)
    }
}
