package fixie.generator.volume

import fixie.generator.displacement.DistanceUnit
import fixie.generator.quantity.FloatQuantityTestsGenerator
import java.io.PrintWriter
import java.math.BigInteger

class VolumeTestsGenerator(
		writer: PrintWriter,
		quantity: VolumeClass,
		packageName: String
) : FloatQuantityTestsGenerator<VolumeClass>(writer, quantity, packageName) {

	override fun generateToDoubleBody() {
		super.generateToDoubleBody()
		writer.println("\t\tassertEquals(25000.0, (25 * ${quantity.className}.CUBIC_METER).toDouble(VolumeUnit.LITER), 0.1)")
		writer.println("\t\tassertEquals(1.0, (1000 * ${quantity.className}.LITER).toDouble(VolumeUnit.CUBIC_METER), 0.001)")
	}

	override fun generateToStringBody() {
		writer.println("\t\tassertEquals(\"2.345m^3\", (${quantity.className}.CUBIC_METER * 2.345).toString(VolumeUnit.CUBIC_METER))")
		writer.println("\t\tassertEquals(\"5.678l\", (5.678 * ${quantity.className}.LITER).toString(VolumeUnit.LITER))")
		writer.println("\t\tassertEquals(\"0.123${quantity.displayUnit.abbreviation}\", (0.1234 * ${quantity.className}.${quantity.displayUnit.name}).toString())")
	}

	override fun generateCompareToBody() {
		super.generateCompareToBody()
		writer.println("\t\tassertTrue(${quantity.className}.CUBIC_METER > ${quantity.className}.LITER * 200)")
		writer.println("\t\tassertTrue(${quantity.className}.LITER < ${quantity.className}.CUBIC_METER / 500)")
		writer.println("\t\tassertTrue(${quantity.className}.LITER > -${quantity.className}.CUBIC_METER)")
	}

	override fun generateArithmeticBody() {
		super.generateArithmeticBody()
		writer.println("\t\tassertEquals(${quantity.className}.CUBIC_METER, 1000 * ${quantity.className}.LITER)")
		writer.println("\t\tassertNotEquals(${quantity.className}.CUBIC_METER, ${quantity.className}.LITER)")

		quantity.displacement?.let { displacement ->

			val meterPair = displacement.computeSupportedUnits().find { it.first == DistanceUnit.METER }
			if (quantity.areaClassName != null && meterPair != null && meterPair.second > BigInteger.valueOf(15)) {
				writer.println("\t\tassertEquals(0.5, ((5 * ${quantity.className}.CUBIC_METER) / (10 * ${quantity.areaClassName}.SQUARE_METER)).toDouble(DistanceUnit.METER), 0.1)")
				writer.println("\t\tassertEquals(6.0, ((3 * ${quantity.className}.CUBIC_METER) / (0.5 * ${quantity.displacementClassName}.METER)).toDouble(AreaUnit.SQUARE_METER), 1.0)")
			}
		}

		quantity.density?.let { density ->
			val canRepresent10 = density.number == null || density.number.internalType.getMaxValue() / density.number.oneValue > BigInteger.TEN
			if (quantity.mass != null && canRepresent10) {
				writer.println("\t\tassertEquals(17.0, (2 * ${quantity.className}.CUBIC_METER * (8.5 * ${density.className}.KGPL)).toDouble(MassUnit.TON))")
			}
		}
	}

	override fun generateMathFunctionsBody() {
		super.generateMathFunctionsBody()

		writer.println("\t\tassertEquals(${quantity.className}.CUBIC_METER, max(800 * ${quantity.className}.LITER, ${quantity.className}.CUBIC_METER))")
	}
}
