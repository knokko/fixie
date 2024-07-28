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
            val precision = 100

            val rawValue = roundBigDecimal(number.oneValue.toBigDecimal().setScale(precision) / divisor)
            val revertedValue = roundBigDecimal(rawValue.toBigDecimal().setScale(precision) * divisor)
            val lostPrecision = ((number.oneValue - revertedValue).toBigDecimal().setScale(precision) / number.oneValue.toBigDecimal().setScale(precision)).toDouble()
            return if (number.internalType.canRepresent(rawValue) && abs(lostPrecision) < 0.05) rawValue
            else null
        }
    }
}
