package fixie.physics

import kotlin.math.max

class SceneQuery(initialTileCapacity: Int = 100, initialEntityCapacity: Int = 100) {

    var tiles = Array<Tile?>(initialTileCapacity) { null }
    var entities = Array(initialEntityCapacity) { EntityQuery() }

    var numTiles = 0
    var numEntities = 0

    fun ensureCapacity(requiredNumTiles: Int, requiredNumEntities: Int) {
        if (requiredNumTiles > tiles.size) tiles = Array(max(requiredNumTiles, 2 * tiles.size)) { null }
        if (requiredNumEntities > entities.size) entities = Array(max(requiredNumEntities, 2 * entities.size)) { EntityQuery() }
    }
}
