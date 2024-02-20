package fixie.physics

import fixie.*

private val GRID_SIZE = 1.m

internal class EntityClustering {

    private val entityMap = mutableMapOf<Long, MutableList<Entity>>()

    private fun determineIndex(x: Displacement): Int {
        val maybeResult = (x / GRID_SIZE).toInt()
        val isNegative = x.value.raw < 0
        return if (isNegative) maybeResult - 1
        else maybeResult
    }

    private fun determineKey(indexX: Int, indexY: Int) = indexX.toLong() + 1_000_000L * indexY.toLong()

    fun reset() {
        // TODO Maybe free their memory
        for (value in entityMap.values) value.clear()
    }

    fun insert(entity: Entity, safeRadius: Displacement) {
        entity.clusteringLists.clear()

        val minIndexX = determineIndex(entity.wipPosition.x - safeRadius)
        val minIndexY = determineIndex(entity.wipPosition.y - safeRadius)
        val maxIndexX = determineIndex(entity.wipPosition.x + safeRadius)
        val maxIndexY = determineIndex(entity.wipPosition.y + safeRadius)

        for (indexX in minIndexX .. maxIndexX) {
            for (indexY in minIndexY .. maxIndexY) {
                val key = determineKey(indexX, indexY)
                val value = entityMap.getOrPut(key) { mutableListOf() }
                value.add(entity)
                entity.clusteringLists.add(value)
            }
        }
    }

    fun query(entity: Entity, outEntities: MutableList<Entity>) {
        if (outEntities.isNotEmpty()) throw IllegalArgumentException()
        entity.isAlreadyPresent = true

        for (cluster in entity.clusteringLists) {
            for (candidate in cluster) {
                if (!candidate.isAlreadyPresent) {
                    outEntities.add(candidate)
                    candidate.isAlreadyPresent = true
                }
            }
        }

        for (candidate in outEntities) candidate.isAlreadyPresent = false
        entity.isAlreadyPresent = false
    }
}
