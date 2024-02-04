package fixie.physics

import fixie.*
import fixie.geometry.LineSegment
import fixie.geometry.Position
import java.awt.Color
import java.awt.Graphics
import java.awt.Toolkit
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.*
import java.awt.event.KeyListener
import java.lang.RuntimeException
import java.lang.Thread.sleep
import javax.swing.JFrame
import javax.swing.WindowConstants.DISPOSE_ON_CLOSE
import kotlin.math.roundToInt
import kotlin.random.Random

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
    val playerProperties = EntityProperties(
        radius = 0.1.m,
        updateFunction = { _, velocity ->
            if (moveLeft) velocity.x -= 100.mm
            if (moveRight) velocity.x += 100.mm
            if (shouldJump) {
                velocity.y += 4.m
                shouldJump = false
            }
        }
    )

    val spawnPlayer = EntitySpawnRequest(x = 0.m, y = 2.m, properties = playerProperties)
    scene.spawnEntity(spawnPlayer)

    val simpleMaterial = TileProperties()
    scene.addTile(TilePlaceRequest(LineSegment(-20.m, 0.m, 100.m, 0.m), simpleMaterial))
    scene.addTile(TilePlaceRequest(LineSegment(2.m, 0.3.m, 3.m, 0.m), simpleMaterial))
    scene.addTile(TilePlaceRequest(LineSegment(7.m, 0.m, 1.m, 4.m), simpleMaterial))
    scene.addTile(TilePlaceRequest(LineSegment(-1.m, 0.m, 0.m, 2.m), simpleMaterial))

    scene.update(0)

    scene.spawnEntity(EntitySpawnRequest(x = 1.m, y = 2.m, properties = EntityProperties(radius = 100.mm)))

    val rng = Random.Default
    for (counter in 0 until 10) {
        scene.spawnEntity(EntitySpawnRequest(
            x = rng.nextInt(-10_000, 10_000).mm,
            y = rng.nextInt(-10_000, 10_000).mm,
            properties = EntityProperties(radius = rng.nextInt(20, 300).mm)
        ))
    }

    val frame = PhysicsFrame(scene, spawnPlayer)
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

class PhysicsFrame(private val scene: Scene, private val player: EntitySpawnRequest) : JFrame() {

    private val sceneQuery = SceneQuery()

    override fun paint(g: Graphics?) {
        super.paint(g)

        val width = this.width
        val height = this.height
        g!!.color = Color.WHITE
        g.fillRect(0, 0, width, height)

        if (!player.processed) return
        val playerID = player.id!!

        scene.read(sceneQuery)

        var playerPosition: Position? = null
        for (index in 0 until sceneQuery.numEntities) {
            if (sceneQuery.entities[index].id == playerID) playerPosition = sceneQuery.entities[index].position
        }

        if (playerPosition == null) throw RuntimeException("Can't find player position")

        fun transformX(x: Displacement) = 300 + (200 * (x - playerPosition.x).toDouble(DistanceUnit.METER)).roundToInt()

        fun transformY(y: Displacement) = 400 - (200 * (y - playerPosition.y).toDouble(DistanceUnit.METER)).roundToInt()

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

            if (entity.id == playerID) g.color = Color.BLUE
            else g.color = Color.BLACK
            g.fillOval(minX, minY, maxX - minX, maxY - minY)
        }

        Toolkit.getDefaultToolkit().sync()
    }
}
