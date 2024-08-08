package fixie.generator.density

import fixie.generator.quantity.HybridQuantityTestsGenerator
import fixie.generator.quantity.QuantityUnit
import java.io.PrintWriter

class DensityTestsGenerator(
		writer: PrintWriter,
		quantity: DensityClass,
		packageName: String
) : HybridQuantityTestsGenerator<DensityClass>(writer, quantity, packageName) {

	override fun getUnits() = listOf(QuantityUnit(
			"KGPL", "", "kg/l", "kgpl", 0.0001, 1e6
	))

	override fun canSupportMultipleUnits() = false

	override fun generateToStringBody() {
		writer.println("\t\tassertEquals(\"2.34kg/l\", (${quantity.className}.KGPL * 2.34).toString())")
	}

	override fun oneUnitName() = "KGPL"

	override fun generateArithmeticBody() {
		super.generateArithmeticBody()

		if (quantity.volumeClassName != null) {
			// TODO Test multiply with volume to get mass
		}
	}
}
