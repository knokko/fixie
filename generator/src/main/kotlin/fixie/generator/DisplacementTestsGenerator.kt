package fixie.generator

import java.io.PrintWriter

class DisplacementTestsGenerator(
    private val writer: PrintWriter,
    private val displacement: DisplacementClass
) {

    fun generate() {
        generateClassPrefix()
        generateEquals()
        generateToDouble()
        generateToString()
        generateCompanionConstructors()
        generateCompareTo()
        generateArithmetic()
        generateAreaClass()
        generateExtensionFunctions()
        generateMathFunctions()
        writer.println("}")
    }

    private fun generateClassPrefix() {
        writer.println("package fixie")
        writer.println()
        writer.println("import org.junit.jupiter.api.Test")
        writer.println("import org.junit.jupiter.api.Assertions.*")
        writer.println()
        writer.println("class Test${displacement.className} {")
    }

    private fun generateEquals() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testEquals() {")
        writer.println("\t\tassertEquals(${displacement.className}.raw(10), ${displacement.className}.raw(10))")
        writer.println("\t\tassertNotEquals(${displacement.className}.raw(10), ${displacement.className}.raw(11))")
        writer.println("\t}")
    }

    private fun generateToDouble() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToDouble() {")
        writer.println("\t\tassertEquals(0.7, (0.7 * ${displacement.className}.${displacement.oneUnit}).toDouble(DistanceUnit.${displacement.oneUnit}), 0.001)")
        writer.println("\t}")
    }

    private fun generateToString() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToString() {")
        writer.println("\t\tassertEquals(\"0.5${displacement.oneUnit.abbreviation}\", (0.5 * ${displacement.className}.${displacement.oneUnit}).toString(DistanceUnit.${displacement.oneUnit}))")
        writer.println("\t}")
    }

    private fun generateCompanionConstructors() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompanionConstructors() {")
        writer.println("\t\tassertEquals(${displacement.className}.${displacement.oneUnit}, ${displacement.className}.${displacement.oneUnit})")
        writer.println("\t\tassertNotEquals(${displacement.className}.${displacement.oneUnit} * 0.9, ${displacement.className}.${displacement.oneUnit})")
        writer.println("\t\tassertTrue(${displacement.className}.${displacement.oneUnit} > ${displacement.className}.${displacement.oneUnit} * 0.9)")
        writer.println("\t}")
    }

    private fun generateCompareTo() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompareTo() {")
        writer.println("\t\tassertTrue(${displacement.className}.raw(40) < ${displacement.className}.raw(41))")
        writer.println("\t\tassertTrue(${displacement.className}.raw(40) <= ${displacement.className}.raw(41))")
        writer.println("\t\tassertTrue(${displacement.className}.raw(40) <= ${displacement.className}.raw(40))")
        writer.println("\t\tassertTrue(${displacement.className}.raw(40) >= ${displacement.className}.raw(40))")
        writer.println("\t\tassertFalse(${displacement.className}.raw(40) >= ${displacement.className}.raw(41))")
        writer.println("\t\tassertFalse(${displacement.className}.raw(40) > ${displacement.className}.raw(41))")
        writer.println()
        writer.println("\t\tassertFalse(${displacement.className}.raw(40) < ${displacement.className}.raw(39))")
        writer.println("\t\tassertFalse(${displacement.className}.raw(40) <= ${displacement.className}.raw(39))")
        writer.println("\t\tassertTrue(${displacement.className}.raw(40) >= ${displacement.className}.raw(39))")
        writer.println("\t\tassertTrue(${displacement.className}.raw(40) > ${displacement.className}.raw(39))")
        writer.println("\t}")
    }

    private fun generateArithmetic() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testArithmetic() {")
        writer.println("\t\tassertEquals(${displacement.className}.raw(73), ${displacement.className}.raw(70) + ${displacement.className}.raw(3))")
        writer.println("\t\tassertEquals(${displacement.className}.raw(20), ${displacement.className}.raw(61) - ${displacement.className}.raw(41))")
        writer.println("\t\tassertEquals(${displacement.className}.raw(63), ${displacement.className}.raw(3) * 21)")
        writer.println("\t\tassertEquals(${displacement.className}.raw(63), ${displacement.className}.raw(3) * ${displacement.number.className}.from(21))")
        writer.println("\t\tassertEquals(${displacement.className}.raw(63), ${displacement.className}.raw(3) * 21L)")
        writer.println("\t\tassertEquals(${displacement.className}.raw(63), ${displacement.className}.raw(3) * 21f)")
        writer.println("\t\tassertEquals(${displacement.className}.raw(63), ${displacement.className}.raw(3) * 21.0)")
        writer.println("\t\tassertEquals(${displacement.className}.raw(20), ${displacement.className}.raw(40) / 2)")
        writer.println("\t\tassertEquals(${displacement.className}.raw(20), ${displacement.className}.raw(40) / ${displacement.number.className}.from(2))")
        writer.println("\t\tassertEquals(${displacement.number.className}.from(20), ${displacement.className}.raw(40) / ${displacement.className}.raw(2))")
        writer.println("\t\tassertEquals(${displacement.className}.raw(20), ${displacement.className}.raw(40) / 2L)")
        writer.println("\t\tassertEquals(${displacement.className}.raw(20), ${displacement.className}.raw(50) / 2.5f)")
        writer.println("\t\tassertEquals(${displacement.className}.raw(20), ${displacement.className}.raw(50) / 2.5)")
        if (displacement.number.internalType.signed) {
            writer.println("\t\tassertEquals(${displacement.className}.raw(-43), -${displacement.className}.raw(43))")
        }
        writer.println("\t}")
    }

    private fun generateAreaClass() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testAreaClass() {")
        writer.println("\t\tval one = ${displacement.className}.${displacement.oneUnit}")
        writer.println("\t\tassertEquals((2 * one) * (2 * one), one * (2 * one) + (2 * one) * one)")
        writer.println("\t\tassertEquals(one * one, (2 * one) * one - one * one)")
        writer.println("\t\tassertEquals(5 * one, sqrt((3 * one) * (3 * one) + (4 * one) * (4 * one)))")
        writer.println("\t\tassertEquals(${displacement.number.className}.from(2), (2 * one * one) / (one * one))")
        writer.println("\t}")
    }

    private fun generateExtensionFunctions() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testExtensionFunctions() {")
        writer.println("\t\tassertEquals(${displacement.className}.raw(63), 21 * ${displacement.className}.raw(3))")
        writer.println("\t\tassertEquals(${displacement.className}.raw(63), 21L * ${displacement.className}.raw(3))")
        writer.println("\t\tassertEquals(${displacement.className}.raw(63), 21f * ${displacement.className}.raw(3))")
        writer.println("\t\tassertEquals(${displacement.className}.raw(63), 21.0 * ${displacement.className}.raw(3))")
        if (displacement.createNumberExtensions) {
            writer.println("\t\tassertEquals(${displacement.className}.${displacement.oneUnit}, 1.${displacement.oneUnit.abbreviation})")
            writer.println("\t\tassertEquals(${displacement.className}.${displacement.oneUnit}, 1L.${displacement.oneUnit.abbreviation})")
            writer.println("\t\tassertEquals(0.5 * ${displacement.className}.${displacement.oneUnit}, 0.5f.${displacement.oneUnit.abbreviation})")
            writer.println("\t\tassertEquals(0.5 * ${displacement.className}.${displacement.oneUnit}, 0.5.${displacement.oneUnit.abbreviation})")
        }
        writer.println("\t}")
    }

    private fun generateMathFunctions() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testMathFunctions() {")
        writer.println("\t\tassertEquals(${displacement.className}.raw(4), min(${displacement.className}.raw(6), ${displacement.className}.raw(4)))")
        writer.println("\t\tassertEquals(${displacement.className}.raw(6), max(${displacement.className}.raw(6), ${displacement.className}.raw(4)))")
        writer.println("\t}")
    }
}
