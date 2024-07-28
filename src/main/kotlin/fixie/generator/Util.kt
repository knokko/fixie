package fixie.generator

import java.math.BigDecimal
import java.math.BigInteger

fun roundBigDecimal(value: BigDecimal): BigInteger {
    if (value > BigDecimal.ZERO) return (value + BigDecimal.valueOf(0.5)).toBigInteger()
    if (value < BigDecimal.ZERO) return (value - BigDecimal.valueOf(0.5)).toBigInteger()
    return BigInteger.ZERO
}

fun bigIntegerToULong(value: BigInteger): ULong {
    var result = 0uL
    var remaining = value
    while (remaining > BigInteger.valueOf(Long.MAX_VALUE)) {
        val oldValue = result
        result += Long.MAX_VALUE.toULong()
        if (result < oldValue) throw IllegalArgumentException("Can't convert $value to ULong")
        remaining -= BigInteger.valueOf(Long.MAX_VALUE)
    }

    val finalResult = result + remaining.longValueExact().toULong()
    if (finalResult < result) throw IllegalArgumentException("Can't convert $value to ULong")
    return finalResult
}

fun uLongToBigInteger(value: ULong): BigInteger {
    var remainingValue = value
    var result = BigInteger.ZERO
    while (remainingValue > Long.MAX_VALUE.toULong()) {
        result += BigInteger.valueOf(Long.MAX_VALUE)
        remainingValue -= Long.MAX_VALUE.toULong()
    }

    return result + BigInteger.valueOf(remainingValue.toLong())
}
