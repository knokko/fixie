package fixie.physics

import fixie.geometry.LineSegment
import java.util.UUID

class Tile(
    val collider: LineSegment,
    val properties: TileProperties
) {
    val id = UUID.randomUUID()
}
