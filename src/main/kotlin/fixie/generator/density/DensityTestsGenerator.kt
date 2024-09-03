package fixie.generator.density

import fixie.generator.quantity.HybridQuantityTestsGenerator
import java.io.PrintWriter
import java.math.BigInteger
import kotlin.math.max

class DensityTestsGenerator(
		writer: PrintWriter,
		quantity: DensityClass,
		packageName: String
) : HybridQuantityTestsGenerator<DensityClass>(writer, quantity, packageName) {

	override fun generateToStringBody() {
		if (quantity.number == null || (quantity.number.oneValue > BigInteger.TEN &&
					quantity.number.oneValue < quantity.number.internalType.getMaxValue() / BigInteger.valueOf(3))) {
			writer.println("\t\tassertEquals(\"2.3kg/l\", (${quantity.className}.KGPL * 2.3).toString())")
		} else {
			writer.println("\t\tval string = (${quantity.className}.KGPL / 2).toString()")
			writer.println("\t\tassertTrue(string.startsWith(\"0.\"))")
			writer.println("\t\tassertTrue(string.endsWith(\"kg/l\"))")
			writer.println("\t\tassertFalse(string.startsWith(\"0.0\"))")
		}
	}

	override fun oneUnitName() = "KGPL"

	override fun generateArithmeticBody() {
		super.generateArithmeticBody()

		if (quantity.volumeClassName != null && quantity.massClassName != null) {
			val margin = if (quantity.number == null) 0.001 else max(0.001, 5.0 / quantity.number.oneValue.toDouble())
			writer.println("\t\tassertEquals(2.5, (0.5 * ${quantity.className}.KGPL * (5 * ${quantity.volumeClassName}.LITER)).toDouble(MassUnit.KILOGRAM), $margin)")
		}
	}
}
