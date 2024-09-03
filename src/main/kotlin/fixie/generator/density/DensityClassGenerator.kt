package fixie.generator.density

import fixie.generator.quantity.HybridQuantityClassGenerator
import java.io.PrintWriter

class DensityClassGenerator(
	writer: PrintWriter,
	quantity: DensityClass,
	packageName: String
) : HybridQuantityClassGenerator<DensityClass>(writer, quantity, packageName) {

	override fun generateToDoubleComment() {
		writer.println("\t/** Gets the density value, in kg/l */")
	}

	override fun generateToString() {
		writer.println()
		val valueString = if (quantity.number == null) "value" else "value.toDouble()"
		writer.println("\toverride fun toString() = String.format(\"%.1f%s\", $valueString, \"kg/l\")")
	}

	override fun generateArithmetic() {
		super.generateArithmetic()

		if (quantity.volumeClassName != null && quantity.massClassName != null) {
			writer.println()
			writer.println("\toperator fun times(right: ${quantity.volumeClassName}) = ${quantity.massClassName}.KILOGRAM * toDouble() * right.toDouble(VolumeUnit.LITER)")
		}
	}

	override fun generateCompanionContent() {
		super.generateCompanionContent()

		if (quantity.floatType != null) {
			val suffix = if (quantity.floatType.numBytes == 4) "f" else ".0"
			writer.println("\t\tval KGPL = ${quantity.className}(1$suffix)")
		} else {
			writer.println("\t\tval KGPL = ${quantity.className}(${quantity.number!!.className}.ONE)")
		}
	}

	override fun generateNumberUnitExtensionFunctions(typeName: String) {
		writer.println()
		writer.println("val $typeName.kgpl")
		writer.println("\tget() = ${quantity.className}.KGPL * this")
	}
}
