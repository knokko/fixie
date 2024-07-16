package fixie.generator.area

import java.io.PrintWriter

internal class AreaTestsGenerator(
        private val writer: PrintWriter,
        private val area: AreaClass,
        private val packageName: String
) {

    fun generate() {
        generateClassPrefix()
        generateAssertEquals()
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
        writer.println("import org.junit.jupiter.api.assertThrows")
        writer.println()
        writer.println("class Test${area.className} {")
    }

    private fun generateAssertEquals() {
        val n = area.className
        writer.println()
        writer.println("\tprivate fun assertEquals(expected: $n, actual: $n, margin: $n = 0.001 * $n.SQUARE_METER) {")
        writer.println("\t\tif (abs(expected - actual) > margin) org.junit.jupiter.api.Assertions.assertEquals(expected, actual)")
        writer.println("\t}")
        writer.println()
        writer.println("\tprivate fun assertNotEquals(expected: $n, actual: $n, margin: $n = 0.001 * $n.SQUARE_METER) {")
        writer.println("\t\tif (abs(expected - actual) <= margin) org.junit.jupiter.api.Assertions.assertNotEquals(expected, actual)")
        writer.println("\t}")
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testAssertEquals() {")
        writer.println("\t\tassertEquals($n.SQUARE_METER, $n.SQUARE_METER)")
        writer.println("\t\tassertEquals($n.SQUARE_METER, $n.SQUARE_METER * 1.000001)")
        writer.println("\t\tassertThrows<AssertionError> { assertEquals($n.SQUARE_METER, $n.SQUARE_METER * 1.1) }")
        writer.println("\t\tassertNotEquals($n.SQUARE_METER, $n.SQUARE_METER * 1.1)")
        writer.println("\t\tassertThrows<AssertionError> { assertNotEquals($n, $n) }")
        writer.println("\t}")
    }

    private fun generateEquals() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testEquals() {")
        writer.println("\t\tval m2 = ${area.className}.SQUARE_METER")
        writer.println("\t\tassertEquals(m2 * 1.1, m2 * 1.1)")
        writer.println("\t\tassertEquals(m2 * 1.1, m2 * 1.1000001)")
        writer.println("\t\tassertEquals(m2, 1000 * ${area.className}.SQUARE_MILLIMETER * 1000L)")
        writer.println("\t\tassertNotEquals(m2 * 1.2, m2 * 1.1)")
        writer.println("\t\tassertNotEquals(m2, ${area.className}.SQUARE_INCH)")
        writer.println("\t\tassertEquals(100 * 100 * m2, ${area.className}.HECTARE)")
        writer.println("\t\tassertFalse(${area.className}.HECTARE == 1.0001 * ${area.className}.HECTARE)")
        writer.println("\t}")
    }

    private fun generateToDouble() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToDouble() {")
        writer.println("\t\tassertEquals(25000.0, (2.5 * ${area.className}.HECTARE).toDouble(AreaUnit.SQUARE_METER), 0.1)")
        writer.println("\t\tassertEquals(0.1, (1000 * ${area.className}.SQUARE_METER).toDouble(AreaUnit.HECTARE), 0.001)")
        writer.println("\t\tassertEquals(1.23, (1.23 * ${area.className}.SQUARE_INCH).toDouble(AreaUnit.SQUARE_INCH), 0.001)")
        writer.println("\t}")
    }

    private fun generateToString() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToString() {")
        writer.println("\t\tassertEquals(\"2.34ha\", (${area.className}.HECTARE * 2.34).toString(AreaUnit.HECTARE))")
        writer.println("\t\tassertEquals(\"5.67in^2\", (5.67 * ${area.className}.SQUARE_INCH).toString(AreaUnit.SQUARE_INCH))")
        writer.println("\t\tassertEquals(\"8.91m^2\", (8.91 * ${area.className}.SQUARE_METER).toString(AreaUnit.SQUARE_METER))")
        writer.println("\t\tassertEquals(\"0.12${area.displayUnit.abbreviation}\", (0.1234 * ${area.className}.${area.displayUnit.name}).toString())")
        writer.println("\t}")
    }

    private fun generateCompareTo() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompareTo() {")
        writer.println("\t\tassertFalse(${area.className}.SQUARE_METER > ${area.className}.SQUARE_METER)")
        writer.println("\t\tassertFalse(${area.className}.SQUARE_METER < ${area.className}.SQUARE_METER)")
        writer.println("\t\tassertTrue(${area.className}.SQUARE_METER > ${area.className}.SQUARE_INCH * 200)")
        writer.println("\t\tassertTrue(${area.className}.SQUARE_METER < ${area.className}.HECTARE / 500)")
        writer.println("\t\tassertTrue(${area.className}.SQUARE_INCH > -${area.className}.SQUARE_INCH)")
        writer.println("\t\tassertTrue(${area.className}.SQUARE_INCH > -${area.className}.HECTARE)")
        writer.println("\t\tassertFalse(-2 * ${area.className}.SQUARE_METER > -${area.className}.SQUARE_METER)")
        writer.println("\t\tassertTrue(2 * ${area.className}.SQUARE_METER > ${area.className}.SQUARE_METER)")
        writer.println("\t\tassertFalse(${area.className}.SQUARE_METER * -1.2 > ${area.className}.SQUARE_METER * -1)")
        writer.println("\t\tassertTrue(${area.className}.HECTARE * -1.2 < ${area.className}.HECTARE * -1)")
        writer.println("\t}")
    }

    private fun generateArithmetic() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testArithmetic() {")
        writer.println("\t\tassertEquals(3L * ${area.className}.SQUARE_INCH, 2f * ${area.className}.SQUARE_INCH + ${area.className}.SQUARE_INCH)")
        writer.println("\t\tassertEquals(${area.className}.HECTARE * 0.3, ${area.className}.HECTARE - 0.7f * ${area.className}.HECTARE)")
        writer.println("\t\tassertEquals(${area.className}.HECTARE / 0.4, 2.5 * ${area.className}.HECTARE)")
        writer.println("\t\tassertEquals(${area.className}.HECTARE, 100 * 100 * ${area.className}.SQUARE_METER)")
        val divSuffix = if (area.floatType.numBytes == 4) "f" else ""
        writer.println("\t\tassertEquals(0.25$divSuffix, ${area.className}.SQUARE_METER / (${area.className}.SQUARE_METER * 4), 0.001$divSuffix)")
        if (area.displacementClassName != null) {
            writer.println("\t\tassertEquals(4.0, ((10 * ${area.className}.SQUARE_INCH) / (2.5 * ${area.displacementClassName}.INCH)).toDouble(DistanceUnit.INCH), 0.01)")
        }
        // TODO Volume
        writer.println("\t}")
    }

    private fun generateExtensionFunctions() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testExtensionFunctions() {")
        if (area.createNumberExtensions) {
            writer.println("\t\tassertEquals(2.m2, 2 * ${area.className}.SQUARE_METER)")
            writer.println("\t\tassertNotEquals(2.1.m2, 2 * ${area.className}.SQUARE_METER)")
        }
        writer.println("\t\tassertEquals(4 * ${area.className}.SQUARE_METER, ${area.className}.SQUARE_METER * 4f)")
        writer.println("\t\tassertEquals(4L * ${area.className}.SQUARE_METER, ${area.className}.SQUARE_METER * 4.0)")
        writer.println("\t}")
    }

    private fun generateMathFunctions() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testMathFunctions() {")
        writer.println("\t\tassertEquals(${area.className}.SQUARE_METER, abs(${area.className}.SQUARE_METER))")
        writer.println("\t\tassertEquals(${area.className}.HECTARE, abs(-${area.className}.HECTARE))")
        writer.println("\t\tassertEquals(0 * ${area.className}.HECTARE, abs(0 * ${area.className}.HECTARE))")
        writer.println("\t\tassertEquals(2 * ${area.className}.HECTARE, min(2 * ${area.className}.HECTARE, 3 * ${area.className}.HECTARE))")
        writer.println("\t\tassertEquals(3 * ${area.className}.SQUARE_METER, max(5 * ${area.className}.SQUARE_INCH, 3 * ${area.className}.SQUARE_METER))")
        writer.println("\t\tassertEquals(-3 * ${area.className}.HECTARE, min(2 * ${area.className}.HECTARE, -3 * ${area.className}.HECTARE))")
        writer.println("\t\tassertEquals(2 * ${area.className}.SQUARE_INCH, max(2 * ${area.className}.SQUARE_INCH, -3 * ${area.className}.SQUARE_INCH))")
        if (area.displacementClassName != null) {
            writer.println("\t\tassertEquals(2.0, sqrt(${area.className}.SQUARE_METER * 4).toDouble(DistanceUnit.METER), 0.01)")
        }
        writer.println("\t}")
    }
}
