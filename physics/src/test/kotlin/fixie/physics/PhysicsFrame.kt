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
import java.lang.System.nanoTime
import java.util.*
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants.DISPOSE_ON_CLOSE
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.DurationUnit

private fun simpleSplitScene(playerProperties: EntityProperties): Pair<Scene, UUID> {
    val scene = Scene()

    val spawnPlayer = EntitySpawnRequest(x = 0.m, y = 1.5.m, properties = playerProperties)
    scene.spawnEntity(spawnPlayer)
    scene.update(Duration.ZERO)

    val length = 10.m

    scene.addTile(TilePlaceRequest(LineSegment(
            startX = -length, startY = 0.m, lengthX = length, lengthY = -length
    ), TileProperties()))
    scene.addTile(TilePlaceRequest(LineSegment(
            startX = length, startY = 0.m, lengthX = -length, lengthY = -length
    ), TileProperties()))

    for (counter in -5 .. 5) {
        scene.spawnEntity(EntitySpawnRequest(
                x = counter.m, y = 0.m, properties = EntityProperties(radius = 0.2.m)
        ))
        scene.spawnEntity(EntitySpawnRequest(
                x = counter.m, y = 0.4.m, properties = EntityProperties(radius = 0.1.m)
        ))
        scene.spawnEntity(EntitySpawnRequest(
                x = counter.m, y = 0.9.m, properties = EntityProperties(radius = 0.3.m)
        ))
    }

    return Pair(scene, spawnPlayer.id!!)
}

private fun impulseTestScene(playerProperties: EntityProperties): Pair<Scene, UUID> {
    val scene = Scene()

    val spawnPlayer = EntitySpawnRequest(x = 0.m, y = 1.5.m, properties = playerProperties)
    scene.spawnEntity(spawnPlayer)
    scene.update(Duration.ZERO)

    val length = 3.m

    scene.addTile(TilePlaceRequest(LineSegment(
        startX = -length, startY = 0.m, lengthX = 2 * length, lengthY = 0.m
    ), TileProperties()))
    scene.addTile(TilePlaceRequest(LineSegment(
        startX = -length, startY = 0.m, lengthX = -length / 2, lengthY = length
    ), TileProperties()))
    scene.addTile(TilePlaceRequest(LineSegment(
        startX = length, startY = 0.m, lengthX = length / 2, lengthY = length
    ), TileProperties()))

    scene.spawnEntity(EntitySpawnRequest(
        x = length / 2, y = 2.m, properties = EntityProperties(radius = 1.m)
    ))
    scene.spawnEntity(EntitySpawnRequest(
            x = -length / 2, y = 2.m, properties = EntityProperties(radius = 20.mm)
    ))

    return Pair(scene, spawnPlayer.id!!)
}

private fun randomBusyScene(playerProperties: EntityProperties): Pair<Scene, UUID> {
    val scene = Scene()

    val spawnPlayer = EntitySpawnRequest(x = 0.m, y = 2.m, properties = playerProperties)
    scene.spawnEntity(spawnPlayer)
    scene.update(Duration.ZERO)

    val rng = Random(1234)
    val simpleMaterial = TileProperties()

    for (counter in 0 until 10_000) {
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

    return Pair(scene, spawnPlayer.id!!)
}

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

    val lastPlayerPosition = Position.origin()
    val playerProperties = EntityProperties(
            radius = 0.1.m,
            updateFunction = { position, velocity ->
                if (moveLeft) velocity.x -= 5.mps * Scene.STEP_DURATION.toDouble(DurationUnit.SECONDS)
                if (moveRight) velocity.x += 5.mps * Scene.STEP_DURATION.toDouble(DurationUnit.SECONDS)
                if (shouldJump) {
                    velocity.y += 4.mps
                    shouldJump = false
                }
                lastPlayerPosition.x = position.x
                lastPlayerPosition.y = position.y
            }
    )

    val (scene, playerID) = simpleSplitScene(playerProperties)

    val panel = PhysicsPanel(scene, lastPlayerPosition, playerID)
    val frame = JFrame()
    frame.setSize(1200, 800)
    frame.isVisible = true
    frame.defaultCloseOperation = DISPOSE_ON_CLOSE
    frame.addKeyListener(PlayerControls())
    frame.add(panel)

    val updateCounter = UpdateCounter()
    val updateThread = Thread(UpdateLoop({ updateLoop ->
        updateCounter.increment()
        val startUpdateTime = nanoTime()
        scene.update(Scene.STEP_DURATION)
        val finishUpdateTime = nanoTime()
        panel.lastUpdateTime = (startUpdateTime + finishUpdateTime) / 2
        if (!frame.isDisplayable) {
            updateLoop.stop()
            panel.storage.getThreadStorage(Thread.currentThread().id).print(System.out, 60, 1.0)
        }
        if (Math.random() < 0.01) println("UPS is ${updateCounter.value}")
    }, Scene.STEP_DURATION.inWholeNanoseconds))
    updateThread.start()

    UpdateLoop({ renderLoop ->
        if (!updateThread.isAlive) frame.dispose()
        frame.repaint()
        if (!frame.isDisplayable) {
            renderLoop.stop()
            panel.profiler.stop()
            //panel.storage.getThreadStorage(panel.threadID).print(System.out, 60, 1.0)
        }
    }, 16_666_667L).start()
}

