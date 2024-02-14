package fixie.geometry

import fixie.*

object Geometry {

    fun sweepCircleToLineSegment(
        cx: Displacement, cy: Displacement, cvx: Displacement, cvy: Displacement, cr: Displacement,
        line: LineSegment, outCirclePosition: Position, outPointOnLine: Position
    ) = sweepCircleToLineSegment(
        cx, cy, cvx, cvy, cr, line.startX, line.startY,
        line.lengthX, line.lengthY, outCirclePosition, outPointOnLine
    )

    fun sweepCircleToLineSegment(
        cx: Displacement, cy: Displacement, cvx: Displacement, cvy: Displacement, cr: Displacement,
        lsx: Displacement, lsy: Displacement, lslx: Displacement, lsly: Displacement,
        outCirclePosition: Position, outPointOnLine: Position
    ): Boolean {
        val fullDistance = distanceBetweenLineSegments(
            cx, cy, cvx, cvy, outCirclePosition,
            lsx, lsy, lslx, lsly, outPointOnLine
        )

        // When the line segment distance is larger than the radius, there is no collision
        if (fullDistance > cr) return false

        val totalMovement = sqrt(cvx * cvx + cvy * cvy)

        // Sanity check to avoid potential endless loops
        if (distanceBetweenPointAndLineSegment(cx, cy, lsx, lsy, lslx, lsly, outPointOnLine) <= cr) {
            throw IllegalArgumentException("circle at ($cx, $cy) with radius $cr is already in line ($lsx, $lsy, $lslx, $lsly)")
        }

        var useBinarySearch = false
        var signumCounter = 0
        var largestSafeMovement = 0.m
        var largestSafeDistance = distanceBetweenPointAndLineSegment(cx, cy, lsx, lsy, lslx, lsly, outPointOnLine)

        val dix = outCirclePosition.x - cx
        val diy = outCirclePosition.y - cy
        var smallestUnsafeMovement = sqrt(dix * dix + diy * diy)
        var smallestUnsafeDistance = fullDistance
        var candidateMovement = smallestUnsafeMovement
        while ((smallestUnsafeMovement - largestSafeMovement) > 0.1.mm && largestSafeDistance - cr > 0.1.mm) {
            val movementFactor = candidateMovement.toDouble(DistanceUnit.METER) / totalMovement.toDouble(DistanceUnit.METER)
            val newX = cx + (movementFactor * cvx.toDouble(DistanceUnit.METER)).m
            val newY = cy + (movementFactor * cvy.toDouble(DistanceUnit.METER)).m
            val distance = distanceBetweenPointAndLineSegment(
                newX, newY, lsx, lsy, lslx, lsly, outPointOnLine
            )

            if (distance > cr) {
                if (signumCounter == -1) useBinarySearch = true
                signumCounter -= 1
                if (largestSafeMovement < candidateMovement) {
                    largestSafeMovement = candidateMovement
                    largestSafeDistance = distance
                }
            } else {
                if (signumCounter == 1) useBinarySearch = true
                signumCounter += 1
                if (smallestUnsafeMovement > candidateMovement) {
                    smallestUnsafeMovement = candidateMovement
                    smallestUnsafeDistance = distance
                }
            }

            if (useBinarySearch) candidateMovement = (largestSafeMovement + smallestUnsafeMovement) / 2
            else {
                // Example:
                // - radius = 100mm
                // - largestSafeDistance = 150mm
                // - largestSafeMovement = 600mm
                // - smallestUnsafeDistance = 40mm
                // - smallestUnsafeMovement = 800mm
                //
                // - distanceFactor = (150 - 100) / (150 - 40) = 50 / 110 = 0.45
                // - candidateMovement = 600 + 0.45 * (800 - 600) = 600 + 0.45 * 200 = 690
                var distanceFactor = (largestSafeDistance - cr) / (largestSafeDistance - smallestUnsafeDistance)

                distanceFactor *= if (largestSafeDistance - cr > cr - smallestUnsafeDistance) 1.05 else 0.95
                if (distanceFactor < 0) distanceFactor = FixDisplacement.ZERO
                if (distanceFactor > 1) distanceFactor = FixDisplacement.ONE
                candidateMovement = largestSafeMovement + distanceFactor * (smallestUnsafeMovement - largestSafeMovement)
            }
        }

        val movementFactor = largestSafeMovement.toDouble(DistanceUnit.METER) / totalMovement.toDouble(DistanceUnit.METER)

        val newX = cx + (movementFactor * cvx.toDouble(DistanceUnit.METER)).m
        val newY = cy + (movementFactor * cvy.toDouble(DistanceUnit.METER)).m
        outCirclePosition.x = newX
        outCirclePosition.y = newY
        distanceBetweenPointAndLineSegment(newX, newY, lsx, lsy, lslx, lsly, outPointOnLine)
        return true
    }

