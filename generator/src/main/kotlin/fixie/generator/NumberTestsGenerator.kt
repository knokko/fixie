package fixie.generator

import java.io.PrintWriter
import java.math.BigInteger
import kotlin.math.max
import kotlin.math.min
import kotlin.math.nextDown
import kotlin.math.roundToLong
import kotlin.random.Random

class NumberTestsGenerator(
        private val writer: PrintWriter,
        private val number: NumberClass
) {

    private val testClassName = "Test${number.className}"

    fun generate() {
        generateClassPrefix()
        // TODO Test overflow checks everywhere
        generateIntConversion(IntType(true, 4))
        generateIntConversion(IntType(true, 8))
        generateDoubleConversion(true)
        generateDoubleConversion(false)
        if (number.internalType.signed) generateUnaryMinus()
        generateAdditionAndSubtraction()
        generateMultiplication()
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
        writer.println("\t}")
    }

    private fun generateDoubleConversion(convertToFloat: Boolean) {
        writer.println()
        writer.println("\t@Test")
        if (convertToFloat) writer.println("\tfun testFloatConversion() {")
        else writer.println("\tfun testDoubleConversion() {")

        val minValue = 1.0 / number.oneValue.toDouble()
        val realMaxValue = number.internalType.getMaxValue().toDouble() / number.oneValue.toDouble()

        // Avoid nasty rounding errors
        var maxValue = realMaxValue
        if (convertToFloat) {
            while (maxValue.toFloat().toDouble() > realMaxValue) maxValue = maxValue.nextDown()
        } else {
            while (maxValue * number.oneValue.toDouble() > number.internalType.getMaxValue().toDouble()) maxValue = maxValue.nextDown()
        }

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
            if (number.internalType.signed) generateTestCase(-candidate)
        }

        writer.println("\t}")
    }

    private fun generateUnaryMinus() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testUnaryMinus() {")

        val testSequence = generateTestSequence(
            number.internalType.getMinValue().add(BigInteger.ONE).longValueExact(),
            number.internalType.getMaxValue().longValueExact(),
            31, 1, 2024012410
        )
        if (number.checkOverflow) writer.println("\t\tassertThrows(FixedPointException::class.java) { -${number.className}.raw(${number.internalType}.MIN_VALUE) }")

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

        val oneLongValue = number.oneValue.longValueExact().toString() + if (number.internalType.signed) "" else "u"
        writer.println("\t\ttestValues(${number.className}.raw(${number.internalType}.MIN_VALUE), ${number.className}.ONE, ${number.className}.raw(${number.internalType}.MIN_VALUE + $oneLongValue))")

        val minValue = (number.internalType.getMinValue() / number.oneValue).longValueExact()
        val maxValue = (number.internalType.getMaxValue() / number.oneValue).longValueExact() / 2
        val intSequence = generateTestSequence(minValue, maxValue, 48, 2, 20242401130)
        val rng = Random(32)
        for (candidate in intSequence) {
            val adder = rng.nextLong(maxValue)
            writer.println("\t\ttestValues($candidate, $adder, ${candidate + adder})")
        }

        writer.println("\t\ttestValues(${number.className}.raw(${number.internalType}.MAX_VALUE - $oneLongValue), ${number.className}.ONE, ${number.className}.raw(${number.internalType}.MAX_VALUE))")

        writer.println("\t}")
    }

    private fun generateMultiplication() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testMultiplication() {")
        writer.println("\t\tassertEquals(${number.className}.raw(${number.internalType}.MAX_VALUE), 1 * ${number.className}.raw(${number.internalType}.MAX_VALUE))")
        if (number.internalType.signed) {
            writer.println("\t\tassertEquals(${number.className}.raw(${number.internalType}.MIN_VALUE), 1 * ${number.className}.raw(${number.internalType}.MIN_VALUE))")
            writer.println("\t\tassertEquals(${number.className}.raw(${number.internalType}.MIN_VALUE + 1), -1 * ${number.className}.raw(${number.internalType}.MAX_VALUE))")
            if (number.checkOverflow) writer.println("\t\tassertThrows(FixedPointException::class.java) { -1 * ${number.className}.raw(${number.internalType}.MIN_VALUE) }")
        } else if (number.checkOverflow){
            writer.println("\t\tassertThrows(FixedPointException::class.java) { -1 * ${number.className}.ONE }")
            writer.println("\t\tassertThrows(FixedPointException::class.java) { -1 * ${number.className}.raw(${number.internalType}.MAX_VALUE)}")
        }
        writer.println()
        writer.println("\t\tfun testValues(a: Long, b: Long) {")
        writer.println("\t\t\tassertEquals(${number.className}.from(a * b), ${number.className}.from(a) * ${number.className}.from(b))")
        writer.println("\t\t\tassertEquals(${number.className}.from(a * b), ${number.className}.from(a) * b)")
        writer.println("\t\t\tassertEquals(${number.className}.from(a * b), b * ${number.className}.from(a))")
        writer.println("\t\t}")

        val minIntValue = (number.internalType.getMinValue() / number.oneValue).longValueExact()
        val maxIntValue = (number.internalType.getMaxValue() / number.oneValue).longValueExact()

        val sequence = generateTestSequence(minIntValue, maxIntValue, 6, 2, 20242401118)
        for (a in sequence) {
            val b = sequence.random()
            val fits = try {
                Math.multiplyExact(a, b) in minIntValue..maxIntValue
            } catch (overflow: ArithmeticException) { false }

            if (fits) {
                writer.println("\t\ttestValues($a, $b)")
            } else if (number.checkOverflow) {
                writer.println("\t\tassertThrows(FixedPointException::class.java) { ${number.className}.from($a) * $b }")
            }
        }

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
        val oneString = "1" + if (number.internalType.signed) "" else "u"
        writer.println("\t\tval minDelta = ${number.className}.raw($oneString)")
        writer.println("\t\tassertEquals(${number.className}.from(12), ${number.className}.from(12))")
        writer.println("\t\tassertNotEquals(${number.className}.from(12), ${number.className}.from(12) + minDelta)")

        val minValue = 1.0 / number.oneValue.toDouble()
        val realMaxValue = number.internalType.getMaxValue().toDouble() / number.oneValue.toDouble()

        // Avoid nasty rounding errors
        var maxValue = realMaxValue
        while (maxValue * number.oneValue.toDouble() > number.internalType.getMaxValue().toDouble()) maxValue = maxValue.nextDown()

        val sequence = generateTestSequence(minValue, maxValue, 52.1, 2, 20241143)
        for (candidate in sequence) {
            if (candidate != minValue) writer.println("\t\tassertFalse(${number.className}.from($candidate) < ${number.className}.from($candidate) - minDelta)")
            if (candidate != maxValue) writer.println("\t\tassertTrue(${number.className}.from($candidate) < ${number.className}.from($candidate) + minDelta)")
        }

        writer.println("\t}")
    }
}
