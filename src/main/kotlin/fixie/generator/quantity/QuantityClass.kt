package fixie.generator.quantity

import fixie.generator.number.NumberClass
import java.math.BigInteger
import kotlin.math.max
import kotlin.math.min

abstract class QuantityClass(
	val className: String,
	val createNumberExtensions: Boolean
) {

	abstract fun getSupportedUnits(): List<QuantityUnit>

	abstract fun getNumberOfUnits(): Int

	protected fun determineFixedUnitMinDelta(
		rawValue: BigInteger, number: NumberClass
	) = max(1.0 / min(rawValue.toDouble(), number.oneValue.toDouble()), 1e-8)

	protected fun determineFixedUnitMaxAmount(
		rawValue: BigInteger, number: NumberClass
	) = (number.internalType.getMaxValue().toBigDecimal(10) / rawValue.toBigDecimal(10)).toDouble()
}
