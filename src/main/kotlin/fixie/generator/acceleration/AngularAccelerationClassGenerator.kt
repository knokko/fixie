package fixie.generator.acceleration

import fixie.generator.quantity.FloatQuantityClassGenerator
import java.io.PrintWriter

class AngularAccelerationClassGenerator(
	writer: PrintWriter,
	quantity: AngularAccelerationClass,
	packageName: String
) : FloatQuantityClassGenerator<AngularAccelerationClass>(writer, quantity, packageName) {

	override fun getImports() = super.getImports() + arrayOf("kotlin.time.Duration", "kotlin.time.DurationUnit")

	override fun generateToDoubleComment() {
		writer.println("\t/** Gets the angular acceleration value, in rad/s^2 */")
	}

	override fun generateToString() {
		writer.println()
		writer.println("\toverride fun toString() = String.format(\"%.2f%s\", value, \"rad/s^2\")")
	}

	override fun generateArithmetic() {
		super.generateArithmetic()

		if (quantity.spin != null) {
			writer.println()
			writer.println("\toperator fun times(right: Duration) = value * ${quantity.spinClassName}.RADIANS_PER_SECOND * right.toDouble(DurationUnit.SECONDS)")
		}
	}

	override fun generateExtensionFunctions() {
		super.generateExtensionFunctions()

		if (quantity.spin != null) {
			writer.println()
			writer.println("operator fun Duration.times(right: ${quantity.className}) = right * this")
		}
	}
}
