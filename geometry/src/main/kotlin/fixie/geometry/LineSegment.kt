package fixie.geometry

import fixie.*

/**
 * Represents a line segment from (startX, startY) to (startX + lengthX, startY + lengthY)
 */
class LineSegment(
    val startX: Displacement,
    val startY: Displacement,
    val lengthX: Displacement,
    val lengthY: Displacement
) {

    val minX: Displacement
        get() = min(startX, startX + lengthX)
    val minY: Displacement
        get() = min(startY, startY + lengthY)
    val maxX: Displacement
        get() = max(startX, startX + lengthX)
    val maxY: Displacement
        get() = max(startY, startY + lengthY)

    override fun toString() = "LineSegment($startX, $startY, $lengthX, $lengthY)"

    fun overlapsBounds(minX: Displacement, minY: Displacement, maxX: Displacement, maxY: Displacement): Boolean {
        return this.minX <= maxX && this.minY <= maxY && this.maxX >= minX && this.maxY >= minY
    }
}
