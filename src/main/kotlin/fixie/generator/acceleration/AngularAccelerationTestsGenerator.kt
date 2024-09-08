package fixie.generator.acceleration

import fixie.generator.quantity.FloatQuantityTestsGenerator
import java.io.PrintWriter

class AngularAccelerationTestsGenerator(
	writer: PrintWriter,
	quantity: AngularAccelerationClass,
	packageName: String
) : FloatQuantityTestsGenerator<AngularAccelerationClass>(writer, quantity, packageName) {

	override fun getImports() = super.getImports() + arrayOf("kotlin.time.Duration.Companion.seconds")

	override fun generateToStringBody() {
		writer.println("\t\tassertEquals(\"2.34rad/s^2\", (${quantity.className}.RADPS2 * 2.34).toString())")
	}

	override fun generateArithmeticBody() {
		super.generateArithmeticBody()

		if (quantity.spin != null) {
			writer.println("\t\tassertEquals(0.8, ((${quantity.className}.RADPS2 * 0.2) * 4.seconds).toDouble(SpinUnit.RADIANS_PER_SECOND), 0.01)")
		}
	}

	override fun generateExtensionFunctionsBody() {
		super.generateExtensionFunctionsBody()

		if (quantity.spin != null) {
			writer.println("\t\tassertEquals(0.8, (4.seconds * (${quantity.className}.RADPS2 * 0.2)).toDouble(SpinUnit.RADIANS_PER_SECOND), 0.01)")
		}
	}
}
