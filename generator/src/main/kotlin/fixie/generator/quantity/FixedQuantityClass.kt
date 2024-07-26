package fixie.generator.quantity

import fixie.generator.number.NumberClass
import fixie.generator.roundBigDecimal
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.abs

abstract class FixedQuantityClass(
        className: String,
        val number: NumberClass,
        createNumberExtensions: Boolean
) : QuantityClass(className, createNumberExtensions) {

    override fun toString() = "$className($number)"

    protected fun determineRawValue(divisor: BigDecimal) = determineRawValue(divisor, number)

    companion object {
        internal fun determineRawValue(divisor: BigDecimal, number: NumberClass): BigInteger? {
            val rawValue = roundBigDecimal(number.oneValue.toBigDecimal() / divisor)
            val revertedValue = roundBigDecimal(rawValue.toBigDecimal() * divisor)
            val lostPrecision = ((number.oneValue - revertedValue).toBigDecimal() / number.oneValue.toBigDecimal()).toDouble()
            return if (number.internalType.canRepresent(rawValue) && abs(lostPrecision) < 0.1) rawValue
            else null
        }
    }
}
