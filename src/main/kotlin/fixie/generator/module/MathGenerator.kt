package fixie.generator.module

import fixie.generator.angle.AngleClass
import fixie.generator.number.IntType
import fixie.generator.number.NumberClass
import java.io.File
import java.io.PrintWriter

private fun addExactUnsigned(type: IntType) = listOf(
        "@Throws(ArithmeticException::class)",
        "fun addExact(left: $type, right: $type) = if ($type.MAX_VALUE - right >= left) (left + right)${if (type.numBytes == 4) "" else ".to$type()"} else throw ArithmeticException()"
)

private fun subtractExactUnsigned(type: IntType) = listOf(
        "@Throws(ArithmeticException::class)",
        "fun subtractExact(left: $type, right: $type) = if (left >= right) (left - right)${if (type.numBytes == 4) "" else ".to$type()"} else throw ArithmeticException()"
)

private fun exactSigned(type: IntType, functionName: String, operator: Char) = listOf(
        "@Throws(ArithmeticException::class)",
        "fun ${functionName}Exact(left: $type, right: $type): $type {",
        "    val intResult = left.toInt() $operator right.toInt()",
        "    if (intResult < $type.MIN_VALUE || intResult > $type.MAX_VALUE) throw ArithmeticException()",
        "    return intResult.to$type()",
        "}"
)

private fun minOrMax(type: IntType, functionName: String, operator: String) = listOf(
        "fun $functionName(a: $type, b: $type) = if (a $operator b) a else b"
)

private fun abs(type: IntType) = listOf(
        "fun abs(value: $type) = if (value < 0) (-value).to$type() else value"
)

private fun multiplyExactSmallUnsigned(type: IntType, largerType: String) = listOf(
        "@Throws(ArithmeticException::class)",
        "fun multiplyExact(left: $type, right: $type) = to${type}Exact(left.to$largerType() * right.to$largerType())"
)

private fun multiplyExactSmallMixed(numBytes: Int, largerType: String): List<String> {
    val signedType = IntType(true, numBytes)
    val unsignedType = IntType(false, numBytes)
    return listOf(
            "@Throws(ArithmeticException::class)",
            "fun multiplyExact(left: $signedType, right: $unsignedType) = to${unsignedType}Exact(left.to$largerType() * right.to$largerType())"
    )
}

private fun multiplyExactSignedWithULong(numBytes: Int): List<String> {
    val signedType = IntType(true, numBytes)
    val leftZero = if (numBytes == 8) "0L" else "0"

    return listOf(
            "@Throws(ArithmeticException::class)",
            "fun multiplyExact(left: $signedType, right: ULong): ULong {",
            "    if (left < $leftZero) throw ArithmeticException()",
            "    if (left == $leftZero || right == 0uL) return 0uL",
            "    val result = left.toU$signedType() * right",
            "    if (result / left.toU$signedType() != right) throw ArithmeticException()",
            "    return result",
            "}"
    )
}

private fun multiplyExactULongWithULong() = listOf(
        "@Throws(ArithmeticException::class)",
        "fun multiplyExact(left: ULong, right: ULong): ULong {",
        "    if (left == 0uL || right == 0uL) return 0uL",
        "    val result = left * right",
        "    if (result / left != right) throw ArithmeticException()",
        "    return result",
        "}"
)

private fun multiplyHigh() = listOf(
        "fun multiplyHigh(x: ULong, y: ULong): ULong {",
        "    // Ripped from Math.multiplyHigh",
        "    val x1 = x shr 32",
        "    val y1 = y shr 32",
        "    val x2 = x and 0xFFFFFFFFuL",
        "    val y2 = y and 0xFFFFFFFFuL",
        "    val a = x1 * y1",
        "    val b = x2 * y2",
        "    val c = (x1 + x2) * (y1 + y2)",
        "    val k = c - a - b",
        "    return ((b shr 32) + k shr 32) + a",
        "}"
)

