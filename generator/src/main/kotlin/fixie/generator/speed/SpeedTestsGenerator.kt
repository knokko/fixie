package fixie.generator.speed

import java.io.PrintWriter
import java.math.BigInteger

internal class SpeedTestsGenerator(
        private val writer: PrintWriter,
        private val speed: SpeedClass,
        private val packageName: String
) {

    private val suffix = if (speed.number.internalType.signed) "" else "u"

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

    private fun shouldTestSeconds() = speed.number.oneValue * BigInteger.valueOf(100L) < speed.number.internalType.getMaxValue()

    private fun shouldTestHours() = speed.number.oneValue * BigInteger.valueOf(100_000L) < speed.number.internalType.getMaxValue()

    private fun generateClassPrefix() {
        writer.println("package $packageName")
        writer.println()
        writer.println("import org.junit.jupiter.api.Test")
        writer.println("import org.junit.jupiter.api.Assertions.*")
        if (shouldTestHours()) writer.println("import kotlin.time.Duration.Companion.hours")
        if (shouldTestSeconds()) writer.println("import kotlin.time.Duration.Companion.seconds")
        writer.println()
        writer.println("class Test${speed.className} {")
    }

    private fun generateEquals() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testEquals() {")
        writer.println("\t\tassertEquals(${speed.className}.raw(10$suffix), ${speed.className}.raw(10$suffix))")
        writer.println("\t\tassertNotEquals(${speed.className}.raw(10$suffix), ${speed.className}.raw(11$suffix))")
        if (speed.number.oneValue > BigInteger.TEN && speed.oneUnit.ordinal > 0) {
            val previousUnit = SpeedUnit.entries[speed.oneUnit.ordinal - 1]
            writer.println("\t\tassertNotEquals(${speed.className}.${speed.oneUnit}, ${speed.className}.$previousUnit)")
        }
        if (speed.number.oneValue * BigInteger.TEN < speed.number.internalType.getMaxValue() && speed.oneUnit != SpeedUnit.entries.last()) {
            val nextUnit = SpeedUnit.entries[speed.oneUnit.ordinal + 1]
            writer.println("\t\tassertNotEquals(${speed.className}.${speed.oneUnit}, ${speed.className}.$nextUnit)")
        }
        writer.println("\t}")
    }

    private fun generateToDouble() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testToDouble() {")
        val testValue = if (speed.number.oneValue > BigInteger.TEN) 0.7 else 7.0
        val margin = if (speed.number.oneValue < BigInteger.TEN) 0.4 else if (speed.number.oneValue < BigInteger.valueOf(1000L)) 0.04 else 0.001
        writer.println("\t\tassertEquals($testValue, ($testValue * ${speed.className}.${speed.oneUnit}).toDouble(SpeedUnit.${speed.oneUnit}), $margin)")
        val anotherUnit = if (speed.oneUnit == SpeedUnit.KILOMETERS_PER_HOUR) SpeedUnit.METERS_PER_SECOND else SpeedUnit.KILOMETERS_PER_HOUR
        writer.println("\t\tassertEquals(${0.4 * anotherUnit.factor / speed.oneUnit.factor}, (0.4 * ${speed.className}.${speed.oneUnit}).toDouble(SpeedUnit.$anotherUnit), $margin)")
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
            writer.println("\t\tassertTrue((0.5 * ${speed.className}.$unit).toString(SpeedUnit.$unit).startsWith(\"0.5\"))")
            writer.println("\t\tassertTrue((0.5 * ${speed.className}.$unit).toString(SpeedUnit.$unit).endsWith(\"${unit.abbreviation}\"))")
        }
        writer.println("\t}")
    }

    private fun generateCompanionConstructors() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompanionConstructors() {")
        writer.println("\t\tassertEquals(${speed.className}.${speed.oneUnit}, ${speed.className}.${speed.oneUnit})")
        if (speed.number.oneValue > BigInteger.TEN) {
            writer.println("\t\tassertNotEquals(${speed.className}.${speed.oneUnit} * 0.9, ${speed.className}.${speed.oneUnit})")
            writer.println("\t\tassertTrue(${speed.className}.${speed.oneUnit} > ${speed.className}.${speed.oneUnit} * 0.9)")
            if (speed.oneUnit.ordinal > 0) {
                val previousUnit = SpeedUnit.entries[speed.oneUnit.ordinal - 1]
                writer.println("\t\tassertTrue(${speed.className}.${speed.oneUnit} > ${speed.className}.$previousUnit)")
            }
        }
        if (speed.number.oneValue * BigInteger.TEN < speed.number.internalType.getMaxValue() && speed.oneUnit != SpeedUnit.entries.last()) {
            val nextUnit = SpeedUnit.entries[speed.oneUnit.ordinal + 1]
            writer.println("\t\tassertTrue(${speed.className}.${speed.oneUnit} < ${speed.className}.$nextUnit)")
        }
        writer.println("\t}")
    }

    private fun generateCompareTo() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testCompareTo() {")
        writer.println("\t\tassertTrue(${speed.className}.raw(40$suffix) < ${speed.className}.raw(41$suffix))")
        writer.println("\t\tassertTrue(${speed.className}.raw(40$suffix) <= ${speed.className}.raw(41$suffix))")
        writer.println("\t\tassertTrue(${speed.className}.raw(40$suffix) <= ${speed.className}.raw(40$suffix))")
        writer.println("\t\tassertTrue(${speed.className}.raw(40$suffix) >= ${speed.className}.raw(40$suffix))")
        writer.println("\t\tassertFalse(${speed.className}.raw(40$suffix) >= ${speed.className}.raw(41$suffix))")
        writer.println("\t\tassertFalse(${speed.className}.raw(40$suffix) > ${speed.className}.raw(41$suffix))")
        writer.println()
        writer.println("\t\tassertFalse(${speed.className}.raw(40$suffix) < ${speed.className}.raw(39$suffix))")
        writer.println("\t\tassertFalse(${speed.className}.raw(40$suffix) <= ${speed.className}.raw(39$suffix))")
        writer.println("\t\tassertTrue(${speed.className}.raw(40$suffix) >= ${speed.className}.raw(39$suffix))")
        writer.println("\t\tassertTrue(${speed.className}.raw(40$suffix) > ${speed.className}.raw(39$suffix))")
        writer.println("\t}")
    }

    private fun generateArithmetic() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testArithmetic() {")
        writer.println("\t\tassertEquals(${speed.className}.raw(73$suffix), ${speed.className}.raw(70$suffix) + ${speed.className}.raw(3$suffix))")
        writer.println("\t\tassertEquals(${speed.className}.raw(20$suffix), ${speed.className}.raw(61$suffix) - ${speed.className}.raw(41$suffix))")
        writer.println("\t\tassertEquals(${speed.className}.raw(63$suffix), ${speed.className}.raw(3$suffix) * 21)")
        if (speed.number.oneValue * BigInteger.valueOf(21L) < speed.number.internalType.getMaxValue()) {
            writer.println("\t\tassertEquals(${speed.className}.raw(63$suffix), ${speed.className}.raw(3$suffix) * ${speed.number.className}.from(21))")
            writer.println("\t\tassertEquals(${speed.className}.raw(63$suffix), ${speed.className}.raw(3$suffix) * 21f)")
            writer.println("\t\tassertEquals(${speed.className}.raw(63$suffix), ${speed.className}.raw(3$suffix) * 21.0)")
            writer.println("\t\tassertEquals(${speed.number.className}.from(20), ${speed.className}.raw(40$suffix) / ${speed.className}.raw(2$suffix))")
        } else {
            writer.println("\t\tassertEquals(${speed.className}.raw(3$suffix), ${speed.className}.raw(3$suffix) * ${speed.number.className}.from(1))")
            writer.println("\t\tassertEquals(${speed.className}.raw(3$suffix), ${speed.className}.raw(3$suffix) * 1f)")
            writer.println("\t\tassertEquals(${speed.className}.raw(3$suffix), ${speed.className}.raw(3$suffix) * 1.0)")
        }
        writer.println("\t\tassertEquals(${speed.className}.raw(63$suffix), ${speed.className}.raw(3$suffix) * 21L)")
        writer.println("\t\tassertEquals(${speed.className}.raw(20$suffix), ${speed.className}.raw(40$suffix) / 2)")
        writer.println("\t\tassertEquals(${speed.className}.raw(20$suffix), ${speed.className}.raw(40$suffix) / 2L)")

        if (speed.number.oneValue * BigInteger.valueOf(3) < speed.number.internalType.getMaxValue()) {
            writer.println("\t\tassertEquals(${speed.className}.raw(20$suffix), ${speed.className}.raw(40$suffix) / ${speed.number.className}.from(2))")
            writer.println("\t\tassertEquals(${speed.className}.raw(20$suffix), ${speed.className}.raw(50$suffix) / 2.5f)")
            writer.println("\t\tassertEquals(${speed.className}.raw(20$suffix), ${speed.className}.raw(50$suffix) / 2.5)")
        }
        if (speed.number.internalType.signed) {
            writer.println("\t\tassertEquals(${speed.className}.raw(-43$suffix), -${speed.className}.raw(43$suffix))")
        }
        if (shouldTestSeconds()) {
            val margin = if (speed.number.oneValue > BigInteger.TEN) 0.01 else 5.0
            writer.println("\t\tassertEquals(20.0, (10 * ${speed.className}.METERS_PER_SECOND * 2.seconds).toDouble(DistanceUnit.METER), $margin)")

            if (shouldTestHours()) {
                writer.println("\t\tassertEquals(20.0, (10 * ${speed.className}.KILOMETERS_PER_HOUR * 2.hours).toDouble(DistanceUnit.KILOMETER), $margin)")
            }
        }
        writer.println("\t}")
    }

    private fun generateExtensionFunctions() {
        writer.println()
        writer.println("\t@Test")
        writer.println("\tfun testExtensionFunctions() {")
        writer.println("\t\tassertEquals(${speed.className}.raw(63$suffix), 21 * ${speed.className}.raw(3$suffix))")
        writer.println("\t\tassertEquals(${speed.className}.raw(63$suffix), 21L * ${speed.className}.raw(3$suffix))")
        if (speed.number.oneValue * BigInteger.valueOf(21L) < speed.number.internalType.getMaxValue()) {
            writer.println("\t\tassertEquals(${speed.className}.raw(63$suffix), 21f * ${speed.className}.raw(3$suffix))")
            writer.println("\t\tassertEquals(${speed.className}.raw(63$suffix), 21.0 * ${speed.className}.raw(3$suffix))")
        }
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
        if (speed.number.internalType.signed) {
            writer.println("\t\tassertEquals(${speed.className}.raw(0), abs(${speed.className}.raw(0)))")
            writer.println("\t\tassertEquals(${speed.className}.raw(5), abs(${speed.className}.raw(-5)))")
            writer.println("\t\tassertEquals(${speed.className}.raw(5), abs(${speed.className}.raw(5)))")
        }
        writer.println("\t\tassertEquals(${speed.className}.raw(4$suffix), min(${speed.className}.raw(6$suffix), ${speed.className}.raw(4$suffix)))")
        writer.println("\t\tassertEquals(${speed.className}.raw(6$suffix), max(${speed.className}.raw(6$suffix), ${speed.className}.raw(4$suffix)))")
        writer.println("\t}")
    }
}
