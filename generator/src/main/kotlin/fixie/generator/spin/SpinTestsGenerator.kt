package fixie.generator.spin

import fixie.generator.number.FloatType
import java.io.PrintWriter

class SpinTestsGenerator(
        private val writer: PrintWriter,
        private val spin: SpinClass,
        private val packageName: String
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
        writer.println("package $packageName")
        writer.println()
        writer.println("import org.junit.jupiter.api.Test")
        writer.println("import org.junit.jupiter.api.Assertions.*")
        writer.println("import org.junit.jupiter.api.assertThrows")
        writer.println("import kotlin.math.PI")
        if (spin.angleClassName != null) {
            writer.println("import kotlin.time.Duration.Companion.seconds")
        }
        writer.println()
        writer.println("class Test${spin.className} {")
        writer.println()
        writer.println("\tprivate val rps = ${spin.className}.RADIANS_PER_SECOND")
        writer.println("\tprivate val dps = ${spin.className}.DEGREES_PER_SECOND")
    }

    private fun generateNearlyEquals() {
        writer.println()
        writer.println("\tprivate fun assertNearlyEquals(expected: ${spin.className}, actual: ${spin.className}) {")
        writer.println("\t\tval difference = (expected - actual).toDouble(SpinUnit.DEGREES_PER_SECOND)")
        writer.println("\t\tif (difference > 0.1 || difference < -0.1) assertEquals(expected, actual)")
        writer.println("\t}")
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testAssertNearlyEquals() {")
        writer.println("\t\tassertNearlyEquals(123 * dps, dps * 123)")
        writer.println("\t\tassertNearlyEquals(123 * dps, dps * 123.0001)")
        writer.println("\t\tassertNearlyEquals(180 * dps, rps * Math.PI)")
        writer.println("\t\tassertThrows<AssertionError> { assertNearlyEquals(dps, dps * 5) }")
        writer.println("\t\tassertThrows<AssertionError> { assertNearlyEquals(dps * 355, dps * 355.5) }")
        writer.println("\t}")
    }

    private fun generateToString() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToString() {")
        writer.println("\t\tassertEquals(\"123°/s\", (123.45 * dps).toString(SpinUnit.DEGREES_PER_SECOND))")
        writer.println("\t\tassertEquals(\"1.23rad/s\", (1.23 * rps).toString(SpinUnit.RADIANS_PER_SECOND))")
        writer.println("\t\tassertEquals(\"-180°/s\", (-Math.PI * rps).toString(SpinUnit.DEGREES_PER_SECOND))")
        writer.println("\t\tassertEquals(\"3.14rad/s\", (180 * dps).toString(SpinUnit.RADIANS_PER_SECOND))")
        if (spin.displayUnit == SpinUnit.DEGREES_PER_SECOND) {
            writer.println("\t\tassertEquals(\"123°/s\", (123.45 * dps).toString())")
        } else {
            writer.println("\t\tassertEquals(\"3.14rad/s\", (180 * dps).toString())")
        }
        writer.println("\t}")
    }

    private fun generateCompanionConstructors() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompanionConstructors() {")
        writer.println("\t\tassertTrue(55 * dps < rps)")
        writer.println("\t\tassertFalse(60 * dps < rps)")
        writer.println("\t}")
    }

    private fun generateCompareTo() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompareTo() {")
        writer.println("\t\tassertTrue(20 * dps > -rps)")
        writer.println("\t\tassertFalse(20 * dps > rps)")
        writer.println("\t\tassertTrue(10 * rps > 5 * rps)")
        writer.println("\t\tassertFalse(10 * rps > 10 * rps)")
        writer.println("\t\tassertFalse(10 * rps > 15 * rps)")
        writer.println("\t\tassertFalse(10 * rps < 5 * rps)")
        writer.println("\t\tassertFalse(10 * rps < 10 * rps)")
        writer.println("\t\tassertTrue(10 * rps < 15 * rps)")
        writer.println("\t}")
    }

    private fun generateArithmetic() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testArithmetic() {")
        writer.println("\t\tassertNearlyEquals(120f * dps, PI * rps - dps * 60)")
        writer.println("\t\tassertNearlyEquals(5.0 * PI * rps, PI * rps / (1.0 / 3.0) + dps * 360L)")
        writer.println("\t\tassertNearlyEquals(-dps, dps / 0.1f - dps * 11f)")
        val suffix = if (spin.floatType == FloatType.SinglePrecision) "f" else ""
        writer.println("\t\tassertEquals(2.0$suffix, dps * 360 / (PI * rps), 0.001$suffix)")
        if (spin.angleClassName != null) {
            writer.println("\t\tassertEquals(150.0, (3.seconds * (50 * dps)).toDouble(AngleUnit.DEGREES), 0.5)")
        }
        writer.println("\t}")
    }

    private fun generateExtensionFunctions() {
        if (spin.createNumberExtensions) {
            writer.println()
            writer.println("\t@Test")
            writer.println("\tfun testExtensionFunctions() {")
            writer.println("\t\tassertNearlyEquals(rps / 10, 0.1.radps)")
            writer.println("\t\tassertNearlyEquals(dps / 2L, 0.5.degps)")
            writer.println("\t}")
        }
    }

    private fun generateMathFunctions() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testMathFunctions() {")
        writer.println("\t\tassertEquals(5 * rps, abs(5 * rps))")
        writer.println("\t\tassertEquals(5 * rps, abs(-5 * rps))")
        writer.println("\t\tassertEquals(0 * rps, abs(0 * rps))")
        writer.println("\t\tassertEquals(5 * dps, min(5 * dps, 10 * dps))")
        writer.println("\t\tassertEquals(-10 * dps, min(5 * dps, -10 * dps))")
        writer.println("\t\tassertEquals(10 * dps, max(5 * dps, 10 * dps))")
        writer.println("\t\tassertEquals(5 * dps, max(5 * dps, -10 * dps))")
        writer.println("\t}")
    }
}