private fun toExact(type: IntType, otherType: String, minCondition: String) = listOf(
        "@Throws(ArithmeticException::class)",
        "fun to${type}Exact(value: $otherType): $type {",
        "    if (${minCondition}value > $type.MAX_VALUE.to$otherType()) throw ArithmeticException(\"Can't convert \$value to $type\")",
        "    return value.to$type()",
        "}"
)

private fun toSignedExact(type: IntType, otherType: String) = toExact(type, otherType, "value < $type.MIN_VALUE.to$otherType() || ")

private fun uLongToBigInteger() = listOf(
        "fun uLongToBigInteger(value: ULong): BigInteger {",
        "    val longValue = value.toLong()",
        "    return if (longValue >= 0) BigInteger.valueOf(longValue)",
        "    else BigInteger.valueOf(longValue).add(-BigInteger.valueOf(Long.MIN_VALUE).multiply(BigInteger.TWO))",
        "}"
)

private fun bigIntegerToULong() = listOf(
        "@Throws(ArithmeticException::class)",
        "fun bigIntegerToULong(value: BigInteger, checkOverflow: Boolean): ULong {",
        "    if (checkOverflow && (value.signum() == -1 || value >= BigInteger.TWO.pow(64))) throw ArithmeticException()",
        "    return value.toLong().toULong()",
        "}"
)

internal fun generateMathFile(numbers: List<NumberClass>, angles: List<AngleClass>, packageName: String, file: File) {
    val functions = mutableSetOf<List<String>>()
    for (number in numbers) {
        if (number.checkOverflow) {
            if (!number.internalType.signed) {
                functions.add(addExactUnsigned(number.internalType))
                functions.add(subtractExactUnsigned(number.internalType))
                if (number.internalType.numBytes <= 4) {
                    functions.add(multiplyExactSmallUnsigned(IntType(false, 4), "Long"))
                    functions.add(toExact(IntType(false, 4), "Long", "value < 0L || "))
                    functions.add(multiplyExactSmallMixed(
                            number.internalType.numBytes,
                            if (number.internalType.numBytes == 4) "Long" else "Int"
                    ))
                    if (number.internalType.numBytes <= 2) {
                        functions.add(multiplyExactSmallUnsigned(number.internalType, "Int"))
                        functions.add(toExact(number.internalType, "Int", "value < 0 || "))
                        functions.add(toExact(number.internalType, "UInt", ""))
                    }
                    functions.add(toExact(number.internalType, "Long", "value < 0L || "))
                    functions.add(toExact(number.internalType, "ULong", ""))
                } else {
                    functions.add(multiplyExactSignedWithULong(4))
                    functions.add(multiplyExactSignedWithULong(8))
                }
                functions.add(multiplyExactULongWithULong())
            } else {
                if (number.internalType.numBytes <= 2) {
                    functions.add(exactSigned(number.internalType, "add", '+'))
                    functions.add(exactSigned(number.internalType, "subtract", '-'))
                    functions.add(exactSigned(number.internalType, "multiply", '*'))
                    functions.add(toSignedExact(number.internalType, "Int"))
                    functions.add(toSignedExact(number.internalType, "Long"))
                }
            }
        }
    }

    for (internalType in numbers.map { it.internalType } + angles.map { it.internalType }) {
        if (internalType.numBytes <= 2) {
            functions.add(minOrMax(internalType, "min", "<="))
            functions.add(minOrMax(internalType, "max", ">="))
            if (internalType.signed) functions.add(abs(internalType))
        }

        if (internalType.numBytes == 8 && !internalType.signed) {
            functions.add(multiplyHigh())
            functions.add(bigIntegerToULong())
            functions.add(uLongToBigInteger())
        }
    }

    if (functions.isNotEmpty()) {
        val writer = PrintWriter(file)
        writer.println("package $packageName")
        if (numbers.find {
            !it.internalType.signed && it.internalType.numBytes == 8
        } != null || angles.find { !it.internalType.signed && it.internalType.numBytes == 8 } != null) {
            writer.println()
            writer.println("import java.math.BigInteger")
        }

        for (functionBody in functions) {
            writer.println()
            for (line in functionBody) writer.println(line)
        }

        writer.flush()
        writer.close()
    }
}
