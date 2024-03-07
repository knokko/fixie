package fixie.generator.speed

import fixie.SpeedUnit
import java.io.PrintWriter

class SpeedTestsGenerator(
    private val writer: PrintWriter,
    private val speed: SpeedClass
) {

    fun generate() {
        generateClassPrefix()
        generateEquals()
        generateToDouble()
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
        writer.println("import kotlin.time.Duration.Companion.hours")
        writer.println("import kotlin.time.Duration.Companion.seconds")
        writer.println()
        writer.println("class Test${speed.className} {")
    }

    private fun generateEquals() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testEquals() {")
        writer.println("\t\tassertEquals(${speed.className}.raw(10), ${speed.className}.raw(10))")
        writer.println("\t\tassertNotEquals(${speed.className}.raw(10), ${speed.className}.raw(11))")
        writer.println("\t\tassertNotEquals(${speed.className}.METERS_PER_SECOND, ${speed.className}.KILOMETERS_PER_HOUR)")
        writer.println("\t\tassertNotEquals(${speed.className}.METERS_PER_SECOND, ${speed.className}.MILES_PER_HOUR)")
        writer.println("\t}")
    }

    private fun generateToDouble() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToDouble() {")
        writer.println("\t\tassertEquals(0.7, (0.7 * ${speed.className}.${speed.oneUnit}).toDouble(SpeedUnit.${speed.oneUnit}), 0.001)")
        val anotherUnit = if (speed.oneUnit == SpeedUnit.KILOMETERS_PER_HOUR) SpeedUnit.METERS_PER_SECOND else SpeedUnit.KILOMETERS_PER_HOUR
        writer.println("\t\tassertEquals(${0.4 * anotherUnit.factor / speed.oneUnit.factor}, (0.4 * ${speed.className}.${speed.oneUnit}).toDouble(SpeedUnit.$anotherUnit), 0.001)")
        writer.println("\t}")
    }

    private fun generateToString() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToString() {")
        val units = mutableListOf(speed.oneUnit)
        if (speed.oneUnit == SpeedUnit.METERS_PER_SECOND) {
            units.add(SpeedUnit.KILOMETERS_PER_HOUR)
            units.add(SpeedUnit.KILOMETERS_PER_SECOND)
        }
        for (unit in units) {
            writer.println("\t\tassertEquals(\"0.5${unit.abbreviation}\", (0.5 * ${speed.className}.$unit).toString(SpeedUnit.$unit))")
        }
        writer.println("\t}")
    }

    private fun generateCompanionConstructors() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompanionConstructors() {")
        writer.println("\t\tassertEquals(${speed.className}.${speed.oneUnit}, ${speed.className}.${speed.oneUnit})")
        writer.println("\t\tassertNotEquals(${speed.className}.${speed.oneUnit} * 0.9, ${speed.className}.${speed.oneUnit})")
        writer.println("\t\tassertTrue(${speed.className}.${speed.oneUnit} > ${speed.className}.${speed.oneUnit} * 0.9)")
        writer.println("\t\tassertTrue(${speed.className}.METERS_PER_SECOND > ${speed.className}.MILES_PER_HOUR)")
        writer.println("\t\tassertFalse(${speed.className}.KILOMETERS_PER_HOUR > ${speed.className}.MILES_PER_HOUR)")
        writer.println("\t}")
    }

    private fun generateCompareTo() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompareTo() {")
        writer.println("\t\tassertTrue(${speed.className}.raw(40) < ${speed.className}.raw(41))")
        writer.println("\t\tassertTrue(${speed.className}.raw(40) <= ${speed.className}.raw(41))")
        writer.println("\t\tassertTrue(${speed.className}.raw(40) <= ${speed.className}.raw(40))")
        writer.println("\t\tassertTrue(${speed.className}.raw(40) >= ${speed.className}.raw(40))")
        writer.println("\t\tassertFalse(${speed.className}.raw(40) >= ${speed.className}.raw(41))")
        writer.println("\t\tassertFalse(${speed.className}.raw(40) > ${speed.className}.raw(41))")
        writer.println()
        writer.println("\t\tassertFalse(${speed.className}.raw(40) < ${speed.className}.raw(39))")
        writer.println("\t\tassertFalse(${speed.className}.raw(40) <= ${speed.className}.raw(39))")
        writer.println("\t\tassertTrue(${speed.className}.raw(40) >= ${speed.className}.raw(39))")
        writer.println("\t\tassertTrue(${speed.className}.raw(40) > ${speed.className}.raw(39))")
        writer.println("\t}")
    }

    private fun generateArithmetic() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testArithmetic() {")
        writer.println("\t\tassertEquals(${speed.className}.raw(73), ${speed.className}.raw(70) + ${speed.className}.raw(3))")
        writer.println("\t\tassertEquals(${speed.className}.raw(20), ${speed.className}.raw(61) - ${speed.className}.raw(41))")
        writer.println("\t\tassertEquals(${speed.className}.raw(63), ${speed.className}.raw(3) * 21)")
        writer.println("\t\tassertEquals(${speed.className}.raw(63), ${speed.className}.raw(3) * ${speed.number.className}.from(21))")
        writer.println("\t\tassertEquals(${speed.className}.raw(63), ${speed.className}.raw(3) * 21L)")
        writer.println("\t\tassertEquals(${speed.className}.raw(63), ${speed.className}.raw(3) * 21f)")
        writer.println("\t\tassertEquals(${speed.className}.raw(63), ${speed.className}.raw(3) * 21.0)")
        writer.println("\t\tassertEquals(${speed.className}.raw(20), ${speed.className}.raw(40) / 2)")
        writer.println("\t\tassertEquals(${speed.className}.raw(20), ${speed.className}.raw(40) / ${speed.number.className}.from(2))")
        writer.println("\t\tassertEquals(${speed.number.className}.from(20), ${speed.className}.raw(40) / ${speed.className}.raw(2))")
        writer.println("\t\tassertEquals(${speed.className}.raw(20), ${speed.className}.raw(40) / 2L)")
        writer.println("\t\tassertEquals(${speed.className}.raw(20), ${speed.className}.raw(50) / 2.5f)")
        writer.println("\t\tassertEquals(${speed.className}.raw(20), ${speed.className}.raw(50) / 2.5)")
        if (speed.number.internalType.signed) {
            writer.println("\t\tassertEquals(${speed.className}.raw(-43), -${speed.className}.raw(43))")
        }
        writer.println("\t\tassertEquals(20.0, (10 * ${speed.className}.METERS_PER_SECOND * 2.seconds).toDouble(DistanceUnit.METER), 0.001)")
        writer.println("\t\tassertEquals(20.0, (10 * ${speed.className}.KILOMETERS_PER_HOUR * 2.hours).toDouble(DistanceUnit.KILOMETER), 0.001)")
        writer.println("\t}")
    }

    private fun generateExtensionFunctions() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testExtensionFunctions() {")
        writer.println("\t\tassertEquals(${speed.className}.raw(63), 21 * ${speed.className}.raw(3))")
        writer.println("\t\tassertEquals(${speed.className}.raw(63), 21L * ${speed.className}.raw(3))")
        writer.println("\t\tassertEquals(${speed.className}.raw(63), 21f * ${speed.className}.raw(3))")
        writer.println("\t\tassertEquals(${speed.className}.raw(63), 21.0 * ${speed.className}.raw(3))")
        if (speed.createNumberExtensions) {
            val abbreviation = speed.oneUnit.abbreviation.replace('/', 'p')
            writer.println("\t\tassertEquals(${speed.className}.${speed.oneUnit}, 1.$abbreviation)")
            writer.println("\t\tassertEquals(${speed.className}.${speed.oneUnit}, 1L.$abbreviation)")
            writer.println("\t\tassertEquals(0.5 * ${speed.className}.${speed.oneUnit}, 0.5f.$abbreviation)")
            writer.println("\t\tassertEquals(0.5 * ${speed.className}.${speed.oneUnit}, 0.5.$abbreviation)")
            writer.println("\t\tassertTrue(1.miph > 1.kmph)")
            writer.println("\t\tassertTrue(1.miph < 1.mps)")
        }
        writer.println("\t}")
    }

    private fun generateMathFunctions() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testMathFunctions() {")
        writer.println("\t\tassertEquals(${speed.className}.raw(0), abs(${speed.className}.raw(0)))")
        writer.println("\t\tassertEquals(${speed.className}.raw(5), abs(${speed.className}.raw(-5)))")
        writer.println("\t\tassertEquals(${speed.className}.raw(5), abs(${speed.className}.raw(5)))")
        writer.println("\t\tassertEquals(${speed.className}.raw(4), min(${speed.className}.raw(6), ${speed.className}.raw(4)))")
        writer.println("\t\tassertEquals(${speed.className}.raw(6), max(${speed.className}.raw(6), ${speed.className}.raw(4)))")
        writer.println("\t}")
    }
}
