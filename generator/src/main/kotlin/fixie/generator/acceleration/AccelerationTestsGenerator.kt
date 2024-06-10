package fixie.generator.acceleration

import java.io.PrintWriter

internal class AccelerationTestsGenerator(
        private val writer: PrintWriter,
        private val acceleration: AccelerationClass,
        private val packageName: String
) {

    fun generate() {
        generateClassPrefix()
        generateEquals()
        generateToDouble()
        generateToString()
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
        writer.println("import kotlin.time.Duration.Companion.seconds")
        writer.println()
        writer.println("class Test${acceleration.className} {")
    }

    private fun generateEquals() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testEquals() {")
        writer.println("\t\tassertEquals((${acceleration.className}.MPS2 * 1.1).toDouble(), (${acceleration.className}.MPS2 * 1.1).toDouble(), 0.001)")
        writer.println("\t\tassertEquals((${acceleration.className}.MPS2 * 1.1).toDouble(), (${acceleration.className}.MPS2 * 1.1000001).toDouble(), 0.001)")
        writer.println("\t\tassertNotEquals((${acceleration.className}.MPS2 * 1.2).toDouble(), (${acceleration.className}.MPS2 * 1.1).toDouble(), 0.001)")
        writer.println("\t}")
    }

    private fun generateToDouble() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToDouble() {")
        writer.println("\t\tassertEquals(1.23, 1.23.mps2.toDouble(), 0.001)")
        writer.println("\t}")
    }

    private fun generateToString() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToString() {")
        writer.println("\t\tassertEquals(\"2.34m/s^2\", (${acceleration.className}.MPS2 * 2.34).toString())")
        writer.println("\t}")
    }

    private fun generateCompareTo() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompareTo() {")
        writer.println("\t\tassertFalse(${acceleration.className}.MPS2 > ${acceleration.className}.MPS2)")
        writer.println("\t\tassertFalse(${acceleration.className}.MPS2 < ${acceleration.className}.MPS2)")
        writer.println("\t\tassertFalse(${acceleration.className}.MPS2 > ${acceleration.className}.MPS2 * 1.2)")
        writer.println("\t\tassertTrue(${acceleration.className}.MPS2 < ${acceleration.className}.MPS2 * 1.2)")
        writer.println("\t\tassertTrue(${acceleration.className}.MPS2 * 1.2 > ${acceleration.className}.MPS2)")
        writer.println("\t\tassertFalse(${acceleration.className}.MPS2 * 1.2 < ${acceleration.className}.MPS2)")
        writer.println("\t\tassertFalse(${acceleration.className}.MPS2 * -1.2 > ${acceleration.className}.MPS2 * -1)")
        writer.println("\t\tassertTrue(${acceleration.className}.MPS2 * -1.2 < ${acceleration.className}.MPS2 * -1)")
        writer.println("\t}")
    }

    private fun generateArithmetic() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testArithmetic() {")
        writer.println("\t\tval x = ${acceleration.className}.MPS2")
        writer.println("\t\tfun approximatelyEqual(a: ${acceleration.className}, b: ${acceleration.className}) {")
        writer.println("\t\t\tassertEquals(a.toDouble(), b.toDouble(), 0.001)")
        writer.println("\t\t}")
        writer.println("\t\tapproximatelyEqual(x * 3L, x + x + x)")
        writer.println("\t\tapproximatelyEqual(x * 0.3, x - x * 0.7f)")
        writer.println("\t\tapproximatelyEqual(x / 0.4, x * 2.5)")
        val divSuffix = if (acceleration.floatType.numBytes == 4) "f" else ""
        writer.println("\t\tassertEquals(0.25$divSuffix, x / (x * 4), 0.001$divSuffix)")
        if (acceleration.speedClassName != null) {
            writer.println("\t\tassertEquals(0.8, ((x * 0.2) * 4.seconds).toDouble(SpeedUnit.METERS_PER_SECOND), 0.01)")
        }
        writer.println("\t}")
    }

    private fun generateExtensionFunctions() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testExtensionFunctions() {")
        if (acceleration.createNumberExtensions) {
            writer.println("\t\tassertEquals(2.mps2.toDouble(), (2 * ${acceleration.className}.MPS2).toDouble(), 0.001)")
            writer.println("\t\tassertNotEquals(2.1.mps2.toDouble(), (2 * ${acceleration.className}.MPS2).toDouble(), 0.001)")
        }
        writer.println("\t\tassertEquals(4.0, (4 * ${acceleration.className}.MPS2).toDouble(), 0.001)")
        writer.println("\t\tassertEquals(4.0, (4L * ${acceleration.className}.MPS2).toDouble(), 0.001)")
        writer.println("\t\tassertEquals(4.0, (4f * ${acceleration.className}.MPS2).toDouble(), 0.001)")
        writer.println("\t\tassertEquals(4.0, (4.0 * ${acceleration.className}.MPS2).toDouble(), 0.001)")
        writer.println("\t}")
    }

    private fun generateMathFunctions() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testMathFunctions() {")
        writer.println("\t\tval x = ${acceleration.className}.MPS2")
        writer.println("\t\tassertEquals(x, abs(x))")
        writer.println("\t\tassertEquals(x, abs(-x))")
        writer.println("\t\tassertEquals(0 * x, abs(0 * x))")
        writer.println("\t\tassertEquals(2 * x, min(2 * x, 3 * x))")
        writer.println("\t\tassertEquals(3 * x, max(2 * x, 3 * x))")
        writer.println("\t\tassertEquals(-3 * x, min(2 * x, -3 * x))")
        writer.println("\t\tassertEquals(2 * x, max(2 * x, -3 * x))")
        writer.println("\t}")
    }
}
