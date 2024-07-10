package fixie.physics

import fixie.geometry.LineSegment
import fixie.*
import fixie.geometry.Position
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

internal fun addNarrowPipes(scene: Scene, radius: Displacement) {
    val d = 2 * radius
    val lengthX = 2.m
    val lengthY = 0.5.m
    val length = sqrt(lengthX * lengthX + lengthY * lengthY)
    val normalX = -lengthY / length
    val normalY = lengthX / length

    fun rightToLeftPipe(baseY: Displacement, margin: Displacement) = arrayOf(
        LineSegment(startX = -0.5.m, startY = baseY, lengthX = lengthX, lengthY = lengthY),
        LineSegment(startX = -0.5.m + (d + margin) * normalX, startY = baseY + (d + margin) * normalY, lengthX = lengthX / 2, lengthY = lengthY / 2),
    )

    fun rightToLeftSlope(baseY: Displacement) = arrayOf(
        LineSegment(startX = 1.8.m, startY = baseY, lengthX = lengthX, lengthY = lengthY),
    )

    fun leftToRightPipe(baseY: Displacement, margin: Displacement) = arrayOf(
        LineSegment(startX = 1.2.m, startY = baseY, lengthX = -lengthX, lengthY = lengthY),
        LineSegment(startX = 1.2.m - (d + margin) * normalX, startY = baseY + (d + margin) * normalY, lengthX = -lengthX / 2, lengthY = lengthY / 2)
    )

    fun leftToRightSlope(baseY: Displacement) = arrayOf(
        LineSegment(startX = 3.5.m, startY = baseY, lengthX = -lengthX, lengthY = lengthY),
    )

    val tiles = listOf(
        LineSegment(startX = -0.8.m, startY = -13.m, lengthX = 0.m, lengthY = 18.m),
        LineSegment(startX = 1.5.m, startY = -13.m, lengthX = 0.m, lengthY = 18.m),
        LineSegment(startX = 3.8.m, startY = -13.m, lengthX = 0.m, lengthY = 18.m),
        LineSegment(startX = -0.8.m, startY = -13.m, lengthX = 4.6.m, lengthY = 0.m)
    ) + rightToLeftPipe(4.m, 5.mm) + leftToRightPipe(3.m, 4.mm) +
            rightToLeftSlope(4.m) + leftToRightSlope(3.m) +

            rightToLeftPipe(2.m, 3.mm) + leftToRightPipe(1.m, 2.mm) +
            rightToLeftSlope(2.m) + leftToRightSlope(1.m) +

            rightToLeftPipe(0.m, 1.mm) + leftToRightPipe(-1.m, 0.5.mm) +
            rightToLeftSlope(0.m) + leftToRightSlope(-1.m) +

            rightToLeftPipe(-2.m, 0.25.mm) + leftToRightPipe(-3.m, 0.13.mm) +
            rightToLeftSlope(-2.m) + leftToRightSlope(-3.m) +

            // First stutters start at 0.06mm = 6 units
            rightToLeftPipe(-4.m, 0.07.mm) + leftToRightPipe(-5.m, 0.06.mm) +
            rightToLeftSlope(-4.m) + leftToRightSlope(-5.m) +

            rightToLeftPipe(-6.m, 0.05.mm) + leftToRightPipe(-7.m, 0.04.mm) +
            rightToLeftSlope(-6.m) + leftToRightSlope(-7.m) +

            // Sometimes gets stuck at 0.03mm = 3 units
            rightToLeftPipe(-8.m, 0.03.mm) + leftToRightPipe(-9.m, 0.02.mm) +
            rightToLeftSlope(-8.m) + leftToRightSlope(-9.m) +

            // Always gets stuck at 0.01mm = 1 unit
            rightToLeftPipe(-10.m, 0.01.mm) + leftToRightPipe(-11.m, 0.m) +
            rightToLeftSlope(-10.m) + leftToRightSlope(-11.m)

    for (tile in tiles) scene.addTile(TilePlaceRequest(collider = tile, properties = TileProperties()))
}

class TestScene {

