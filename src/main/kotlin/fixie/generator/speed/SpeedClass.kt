package fixie.generator.speed

import fixie.generator.acceleration.AccelerationClass
import fixie.generator.displacement.DisplacementClass
import fixie.generator.number.FloatType
import fixie.generator.number.NumberClass
import fixie.generator.quantity.FixedQuantityClass.Companion.determineRawValue
import fixie.generator.quantity.HybridQuantityClass
import fixie.generator.quantity.QuantityUnit
import java.math.BigDecimal
import java.math.BigInteger

class SpeedClass(
	className: String,
	number: NumberClass?,
	floatType: FloatType?,
	val oneUnit: SpeedUnit,
	val displayUnit: SpeedUnit,
	val displacementClassName: String?,
	val accelerationClassName: String?,
	createNumberExtensions: Boolean
) : HybridQuantityClass(className, number, floatType, createNumberExtensions) {

	var displacementClass: DisplacementClass? = null
	var accelerationClass: AccelerationClass? = null

	fun computeSupportedUnits(): List<Pair<SpeedUnit, BigInteger>> {
		if (number == null) return SpeedUnit.entries.map { Pair(it, BigInteger.ONE) }

		val supportedUnits = mutableListOf(Pair(oneUnit, number.oneValue))
		for (unit in SpeedUnit.entries.reversed()) {
			if (unit != this.oneUnit) {
				val divisor = unit.factor / this.oneUnit.factor
				val rawValue = determineRawValue(BigDecimal.valueOf(divisor), number)
				if (rawValue != null) supportedUnits.add(Pair(unit, rawValue))
			}
		}

		return supportedUnits
	}

	override fun getSupportedUnits(): List<QuantityUnit> {
		if (number == null) return SpeedUnit.entries.map {
			QuantityUnit(
				name = it.name,
				enumName = "SpeedUnit",
				suffix = it.abbreviation,
				extensionName = it.abbreviation.replace('/', 'p'),
				minDelta = 0.001,
				maxAmount = 1e6,
				relativeSize = oneUnit.factor / it.factor
			)
		}

		return computeSupportedUnits().map { (unit, rawValue) ->
			QuantityUnit(
				name = unit.name,
				enumName = "SpeedUnit",
				suffix = unit.abbreviation,
				extensionName = unit.abbreviation.replace('/', 'p'),
				minDelta = determineFixedUnitMinDelta(rawValue, number),
				maxAmount = determineFixedUnitMaxAmount(rawValue, number),
				relativeSize = oneUnit.factor / unit.factor
			)
		}.sortedBy { -it.maxAmount }
	}

	override fun getNumberOfUnits() = SpeedUnit.entries.size
}
