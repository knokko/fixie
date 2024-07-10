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
        if (fullDistance > cr + 1.mm) return false

        val totalMovement = sqrt(cvx * cvx + cvy * cvy)

        // Sanity check to avoid potential endless loops
        var largestSafeDistance = distanceBetweenPointAndLineSegment(cx, cy, lsx, lsy, lslx, lsly, outPointOnLine)
        if (largestSafeDistance <= cr) {
            throw IllegalArgumentException("circle at ($cx, $cy) with radius $cr is already in line ($lsx, $lsy, $lslx, $lsly)")
        }

        val idealDistance = distanceBetweenPointAndLineSegment(cx + cvx, cy + cvy, lsx, lsy, lslx, lsly, outPointOnLine)

        // Dirty trick
        if (idealDistance > cr && fullDistance > 0.99 * cr) return false

        var useBinarySearch = false
        var signumCounter = 0
        var largestSafeMovement = 0.m

        val dix = outCirclePosition.x - cx
        val diy = outCirclePosition.y - cy
        var smallestUnsafeMovement = sqrt(dix * dix + diy * diy)
        var smallestUnsafeDistance = fullDistance
        var candidateMovement = smallestUnsafeMovement
        while ((smallestUnsafeMovement - largestSafeMovement) > 0.1.mm && largestSafeDistance - cr > 0.1.mm) {
            val movementFactor = candidateMovement / totalMovement
            val newX = cx + movementFactor * cvx
            val newY = cy + movementFactor * cvy
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
                if (distanceFactor < 0) distanceFactor = 0.0
                if (distanceFactor > 1) distanceFactor = 1.0
                candidateMovement = largestSafeMovement + distanceFactor * (smallestUnsafeMovement - largestSafeMovement)
            }
        }

        val movementFactor = largestSafeMovement / totalMovement

        val newX = cx + movementFactor * cvx
        val newY = cy + movementFactor * cvy
        outCirclePosition.x = newX
        outCirclePosition.y = newY
        distanceBetweenPointAndLineSegment(newX, newY, lsx, lsy, lslx, lsly, outPointOnLine)
        return true
    }

    fun sweepCircleToCircle(
            x1: Displacement, y1: Displacement, r1: Displacement, vx: Displacement, vy: Displacement,
            x2: Displacement, y2: Displacement, r2: Displacement, outPosition: Position
    ): Boolean {
        if (vx.value.raw == 0 && vy.value.raw == 0) return false

        val r = r1 + r2
        val rSquared = r * r

        val originalDistanceSquared = Position.distanceSquared(x1, y1, x2, y2)
        if (originalDistanceSquared <= rSquared) {
            throw IllegalArgumentException("Circle at ($x1, $y1) with r=$r1 is already inside ($x2, $y2) with r=$r2 since d=$originalDistanceSquared")
        }

        val distanceAtIdealDestinationSquared = Position.distanceSquared(x1 + vx, y1 + vy, x2, y2)

        val closestB = solveClosestPointOnLineToPoint(x2, y2, x1, y1, vx, vy)

        if (distanceAtIdealDestinationSquared > rSquared && (closestB <= 0.0 || closestB >= 1.0)) return false

        // The point that is closest to (x2, y2) and lies on the line through (x1, y1) with direction (vx, vy)
        val closestX = x1 + closestB * vx
        val closestY = y1 + closestB * vy

        val closestDistanceToCenter2Squared = Position.distanceSquared(closestX, closestY, x2, y2)

        // Dirty trick
        if (distanceAtIdealDestinationSquared > rSquared && closestDistanceToCenter2Squared > rSquared * 0.99) return false

        // Rare precision edge case
        if (closestB <= 0.0) {
            outPosition.x = x1
            outPosition.y = y1
            return true
        }

        val originalToClosestDistance = Position.distance(x1, y1, closestX, closestY)
        val originalToIdealDistance = sqrt(vx * vx + vy * vy)
        val requiredDistanceToClosest = sqrt(rSquared - closestDistanceToCenter2Squared)
        var estimatedMaxDistance = min(originalToIdealDistance, originalToClosestDistance - requiredDistanceToClosest)

        var backtrackDistance = 0.1.mm
        var testX: Displacement
        var testY: Displacement

        val invOriginalToIdealDistance = 1.0 / originalToIdealDistance.value.toDouble()
        do {
            if (estimatedMaxDistance.value.raw <= 0) {
                outPosition.x = x1
                outPosition.y = y1
                return true
            }

            val movementFactor = estimatedMaxDistance.value.toDouble() * invOriginalToIdealDistance
            testX = x1 + movementFactor * vx
            testY = y1 + movementFactor * vy

            val testDX = testX - x2
            val testDY = testY - y2
            val testDistance = testDX * testDX + testDY * testDY
            estimatedMaxDistance -= backtrackDistance
            backtrackDistance += 0.1.mm

        } while (testDistance <= rSquared)

        outPosition.x = testX
        outPosition.y = testY

        return true
    }

    fun distanceBetweenLineSegments(
        l1: LineSegment, outPoint1: Position, l2: LineSegment, outPoint2: Position
    ) = distanceBetweenLineSegments(
        l1.startX, l1.startY, l1.lengthX, l1.lengthY, outPoint1,
        l2.startX, l2.startY, l2.lengthX, l2.lengthY, outPoint2
    )

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

    /**
     * Consider the line **l** that goes through the points (lx, ly) and (lx + ldx, ly + ldy).
     * This method computes *b* such that (lx, ly) + b * (ldx, ldy) is
     * the closest point to (px, py) that lies on the line **l**.
     */
    private fun solveClosestPointOnLineToPoint(
        px: Displacement, py: Displacement, lx: Displacement, ly: Displacement,
        ldx: Displacement, ldy: Displacement
    ): Double {
        // Place the point position in the origin and convert the rest to double to obtain maximum precision
        val offsetX = (px - lx).toDouble(DistanceUnit.METER)
        val offsetY = (py - ly).toDouble(DistanceUnit.METER)
        val lengthX = ldx.toDouble(DistanceUnit.METER)
        val lengthY = ldy.toDouble(DistanceUnit.METER)

        // We need to find the intersection with the line through (px, py) perpendicular to l. Use:
        // - perp(endicular)X = ldy
        // - perp(endicular)Y = -ldx

        // Solve the following equations:
        // (1) px + a * perpX = lx + b * ldx
        // (2) py + a * perpY = ly + b * ldy

        // Isolating a gives:
        // (3) a = (lx + b * ldx - px) / perpX = (b * lengthX - offsetX) / perpX
        // (4) a = (ly + b * ldy - py) / perpY = (b * lengthY - offsetY) / perpY

        // Combining (3) and (4) gives:
        // (5) (b * lengthX - offsetX) / perpX = (b * lengthY - offsetY) / perpY

        // Multiplying both sides by perpX * perpY gives:
        // (6) perpY * (b * lengthX - offsetX) = perpX * (b * lengthY - offsetY)

        // Isolate b...
        // (7) b * lengthX * perpY - b * lengthY * perpX = perpY * offsetX - perpX * offsetY
        // (8) b * (lengthX * perpY - lengthY * perpX) = perpY * offsetX - perpX * offsetY
        // (9) b = (perpY * offsetX - perpX * offsetY) / (lengthX * perpY - lengthY * perpX)

        // Replace perpX with lengthY and perpY with -lengthX:
        // (10) b = (-lengthX * offsetX - lengthY * offsetY) / (-lengthX * lengthX - lengthY * lengthY)

        // Simplify...
        // (11) b = (lengthX * offsetX + lengthY * offsetY) / (lengthX * lengthX + lengthY * lengthY)
        return (lengthX * offsetX + lengthY * offsetY) / (lengthX * lengthX + lengthY * lengthY)
    }

    fun findClosestPointOnLineSegmentToPoint(
            px: Displacement, py: Displacement, lx: Displacement, ly: Displacement,
            ldx: Displacement, ldy: Displacement, outPointOnLine: Position
    ) {
        var b = solveClosestPointOnLineToPoint(px, py, lx, ly, ldx, ldy)
        if (b < 0.0) b = 0.0
        if (b > 1.0) b = 1.0

        outPointOnLine.x = lx + b * ldx
        outPointOnLine.y = ly + b * ldy
    }

    internal fun distanceBetweenPointAndLineSegment(
        px: Displacement, py: Displacement, lx: Displacement, ly: Displacement,
        ldx: Displacement, ldy: Displacement, outPointOnLine: Position
    ): Displacement {
        findClosestPointOnLineSegmentToPoint(px, py, lx, ly, ldx, ldy, outPointOnLine)

        val dx = outPointOnLine.x - px
        val dy = outPointOnLine.y - py

        return sqrt(dx * dx + dy * dy)
    }
}
