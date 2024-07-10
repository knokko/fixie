package fixie.physics

import fixie.*
import fixie.geometry.Geometry
import fixie.geometry.Position

fun createMargin(
        position: Position, radius: Displacement,
        otherEntities: List<Entity>, tiles: List<Tile>, margin: Displacement
): Boolean {

    val largeMargin = 2 * margin
    val largestMargin = 3 * margin
    val veryCloseEntities = otherEntities.filter {
        val currentDistance = position.distance(it.wipPosition.x, it.wipPosition.y)
        val currentMargin = currentDistance - radius - it.properties.radius
        currentMargin < largestMargin
    }

    val dummyPoint = Position.origin()
    val veryCloseTiles = tiles.filter {
        val currentDistance = Geometry.distanceBetweenPointAndLineSegment(
                position.x, position.y, it.collider, dummyPoint
        )
        val currentMargin = currentDistance - radius
        currentMargin < largestMargin
    }

    var desiredMargin = margin

    for (counter in 0 until 5) {
        var newX = position.x
        var newY = position.y
        for (other in veryCloseEntities) {
            val dx = other.wipPosition.x - newX
            val dy = other.wipPosition.y - newY
            val currentDistance = sqrt(dx * dx + dy * dy)
            val currentMargin = currentDistance - radius - other.properties.radius
            if (desiredMargin > currentMargin) {
                val pushDistance = desiredMargin - currentMargin
                newX -= dx / currentDistance * pushDistance
                newY -= dy / currentDistance * pushDistance
            }
        }
        for (tile in veryCloseTiles) {
            val currentDistance = Geometry.distanceBetweenPointAndLineSegment(
                    newX, newY, tile.collider, dummyPoint
            )
            val currentMargin = currentDistance - radius
            if (desiredMargin > currentMargin) {
                val pushDistance = desiredMargin - currentMargin
                newX += (newX - dummyPoint.x) / currentDistance * pushDistance
                newY += (newY - dummyPoint.y) / currentDistance * pushDistance
            }
        }

        if (position.x == newX && position.y == newY) return false

        var failed = position.distance(newX, newY) > largeMargin
        for (other in veryCloseEntities) {
            val currentDistance = other.wipPosition.distance(newX, newY)
            val currentMargin = currentDistance - radius - other.properties.radius
            if (desiredMargin * 0.6 > currentMargin) {
                failed = true
                break
            }
        }
        for (tile in veryCloseTiles) {
            val currentDistance = Geometry.distanceBetweenPointAndLineSegment(
                    newX, newY, tile.collider, dummyPoint
            )
            val currentMargin = currentDistance - radius
            if (desiredMargin * 0.6 > currentMargin) {
                failed = true
                break
            }
        }

        if (!failed) {
            position.x = newX
            position.y = newY
            return true
        }

        desiredMargin /= 2
    }

    return false
}
