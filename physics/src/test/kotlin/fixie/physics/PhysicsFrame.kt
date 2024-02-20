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
import kotlin.math.max
import kotlin.math.min
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
            if (moveLeft) velocity.x -= 0.05.mps
            if (moveRight) velocity.x += 0.05.mps
            if (shouldJump) {
                velocity.y += 4.mps
                shouldJump = false
            }
        }
    )

    val spawnPlayer = EntitySpawnRequest(x = 0.m, y = 2.m, properties = playerProperties)
    scene.spawnEntity(spawnPlayer)
    scene.update(Duration.ZERO)

    val rng = Random(1234)
    val simpleMaterial = TileProperties()

    for (counter in 0 until 100_000) {
        scene.addTile(TilePlaceRequest(LineSegment(
            startX = rng.nextInt(-100_000, 100_000).mm,
            startY = rng.nextInt(-100_000, 100_000).mm,
            lengthX = rng.nextInt(100, 1_000).mm,
            lengthY = rng.nextInt(100, 1_000).mm,
        ), simpleMaterial))
    }
    scene.addTile(TilePlaceRequest(LineSegment(
        startX = -100.m, startY = -100.m,
        lengthX = 200.m, lengthY = 0.m
    ), simpleMaterial))
    scene.addTile(TilePlaceRequest(LineSegment(
        startX = -100.m, startY = -100.m,
        lengthX = 0.m, lengthY = 2000.m
    ), simpleMaterial))
    scene.addTile(TilePlaceRequest(LineSegment(
        startX = -100.m, startY = 100.m,
        lengthX = 200.m, lengthY = 0.m
    ), simpleMaterial))
    scene.addTile(TilePlaceRequest(LineSegment(
        startX = 100.m, startY = -100.m,
        lengthX = 0.m, lengthY = 2000.m
    ), simpleMaterial))
    scene.update(Duration.ZERO)

    for (counter in 0 until 3000) {
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
        if (!frame.isDisplayable) {
            updateLoop.stop()
            panel.storage.getThreadStorage(Thread.currentThread().id).print(System.out, 60, 1.0)
        }
        if (Math.random() < 0.01) println("UPS is ${updateCounter.value}")
    }, 20_000_000L)).start()

    UpdateLoop({ renderLoop ->
        frame.repaint()
        if (!frame.isDisplayable) {
            renderLoop.stop()
            panel.profiler.stop()
            //panel.storage.getThreadStorage(panel.threadID).print(System.out, 60, 1.0)
        }
    }, 16_666_667L).start()
}

class PhysicsPanel(private val scene: Scene, private val player: EntitySpawnRequest) : JPanel() {

    private val sceneQuery = SceneQuery()
    private val counter = UpdateCounter()

    val storage: SampleStorage<FrequencyThreadStorage> = SampleStorage.frequency()
    val profiler = SampleProfiler(storage)
    var threadID = 0L

    init {
        profiler.sleepTime = 1
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
            val startX = transformX(tile.collider.startX)
            val startY = transformY(tile.collider.startY)
            val endX = transformX(tile.collider.startX + tile.collider.lengthX)
            val endY = transformY(tile.collider.startY + tile.collider.lengthY)
            val minX = min(startX, endX)
            val minY = min(startY, endY)
            val maxX = max(startX, endX)
            val maxY = max(startY, endY)
            if (maxX >= 0 && maxY >= 0 && minX <= width && minY <= height) {
                g.drawLine(startX, startY, endX, endY)
            }
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

        //profiler.isPaused = true
        Toolkit.getDefaultToolkit().sync()
    }
}
