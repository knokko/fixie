package fixie.generator.speed

import fixie.generator.quantity.FloatQuantityTestsGenerator
import java.io.PrintWriter
import java.math.BigInteger
import kotlin.math.max

class SquareSpeedTestsGenerator(
	writer: PrintWriter,
	quantity: SquareSpeedClass,
	packageName: String
) : FloatQuantityTestsGenerator<SquareSpeedClass>(writer, quantity, packageName) {

	override fun generateToStringBody() {
		writer.println("\t\tassertEquals(\"2.34(m/s)^2\", (${quantity.className}.SQUARE_METERS_PER_SECOND * 2.34).toString())")
	}

	private fun shouldTestSqrt(): Double? {
		quantity.speed?.let { speed ->
			if (speed.number == null) return 0.001
			val mpsPair = speed.computeSupportedUnits().find { it.first == SpeedUnit.METERS_PER_SECOND }
			if (mpsPair != null && mpsPair.second > BigInteger.valueOf(20)) {
				return max(0.001, 3.0 / mpsPair.second.toDouble())
			}
		}

		return null
	}

	override fun generateArithmeticBody() {
		super.generateArithmeticBody()

		val margin = shouldTestSqrt()
		if (margin != null) {
			writer.println("\t\tassertEquals(0.25, (0.1 * ${quantity.className}.SQUARE_METERS_PER_SECOND / " +
					"(0.4 * ${quantity.speedClassName}.METERS_PER_SECOND)).toDouble(SpeedUnit.METERS_PER_SECOND), $margin)")
		}
	}

	override fun generateMathFunctionsBody() {
		super.generateMathFunctionsBody()

		val margin = shouldTestSqrt()
		if (margin != null) {
			writer.println("\t\tassertEquals(0.5, sqrt(0.25 * ${quantity.className}.SQUARE_METERS_PER_SECOND)" +
					".toDouble(SpeedUnit.METERS_PER_SECOND), $margin)")
		}
	}
}
