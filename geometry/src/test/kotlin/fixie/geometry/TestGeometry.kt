package fixie.geometry

import fixie.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.random.Random

class TestGeometry {

    private fun assertEquals(expected: Displacement, actual: Displacement) {
        val difference = expected - actual
        if (difference < -0.02.mm || difference > 0.02.mm) Assertions.assertEquals(expected, actual)
    }

    private fun assertEquals(expectedPoint: Position, actual: Position) {
        this.assertEquals(expectedPoint.x, actual.x)
        this.assertEquals(expectedPoint.y, actual.y)
    }

    @Test
    fun testDistanceBetweenPointAndLineSegmentPrecision() {
        val linePoint = Position.origin()
        Geometry.distanceBetweenPointAndLineSegment(
            0.02852.m, 0.10005.m, LineSegment(-20.m, 0.m, 100.m, 0.m), linePoint
        )
        println("linePoint is $linePoint")
    }

    @Test
    fun testDistanceBetweenPointAndLineSegment() {
        val rng = Random.Default
        for (counter in 0 until 10) {
            val maxDisplacement = Int.MAX_VALUE / 3
            val offsetX = Displacement.raw(rng.nextInt(-maxDisplacement, maxDisplacement))
            val offsetY = Displacement.raw(rng.nextInt(-maxDisplacement, maxDisplacement))

            fun testPointLine(
                px: Displacement, py: Displacement, lineSegment: LineSegment,
                expectedDistance: Displacement, expectedPoint: Position
            ) {
                val actualPoint = Position.origin()
                val actualDistance = Geometry.distanceBetweenPointAndLineSegment(
                    px + offsetX, py + offsetY, lineSegment, actualPoint
                )
                this.assertEquals(expectedDistance, actualDistance)
                this.assertEquals(expectedPoint, Position(actualPoint.x - offsetX, actualPoint.y - offsetY))
            }

            val horizontal = LineSegment(startX = 10.m + offsetX, startY = 3.m + offsetY, lengthX = 50.m, lengthY = 0.m)

            testPointLine(5.m, 3.m, horizontal, 5.m, Position(10.m, 3.m))
            testPointLine(10.m, 3.m, horizontal, 0.m, Position(10.m, 3.m))
            testPointLine(50.m, 3.m, horizontal, 0.m, Position(50.m, 3.m))
            testPointLine(60.m, 3.m, horizontal, 0.m, Position(60.m, 3.m))
            testPointLine(65.m, 3.m, horizontal, 5.m, Position(60.m, 3.m))

            testPointLine(7.m, 7.m, horizontal, 5.m, Position(10.m, 3.m))
            testPointLine(7.m, -1.m, horizontal, 5.m, Position(10.m, 3.m))
            testPointLine(10.m, 10.m, horizontal, 7.m, Position(10.m, 3.m))
            testPointLine(20.m, -4.m, horizontal, 7.m, Position(20.m, 3.m))
            testPointLine(60.m, -4.m, horizontal, 7.m, Position(60.m, 3.m))
            testPointLine(63.m, 7.m, horizontal, 5.m, Position(60.m, 3.m))

            val vertical = LineSegment(startX = 50.m + offsetX, startY = 10.m + offsetY, lengthX = 0.m, lengthY = 90.m)

            testPointLine(50.m, 10.m, vertical, 0.m, Position(50.m, 10.m))
            testPointLine(50.m, 80.m, vertical, 0.m, Position(50.m, 80.m))
            testPointLine(40.m, 80.m, vertical, 10.m, Position(50.m, 80.m))
            testPointLine(50.m, 100.m, vertical, 0.m, Position(50.m, 100.m))
            testPointLine(40.m, 100.m, vertical, 10.m, Position(50.m, 100.m))

            // Edge case
            testPointLine(-300.m, 400.m, LineSegment(
                startX = offsetX, startY = offsetY, lengthX = 0.01.mm, lengthY = 0.m
            ), 500.m, Position(0.m, 0.m))
        }
    }

