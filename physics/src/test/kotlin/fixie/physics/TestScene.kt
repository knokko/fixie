package fixie.physics

import fixie.geometry.LineSegment
import fixie.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class TestScene {

    @Test
    fun stickyBallsRegressionTest() {
        // This test reproduced a former bug where two balls would stick together when they collide
        val scene = Scene()

        val spawnBigBall = EntitySpawnRequest(x = 0.m, y = 1.601.m, properties = EntityProperties(radius = 100.mm), velocityX = 4.mps)
        val spawnSmallBall = EntitySpawnRequest(x = 1.5.m, y = 1.521.m, velocityX = 0.5.mps, properties = EntityProperties(radius = 20.mm))

        scene.spawnEntity(spawnSmallBall)
        scene.spawnEntity(spawnBigBall)
        scene.addTile(TilePlaceRequest(
                collider = LineSegment(startX = -10.m, startY = 1.5.m, lengthX = 30.m, lengthY = 0.m),
                properties = TileProperties()
        ))

        var collidingTime = 0.milliseconds

        val query = SceneQuery()
        for (counter in 0 until 5000) {
            scene.update(1.milliseconds)
            scene.read(query, -10.m, -10.m, 50.m, 20.m)

            assertEquals(2, query.entities.size)

            // If the distance between the objects is smaller than 30mm (= 150mm - radius1 - radius2),
            // the objects are either colliding or nearly colliding
            val distance = query.entities[0].position.distance(query.entities[1].position)
            if (distance < 150.mm) collidingTime += 1.milliseconds
        }

        // The colliding time should be smaller than 200ms. If not, the objects are basically sticking together.
        assertTrue(collidingTime < 200.milliseconds, "Colliding time $collidingTime should be smaller than 200ms")
    }

    @Test
    fun extremeBounceRegressionTest() {
        extremeBounceRegressionTest(1.mps)
        extremeBounceRegressionTest(1.1.mps)
    }

    private fun extremeBounceRegressionTest(velocityX: Speed) {
        // This test reproduces a former bug where a small ball rolls to a big ball, and is bounced back much harder
        // than it should. See sketches/scene/extreme-bounce.png
        val scene = Scene()

        val spawnPlayer = EntitySpawnRequest(x = 0.m, y = 1.101.m, velocityX = velocityX, properties = EntityProperties(radius = 100.mm))
        scene.spawnEntity(spawnPlayer)
        scene.spawnEntity(EntitySpawnRequest(x = 1.7.m, y = 3.001.m, properties = EntityProperties(radius = 2.m)))
        scene.addTile(TilePlaceRequest(
                collider = LineSegment(startX = -100.mm, startY = 1.m, lengthX = 3.m, lengthY = 0.m),
                properties = TileProperties()
        ))

        scene.update(10.seconds)

        val query = SceneQuery()
        scene.read(query, -5.m, -5.m, 5.m, 5.m)

        // The test should succeed when both balls are still within the (-5, -5) to (5, 5) region, and it should
        // fail when the small ball is pushed out of it.
        assertEquals(2, query.entities.size)
    }

    @Test
    fun stuckRegressionTest() {
        // See sketches/scene/stuck.png
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
