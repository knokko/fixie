package fixie.generator.mass

import fixie.generator.quantity.FloatQuantityClassGenerator
import java.io.PrintWriter

class MassClassGenerator(
	writer: PrintWriter,
	mass: MassClass,
	packageName: String
) : FloatQuantityClassGenerator<MassClass>(writer, mass, packageName) {

	override fun generateToString() {
		writer.println()
		writer.println("\tfun toString(unit: MassUnit) = String.format(\"%.3f%s\", toDouble(unit), unit.abbreviation)")
		writer.println()
		writer.println("\toverride fun toString() = toString(MassUnit.${quantity.displayUnit})")
	}

	override fun generateArithmetic() {
		super.generateArithmetic()

		if (quantity.density != null && quantity.volume != null) {
			writer.println()
			writer.println(
				"\toperator fun div(right: ${quantity.densityClassName}) = ${quantity.volumeClassName}.LITER *" +
						" value / right.toDouble()"
			)
			writer.println()
			writer.println("\toperator fun div(right: ${quantity.volumeClassName}) = ${quantity.densityClassName}.KGPL * value / right.toDouble(VolumeUnit.LITER)")
		}
	}

	override fun generateCompanionContent() {
		val suffix = if (quantity.floatType.numBytes == 4) "f" else ""
		for (unit in MassUnit.entries) {
			writer.println("\t\tval ${unit.name} = ${quantity.className}(${unit.factor}$suffix)")
		}
	}

	override fun generateNumberUnitExtensionFunctions(typeName: String) {
		writer.println()
		for (unit in MassUnit.entries) {
			writer.println("val $typeName.${unit.abbreviation}")
			writer.println("\tget() = ${quantity.className}.${unit.name} * this")
		}
	}
}
