package fixie.generator.number

import java.io.PrintWriter
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.max

internal class NumberClassGenerator(
        private val writer: PrintWriter,
        private val number: NumberClass,
        private val packageName: String
) {

    fun generate() {
        generateClassPrefix()
        generateToString()
        generateConversions()
        generateArithmetic()
        generateCompanionObject()
        generateArrayClass()
        writer.println("}")
        generateExtensionFunctions()
        generateMathFunctions()
    }

    private fun generateClassPrefix() {
        val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
        writer.println("// Generated by fixie at $currentTime")
        writer.println("package $packageName")

        val imports = mutableListOf<String>()
        if (number.internalType.numBytes == 8 || isPowerOf10(number.oneValue) == null) imports.add("java.math.BigInteger")
        if (number.internalType.signed || number.checkOverflow) imports.add("java.lang.Math.*")

        if (imports.isNotEmpty()) {
            writer.println()
            for (importedClass in imports) writer.println("import $importedClass")
        }

        writer.println()
        writer.println(number.internalType.declareValue("private const val RAW_ONE", number.oneValue))
        writer.println()
        writer.println("@JvmInline")
        writer.println("value class ${number.className} private constructor(val raw: ${number.internalType.className}) : " +
                "Comparable<${number.className}> {")
    }

    private fun isPowerOf10(value: BigInteger): Int? {
        var candidate = BigInteger.ONE
        var exponent = 0
        for (power in 0 until 25) {
            if (value == candidate) return exponent
            candidate *= BigInteger.TEN
            exponent += 1
        }
        return null
    }

    private fun highestPowerOf10Below(value: BigInteger): Pair<BigInteger, Int> {
        var candidate = BigInteger.ONE
        var exponent = 0
        while (candidate < value) {
            candidate *= BigInteger.TEN
            exponent += 1
        }
        return Pair(candidate / BigInteger.TEN, exponent - 1)
    }

    private fun generateToString() {
        writer.println()
        writer.println("\toverride fun toString(): String {")
        writer.println("\t\tval intPart = raw / RAW_ONE")
        val maybe10Power = isPowerOf10(number.oneValue)
        if (maybe10Power != null) {
            writer.println("\t\tvar fractPart = (raw % RAW_ONE).toString().replace(\"-\", \"\")")
            val loopCondition = if (maybe10Power == 1) "fractPart.isEmpty()" else "fractPart.length < $maybe10Power"
            writer.println("\t\twhile ($loopCondition) fractPart = \"0\$fractPart\"")
        } else {
            val (power, exponent) = highestPowerOf10Below(number.oneValue)
            val toLong = if (number.internalType.numBytes == 8 && number.internalType.signed) "" else ".toLong()"

            val nextPower = if (power * BigInteger.TEN <= BigInteger.valueOf(Long.MAX_VALUE)) "BigInteger.valueOf(${power * BigInteger.TEN})"
            else if (number.internalType.signed) "(BigInteger.valueOf($power) * BigInteger.TEN)"
            else "(uLongToBigInteger(${power}u) * BigInteger.TEN)"

            val (divByOne, remainder) = if (number.internalType.numBytes == 8 && !number.internalType.signed) {
                Pair("uLongToBigInteger(RAW_ONE)", "uLongToBigInteger(raw % RAW_ONE)")
            } else Pair("BigInteger.valueOf(RAW_ONE.toLong())", "BigInteger.valueOf((raw % RAW_ONE)$toLong)")

            writer.println("\t\tval bigFract = $remainder * $nextPower")
            writer.println("\t\tval fractNumber = bigFract / $divByOne")
            writer.println("\t\tvar fractPart = fractNumber.toString().replace(\"-\", \"\")")
            val loopCondition = if (exponent == 0) "fractPart.isEmpty()" else "fractPart.length < ${exponent + 1}"
            writer.println("\t\twhile ($loopCondition) fractPart = \"0\$fractPart\"")
        }
        writer.println("\t\tfractPart = \".\$fractPart\"")
        writer.println("\t\twhile (fractPart.endsWith('0')) fractPart = fractPart.substring(0 until fractPart.length - 1)")
        writer.println("\t\tif (fractPart == \".\") fractPart = \"\"")
        if (number.internalType.signed) {
            val suffix = if (number.internalType.numBytes == 8) "L" else ""
            writer.println("\t\tval minus = if (raw < 0$suffix && intPart == 0$suffix) \"-\" else \"\"")
            writer.println("\t\treturn \"\$minus\$intPart\$fractPart\"")
        } else writer.println("\t\treturn \"\$intPart\$fractPart\"")
        writer.println("\t}")
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
        generateDivSelf()
        generateStandardTypesArithmetic("div", "/")
        generateCompareToSelf()
        generateCompareToOtherTypes()
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
            if ((otherType == "Int" || otherType == "Long") && (operator == "*" || operator == "/")) {

                val numOtherBytes = if (otherType == "Int") 4 else 8
                val right = if (number.internalType.signed) "right" else "right.to${IntType(false, max(numOtherBytes, number.internalType.numBytes))}()"
                var raw = "raw"
                if (number.internalType.numBytes < numOtherBytes) raw = "raw.to${IntType(number.internalType.signed, numOtherBytes)}()"

                if (number.checkOverflow) {

                    var function = if (operator == "*") "multiplyExact($raw, $right)" else "$raw / $right"
                    if (number.internalType.numBytes < 4 || (number.internalType.numBytes == 4 && otherType == "Long")) {
                        function = "to${number.internalType}Exact($function)"
                    }

                    writer.println("\toperator fun $methodName(right: $otherType): ${number.className} {")
                    if (!number.internalType.signed && operator == "*") {
                        writer.println("\t\tif (right < 0) throw FixedPointException(\"Negative numbers are not supported\")")
                    }

                    if (operator == "*") {
                        generateTryCatch("\t\t", listOf(
                            "return ${number.className}($function)"
                        ), "Can't represent \$this * \$right")
                    } else {
                        val suffix = if (otherType == "Long") "L" else ""
                        if (number.internalType.signed) {
                            writer.println("\t\tif (right == 0$suffix || (raw == ${number.internalType}.MIN_VALUE && right == -1$suffix)) {")
                        } else writer.println("\t\tif (right <= 0$suffix) {")
                        writer.println("\t\t\tthrow FixedPointException(\"Can't represent \$this / \$right\")")
                        writer.println("\t\t}")
                        writer.println("\t\treturn ${number.className}($function)")
                    }
                    writer.println("\t}")
                } else {
                    var function = "raw $operator $right"
                    if (number.internalType.numBytes < numOtherBytes) function = "($function).to${number.internalType}()"
                    writer.println("\toperator fun $methodName(right: $otherType) = ${number.className}($function)")
                }
            } else writer.println("\toperator fun $methodName(right: $otherType) = this $operator from(right)")
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
                        writer.println("\t\tif (highProductBits < -(1L shl ${exponent - 1}) || highProductBits >= (1L shl ${exponent - 1})) {")
                    } else {
                        writer.println("\t\tif (highProductBits >= (1uL shl $exponent)) {")
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
                if (number.internalType.numBytes >= 4) {
                    generateTryCatch("\t\t", listOf(
                        "return ${number.className}(to${number.internalType}Exact($backReduction))"
                    ), "Can't represent \$this * \$right")
                } else writer.println("\t\treturn ${number.className}(to${number.internalType}Exact($backReduction))")
            } else writer.println("\t\treturn ${number.className}(($backReduction).to${number.internalType}())")
        }
        writer.println("\t}")
    }

    private fun generateDivSelf() {
        writer.println()
        if (number.checkOverflow) writer.println("\t@Throws(FixedPointException::class)")
        writer.println("\toperator fun div(right: ${number.className}): ${number.className} {")
        if (number.internalType.numBytes == 8) {

            val exponent: Int? = if (number.oneValue <= BigInteger.valueOf(Long.MAX_VALUE)) {
                val oneValue = number.oneValue.longValueExact()
                if (oneValue.countOneBits() == 1) oneValue.countTrailingZeroBits() else null
            } else {
                if (number.oneValue == -BigInteger.valueOf(Long.MIN_VALUE)) 63 else null
            }

            if (exponent == null) {
                writer.println("\t\tval highProductBits = multiplyHigh(this.raw, RAW_ONE)")
                writer.println("\t\tval lowProductBits = this.raw * RAW_ONE")
            } else {
                writer.println("\t\tval highProductBits = this.raw shr ${64 - exponent}")
                writer.println("\t\tval lowProductBits = this.raw shl $exponent")
            }

            if (number.internalType.signed) {
                writer.println("\t\treturn if ((highProductBits == 0L && this.raw >= 0 && lowProductBits >= 0) || (highProductBits == -1L && this.raw < 0 && lowProductBits < 0)) {")
            } else {
                writer.println("\t\treturn if (highProductBits == 0uL) {")
            }

            writer.println("\t\t\t${number.className}(lowProductBits / right.raw)")
            writer.println("\t\t} else {")

            fun toBigInt(parameter: String) = if (number.internalType.signed) "BigInteger.valueOf($parameter)"
            else "uLongToBigInteger($parameter)"

            writer.println("\t\t\tval result = (${toBigInt("this.raw")} * ${toBigInt("RAW_ONE")}) / ${toBigInt("right.raw")}")
            if (number.checkOverflow) {
                val conversion = if (number.internalType.signed) "result.longValueExact()" else "bigIntegerToULong(result, true)"
                generateTryCatch("\t\t\t", listOf(
                    "${number.className}($conversion)"
                ), "Can't compute \$this / \$right")
            } else {
                val conversion = if (number.internalType.signed) "result.toLong()" else "bigIntegerToULong(result, false)"
                writer.println("\t\t\t${number.className}($conversion)")
            }
            writer.println("\t\t}")
        } else {
            var largerType = IntType(number.internalType.signed, 2 * number.internalType.numBytes)
            val oneValue = number.oneValue.longValueExact()
            val multiplication = if (oneValue.countOneBits() == 1) {
                if (largerType.numBytes == 2) largerType = IntType(largerType.signed, 4)
                "shl ${oneValue.countTrailingZeroBits()}"
            } else "* RAW_ONE.to$largerType()"

            writer.println("\t\tval largeValue = this.raw.to$largerType() $multiplication")

            if (number.checkOverflow) {
                generateTryCatch("\t\t", listOf(
                    "return ${number.className}(to${number.internalType}Exact(largeValue / right.raw))"
                ), "Can't represent \$this / \$right")
            } else writer.println("\t\treturn ${number.className}((largeValue / right.raw).to${number.internalType}())")
        }
        writer.println("\t}")
    }

    private fun generateCompareToSelf() {
        writer.println()
        writer.println("\toverride operator fun compareTo(other: ${number.className}) = this.raw.compareTo(other.raw)")
    }

    private fun generateCompareToOtherTypes() {
        val minIntValue = number.internalType.getMinValue() / number.oneValue
        val maxIntValue = number.internalType.getMaxValue() / number.oneValue

        fun generateFloat(typeName: String) {
            val extraChecks = if (typeName == "Float") {
                val minValue = number.internalType.getMinValue().toFloat() / number.oneValue.toFloat()
                val maxValue = number.internalType.getMaxValue().toFloat() / number.oneValue.toFloat()
                " if (other < ${minValue}f) 1 else if (other > ${maxValue}f) -1 else"
            } else {
                val minValue = number.internalType.getMinValue().toDouble() / number.oneValue.toDouble()
                val maxValue = number.internalType.getMaxValue().toDouble() / number.oneValue.toDouble()
                " if (other < $minValue) 1 else if (other > $maxValue) -1 else"
            }
            writer.println("\toperator fun compareTo(other: $typeName) =$extraChecks this.compareTo(from(other))")
        }

        fun generateInt(type: IntType) {
            writer.println()

            val canOverflow = type.canRepresent(maxIntValue.add(BigInteger.ONE))
            val canUnderflow = type.canRepresent(minIntValue.subtract(BigInteger.ONE))

            val extraChecks = if (canOverflow && canUnderflow) {
                " if (other < $minIntValue) 1 else if (other > $maxIntValue) -1 else"
            } else if (canOverflow) {
                " if (other > ${maxIntValue}u) -1 else"
            } else if (canUnderflow) {
                " if (other < ${minIntValue}) 1 else"
            } else ""
            writer.println("\toperator fun compareTo(other: $type) =$extraChecks this.compareTo(from(other))")
            if (!number.internalType.signed && type.signed) {
                generateInt(IntType(false, type.numBytes))
            }
        }

        if (number.internalType.numBytes < 4) generateInt(IntType(true, number.internalType.numBytes))
        generateInt(IntType(true, 4))
        generateInt(IntType(true, 8))
        generateFloat("Float")
        generateFloat("Double")
    }

    private fun generateFromIntType(intType: IntType) {
        writer.println()
        if (intType.numBytes > number.internalType.numBytes) {
            if (number.checkOverflow) {
                writer.println("\t\t@Throws(FixedPointException::class)")
                writer.println("\t\tfun from(value: $intType): ${number.className} {")
                generateTryCatch("\t\t\t", listOf(
                    "return from(to${number.internalType}Exact(value))"
                ), "Can't represent \$value")
                writer.println("\t\t}")
            } else {
                writer.println("\t\tfun from(value: $intType) = from(value.to${number.internalType}())")
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
                val toInternalType = if (intType.numBytes < 8 && number.internalType.numBytes == 8) ".to${number.internalType}()" else ""
                generateTryCatch(
                    "\t\t\t", listOf(
                        "return ${number.className}(multiplyExact(value$toInternalType, RAW_ONE))"
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
        for (signed in setOf(true, number.internalType.signed)) {
            if (number.internalType.numBytes < 4) generateFromIntType(IntType(signed, number.internalType.numBytes))
            generateFromIntType(IntType(signed, 4))
            generateFromIntType(IntType(signed, 8))
        }
        generateFromFloatAndDouble()
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
        writer.println("\t\tconstructor(size: Int, initializer: (Int) -> ${number.className}) : this(${number.internalType}Array(size) { index -> initializer(index).raw })")
        writer.println()
        writer.println("\t\tval size: Int")
        writer.println("\t\t\tget() = raw.size")
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
        generateArithmeticExtensionFunctions("div", "/")
        generateCompareToExtensionFunctions()
    }

    private fun generateArithmeticExtensionFunctions(methodName: String, operator: String) {
        for (typeName in arrayOf("Int", "Long", "Float", "Double")) {
            writer.println()
            if (number.checkOverflow) writer.println("@Throws(FixedPointException::class)")
            if (operator == "*") writer.println("operator fun $typeName.times(right: ${number.className}) = right * this")
            else writer.println("operator fun $typeName.$methodName(right: ${number.className}) = ${number.className}.from(this) $operator right")
        }
    }

    private fun generateCompareToExtensionFunctions() {
        for (typeName in arrayOf("Int", "Long", "Float", "Double")) {
            writer.println()
            writer.println("operator fun $typeName.compareTo(right: ${number.className}) = ${number.className}.from(this).compareTo(right)")
        }
    }

    private fun generateMathFunctions() {
        val prefix = if (number.internalType.numBytes >= 4) "kotlin.math." else ""
        if (number.internalType.signed) {
            writer.println()
            if (number.checkOverflow) {
                writer.println("@Throws(FixedPointException::class)")
                writer.println("fun abs(value: ${number.className}) = if (value.raw != ${number.internalType}.MIN_VALUE) ${number.className}.raw(${prefix}abs(value.raw))")
                writer.println("\t\telse throw FixedPointException(\"Can't represent abs of min value\")")
            } else writer.println("fun abs(value: ${number.className}) = ${number.className}.raw(${prefix}abs(value.raw))")
        }
        generateBinaryMathFunction("min", prefix)
        generateBinaryMathFunction("max", prefix)
    }

    private fun generateBinaryMathFunction(name: String, prefix: String) {
        writer.println()
        writer.println("fun $name(a: ${number.className}, b: ${number.className}) = ${number.className}.raw($prefix$name(a.raw, b.raw))")
    }
}