    @Test
    fun narrowPipesTest() {
        /*
         * In this test scene, a ball needs to roll down through pipes that get more and more narrow. Another ball
         * needs to roll through the same parkour, but without the narrow pipes. A part of the parkour is shown in
         * sketches/scene/narrow-pipes.png.
         *
         * This test will check that the ball can at least keep rolling until it reaches the 0.5mm pipe,
         * and that it doesn't go much slower than the ball that is unhindered by the narrow pipes.
         */

        val scene = Scene()
        val radius = 100.mm
        val leftRequest = EntitySpawnRequest(x = 1.m, y = 5.m, properties = EntityProperties(radius = radius))
        val rightRequest = EntitySpawnRequest(x = 3.3.m, y = 5.m, properties = EntityProperties(radius = radius))

        scene.spawnEntity(leftRequest)
        scene.spawnEntity(rightRequest)
        addNarrowPipes(scene, radius)
        scene.update(0.seconds)

        val query = SceneQuery()
        for (counter in 0 until 100) {
            scene.update(1.seconds)
            scene.read(query, -5.m, -20.m, 5.m, 20.m)

            assertEquals(2, query.entities.size)
            val y1 = query.entities[0].position.y
            val y2 = query.entities[1].position.y

            if (y1 > -1.m) assertTrue(abs(y1 - y2) < 0.1.m, "Difference between $y1 and $y2 is too large")
        }

        val y1 = query.entities[0].position.y
        val y2 = query.entities[1].position.y
        assertTrue(
            y1 < -1.m && y2 < -1.m
            , "Both balls should have rolled below the 0.5mm pipe by now, but their positions are $y1 and $y2"
        )
        assertTrue(
            y1 < -12.m || y2 < -12.m,
            "One of the balls should have reached the bottom by now, but their positions are $y1 and $y2"
        )
    }

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
    fun slowRollRegressionTest() {
        // This test reproduces a former bug that caused an acceleration ball that is rolling uphill to be
        // much slower than it should. See sketches/scene/slow-roll.png

        val scene = Scene()

        var passedTime = 0.seconds
        val entityProperties = EntityProperties(radius = 100.mm) { _, velocity ->
            passedTime += Scene.STEP_DURATION

            // After 0.5 seconds, the ball will accelerate to the right with 5m/s^2, which should cause it to leave the
            // area within 5 seconds
            if (passedTime > 500.milliseconds) {
                velocity.x += 5.mps2 * Scene.STEP_DURATION
            }
        }

        scene.spawnEntity(EntitySpawnRequest(x = 1.61.m, y = 2.63.m, properties = entityProperties))

        val tiles = listOf(
                LineSegment(startX = 1.5.m, startY = 2.5.m, lengthX = 0.3.m, lengthY = 70.mm),
                LineSegment(startX = 1.5.m, startY = 2.5.m, lengthX = -2.m, lengthY = -0.5.m),
                LineSegment(startX = 1.5.m, startY = 2.59.m, lengthX = -2.m, lengthY = -0.5.m)
        )
        for (tile in tiles) scene.addTile(TilePlaceRequest(collider = tile, properties = TileProperties()))

        scene.update(5.seconds)

        val query = SceneQuery()
        scene.read(query, 1.m, 2.m, 3.m, 3.m)

        assertEquals(0, query.entities.size)
    }

    @Test
    fun testNotMovingConstraint() {
        // This scene is depicted in sketches/scene/not-moving.png. The balls should fall into this position within
        // 20 seconds, after which they should stabilize. This test checks that their velocities stay nearly zero and
        // that their positions don't change too much.
        val scene = Scene()

        val length = 10.m

        scene.addTile(TilePlaceRequest(LineSegment(
                startX = -length, startY = 0.m, lengthX = length, lengthY = -length
        ), TileProperties()))
        scene.addTile(TilePlaceRequest(LineSegment(
                startX = length, startY = 0.m, lengthX = -length, lengthY = -length
        ), TileProperties()))

        for (counter in -5 .. 5) {
            scene.spawnEntity(EntitySpawnRequest(
                    x = counter.m, y = 0.m, properties = EntityProperties(radius = 0.2.m)
            ))
            scene.spawnEntity(EntitySpawnRequest(
                    x = counter.m, y = 0.4.m, properties = EntityProperties(radius = 0.1.m)
            ))
            scene.spawnEntity(EntitySpawnRequest(
                    x = counter.m, y = 0.9.m, properties = EntityProperties(radius = 0.3.m)
            ))
        }

        // The scene should be stabilized after 20 seconds
        scene.update(20.seconds)

        val stablePositions = Array(33) { Position.origin() }
        val query = SceneQuery()
        for (counter in 0 until 100) {
            scene.read(query, -10.m, -10.m, 10.m, 10.m)
            assertEquals(33, query.entities.size)

            for (index in 0 until query.entities.size) {
                val entity = query.entities[index]
                val threshold = 2.mps
                assertTrue(
                        abs(entity.velocity.x) < threshold && abs(entity.velocity.y) < threshold,
                        "The velocity components ${entity.velocity} can be at most $threshold"
                )
                val stablePosition = stablePositions[index]
                if (stablePosition == Position.origin()) {
                    stablePosition.x = entity.position.x
                    stablePosition.y = entity.position.y
                }

                val distance = stablePosition.distance(entity.position)
                assertTrue(distance < 50.mm, "Distance $distance to stable position can be at most 50mm")
            }

            scene.update(Scene.STEP_DURATION)
        }
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
