package fixie.generator.momentum

import fixie.generator.quantity.FloatQuantityTestsGenerator
import java.io.PrintWriter
import java.math.BigInteger
import kotlin.math.max

class MomentumTestsGenerator(
	writer: PrintWriter,
	quantity: MomentumClass,
	packageName: String
) : FloatQuantityTestsGenerator<MomentumClass>(writer, quantity, packageName) {

	override fun generateToStringBody() {
		writer.println("\t\tassertEquals(\"2.34Ns\", (${quantity.className}.NEWTON_SECOND * 2.34).toString())")
	}

	override fun generateArithmeticBody() {
		super.generateArithmeticBody()

		if (MomentumClassGenerator(writer, quantity, packageName).shouldGenerateMassDivision()) {
			var margin = 0.001
			if (quantity.speed!!.number != null) {
				margin = max(margin, 1.0 / quantity.speed!!.number!!.oneValue.toDouble())
				if (quantity.speed!!.number!!.oneValue > BigInteger.TWO.pow(50)) margin = 0.5
			}
			writer.println("\t\tassertEquals(0.8, ((${quantity.className}.NEWTON_SECOND * 1.6) / (2 * " +
					"${quantity.massClassName}.KILOGRAM)).toDouble(SpeedUnit.METERS_PER_SECOND), $margin)")
			writer.println("\t\tassertEquals(1.5, (0.75 * ${quantity.className}.NEWTON_SECOND / (0.5 * " +
					"${quantity.speedClassName}.METERS_PER_SECOND)).toDouble(MassUnit.KILOGRAM), $margin)")
		}

		if (quantity.square != null) {
			writer.println("\t\tassertEquals(2.4, ((${quantity.className}.NEWTON_SECOND * 1.2) * " +
					"(${quantity.className}.NEWTON_SECOND * 2)).toDouble(), 0.001)")
		}
	}
}
