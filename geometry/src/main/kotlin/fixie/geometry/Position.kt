package fixie.geometry

import fixie.*

class Position(
    var x: Displacement,
    var y: Displacement
) {
    override fun toString() = "Pos($x, $y)"

    companion object {

        fun origin() = Position(0.m, 0.m)
    }
}
