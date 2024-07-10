package fixie.physics

import fixie.*
import fixie.geometry.LineSegment
import fixie.geometry.Position
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class TestMargin {

    private fun assertEquals(a: Displacement, b: Displacement, maxError: Displacement) {
        if (abs(a - b) > maxError) assertEquals(a, b)
    }

    private fun assertEquals(a: Position, b: Position, maxError: Displacement) {
        if (a.distance(b.x, b.y) > maxError) assertEquals(a, b)
    }

    private fun dummyEntity(radius: Displacement, x: Displacement, y: Displacement): Entity {
        val entity = Entity(
                properties = EntityProperties(radius = radius),
                position = Position(x, y),
                velocity = Velocity.zero(),
                angle = 0.degrees
        )
        entity.wipPosition.x = x
        entity.wipPosition.y = y
        return entity
    }

    private fun dummyTile(startX: Displacement, startY: Displacement, lengthX: Displacement, lengthY: Displacement) = Tile(
            collider = LineSegment(startX, startY, lengthX, lengthY), properties = TileProperties()
    )

    @Test
    fun testWithoutOthers() {
        val position = Position(12.mm, 13.mm)

        assertFalse(createMargin(position, 100.mm, emptyList(), emptyList(), 1.mm))

        assertEquals(Position(12.mm, 13.mm), position, 0.mm)
    }

    @Test
    fun testWith1CloseEntity() {
        val position = Position(5.m, 3000.1.mm)
        val other = dummyEntity(2.m, 5.m, 0.m)

        createMargin(position, 1.m, listOf(other), emptyList(), 1.mm)

        assertEquals(Position(5.m, 3001.mm), position, 0.2.mm)
    }

    @Test
    fun testWith2CloseEntitiesOnTheLeft() {
        val position = Position(700.mm, 0.m)
        val upperLeft = dummyEntity(500.mm, 0.m, 714.mm)
        val lowerLeft = dummyEntity(500.mm, 0.m, -714.mm)

        createMargin(position, 500.mm, listOf(upperLeft, lowerLeft), emptyList(), 1.mm)

        assertEquals(1001.mm, position.distance(0.m, 714.mm), 0.5.mm)
    }

    @Test
    fun testEntityStack() {
        val position = Position.origin()
        val upper = dummyEntity(1.m, 0.m, 2001.mm)
        val lower = dummyEntity(1.m, 0.m, -2.m)

        createMargin(position, 1.m, listOf(upper, lower), emptyList(), 1.mm)

        assertEquals(Position(0.m, 0.5.mm), position, 0.3.mm)
    }

    @Test
    fun testNarrowTunnel() {
        val position = Position.origin()
        val upper = dummyTile(-10.m, 1001.mm, 20.m, 0.m)
        val lower = dummyTile(-10.m, -1.m, 20.m, 0.m)

        createMargin(position, 1.m, emptyList(), listOf(upper, lower), 1.mm)

        assertEquals(Position(0.m, 0.5.mm), position, 0.3.mm)
    }
}
