package fixie.generator

import java.io.PrintWriter

class NumberClassGenerator(
        private val writer: PrintWriter,
        private val number: NumberClass
) {

    fun generate() {
        generateClassPrefix()
        generateConversions()
        generateArithmetic()
        // TODO Generate toString
        generateCompanionObject()
        writer.println("}")
        // TODO Extension functions
    }

    private fun generateClassPrefix() {
        writer.println("package fixie")
        writer.println()
        writer.println("import kotlin.math.roundToLong")
        writer.println()
        writer.println(number.internalType.declareValue("private const val RAW_ONE", number.oneValue))
        writer.println()
        writer.println("@JvmInline")
        writer.println("value class ${number.className} private constructor(val raw: ${number.internalType.className}) : " +
                "Comparable<${number.className}> {")
    }

    private fun generateConversions() {
        generateToInt()
        generateToLong()
        generateToFloatAndDouble()
    }

    private fun generateToInt() {
        writer.println()
        val cantOverflow = IntType(true, 4).canRepresent(number.internalType.getMaxValue() / number.oneValue)
        val cantUnderflow = IntType(true, 4).canRepresent(number.internalType.getMinValue() / number.oneValue)
        if (!number.checkOverflow || (cantOverflow && cantUnderflow)) writer.println("\tfun toInt() = (raw / RAW_ONE).toInt()")
        else {
            writer.println("\t@Throws(FixedPointException::class)")
            writer.println("\tfun toInt(): Int {")
            writer.println("\t\tval uncheckedResult = raw / RAW_ONE")
            writer.println("\t\tif (uncheckedResult < Int.MIN_VALUE) throw FixedPointException(\"\$uncheckedResult to too small\")")
            writer.println("\t\tif (uncheckedResult > Int.MAX_VALUE) throw FixedPointException(\"\$uncheckedResult to too large\")")
            writer.println("\t\treturn uncheckedResult.toInt()")
            writer.println("\t}")
        }
    }

    private fun generateToLong() {
        writer.println()
        if (number.internalType.signed) writer.println("\tfun toLong() = raw / RAW_ONE")
        else {
            val cantOverflow = IntType(true, 8).canRepresent(number.internalType.getMaxValue() / number.oneValue)
            if (!number.checkOverflow || !cantOverflow) writer.println("\tfun toLong() = (raw / RAW_ONE).toLong()")
            else {
                writer.println("\t@Throws(FixedPointException::class)")
                writer.println("\tfun toLong(): Long {")
                writer.println("\t\tval uncheckedResult = raw / RAW_ONE")
                writer.println("\t\tif (uncheckedResult > Long.MAX_VALUE) throw FixedPointException(\"\$uncheckedResult to too large\")")
                writer.println("\t\treturn uncheckedResult.toLong()")
                writer.println("\t}")
            }
        }
    }

    private fun generateToFloatAndDouble() {
        writer.println()
        writer.println("\tfun toFloat() = toDouble().toFloat()")
        writer.println()
        writer.println("\tfun toDouble() = raw.toDouble() / RAW_ONE.toDouble()")
    }

    private fun generateArithmetic() {
        generateUnaryMinus()
        generatePlusSelf()
        generateMinusSelf()
        // TODO Multiplication
        // TODO Division
        generateCompareToSelf()
    }

    private fun generateUnaryMinus() {
        if (number.internalType.signed) {
            writer.println()
            if (number.checkOverflow) {
                writer.println("\t@Throws(FixedPointException::class)")
                writer.println("\toperator fun unaryMinus() = if (raw != ${number.internalType}.MIN_VALUE)")
                writer.println("\t\t${number.className}(-raw) else throw FixedPointException(\"Can't negate MIN_VALUE\")")
            } else writer.println("\toperator fun unaryMinus() = ${number.className}(-raw)")
        }
    }

    private fun generateTryCatch(prefix: String, body: List<String>, errorMessage: String) {
        writer.println("${prefix}try {")
        for (line in body) {
            writer.println("$prefix\t$line")
        }
        writer.println("${prefix}} catch (overflow: ArithmeticException) {")
        writer.println("$prefix\tthrow FixedPointException(\"$errorMessage\")")
        writer.println("${prefix}}")
    }

    private fun generatePlusSelf() {
        writer.println()
        if (number.checkOverflow) {
            writer.println("\t@Throws(FixedPointException::class)")
            writer.println("\toperator fun plus(right: ${number.className}): ${number.className} {")
            generateTryCatch(
                    "\t\t", listOf("return ${number.className}(Math.addExact(this.raw, right.raw))"),
                    "Tried to compute \$this + \$right"
            )
            writer.println("\t}")
        } else {
            writer.println("\toperator fun plus(right: ${number.className}) = ${number.className}(this.raw + right.raw)")
        }
    }

    private fun generateMinusSelf() {
        writer.println()
        if (number.checkOverflow) {
            writer.println("\t@Throws(FixedPointException::class)")
            writer.println("\toperator fun minus(right: ${number.className}): ${number.className} {")
            generateTryCatch(
                    "\t\t", listOf("return ${number.className}(Math.subtractExact(this.raw, right.raw))"),
                    "Tried to compute \$this - \$right"
            )
            writer.println("\t}")
        } else {
            writer.println("\toperator fun minus(right: ${number.className}) = ${number.className}(this.raw - right.raw)")
        }
    }

    private fun generateCompareToSelf() {
        writer.println()
        writer.println("\toverride operator fun compareTo(other: ${number.className}) = this.raw.compareTo(other.raw)")
    }

    private fun generateFromIntType(intType: IntType) {
        writer.println()
        val cantOverflow = number.internalType.canRepresent(number.oneValue.multiply(intType.getMaxValue()))
        val cantUnderflow = number.internalType.canRepresent(number.oneValue.multiply(intType.getMinValue()))
        if (!number.checkOverflow || (cantUnderflow && cantOverflow)) {
            writer.println("\t\tfun from(value: $intType) = ${number.className}(value * RAW_ONE)")
        } else {
            writer.println("\t\t@Throws(FixedPointException::class)")
            writer.println("\t\tfun from(value: $intType): ${number.className} {")
            generateTryCatch("\t\t\t", listOf(
                    "return ${number.className}(Math.multiplyExact(value, RAW_ONE))"
            ), "Can't represent \$value")
            writer.println("\t\t}")
        }
    }

    private fun generateFromFloatAndDouble() {
        writer.println()
        writer.println("\t\tfun from(value: Float) = from(value.toDouble())")
        writer.println()
        if (number.checkOverflow) {
            writer.println("\t\t@Throws(FixedPointException::class)")
            writer.println("\t\tfun from(value: Double): ${number.className} {")
            writer.println("\t\t\tval doubleValue = RAW_ONE.toDouble() * value")
            writer.println("\t\t\tif (doubleValue > ${number.internalType}.MAX_VALUE.toDouble() || doubleValue < ${number.internalType}.MIN_VALUE.toDouble()) {")
            writer.println("\t\t\t\tthrow FixedPointException(\"Can't represent \$value\")")
            writer.println("\t\t\t}")
            writer.println("\t\t\treturn ${number.className}(doubleValue.roundTo${number.internalType}())")
            writer.println("\t\t}")
        } else {
            writer.println("\t\tfun from(value: Double) = (RAW_ONE.toDouble() * value).roundTo${number.internalType}()")
        }
    }

    private fun generateCompanionObject() {
        writer.println()
        writer.println("\tcompanion object {")
        writer.println()
        writer.println("\t\tval ZERO = from(0)")
        writer.println("\t\tval ONE = from(1)")
        writer.println()
        writer.println("\t\tfun raw(rawValue: ${number.internalType}) = ${number.className}(rawValue)")
        generateFromIntType(IntType(true, 4))
        generateFromIntType(IntType(true, 8))
        generateFromFloatAndDouble()
        // TODO fraction
        writer.println("\t}")
    }
}