    @Test
    fun testDistanceBetweenLineSegments() {

        val rng = Random.Default
        for (counter in 0 until 1000) {
            val maxDisplacement = Int.MAX_VALUE / 3
            val offsetX = Displacement.raw(rng.nextInt(-maxDisplacement, maxDisplacement))
            val offsetY = Displacement.raw(rng.nextInt(-maxDisplacement, maxDisplacement))

            fun testLines(
                x1: Displacement, y1: Displacement, lx1: Displacement, ly1: Displacement, lineSegment: LineSegment,
                expectedDistance: Displacement, expectedPoint1: Position, expectedPoint2: Position
            ) {
                val outPoint1 = Position.origin()
                val outPoint2 = Position.origin()
                var actualDistance = Geometry.distanceBetweenLineSegments(
                    x1 + offsetX, y1 + offsetY, lx1, ly1, outPoint1,
                    lineSegment.startX, lineSegment.startY, lineSegment.lengthX, lineSegment.lengthY, outPoint2
                )
                this.assertEquals(expectedDistance, actualDistance)
                this.assertEquals(expectedPoint1, Position(outPoint1.x - offsetX, outPoint1.y - offsetY))
                this.assertEquals(expectedPoint2, Position(outPoint2.x - offsetX, outPoint2.y - offsetY))

                // Test symmetry
                actualDistance = Geometry.distanceBetweenLineSegments(
                    lineSegment.startX, lineSegment.startY, lineSegment.lengthX, lineSegment.lengthY, outPoint2,
                    x1 + offsetX, y1 + offsetY, lx1, ly1, outPoint1
                )
                this.assertEquals(expectedDistance, actualDistance)
                this.assertEquals(expectedPoint1, Position(outPoint1.x - offsetX, outPoint1.y - offsetY))
                this.assertEquals(expectedPoint2, Position(outPoint2.x - offsetX, outPoint2.y - offsetY))
            }

            val vertical = LineSegment(5.m + offsetX, 10.m + offsetY, 0.m, 20.m)
            testLines(1.m, 20.m, 3.m, 0.m, vertical, 1.m, Position(4.m, 20.m), Position(5.m, 20.m))
            testLines(1.m, 20.m, 4.m, 0.m, vertical, 0.m, Position(5.m, 20.m), Position(5.m, 20.m))
            testLines(1.m, 20.m, 5.m, 0.m, vertical, 0.m, Position(5.m, 20.m), Position(5.m, 20.m))

            testLines(1.m, 0.m, 4.m, 0.m, vertical, 10.m, Position(5.m, 0.m), Position(5.m, 10.m))
            testLines(1.m, 0.m, 5.m, 0.m, vertical, 10.m, Position(5.m, 0.m), Position(5.m, 10.m))

            testLines(1.m, 6.m, 1.m, 0.m, vertical, 5.m, Position(2.m, 6.m), Position(5.m, 10.m))

            testLines(1.m, 40.m, 4.m, 0.m, vertical, 10.m, Position(5.m, 40.m), Position(5.m, 30.m))
            testLines(1.m, 40.m, 5.m, 0.m, vertical, 10.m, Position(5.m, 40.m), Position(5.m, 30.m))

            testLines(1.m, 34.m, 1.m, 0.m, vertical, 5.m, Position(2.m, 34.m), Position(5.m, 30.m))

            testLines(6.m, 20.m, 3.m, 0.m, vertical, 1.m, Position(6.m, 20.m), Position(5.m, 20.m))
            testLines(5.m, 0.m, 4.m, 0.m, vertical, 10.m, Position(5.m, 0.m), Position(5.m, 10.m))
            testLines(8.m, 6.m, 1.m, 0.m, vertical, 5.m, Position(8.m, 6.m), Position(5.m, 10.m))
            testLines(5.m, 40.m, 4.m, 0.m, vertical, 10.m, Position(5.m, 40.m), Position(5.m, 30.m))
            testLines(8.m, 34.m, 1.m, 0.m, vertical, 5.m, Position(8.m, 34.m), Position(5.m, 30.m))

            // Nasty overlap tests
            testLines(5.m, 0.m, 0.m, 10.m, vertical, 0.m, Position(5.m, 10.m), Position(5.m, 10.m))
            testLines(5.m, 30.m, 0.m, 10.m, vertical, 0.m, Position(5.m, 30.m), Position(5.m, 30.m))
            testLines(5.m, 30.m, 0.m, 20.m, vertical, 0.m, Position(5.m, 30.m), Position(5.m, 30.m))
            val point1 = Position.origin()
            val point2 = Position.origin()
            this.assertEquals(0.m, Geometry.distanceBetweenLineSegments(
                5.m + offsetX, 0.m + offsetY, 0.m, 20.m, point1,
                vertical.startX, vertical.startY, vertical.lengthX, vertical.lengthY, point2
            ))
            this.assertEquals(5.m + offsetX, point1.x)
            this.assertEquals(point1, point2)
            this.assertEquals(0.m, Geometry.distanceBetweenLineSegments(
                5.m + offsetX, 0.m + offsetY, 0.m, 30.m, point1,
                vertical.startX, vertical.startY, vertical.lengthX, vertical.lengthY, point2
            ))
            this.assertEquals(5.m + offsetX, point1.x)
            this.assertEquals(point1, point2)
            this.assertEquals(0.m, Geometry.distanceBetweenLineSegments(
                5.m + offsetX, 10.m + offsetY, 0.m, 20.m, point1,
                vertical.startX, vertical.startY, vertical.lengthX, vertical.lengthY, point2
            ))
            this.assertEquals(5.m + offsetX, point1.x)
            this.assertEquals(point1, point2)
            this.assertEquals(0.m, Geometry.distanceBetweenLineSegments(
                5.m + offsetX, 10.m + offsetY, 0.m, 10.m, point1,
                vertical.startX, vertical.startY, vertical.lengthX, vertical.lengthY, point2
            ))
            this.assertEquals(5.m + offsetX, point1.x)
            this.assertEquals(point1, point2)
            this.assertEquals(0.m, Geometry.distanceBetweenLineSegments(
                5.m + offsetX, 10.m + offsetY, 0.m, 30.m, point1,
                vertical.startX, vertical.startY, vertical.lengthX, vertical.lengthY, point2
            ))
            this.assertEquals(5.m + offsetX, point1.x)
            this.assertEquals(point1, point2)

            // Also horizontal overlap
            this.assertEquals(0.m, Geometry.distanceBetweenLineSegments(
                offsetX, offsetY, 10.m, 0.m, point1,
                offsetX + 1.m, offsetY, 8.m, 0.m, point2
            ))
            this.assertEquals(offsetY, point1.y)
            this.assertEquals(point1, point2)
        }

        // A disgusting nearly-parallel edge-case
        val point1 = Position.origin()
        val point2 = Position.origin()
        this.assertEquals(0.m, Geometry.distanceBetweenLineSegments(
            100.m, 10.m + Displacement.raw(1), 100.m, -Displacement.raw(2), point1,
            100.m, 10.m, 100.m, 0.m, point2
        ))
        this.assertEquals(150.m, point1.x)
        this.assertEquals(10.m, point1.y)
        this.assertEquals(point1, point2)
    }

    @Test
    fun testSweepCircleToLineSegment() {
        // TODO This needs some actual testing
        val circlePosition = Position.origin()
        val pointOnLine = Position.origin()
        val wasHit = Geometry.sweepCircleToLineSegment(
            2.m, 10.m, 3.m, -10.m, 500.mm, 5.m, 0.m, 200.m, 0.m, circlePosition, pointOnLine
        )

        if (wasHit) {
            println("Hit line at $pointOnLine, with center at $circlePosition")
        } else println("miss")
    }
}
