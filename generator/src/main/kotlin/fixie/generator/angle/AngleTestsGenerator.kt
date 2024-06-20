package fixie.generator.angle

import java.io.PrintWriter

internal class AngleTestsGenerator(
        private val writer: PrintWriter,
        private val angle: AngleClass,
        private val packageName: String
) {

    private val suffix = if (angle.internalType.signed) "" else "u"

    fun generate() {
        generateClassPrefix()
        generateNearlyEquals()
        generateToString()
        generateCompanionConstructors()
        generateCompareTo()
        generateArithmetic()
        generateExtensionFunctions()
        generateMathFunctions()
        writer.println("}")
    }

    private fun generateClassPrefix() {
        writer.println("package $packageName")
        writer.println()
        writer.println("import org.junit.jupiter.api.Test")
        writer.println("import org.junit.jupiter.api.Assertions.*")
        writer.println("import org.junit.jupiter.api.assertThrows")
        writer.println("import kotlin.math.PI")
        if (angle.allowDivisionAndFloatMultiplication && angle.spinClass != null) {
            writer.println("import kotlin.time.Duration.Companion.seconds")
        }
        writer.println()
        writer.println("class Test${angle.className} {")
    }

    private fun generateNearlyEquals() {
        writer.println()
        writer.println("\tprivate fun assertNearlyEquals(expected: ${angle.className}, actual: ${angle.className}) {")
        writer.println("\t\tval difference = (expected - actual).toDouble(AngleUnit.DEGREES)")
        val maxError = if (angle.internalType.numBytes == 1) 3.0 else 0.1
        if (angle.internalType.signed) {
            writer.println("\t\tif (difference > $maxError || difference < -$maxError) assertEquals(expected, actual)")
        } else {
            writer.println("\t\tif (difference > $maxError && difference < ${360 - maxError}) assertEquals(expected, actual)")
        }
        writer.println("\t}")
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testAssertNearlyEquals() {")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(123), ${angle.className}.degrees(123))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(123), ${angle.className}.degrees(123.0001))")
        writer.println("\t\tassertThrows<AssertionError> { assertNearlyEquals(${angle.className}.degrees(0), ${angle.className}.degrees(5)) }")
        writer.println("\t\tassertThrows<AssertionError> { assertNearlyEquals(${angle.className}.degrees(0), ${angle.className}.degrees(-5)) }")
        writer.println("\t\tassertThrows<AssertionError> { assertNearlyEquals(${angle.className}.degrees(355), ${angle.className}.degrees(359)) }")
        writer.println("\t}")
    }

    private fun generateToString() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToString() {")
        if (angle.internalType.numBytes > 1) {
            writer.println("\t\tassertEquals(\"100°\", ${angle.className}.degrees(100).toString(AngleUnit.DEGREES, 1))")
            writer.println("\t\tassertEquals(\"1.23rad\", ${angle.className}.radians(1.23).toString(AngleUnit.RADIANS, 2))")
        }
        writer.println("\t\tassertEquals(\"0°\", ${angle.className}.degrees(0).toString(AngleUnit.DEGREES, 1))")
        writer.println("\t\tassertEquals(\"0rad\", ${angle.className}.degrees(0).toString(AngleUnit.RADIANS, 2))")
        writer.println("\t\tassertEquals(\"0°\", ${angle.className}.radians(0L).toString(AngleUnit.DEGREES, 1))")
        writer.println("\t\tassertEquals(\"0rad\", ${angle.className}.radians(0).toString(AngleUnit.RADIANS, 2))")
        writer.println("\t\tassertEquals(\"90°\", ${angle.className}.radians(0.5 * PI).toString(AngleUnit.DEGREES, 0))")

        writer.println("\t}")
    }

    private fun generateCompanionConstructors() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompanionConstructors() {")
        if (angle.internalType.numBytes == 1) {
            writer.println("\t\tassertNotEquals(${angle.className}.degrees(100), ${angle.className}.degrees(103))")
            writer.println("\t\tassertNotEquals(${angle.className}.radians(1.2), ${angle.className}.radians(1.3))")
        } else {
            writer.println("\t\tassertNotEquals(${angle.className}.degrees(100), ${angle.className}.degrees(101))")
            writer.println("\t\tassertNotEquals(${angle.className}.radians(1.2), ${angle.className}.radians(1.21))")
        }
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(100), ${angle.className}.degrees(820))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(60f), ${angle.className}.degrees(-300f))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(260.0), ${angle.className}.degrees(-100))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(0), ${angle.className}.degrees(360))")
        val degreeDelta = if (angle.internalType.numBytes == 1) 2.0 else 0.01
        writer.println("\t\tassertEquals(123.0, ${angle.className}.degrees(123).toDouble(AngleUnit.DEGREES), $degreeDelta)")
        writer.println("\t\tassertEquals(45.0, ${angle.className}.degrees(45f).toDouble(AngleUnit.DEGREES), $degreeDelta)")
        val expectedValue = if (angle.internalType.signed) -60.0 else 300.0
        writer.println("\t\tassertEquals($expectedValue, ${angle.className}.degrees(-60.0).toDouble(AngleUnit.DEGREES), $degreeDelta)")
        writer.println("\t\tassertEquals(1.23, ${angle.className}.radians(1.23).toDouble(AngleUnit.RADIANS), ${degreeDelta / 10})")
        writer.println(angle.internalType.declareValue("\t\tval rawValue", 123))
        writer.println("\t\tassertEquals(rawValue, ${angle.className}.raw(rawValue).raw)")
        writer.println("\t}")
    }

    private fun generateCompareTo() {
        if (angle.allowComparisons) {
            writer.println()
            writer.println("\t@Test")
            writer.println("\tfun testCompareTo() {")
            writer.println("\t\tassertTrue(${angle.className}.raw(40$suffix) < ${angle.className}.raw(41$suffix))")
            writer.println("\t\tassertTrue(${angle.className}.raw(40$suffix) <= ${angle.className}.raw(41$suffix))")
            writer.println("\t\tassertTrue(${angle.className}.raw(40$suffix) <= ${angle.className}.raw(40$suffix))")
            writer.println("\t\tassertTrue(${angle.className}.raw(40$suffix) >= ${angle.className}.raw(40$suffix))")
            writer.println("\t\tassertFalse(${angle.className}.raw(40$suffix) >= ${angle.className}.raw(41$suffix))")
            writer.println("\t\tassertFalse(${angle.className}.raw(40$suffix) > ${angle.className}.raw(41$suffix))")
            writer.println()
            writer.println("\t\tassertFalse(${angle.className}.raw(40$suffix) < ${angle.className}.raw(39$suffix))")
            writer.println("\t\tassertFalse(${angle.className}.raw(40$suffix) <= ${angle.className}.raw(39$suffix))")
            writer.println("\t\tassertTrue(${angle.className}.raw(40$suffix) >= ${angle.className}.raw(39$suffix))")
            writer.println("\t\tassertTrue(${angle.className}.raw(40$suffix) > ${angle.className}.raw(39$suffix))")
            writer.println()
            writer.println("\t\tassertTrue(${angle.className}.degrees(100) > ${angle.className}.radians(0.5 * PI))")
            writer.println("\t\tassertFalse(${angle.className}.degrees(100) > ${angle.className}.radians(0.7 * PI))")
            writer.println("\t\tassertTrue(${angle.className}.degrees(50) < ${angle.className}.degrees(100))")
            writer.println("\t\tassertFalse(${angle.className}.radians(3f) < ${angle.className}.radians(2.9))")

            if (angle.internalType.signed) {
                writer.println("\t\tassertTrue(${angle.className}.degrees(-50) < ${angle.className}.degrees(0))")
                writer.println("\t\tassertTrue(${angle.className}.degrees(300) < ${angle.className}.degrees(0))")
                writer.println("\t\tfor (angle in -170 until 170) assertTrue(${angle.className}.degrees(angle) < ${angle.className}.degrees(angle + 10))")
                writer.println("\t\tfor (angle in -400 until -200) assertTrue(${angle.className}.degrees(angle) < ${angle.className}.degrees(angle + 10))")
                writer.println("\t\tfor (angle in 190 until 400) assertTrue(${angle.className}.degrees(angle) < ${angle.className}.degrees(angle + 10))")
                writer.println("\t\tfor (angle in -3 until 3) assertTrue(${angle.className}.radians(angle) < ${angle.className}.radians(angle + 1.0))")
                writer.println("\t\tfor (angle in -8 until -4) assertTrue(${angle.className}.radians(angle) < ${angle.className}.radians(angle + 1.0))")
                writer.println("\t\tfor (angle in 4 until 9) assertTrue(${angle.className}.radians(angle) < ${angle.className}.radians(angle + 1.0))")
            } else {
                writer.println("\t\tfor (angle in 0 until 350) assertTrue(${angle.className}.degrees(angle) < ${angle.className}.degrees(angle + 10))")
                writer.println("\t\tfor (angle in 370 until 700) assertTrue(${angle.className}.degrees(angle) < ${angle.className}.degrees(angle + 10))")
                writer.println("\t\tfor (angle in 0 until 6) assertTrue(${angle.className}.radians(angle) < ${angle.className}.radians(angle + 1.0))")
                writer.println("\t\tfor (angle in 7 until 12) assertTrue(${angle.className}.radians(angle) < ${angle.className}.radians(angle + 1.0))")
            }

            writer.println("\t}")
        }
    }

    private fun generateArithmetic() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testArithmetic() {")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(50), ${angle.className}.degrees(30) + ${angle.className}.degrees(20))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.radians(0f), ${angle.className}.degrees(180) + ${angle.className}.degrees(180))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.radians(0f), ${angle.className}.radians(PI) + ${angle.className}.degrees(180))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.radians(0.25f * PI.toFloat()), ${angle.className}.radians(0.75 * PI) - ${angle.className}.degrees(90))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(300), ${angle.className}.degrees(320) + ${angle.className}.degrees(340))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(30f), ${angle.className}.degrees(50) - ${angle.className}.degrees(20))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(180), ${angle.className}.degrees(180L) - ${angle.className}.degrees(0))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(320L), ${angle.className}.degrees(300) - ${angle.className}.degrees(340))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(100L), 2 * ${angle.className}.degrees(50))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(100), 2L * ${angle.className}.degrees(50))")
        if (angle.allowDivisionAndFloatMultiplication) {
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(50), ${angle.className}.degrees(100) / 2)")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(50), ${angle.className}.degrees(100) / 2L)")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(100), 2f * ${angle.className}.degrees(50))")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(50), ${angle.className}.degrees(100) / 2f)")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(100), 2.0 * ${angle.className}.degrees(50))")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(50), ${angle.className}.degrees(100) / 2.0)")

            if (angle.internalType.signed) {
                writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(-90), 1.5 * ${angle.className}.degrees(-60))")
                writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(-60), ${angle.className}.degrees(-90) / 1.5)")
                writer.println("\t\t// Note that 1.5 * 160 = 240 = 360 - 120")
                writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(-120), 1.5 * ${angle.className}.degrees(160))")
                writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(120), 1.5 * ${angle.className}.degrees(-160))")
            } else {
                writer.println("\t\t// Note that 1.5 * 300 = 450 = 360 + 90")
                writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(90), 1.5 * ${angle.className}.degrees(300))")
                writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(60), ${angle.className}.degrees(90) / 1.5)")
            }

            if (angle.spinClass != null) {
                writer.println("\t\tassertEquals(5.0, (${angle.className}.degrees(15) / 3.seconds).toDouble(SpinUnit.DEGREES_PER_SECOND), 0.001)")
            }
        }
        writer.println("\t}")
    }

    private fun generateExtensionFunctions() {
        if (angle.createNumberExtensions) {
            writer.println()
            writer.println("\t@Test")
            writer.println("\tfun testExtensionFunctions() {")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(123), 123.degrees)")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(123), 123L.degrees)")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(-45), -45f.degrees)")
            writer.println("\t\tassertNearlyEquals(${angle.className}.radians(1.5), 1.5.radians)")
            if (angle.allowComparisons) writer.println("\t\tassertTrue(1L.degrees < 1.radians)")
            writer.println("\t}")
        }
    }

    private fun generateMathFunctions() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testMathFunctions() {")
        if (angle.internalType.signed) {
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(0), abs(${angle.className}.degrees(0)))")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(40), abs(${angle.className}.degrees(40)))")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(40), abs(${angle.className}.degrees(-40)))")
        }
        if (angle.allowComparisons) {
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(4), min(${angle.className}.degrees(6), ${angle.className}.degrees(4)))")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(6), max(${angle.className}.degrees(6), ${angle.className}.degrees(4)))")
        }
        val margin = if (angle.internalType.numBytes == 1) "0.1" else "0.01"
        writer.println("\t\tassertEquals(0.5, sin(${angle.className}.degrees(30)), $margin)")
        writer.println("\t\tassertEquals(-1.0, cos(${angle.className}.radians(PI)), $margin)")
        writer.println("\t\tassertEquals(1.0, tan(${angle.className}.degrees(45)), $margin)")
        writer.println("\t}")
    }
}
