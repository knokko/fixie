package fixie.generator.angle

import fixie.generator.quantity.QuantityClassGenerator
import fixie.generator.spin.SpinUnit
import java.io.PrintWriter
import java.math.BigInteger
import java.util.*

internal class AngleClassGenerator(
        writer: PrintWriter,
        angle: AngleClass,
        packageName: String
) : QuantityClassGenerator<AngleClass>(writer, angle, packageName) {

    override fun getImports() = arrayOf("java.text.DecimalFormat", "java.util.Locale", "kotlin.math.*") + if(
        quantity.spinClass != null && quantity.allowDivisionAndFloatMultiplication
    ) arrayOf("kotlin.time.Duration", "kotlin.time.DurationUnit") else emptyArray()

    override fun getInternalTypeName() = quantity.internalType.className

    override fun generateClassHeader() {
        writer.println("@JvmInline")
        writer.print("value class ${quantity.className} internal constructor(val raw: ${quantity.internalType}) ")
        if (quantity.allowComparisons) writer.print(": Comparable<${quantity.className}> ")
        writer.println("{")
    }

    override fun generateToDouble() {
        writer.println()
        writer.println("\tfun toDouble(unit: AngleUnit) = when(unit) {")
        val internalWrapValue = if (quantity.internalType.signed) - quantity.internalType.getMinValue()
                else BigInteger.ONE + quantity.internalType.getMaxValue()
        for (unit in AngleUnit.entries) {
            val maxValue = if (quantity.internalType.signed) 0.5 * unit.maxValue else unit.maxValue
            writer.println("\t\tAngleUnit.$unit -> ${maxValue / internalWrapValue.toDouble()} * raw.toDouble()")
        }
        writer.println("\t}")
    }

    override fun generateToString() {
        writer.println()
        writer.println("\toverride fun toString() = toString(AngleUnit.${quantity.displayUnit}, 1)")
        writer.println()
        writer.println("\tfun toString(unit: AngleUnit, maximumFractionDigits: Int): String {")
        writer.println("\t\tval format = DecimalFormat.getInstance(Locale.ROOT)")
        writer.println("\t\tformat.maximumFractionDigits = maximumFractionDigits")
        writer.println("\t\treturn format.format(toDouble(unit)) + unit.suffix")
        writer.println("\t}")
    }

    override fun generateArithmetic() {
        if (quantity.allowComparisons) {
            writer.println()
            writer.println("\toverride operator fun compareTo(other: ${quantity.className}) = this.raw.compareTo(other.raw)")
        }

        fun conversion(value: String, numBytes: Int = 4) = if (quantity.internalType.numBytes >= numBytes) value
                else "($value).to${quantity.internalType}()"

        writer.println()
        if (quantity.internalType.signed) {
            writer.println("\toperator fun unaryMinus() = ${quantity.className}(${conversion("-this.raw")})")
        } else {
            writer.println("\toperator fun unaryMinus() = ${quantity.className}(0u) - this")
        }

        writer.println()
        writer.println("\toperator fun plus(right: ${quantity.className}) = ${quantity.className}(${conversion("this.raw + right.raw")})")
        writer.println()
        writer.println("\toperator fun minus(right: ${quantity.className}) = ${quantity.className}(${conversion("this.raw - right.raw")})")

        val fixSign = if (quantity.internalType.signed) "" else ".toUInt()"
        if (quantity.allowDivisionAndFloatMultiplication) {
            if (quantity.internalType.numBytes == 8) {
                for ((functionName, operator) in arrayOf(Pair("times", "*"), Pair("div", "/"))) {
                    writer.println()
                    writer.println("\toperator fun $functionName(right: Float) = $functionName(right.toDouble())")
                    writer.println()
                    writer.println("\toperator fun $functionName(right: Double): ${quantity.className} {")
                    writer.println("\t\tvar maybeBig = this.raw.toDouble() $operator right")
                    if (quantity.internalType.signed) {
                        writer.println("\t\tif (maybeBig > Long.MAX_VALUE) maybeBig = (maybeBig + Long.MAX_VALUE) % ULong.MAX_VALUE.toDouble() - Long.MAX_VALUE")
                        writer.println("\t\tif (maybeBig < Long.MIN_VALUE) maybeBig = (maybeBig + Long.MIN_VALUE) % ULong.MAX_VALUE.toDouble() - Long.MIN_VALUE")
                        writer.println("\t\treturn ${quantity.className}(maybeBig.roundToLong())")
                    } else {
                        writer.println("\t\tif (maybeBig > ULong.MAX_VALUE.toDouble()) maybeBig %= ULong.MAX_VALUE.toDouble()")
                        writer.println("\t\treturn ${quantity.className}(maybeBig.toULong())")
                    }
                    writer.println("\t}")
                }
            } else {
                for (typeName in arrayOf("Float", "Double")) {
                    writer.println()
                    writer.println("\toperator fun times(right: $typeName) = ${quantity.className}((this.raw.to$typeName() * right).roundToLong().to${quantity.internalType}())")
                    writer.println()
                    writer.println("\toperator fun div(right: $typeName) = ${quantity.className}((this.raw.to$typeName() / right).roundToLong().to${quantity.internalType}())")
                }
            }

            writer.println()
            writer.println("\toperator fun div(right: Int) = ${quantity.className}(${conversion("this.raw / right$fixSign")})")
            writer.println()
            writer.println("\toperator fun div(right: Long) = ${quantity.className}(${conversion("this.raw / right$fixSign", 8)})")

            writer.println()
            writer.println("\toperator fun div(right: ${quantity.className}) = this.raw.toDouble() / right.raw.toDouble()")

            if (quantity.spinClass != null) {
                writer.println()
                val angleUnit = if (quantity.spinClass.oneUnit == SpinUnit.DEGREES_PER_SECOND) AngleUnit.DEGREES else AngleUnit.RADIANS
                writer.println("\toperator fun div(right: Duration) = ${quantity.spinClass.className}.${quantity.spinClass.oneUnit} * this.toDouble(AngleUnit.$angleUnit) / right.toDouble(DurationUnit.SECONDS)")
            }
        }

        writer.println()
        writer.println("\toperator fun times(right: Int) = ${quantity.className}(${conversion("this.raw * right$fixSign")})")
        writer.println()
        writer.println("\toperator fun times(right: Long) = ${quantity.className}(${conversion("this.raw * right$fixSign", 8)})")
    }

    override fun generateCompanionContent() {
        writer.println("\t\tfun raw(value: ${quantity.internalType}) = ${quantity.className}(value)")

        for (unit in AngleUnit.entries) {

            val maxValue = if (quantity.internalType.signed) 0.5 * unit.maxValue else unit.maxValue
            val factor = quantity.internalType.getMaxValue().toDouble() / maxValue

            writer.println()

            if (quantity.internalType.numBytes == 8) {
                writer.println("\t\tfun ${unit.name.lowercase(Locale.ROOT)}(value: Double): ${quantity.className} {")
                writer.println("\t\t\tvar bigResult = (value % ${unit.maxValue}) * $factor")
                if (quantity.internalType.signed) {
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
                val signedConversion = "raw(((value % ${unit.maxValue}) * $factor).roundToLong().to${quantity.internalType}())"

                if (quantity.internalType.signed) writer.println("\t\tfun ${unit.name.lowercase(Locale.ROOT)}(value: Double) = $signedConversion")
                else {
                    writer.println("\t\tfun ${unit.name.lowercase(Locale.ROOT)}(value: Double) = if (value >= 0.0) $signedConversion")
                    writer.println("\t\telse raw(((value % ${unit.maxValue} + ${unit.maxValue}) * $factor).roundToLong().to${quantity.internalType}())")
                }
            }

            for (typeName in arrayOf("Int", "Long", "Float")) {
                writer.println()
                val functionName = unit.name.lowercase(Locale.ROOT)
                writer.println("\t\tfun $functionName(value: $typeName) = $functionName(value.toDouble())")
            }
        }
    }

    override fun generateExtensionFunctions() {
        for (typeName in arrayOf("Int", "Long", "Float", "Double")) {
            if (quantity.allowDivisionAndFloatMultiplication || typeName == "Int" || typeName == "Long") {
                writer.println()
                writer.println("operator fun $typeName.times(right: ${quantity.className}) = right * this")
            }

            if (quantity.createNumberExtensions) {
                for (unit in AngleUnit.entries) {
                    writer.println()
                    writer.println("val $typeName.${unit.name.lowercase(Locale.ROOT)}")
                    writer.println("\tget() = ${quantity.className}.${unit.name.lowercase(Locale.ROOT)}(this)")
                }
            }
        }
    }

    override fun generateMathFunctions() {
        if (quantity.internalType.signed) {
            writer.println()
            writer.println("fun abs(x: ${quantity.className}) = ${quantity.className}(abs(x.raw))")
        }

        if (quantity.allowComparisons) {
            writer.println()
            writer.println("fun min(a: ${quantity.className}, b: ${quantity.className}) = ${quantity.className}(min(a.raw, b.raw))")
            writer.println()
            writer.println("fun max(a: ${quantity.className}, b: ${quantity.className}) = ${quantity.className}(max(a.raw, b.raw))")
        }

        for (functionName in arrayOf("sin", "cos", "tan")) {
            writer.println()
            writer.println("fun $functionName(x: ${quantity.className}) = $functionName(x.toDouble(AngleUnit.RADIANS))")
        }
    }
}
