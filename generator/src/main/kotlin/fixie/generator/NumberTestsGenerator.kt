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
        generateUnaryMinus()
        generateAdditionAndSubtraction()
        generateCompareTo()
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
        writer.println("import org.junit.jupiter.api.Assertions.*")
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

    private fun generateUnaryMinus() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testUnaryMinus() {")

        val testSequence = generateTestSequence(Long.MIN_VALUE + 1, Long.MAX_VALUE, 31, 1, 2024012410)
        if (number.checkOverflow) writer.println("\t\tassertThrows(FixedPointException::class.java) { -${number.className}.raw(Long.MIN_VALUE) }")

        for (candidate in testSequence) {
            writer.println("\t\tassertEquals(${-candidate}, -${number.className}.raw($candidate).raw)")
        }
        writer.println("\t}")
    }

    private fun generateAdditionAndSubtraction() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testAdditionAndSubtraction() {")
        writer.println("\t\tfun testValues(a: ${number.className}, b: ${number.className}, c: ${number.className}) {")
        writer.println("\t\t\tassertEquals(c, a + b)")
        writer.println("\t\t\tassertEquals(c, b + a)")
        writer.println("\t\t\tassertEquals(a, c - b)")
        writer.println("\t\t\tassertEquals(b, c - a)")
        writer.println("\t\t}")
        writer.println("\t\tfun testValues(a: Long, b: Long, c: Long) {")
        writer.println("\t\t\ttestValues(${number.className}.from(a), ${number.className}.from(b), ${number.className}.from(c))")
        writer.println("\t\t\tassertEquals(${number.className}.from(c), ${number.className}.from(a) + b)")
        writer.println("\t\t\tassertEquals(${number.className}.from(c), b + ${number.className}.from(a))")
        writer.println("\t\t\tassertEquals(${number.className}.from(a), ${number.className}.from(c) - b)")
        writer.println("\t\t\tassertEquals(${number.className}.from(b), c - ${number.className}.from(a))")
        writer.println("\t\t}")

        writer.println("\t\ttestValues(${number.className}.raw(${number.internalType}.MIN_VALUE), ${number.className}.ONE, ${number.className}.raw(${number.internalType}.MIN_VALUE + ${number.oneValue.toLong()}))")

        val minValue = (number.internalType.getMinValue() / number.oneValue).longValueExact()
        val maxValue = (number.internalType.getMaxValue() / number.oneValue).longValueExact() / 2
        val intSequence = generateTestSequence(minValue, maxValue, 48, 2, 20242401130)
        val rng = Random(32)
        for (candidate in intSequence) {
            val adder = rng.nextLong(maxValue)
            writer.println("\t\ttestValues($candidate, $adder, ${candidate + adder})")
        }

        writer.println("\t\ttestValues(${number.className}.raw(${number.internalType}.MAX_VALUE), -${number.className}.ONE, ${number.className}.raw(${number.internalType}.MAX_VALUE - ${number.oneValue.toLong()}))")

        writer.println("\t}")
    }

    private fun generateCompareTo() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompareTo() {")
        writer.println("\t\tassertTrue(${number.className}.ZERO < ${number.className}.ONE)")
        writer.println("\t\tassertFalse(${number.className}.ZERO > ${number.className}.ONE)")
        writer.println("\t\tassertFalse(${number.className}.ONE < ${number.className}.ONE)")
        writer.println("\t\tassertFalse(${number.className}.ONE > ${number.className}.ONE)")
        writer.println("\t\tassertTrue(${number.className}.ONE <= ${number.className}.ONE)")
        writer.println("\t\tassertTrue(${number.className}.ONE >= ${number.className}.ONE)")
        writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MIN_VALUE) < ${number.className}.raw(${number.internalType}.MAX_VALUE))")
        writer.println()
        writer.println("\t\tval minDelta = ${number.className}.raw(1)")
        writer.println("\t\tassertEquals(${number.className}.from(12), ${number.className}.from(12))")
        writer.println("\t\tassertNotEquals(${number.className}.from(12), ${number.className}.from(12) + minDelta)")

        val minValue = 1.0 / number.oneValue.toDouble()
        val maxValue = number.internalType.getMaxValue().toDouble() / number.oneValue.toDouble()
        val sequence = generateTestSequence(minValue, maxValue, 52.1, 2, 20241143)
        for (candidate in sequence) {
            if (candidate != minValue) writer.println("\t\tassertFalse(${number.className}.from($candidate) < ${number.className}.from($candidate) - minDelta)")
            if (candidate != maxValue) writer.println("\t\tassertTrue(${number.className}.from($candidate) < ${number.className}.from($candidate) + minDelta)")
        }

        writer.println("\t}")
    }
}
