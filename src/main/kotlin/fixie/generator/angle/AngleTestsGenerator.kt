package fixie.generator.angle

import fixie.generator.quantity.QuantityTestsGenerator
import fixie.generator.quantity.QuantityUnit
import java.io.PrintWriter
import java.util.*

internal class AngleTestsGenerator(
        writer: PrintWriter,
        angle: AngleClass,
        packageName: String
) : QuantityTestsGenerator<AngleClass>(writer, angle, packageName) {
    override fun canBeNegative() = quantity.internalType.signed

    override fun getImports() = super.getImports() + arrayOf("kotlin.math.PI") + if (
        quantity.allowDivisionAndFloatMultiplication && quantity.spinClass != null
    ) arrayOf("kotlin.time.Duration.Companion.seconds") else emptyArray()

    override fun generateNearlyEquals() {

        val maxError = if (quantity.internalType.numBytes == 1) 3.0 else 0.1
        writer.println()
        writer.println("\tprivate fun assertEquals(expected: ${quantity.className}, actual: ${quantity.className}) {")
        writer.println("\t\tval difference = (expected - actual).toDouble(AngleUnit.DEGREES)")
        if (quantity.internalType.signed) {
            writer.println("\t\tif (difference > $maxError || difference < -$maxError) " +
                    "org.junit.jupiter.api.Assertions.assertEquals(expected, actual)")
        } else {
            writer.println("\t\tif (difference > $maxError && difference < ${360 - maxError}) " +
                    "org.junit.jupiter.api.Assertions.assertEquals(expected, actual)")
        }
        writer.println("\t}")
        writer.println()
        writer.println("\tprivate fun assertNotEquals(expected: ${quantity.className}, actual: ${quantity.className}) {")
        writer.println("\t\tassertThrows<AssertionError> { assertEquals(expected, actual) }")
        writer.println("\t}")
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testAssertEquals() {")
        writer.println("\t\tassertEquals(${quantity.className}.degrees(123), ${quantity.className}.degrees(123))")
        writer.println("\t\tassertThrows<AssertionError> { assertNotEquals(${quantity.className}.degrees(123), ${quantity.className}.degrees(123)) }")
        writer.println("\t\tassertEquals(${quantity.className}.degrees(123), ${quantity.className}.degrees(123.0001))")
        writer.println("\t\tassertThrows<AssertionError> { assertEquals(${quantity.className}.degrees(0), ${quantity.className}.degrees(5)) }")
        writer.println("\t\tassertNotEquals(${quantity.className}.degrees(0), ${quantity.className}.degrees(5))")
        writer.println("\t\tassertThrows<AssertionError> { assertEquals(${quantity.className}.degrees(0), ${quantity.className}.degrees(-5)) }")
        writer.println("\t\tassertThrows<AssertionError> { assertEquals(${quantity.className}.degrees(355), ${quantity.className}.degrees(359)) }")
        writer.println("\t}")
    }

    override fun getUnits() = AngleUnit.entries.map { QuantityUnit(
        name = it.name,
        enumName = "AngleUnit",
        suffix = it.suffix,
        extensionName = it.name.lowercase(Locale.ROOT),
        minDelta = quantity.internalType.getMaxValue().toDouble() / it.maxValue,
        maxAmount = 1e6
    ) }

    override fun canSupportMultipleUnits() = true

    override fun generateToDoubleBody() {
        if (quantity.allowDivisionAndFloatMultiplication) super.generateToDoubleBody()
        val degreesMargin = if (quantity.internalType.numBytes == 1) "3.0" else "0.1"
        writer.println("\t\tassertEquals(12.0, ${quantity.className}.degrees(12).toDouble(AngleUnit.DEGREES), $degreesMargin)")
        writer.println("\t\tassertEquals(90.0, ${quantity.className}.radians(0.5 * PI).toDouble(AngleUnit.DEGREES), $degreesMargin)")

        val radiansMargin = if (quantity.internalType.numBytes == 1) "0.1" else "0.001"
        writer.println("\t\tassertEquals(2.0, ${quantity.className}.radians(2).toDouble(AngleUnit.RADIANS), $radiansMargin)")
        writer.println("\t\tassertEquals(0.5 * PI, ${quantity.className}.degrees(90).toDouble(AngleUnit.RADIANS), $radiansMargin)")
    }

    override fun generateToStringBody() {
        if (quantity.internalType.numBytes > 1) {
            writer.println("\t\tassertEquals(\"100°\", ${quantity.className}.degrees(100).toString(AngleUnit.DEGREES, 1))")
            writer.println("\t\tassertEquals(\"1.23rad\", ${quantity.className}.radians(1.23).toString(AngleUnit.RADIANS, 2))")
        }
        writer.println("\t\tassertEquals(\"0°\", ${quantity.className}.degrees(0).toString(AngleUnit.DEGREES, 1))")
        writer.println("\t\tassertEquals(\"0rad\", ${quantity.className}.degrees(0).toString(AngleUnit.RADIANS, 2))")
        writer.println("\t\tassertEquals(\"0°\", ${quantity.className}.radians(0L).toString(AngleUnit.DEGREES, 1))")
        writer.println("\t\tassertEquals(\"0rad\", ${quantity.className}.radians(0).toString(AngleUnit.RADIANS, 2))")
        writer.println("\t\tassertEquals(\"90°\", ${quantity.className}.radians(0.5 * PI).toString(AngleUnit.DEGREES, 0))")
    }

    override fun generateCompareToBody() {
        val suffix = if (quantity.internalType.signed) "" else "u"
        if (quantity.allowComparisons) {
            if (quantity.allowDivisionAndFloatMultiplication) super.generateCompareToBody()
            writer.println("\t\tassertTrue(${quantity.className}.raw(40$suffix) < ${quantity.className}.raw(41$suffix))")
            writer.println("\t\tassertTrue(${quantity.className}.raw(40$suffix) <= ${quantity.className}.raw(41$suffix))")
            writer.println("\t\tassertTrue(${quantity.className}.raw(40$suffix) <= ${quantity.className}.raw(40$suffix))")
            writer.println("\t\tassertTrue(${quantity.className}.raw(40$suffix) >= ${quantity.className}.raw(40$suffix))")
            writer.println("\t\tassertFalse(${quantity.className}.raw(40$suffix) >= ${quantity.className}.raw(41$suffix))")
            writer.println("\t\tassertFalse(${quantity.className}.raw(40$suffix) > ${quantity.className}.raw(41$suffix))")
            writer.println()
            writer.println("\t\tassertFalse(${quantity.className}.raw(40$suffix) < ${quantity.className}.raw(39$suffix))")
            writer.println("\t\tassertFalse(${quantity.className}.raw(40$suffix) <= ${quantity.className}.raw(39$suffix))")
            writer.println("\t\tassertTrue(${quantity.className}.raw(40$suffix) >= ${quantity.className}.raw(39$suffix))")
            writer.println("\t\tassertTrue(${quantity.className}.raw(40$suffix) > ${quantity.className}.raw(39$suffix))")
            writer.println()
            writer.println("\t\tassertTrue(${quantity.className}.degrees(100) > ${quantity.className}.radians(0.5 * PI))")
            writer.println("\t\tassertFalse(${quantity.className}.degrees(100) > ${quantity.className}.radians(0.7 * PI))")
            writer.println("\t\tassertTrue(${quantity.className}.degrees(50) < ${quantity.className}.degrees(100))")
            writer.println("\t\tassertFalse(${quantity.className}.radians(3f) < ${quantity.className}.radians(2.9))")

            if (quantity.internalType.signed) {
                writer.println("\t\tassertTrue(${quantity.className}.degrees(-50) < ${quantity.className}.degrees(0))")
                writer.println("\t\tassertTrue(${quantity.className}.degrees(300) < ${quantity.className}.degrees(0))")
                writer.println("\t\tfor (angle in -170 until 170) assertTrue(${quantity.className}.degrees(angle) < ${quantity.className}.degrees(angle + 10))")
                writer.println("\t\tfor (angle in -400 until -200) assertTrue(${quantity.className}.degrees(angle) < ${quantity.className}.degrees(angle + 10))")
                writer.println("\t\tfor (angle in 190 until 400) assertTrue(${quantity.className}.degrees(angle) < ${quantity.className}.degrees(angle + 10))")
                writer.println("\t\tfor (angle in -3 until 3) assertTrue(${quantity.className}.radians(angle) < ${quantity.className}.radians(angle + 1.0))")
                writer.println("\t\tfor (angle in -8 until -4) assertTrue(${quantity.className}.radians(angle) < ${quantity.className}.radians(angle + 1.0))")
                writer.println("\t\tfor (angle in 4 until 9) assertTrue(${quantity.className}.radians(angle) < ${quantity.className}.radians(angle + 1.0))")
            } else {
                writer.println("\t\tfor (angle in 0 until 350) assertTrue(${quantity.className}.degrees(angle) < ${quantity.className}.degrees(angle + 10))")
                writer.println("\t\tfor (angle in 370 until 700) assertTrue(${quantity.className}.degrees(angle) < ${quantity.className}.degrees(angle + 10))")
                writer.println("\t\tfor (angle in 0 until 6) assertTrue(${quantity.className}.radians(angle) < ${quantity.className}.radians(angle + 1.0))")
                writer.println("\t\tfor (angle in 7 until 12) assertTrue(${quantity.className}.radians(angle) < ${quantity.className}.radians(angle + 1.0))")
            }
        }
        if (quantity.internalType.numBytes == 1) {
            writer.println("\t\tassertNotEquals(${quantity.className}.degrees(100), ${quantity.className}.degrees(105))")
            writer.println("\t\tassertNotEquals(${quantity.className}.radians(1.2), ${quantity.className}.radians(1.4))")
        } else {
            writer.println("\t\tassertNotEquals(${quantity.className}.degrees(100), ${quantity.className}.degrees(101))")
            writer.println("\t\tassertNotEquals(${quantity.className}.radians(1.2), ${quantity.className}.radians(1.21))")
        }
        writer.println("\t\tassertEquals(${quantity.className}.degrees(100), ${quantity.className}.degrees(820))")
        writer.println("\t\tassertEquals(${quantity.className}.degrees(60f), ${quantity.className}.degrees(-300f))")
        writer.println("\t\tassertEquals(${quantity.className}.degrees(260.0), ${quantity.className}.degrees(-100))")
        writer.println("\t\tassertEquals(${quantity.className}.degrees(0), ${quantity.className}.degrees(360))")
        val degreeDelta = if (quantity.internalType.numBytes == 1) 2.0 else 0.01
        writer.println("\t\tassertEquals(123.0, ${quantity.className}.degrees(123).toDouble(AngleUnit.DEGREES), $degreeDelta)")
        writer.println("\t\tassertEquals(45.0, ${quantity.className}.degrees(45f).toDouble(AngleUnit.DEGREES), $degreeDelta)")
        val expectedValue = if (quantity.internalType.signed) -60.0 else 300.0
        writer.println("\t\tassertEquals($expectedValue, ${quantity.className}.degrees(-60.0).toDouble(AngleUnit.DEGREES), $degreeDelta)")
        writer.println("\t\tassertEquals(1.23, ${quantity.className}.radians(1.23).toDouble(AngleUnit.RADIANS), ${degreeDelta / 10})")
        writer.println(quantity.internalType.declareValue("\t\tval rawValue", 123))
        writer.println("\t\tassertEquals(rawValue, ${quantity.className}.raw(rawValue).raw)")
    }

    override fun generateArithmeticBody() {
        writer.println("\t\tassertEquals(${quantity.className}.degrees(50), ${quantity.className}.degrees(30) + ${quantity.className}.degrees(20))")
        writer.println("\t\tassertEquals(${quantity.className}.radians(0f), ${quantity.className}.degrees(180) + ${quantity.className}.degrees(180))")
        writer.println("\t\tassertEquals(${quantity.className}.radians(0f), ${quantity.className}.radians(PI) + ${quantity.className}.degrees(180))")
        writer.println("\t\tassertEquals(${quantity.className}.radians(0.25f * PI.toFloat()), ${quantity.className}.radians(0.75 * PI) - ${quantity.className}.degrees(90))")
        writer.println("\t\tassertEquals(${quantity.className}.degrees(300), ${quantity.className}.degrees(320) + ${quantity.className}.degrees(340))")
        writer.println("\t\tassertEquals(${quantity.className}.degrees(30f), ${quantity.className}.degrees(50) - ${quantity.className}.degrees(20))")
        writer.println("\t\tassertEquals(${quantity.className}.degrees(180), ${quantity.className}.degrees(180L) - ${quantity.className}.degrees(0))")
        writer.println("\t\tassertEquals(${quantity.className}.degrees(320L), ${quantity.className}.degrees(300) - ${quantity.className}.degrees(340))")
        writer.println("\t\tassertEquals(${quantity.className}.degrees(100L), 2 * ${quantity.className}.degrees(50))")
        writer.println("\t\tassertEquals(${quantity.className}.degrees(100), 2L * ${quantity.className}.degrees(50))")
        if (quantity.allowDivisionAndFloatMultiplication) {
            writer.println("\t\tassertEquals(${quantity.className}.degrees(50), ${quantity.className}.degrees(100) / 2)")
            writer.println("\t\tassertEquals(${quantity.className}.degrees(50), ${quantity.className}.degrees(100) / 2L)")
            writer.println("\t\tassertEquals(${quantity.className}.degrees(100), 2f * ${quantity.className}.degrees(50))")
            writer.println("\t\tassertEquals(${quantity.className}.degrees(50), ${quantity.className}.degrees(100) / 2f)")
            writer.println("\t\tassertEquals(${quantity.className}.degrees(100), 2.0 * ${quantity.className}.degrees(50))")
            writer.println("\t\tassertEquals(${quantity.className}.degrees(50), ${quantity.className}.degrees(100) / 2.0)")

            if (quantity.internalType.signed) {
                writer.println("\t\tassertEquals(${quantity.className}.degrees(-90), 1.5 * ${quantity.className}.degrees(-60))")
                writer.println("\t\tassertEquals(${quantity.className}.degrees(-60), ${quantity.className}.degrees(-90) / 1.5)")
                writer.println("\t\t// Note that 1.5 * 160 = 240 = 360 - 120")
                writer.println("\t\tassertEquals(${quantity.className}.degrees(-120), 1.5 * ${quantity.className}.degrees(160))")
                writer.println("\t\tassertEquals(${quantity.className}.degrees(120), 1.5 * ${quantity.className}.degrees(-160))")
            } else {
                writer.println("\t\t// Note that 1.5 * 300 = 450 = 360 + 90")
                writer.println("\t\tassertEquals(${quantity.className}.degrees(90), 1.5 * ${quantity.className}.degrees(300))")
                writer.println("\t\tassertEquals(${quantity.className}.degrees(60), ${quantity.className}.degrees(90) / 1.5)")
            }

            if (quantity.spinClass != null) {
                val margin = if (quantity.internalType.numBytes > 1) 0.01 else 2.0
                writer.println("\t\tassertEquals(5.0, (${quantity.className}.degrees(15) / 3.seconds).toDouble(SpinUnit.DEGREES_PER_SECOND), $margin)")
            }
        }
    }

    override fun generateExtensionFunctionsBody() {
        if (quantity.createNumberExtensions) {
            writer.println("\t\tassertEquals(${quantity.className}.degrees(123), 123.degrees)")
            writer.println("\t\tassertEquals(${quantity.className}.degrees(123), 123L.degrees)")
            writer.println("\t\tassertEquals(${quantity.className}.degrees(-45), -45f.degrees)")
            writer.println("\t\tassertEquals(${quantity.className}.radians(1.5), 1.5.radians)")
        }
    }

    override fun generateMathFunctionsBody() {
        if (quantity.internalType.signed) {
            writer.println("\t\tassertEquals(${quantity.className}.degrees(0), abs(${quantity.className}.degrees(0)))")
            writer.println("\t\tassertEquals(${quantity.className}.degrees(40), abs(${quantity.className}.degrees(40)))")
            writer.println("\t\tassertEquals(${quantity.className}.degrees(40), abs(${quantity.className}.degrees(-40)))")
        }
        if (quantity.allowComparisons) {
            writer.println("\t\tassertEquals(${quantity.className}.degrees(4), min(${quantity.className}.degrees(6), ${quantity.className}.degrees(4)))")
            writer.println("\t\tassertEquals(${quantity.className}.degrees(6), max(${quantity.className}.degrees(6), ${quantity.className}.degrees(4)))")
        }
        val margin = if (quantity.internalType.numBytes == 1) "0.1" else "0.01"
        writer.println("\t\tassertEquals(0.5, sin(${quantity.className}.degrees(30)), $margin)")
        writer.println("\t\tassertEquals(-1.0, cos(${quantity.className}.radians(PI)), $margin)")
        writer.println("\t\tassertEquals(1.0, tan(${quantity.className}.degrees(45)), $margin)")
    }
}
