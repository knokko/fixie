package fixie.generator.momentum

import fixie.generator.quantity.FloatQuantityClassGenerator
import fixie.generator.speed.SpeedUnit
import java.io.PrintWriter

class MomentumClassGenerator(
	writer: PrintWriter,
	quantity: MomentumClass,
	packageName: String
) : FloatQuantityClassGenerator<MomentumClass>(writer, quantity, packageName) {

	override fun generateToDoubleComment() {
		writer.println("\t/** Gets the momentum value, in Ns */")
	}

	override fun generateToString() {
		writer.println()
		writer.println("\toverride fun toString() = String.format(\"%.2f%s\", value, \"Ns\")")
	}

	fun shouldGenerateMassDivision(): Boolean {
		quantity.speed?.let { speed ->
			return quantity.mass != null && speed.computeSupportedUnits().find { it.first == SpeedUnit.METERS_PER_SECOND } != null
		}
		return false
	}

	override fun generateArithmetic() {
		super.generateArithmetic()

		if (shouldGenerateMassDivision()) {
			writer.println()
			writer.println("\toperator fun div(right: ${quantity.speedClassName}) = ${quantity.massClassName}.KILOGRAM * " +
					"(toDouble() / right.toDouble(SpeedUnit.METERS_PER_SECOND))")

			writer.println()
			writer.println("\toperator fun div(right: ${quantity.massClassName}) = " +
					"${quantity.speedClassName}.METERS_PER_SECOND * (toDouble() / right.toDouble(MassUnit.KILOGRAM))")
		}
	}

	override fun generateCompanionContent() {
		val suffix = if (quantity.floatType.numBytes == 4) "f" else ".0"
		writer.println("\t\tval NEWTON_SECOND = ${quantity.className}(1$suffix)")
	}

	override fun generateNumberUnitExtensionFunctions(typeName: String) {
		writer.println()
		writer.println("val $typeName.newSec")
		writer.println("\tget() = ${quantity.className}.NEWTON_SECOND * this")
	}
}
