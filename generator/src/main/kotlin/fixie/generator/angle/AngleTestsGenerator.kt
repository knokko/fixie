package fixie.generator.angle

import java.io.PrintWriter

class AngleTestsGenerator(
        private val writer: PrintWriter,
        private val angle: AngleClass
) {

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
        writer.println("package fixie")
        writer.println()
        writer.println("import org.junit.jupiter.api.Test")
        writer.println("import org.junit.jupiter.api.Assertions.*")
        writer.println("import org.junit.jupiter.api.assertThrows")
        writer.println("import kotlin.math.PI")
        writer.println()
        writer.println("class Test${angle.className} {")
    }

    private fun generateNearlyEquals() {
        writer.println()
        writer.println("\tprivate fun assertNearlyEquals(expected: Angle, actual: Angle) {")
        writer.println("\t\tval difference = (expected - actual).toDouble(AngleUnit.DEGREES)")
        val maxError = if (angle.number.internalType.numBytes == 1) 3.0 else 0.1
        if (angle.number.internalType.signed) {
            writer.println("\t\tif (difference > $maxError || difference < -$maxError) assertEquals(expected, actual)")
        } else {
            writer.println("\t\tif (difference > $maxError && difference < ${360 - maxError}) assertEquals(expected, actual)")
        }
        writer.println("\t}")
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testAssertNearlyEquals() {")
        writer.println("\t\tassertNearlyEquals(Angle.degrees(123), Angle.degrees(123))")
        writer.println("\t\tassertNearlyEquals(Angle.degrees(123), Angle.degrees(123.0001))")
        writer.println("\t\tassertThrows<AssertionError> { assertNearlyEquals(Angle.degrees(0), Angle.degrees(5)) }")
        writer.println("\t\tassertThrows<AssertionError> { assertNearlyEquals(Angle.degrees(0), Angle.degrees(-5)) }")
        writer.println("\t\tassertThrows<AssertionError> { assertNearlyEquals(Angle.degrees(355), Angle.degrees(359)) }")
        writer.println("\t}")
    }

    private fun generateToString() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToString() {")
        if (angle.number.internalType.numBytes > 1) {
            writer.println("\t\tassertEquals(\"100°\", ${angle.className}.degrees(100).toString(AngleUnit.DEGREES, 1))")
            writer.println("\t\tassertEquals(\"1.23rad\", ${angle.className}.radians(1.23).toString(AngleUnit.RADIANS, 2))")
        }
        writer.println("\t\tassertEquals(\"0°\", ${angle.className}.degrees(0).toString(AngleUnit.DEGREES, 1))")
        writer.println("\t\tassertEquals(\"0rad\", ${angle.className}.degrees(0).toString(AngleUnit.RADIANS, 2))")
        writer.println("\t\tassertEquals(\"0°\", ${angle.className}.radians(0).toString(AngleUnit.DEGREES, 1))")
        writer.println("\t\tassertEquals(\"0rad\", ${angle.className}.radians(0).toString(AngleUnit.RADIANS, 2))")

        writer.println("\t}")
    }

    private fun generateCompanionConstructors() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompanionConstructors() {")
        if (angle.number.internalType.numBytes == 1) {
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
        val degreeDelta = if (angle.number.internalType.numBytes == 1) 2.0 else 0.01
        writer.println("\t\tassertEquals(123.0, ${angle.className}.degrees(123).toDouble(AngleUnit.DEGREES), $degreeDelta)")
        writer.println("\t\tassertEquals(45.0, ${angle.className}.degrees(45f).toDouble(AngleUnit.DEGREES), $degreeDelta)")
        val expectedValue = if (angle.number.internalType.signed) -60.0 else 300.0
        writer.println("\t\tassertEquals($expectedValue, ${angle.className}.degrees(-60.0).toDouble(AngleUnit.DEGREES), $degreeDelta)")
        writer.println("\t\tassertEquals(1.23, ${angle.className}.radians(1.23).toDouble(AngleUnit.RADIANS), ${degreeDelta / 10})")
        writer.println(angle.number.internalType.declareValue("\t\tval rawValue", 123))
        writer.println("\t\tassertEquals(rawValue, ${angle.className}.raw(rawValue).value.raw)")
        writer.println("\t}")
    }

    private fun generateCompareTo() {
        if (angle.allowComparisons) {
            writer.println()
            writer.println("\t@Test")
            writer.println("\tfun testCompareTo() {")
            writer.println("\t\tassertTrue(${angle.className}.raw(40) < ${angle.className}.raw(41))")
            writer.println("\t\tassertTrue(${angle.className}.raw(40) <= ${angle.className}.raw(41))")
            writer.println("\t\tassertTrue(${angle.className}.raw(40) <= ${angle.className}.raw(40))")
            writer.println("\t\tassertTrue(${angle.className}.raw(40) >= ${angle.className}.raw(40))")
            writer.println("\t\tassertFalse(${angle.className}.raw(40) >= ${angle.className}.raw(41))")
            writer.println("\t\tassertFalse(${angle.className}.raw(40) > ${angle.className}.raw(41))")
            writer.println()
            writer.println("\t\tassertFalse(${angle.className}.raw(40) < ${angle.className}.raw(39))")
            writer.println("\t\tassertFalse(${angle.className}.raw(40) <= ${angle.className}.raw(39))")
            writer.println("\t\tassertTrue(${angle.className}.raw(40) >= ${angle.className}.raw(39))")
            writer.println("\t\tassertTrue(${angle.className}.raw(40) > ${angle.className}.raw(39))")
            writer.println()
            writer.println("\t\tassertTrue(${angle.className}.degrees(100) > ${angle.className}.radians(0.5 * PI))")
            writer.println("\t\tassertFalse(${angle.className}.degrees(100) > ${angle.className}.radians(0.7 * PI))")
            writer.println("\t\tassertTrue(${angle.className}.degrees(50) < ${angle.className}.degrees(100))")
            writer.println("\t\tassertFalse(${angle.className}.radians(3) < ${angle.className}.radians(2.9))")

            if (angle.number.internalType.signed) {
                writer.println("\t\tassertTrue(${angle.className}.degrees(-50) < ${angle.className}.degrees(0))")
                writer.println("\t\tassertTrue(${angle.className}.degrees(300) < ${angle.className}.degrees(0))")
            }
            writer.println("\t}")
        }
    }

    private fun generateArithmetic() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testArithmetic() {")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(50), ${angle.className}.degrees(30) + ${angle.className}.degrees(20))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(0), ${angle.className}.degrees(180) + ${angle.className}.degrees(180))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(300), ${angle.className}.degrees(320) + ${angle.className}.degrees(340))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(30), ${angle.className}.degrees(50) - ${angle.className}.degrees(20))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(180), ${angle.className}.degrees(180) - ${angle.className}.degrees(0))")
        writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(320), ${angle.className}.degrees(300) - ${angle.className}.degrees(340))")
        if (angle.allowDivisionAndMultiplication) {
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(100), 2 * ${angle.className}.degrees(50))")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(50), ${angle.className}.degrees(100) / 2)")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(100), 2f * ${angle.className}.degrees(50))")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(50), ${angle.className}.degrees(100) / 2f)")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(100), 2.0 * ${angle.className}.degrees(50))")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(50), ${angle.className}.degrees(100) / 2.0)")

            if (angle.number.internalType.signed) {
                writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(-90), 1.5 * ${angle.className}.degrees(-60))")
                writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(-60), ${angle.className}.degrees(-90) / 1.5)")
            } else {
                writer.println("\t\t Note that 1.5 * 300 = 450 = 360 + 90")
                writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(90), 1.5 * ${angle.className}.degrees(300))")
                writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(60), ${angle.className}.degrees(90) / 1.5)")
            }
        }
        writer.println("\t}")
    }

    private fun generateExtensionFunctions() {
        if (angle.allowDivisionAndMultiplication || angle.createNumberExtensions) {
            writer.println()
            writer.println("\t@Test")
            writer.println("\tfun testExtensionFunctions() {")
            if (angle.allowDivisionAndMultiplication) {
                writer.println("\t\tassertNearlyEquals(${angle.className}.raw(63), 21 * ${angle.className}.raw(3))")
                writer.println("\t\tassertNearlyEquals(${angle.className}.raw(63), 21L * ${angle.className}.raw(3))")
                writer.println("\t\tassertNearlyEquals(${angle.className}.raw(63), 21f * ${angle.className}.raw(3))")
                writer.println("\t\tassertNearlyEquals(${angle.className}.raw(63), 21.0 * ${angle.className}.raw(3))")
            }
            if (angle.createNumberExtensions) {
                writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(123), 123.degrees)")
                writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(-45), -45f.degrees)")
                writer.println("\t\tassertNearlyEquals(${angle.className}.radians(1.5), 1.5.radians)")
                if (angle.allowComparisons) writer.println("\t\tassertTrue(1.degrees < 1.radians)")
            }
            writer.println("\t}")
        }
    }

    private fun generateMathFunctions() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testMathFunctions() {")
        if (angle.number.internalType.signed) {
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(0), abs(${angle.className}.degrees(0)))")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(40), abs(${angle.className}.degrees(40)))")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(40), abs(${angle.className}.degrees(-40)))")
        }
        if (angle.allowComparisons) {
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(4), min(${angle.className}.degrees(6), ${angle.className}.degrees(4)))")
            writer.println("\t\tassertNearlyEquals(${angle.className}.degrees(6), max(${angle.className}.degrees(6), ${angle.className}.degrees(4)))")
        }
        val margin = if (angle.number.internalType.numBytes == 1) "0.1" else "0.01"
        writer.println("\t\tassertEquals(0.5, sin(${angle.className}.degrees(30)), $margin)")
        writer.println("\t\tassertEquals(-1.0, cos(${angle.className}.radians(PI)), $margin)")
        writer.println("\t\tassertEquals(1.0, tan(${angle.className}.degrees(45)), $margin)")
        writer.println("\t}")
    }
}
