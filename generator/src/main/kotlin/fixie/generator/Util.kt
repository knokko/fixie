package fixie.generator

import java.math.BigDecimal
import java.math.BigInteger

fun roundBigDecimal(value: BigDecimal): BigInteger {
    if (value > BigDecimal.ZERO) return (value + BigDecimal.valueOf(0.5)).toBigInteger()
    if (value < BigDecimal.ZERO) return (value - BigDecimal.valueOf(0.5)).toBigInteger()
    return BigInteger.ZERO
}