class PhysicsPanel(private val scene: Scene, private val playerPosition: Position, private val playerID: UUID) : JPanel() {

    private val sceneQuery = SceneQuery()
    private val counter = UpdateCounter()
    var lastUpdateTime = 0L

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
        val startTime = nanoTime()

        var extrapolationTime = 0.nanoseconds
        if (lastUpdateTime > 0L) extrapolationTime = (startTime - lastUpdateTime).nanoseconds

        val extrapolationFactor = min(1.0, extrapolationTime / Scene.STEP_DURATION)

        counter.increment()
        val width = this.width
        val height = this.height
        g!!.color = Color.WHITE
        g.fillRect(0, 0, width, height)

        val viewDistance = 5.m
        scene.read(
                sceneQuery, playerPosition.x - viewDistance, playerPosition.y - viewDistance,
                playerPosition.x + viewDistance, playerPosition.y + viewDistance
        )

        val miniScene = Scene()
        val miniPlayerProperties = EntityProperties(radius = 100.mm)
        for (index in 0 until sceneQuery.entities.size) {
            val entity = sceneQuery.entities[index]
            miniScene.spawnEntity(EntitySpawnRequest(
                    entity.position.x, entity.position.y, if (entity.id == playerID) miniPlayerProperties else entity.properties,
                    entity.velocity.x, entity.velocity.y, entity.angle
            ))
        }
        for (index in 0 until sceneQuery.tiles.size) {
            val tile = sceneQuery.tiles[index]
            miniScene.addTile(TilePlaceRequest(collider = tile.collider, properties = tile.properties))
        }
        miniScene.update(Scene.STEP_DURATION)

        val miniQuery = SceneQuery()
        miniScene.read(
                miniQuery, playerPosition.x - viewDistance, playerPosition.y - viewDistance,
                playerPosition.x + viewDistance, playerPosition.y + viewDistance
        )

        var playerPosition = playerPosition
        for (index in 0 until sceneQuery.entities.size) {
            val entity = sceneQuery.entities[index]
            for (miniIndex in 0 until miniQuery.entities.size) {
                val miniEntity = miniQuery.entities[miniIndex]

                if (miniEntity.properties === entity.properties || (entity.id == playerID && miniEntity.properties == miniPlayerProperties)) {
                    entity.position.x = (1.0 - extrapolationFactor) * entity.position.x + extrapolationFactor * miniEntity.position.x
                    entity.position.y = (1.0 - extrapolationFactor) * entity.position.y + extrapolationFactor * miniEntity.position.y
                    break
                }
            }
            if (entity.id == playerID) playerPosition = entity.position
        }

        fun transformX(x: Displacement) = width / 2 + (200 * (x - playerPosition.x).toDouble(DistanceUnit.METER)).roundToInt()

        fun transformY(y: Displacement) = height / 2 - (200 * (y - playerPosition.y).toDouble(DistanceUnit.METER)).roundToInt()

        g.color = Color.BLACK
        for (index in 0 until sceneQuery.tiles.size) {
            val tile = sceneQuery.tiles[index]
            val startX = transformX(tile.collider.startX)
            val startY = transformY(tile.collider.startY)
            val endX = transformX(tile.collider.startX + tile.collider.lengthX)
            val endY = transformY(tile.collider.startY + tile.collider.lengthY)
            g.drawLine(startX, startY, endX, endY)
        }
        for (index in 0 until sceneQuery.entities.size) {
            val entity = sceneQuery.entities[index]
            val minX = transformX(entity.position.x - entity.properties.radius)
            val minY = transformY(entity.position.y + entity.properties.radius)
            val maxX = transformX(entity.position.x + entity.properties.radius)
            val maxY = transformY(entity.position.y - entity.properties.radius)

            fun toColorValue(x: Speed) = min(255, abs(30 * x.toDouble(SpeedUnit.METERS_PER_SECOND)).roundToInt())
            var blue = 0
            if (entity.id == playerID) blue = 100

            g.color = Color(toColorValue(entity.velocity.x), toColorValue(entity.velocity.y), blue)
            g.fillOval(minX, minY, maxX - minX, maxY - minY)

            val spotOffset = 0.5f * entity.properties.radius
            val spotRadius = 0.3f * entity.properties.radius
            val spotX = entity.position.x + spotOffset * cos(entity.angle)
            val spotY = entity.position.y + spotOffset * sin(entity.angle)

            val minSpotX = transformX(spotX - spotRadius)
            val minSpotY = transformY(spotY + spotRadius)
            val maxSpotX = transformX(spotX + spotRadius)
            val maxSpotY = transformY(spotY - spotRadius)

            g.color = Color.PINK
            g.fillOval(minSpotX, minSpotY, maxSpotX - minSpotX, maxSpotY - minSpotY)
        }

        if (Math.random() < 0.01) {
            println("FPS is ${counter.value}")
            println("Took ${(nanoTime() - startTime) / 1000}us")
        }

        //profiler.isPaused = true
        Toolkit.getDefaultToolkit().sync()
    }
}
