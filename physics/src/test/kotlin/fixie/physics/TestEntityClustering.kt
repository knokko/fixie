package fixie.physics

import fixie.geometry.Position
import fixie.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class TestEntityClustering {

    private fun entity(props: EntityProperties, x: Displacement, y: Displacement): Entity {
        val entity = Entity(properties = props, position = Position.origin(), velocity = Velocity.zero())
        entity.wipPosition.x = x
        entity.wipPosition.y = y
        return entity
    }

    @Test
    fun testBasic() {
        val clustering = EntityClustering()
        val props = EntityProperties(radius = 1.m)
        val pos1 = entity(props, 100.m, 1.km)
        val pos2 = entity(props, 101.m, 1.km)
        val neg1 = entity(props, -100.m, -1.km)
        val neg2 = entity(props, -100.m, -1.km)

        val lonePos = entity(props, 300.m, 1.km)
        val loneNeg = entity(props, -300.m, -1.km)

        clustering.insert(pos1, 20.m)
        clustering.insert(pos1, 20.m)
        clustering.insert(neg1, 20.m)
        clustering.insert(neg1, 20.m)
        clustering.insert(lonePos, 20.m)
        clustering.insert(loneNeg, 20.m)

        val allEntities = arrayOf(pos1, pos2, neg1, neg2, lonePos, loneNeg)

        fun check(entity: Entity, expected: List<Entity>) {
            val result = mutableListOf<Entity>()
            clustering.query(entity, result)

            assertTrue(allEntities.contains(entity))
            for (candidate in allEntities) {
                assertEquals(expected.contains(entity), result.contains(candidate))
            }
        }

        check(pos1, listOf(pos2))
        check(pos2, listOf(pos1))
        check(neg1, listOf(neg2))
        check(neg2, listOf(neg1))
        check(lonePos, emptyList())
        check(loneNeg, emptyList())
    }

    @Test
    fun testInsertMultipleEntitiesAtSamePosition() {
        val clustering = EntityClustering()
        val props = EntityProperties(radius = 1.mm)
        val a = entity(props, 12.m, 123.m)
        val b = entity(props, 12.m, 123.m)

        clustering.insert(a, 2.mm)
        clustering.insert(b, 2.mm)

        val result = mutableListOf<Entity>()
        clustering.query(a, result)
        assertEquals(1, result.size)
        assertTrue(result.contains(b))
    }

    @Test
    fun testRandom() {
        val props = EntityProperties(radius = 1.m)
        val clustering = EntityClustering()

        val rng = Random(123)
        val entities = Array(2000) {
            entity(props, (-20_000 + rng.nextInt(40_000)).mm, (-20_000 + rng.nextInt(40_000)).mm)
        }

        fun check(entity: Entity) {
            val actual = mutableListOf<Entity>()
            clustering.query(entity, actual)
            assertFalse(actual.contains(entity))

            for (candidate in entities) {
                if (candidate == entity) continue
                val dx = entity.wipPosition.x - candidate.wipPosition.x
                val dy = entity.wipPosition.y - candidate.wipPosition.y
                val centerDistance = sqrt(dx * dx + dy * dy)
                val safeRadius = 2 * (entity.properties.radius + candidate.properties.radius)
                if (centerDistance <= safeRadius) assertTrue(actual.contains(candidate))
                if (centerDistance > 10 * safeRadius) assertFalse(actual.contains(candidate))
            }
        }

        for (counter in 0 until 2) {
            clustering.reset()

            for (entity in entities) {
                clustering.insert(entity, 2 * entity.properties.radius)
            }

            for (entity in entities) check(entity)
        }
    }
}
