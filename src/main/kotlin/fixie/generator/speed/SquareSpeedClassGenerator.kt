package fixie.generator.speed

import fixie.generator.quantity.FloatQuantityClassGenerator
import java.io.PrintWriter

class SquareSpeedClassGenerator(
	writer: PrintWriter,
	quantity: SquareSpeedClass,
	packageName: String
) : FloatQuantityClassGenerator<SquareSpeedClass>(writer, quantity, packageName) {

	private fun shouldGenerateSqrt() = quantity.speed != null &&
			quantity.speed!!.computeSupportedUnits().find { it.first == SpeedUnit.METERS_PER_SECOND } != null

	override fun getImports() =
		super.getImports() + if (shouldGenerateSqrt()) arrayOf("kotlin.math.sqrt") else emptyArray()

	override fun generateToDoubleComment() {
		writer.println("\t/** Gets the square speed value, in (m/s)^2 */")
	}

	override fun generateToString() {
		writer.println()
		writer.println("\toverride fun toString() = String.format(\"%.2f%s\", value, \"(m/s)^2\")")
	}

	override fun generateArithmetic() {
		super.generateArithmetic()

		if (shouldGenerateSqrt()) {
			writer.println()
			writer.println("\toperator fun div(right: ${quantity.speedClassName}) = " +
					"${quantity.speedClassName}.METERS_PER_SECOND * toDouble() / right.toDouble(SpeedUnit.METERS_PER_SECOND)")
		}
	}

	override fun generateMathFunctions() {
		super.generateMathFunctions()

		if (shouldGenerateSqrt()) {
			writer.println()
			writer.println("fun sqrt(x: ${quantity.className}) = ${quantity.speedClassName}.METERS_PER_SECOND * sqrt(x.value)")
		}
	}
}