    fun sweepCircleToCircle(
        x1: Displacement, y1: Displacement, r1: Displacement, vx: Displacement, vy: Displacement,
        x2: Displacement, y2: Displacement, r2: Displacement, outPosition: Position
    ): Boolean {
        val r = r1 + r2
        val originalDistance = sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
        if (originalDistance <= r1 + r2) throw IllegalArgumentException("Circle at ($x1, $y1) with r=$r1 is already inside ($x2, $y2) with r=$r2")

        findClosestPointOnLineToPoint(x2, y2, x1, y1, vx, vy, outPosition)

        val minDistance = sqrt((x2 - outPosition.x) * (x2 - outPosition.x) + (y2 - outPosition.y) * (y2 - outPosition.y))
        if (minDistance > r + 1.mm) return false

        val dx = outPosition.x - x1
        val dy = outPosition.y - y1

        if (abs(vx) >= abs(vy)) {
            if ((vx >= 0.m) != (dx >= 0.m)) return false
        } else {
            if ((vy >= 0.m) != (dy >= 0.m)) return false
        }

        val idealX = x1 + vx
        val idealY = y1 + vy
        val idealDistance = sqrt(vx * vx + vy * vy)

        val blockedDistance = sqrt(r * r - minDistance * minDistance)
        val maxDistance = sqrt(dx * dx + dy * dy)
        var maxMoveDistance = maxDistance - blockedDistance
        if (maxMoveDistance <= 0.m) {
            outPosition.x = x1
            outPosition.y = y1
            return true
        }

        if (idealDistance <= maxMoveDistance) {
            if (sqrt((idealX - x2) * (idealX - x2) + (idealY - y2) * (idealY - y2)) > r) return false
        }

        do {
            val movementFactor = maxMoveDistance.toDouble(DistanceUnit.METER) / idealDistance.toDouble(DistanceUnit.METER)
            val testX = x1 + (movementFactor * vx.toDouble(DistanceUnit.METER)).m
            val testY = y1 + (movementFactor * vy.toDouble(DistanceUnit.METER)).m
            val testDistance = sqrt((testX - x2) * (testX - x2) + (testY - y2) * (testY - y2))
            maxMoveDistance -= 0.03.mm
            outPosition.x = testX
            outPosition.y = testY
        } while (testDistance <= r)

        return true
    }

