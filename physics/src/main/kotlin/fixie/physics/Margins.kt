package fixie.physics

import fixie.*
import fixie.geometry.Geometry
import fixie.geometry.Position

fun createMargin(entity: Entity, otherEntities: List<Entity>, tiles: List<Tile>, margin: Displacement) {

    val largeMargin = 2 * margin
    val largestMargin = 3 * margin
    val veryCloseEntities = otherEntities.filter {
        val currentDistance = entity.wipPosition.distance(it.wipPosition.x, it.wipPosition.y)
        val currentMargin = currentDistance - entity.properties.radius - it.properties.radius
        //println("currentMargin is $currentMargin because distance is $currentDistance and radius are ${entity.properties.radius} and ${it.properties.radius}")
        currentMargin < largestMargin
    }

    val dummyPoint = Position.origin()
    val veryCloseTiles = tiles.filter {
        val currentDistance = Geometry.distanceBetweenPointAndLineSegment(
                entity.wipPosition.x, entity.wipPosition.y, it.collider, dummyPoint
        )
        val currentMargin = currentDistance - entity.properties.radius
        currentMargin < largestMargin
    }

    //println("there are ${veryCloseEntities.size} very close entities from the ${otherEntities.size} close entities")
    //println("there are ${veryCloseTiles.size} very close tiles from the ${tiles.size} close tiles")

    var desiredMargin = margin

    for (counter in 0 until 5) {
        var newX = entity.wipPosition.x
        var newY = entity.wipPosition.y
        for (other in veryCloseEntities) {
            val dx = other.wipPosition.x - newX
            val dy = other.wipPosition.y - newY
            val currentDistance = sqrt(dx * dx + dy * dy)
            val currentMargin = currentDistance - entity.properties.radius - other.properties.radius
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
            val currentMargin = currentDistance - entity.properties.radius
            if (desiredMargin > currentMargin) {
                val pushDistance = desiredMargin - currentMargin
                newX += (newX - dummyPoint.x) / currentDistance * pushDistance
                newY += (newY - dummyPoint.y) / currentDistance * pushDistance
            }
        }

        var failed = entity.wipPosition.distance(newX, newY) > largeMargin
        for (other in veryCloseEntities) {
            val currentDistance = other.wipPosition.distance(newX, newY)
            val currentMargin = currentDistance - entity.properties.radius - other.properties.radius
            if (desiredMargin * 0.6 > currentMargin) {
                //println("entity: current margin is $currentMargin and desired margin is $desiredMargin")
                failed = true
                break
            }
        }
        for (tile in veryCloseTiles) {
            val currentDistance = Geometry.distanceBetweenPointAndLineSegment(
                    newX, newY, tile.collider, dummyPoint
            )
            val currentMargin = currentDistance - entity.properties.radius
            if (desiredMargin * 0.6 > currentMargin) {
                //println("tile : current margin is $currentMargin and desired margin is $desiredMargin")
                failed = true
                break
            }
        }

        if (!failed) {
            entity.wipPosition.x = newX
            entity.wipPosition.y = newY
            break
        }

        desiredMargin /= 2
    }
}
