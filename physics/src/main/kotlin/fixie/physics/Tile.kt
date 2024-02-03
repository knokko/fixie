package fixie.physics

import fixie.geometry.LineSegment
import java.util.UUID

class Tile(
    val id: UUID,
    val collider: LineSegment,
    val properties: TileProperties
)
