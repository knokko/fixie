package fixie.generator.momentum

import fixie.generator.quantity.FloatQuantityTestsGenerator
import java.io.PrintWriter

class SquareMomentumTestsGenerator(
	writer: PrintWriter,
	quantity: SquareMomentumClass,
	packageName: String
) : FloatQuantityTestsGenerator<SquareMomentumClass>(writer, quantity, packageName) {

	override fun generateToStringBody() {
		writer.println("\t\tassertEquals(\"2.34(Ns)^2\", (${quantity.className}.SQUARE_NEWTON_SECOND * 2.34).toString())")
	}

	override fun generateArithmeticBody() {
		super.generateArithmeticBody()

		if (quantity.momentum != null) {
			writer.println("\t\tassertEquals(2.5, ((10 * ${quantity.className}.SQUARE_NEWTON_SECOND) / " +
					"(4 * ${quantity.momentumClassName}.NEWTON_SECOND)).toDouble(), 0.001)")
		}
	}

	override fun generateMathFunctionsBody() {
		super.generateMathFunctionsBody()

		if (quantity.momentum != null) {
			writer.println("\t\tassertEquals(3.0, sqrt(9 * ${quantity.className}.SQUARE_NEWTON_SECOND).toDouble(), 0.001)")
		}
	}
}
