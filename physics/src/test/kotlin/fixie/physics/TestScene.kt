package fixie.physics

import fixie.geometry.LineSegment
import fixie.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TestScene {

    @Test
    fun stuckRegressionTest() {
        val scene = Scene()

        val spawnPlayer = EntitySpawnRequest(x = -8.6123.m, y = -19.72078.m, properties = EntityProperties(radius=100.mm))
        scene.spawnEntity(spawnPlayer)
        scene.update(Duration.ZERO)

        val lines = arrayOf(
                LineSegment(-9.301.m, -19.961.m, 0.872.m, 0.175.m),
                LineSegment(-9.069.m, -20.015.m, 0.704.m, 0.287.m)
        )

        for (line in lines) {
            scene.addTile(TilePlaceRequest(line, TileProperties()))
        }

        scene.update(10.seconds)

        val query = SceneQuery()
        scene.read(query, -10.m, -21.m, -7.m, -17.m)

        assertEquals(0, query.entities.size)
    }

    @Test
    fun testGravityAcceleration() {
        val scene = Scene()

        scene.spawnEntity(EntitySpawnRequest(
                10.m, 0.m, EntityProperties(radius = 1.m)
        ))

        scene.update(1.seconds)

        val query = SceneQuery()
        scene.read(query, 9.m, -10.m, 11.m, 0.m)

        assertEquals(1, query.entities.size)
        val subject = query.entities[0]
        assertEquals(10.m, subject.position.x)
        assertEquals(-4.9.m, subject.position.y, 100.mm)
        assertEquals(0.mps, subject.velocity.x)
        assertEquals(-9.8.mps, subject.velocity.y, 0.1.mps)
    }

    private fun assertEquals(expected: Displacement, actual: Displacement, maxError: Displacement) {
        if (abs(expected - actual) > maxError) assertEquals(expected, actual)
    }

    private fun assertEquals(expected: Speed, actual: Speed, maxError: Speed) {
        if (abs(expected - actual) > maxError) assertEquals(expected, actual)
    }
}
