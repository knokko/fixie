package fixie.physics

import fixie.*
import fixie.geometry.LineSegment
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.random.Random

class TestTileTree {

    private val material = TileProperties()

    @Test
    fun testBigInsertionsDoNotCrash() {
        val tree = TileTree(minX = -1.m, minY = -1.m, maxX = 1.m, maxY = 1.m)
        tree.insert(Tile(LineSegment(startX = -2.m, startY = -2.m, lengthX = 5.m, lengthY = 5.m), material))
        tree.insert(Tile(LineSegment(startX = 0.m, startY = -2.m, lengthX = 0.m, lengthY = 5.m), material))
        tree.insert(Tile(LineSegment(startX = -2.m, startY = 0.m, lengthX = 5.m, lengthY = 0.m), material))
    }

    @Test
    fun testRandomInsertionPerformance() {
        val tree = TileTree(minX = -1.m, minY = -1.m, maxX = 1.m, maxY = 1.m)
        val rng = Random(1234)

        fun next() = rng.nextInt(-1000, 1000).mm

        val startTime = System.nanoTime()
        for (counter in 0 until 100_000) {
            tree.insert(Tile(LineSegment(
                    startX = next(),
                    startY = next(),
                    lengthX = next(),
                    lengthY = next()
            ), material))
        }
        val took = System.nanoTime() - startTime;
        if (took > 5_000_000_000) {
            throw AssertionError("Expected to finish within 5 seconds, but took ${took / 1_000_000_000L} seconds")
        }
    }

    @Test
    fun testStackOverflowRegression() {
        // This unit test only checks that inserting tiles won't crash or hang
        val tree = TileTree(minX = -10.km, minY = -10.km, maxX = 10.km, maxY = 10.km)
        for (counter in 0 until 51) {
            tree.insert(Tile(LineSegment(counter.m, counter.m, counter.m, counter.m), material))
        }
        tree.insert(Tile(LineSegment(-97.697.m, 33.817.m, 0.331.m, 0.169.m), material))
    }

    @Test
    fun testRandomIntersectionCorrectness() {
        val rng = Random(1234)
        val tree = TileTree(minX = -10.km, minY = -10.km, maxX = 10.km, maxY = 10.km)

        fun next() = (rng.nextInt(-100, 100) * rng.nextInt(-100, 100) * rng.nextInt(-100, 100)).mm

        val tiles = Array(1000) {
            Tile(LineSegment(
                    startX = next(), startY = next(),
                    lengthX = next(), lengthY = next()
            ), material)
        }

        fun check(tile: Tile) {

            fun check(minX: Displacement, minY: Displacement, maxX: Displacement, maxY: Displacement) {
                val testList = mutableListOf<Tile>()
                tree.query(minX, minY, maxX, maxY, testList)
                assertTrue(testList.contains(tile))
            }

            val ls = tile.collider
            check(ls.minX, ls.minY, ls.maxX, ls.maxY)
            check(ls.minX - 1.mm, ls.minY - 1.mm, ls.maxX + 1.mm, ls.maxY + 1.mm)

            val midX = (ls.minX + ls.maxX) / 2
            val midY = (ls.minY + ls.maxY) / 2
            check(midX, midY, ls.maxX, ls.maxY)
            check(ls.minX, ls.minY, midX, midY)
            check((midX + ls.minX) / 2, (midY + ls.minY) / 2, (midX + ls.maxX) / 2, (midY + ls.maxY) / 2)

            check(ls.minX - 1.m, ls.minY - 1.m, ls.minX, ls.minY)
            check(ls.maxX, ls.maxY, ls.maxX + 1.m, ls.maxY + 1.m)
        }

        for (tile in tiles) {
            tree.insert(tile)
            check(tile)
        }

        for (tile in tiles) check(tile)
    }
}
