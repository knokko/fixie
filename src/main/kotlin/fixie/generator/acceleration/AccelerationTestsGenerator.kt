package fixie.generator.acceleration

import fixie.generator.quantity.FloatQuantityTestsGenerator
import java.io.PrintWriter

internal class AccelerationTestsGenerator(
	writer: PrintWriter,
	acceleration: AccelerationClass,
	packageName: String
) : FloatQuantityTestsGenerator<AccelerationClass>(writer, acceleration, packageName) {

	override fun getImports() = super.getImports() + arrayOf("kotlin.time.Duration.Companion.seconds")

	override fun generateToStringBody() {
		writer.println("\t\tassertEquals(\"2.34m/s^2\", (${quantity.className}.MPS2 * 2.34).toString())")
	}

	override fun generateArithmeticBody() {
		super.generateArithmeticBody()

		if (AccelerationClassGenerator(writer, quantity, packageName).shouldGenerateTimesDuration()) {
			writer.println("\t\tassertEquals(0.8, ((${quantity.className}.MPS2 * 0.2) * 4.seconds).toDouble(SpeedUnit.METERS_PER_SECOND), 0.01)")
		}
	}

	override fun generateExtensionFunctionsBody() {
		super.generateExtensionFunctionsBody()

		if (AccelerationClassGenerator(writer, quantity, packageName).shouldGenerateTimesDuration()) {
			writer.println("\t\tassertEquals(0.8, (4.seconds * (${quantity.className}.MPS2 * 0.2)).toDouble(SpeedUnit.METERS_PER_SECOND), 0.01)")
		}
	}
}
