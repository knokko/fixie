package fixie.generator.acceleration

import fixie.generator.quantity.FloatQuantityClassGenerator
import fixie.generator.speed.SpeedUnit
import java.io.PrintWriter

internal class AccelerationClassGenerator(
	writer: PrintWriter,
	acceleration: AccelerationClass,
	packageName: String
) : FloatQuantityClassGenerator<AccelerationClass>(writer, acceleration, packageName) {

	override fun getImports() = super.getImports() + arrayOf("kotlin.time.Duration", "kotlin.time.DurationUnit")

	override fun generateToDoubleComment() {
		writer.println("\t/** Gets the acceleration value, in m/s^2 */")
	}

	override fun generateToString() {
		writer.println()
		writer.println("\toverride fun toString() = String.format(\"%.2f%s\", value, \"m/s^2\")")
	}

	fun shouldGenerateTimesDuration(): Boolean {
		quantity.speed?.let { speed ->
			return speed.computeSupportedUnits().find { it.first == SpeedUnit.METERS_PER_SECOND } != null
		}
		return false
	}

	override fun generateArithmetic() {
		super.generateArithmetic()

		if (shouldGenerateTimesDuration()) {
			writer.println()
			writer.println("\toperator fun times(right: Duration) = value * ${quantity.speedClassName}.METERS_PER_SECOND * right.toDouble(DurationUnit.SECONDS)")
		}
	}

	override fun generateExtensionFunctions() {
		super.generateExtensionFunctions()

		if (shouldGenerateTimesDuration()) {
			writer.println()
			writer.println("operator fun Duration.times(right: ${quantity.className}) = right * this")
		}
	}
}
