package fixie

import java.math.BigInteger

@Throws(ArithmeticException::class)
fun addExact(left: UByte, right: UByte) = if (UByte.MAX_VALUE - right >= left) (left + right).toUByte() else throw ArithmeticException()

@Throws(ArithmeticException::class)
fun subtractExact(left: UByte, right: UByte) = if (left >= right) (left - right).toUByte() else throw ArithmeticException()

fun min(a: UByte, b: UByte) = if (a <= b) a else b

fun max(a: UByte, b: UByte) = if (a >= b) a else b

@Throws(ArithmeticException::class)
fun addExact(left: UShort, right: UShort) = if (UShort.MAX_VALUE - right >= left) (left + right).toUShort() else throw ArithmeticException()

@Throws(ArithmeticException::class)
fun subtractExact(left: UShort, right: UShort) = if (left >= right) (left - right).toUShort() else throw ArithmeticException()

fun min(a: UShort, b: UShort) = if (a <= b) a else b

fun max(a: UShort, b: UShort) = if (a >= b) a else b

@Throws(ArithmeticException::class)
fun addExact(left: UInt, right: UInt) = if (UInt.MAX_VALUE - right >= left) left + right else throw ArithmeticException()

@Throws(ArithmeticException::class)
fun subtractExact(left: UInt, right: UInt) = if (left >= right) left - right else throw ArithmeticException()

@Throws(ArithmeticException::class)
fun addExact(left: ULong, right: ULong) = if (ULong.MAX_VALUE - right >= left) left + right else throw ArithmeticException()

@Throws(ArithmeticException::class)
fun subtractExact(left: ULong, right: ULong) = if (left >= right) left - right else throw ArithmeticException()

@Throws(ArithmeticException::class)
fun addExact(left: Byte, right: Byte): Byte {
    val intResult = left.toInt() + right.toInt()
    if (intResult < Byte.MIN_VALUE || intResult > Byte.MAX_VALUE) throw ArithmeticException()
    return intResult.toByte()
}

@Throws(ArithmeticException::class)
fun subtractExact(left: Byte, right: Byte): Byte {
    val intResult = left.toInt() - right.toInt()
    if (intResult < Byte.MIN_VALUE || intResult > Byte.MAX_VALUE) throw ArithmeticException()
    return intResult.toByte()
}

@Throws(ArithmeticException::class)
fun multiplyExact(left: Byte, right: Byte): Byte {
    val intResult = left.toInt() * right.toInt()
    if (intResult < Byte.MIN_VALUE || intResult > Byte.MAX_VALUE) throw ArithmeticException()
    return intResult.toByte()
}

fun min(a: Byte, b: Byte) = if (a <= b) a else b

fun max(a: Byte, b: Byte) = if (a >= b) a else b

fun abs(value: Byte) = if (value == Byte.MIN_VALUE) throw ArithmeticException() else if (value < 0) (-value).toByte() else value

@Throws(ArithmeticException::class)
fun addExact(left: Short, right: Short): Short {
    val intResult = left.toInt() + right.toInt()
    if (intResult < Short.MIN_VALUE || intResult > Short.MAX_VALUE) throw ArithmeticException()
    return intResult.toShort()
}

@Throws(ArithmeticException::class)
fun subtractExact(left: Short, right: Short): Short {
    val intResult = left.toInt() - right.toInt()
    if (intResult < Short.MIN_VALUE || intResult > Short.MAX_VALUE) throw ArithmeticException()
    return intResult.toShort()
}

@Throws(ArithmeticException::class)
fun multiplyExact(left: Short, right: Short): Short {
    val intResult = left.toInt() * right.toInt()
    if (intResult < Short.MIN_VALUE || intResult > Short.MAX_VALUE) throw ArithmeticException()
    return intResult.toShort()
}

fun min(a: Short, b: Short) = if (a <= b) a else b

fun max(a: Short, b: Short) = if (a >= b) a else b

fun abs(value: Short) = if (value == Short.MIN_VALUE) throw ArithmeticException() else if (value < 0) (-value).toShort() else value

@Throws(ArithmeticException::class)
fun multiplyExact(left: Byte, right: UByte) = toUByteExact(left.toInt() * right.toInt())

@Throws(ArithmeticException::class)
fun multiplyExact(left: UByte, right: UByte) = toUByteExact(left.toInt() * right.toInt())

@Throws(ArithmeticException::class)
fun multiplyExact(left: Short, right: UShort) = toUShortExact(left.toInt() * right.toInt())

@Throws(ArithmeticException::class)
fun multiplyExact(left: UShort, right: UShort) = toUShortExact(left.toInt() * right.toInt())

