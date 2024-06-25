package fixie.generator.angle

import fixie.generator.spin.SpinUnit
import java.io.PrintWriter
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

internal class AngleClassGenerator(
        private val writer: PrintWriter,
        private val angle: AngleClass,
        private val packageName: String
) {

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
        writer.println("import java.text.DecimalFormat")
        writer.println("import java.util.Locale")
        writer.println("import kotlin.math.*")
        if (angle.spinClass != null && angle.allowDivisionAndFloatMultiplication) {
            writer.println("import kotlin.time.Duration")
            writer.println("import kotlin.time.DurationUnit")
        }
        writer.println()
        writer.println("@JvmInline")
        writer.print("value class ${angle.className} internal constructor(val raw: ${angle.internalType}) ")
        if (angle.allowComparisons) writer.print(": Comparable<${angle.className}> ")
        writer.println("{")
    }

    private fun generateToDouble() {
        writer.println()
        writer.println("\tfun toDouble(unit: AngleUnit) = when(unit) {")
        val internalWrapValue = if (angle.internalType.signed) -angle.internalType.getMinValue()
                else BigInteger.ONE + angle.internalType.getMaxValue()
        for (unit in AngleUnit.entries) {
            val maxValue = if (angle.internalType.signed) 0.5 * unit.maxValue else unit.maxValue
            writer.println("\t\tAngleUnit.$unit -> ${maxValue / internalWrapValue.toDouble()} * raw.toDouble()")
        }
        writer.println("\t}")
    }

    private fun generateToString() {
        writer.println()
        writer.println("\toverride fun toString() = toString(AngleUnit.${angle.displayUnit}, 1)")
        writer.println()
        writer.println("\tfun toString(unit: AngleUnit, maximumFractionDigits: Int): String {")
        writer.println("\t\tval format = DecimalFormat.getInstance(Locale.ROOT)")
        writer.println("\t\tformat.maximumFractionDigits = maximumFractionDigits")
        writer.println("\t\treturn format.format(toDouble(unit)) + unit.suffix")
        writer.println("\t}")
    }

    private fun generateArithmetic() {
        if (angle.allowComparisons) {
            writer.println()
            writer.println("\toverride operator fun compareTo(other: ${angle.className}) = this.raw.compareTo(other.raw)")
        }

        fun conversion(value: String, numBytes: Int = 4) = if (angle.internalType.numBytes >= numBytes) value
                else "($value).to${angle.internalType}()"

        writer.println()
        if (angle.internalType.signed) {
            writer.println("\toperator fun unaryMinus() = ${angle.className}(${conversion("-this.raw")})")
        } else {
            writer.println("\toperator fun unaryMinus() = ${angle.className}(0u) - this")
        }

        writer.println()
        writer.println("\toperator fun plus(right: ${angle.className}) = ${angle.className}(${conversion("this.raw + right.raw")})")
        writer.println()
        writer.println("\toperator fun minus(right: ${angle.className}) = ${angle.className}(${conversion("this.raw - right.raw")})")

        val fixSign = if (angle.internalType.signed) "" else ".toUInt()"
        if (angle.allowDivisionAndFloatMultiplication) {
            if (angle.internalType.numBytes == 8) {
                for ((functionName, operator) in arrayOf(Pair("times", "*"), Pair("div", "/"))) {
                    writer.println()
                    writer.println("\toperator fun $functionName(right: Float) = $functionName(right.toDouble())")
                    writer.println()
                    writer.println("\toperator fun $functionName(right: Double): ${angle.className} {")
                    writer.println("\t\tvar maybeBig = this.raw.toDouble() $operator right")
                    if (angle.internalType.signed) {
                        writer.println("\t\tif (maybeBig > Long.MAX_VALUE) maybeBig = (maybeBig + Long.MAX_VALUE) % ULong.MAX_VALUE.toDouble() - Long.MAX_VALUE")
                        writer.println("\t\tif (maybeBig < Long.MIN_VALUE) maybeBig = (maybeBig + Long.MIN_VALUE) % ULong.MAX_VALUE.toDouble() - Long.MIN_VALUE")
                        writer.println("\t\treturn ${angle.className}(maybeBig.roundToLong())")
                    } else {
                        writer.println("\t\tif (maybeBig > ULong.MAX_VALUE.toDouble()) maybeBig %= ULong.MAX_VALUE.toDouble()")
                        writer.println("\t\treturn ${angle.className}(maybeBig.toULong())")
                    }
                    writer.println("\t}")
                }
            } else {
                for (typeName in arrayOf("Float", "Double")) {
                    writer.println()
                    writer.println("\toperator fun times(right: $typeName) = ${angle.className}((this.raw.to$typeName() * right).roundToLong().to${angle.internalType}())")
                    writer.println()
                    writer.println("\toperator fun div(right: $typeName) = ${angle.className}((this.raw.to$typeName() / right).roundToLong().to${angle.internalType}())")
                }
            }

            writer.println()
            writer.println("\toperator fun div(right: Int) = ${angle.className}(${conversion("this.raw / right$fixSign")})")
            writer.println()
            writer.println("\toperator fun div(right: Long) = ${angle.className}(${conversion("this.raw / right$fixSign", 8)})")

            writer.println()
            writer.println("\toperator fun div(right: ${angle.className}) = this.raw.toDouble() / right.raw.toDouble()")

            if (angle.spinClass != null) {
                writer.println()
                val angleUnit = if (angle.spinClass.oneUnit == SpinUnit.DEGREES_PER_SECOND) AngleUnit.DEGREES else AngleUnit.RADIANS
                writer.println("\toperator fun div(right: Duration) = ${angle.spinClass.className}.${angle.spinClass.oneUnit} * this.toDouble(AngleUnit.$angleUnit) / right.toDouble(DurationUnit.SECONDS)")
            }
        }

        writer.println()
        writer.println("\toperator fun times(right: Int) = ${angle.className}(${conversion("this.raw * right$fixSign")})")
        writer.println()
        writer.println("\toperator fun times(right: Long) = ${angle.className}(${conversion("this.raw * right$fixSign", 8)})")
    }

    private fun generateCompanionObject() {
        writer.println()
        writer.println("\tcompanion object {")
        writer.println()
        writer.println("\t\tfun raw(value: ${angle.internalType}) = ${angle.className}(value)")

        for (unit in AngleUnit.entries) {

            val maxValue = if (angle.internalType.signed) 0.5 * unit.maxValue else unit.maxValue
            val factor = angle.internalType.getMaxValue().toDouble() / maxValue

            writer.println()

            if (angle.internalType.numBytes == 8) {
                writer.println("\t\tfun ${unit.name.lowercase(Locale.ROOT)}(value: Double): ${angle.className} {")
                writer.println("\t\t\tvar bigResult = (value % ${unit.maxValue}) * $factor")
                if (angle.internalType.signed) {
                    writer.println("\t\t\tif (bigResult > Long.MAX_VALUE) bigResult -= ULong.MAX_VALUE.toDouble()")
                    writer.println("\t\t\tif (bigResult < Long.MIN_VALUE) bigResult += ULong.MAX_VALUE.toDouble()")
                    writer.println("\t\t\treturn raw(bigResult.roundToLong())")
                } else {
                    writer.println("\t\t\tif (bigResult > ULong.MAX_VALUE.toDouble()) bigResult -= ULong.MAX_VALUE.toDouble()")
                    writer.println("\t\t\tif (bigResult < 0) bigResult += ULong.MAX_VALUE.toDouble()")
                    writer.println("\t\t\treturn raw(bigResult.toULong())")
                }
                writer.println("\t\t}")
            } else {
                val signedConversion = "raw(((value % ${unit.maxValue}) * $factor).roundToLong().to${angle.internalType}())"

                if (angle.internalType.signed) writer.println("\t\tfun ${unit.name.lowercase(Locale.ROOT)}(value: Double) = $signedConversion")
                else {
                    writer.println("\t\tfun ${unit.name.lowercase(Locale.ROOT)}(value: Double) = if (value >= 0.0) $signedConversion")
                    writer.println("\t\telse raw(((value % ${unit.maxValue} + ${unit.maxValue}) * $factor).roundToLong().to${angle.internalType}())")
                }
            }

            for (typeName in arrayOf("Int", "Long", "Float")) {
                writer.println()
                val functionName = unit.name.lowercase(Locale.ROOT)
                writer.println("\t\tfun $functionName(value: $typeName) = $functionName(value.toDouble())")
            }
        }

        writer.println("\t}")
    }

    private fun generateExtensionFunctions() {
        for (typeName in arrayOf("Int", "Long", "Float", "Double")) {
            if (angle.allowDivisionAndFloatMultiplication || typeName == "Int" || typeName == "Long") {
                writer.println()
                writer.println("operator fun $typeName.times(right: ${angle.className}) = right * this")
            }

            if (angle.createNumberExtensions) {
                for (unit in AngleUnit.entries) {
                    writer.println()
                    writer.println("val $typeName.${unit.name.lowercase(Locale.ROOT)}")
                    writer.println("\tget() = ${angle.className}.${unit.name.lowercase(Locale.ROOT)}(this)")
                }
            }
        }
    }

    private fun generateMathFunctions() {
        if (angle.internalType.signed) {
            writer.println()
            writer.println("fun abs(x: ${angle.className}) = ${angle.className}(abs(x.raw))")
        }

        if (angle.allowComparisons) {
            writer.println()
            writer.println("fun min(a: ${angle.className}, b: ${angle.className}) = ${angle.className}(min(a.raw, b.raw))")
            writer.println()
            writer.println("fun max(a: ${angle.className}, b: ${angle.className}) = ${angle.className}(max(a.raw, b.raw))")
        }

        for (functionName in arrayOf("sin", "cos", "tan")) {
            writer.println()
            writer.println("fun $functionName(x: ${angle.className}) = $functionName(x.toDouble(AngleUnit.RADIANS))")
        }
    }
}