package fixie.physics

import com.github.knokko.profiler.SampleProfiler
import com.github.knokko.profiler.storage.FrequencyThreadStorage
import com.github.knokko.profiler.storage.SampleStorage
import com.github.knokko.update.UpdateCounter
import com.github.knokko.update.UpdateLoop
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
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants.DISPOSE_ON_CLOSE
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

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
            if (moveLeft) velocity.x -= 0.02.mps
            if (moveRight) velocity.x += 0.02.mps
            if (shouldJump) {
                velocity.y += 4.mps
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

    scene.update(Duration.ZERO)

    scene.spawnEntity(EntitySpawnRequest(x = 1.m, y = 2.m, properties = EntityProperties(radius = 100.mm)))

    val rng = Random.Default
    for (counter in 0 until 10) {
        scene.spawnEntity(EntitySpawnRequest(
            x = rng.nextInt(-10_000, 10_000).mm,
            y = rng.nextInt(-10_000, 10_000).mm,
            properties = EntityProperties(radius = rng.nextInt(20, 300).mm)
        ))
    }

    val panel = PhysicsPanel(scene, spawnPlayer)
    val frame = JFrame()
    frame.setSize(1200, 800)
    frame.isVisible = true
    frame.defaultCloseOperation = DISPOSE_ON_CLOSE
    frame.addKeyListener(PlayerControls())
    frame.add(panel)

    val updateCounter = UpdateCounter()
    Thread(UpdateLoop({ updateLoop ->
        updateCounter.increment()
        scene.update(20.milliseconds)
        if (!frame.isDisplayable) updateLoop.stop()
        if (Math.random() < 0.01) println("UPS is ${updateCounter.value}")
    }, 20_000_000L)).start()

    UpdateLoop({ renderLoop ->
        frame.repaint()
        if (!frame.isDisplayable) {
            renderLoop.stop()
            //panel.storage.getThreadStorage(panel.threadID).print(System.out, 60, 1.0)
        }
    }, 16_666_667L).start()
}

class PhysicsPanel(private val scene: Scene, private val player: EntitySpawnRequest) : JPanel() {

    private val sceneQuery = SceneQuery()
    private val counter = UpdateCounter()

    val storage: SampleStorage<FrequencyThreadStorage> = SampleStorage.frequency()
    private val profiler = SampleProfiler(storage)
    var threadID = 0L

    init {
        profiler.sleepTime = 0
        profiler.start()
    }

    override fun paint(g: Graphics?) {
        profiler.isPaused = false
        threadID = Thread.currentThread().id
        val startTime = System.nanoTime()
        counter.increment()
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

        if (Math.random() < 0.01) {
            println("FPS is ${counter.value}")
            println("Took ${(System.nanoTime() - startTime) / 1000}us")
        }

        profiler.isPaused = true
        Toolkit.getDefaultToolkit().sync()
    }
}
