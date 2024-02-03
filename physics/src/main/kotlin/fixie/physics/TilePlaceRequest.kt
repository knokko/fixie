package fixie.physics

import fixie.geometry.LineSegment
import java.util.*

class TilePlaceRequest(
    val collider: LineSegment,
    val properties: TileProperties
) {
    var id: UUID? = null

    @Volatile
    var processed = false
}
