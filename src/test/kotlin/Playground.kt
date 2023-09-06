import fixie.FixNano64
import java.util.*
import kotlin.math.absoluteValue
import kotlin.system.measureNanoTime

fun main() {
    tryDoublePerformance()
}

private fun tryFloatPerformance() {
    val rng = Random()
    val values = FloatArray(100_000_000) { 0.5f + rng.nextFloat() }

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
    val values = DoubleArray(100_000_000) { 0.5 + rng.nextDouble() }

    val spentTime = measureNanoTime {
        var result = 0.0
        for (index in 1 until values.size) {
            result += values[index - 1] * values[index]
            if (result.absoluteValue > 10_000.0) result = 0.0
        }
        println("result is $result")
    }

    println("Took ${spentTime / 1_000_000.0} ms")
}

private fun tryFixedPerformance() {
    val rng = Random()
    val values = LongArray(100_000_000) { FixNano64.fromDouble(0.5 + 50.0 * rng.nextDouble()).raw }

    val spentTime = measureNanoTime {
        var result = FixNano64.ZERO
        for (index in 1 until values.size) {
            result += FixNano64.raw(values[index - 1]) * FixNano64.raw(values[index])
            if (result.toDouble().absoluteValue > 10_000.0) result = FixNano64.ZERO
        }
        println("result is $result")
    }

    println("Took ${spentTime / 1_000_000.0} ms")
}
