package fixie.generator

import java.io.PrintWriter
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToLong
import kotlin.random.Random

class NumberTestsGenerator(
        private val writer: PrintWriter,
        private val number: NumberClass
) {

    private val testClassName = "Test${number.className}"

    fun generate() {
        generateClassPrefix()
        generateIntConversion(IntType(true, 4))
        generateIntConversion(IntType(true, 8))
        generateDoubleConversion(true)
        generateDoubleConversion(false)
        writer.println("}")
    }

    private fun generateTestSequence(minValue: Double, maxValue: Double, growFactor: Double, density: Int, seed: Long): List<Double> {
        val rng = Random(seed)
        val sequence = mutableListOf(minValue, maxValue)

        var value = growFactor * minValue
        while (value < maxValue / growFactor) {
            val marginFactor = 1.0 + 0.7 * (growFactor - 1.0)
            for (counter in 0 until density) {
                sequence.add(rng.nextDouble(value / marginFactor, value * marginFactor))
            }
            value *= growFactor
        }

        sequence.sort()
        return sequence
    }

    private fun generateTestSequence(minValue: Long, maxValue: Long, growFactor: Int, density: Int, seed: Long): List<Long> {
        val rng = Random(seed)
        val sequence = mutableListOf(minValue, maxValue)

        var value = minValue
        while (value < maxValue / growFactor) {
            if (value != minValue) {
                val marginFactor = 1.0 + 0.7 * (growFactor - 1.0)
                for (counter in 0 until density) {
                    val bound1 = (value / marginFactor).roundToLong()
                    val bound2 = (value * marginFactor).roundToLong()
                    sequence.add(rng.nextLong(min(bound1, bound2), max(bound1, bound2)))
                }
            }

            if (value < -growFactor * growFactor) value /= growFactor
            else if (value > growFactor * growFactor) value *= growFactor
            else value += growFactor * growFactor
        }

        if (!sequence.contains(0)) sequence.add(0)
        if (!sequence.contains(1)) sequence.add(1)

        sequence.sort()
        return sequence
    }

    private fun generateClassPrefix() {
        writer.println("package fixie")
        writer.println()
        writer.println("import org.junit.jupiter.api.Test")
        writer.println("import org.junit.jupiter.api.Assertions.assertEquals")
        writer.println()
        writer.println("class $testClassName {")
    }

    private fun generateIntConversion(type: IntType) {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun test${type}Conversion() {")
        writer.println("\t\tfun testValue(value: $type) = assertEquals(value, ${number.className}.from(value).to$type())")

        val minValue = (number.internalType.getMinValue() / number.oneValue).max(type.getMinValue())
        val maxValue = (number.internalType.getMaxValue() / number.oneValue).min(type.getMaxValue())

        val testSequence = generateTestSequence(
                minValue.longValueExact(), maxValue.longValueExact(), 15, 1, 2024012346
        )
        for (candidate in testSequence) {
            writer.println("\t\ttestValue($candidate)")
        }
        writer.println("}")
    }

    private fun generateDoubleConversion(convertToFloat: Boolean) {
        writer.println()
        writer.println("\t@Test")
        if (convertToFloat) writer.println("\tfun testFloatConversion() {")
        else writer.println("\tfun testDoubleConversion() {")

        val minValue = 1.0 / number.oneValue.toDouble()
        val maxValue = number.internalType.getMaxValue().toDouble() / number.oneValue.toDouble()

        fun generateTestCase(value: Double) {
            if (convertToFloat) {
                writer.println("\t\tassertEquals(${value.toFloat()}f, ${number.className}.from(${value.toFloat()}f).toFloat(), delta)")
            } else {
                writer.println("\t\tassertEquals($value, ${number.className}.from($value).toDouble(), delta)")
            }
        }

        val testSequence = generateTestSequence(minValue, maxValue, 17.0, 1, 2024012322)
        val delta = 2 * minValue
        if (convertToFloat) writer.println("\t\tval delta = ${delta.toFloat()}f")
        else writer.println("\t\tval delta = $delta")
        for (candidate in testSequence) {
            generateTestCase(candidate)
            generateTestCase(-candidate)
        }

        writer.println("\t}")
    }
}
