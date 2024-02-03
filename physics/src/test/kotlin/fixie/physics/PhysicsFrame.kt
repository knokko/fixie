package fixie.physics

import fixie.*
import fixie.geometry.LineSegment
import java.awt.Color
import java.awt.Graphics
import java.awt.Toolkit
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.*
import java.awt.event.KeyListener
import java.lang.Thread.sleep
import javax.swing.JFrame
import javax.swing.WindowConstants.DISPOSE_ON_CLOSE
import kotlin.math.roundToInt

var lastX = 0.m
var lastY = 0.m

fun main() {
    var moveLeft = false
    var moveRight = false
    var shouldJump = false

    class PlayerControls : KeyListener {
        override fun keyTyped(e: KeyEvent?) {}

        override fun keyPressed(e: KeyEvent?) {
            if (e!!.keyCode == VK_LEFT || e.keyCode == VK_A) moveLeft = true
            if (e.keyCode == VK_RIGHT || e.keyCode == VK_D) moveRight = true
            if (e.keyCode == VK_SPACE || e.keyCode == VK_W || e.keyCode == VK_UP) shouldJump = true
        }

        override fun keyReleased(e: KeyEvent?) {
            if (e!!.keyCode == VK_LEFT || e.keyCode == VK_A) moveLeft = false
            if (e.keyCode == VK_RIGHT || e.keyCode == VK_D) moveRight = false
            if (e.keyCode == VK_SPACE || e.keyCode == VK_W || e.keyCode == VK_UP) shouldJump = false
        }
    }

    val scene = Scene()
    scene.spawnEntity(EntityProperties(
        radius = 0.1.m,
        updateFunction = { position, velocity ->
            if (moveLeft) velocity.x -= 100.mm
            if (moveRight) velocity.x += 100.mm
            if (shouldJump) {
                velocity.y += 4.m
                shouldJump = false
            }
            lastX = position.x
            lastY = position.y
        }
    ), 0.m, 2.m, 1200.mm, 0.m)

    val simpleMaterial = TileProperties()
    scene.addTile(LineSegment(-20.m, 0.m, 100.m, 0.m), simpleMaterial)
    scene.addTile(LineSegment(2.m, 0.3.m, 3.m, 0.m), simpleMaterial)
    scene.addTile(LineSegment(7.m, 0.m, 1.m, 4.m), simpleMaterial)
    scene.addTile(LineSegment(-1.m, 0.m, 0.m, 2.m), simpleMaterial)

    val frame = PhysicsFrame(scene)
    frame.setSize(1200, 800)
    frame.isVisible = true
    frame.defaultCloseOperation = DISPOSE_ON_CLOSE
    frame.addKeyListener(PlayerControls())

    while (frame.isDisplayable) {
        scene.update(20)
        frame.repaint()
        sleep(20)
    }
}

class PhysicsFrame(private val scene: Scene) : JFrame() {

    private val sceneQuery = SceneQuery()

    override fun paint(g: Graphics?) {
        super.paint(g)

        val width = this.width
        val height = this.height
        g!!.color = Color.WHITE
        g.fillRect(0, 0, width, height)

        scene.read(sceneQuery)

        fun transformX(x: Displacement) = 300 + (200 * (x - lastX).toDouble(DistanceUnit.METER)).roundToInt()

        fun transformY(y: Displacement) = 400 - (200 * (y - lastY).toDouble(DistanceUnit.METER)).roundToInt()

        g.color = Color.BLACK
        for (index in 0 until sceneQuery.numTiles) {
            val tile = sceneQuery.tiles[index]!!
            g.drawLine(
                transformX(tile.collider.startX), transformY(tile.collider.startY),
                transformX(tile.collider.startX + tile.collider.lengthX), transformY(tile.collider.startY + tile.collider.lengthY)
            )
        }
        for (index in 0 until sceneQuery.numEntities) {
            val entity = sceneQuery.entities[index]
            val minX = transformX(entity.position.x - entity.properties.radius)
            val minY = transformY(entity.position.y + entity.properties.radius)
            val maxX = transformX(entity.position.x + entity.properties.radius)
            val maxY = transformY(entity.position.y - entity.properties.radius)

            g.fillOval(minX, minY, maxX - minX, maxY - minY)
        }

        Toolkit.getDefaultToolkit().sync()
    }
}
