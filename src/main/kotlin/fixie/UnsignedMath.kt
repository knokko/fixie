package fixie

import java.math.BigInteger

@Throws(ArithmeticException::class)
fun addExact(left: UInt, right: UInt) = if (UInt.MAX_VALUE - right >= left) left + right else throw ArithmeticException()

@Throws(ArithmeticException::class)
fun subtractExact(left: UInt, right: UInt) = if (left >= right) left - right else throw ArithmeticException()

@Throws(ArithmeticException::class)
fun addExact(left: ULong, right: ULong) = if (ULong.MAX_VALUE - right >= left) left + right else throw ArithmeticException()

@Throws(ArithmeticException::class)
fun subtractExact(left: ULong, right: ULong) = if (left >= right) left - right else throw ArithmeticException()

@Throws(ArithmeticException::class)
fun multiplyExact(left: Int, right: UInt) = toUIntExact(left.toLong() * right.toLong())

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