    fun distanceBetweenLineSegments(
        x1: Displacement, y1: Displacement, lx1: Displacement, ly1: Displacement, outPoint1: Position,
        x2: Displacement, y2: Displacement, lx2: Displacement, ly2: Displacement, outPoint2: Position
    ): Displacement {
        val xTo1 = x1 + lx1
        val yTo1 = y1 + ly1

        val xTo2 = x2 + lx2
        val yTo2 = y2 + ly2

        // Whether the starting point of line segment 1 is 'above' line 2
        val start1Above2 = (lx2 * (y1 - y2) - ly2 * (x1 - x2)).raw >= 0

        // Whether the end point of line segment 1 is 'above' line 2
        val end1Above2 = (lx2 * (yTo1 - y2) - ly2 * (xTo1 - x2)).raw >= 0

        // Whether the starting point of line segment 2 is 'above' line 1
        val start2Above1 = (lx1 * (y2 - y1) - ly1 * (x2 - x1)).raw >= 0

        // Whether the end point of line segment 2 is 'above' line 1
        val end2Above1 = (lx1 * (yTo2 - y1) - ly1 * (xTo2 - x1)).raw >= 0

        // Case 1: intersection
        if (start1Above2 != end1Above2 && start2Above1 != end2Above1) {
            // To find the intersection, solve:
            // (1.1) x1 + a * lx1 = x2 + b * lx2
            // (1.2) y1 + a * ly1 = y2 + b * ly2

            // Isolate (a):
            // (2.1) a = (x2 - x1 + b * lx2) / lx1
            // (2.2) a = (y2 - y1 + b * ly2) / ly1

            // This gives:
            // (3) (x2 - x1 + b * lx2) / lx1 = (y2 - y1 + b * ly2) / ly1

            // Multiply both sides with lx1 * ly1:
            // (4) ly1 * (x2 - x1 + b * lx2) = lx1 * (y2 - y1 + b * ly2)

            // Isolate (b)...
            // (5) b * lx2 * ly1 + ly1 * (x2 - x1) = b * ly2 * lx1 + lx1 * (y2 - y1)
            // (6) b * (lx2 * ly1 - ly2 * lx1) = lx1 * (y2 - y1) - ly1 * (x2 - x1)
            // (7) b = (lx1 * (y2 - y1) - ly1 * (x2 - x1)) / (lx2 * ly1 - ly2 * lx1)

            val b = (lx1 * (y2 - y1) - ly1 * (x2 - x1)) / (lx2 * ly1 - ly2 * lx1)
            val x = x2 + b * lx2
            val y = y2 + b * ly2
            outPoint1.x = x
            outPoint1.y = y
            outPoint2.x = x
            outPoint2.y = y
            return 0.m
        }

        // Case 2: line segment 1 intersects the line through line segment 2
        if (start1Above2 != end1Above2) {
            val startDistance = distanceBetweenPointAndLineSegment(x2, y2, x1, y1, lx1, ly1, outPoint1)
            val endDistance = distanceBetweenPointAndLineSegment(xTo2, yTo2, x1, y1, lx1, ly1, outPoint2)
            return if (startDistance > endDistance) {
                outPoint1.x = outPoint2.x
                outPoint1.y = outPoint2.y
                outPoint2.x = xTo2
                outPoint2.y = yTo2
                endDistance
            } else {
                outPoint2.x = x2
                outPoint2.y = y2
                startDistance
            }
        }

        // Case 3: line segment 2 intersects the line through line segment 1
        if (start2Above1 != end2Above1) {
            val startDistance = distanceBetweenPointAndLineSegment(x1, y1, x2, y2, lx2, ly2, outPoint1)
            val endDistance = distanceBetweenPointAndLineSegment(xTo1, yTo1, x2, y2, lx2, ly2, outPoint2)
            return if (startDistance > endDistance) {
                outPoint1.x = xTo1
                outPoint1.y = yTo1
                endDistance
            } else {
                outPoint2.x = outPoint1.x
                outPoint2.y = outPoint1.y
                outPoint1.x = x1
                outPoint1.y = y1
                startDistance
            }
        }

        // Case 4 remains: the line segments are (almost) parallel
        val endDistance1 = distanceBetweenPointAndLineSegment(xTo1, yTo1, x2, y2, lx2, ly2, outPoint2)
        val startDistance1 = distanceBetweenPointAndLineSegment(x1, y1, x2, y2, lx2, ly2, outPoint2)
        val endDistance2 = distanceBetweenPointAndLineSegment(xTo2, yTo2, x1, y1, lx1, ly1, outPoint1)
        val startDistance2 = distanceBetweenPointAndLineSegment(x2, y2, x1, y1, lx1, ly1, outPoint1)


        if (startDistance1 <= endDistance1 && startDistance1 <= startDistance2 && startDistance1 <= endDistance2) {
            outPoint1.x = x1
            outPoint1.y = y1
            return startDistance1
        }

        if (startDistance2 <= endDistance1 && startDistance2 <= endDistance2) {
            outPoint2.x = x2
            outPoint2.y = y2
            return startDistance2
        }

        if (endDistance2 <= endDistance1) {
            outPoint2.x = xTo2
            outPoint2.y = yTo2
            return distanceBetweenPointAndLineSegment(xTo2, yTo2, x1, y1, lx1, ly1, outPoint1)
        }

        outPoint1.x = xTo1
        outPoint1.y = yTo1
        return distanceBetweenPointAndLineSegment(xTo1, yTo1, x2, y2, lx2, ly2, outPoint2)
    }

