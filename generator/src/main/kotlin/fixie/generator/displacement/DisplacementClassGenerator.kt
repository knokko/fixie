package fixie.generator.displacement

import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.roundToLong

internal class DisplacementClassGenerator(
        private val writer: PrintWriter,
        private val displacement: DisplacementClass,
        private val packageName: String
) {

    private var unitConstants = mutableListOf(displacement.oneUnit)

    fun generate() {
        generateClassPrefix()
        generateToDouble()
        generateToString()
        generateArithmetic()
        generateCompanionObject()
        generateAreaClass()
        writer.println("}")
        generateExtensionFunctions()
        generateMathFunctions()
    }

    private fun generateClassPrefix() {
        val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
        writer.println("// Generated by fixie at $currentTime")
        writer.println("package $packageName")
        writer.println()
        writer.println("import kotlin.math.sqrt")
        if (displacement.speed != null) {
            writer.println("import kotlin.time.Duration")
            writer.println("import kotlin.time.DurationUnit")
        }
        writer.println()
        writer.println("@JvmInline")
        writer.println("value class ${displacement.className} internal constructor(val value: ${displacement.number.className}) " +
                ": Comparable<${displacement.className}> {")
    }

    private fun generateToDouble() {
        writer.println()
        writer.println("\tfun toDouble(unit: DistanceUnit) = when(unit) {")
        for (unit in DistanceUnit.entries) {
            var factor = unit.divisor.toDouble()
            if (unit.isMetric != displacement.oneUnit.isMetric) {
                if (unit.isMetric) factor *= 1.609344
                else factor /= 1.609344
            }
            factor /= displacement.oneUnit.divisor.toDouble()
            val conversion = if (factor == 1.0) "" else " * $factor"
            writer.println("\t\tDistanceUnit.$unit -> value.toDouble()$conversion")
        }
        writer.println("\t}")
    }

    private fun generateToString() {
        writer.println()
        writer.println("\toverride fun toString() = toString(DistanceUnit.${displacement.displayUnit})")
        writer.println()
        writer.println("\tfun toString(unit: DistanceUnit): String {")
        val body = arrayOf(
                "val rawDouble = raw(1${if (displacement.number.internalType.signed) "" else "u"}).toDouble(unit)",
                "var numDecimalDigits = 0",
                "var referenceValue = 0.88",
                "while (rawDouble < referenceValue) {",
                "\treferenceValue *= 0.1",
                "\tnumDecimalDigits += 1",
                "}",
                "return String.format(\"%.\${numDecimalDigits}f%s\", toDouble(unit), unit.abbreviation)"
        )
        for (line in body) writer.println("\t\t$line")
        writer.println("\t}")
    }

    private fun generateArithmetic() {
        fun next() {
            writer.println()
            if (displacement.number.checkOverflow) writer.println("\t@Throws(FixedPointException::class)")
        }

        next()
        writer.println("\toverride operator fun compareTo(other: ${displacement.className}) = this.value.compareTo(other.value)")
        next()

        if (displacement.number.internalType.signed) {
            writer.println("\toperator fun unaryMinus() = ${displacement.className}(-value)")
            next()
        }

        writer.println("\toperator fun plus(right: ${displacement.className}) = ${displacement.className}(this.value + right.value)")
        next()
        writer.println("\toperator fun minus(right: ${displacement.className}) = ${displacement.className}(this.value - right.value)")

        for (typeName in arrayOf("Int", "Long", displacement.number.className)) {
            next()
            writer.println("\toperator fun times(right: $typeName) = ${displacement.className}(this.value * right)")
            next()
            writer.println("\toperator fun div(right: $typeName) = ${displacement.className}(this.value / right)")
        }
        for (typeName in arrayOf("Float", "Double")) {
            next()
            writer.println("\toperator fun times(right: $typeName) = ${displacement.className}(${displacement.number.className}.from(this.value.toDouble() * right))")
            next()
            writer.println("\toperator fun div(right: $typeName) = ${displacement.className}(${displacement.number.className}.from(this.value.toDouble() / right))")
        }

        next()
        writer.println("\toperator fun times(right: ${displacement.className}) = Area(this.value.toDouble() * right.value.toDouble())")
        next()
        writer.println("\toperator fun div(right: ${displacement.className}) = this.value.toDouble() / right.value.toDouble()")

        if (displacement.speed != null) {
            next()
            writer.println("\toperator fun div(right: Duration) = ${displacement.speed.className}.METERS_PER_SECOND * (this.toDouble(DistanceUnit.METER) / right.toDouble(DurationUnit.SECONDS))")
        }
    }

    private fun generateCompanionObject() {
        writer.println()
        writer.println("\tcompanion object {")
        writer.println()
        writer.println("\t\tfun raw(value: ${displacement.number.internalType}) = ${displacement.className}(${displacement.number.className}.raw(value))")
        writer.println()
        writer.println("\t\tval ${displacement.oneUnit} = ${displacement.className}(${displacement.number.className}.ONE)")

        for (unit in DistanceUnit.entries.reversed()) {
            if (unit != displacement.oneUnit) {
                val conversionFactor = if (displacement.oneUnit.isMetric == unit.isMetric) 1.0
                else if (unit.isMetric) 1.609344 else 1.0 / 1.609344

                val divisor = conversionFactor * unit.divisor.toDouble() / displacement.oneUnit.divisor.toDouble()
                val rawValue = (displacement.number.oneValue.toDouble() / divisor).roundToLong()
                val revertedValue = (rawValue * divisor).roundToLong()
                val lostPrecision = (displacement.number.oneValue.toDouble() - revertedValue) / displacement.number.oneValue.toDouble()
                if (displacement.number.internalType.canRepresent(rawValue) && abs(lostPrecision) < 0.1) {
                    writer.println()
                    writer.println("\t\tval $unit = raw($rawValue${if (displacement.number.internalType.signed) "" else "u"})")
                    unitConstants.add(unit)
                }
            }
        }

        writer.println("\t}")
    }

    private fun generateAreaClass() {
        writer.println()
        writer.println("\t@JvmInline")
        writer.println("\tvalue class Area(val raw: Double) : Comparable<Area> {")
        writer.println()
        writer.println("\t\toperator fun plus(right: Area) = Area(this.raw + right.raw)")
        writer.println()
        writer.println("\t\toperator fun minus(right: Area) = Area(this.raw - right.raw)")
        writer.println()
        writer.println("\t\toperator fun div(right: Area) = ${displacement.number.className}.from(this.raw / right.raw)")
        writer.println()
        writer.println("\t\toperator fun times(right: Double) = Area(this.raw * right)")
        writer.println()
        writer.println("\t\toverride operator fun compareTo(other: Area) = this.raw.compareTo(other.raw)")
        writer.println("\t}")
    }

    private fun generateExtensionFunctions() {
        for (typeName in arrayOf("Int", "Long", "Float", "Double", displacement.number.className)) {
            writer.println()
            if (displacement.number.checkOverflow) writer.println("@Throws(FixedPointException::class)")
            writer.println("operator fun $typeName.times(right: ${displacement.className}) = right * this")

            if (displacement.createNumberExtensions) {
                for (unit in unitConstants) {
                    writer.println()
                    writer.println("val $typeName.${unit.abbreviation}")
                    writer.println("\tget() = ${displacement.className}.$unit * this")
                }
            }
        }
    }

    private fun generateMathFunctions() {
        if (displacement.number.internalType.signed) {
            writer.println()
            if (displacement.number.checkOverflow) writer.println("@Throws(FixedPointException::class)")
            writer.println("fun abs(x: ${displacement.className}) = ${displacement.className}(abs(x.value))")
        }

        writer.println()
        if (displacement.number.checkOverflow) writer.println("@Throws(FixedPointException::class)")
        writer.println("fun sqrt(area: ${displacement.className}.Area) = sqrt(area.raw) * ${displacement.className}.${displacement.oneUnit}")
        writer.println()
        writer.println("fun min(a: ${displacement.className}, b: ${displacement.className}) = ${displacement.className}(min(a.value, b.value))")
        writer.println()
        writer.println("fun max(a: ${displacement.className}, b: ${displacement.className}) = ${displacement.className}(max(a.value, b.value))")
    }
}