@Throws(ArithmeticException::class)
fun multiplyExact(left: Int, right: UInt) = toUIntExact(left.toLong() * right.toLong())

@Throws(ArithmeticException::class)
fun multiplyExact(left: UInt, right: UInt) = toUIntExact(left.toLong() * right.toLong())

@Throws(ArithmeticException::class)
fun multiplyExact(left: Int, right: ULong): ULong {
    if (left < 0) throw ArithmeticException()
    if (left == 0 || right == 0uL) return 0uL
    val result = left.toUInt() * right
    if (result / left.toUInt() != right) throw ArithmeticException()
    return result
}

@Throws(ArithmeticException::class)
fun multiplyExact(left: Long, right: ULong): ULong {
    if (left < 0) throw ArithmeticException()
    if (left == 0L || right == 0uL) return 0uL
    val result = left.toULong() * right
    if (result / left.toULong() != right) throw ArithmeticException()
    return result
}

@Throws(ArithmeticException::class)
fun multiplyExact(left: ULong, right: ULong): ULong {
    if (left == 0uL || right == 0uL) return 0uL
    val result = left * right
    if (result / left != right) throw ArithmeticException()
    return result
}

fun multiplyHigh(x: ULong, y: ULong): ULong {
    // Ripped from Math.multiplyHigh
    val x1 = x shr 32
    val y1 = y shr 32
    val x2 = x and 0xFFFFFFFFuL
    val y2 = y and 0xFFFFFFFFuL
    val a = x1 * y1
    val b = x2 * y2
    val c = (x1 + x2) * (y1 + y2)
    val k = c - a - b
    return ((b shr 32) + k shr 32) + a
}

@Throws(FixedPointException::class)
fun toByteExact(value: Int): Byte {
    if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) throw FixedPointException("Can't convert $value to Byte")
    return value.toByte()
}

@Throws(ArithmeticException::class)
fun toByteExact(value: Long): Byte {
    if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) throw FixedPointException("Can't convert $value to Byte")
    return value.toByte()
}

@Throws(ArithmeticException::class)
fun toShortExact(value: Int): Short {
    if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) throw FixedPointException("Can't convert $value to Short")
    return value.toShort()
}

@Throws(ArithmeticException::class)
fun toShortExact(value: Long): Short {
    if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) throw FixedPointException("Can't convert $value to Short")
    return value.toShort()
}

@Throws(ArithmeticException::class)
fun toUByteExact(value: Int): UByte {
    if (value < 0 || value > UByte.MAX_VALUE.toInt()) throw ArithmeticException()
    return value.toUByte()
}

@Throws(ArithmeticException::class)
fun toUByteExact(value: UInt): UByte{
    if (value > UByte.MAX_VALUE.toUInt()) throw ArithmeticException()
    return value.toUByte()
}

@Throws(ArithmeticException::class)
fun toUByteExact(value: ULong): UByte {
    if (value > UByte.MAX_VALUE.toULong()) throw ArithmeticException()
    return value.toUByte()
}

@Throws(ArithmeticException::class)
fun toUShortExact(value: Int): UShort {
    if (value < 0 || value > UShort.MAX_VALUE.toInt()) throw ArithmeticException()
    return value.toUShort()
}

@Throws(ArithmeticException::class)
fun toUShortExact(value: UInt): UShort {
    if (value > UShort.MAX_VALUE.toUInt()) throw ArithmeticException()
    return value.toUShort()
}

@Throws(ArithmeticException::class)
fun toUShortExact(value: ULong): UShort {
    if (value > UShort.MAX_VALUE.toULong()) throw ArithmeticException()
    return value.toUShort()
}

@Throws(ArithmeticException::class)
fun toUIntExact(value: Long): UInt {
    if (value < 0 || value > UInt.MAX_VALUE.toLong()) throw ArithmeticException()
    return value.toUInt()
}

@Throws(ArithmeticException::class)
fun toUIntExact(value: ULong): UInt {
    if (value > UInt.MAX_VALUE.toULong()) throw ArithmeticException()
    return value.toUInt()
}

fun uLongToBigInteger(value: ULong): BigInteger {
    val longValue = value.toLong()
    return if (longValue >= 0) BigInteger.valueOf(longValue)
    else BigInteger.valueOf(longValue).add(-BigInteger.valueOf(Long.MIN_VALUE).multiply(BigInteger.TWO))
}

@Throws(ArithmeticException::class)
fun bigIntegerToULong(value: BigInteger, checkOverflow: Boolean): ULong {
    if (checkOverflow && (value.signum() == -1 || value >= BigInteger.TWO.pow(64))) throw ArithmeticException()
    return value.toLong().toULong()
}
