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
	"\tval intResult = left.toInt() $operator right.toInt()",
	"\tif (intResult < $type.MIN_VALUE || intResult > $type.MAX_VALUE) throw ArithmeticException()",
	"\treturn intResult.to$type()",
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
		"\tif (left < $leftZero) throw ArithmeticException()",
		"\tif (left == $leftZero || right == 0uL) return 0uL",
		"\tval result = left.toU$signedType() * right",
		"\tif (result / left.toU$signedType() != right) throw ArithmeticException()",
		"\treturn result",
		"}"
	)
}

private fun multiplyExactULongWithULong() = listOf(
	"@Throws(ArithmeticException::class)",
	"fun multiplyExact(left: ULong, right: ULong): ULong {",
	"\tif (left == 0uL || right == 0uL) return 0uL",
	"\tval result = left * right",
	"\tif (result / left != right) throw ArithmeticException()",
	"\treturn result",
	"}"
)

private fun multiplyHigh() = listOf(
	"fun multiplyHigh(x: ULong, y: ULong): ULong {",
	"\t// Ripped from Math.multiplyHigh",
	"\tval x1 = x shr 32",
	"\tval y1 = y shr 32",
	"\tval x2 = x and 0xFFFFFFFFuL",
	"\tval y2 = y and 0xFFFFFFFFuL",
	"\tval a = x1 * y1",
	"\tval b = x2 * y2",
	"\tval c = (x1 + x2) * (y1 + y2)",
	"\tval k = c - a - b",
	"\treturn ((b shr 32) + k shr 32) + a",
	"}"
)

private fun toExact(type: IntType, otherType: String, minCondition: String) = listOf(
	"@Throws(ArithmeticException::class)",
	"fun to${type}Exact(value: $otherType): $type {",
	"\tif (${minCondition}value > $type.MAX_VALUE.to$otherType()) throw ArithmeticException(\"Can't convert \$value to $type\")",
	"\treturn value.to$type()",
	"}"
)

private fun toSignedExact(type: IntType, otherType: String) =
	toExact(type, otherType, "value < $type.MIN_VALUE.to$otherType() || ")

private fun uLongToBigInteger() = listOf(
	"fun uLongToBigInteger(value: ULong): BigInteger {",
	"\tval longValue = value.toLong()",
	"\treturn if (longValue >= 0) BigInteger.valueOf(longValue)",
	"\telse BigInteger.valueOf(longValue).add(-BigInteger.valueOf(Long.MIN_VALUE).multiply(BigInteger.TWO))",
	"}"
)

private fun bigIntegerToULong() = listOf(
	"@Throws(ArithmeticException::class)",
	"fun bigIntegerToULong(value: BigInteger, checkOverflow: Boolean): ULong {",
	"\tif (checkOverflow && (value.signum() == -1 || value >= BigInteger.TWO.pow(64))) throw ArithmeticException()",
	"\treturn value.toLong().toULong()",
	"}"
)

internal fun generateMathFile(
	numbers: List<NumberClass>, angles: List<AngleClass>, packageName: String,
	file: File, createPrintWriter: (File) -> PrintWriter
) {
	val functions = mutableSetOf<List<String>>()
	for (number in numbers) {
		if (number.checkOverflow) {
			if (!number.internalType.signed) {
				functions.add(addExactUnsigned(number.internalType))
				functions.add(subtractExactUnsigned(number.internalType))
				if (number.internalType.numBytes <= 4) {
					functions.add(multiplyExactSmallUnsigned(IntType(false, 4), "Long"))
					functions.add(toExact(IntType(false, 4), "Long", "value < 0L || "))
					functions.add(
						multiplyExactSmallMixed(
							number.internalType.numBytes,
							if (number.internalType.numBytes == 4) "Long" else "Int"
						)
					)
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
		val writer = createPrintWriter(file)
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
