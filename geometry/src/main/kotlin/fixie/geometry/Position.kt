package fixie.geometry

import fixie.*

class Position(
    var x: Displacement,
    var y: Displacement
) {
    override fun toString() = "Pos($x, $y)"

    fun distance(otherX: Displacement, otherY: Displacement) = distance(this.x, this.y, otherX, otherY)

    companion object {

        fun origin() = Position(0.m, 0.m)

        fun distanceSquared(x1: Displacement, y1: Displacement, x2: Displacement, y2: Displacement): Displacement.Area {
            val dx = x2 - x1
            val dy = y2 - y1
            return dx * dx + dy * dy
        }

        fun distance(x1: Displacement, y1: Displacement, x2: Displacement, y2: Displacement) = sqrt(distanceSquared(x1, y1, x2, y2))
    }
}
