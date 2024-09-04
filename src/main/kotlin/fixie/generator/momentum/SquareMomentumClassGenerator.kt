package fixie.generator.momentum

import fixie.generator.quantity.FloatQuantityClassGenerator
import java.io.PrintWriter

class SquareMomentumClassGenerator(
	writer: PrintWriter,
	quantity: SquareMomentumClass,
	packageName: String
) : FloatQuantityClassGenerator<SquareMomentumClass>(writer, quantity, packageName) {

	override fun getImports() =
		super.getImports() + if (quantity.momentum != null) arrayOf("kotlin.math.sqrt") else emptyArray()

	override fun generateToDoubleComment() {
		writer.println("\t/** Gets the square momentum value, in (Ns)^2 */")
	}

	override fun generateNumberUnitExtensionFunctions(typeName: String) {
		// TODO Why is this not done automatically?
		writer.println()
		writer.println("val $typeName.squareNs")
		writer.println("\tget() = ${quantity.className}.SQUARE_NEWTON_SECOND * this")
	}

	override fun generateToString() {
		writer.println()
		writer.println("\toverride fun toString() = String.format(\"%.2f%s\", value, \"(Ns)^2\")")
	}

	override fun generateCompanionContent() {
		// TODO Maybe do this automatically
		val suffix = if (quantity.floatType.numBytes == 4) "f" else ".0"
		writer.println("\t\tval SQUARE_NEWTON_SECOND = ${quantity.className}(1$suffix)")
	}

	override fun generateArithmetic() {
		super.generateArithmetic()

		if (quantity.momentum != null) {
			writer.println()
			writer.println("\toperator fun div(right: ${quantity.momentumClassName}) = " +
					"${quantity.momentumClassName}.NEWTON_SECOND * toDouble() / right.toDouble()")
		}
	}

	override fun generateMathFunctions() {
		super.generateMathFunctions()

		if (quantity.momentum != null) {
			writer.println()
			writer.println("fun sqrt(x: ${quantity.className}) = ${quantity.momentumClassName}.NEWTON_SECOND * sqrt(x.value)")
		}
	}
}