    fun distanceBetweenPointAndLineSegment(
        px: Displacement, py: Displacement, line: LineSegment, outPointOnLine: Position
    ) = distanceBetweenPointAndLineSegment(
        px, py, line.startX, line.startY, line.lengthX, line.lengthY, outPointOnLine
    )

    fun findClosestPointOnLineToPoint(
        px: Displacement, py: Displacement, lx: Displacement, ly: Displacement,
        ldx: Displacement, ldy: Displacement, outPointOnLine: Position
    ) {
        // Place the point position in the origin and convert the rest to double to obtain maximum precision
        val offsetX = (lx - px).toDouble(DistanceUnit.METER)
        val offsetY = (ly - py).toDouble(DistanceUnit.METER)
        val lengthX = ldx.toDouble(DistanceUnit.METER)
        val lengthY = ldy.toDouble(DistanceUnit.METER)

        // First, we need to find the intersection with the line through (px, py) perpendicular to l. Use:
        // - perp(endicular)X = ldy
        // - perp(endicular)Y = -ldx

        // Solve the following equations:
        // (1) px + a * perpX = lx + b * ldx
        // (2) py + a * perpY = ly + b * ldy

        // Isolating a gives:
        // (3) a = (lx + b * ldx - px) / perpX = (offsetX + b * lengthX) / perpX
        // (4) a = (ly + b * ldy - py) / perpY = (offsetY + b * lengthY) / perpY

        // Combining (3) and (4) gives:
        // (5) (offsetX + b * lengthX) / perpX = (offsetY + b * lengthY) / perpY

        // Multiplying both sides by perpX * perpY gives:
        // (6) perpY * (offsetX + b * lengthX) = perpX * (offsetY + b * lengthY)

        // Isolate b...
        // (7) b * lengthX * perpY - b * lengthY * perpX = perpX * offsetY - perpY * offsetX
        // (8) b * (lengthX * perpY - lengthY * perpX) = perpX * offsetY - perpY * offsetX
        // (9) b = (perpX * offsetY - perpY * offsetX) / (lengthX * perpY - lengthY * perpX)

        // Replace perpX with lengthY and perpY with -lengthX:
        // (10) b = (lengthY * offsetY + lengthX * offsetX) / (-lengthX * lengthX - lengthY * lengthY)

        // Simplify...
        // (11) b = -(lengthX * offsetX + lengthY * offsetY) / (lengthX * lengthX + lengthY * lengthY)
        val b = -(lengthX * offsetX + lengthY * offsetY) / (lengthX * lengthX + lengthY * lengthY)

        // The point on the line is given by:
        // (12) x = lx + ldx * b
        // (13) y = ly + ldy * b

        outPointOnLine.x = lx + (b * lengthX).m
        outPointOnLine.y = ly + (b * lengthY).m
    }

    internal fun distanceBetweenPointAndLineSegment(
        px: Displacement, py: Displacement, lx: Displacement, ly: Displacement,
        ldx: Displacement, ldy: Displacement, outPointOnLine: Position
    ): Displacement {
        findClosestPointOnLineToPoint(px, py, lx, ly, ldx, ldy, outPointOnLine)

        // The closest point on the *line* may or may not lie on the line *segment*
        val minX = min(lx, lx + ldx)
        val maxX = max(lx, lx + ldx)
        val x = min(max(outPointOnLine.x, minX), maxX)

        val minY = min(ly, ly + ldy)
        val maxY = max(ly, ly + ldy)
        val y = min(max(outPointOnLine.y, minY), maxY)

        outPointOnLine.x = x
        outPointOnLine.y = y

        return sqrt((x - px) * (x - px) + (y - py) * (y - py))
    }
}
