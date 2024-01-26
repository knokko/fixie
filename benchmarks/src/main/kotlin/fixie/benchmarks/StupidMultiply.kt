package fixie.benchmarks

import fixie.*
import java.util.*
import kotlin.math.absoluteValue
import kotlin.system.measureNanoTime

private const val size = 200_000_000

fun main() {
    for (counter in 0 until 5) tryFixedPerformance()
}

private fun tryFloatPerformance() {
    val rng = Random()
    val values = FloatArray(size) { 0.5f + rng.nextFloat() }

    val spentTime = measureNanoTime {
        var result = 0f
        for (index in 1 until values.size) {
            result += values[index - 1] * values[index]
            if (result.absoluteValue > 10_000f) result = 0f
        }
        println("result is $result")
    }

    println("Took ${spentTime / 1_000_000.0} ms")
}

private fun tryDoublePerformance() {
    val rng = Random()
    val values = DoubleArray(size) { 0.5 + rng.nextDouble() }

    val spentTime = measureNanoTime {
        var result = 0.0
        for (index in 1 until values.size) {
            result += values[index - 1] * values[index]
            //if (result.absoluteValue > 10_000.0) result = 0.0
        }
        println("result is $result")
    }

    println("Took ${spentTime / 1_000_000.0} ms")
}

private typealias StupidNum = FixDecMicro64

private fun tryFixedPerformance() {
    val rng = Random()
    val values = FixDecMicro64.Array(size) { StupidNum.from(0.5 + 50.0 * rng.nextDouble()) }

    val spentTime = measureNanoTime {
        var result = StupidNum.ZERO
        for (index in 1 until values.size) {
            result += values[index - 1] * values[index]
            //if (result.toDouble().absoluteValue > 10_000.0) result = StupidNum.ZERO
        }
        println("result is $result")
    }

    println("Took ${spentTime / 1_000_000.0} ms")
}