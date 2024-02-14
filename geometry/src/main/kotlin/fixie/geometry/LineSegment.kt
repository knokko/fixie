package fixie.geometry

import fixie.Displacement

/**
 * Represents a line segment from (startX, startY) to (startX + lengthX, startY + lengthY)
 */
class LineSegment(
    val startX: Displacement,
    val startY: Displacement,
    val lengthX: Displacement,
    val lengthY: Displacement
) {
    override fun toString() = "LineSegment($startX, $startY, $lengthX, $lengthY)"
}
