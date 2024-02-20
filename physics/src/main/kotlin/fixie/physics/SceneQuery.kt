package fixie.physics

class SceneQuery {
    val tiles = mutableListOf<Tile>()
    val entities = mutableListOf<EntityQuery>()

    internal val objectPool = mutableListOf<EntityQuery>()
}
