package fixie.generator.number

import java.io.PrintWriter
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.*
import kotlin.random.Random

internal class NumberTestsGenerator(
        private val writer: PrintWriter,
        private val number: NumberClass,
        private val packageName: String
) {

    fun generate() {
        generateClassPrefix()
        generateAssertEquals()
        generateToString()
        generateIntConversion(IntType(true, 4))
        generateIntConversion(IntType(true, 8))
        generateDoubleConversion(true)
        generateDoubleConversion(false)
        if (number.internalType.signed) generateUnaryMinus()
        generateAdditionAndSubtraction()
        generateMultiplicationAndDivision()
        generateCompareTo()
        generateArrayClass()
        generateHelperFunctions()
        writer.println("}")
    }

    private fun generateTestSequence(minValue: Double, maxValue: Double, growFactor: Double, density: Int, seed: Long): List<Double> {
        if (minValue > maxValue) throw IllegalArgumentException("min value is $minValue, but max value is $maxValue")
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
        if (sequence[0] == 1.0) {
            throw IllegalStateException()
        }
        return sequence
    }

    private fun generateTestSequence(
            minValue: BigInteger, maxValue: BigInteger,
            growFactor: Int, density: Int, seed: Long
    ): List<BigInteger> {
        val rng = Random(seed)
        val sequence = mutableListOf(minValue, maxValue)

        var value = minValue
        while (value < maxValue / BigInteger.valueOf(growFactor.toLong()) && value < maxValue) {
            if (value != minValue) {
                val marginFactor = BigDecimal.valueOf(1.0 + 0.7 * (growFactor - 1.0))
                for (counter in 0 until density) {
                    val bound1 = (value.toBigDecimal() / marginFactor + BigDecimal.valueOf(0.5 * value.signum())).toBigInteger()
                    val bound2 = (value.toBigDecimal() * marginFactor + BigDecimal.valueOf(0.5 * value.signum())).toBigInteger()

                    val minBound = bound1.min(bound2)
                    val maxBound = bound1.max(bound2)
                    sequence.add(minBound + BigInteger.valueOf(rng.nextLong((maxBound - minBound).longValueExact())))
                }
            }

            if (value < BigInteger.valueOf(-growFactor.toLong() * growFactor)) value /= BigInteger.valueOf(growFactor.toLong())
            else if (value > BigInteger.valueOf(growFactor.toLong() * growFactor)) value *= BigInteger.valueOf(growFactor.toLong())
            else value += BigInteger.valueOf(growFactor.toLong() * growFactor)
        }

        if (!sequence.contains(BigInteger.ZERO) && minValue <= BigInteger.ZERO && maxValue >= BigInteger.ZERO) {
            sequence.add(BigInteger.ZERO)
        }
        if (!sequence.contains(BigInteger.ONE) && minValue <= BigInteger.ONE && maxValue >= BigInteger.ONE) {
            sequence.add(BigInteger.ONE)
        }

        sequence.sort()
        return sequence
    }

    private fun generateClassPrefix() {
        writer.println("package $packageName")
        writer.println()
        writer.println("import kotlin.math.absoluteValue")
        writer.println("import org.junit.jupiter.api.Test")
        writer.println("import org.junit.jupiter.api.Assertions.*")
        writer.println()
        writer.println("class Test${number.className} {")
    }

    private fun generateAssertEquals() {
        writer.println()
        writer.println("\tfun assertEquals(a: ${number.className}, b: ${number.className}, maxDelta: ${number.className}) {")
        val conversion = if (number.internalType.signed) "" else ".to${IntType(true, number.internalType.numBytes)}()"
        writer.println("\t\tval rawDifference = a.raw$conversion - b.raw$conversion")
        writer.println("\t\tif (rawDifference.absoluteValue > maxDelta.raw$conversion) assertEquals(a, b)")
        writer.println("\t}")
    }

    private fun generateToString() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToString() {")
        writer.println("\t\tassertEquals(\"0\", ${number.className}.ZERO.toString())")
        writer.println("\t\tassertEquals(\"1\", ${number.className}.ONE.toString())")
        if (number.oneValue > BigInteger.valueOf(1000)) {
            writer.println("\t\tassertTrue((${number.className}.ONE / 3).toString().startsWith(\"0.33\"))")
        } else if (number.oneValue >= BigInteger.valueOf(8)){
            writer.println("\t\tassertTrue((${number.className}.ONE / 3).toString().startsWith(\"0.3\"))")
        }
        if (number.internalType.signed) {
            writer.println("\t\tassertEquals(\"-1\", (-${number.className}.ONE).toString())")
            if (number.oneValue > BigInteger.valueOf(1000)) {
                writer.println("\t\tassertTrue((${number.className}.ONE / -3).toString().startsWith(\"-0.33\"))")
            } else if (number.oneValue >= BigInteger.valueOf(8)) {
                writer.println("\t\tassertTrue((${number.className}.ONE / -3).toString().startsWith(\"-0.3\"))")
            }
        }

        // Cancellation check
        val maxIntValue = number.internalType.getMaxValue() / number.oneValue
        writer.println("\t\tassertTrue((${number.className}.from(${maxIntValue - BigInteger.ONE}) + ${number.className}.ONE / 3).toString().endsWith((${number.className}.ONE / 3).toString().substring(1)))")

        // Test numbers like 0.0x
        if (number.oneValue.remainder(BigInteger.valueOf(16)) == BigInteger.ZERO) {
            if (number.oneValue > BigInteger.valueOf(50_000)) {
                writer.println("\t\tassertEquals(\"0.0625\", (${number.className}.ONE / 16).toString())")
            } else writer.println("\t\tassertTrue((${number.className}.ONE / 16).toString().startsWith(\"0.06\"))")
        }
        if (number.oneValue.remainder(BigInteger.valueOf(100)) == BigInteger.ZERO) {
            writer.println("\t\tassertEquals(\"0.01\", (${number.className}.ONE / 100).toString())")
        }

        writer.println("\t}")
    }

    private fun generateIntConversion(type: IntType) {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun test${type}Conversion() {")
        writer.println("\t\t${type.declareValue("val one", 1)}")
        writer.println("\t\tassertEquals(${number.className}.ONE, ${number.className}.from(one))")
        writer.println()

        val minValue = (number.internalType.getMinValue() / number.oneValue).max(type.getMinValue())
        val maxValue = (number.internalType.getMaxValue() / number.oneValue).min(type.getMaxValue())

        writer.println("\t\tfun testValue(value: $type) = assertEquals(value, ${number.className}.from(value).to$type())")

        val canUnderflow = type.canRepresent(minValue.subtract(BigInteger.ONE))
        val canOverflow = type.canRepresent(maxValue.add(BigInteger.ONE))
        if (number.checkOverflow && (canOverflow || canUnderflow)) {
            writer.println("\t\tfun testOverflow(value: $type) = assertThrows(FixedPointException::class.java) { ${number.className}.from(value) }")
        }

        val testSequence = generateTestSequence(
                minValue, maxValue, 15, 1, 2024012346
        )
        for (candidate in testSequence) {
            writer.println("\t\ttestValue($candidate)")
        }

        if (number.checkOverflow && canUnderflow) {
            writer.println()
            val minTypeValue = type.getMinValue()
            val underflowSequence = generateTestSequence(minTypeValue, minValue - BigInteger.ONE, 45, 1, 242420001)
            for (candidate in underflowSequence) {
                if (candidate == BigInteger.valueOf(Long.MIN_VALUE)) writer.println("\t\ttestOverflow(Long.MIN_VALUE)")
                else writer.println("\t\ttestOverflow($candidate)")
            }
        }

        if (number.checkOverflow && canOverflow) {
            writer.println()
            val overflowSequence = generateTestSequence(maxValue + BigInteger.ONE, type.getMaxValue(), 45, 1, 27202401)
            for (candidate in overflowSequence) {
                writer.println("\t\ttestOverflow($candidate)")
            }
        }

        writer.println("\t}")
    }

    private fun generateDoubleConversion(convertToFloat: Boolean) {
        writer.println()
        writer.println("\t@Test")
        if (convertToFloat) writer.println("\tfun testFloatConversion() {")
        else writer.println("\tfun testDoubleConversion() {")

        if (number.oneValue < BigInteger.TWO.pow(50)) {
            if (convertToFloat) writer.println("\t\tassertEquals(${number.className}.ONE, ${number.className}.from(1f))")
            else writer.println("\t\tassertEquals(${number.className}.ONE, ${number.className}.from(1.0))")
        }

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

        if (number.checkOverflow) {
            writer.println()

            fun generateOverflowCase(value: Double) {
                if (convertToFloat) {
                    writer.println("\t\tassertThrows(FixedPointException::class.java) { ${number.className}.from(${value.toFloat()}f) }")
                } else {
                    writer.println("\t\tassertThrows(FixedPointException::class.java) { ${number.className}.from($value) }")
                }
            }

            val minOverflowValue = max(realMaxValue * 1.0001, realMaxValue + 1)
            val overflowSequence = generateTestSequence(
                minOverflowValue, minOverflowValue * minOverflowValue,
                31.3, 1, 20272401
            )
            for (candidate in overflowSequence) {
                generateOverflowCase(candidate)
                generateOverflowCase(-candidate)
            }
        }

        writer.println("\t}")
    }

    private fun generateUnaryMinus() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testUnaryMinus() {")

        val testSequence = generateTestSequence(
            number.internalType.getMinValue().add(BigInteger.ONE),
            number.internalType.getMaxValue(),
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
        writer.println()
        writer.println("\t\tfun testValues(a: Long, b: Long, c: Long) {")
        writer.println("\t\t\ttestValues(${number.className}.from(a), ${number.className}.from(b), ${number.className}.from(c))")
        writer.println("\t\t\tassertEquals(${number.className}.from(c), ${number.className}.from(a) + b)")
        writer.println("\t\t\tassertEquals(${number.className}.from(c), b + ${number.className}.from(a))")
        writer.println("\t\t\tassertEquals(${number.className}.from(a), ${number.className}.from(c) - b)")
        writer.println("\t\t\tassertEquals(${number.className}.from(b), c - ${number.className}.from(a))")
        writer.println("\t\t}")
        writer.println()

        val oneLongValue = number.oneValue.longValueExact().toString() + if (number.internalType.signed) "" else "u"
        var minusPlusOne = "${number.internalType}.MIN_VALUE + $oneLongValue"
        if (number.internalType.numBytes < 4) minusPlusOne = "($minusPlusOne).to${number.internalType}()"
        writer.println("\t\ttestValues(${number.className}.raw(${number.internalType}.MIN_VALUE), ${number.className}.ONE, ${number.className}.raw($minusPlusOne))")

        val minValue = (number.internalType.getMinValue() / number.oneValue)
        val maxValue = (number.internalType.getMaxValue() / number.oneValue)
        val intSequence = generateTestSequence(minValue, maxValue, 3 * number.internalType.numBytes, 2, 20242401130)
        val rng = Random(32)
        for (candidate in intSequence) {
            var adderBound = BigInteger.ONE + maxValue - candidate
            if (adderBound < BigInteger.ZERO) adderBound = maxValue
            val adder = rng.nextLong(adderBound.min(maxValue).longValueExact())
            writer.println("\t\ttestValues($candidate, $adder, ${candidate + BigInteger.valueOf(adder)})")
        }

        var maxMinusOne = "${number.internalType}.MAX_VALUE - $oneLongValue"
        if (number.internalType.numBytes < 4) maxMinusOne = "($maxMinusOne).to${number.internalType}()"
        writer.println("\t\ttestValues(${number.className}.raw($maxMinusOne), ${number.className}.ONE, ${number.className}.raw(${number.internalType}.MAX_VALUE))")

        if (number.checkOverflow) {
            fun generateOverflowTest(typeName: String, operationName: String, operator: String) {
                writer.println()
                writer.println("\t\tfun testOverflow$operationName(a: $typeName, b: $typeName) {")
                writer.println("\t\t\tassertThrows(FixedPointException::class.java) { ${number.className}.from(a) $operator ${number.className}.from(b) }")
                writer.println("\t\t\tassertThrows(FixedPointException::class.java) { ${number.className}.from(a) $operator b }")
                writer.println("\t\t\tassertThrows(FixedPointException::class.java) { a $operator ${number.className}.from(b) }")
                writer.println("\t\t}")
            }
            for (typeName in arrayOf("Int", "Long", "Float", "Double")) {
                generateOverflowTest(typeName, "Plus", "+")
                generateOverflowTest(typeName, "Minus", "-")
            }

            val intSequence2 = generateTestSequence(
                minValue, maxValue, 9 * number.internalType.numBytes, 1, 20242401130
            )

            writer.println()
            for (candidate in intSequence2) {
                val adder1 = maxValue - candidate + BigInteger.ONE
                val adder2 = (adder1 + BigInteger.ONE) * (adder1 + BigInteger.ONE)

                val difference1 = candidate - minValue + BigInteger.ONE
                val difference2 = (difference1 + BigInteger.ONE) * (difference1 + BigInteger.ONE)

                for (adder in arrayOf(adder1, adder2)) {
                    if (BigInteger.valueOf(candidate.toInt().toLong()) == candidate &&
                            BigInteger.valueOf(adder.toInt().toLong()) == adder) {
                        writer.println("\t\ttestOverflowPlus($candidate, $adder)")
                    }
                    if (BigInteger.valueOf(candidate.toLong()) == candidate &&
                            BigInteger.valueOf(adder.toLong()) == adder) {
                        val longAdderString = if (adder == BigInteger.valueOf(Long.MIN_VALUE)) "Long.MIN_VALUE" else "${adder}L"
                        writer.println("\t\ttestOverflowPlus(${candidate}L, $longAdderString)")
                    }

                    // Avoid precision issues
                    if (adder.toDouble() > 0.01 * abs(candidate.toDouble()) &&
                            abs(candidate.toDouble()) > 0.01 * adder.toDouble() &&
                            BigInteger.valueOf(adder.toFloat().roundToLong()) == adder &&
                            BigInteger.valueOf(candidate.toFloat().roundToLong()) == candidate
                            ) {
                        writer.println("\t\ttestOverflowPlus(${candidate.toFloat()}f, ${adder.toFloat()}f)")
                    }
                    if (adder.toDouble() > 0.0001 * abs(candidate.toDouble()) &&
                            abs(candidate.toDouble()) > 0.0001 * adder.toDouble() &&
                            BigInteger.valueOf(adder.toDouble().roundToLong()) == adder &&
                            BigInteger.valueOf(candidate.toDouble().roundToLong()) == candidate
                            ) {
                        writer.println("\t\ttestOverflowPlus(${candidate.toDouble()}, ${adder.toDouble()})")
                    }
                }

                for (difference in arrayOf(difference1, difference2)) {
                    if (BigInteger.valueOf(candidate.toInt().toLong()) == candidate
                            && BigInteger.valueOf(difference.toInt().toLong()) == difference) {
                        writer.println("\t\ttestOverflowMinus($candidate, $difference)")
                    }

                    if (BigInteger.valueOf(difference.toLong()) == difference &&
                            BigInteger.valueOf(candidate.toLong()) == candidate) {
                        val longDifferenceString = if (difference == BigInteger.valueOf(Long.MIN_VALUE)) "Long.MIN_VALUE" else "${difference}L"
                        writer.println("\t\ttestOverflowMinus(${candidate}L, $longDifferenceString)")
                    }

                    // Avoid precision issues
                    if (difference.toDouble() > 0.01 * abs(candidate.toDouble()) &&
                            abs(candidate.toDouble()) > 0.01 * difference.toDouble() &&
                            (difference == difference2 || abs(candidate.toDouble()) < 10_000) &&
                            BigInteger.valueOf(difference.toFloat().roundToLong()) == difference &&
                            BigInteger.valueOf(candidate.toFloat().roundToLong()) == candidate) {
                        writer.println("\t\ttestOverflowMinus(${candidate.toFloat()}f, ${difference.toFloat()}f)")
                    }
                    if (difference.toDouble() > 0.0001 * abs(candidate.toDouble())
                            && abs(candidate.toDouble()) > 0.0001 * difference.toDouble()
                            && BigInteger.valueOf(difference.toDouble().roundToLong()) == difference
                            && BigInteger.valueOf(candidate.toDouble().roundToLong()) == candidate) {
                        writer.println("\t\ttestOverflowMinus(${candidate.toDouble()}, ${difference.toDouble()})")
                    }
                }
            }
        }

        val raw = BigInteger.ONE + BigInteger.valueOf(rng.nextLong(number.oneValue.subtract(BigInteger.ONE).longValueExact()))

        if (number.internalType.signed) {
            writer.println("\t\tassertEquals(${number.className}.raw(${raw + (maxValue - BigInteger.ONE) * number.oneValue}), ${number.className}.raw(${-number.oneValue + raw}) + $maxValue)")
        } else {
            writer.println("\t\tassertEquals(${number.className}.raw(${raw + (maxValue - BigInteger.ONE) * number.oneValue}u), ${number.className}.raw(${raw}u) + ${maxValue - BigInteger.ONE})")
        }

        writer.println("\t}")
    }

    private fun generateMultiplicationAndDivision() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testMultiplicationAndDivision() {")
        writer.println("\t\tassertEquals(${number.className}.raw(${number.internalType}.MAX_VALUE), 1 * ${number.className}.raw(${number.internalType}.MAX_VALUE))")
        writer.println("\t\tassertEquals(${number.className}.raw(${number.internalType}.MAX_VALUE), ${number.className}.raw(${number.internalType}.MAX_VALUE) / 1)")
        if (number.internalType.signed) {
            writer.println("\t\tassertEquals(${number.className}.raw(${number.internalType}.MIN_VALUE), 1 * ${number.className}.raw(${number.internalType}.MIN_VALUE))")
            writer.println("\t\tassertEquals(${number.className}.raw(${number.internalType}.MIN_VALUE), ${number.className}.raw(${number.internalType}.MIN_VALUE) / 1)")
            var minPlusOne = "${number.internalType}.MIN_VALUE + 1"
            if (number.internalType.numBytes < 4) minPlusOne = "($minPlusOne).to${number.internalType}()"
            writer.println("\t\tassertEquals(${number.className}.raw($minPlusOne), -1 * ${number.className}.raw(${number.internalType}.MAX_VALUE))")
            writer.println("\t\tassertEquals(${number.className}.raw($minPlusOne), ${number.className}.raw(${number.internalType}.MAX_VALUE) / -1)")
            if (number.checkOverflow) {
                writer.println("\t\tassertThrows(FixedPointException::class.java) { -1 * ${number.className}.raw(${number.internalType}.MIN_VALUE) }")
                writer.println("\t\tassertThrows(FixedPointException::class.java) { ${number.className}.raw(${number.internalType}.MIN_VALUE) / -1 }")
            }
        } else if (number.checkOverflow){
            writer.println("\t\tassertThrows(FixedPointException::class.java) { -1 * ${number.className}.ONE }")
            writer.println("\t\tassertThrows(FixedPointException::class.java) { ${number.className}.ONE / -1 }")
            writer.println("\t\tassertThrows(FixedPointException::class.java) { -1 * ${number.className}.raw(${number.internalType}.MAX_VALUE)}")
            writer.println("\t\tassertThrows(FixedPointException::class.java) { ${number.className}.raw(${number.internalType}.MAX_VALUE) / -1 }")
        }
        writer.println()
        writer.println("\t\tfun testValues(a: Long, b: Long) {")
        writer.println("\t\t\tassertEquals(${number.className}.from(a * b), ${number.className}.from(a) * ${number.className}.from(b))")
        writer.println("\t\t\tassertEquals(${number.className}.from(a * b), ${number.className}.from(a) * b)")
        writer.println("\t\t\tassertEquals(${number.className}.from(a * b), b * ${number.className}.from(a))")
        writer.println("\t\t\tif (b != 0L) assertEquals(${number.className}.from(a), ${number.className}.from(a * b) / b)")
        writer.println("\t\t\tif (a != 0L) assertEquals(${number.className}.from(b), ${number.className}.from(a * b) / a)")
        writer.println("\t\t\tif (a != 0L && a.toInt().toLong() == a) {")
        writer.println("\t\t\t\tassertEquals(${number.className}.from(b), ${number.className}.from(a * b) / a.toInt())")
        writer.println("\t\t\t}")
        writer.println("\t\t\tif (b.toInt().toLong() == b) {")
        writer.println("\t\t\t\tassertEquals(${number.className}.from(a * b), ${number.className}.from(a) * b.toInt())")
        writer.println("\t\t\t\tassertEquals(${number.className}.from(a * b), b.toInt() * ${number.className}.from(a))")
        writer.println("\t\t\t}")
        writer.println("\t\t}")

        val minIntValue = (number.internalType.getMinValue() / number.oneValue)
        val maxIntValue = (number.internalType.getMaxValue() / number.oneValue)

        val sequence = generateTestSequence(minIntValue, maxIntValue, 6, 2, 20242401118)
        val rng = Random(26012024235)
        for (a in sequence) {
            val b = sequence.random(rng)
            val fits = a * b in minIntValue..maxIntValue

            if (fits) {
                writer.println("\t\ttestValues($a, $b)")
            } else if (number.checkOverflow) {
                writer.println("\t\tassertThrows(FixedPointException::class.java) { ${number.className}.from($a) * $b }")
            }
        }

        var raw1 = -rng.nextLong(number.oneValue.longValueExact() / 2, number.oneValue.longValueExact())
        if (!number.internalType.signed) raw1 = -raw1
        val raw2 = rng.nextLong(number.oneValue.longValueExact() / 2, number.oneValue.longValueExact())
        val suffix = if (number.internalType.signed) "" else "u"
        val maxDifference = max(2, number.oneValue.longValueExact() / 100)
        writer.println("\t\tassertEquals(${number.className}.raw($raw1$suffix), (${number.className}.raw($raw1$suffix) * ${number.className}.raw($raw2$suffix)) / ${number.className}.raw($raw2$suffix), ${number.className}.raw($maxDifference$suffix))")

        writer.println("\t}")
    }

    private fun generateCompareTo() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompareTo() {")
        writer.println("\t\tassertTrue(${number.className}.ZERO < ${number.className}.ONE)")
        writer.println("\t\tassertTrue(0 < ${number.className}.ONE)")
        writer.println("\t\tassertFalse(${number.className}.ZERO > ${number.className}.ONE)")
        writer.println("\t\tassertFalse(0 > ${number.className}.ONE)")
        writer.println("\t\tassertFalse(${number.className}.ONE < ${number.className}.ONE)")
        writer.println("\t\tassertFalse(${number.className}.ONE < 1)")
        writer.println("\t\tassertFalse(${number.className}.ONE > ${number.className}.ONE)")
        writer.println("\t\tassertTrue(${number.className}.ONE <= ${number.className}.ONE)")
        writer.println("\t\tassertTrue(${number.className}.ONE >= ${number.className}.ONE)")
        writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MIN_VALUE) < ${number.className}.raw(${number.internalType}.MAX_VALUE))")
        writer.println()
        val oneString = "1" + if (number.internalType.signed) "" else "u"
        writer.println("\t\tval minDelta = ${number.className}.raw($oneString)")

        var smallValue = 12
        while (BigInteger.valueOf(smallValue.toLong()) * number.oneValue > number.internalType.getMaxValue()) {
            smallValue /= 2
        }
        writer.println("\t\tassertEquals(${number.className}.from($smallValue), ${number.className}.from($smallValue))")
        writer.println("\t\tassertNotEquals(${number.className}.from($smallValue), ${number.className}.from($smallValue) - minDelta)")

        val minValue = 1.0 / number.oneValue.toDouble()
        val realMaxValue = number.internalType.getMaxValue().toDouble() / number.oneValue.toDouble()

        // Avoid nasty rounding errors
        var maxValue = realMaxValue
        while (maxValue * number.oneValue.toDouble() > number.internalType.getMaxValue().toDouble()) maxValue = maxValue.nextDown()

        val sequence = generateTestSequence(minValue, maxValue, 52.1, 2, 20241143)
        for (candidate in sequence) {
            if (candidate != minValue) {
                writer.println("\t\tassertFalse(${number.className}.from($candidate) < ${number.className}.from($candidate) - minDelta)")
                writer.println("\t\tassertFalse($candidate < ${number.className}.from($candidate) - minDelta)")
            }
            if (candidate != maxValue) {
                writer.println("\t\tassertTrue(${number.className}.from($candidate) < ${number.className}.from($candidate) + minDelta)")
                writer.println("\t\tassertTrue($candidate < ${number.className}.from($candidate) + minDelta)")
            }
        }

        // Annoying overflow-related cases
        val maxIntValue = number.internalType.getMaxValue() / number.oneValue
        writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MAX_VALUE) >= $maxIntValue)")
        if (maxIntValue * number.oneValue != number.internalType.getMaxValue()) {
            writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MAX_VALUE) > $maxIntValue)")
        }
        writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MAX_VALUE) < ${maxIntValue.add(BigInteger.ONE)}${if (number.internalType.signed) "" else "u"})")
        writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MAX_VALUE) < ${maxIntValue.add(BigInteger.ONE).toDouble() * 1.001})")
        writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MAX_VALUE) < ${number.internalType}.MAX_VALUE)")
        writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MAX_VALUE) < ${number.internalType}.MAX_VALUE.toFloat())")
        writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MAX_VALUE) < ${number.internalType}.MAX_VALUE.toDouble())")

        if (number.internalType.signed) {
            val minIntValue = number.internalType.getMinValue() / number.oneValue
            writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MIN_VALUE) <= $minIntValue)")
            if (minIntValue * number.oneValue != number.internalType.getMinValue()) {
                writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MIN_VALUE) < $minIntValue)")
            }
            writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MIN_VALUE) > ${minIntValue.subtract(BigInteger.ONE)})")
            writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MIN_VALUE) > ${minIntValue.subtract(BigInteger.ONE).toDouble() * 1.001})")
            writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MIN_VALUE) > ${number.internalType}.MIN_VALUE)")
            writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MIN_VALUE) > ${number.internalType}.MIN_VALUE.toFloat())")
            writer.println("\t\tassertTrue(${number.className}.raw(${number.internalType}.MIN_VALUE) > ${number.internalType}.MIN_VALUE.toDouble())")
        } else {
            writer.println("\t\tassertTrue(${number.className}.ZERO > -1)")
            writer.println("\t\tassertTrue(${number.className}.ZERO > -0.001f)")
            writer.println("\t\tassertTrue(${number.className}.ZERO > -0.001)")
            writer.println("\t\tassertTrue(${number.className}.ZERO > Long.MIN_VALUE)")
            writer.println("\t\tassertTrue(${number.className}.ZERO > Long.MIN_VALUE.toFloat())")
            writer.println("\t\tassertTrue(${number.className}.ZERO > Long.MIN_VALUE.toDouble())")
        }

        writer.println("\t}")
    }

    private fun generateArrayClass() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testArrayClass() {")
        writer.println("\t\tval testArray = ${number.className}.Array(2) { ${number.className}.ONE }")
        writer.println("\t\tassertEquals(2, testArray.size)")
        writer.println("\t\tassertEquals(${number.className}.ONE, testArray[0])")
        writer.println("\t\tassertEquals(${number.className}.ONE, testArray[1])")
        writer.println("\t\ttestArray[1] = ${number.className}.ZERO")
        writer.println("\t\tassertEquals(${number.className}.ONE, testArray[0])")
        writer.println("\t\tassertEquals(${number.className}.ZERO, testArray[1])")
        writer.println("\t\ttestArray.fill(${number.className}.ZERO)")
        writer.println("\t\tassertEquals(${number.className}.ZERO, testArray[0])")
        writer.println("\t\tassertEquals(${number.className}.ZERO, testArray[1])")
        writer.println("\t}")
    }

    private fun generateHelperFunctions() {
        generateAbs()
        generateMinMax()
    }

    private fun generateAbs() {
        if (!number.internalType.signed) return

        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testAbs() {")
        writer.println("\t\tassertEquals(${number.className}.ZERO, abs(${number.className}.ZERO))")
        writer.println("\t\tassertEquals(${number.className}.ONE, abs(${number.className}.ONE))")
        writer.println("\t\tassertEquals(${number.className}.ONE, abs(-${number.className}.ONE))")
        writer.println("\t\tassertEquals(${number.className}.raw(${number.internalType}.MAX_VALUE), abs(${number.className}.raw(${number.internalType}.MAX_VALUE)))")
        writer.println("\t\tassertEquals(${number.className}.raw(${number.internalType}.MAX_VALUE), abs(-${number.className}.raw(${number.internalType}.MAX_VALUE)))")
        if (number.checkOverflow) {
            writer.println("\t\tassertThrows(FixedPointException::class.java) { abs(${number.className}.raw(${number.internalType}.MIN_VALUE)) }")
        }
        writer.println("\t}")
    }

    private fun generateMinMax() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testMinMax() {")

        fun generate(a: String, b: String, minA: Boolean) {
            val smallest = if (minA) a else b
            val largest = if (minA) b else a
            writer.println("\t\tassertEquals($smallest, min($a, $b))")
            writer.println("\t\tassertEquals($largest, max($a, $b))")
        }

        generate("${number.className}.ZERO", "${number.className}.ZERO", false)
        generate("${number.className}.ONE", "${number.className}.ZERO", false)
        generate("${number.className}.ZERO", "${number.className}.ONE", true)
        generate("${number.className}.ZERO", "${number.className}.raw(${number.internalType}.MAX_VALUE)", true)
        if (number.internalType.signed) {
            generate("-${number.className}.ONE", "${number.className}.ZERO", true)
            generate("${number.className}.ZERO", "${number.className}.raw(${number.internalType}.MIN_VALUE)", false)
        }

        writer.println("\t}")
    }
}
