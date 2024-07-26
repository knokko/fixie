package fixie.generator.quantity

import fixie.generator.number.FloatType
import fixie.generator.number.NumberClass
import java.io.PrintWriter
import java.math.BigInteger
import kotlin.math.max
import kotlin.math.min

abstract class QuantityTestsGenerator<T: QuantityClass>(
    protected val writer: PrintWriter,
    protected val quantity: T,
    protected val packageName: String
) {

    fun generate() {
        generateClassPrefix()
        generateNearlyEquals()
        generateToDouble()
        generateToString()
        generateCompareTo()
        generateArithmetic()
        generateExtensionFunctions()
        generateMathFunctions()
        writer.println("}")
    }

    protected abstract fun canBeNegative(): Boolean

    protected open fun getImports() = arrayOf(
        "org.junit.jupiter.api.Test",
        "org.junit.jupiter.api.Assertions.*",
        "org.junit.jupiter.api.assertThrows"
    )

    protected open fun generateClassPrefix() {
        writer.println("package $packageName")
        writer.println()
        for (value in getImports()) {
            writer.println("import $value")
        }
        writer.println()
        writer.println("class Test${quantity.className} {")
    }

    protected fun generateFixedNearlyEquals(number: NumberClass, oneUnitName: String) {
        val toBaseDouble = ".toDouble(${getUnits()[0].enumName}.$oneUnitName)"
        val margin = max(0.001, 1.0 / number.oneValue.toDouble())

        writer.println()
        writer.println("\tprivate fun assertEquals(a: ${quantity.className}, b: ${quantity.className}, margin: Double = $margin) {")
        writer.println("\t\tassertEquals(a$toBaseDouble, b$toBaseDouble, margin)")
        writer.println("\t}")

        writer.println()
        writer.println("\tprivate fun assertNotEquals(a: ${quantity.className}, b: ${quantity.className}, margin: Double = $margin) {")
        writer.println("\t\tassertNotEquals(a$toBaseDouble, b$toBaseDouble, margin)")
        writer.println("\t}")

        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testAssertEquals() {")
        val unsignedSuffix = if (number.internalType.signed) "" else "u"
        val baseValue = if (number.oneValue < BigInteger.valueOf(50)) {
            if (number.internalType.numBytes == 1) "${quantity.className}.raw(50$unsignedSuffix)"
            else "${quantity.className}.raw(5000$unsignedSuffix)"
        } else if (number.internalType.getMaxValue() / number.oneValue <= BigInteger.TWO) {
            "${quantity.className}.raw(${number.internalType.getMaxValue() / BigInteger.TWO}$unsignedSuffix)"
        } else {
            "${quantity.className}.$oneUnitName"
        }
        writer.println("\t\tval base = $baseValue")

        val (intPart, base) = if (number.internalType.getMaxValue() / number.oneValue > BigInteger.TWO) {
            Pair("1", 1.23)
        } else Pair("0", 0.23)
        writer.println("\t\tassertEquals($intPart.23 * base, $intPart.23 * base)")
        writer.println("\t\tassertNotEquals($intPart.23 * base, ${base + 0.02 + 2 * margin} * base)")
        writer.println("\t\tassertEquals($intPart.23 * base, $intPart.23001 * base)")

        val customMargin = 0.2 / (2.23 - base)
        if (margin < 0.05) {
            writer.println("\t\tassertEquals($intPart.5 * base, $intPart.6 * base, $customMargin)")
            writer.println("\t\tassertNotEquals($intPart.5 * base, $intPart.8 * base, $customMargin)")
        }
        writer.println("\t\tassertThrows<AssertionError> { assertNotEquals($intPart.23 * base, $intPart.23 * base) }")
        writer.println("\t\tassertThrows<AssertionError> { assertEquals($intPart.23 * base, ${base + 0.02 + 2 * margin} * base) }")
        writer.println("\t\tassertThrows<AssertionError> { assertNotEquals($intPart.23 * base, $intPart.23001 * base) }")
        if (margin < 0.05) {
            writer.println("\t\tassertThrows<AssertionError> { assertNotEquals($intPart.5 * base, $intPart.6 * base, $customMargin) }")
            writer.println("\t\tassertThrows<AssertionError> { assertEquals($intPart.5 * base, $intPart.8 * base, $customMargin) }")
        }
        writer.println("\t}")
    }

    protected fun generateFloatNearlyEquals(floatType: FloatType) {
        val suffix = if (floatType == FloatType.SinglePrecision) "f" else ""

        val toRightType = if (floatType == FloatType.SinglePrecision) ".toFloat()" else ""
        writer.println()
        writer.println("\tprivate fun assertEquals(a: ${quantity.className}, b: ${quantity.className}, margin: Double = 0.001) {")
        writer.println("\t\tassertEquals(a.value, b.value, margin$toRightType)")
        writer.println("\t}")

        writer.println()
        writer.println("\tprivate fun assertNotEquals(a: ${quantity.className}, b: ${quantity.className}, margin: Double = 0.001) {")
        writer.println("\t\tassertNotEquals(a.value, b.value, margin$toRightType)")
        writer.println("\t}")

        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testAssertEquals() {")
        writer.println("\t\tassertEquals(${quantity.className}(1.23$suffix), ${quantity.className}(1.23$suffix))")
        writer.println("\t\tassertNotEquals(${quantity.className}(1.23$suffix), ${quantity.className}(1.24$suffix))")
        writer.println("\t\tassertEquals(${quantity.className}(1.23$suffix), ${quantity.className}(1.23001$suffix))")
        writer.println("\t\tassertEquals(${quantity.className}(1.5$suffix), ${quantity.className}(1.6$suffix), 0.2)")
        writer.println("\t\tassertNotEquals(${quantity.className}(1.5$suffix), ${quantity.className}(1.8$suffix), 0.2)")
        writer.println("\t\tassertThrows<AssertionError> { assertNotEquals(${quantity.className}(1.23$suffix), ${quantity.className}(1.23$suffix)) }")
        writer.println("\t\tassertThrows<AssertionError> { assertEquals(${quantity.className}(1.23$suffix), ${quantity.className}(1.24$suffix)) }")
        writer.println("\t\tassertThrows<AssertionError> { assertNotEquals(${quantity.className}(1.23$suffix), ${quantity.className}(1.23001$suffix)) }")
        writer.println("\t\tassertThrows<AssertionError> { assertNotEquals(${quantity.className}(1.5$suffix), ${quantity.className}(1.6$suffix), 0.2) }")
        writer.println("\t\tassertThrows<AssertionError> { assertEquals(${quantity.className}(1.5$suffix), ${quantity.className}(1.8$suffix), 0.2) }")
        writer.println("\t}")
    }

    protected abstract fun generateNearlyEquals()

    protected fun determineFixedUnitMinDelta(
        rawValue: BigInteger, number: NumberClass
    ) = max(1.0 / min(rawValue.toDouble(), number.oneValue.toDouble()), 1e-8)

    protected fun determineFixedUnitMaxAmount(
        rawValue: BigInteger, number: NumberClass
    ) = (number.internalType.getMaxValue().toBigDecimal(10) / rawValue.toBigDecimal(10)).toDouble()

    protected abstract fun getUnits(): List<QuantityUnit>

    protected abstract fun canSupportMultipleUnits(): Boolean

    protected open fun generateToDoubleBody() {
        val units = getUnits()

        // TODO Automate code generation of toDouble()
        if (units.isEmpty()) throw IllegalArgumentException("There are no units")
        if (!canSupportMultipleUnits()) {
            val unit = units.first()
            writer.println("\t\tassertEquals(0.234, (0.234 * ${quantity.className}.${unit.name}).toDouble(), ${2 * unit.minDelta})")
        } else {
            for ((index, unit) in units.withIndex()) {
                writer.println("\t\tassertEquals(1.0, ${quantity.className}.${unit.name}.toDouble(${unit.enumName}.${unit.name}), ${2 * unit.minDelta})")
                writer.println("\t\tassertEquals(0.234, (0.234 * ${quantity.className}.${unit.name}).toDouble(${unit.enumName}.${unit.name}), ${2 * unit.minDelta})")
                if (index + 1 < units.size) {
                    writer.println("\t\tassertFalse(${quantity.className}.${unit.name} == ${quantity.className}.${units[index + 1].name})")
                }
            }
        }
    }

    private fun generateToDouble() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToDouble() {")
        generateToDoubleBody()
        writer.println("\t}")
    }

    protected abstract fun generateToStringBody()

    private fun generateToString() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToString() {")
        generateToStringBody()
        writer.println("\t}")
    }

    protected open fun generateCompareToBody() {
        for (unit in getUnits()) {
            val unitConstant = "${quantity.className}.${unit.name}"
            writer.println("\t\tassertTrue($unitConstant >= $unitConstant)")
            writer.println("\t\tassertTrue($unitConstant <= $unitConstant)")
            writer.println("\t\tassertFalse($unitConstant > $unitConstant)")
            writer.println("\t\tassertFalse($unitConstant < $unitConstant)")
            if (unit.minDelta < 0.1) {
                writer.println("\t\tassertTrue($unitConstant > $unitConstant * 0.8f)")
                writer.println("\t\tassertFalse($unitConstant < $unitConstant * 0.8)")

                if (canBeNegative()) {
                    writer.println("\t\tassertTrue($unitConstant / 2 > -$unitConstant)")
                    writer.println("\t\tassertTrue(-$unitConstant / 2L > -$unitConstant)")
                }
            }
        }

        val units = getUnits()
        for ((index, unit) in units.withIndex()) {
            if (index != 0) {
                val previousUnit = units[index - 1]
                writer.println("\t\tassertTrue(${quantity.className}.${unit.name} > ${quantity.className}.${previousUnit.name})")
            }
        }
    }

    protected fun generateExtraFixedCompareToChecks(number: NumberClass, oneUnitName: String) {
        if (number.oneValue > BigInteger.TEN) {
            writer.println("\t\tassertNotEquals(${quantity.className}.$oneUnitName * 0.9, ${quantity.className}.$oneUnitName)")
            writer.println("\t\tassertTrue(${quantity.className}.$oneUnitName > ${quantity.className}.$oneUnitName * 0.9)")
        } else {
            writer.println("\t\tassertNotEquals(${quantity.className}.$oneUnitName * 0.2, ${quantity.className}.$oneUnitName)")
            writer.println("\t\tassertTrue(${quantity.className}.$oneUnitName > ${quantity.className}.$oneUnitName * 0.3)")
        }
    }

    private fun generateCompareTo() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompareTo() {")
        generateCompareToBody()
        writer.println("\t}")
    }

    protected fun generateExtraFixedArithmeticChecks(number: NumberClass) {
        val suffix = if (number.internalType.signed) "" else "u"
        val safeMaxValue = (number.internalType.getMaxValue() / number.oneValue).min(BigInteger.TWO.pow(40))
        writer.println("\t\tassertEquals(${quantity.className}.raw($safeMaxValue$suffix), ${quantity.className}.raw(1$suffix) * $safeMaxValue.0)")
    }

    protected open fun generateArithmeticBody() {
        for (unit in getUnits()) {
            val margin = max(0.001, 5 * unit.minDelta)
            val unitConstant = "${quantity.className}.${unit.name}"

            writer.println("\t\tassertEquals($unitConstant / 2, $unitConstant - 0.5f * $unitConstant, $margin)")
            if (unit.minDelta < 0.1) {
                writer.println("\t\tassertEquals($unitConstant, $unitConstant / 4L + 0.75 * $unitConstant, $margin)")
            }

            if (unit.maxAmount > 4.0) {
                writer.println("\t\tassertEquals(0.25, $unitConstant / (4 * $unitConstant), $margin)")
            }

            if (canBeNegative()) {
                writer.println("\t\tassertEquals(-$unitConstant / 2, $unitConstant / 2 - $unitConstant, $margin)")
            }
        }
    }

    private fun generateArithmetic() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testArithmetic() {")
        generateArithmeticBody()
        writer.println("\t}")
    }

    protected open fun generateExtensionFunctionsBody() {
        if (quantity.createNumberExtensions) {
            for (unit in getUnits()) {
                writer.println("\t\tassertEquals(0.8 * ${quantity.className}.${unit.name}, 0.8.${unit.extensionName})")
                writer.println("\t\tassertEquals(0.6f * ${quantity.className}.${unit.name}, 0.6f.${unit.extensionName})")
                writer.println("\t\tassertEquals(${quantity.className}.${unit.name}, 1.${unit.extensionName})")
                writer.println("\t\tassertEquals(${quantity.className}.${unit.name}, 1L.${unit.extensionName})")
            }
        }

        for (unit in getUnits()) {
            val unitConstant = "${quantity.className}.${unit.name}"
            writer.println("\t\tassertEquals(0.8 * $unitConstant, $unitConstant * 0.8)")
            writer.println("\t\tassertEquals(0.3f * $unitConstant, $unitConstant * 0.3f)")
            writer.println("\t\tassertEquals(1 * $unitConstant, $unitConstant * 1)")
            if (unit.maxAmount > 2.0) writer.println("\t\tassertEquals(2L * $unitConstant, $unitConstant * 2L)")
        }
    }

    private fun generateExtensionFunctions() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testExtensionFunctions() {")
        generateExtensionFunctionsBody()
        writer.println("\t}")
    }

    protected open fun generateMathFunctionsBody() {
        val unit = getUnits()[getUnits().size / 2]
        val unitConstant = "${quantity.className}.${unit.name}"

        if (canBeNegative()) {
            writer.println("\t\tassertEquals($unitConstant, abs($unitConstant))")
            writer.println("\t\tassertEquals($unitConstant / 2, abs($unitConstant / 2))")
            writer.println("\t\tassertEquals(0 * $unitConstant, abs(0 * $unitConstant))")
            writer.println("\t\tassertEquals($unitConstant, abs(-$unitConstant))")
        }

        writer.println("\t\tassertEquals($unitConstant / 2, min($unitConstant, $unitConstant / 2))")
        writer.println("\t\tassertEquals($unitConstant, max($unitConstant, $unitConstant / 2))")
        if (canBeNegative()) {
            writer.println("\t\tassertEquals(-$unitConstant, min(-$unitConstant, -$unitConstant / 2))")
            writer.println("\t\tassertEquals(-$unitConstant / 2, max(-$unitConstant, -$unitConstant / 2))")
        }
        writer.println("\t\tassertEquals($unitConstant * 0, min($unitConstant, $unitConstant * 0))")
        writer.println("\t\tassertEquals($unitConstant, max($unitConstant, $unitConstant * 0))")
        if (canBeNegative()) {
            writer.println("\t\tassertEquals(-$unitConstant, min(-$unitConstant, -$unitConstant * 0))")
            writer.println("\t\tassertEquals(-$unitConstant * 0, max(-$unitConstant, -$unitConstant * 0))")
            writer.println("\t\tassertEquals(-$unitConstant, min($unitConstant / 2, -$unitConstant))")
            writer.println("\t\tassertEquals($unitConstant / 2, max($unitConstant / 2, -$unitConstant))")
        }
    }

    private fun generateMathFunctions() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testMathFunctions() {")
        generateMathFunctionsBody()
        writer.println("\t}")
    }
}
