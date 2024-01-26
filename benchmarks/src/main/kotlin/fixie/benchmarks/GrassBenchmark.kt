package fixie.benchmarks

import com.github.knokko.profiler.SampleProfiler
import com.github.knokko.profiler.storage.SampleStorage
import fixie.*
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.*
import kotlin.random.Random

private typealias Num = Float
private typealias NumArray = FloatArray
private val ONE = 1.0f

private val TWO_PI = ONE * 2.0f * PI.toFloat()
private val MAX_VERT_ANGLE = ONE * 70.0f * PI.toFloat() / 180.0f

fun main() {
    val texture = Texture(9000, 9000)

    val storage = SampleStorage.frequency()
    val profiler = SampleProfiler(storage)
    profiler.sleepTime = 1
    profiler.start()

    drawGrass(texture, 1)

    profiler.stop()
    storage.getThreadStorage(Thread.currentThread().id).print(System.out, 5, 0.1)

    val image = BufferedImage(texture.width, texture.height, TYPE_INT_RGB)
    for (y in 0 until texture.height) {
        for (x in 0 until texture.width) {
            val index = x + y * texture.width
            image.setRGB(x, y, Color(texture.red[index].toFloat(), texture.green[index].toFloat(), texture.blue[index].toFloat()).rgb)
        }
    }

    ImageIO.write(image, "PNG", File("grass.png"))
}

private class Texture(val width: Int, val height: Int) {
    val red = NumArray(width * height)
    val green = NumArray(width * height)
    val blue = NumArray(width * height)

    fun fill(r: Num, g: Num, b: Num) {
        red.fill(r)
        green.fill(g)
        blue.fill(b)
    }

    fun setPixel(x: Int, y: Int, r: Num, g: Num, b: Num) {
        val index = x + width * y
        red[index] = r
        green[index] = g
        blue[index] = b
    }
}

private fun drawGrass(texture: Texture, scale: Int = 1, rand: Random = Random.Default) {

    val redBase = ONE * 0.03f
    val greenBase = ONE * 0.3f
    val blueBase = ONE * 0.05f

    val redBright = ONE * 0.15f
    val greenBright = ONE * 0.9f
    val blueBright = ONE * 0.25f

    val backgroundRed = ONE * 0.01f
    val backgroundGreen = ONE * 0.01f
    val backgroundBlue = ONE * 0.01f

    val startTime = System.currentTimeMillis()
    texture.fill(backgroundRed, backgroundGreen, backgroundBlue)

    val maxX = texture.width - 1
    val maxY = texture.height - 1

    val redLeft = redBright - redBase
    val greenLeft = greenBright - greenBase
    val blueLeft = blueBright - blueBase

    val area = texture.width * texture.height

    val heightTable = NumArray(area)

    val numGrassLines = (area / 70 / (scale * scale))
    for (counter in 0 until numGrassLines){
        val startX = rand.nextInt(texture.width)
        val startY = rand.nextInt(texture.height)
        val angle = rand.nextFloat() * TWO_PI

        val vertAngle = rand.nextFloat() * MAX_VERT_ANGLE
        val sinVertAngle = sin(vertAngle.toFloat())

        val length = ONE * (50.0f + rand.nextFloat() * 30.0f) * sinVertAngle * scale
        //val invLength = ONE / length

        val cosVertAngleTimesLength = cos(vertAngle.toFloat()) * length
        val sinAngle = sin(angle.toFloat())
        val cosAngle = cos(angle.toFloat())

        val width = ONE * (4.0f + 3.0f * rand.nextFloat()) * scale

        val endX = startX + (cosAngle * length).toInt()
        val endY = startY + (sinAngle * length).toInt()

        val widthX = (sinAngle * width).toInt()
        val widthY = (cosAngle * width).toInt()

        // The line through these coordinates will be perpendicular to angle and go through (startX,startY)
        val startX1 = startX - widthX
        val startY1 = startY + widthY
        val startX2 = startX + widthX
        val startY2 = startY - widthY

        // The next variables will make it easier to loop
        val localMinX = min(min(startX1, startX2), endX)
        val localMinY = min(min(startY1, startY2), endY)
        val localMaxX = max(max(startX1, startX2), endX)
        val localMaxY = max(max(startY1, startY2), endY)
        val effectiveWidth = localMaxX - localMinX + 1
        val effectiveHeight = localMaxY - localMinY + 1
        val fictiveStartX = ONE * (startX - localMinX)
        val fictiveStartY = ONE * (startY - localMinY)

        // Loop over all relevant coordinates
        for (x in 0 until effectiveWidth){

            var realX = localMinX + x

            // If we get a little outside the texture range, we continue on the other side
            if (realX > maxX){
                realX -= texture.width
            }
            if (realX < 0){
                realX += texture.width
            }

            val dx = x - fictiveStartX

            for (y in 0 until effectiveHeight){

                // Rotate (x - fictiveStartX, y - fictiveStartY)
                val dy = y - fictiveStartY
                val transformedX = sinAngle * dx - cosAngle * dy
                val transformedY = sinAngle * dy + cosAngle * dx

                // Check if the pixel at this location should be affected
                if (transformedX > -width && transformedX < width && transformedY >= 0 && transformedY < length){
                    //val progress = transformedY * invLength
                    val progress = transformedY / length
                    if (abs(transformedX) <= sqrt(1.0 - progress.toDouble()) * width){

                        // Let's now get the actual y-coordinate
                        var realY = localMinY + y

                        while (realY > maxY){
                            realY -= texture.height
                        }
                        while (realY < 0){
                            realY += texture.height
                        }

                        // Finally test if we are not 'below' some other grass 'line'
                        val realHeight = cosVertAngleTimesLength * progress
                        val currentHeightIndex = realX + texture.width * realY
                        if (realHeight >= heightTable[currentHeightIndex]){
                            heightTable[currentHeightIndex] = realHeight

                            val extraColor = sinVertAngle * progress * progress
                            val newRed = redBase + extraColor * redLeft
                            val newGreen = greenBase + extraColor * greenLeft
                            val newBlue = blueBase + extraColor * blueLeft
                            texture.setPixel(realX, realY, newRed, newGreen, newBlue)
                        }
                    }
                }
            }
        }
    }

    val endTime = System.currentTimeMillis()
    println("Drawing grass took ${endTime - startTime} ms")
}
