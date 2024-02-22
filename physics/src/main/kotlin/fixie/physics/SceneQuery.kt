package fixie.physics

import fixie.geometry.LineSegment
import fixie.*

internal val DUMMY_TILE = Tile(
        collider = LineSegment(0.m, 0.m, 0.m, 0.m),
        properties = TileProperties()
)

class SceneQuery {
    val tiles = GrowingBuffer.withImmutableElements(20, DUMMY_TILE)
    val entities = GrowingBuffer.withMutableElements(10) { EntityQuery() }
}
