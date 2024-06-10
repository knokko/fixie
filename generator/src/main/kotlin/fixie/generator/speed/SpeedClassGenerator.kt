package fixie.generator.speed

import fixie.generator.number.FloatType
import java.io.PrintWriter
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal class SpeedClassGenerator(
        private val writer: PrintWriter,
        private val speed: SpeedClass,
        private val packageName: String
) {

    private var unitConstants = mutableListOf(speed.oneUnit)

    fun generate() {
        generateClassPrefix()
        generateToDouble()
        generateToString()
        generateArithmetic()
        generateCompanionObject()
        writer.println("}")
        generateExtensionFunctions()
        generateMathFunctions()
    }

    private fun generateClassPrefix() {
        val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
        writer.println("// Generated by fixie at $currentTime")
        writer.println("package $packageName")
        writer.println()
        if (speed.number == null) {
            for (functionName in arrayOf("abs", "max", "min")) writer.println("import kotlin.math.$functionName")
        }
        writer.println("import kotlin.time.Duration")
        writer.println("import kotlin.time.DurationUnit")
        writer.println()
        writer.println("@JvmInline")
        val valueType = speed.number?.className ?: speed.floatType!!.typeName;
        writer.println("value class ${speed.className} internal constructor(val value: $valueType) " +
                ": Comparable<${speed.className}> {")
    }

    private fun generateToDouble() {
        writer.println()
        writer.println("\tfun toDouble(unit: SpeedUnit) = when(unit) {")
        for (unit in SpeedUnit.entries) {
            val factor = unit.factor / speed.oneUnit.factor
            val conversion = if (factor == 1.0) "" else " * $factor"
            val toDouble = if (speed.floatType == FloatType.DoublePrecision) "" else ".toDouble()"
            writer.println("\t\tSpeedUnit.$unit -> value$toDouble$conversion")
        }
        writer.println("\t}")
    }

    private fun generateToString() {
        writer.println()
        writer.println("\toverride fun toString() = toString(SpeedUnit.${speed.displayUnit})")
        writer.println()
        writer.println("\tfun toString(unit: SpeedUnit): String {")
        writer.println("\t\treturn String.format(\"%.4f%s\", toDouble(unit), unit.abbreviation)")
        writer.println("\t}")
    }

    private fun getMultiplicationTypes() = arrayOf("Int", "Long", "Float", "Double") + if (speed.number != null)
        arrayOf(speed.number.className) else emptyArray()

    private fun generateArithmetic() {
        fun next() {
            writer.println()
            if (speed.number?.checkOverflow == true) writer.println("\t@Throws(FixedPointException::class)")
        }

        next()
        writer.println("\toverride operator fun compareTo(other: ${speed.className}) = this.value.compareTo(other.value)")
        next()

        if (speed.number?.internalType?.signed != false) {
            writer.println("\toperator fun unaryMinus() = ${speed.className}(-value)")
            next()
        }

        writer.println("\toperator fun plus(right: ${speed.className}) = ${speed.className}(this.value + right.value)")
        next()
        writer.println("\toperator fun minus(right: ${speed.className}) = ${speed.className}(this.value - right.value)")

        for (typeName in getMultiplicationTypes()) {
            val rightSuffix = if (typeName == "Double" && speed.floatType == FloatType.SinglePrecision) ".toFloat()" else ""
            next()
            writer.println("\toperator fun times(right: $typeName) = ${speed.className}(this.value * right$rightSuffix)")
            next()
            writer.println("\toperator fun div(right: $typeName) = ${speed.className}(this.value / right$rightSuffix)")
        }

        if (speed.displacementClassName != null) {
            next()
            writer.println("\toperator fun times(right: Duration) = toDouble(SpeedUnit.METERS_PER_SECOND) * ${speed.displacementClassName}.METER * right.toDouble(DurationUnit.SECONDS)")
        }
        if (speed.acceleration != null) {
            writer.println()
            writer.println("\toperator fun div(right: Duration) = toDouble(SpeedUnit.METERS_PER_SECOND) * ${speed.acceleration.className}.MPS2 / right.toDouble(DurationUnit.SECONDS)")
        }
        next()
        writer.println("\toperator fun div(right: ${speed.className}) = this.value / right.value")
    }

    private fun generateCompanionObject() {
        writer.println()
        writer.println("\tcompanion object {")
        writer.println()
        if (speed.number != null) {
            writer.println("\t\tfun raw(value: ${speed.number.internalType}) = ${speed.className}(${speed.number.className}.raw(value))")
        } else {
            writer.println("\t\tfun raw(value: ${speed.floatType!!.typeName}) = ${speed.className}(value)")
        }
        writer.println()
        val oneValue = if (speed.number != null) speed.number.className + ".ONE"
        else if (speed.floatType == FloatType.SinglePrecision) "1f" else "1.0"
        writer.println("\t\tval ${speed.oneUnit} = ${speed.className}($oneValue)")

        for (unit in SpeedUnit.entries) {
            if (unit != speed.oneUnit) {
                val factor = speed.oneUnit.factor / unit.factor

                val canRepresent = if (speed.number != null) {
                    val rawValue = BigDecimal.valueOf(speed.number.oneValue.toDouble()) * BigDecimal.valueOf(factor)
                    speed.number.internalType.canRepresent(rawValue.toBigInteger() + BigInteger.TWO)
                } else true

                if (canRepresent) {
                    writer.println()
                    writer.println("\t\tval $unit = ${speed.oneUnit} * $factor")
                    unitConstants.add(unit)
                }
            }
        }

        writer.println("\t}")
    }

    private fun generateExtensionFunctions() {
        for (typeName in getMultiplicationTypes()) {
            writer.println()
            if (speed.number?.checkOverflow == true) writer.println("@Throws(FixedPointException::class)")
            writer.println("operator fun $typeName.times(right: ${speed.className}) = right * this")

            if (speed.createNumberExtensions) {
                for (unit in unitConstants) {
                    writer.println()
                    writer.println("val $typeName.${unit.abbreviation.replace('/', 'p')}")
                    writer.println("\tget() = ${speed.className}.$unit * this")
                }
            }
        }
        writer.println()
        writer.println("operator fun Duration.times(right: ${speed.className}) = right * this")
    }

    private fun generateMathFunctions() {
        if (speed.number?.internalType?.signed != false) {
            writer.println()
            if (speed.number?.checkOverflow == true) writer.println("@Throws(FixedPointException::class)")
            writer.println("fun abs(x: ${speed.className}) = ${speed.className}(abs(x.value))")
        }

        writer.println()
        writer.println("fun min(a: ${speed.className}, b: ${speed.className}) = ${speed.className}(min(a.value, b.value))")
        writer.println()
        writer.println("fun max(a: ${speed.className}, b: ${speed.className}) = ${speed.className}(max(a.value, b.value))")
    }
}
