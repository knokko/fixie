package fixie.generator

import java.io.PrintWriter
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        generateArrayClass()
        writer.println("}")
        generateExtensionFunctions()
        generateMathFunctions()
    }

    private fun generateClassPrefix() {
        val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
        writer.println("// Generated by fixie at $currentTime")
        writer.println("package fixie")
        writer.println()
        if (number.internalType.numBytes == 8 && number.internalType.signed && number.oneValue.toLong().countOneBits() != 1) {
            writer.println("import java.math.BigInteger")
            writer.println()
        }
        if (number.internalType.signed || number.checkOverflow) {
            writer.println("import java.lang.Math.*")
            writer.println()
        }
        writer.println(number.internalType.declareValue("private const val RAW_ONE", number.oneValue))
        writer.println()
        writer.println("@JvmInline")
        writer.println("value class ${number.className} private constructor(val raw: ${number.internalType.className}) : " +
                "Comparable<${number.className}> {")
    }

    private fun generateConversions() {
        generateToIntType(IntType(true, 4))
        generateToIntType(IntType(true, 8))
        generateToFloatAndDouble()
    }

    private fun generateToIntType(type: IntType) {
        writer.println()
        if (type == number.internalType || (number.internalType.signed == type.signed && number.internalType.numBytes < 4 && type.numBytes == 4)) {
            writer.println("\tfun to$type() = raw / RAW_ONE")
        } else {
            val cantOverflow = type.canRepresent(number.internalType.getMaxValue() / number.oneValue)
            val cantUnderflow = type.canRepresent(number.internalType.getMinValue() / number.oneValue)
            if (!number.checkOverflow || (cantOverflow && cantUnderflow)) writer.println("\tfun to$type() = (raw / RAW_ONE).to$type()")
            else {
                writer.println("\t@Throws(FixedPointException::class)")
                writer.println("\tfun to$type(): $type {")
                writer.println("\t\tval uncheckedResult = raw / RAW_ONE")
                val unsignedSuffix = if (number.internalType.signed) "" else ".toU$type()"
                if (!cantUnderflow) writer.println("\t\tif (uncheckedResult < $type.MIN_VALUE) throw FixedPointException(\"\$uncheckedResult to too small\")")
                if (!cantOverflow) writer.println("\t\tif (uncheckedResult > $type.MAX_VALUE$unsignedSuffix) throw FixedPointException(\"\$uncheckedResult to too large\")")
                writer.println("\t\treturn uncheckedResult.to$type()")
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
        generateStandardTypesArithmetic("plus", "+")
        generateMinusSelf()
        generateStandardTypesArithmetic("minus", "-")
        generateTimesSelf()
        generateStandardTypesArithmetic("times", "*")
        // TODO Division
        generateCompareToSelf()
    }

    private fun generateUnaryMinus() {
        if (number.internalType.signed) {
            writer.println()

            val negation = if (number.internalType.numBytes >= 4) "-raw" else "(-raw).to${number.internalType}()"
            if (number.checkOverflow) {
                writer.println("\t@Throws(FixedPointException::class)")
                writer.println("\toperator fun unaryMinus() = if (raw != ${number.internalType}.MIN_VALUE)")
                writer.println("\t\t${number.className}($negation) else throw FixedPointException(\"Can't negate MIN_VALUE\")")
            } else writer.println("\toperator fun unaryMinus() = ${number.className}($negation)")
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

    private fun generateStandardTypesArithmetic(methodName: String, operator: String) {
        for (otherType in arrayOf("Int", "Long", "Float", "Double")) {
            writer.println()
            if (number.checkOverflow) writer.println("\t@Throws(FixedPointException::class)")
            writer.println("\toperator fun $methodName(right: $otherType) = this $operator from(right)")
        }
    }

    private fun generatePlusSelf() {
        writer.println()
        if (number.checkOverflow) {
            writer.println("\t@Throws(FixedPointException::class)")
            writer.println("\toperator fun plus(right: ${number.className}): ${number.className} {")
            generateTryCatch(
                    "\t\t", listOf("return ${number.className}(addExact(this.raw, right.raw))"),
                    "Tried to compute \$this + \$right"
            )
            writer.println("\t}")
        } else {
            var sum = "this.raw + right.raw"
            if (number.internalType.numBytes < 4) sum = "($sum).to${number.internalType}()"
            writer.println("\toperator fun plus(right: ${number.className}) = ${number.className}($sum)")
        }
    }

    private fun generateMinusSelf() {
        writer.println()
        if (number.checkOverflow) {
            writer.println("\t@Throws(FixedPointException::class)")
            writer.println("\toperator fun minus(right: ${number.className}): ${number.className} {")
            generateTryCatch(
                    "\t\t", listOf("return ${number.className}(subtractExact(this.raw, right.raw))"),
                    "Tried to compute \$this - \$right"
            )
            writer.println("\t}")
        } else {
            var difference = "this.raw - right.raw"
            if (number.internalType.numBytes < 4) difference = "($difference).to${number.internalType}()"
            writer.println("\toperator fun minus(right: ${number.className}) = ${number.className}($difference)")
        }
    }

    private fun generateTimesSelf() {
        writer.println()
        if (number.checkOverflow) writer.println("\t@Throws(FixedPointException::class)")
        writer.println("\toperator fun times(right: ${number.className}): ${number.className} {")
        if (number.internalType.numBytes == 8) {
            writer.println("\t\tval highProductBits = multiplyHigh(this.raw, right.raw)")
            writer.println("\t\tval lowProductBits = this.raw * right.raw")

            val exponent: Int? = if (number.oneValue <= BigInteger.valueOf(Long.MAX_VALUE)) {
                val oneValue = number.oneValue.longValueExact()
                if (oneValue.countOneBits() == 1) oneValue.countTrailingZeroBits() else null
            } else {
                if (number.oneValue == -BigInteger.valueOf(Long.MIN_VALUE)) 63 else null
            }

            if (exponent != null) {
                if (number.checkOverflow) {
                    if (number.internalType.signed) {
                        writer.println("\t\tif (highProductBits < -(1 shl ${exponent - 1}) || highProductBits >= (1 shl ${exponent - 1})) {")
                    } else {
                        writer.println("\t\tif (highProductBits >= (1u shl $exponent)) {")
                    }
                    writer.println("\t\t\tthrow FixedPointException(\"Can't represent \$this * \$right\")")
                    writer.println("\t\t}")
                }

                val lowOperator = if (number.internalType.signed) "ushr" else "shr"
                writer.println("\t\treturn ${number.className}((lowProductBits $lowOperator $exponent) or (highProductBits shl ${64 - exponent}))")
            } else {
                if (number.internalType.signed) {
                    writer.println("\t\tval shouldBeNegative = (this.raw < 0) != (right.raw < 0)")
                    writer.println("\t\treturn if ((highProductBits == 0L && !shouldBeNegative && lowProductBits >= 0) || (highProductBits == -1L && shouldBeNegative && lowProductBits < 0)) {")
                } else {
                    writer.println("\t\treturn if (highProductBits == 0uL) {")
                }
                writer.println("\t\t\t${number.className}(lowProductBits / RAW_ONE)")
                writer.println("\t\t} else {")

                fun toBigInt(parameter: String) = if (number.internalType.signed) "BigInteger.valueOf($parameter)"
                else "uLongToBigInteger($parameter)"

                writer.println("\t\t\tval result = (${toBigInt("this.raw")} * ${toBigInt("right.raw")}) / ${toBigInt("RAW_ONE")}")
                if (number.checkOverflow) {
                    val conversion = if (number.internalType.signed) "result.longValueExact()" else "bigIntegerToULong(result, true)"
                    generateTryCatch("\t\t\t", listOf(
                        "${number.className}($conversion)"
                    ), "Can't compute \$this * \$right")
                } else {
                    val conversion = if (number.internalType.signed) "result.toLong()" else "bigIntegerToULong(result, false)"
                    writer.println("\t\t\t${number.className}($conversion)")
                }
                writer.println("\t\t}")
            }
        } else {
            val largerType = IntType(number.internalType.signed, 2 * number.internalType.numBytes)
            writer.println("\t\tval largeValue = this.raw.to$largerType() * right.raw.to$largerType()")

            val oneValue = number.oneValue.longValueExact()
            val backReduction = if (oneValue.countOneBits() == 1) {
                "largeValue shr ${oneValue.countTrailingZeroBits()}"
            } else "largeValue / RAW_ONE"

            if (number.checkOverflow) {
                generateTryCatch("\t\t", listOf(
                    "return ${number.className}(to${number.internalType}Exact($backReduction))"
                ), "Can't represent \$this * \$right")
            } else writer.println("\t\treturn ${number.className}(($backReduction).to${number.internalType}())")
        }
        writer.println("\t}")
    }

    private fun generateCompareToSelf() {
        writer.println()
        writer.println("\toverride operator fun compareTo(other: ${number.className}) = this.raw.compareTo(other.raw)")
    }

    private fun generateFromIntType(intType: IntType) {
        writer.println()
        if (intType.numBytes > number.internalType.numBytes) {
            val downType = IntType(true, number.internalType.numBytes)
            if (number.checkOverflow) {
                writer.println("\t\t@Throws(FixedPointException::class)")
                writer.println("\t\tfun from(value: $intType) = from(to${downType}Exact(value))")
            } else {
                writer.println("\t\tfun from(value: $intType) = from(value.to${downType}())")
            }
        } else {
            val cantOverflow = number.internalType.canRepresent(number.oneValue.multiply(intType.getMaxValue()))
            val cantUnderflow = number.internalType.canRepresent(number.oneValue.multiply(intType.getMinValue()))
            if (!number.checkOverflow || (cantUnderflow && cantOverflow)) {
                val conversion = if (number.internalType == intType) "" else ".to${number.internalType}()"
                var result = "value$conversion * RAW_ONE"
                if (number.internalType.numBytes < 4) result = "($result).to${number.internalType}()"
                writer.println("\t\tfun from(value: $intType) = ${number.className}($result)")
            } else {
                writer.println("\t\t@Throws(FixedPointException::class)")
                writer.println("\t\tfun from(value: $intType): ${number.className} {")
                generateTryCatch(
                    "\t\t\t", listOf(
                        "return ${number.className}(multiplyExact(value, RAW_ONE))"
                    ), "Can't represent \$value"
                )
                writer.println("\t\t}")
            }
        }
    }

    private fun generateFromFloatAndDouble() {
        writer.println()
        writer.println("\t\tfun from(value: Float) = from(value.toDouble())")
        writer.println()
        fun roundingOperation(parameter: String) = if (number.internalType.signed) {
            "round($parameter)"
        } else "kotlin.math.floor($parameter + 0.5)"
        var toFunction = if (number.internalType.signed && number.internalType.numBytes == 8) "" else ".to${number.internalType}()"
        if (!number.internalType.signed && number.internalType.numBytes < 4) toFunction = ".toUInt()$toFunction"
        if (number.checkOverflow) {
            writer.println("\t\t@Throws(FixedPointException::class)")
            writer.println("\t\tfun from(value: Double): ${number.className} {")
            writer.println("\t\t\tval doubleValue = RAW_ONE.toDouble() * value")
            writer.println("\t\t\tif (doubleValue > ${number.internalType}.MAX_VALUE.toDouble() || doubleValue < ${number.internalType}.MIN_VALUE.toDouble()) {")
            writer.println("\t\t\t\tthrow FixedPointException(\"Can't represent \$value\")")
            writer.println("\t\t\t}")
            writer.println("\t\t\treturn ${number.className}(${roundingOperation("doubleValue")}$toFunction)")
            writer.println("\t\t}")
        } else {
            writer.println("\t\tfun from(value: Double) = ${number.className}(${roundingOperation("RAW_ONE.toDouble() * value")}$toFunction)")
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
        if (number.internalType.numBytes < 4) generateFromIntType(IntType(true, number.internalType.numBytes))
        generateFromIntType(IntType(true, 4))
        generateFromIntType(IntType(true, 8))
        generateFromFloatAndDouble()
        // TODO fraction
        writer.println("\t}")
    }

    private fun generateArrayClass() {
        writer.println()
        writer.println("\t@JvmInline")
        if (!number.internalType.signed) writer.println("\t@OptIn(ExperimentalUnsignedTypes::class)")
        writer.println("\tvalue class Array private constructor(val raw: ${number.internalType}Array) {")
        writer.println()
        writer.println("\t\tconstructor(size: Int) : this(${number.internalType}Array(size))")
        writer.println()
        writer.println("\t\toperator fun get(index: Int) = ${number.className}(raw[index])")
        writer.println()
        writer.println("\t\toperator fun set(index: Int, value: ${number.className}) {")
        writer.println("\t\t\traw[index] = value.raw")
        writer.println("\t\t}")
        writer.println()
        writer.println("\t\tfun fill(value: ${number.className}) {")
        writer.println("\t\t\traw.fill(value.raw)")
        writer.println("\t\t}")
        writer.println("\t}")
    }

    private fun generateExtensionFunctions() {
        generateArithmeticExtensionFunctions("plus", "+")
        generateArithmeticExtensionFunctions("minus", "-")
        generateArithmeticExtensionFunctions("times", "*")
    }

    private fun generateArithmeticExtensionFunctions(methodName: String, operator: String) {
        for (typeName in arrayOf("Int", "Long", "Float", "Double")) {
            writer.println()
            if (number.checkOverflow) writer.println("@Throws(FixedPointException::class)")
            writer.println("operator fun $typeName.$methodName(right: ${number.className}) = ${number.className}.from(this) $operator right")
        }
    }

    private fun generateMathFunctions() {
        if (number.internalType.numBytes >= 4) {
            if (number.internalType.signed) generateUnaryMathFunction("abs")
            // TODO Support smaller types
            generateBinaryMathFunction("min")
            generateBinaryMathFunction("max")
        }
    }

    private fun generateUnaryMathFunction(name: String) {
        writer.println("fun $name(value: ${number.className}) = ${number.className}.raw(kotlin.math.$name(value.raw))")
    }

    private fun generateBinaryMathFunction(name: String) {
        writer.println("fun $name(a: ${number.className}, b: ${number.className}) = ${number.className}.raw(kotlin.math.$name(a.raw, b.raw))")
    }
}