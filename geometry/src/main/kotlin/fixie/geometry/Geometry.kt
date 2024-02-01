package fixie.geometry

import fixie.*

object Geometry {

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

        var largestSafeMovement = 0.m
        var smallestUnsafeMovement = totalMovement + (fullDistance - cr) * 0.99
        var candidateMovement = smallestUnsafeMovement
        while ((smallestUnsafeMovement - largestSafeMovement) > 0.1.mm) {
            val movementFactor = candidateMovement / totalMovement
            val distance = distanceBetweenPointAndLineSegment(
                cx + movementFactor * cvx, cy + movementFactor * cvy,
                lsx, lsy, lslx, lsly, outPointOnLine
            )

            if (distance > cr) {
                // TODO Update candidateMovement
                if (largestSafeMovement < candidateMovement) largestSafeMovement = candidateMovement
            } else {
                // TODO Update candidateMovement
                if (smallestUnsafeMovement > candidateMovement) smallestUnsafeMovement = candidateMovement
            }
            candidateMovement = (largestSafeMovement + smallestUnsafeMovement) / 2
        }

        val movementFactor = largestSafeMovement / totalMovement

        val newX = cx + movementFactor * cvx
        val newY = cy + movementFactor * cvy
        outCirclePosition.x = newX
        outCirclePosition.y = newY
        distanceBetweenPointAndLineSegment(newX, newY, lsx, lsy, lslx, lsly, outPointOnLine)
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

    fun distanceBetweenPointAndLineSegment(
        px: Displacement, py: Displacement, lx: Displacement, ly: Displacement,
        ldx: Displacement, ldy: Displacement, outPointOnLine: Position
    ): Displacement {
        // First, we need to find the intersection with the line through (px, py) perpendicular to l. Use:
        // - perp(endicular)X = ldy
        // - perp(endicular)Y = -ldx

        // Solve the following equations:
        // (1) px + a * perpX = lx + b * ldx
        // (2) py + a * perpY = ly + b * ldy

        // Isolating a gives:
        // (3) a = (lx + b * ldx - px) / perpX
        // (4) a = (ly + b * ldy - py) / perpY

        // Combining (3) and (4) gives:
        // (5) (lx + b * ldx - px) / perpX = (ly + b * ldy - py) / perpY

        // Multiplying both sides by perpX * perpY gives:
        // (6) perpY * (lx + b * ldx - px) = perpX * (ly + b * ldy - py)

        // Isolate b...
        // (7) -b * ldy * perpX + b * ldx * perpY = -lx * perpY + px * perpY + ly * perpX - py * perpX
        // (8) b * (ldx * perpY - ldy * perpX) = ...
        // (9) b = ... / (ldx * perpY - ldy * perpX)

        // Replace perpX with ldy and perpY with -ldx:
        // (10) b = (lx * ldx - px * ldx + ly * ldy - py * ldy) / (-ldx * ldx - ldy * ldy)

        // Simplify...
        // (11) b = ((lx - px) * ldx + (ly - py) * ldy) / (-ldx * ldx - ldy * ldy)
        // (12) b = ((px - lx) * ldx + (py - ly) * ldy) / (ldx * ldx + ldy * ldy)

        // Unfortunately, we also need to handle potential overflow and division by 0, as well as loss of precision
        val largestLength = max(abs(ldx / 1.m), abs(ldy / 1.m))
        val threshold = 3
        val factor = if (largestLength > threshold) threshold / largestLength else FixDisplacement.ONE

        val smallLdx = ldx * factor
        val smallLdy = ldy * factor

        val numerator = ((px - lx) * smallLdx + (py - ly) * smallLdy)
        val denominator = (smallLdx * smallLdx + smallLdy * smallLdy)

        val b = if (denominator.raw != 0.0) {
            val rawB = numerator.raw / denominator.raw
            // If b is not between 0 and 1, the closest point is not on the line segment
            if (rawB < 0) FixDisplacement.ZERO
            else if (rawB * factor.toDouble() > 1) FixDisplacement.ONE / factor
            else FixDisplacement.from(rawB)
        } else FixDisplacement.ZERO // When the denominator is 0, the length of the line is (nearly) 0, so b doesn't matter

        val x = lx + smallLdx * b
        val y = ly + smallLdy * b

        outPointOnLine.x = x
        outPointOnLine.y = y

        return sqrt((x - px) * (x - px) + (y - py) * (y - py))
    }
}
