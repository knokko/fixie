package fixie.generator.density

import fixie.generator.quantity.HybridQuantityClassGenerator
import java.io.PrintWriter

class DensityClassGenerator(
		writer: PrintWriter,
		quantity: DensityClass,
		packageName: String
) : HybridQuantityClassGenerator<DensityClass>(writer, quantity, packageName) {

	override fun generateToDouble() {
		writer.println()
		writer.println("\t/** Gets the density value, in kg/l */")
		val conversion = if (quantity.floatType?.numBytes == 8) "" else ".toDouble()"
		writer.println("\tfun toDouble() = value$conversion")
	}

	override fun generateToString() {
		writer.println()
		writer.println("\toverride fun toString() = String.format(\"%.1f%s\", value, \"kg/l\")")
	}

	override fun generateArithmetic() {
		super.generateArithmetic()

		if (quantity.volumeClassName != null) {
			// TODO Multiply with volume to get mass
		}
	}

	override fun generateCompanionContent() {
		if (quantity.floatType != null) {
			val suffix = if (quantity.floatType.numBytes == 4) "f" else ".0"
			writer.println("\t\tval MPS2 = ${quantity.className}(1$suffix)")
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
