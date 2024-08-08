package fixie.generator.density

import fixie.generator.quantity.HybridQuantityTestsGenerator
import fixie.generator.quantity.QuantityUnit
import java.io.PrintWriter
import java.math.BigInteger

class DensityTestsGenerator(
		writer: PrintWriter,
		quantity: DensityClass,
		packageName: String
) : HybridQuantityTestsGenerator<DensityClass>(writer, quantity, packageName) {

	override fun getUnits() = listOf(QuantityUnit(
			"KGPL", "", "kg/l", "kgpl",
		if (quantity.number == null) 0.0001 else 1.0 / quantity.number.oneValue.toDouble(),
		if (quantity.number == null) 1e6 else quantity.number.internalType.getMaxValue().toDouble() / quantity.number.oneValue.toDouble()
	))

	override fun canSupportMultipleUnits() = false

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

		if (quantity.volumeClassName != null) {
			// TODO Test multiply with volume to get mass
		}
	}
}
