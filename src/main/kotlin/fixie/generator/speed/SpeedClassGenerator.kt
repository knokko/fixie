package fixie.generator.speed

import fixie.generator.displacement.DistanceUnit
import fixie.generator.number.FloatType
import fixie.generator.quantity.HybridQuantityClassGenerator
import java.io.PrintWriter

internal class SpeedClassGenerator(
	writer: PrintWriter,
	speed: SpeedClass,
	packageName: String
) : HybridQuantityClassGenerator<SpeedClass>(writer, speed, packageName) {

	override fun getImports() = super.getImports() + if (generateTimesDuration() || quantity.acceleration != null) {
		arrayOf("kotlin.time.Duration", "kotlin.time.DurationUnit")
	} else emptyArray()

	override fun generateToString() {
		writer.println()
		writer.println("\toverride fun toString() = toString(SpeedUnit.${quantity.displayUnit})")
		writer.println()
		writer.println("\tfun toString(unit: SpeedUnit): String {")
		writer.println("\t\treturn String.format(\"%.4f%s\", toDouble(unit), unit.abbreviation)")
		writer.println("\t}")
	}

	private fun generateTimesDuration(): Boolean {
		quantity.displacement?.let { displacement ->
			return displacement.computeSupportedUnits().find { it.first == DistanceUnit.METER } != null
		}
		return false
	}

	override fun generateArithmetic() {
		super.generateArithmetic()

		if (generateTimesDuration()) {
			writer.println()
			if (quantity.displacement!!.number.checkOverflow) writer.println("\t@Throws(FixedPointException::class)")
			writer.println(
				"\toperator fun times(right: Duration) = toDouble(SpeedUnit.METERS_PER_SECOND) * " +
						"${quantity.displacementClassName}.METER * right.toDouble(DurationUnit.SECONDS)"
			)

			if (quantity.spin != null) {
				writer.println()
				writer.println("\tfun toSpin(radius: ${quantity.displacementClassName}) = " +
						"${quantity.spinClassName}.RADIANS_PER_SECOND * toDouble(SpeedUnit.METERS_PER_SECOND) / " +
						"radius.toDouble(DistanceUnit.METER)")
			}
		}

		quantity.acceleration?.let { acceleration ->
			writer.println()
			writer.println(
				"\toperator fun div(right: Duration) = toDouble(SpeedUnit.METERS_PER_SECOND) * " +
						"${acceleration.className}.MPS2 / right.toDouble(DurationUnit.SECONDS)"
			)
		}

		if (quantity.mass != null && quantity.momentum != null) {
			writer.println()
			writer.println("\toperator fun times(right: ${quantity.massClassName}) = " +
					"${quantity.momentumClassName}.NEWTON_SECOND * toDouble(SpeedUnit.METERS_PER_SECOND) * right.toDouble(MassUnit.KILOGRAM)")
		}

		if (quantity.square != null) {
			writer.println()
			writer.println("\toperator fun times(right: ${quantity.className}) = ${quantity.squareClassName}.SQUARE_METERS_PER_SECOND " +
					"* toDouble(SpeedUnit.METERS_PER_SECOND) * right.toDouble(SpeedUnit.METERS_PER_SECOND)")
		}
	}

	override fun generateCompanionContent() {
		super.generateCompanionContent()

		if (quantity.number == null) {
			generateFloatCompanionContent(quantity.floatType!!)
			return
		}

		for ((unit, rawValue) in quantity.computeSupportedUnits()) {
			writer.println()
			writer.println("\t\tval $unit = raw($rawValue${if (quantity.number.internalType.signed) "" else "u"})")
		}
	}

	override fun generateExtensionFunctions() {
		super.generateExtensionFunctions()

		if (generateTimesDuration()) {
			writer.println()
			if (quantity.displacement!!.number.checkOverflow) writer.println("@Throws(FixedPointException::class)")
			writer.println("operator fun Duration.times(right: ${quantity.className}) = right * this")
		}
	}
}
