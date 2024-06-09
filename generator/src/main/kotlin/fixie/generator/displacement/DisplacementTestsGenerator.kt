package fixie.generator.displacement

import java.io.PrintWriter
import java.math.BigInteger

internal class DisplacementTestsGenerator(
        private val writer: PrintWriter,
        private val displacement: DisplacementClass,
        private val packageName: String
) {

    private val suffix = if (displacement.number.internalType.signed) "" else "u"

    fun generate() {
        generateClassPrefix()
        generateEquals()
        generateToDouble()
        generateToString()
        generateCompanionConstructors()
        generateCompareTo()
        generateArithmetic()
        if (displacement.number.oneValue * BigInteger.TEN < displacement.number.internalType.getMaxValue()) generateAreaClass()
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
        writer.println("class Test${displacement.className} {")
    }

    private fun generateEquals() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testEquals() {")
        writer.println("\t\tassertEquals(${displacement.className}.raw(10$suffix), ${displacement.className}.raw(10$suffix))")
        writer.println("\t\tassertNotEquals(${displacement.className}.raw(10$suffix), ${displacement.className}.raw(11$suffix))")
        writer.println("\t}")
    }

    private fun generateToDouble() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToDouble() {")
        val margin = if (displacement.number.internalType.numBytes == 1) "0.01" else "0.001"
        if (displacement.number.oneValue > BigInteger.TEN) {
            writer.println("\t\tassertEquals(0.7, (0.7 * ${displacement.className}.${displacement.oneUnit}).toDouble(DistanceUnit.${displacement.oneUnit}), $margin)")
        } else {
            writer.println("\t\tassertEquals(7.0, (7.0 * ${displacement.className}.${displacement.oneUnit}).toDouble(DistanceUnit.${displacement.oneUnit}), $margin)")
        }
        writer.println("\t}")
    }

    private fun generateToString() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToString() {")
        writer.println("\t\tval displacementString = (0.5 * ${displacement.className}.${displacement.oneUnit}).toString(DistanceUnit.${displacement.oneUnit})")
        writer.println("\t\tassertTrue(displacementString.startsWith(\"0.5\"))")
        writer.println("\t\tassertTrue(displacementString.endsWith(\"${displacement.oneUnit.abbreviation}\"))")
        writer.println("\t}")
    }

    private fun generateCompanionConstructors() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompanionConstructors() {")
        writer.println("\t\tassertEquals(${displacement.className}.${displacement.oneUnit}, ${displacement.className}.${displacement.oneUnit})")
        if (displacement.number.oneValue > BigInteger.TEN) {
            writer.println("\t\tassertNotEquals(${displacement.className}.${displacement.oneUnit} * 0.9, ${displacement.className}.${displacement.oneUnit})")
            writer.println("\t\tassertTrue(${displacement.className}.${displacement.oneUnit} > ${displacement.className}.${displacement.oneUnit} * 0.9)")

            if (displacement.oneUnit != DistanceUnit.entries.first() && displacement.number.oneValue > BigInteger.TEN.pow(3)) {
                val previousUnit = DistanceUnit.entries[displacement.oneUnit.ordinal - 1]
                writer.println("\t\tassertTrue(${displacement.className}.${displacement.oneUnit} > ${displacement.className}.$previousUnit)")
            }
        } else {
            writer.println("\t\tassertNotEquals(${displacement.className}.${displacement.oneUnit} * 0.3, ${displacement.className}.${displacement.oneUnit})")
            writer.println("\t\tassertTrue(${displacement.className}.${displacement.oneUnit} > ${displacement.className}.${displacement.oneUnit} * 0.3)")

            if (displacement.oneUnit != DistanceUnit.entries.last() && displacement.number.internalType.numBytes > 1) {
                val nextUnit = DistanceUnit.entries[displacement.oneUnit.ordinal + 1]
                writer.println("\t\tassertTrue(${displacement.className}.${displacement.oneUnit} < ${displacement.className}.$nextUnit)")
            }
        }
        writer.println("\t}")
    }

    private fun generateCompareTo() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompareTo() {")
        writer.println("\t\tassertTrue(${displacement.className}.raw(40$suffix) < ${displacement.className}.raw(41$suffix))")
        writer.println("\t\tassertTrue(${displacement.className}.raw(40$suffix) <= ${displacement.className}.raw(41$suffix))")
        writer.println("\t\tassertTrue(${displacement.className}.raw(40$suffix) <= ${displacement.className}.raw(40$suffix))")
        writer.println("\t\tassertTrue(${displacement.className}.raw(40$suffix) >= ${displacement.className}.raw(40$suffix))")
        writer.println("\t\tassertFalse(${displacement.className}.raw(40$suffix) >= ${displacement.className}.raw(41$suffix))")
        writer.println("\t\tassertFalse(${displacement.className}.raw(40$suffix) > ${displacement.className}.raw(41$suffix))")
        writer.println()
        writer.println("\t\tassertFalse(${displacement.className}.raw(40$suffix) < ${displacement.className}.raw(39$suffix))")
        writer.println("\t\tassertFalse(${displacement.className}.raw(40$suffix) <= ${displacement.className}.raw(39$suffix))")
        writer.println("\t\tassertTrue(${displacement.className}.raw(40$suffix) >= ${displacement.className}.raw(39$suffix))")
        writer.println("\t\tassertTrue(${displacement.className}.raw(40$suffix) > ${displacement.className}.raw(39$suffix))")
        writer.println("\t}")
    }

    private fun generateArithmetic() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testArithmetic() {")
        writer.println("\t\tassertEquals(${displacement.className}.raw(73$suffix), ${displacement.className}.raw(70$suffix) + ${displacement.className}.raw(3$suffix))")
        writer.println("\t\tassertEquals(${displacement.className}.raw(20$suffix), ${displacement.className}.raw(61$suffix) - ${displacement.className}.raw(41$suffix))")
        writer.println("\t\tassertEquals(${displacement.className}.raw(63$suffix), ${displacement.className}.raw(3$suffix) * 21)")
        if (displacement.number.oneValue * BigInteger.valueOf(21L) < displacement.number.internalType.getMaxValue()) {
            writer.println("\t\tassertEquals(${displacement.className}.raw(63$suffix), ${displacement.className}.raw(3$suffix) * ${displacement.number.className}.from(21))")
            writer.println("\t\tassertEquals(${displacement.className}.raw(63$suffix), ${displacement.className}.raw(3$suffix) * 21f)")
            writer.println("\t\tassertEquals(${displacement.className}.raw(63$suffix), ${displacement.className}.raw(3$suffix) * 21.0)")
            writer.println("\t\tassertEquals(20.0, ${displacement.className}.raw(40$suffix) / ${displacement.className}.raw(2$suffix), 0.001)")
        } else {
            writer.println("\t\tassertEquals(${displacement.className}.raw(3$suffix), ${displacement.className}.raw(3$suffix) * ${displacement.number.className}.from(1))")
            writer.println("\t\tassertEquals(${displacement.className}.raw(3$suffix), ${displacement.className}.raw(3$suffix) * 1f)")
            writer.println("\t\tassertEquals(${displacement.className}.raw(3$suffix), ${displacement.className}.raw(3$suffix) * 1.0)")
        }
        writer.println("\t\tassertEquals(${displacement.className}.raw(63$suffix), ${displacement.className}.raw(3$suffix) * 21L)")
        writer.println("\t\tassertEquals(${displacement.className}.raw(20$suffix), ${displacement.className}.raw(40$suffix) / 2)")
        writer.println("\t\tassertEquals(${displacement.className}.raw(20$suffix), ${displacement.className}.raw(40$suffix) / 2L)")

        if (displacement.number.oneValue * BigInteger.valueOf(3) < displacement.number.internalType.getMaxValue()) {
            writer.println("\t\tassertEquals(${displacement.className}.raw(20$suffix), ${displacement.className}.raw(40$suffix) / ${displacement.number.className}.from(2))")
            writer.println("\t\tassertEquals(${displacement.className}.raw(20$suffix), ${displacement.className}.raw(50$suffix) / 2.5f)")
            writer.println("\t\tassertEquals(${displacement.className}.raw(20$suffix), ${displacement.className}.raw(50$suffix) / 2.5)")
        }
        if (displacement.number.internalType.signed) {
            writer.println("\t\tassertEquals(${displacement.className}.raw(-43), -${displacement.className}.raw(43))")
        }
        if (displacement.speed != null && displacement.number.oneValue * BigInteger.TEN < displacement.number.internalType.getMaxValue()) {
            var smallestOneValue = displacement.number.oneValue
            if (displacement.speed.number != null) smallestOneValue = smallestOneValue.min(displacement.speed.number.oneValue)
            val margin = if (smallestOneValue > BigInteger.TEN) 0.001 else 0.2
            writer.println("\t\tassertEquals(2.0, (10 * ${displacement.className}.METER / 5.seconds).toDouble(SpeedUnit.METERS_PER_SECOND), $margin)")
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
        writer.println("\t\tassertTrue(2 * one * one <= one * 3 * one)")
        writer.println("\t\tassertTrue(4 * one * one >= one * 3 * one)")
        writer.println("\t}")
    }

    private fun generateExtensionFunctions() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testExtensionFunctions() {")
        writer.println("\t\tassertEquals(${displacement.className}.raw(63$suffix), 21 * ${displacement.className}.raw(3$suffix))")
        writer.println("\t\tassertEquals(${displacement.className}.raw(63$suffix), 21L * ${displacement.className}.raw(3$suffix))")

        if (displacement.number.oneValue * BigInteger.valueOf(21L) < displacement.number.internalType.getMaxValue()) {
            writer.println("\t\tassertEquals(${displacement.className}.raw(63$suffix), 21f * ${displacement.className}.raw(3$suffix))")
            writer.println("\t\tassertEquals(${displacement.className}.raw(63$suffix), 21.0 * ${displacement.className}.raw(3$suffix))")
        }

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
        if (displacement.number.internalType.signed) {
            writer.println("\t\tassertEquals(${displacement.className}.raw(0), abs(${displacement.className}.raw(0)))")
            writer.println("\t\tassertEquals(${displacement.className}.raw(5), abs(${displacement.className}.raw(-5)))")
            writer.println("\t\tassertEquals(${displacement.className}.raw(5), abs(${displacement.className}.raw(5)))")
        }
        writer.println("\t\tassertEquals(${displacement.className}.raw(4$suffix), min(${displacement.className}.raw(6$suffix), ${displacement.className}.raw(4$suffix)))")
        writer.println("\t\tassertEquals(${displacement.className}.raw(6$suffix), max(${displacement.className}.raw(6$suffix), ${displacement.className}.raw(4$suffix)))")
        writer.println("\t}")
    }
}
