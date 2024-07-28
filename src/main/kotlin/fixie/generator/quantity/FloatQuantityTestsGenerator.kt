package fixie.generator.quantity

import java.io.PrintWriter

abstract class FloatQuantityTestsGenerator<T : FloatQuantityClass>(
        writer: PrintWriter,
        quantity: T,
        packageName: String
) : QuantityTestsGenerator<T>(writer, quantity, packageName) {

    override fun canBeNegative() = true

    override fun canSupportMultipleUnits() = getUnits().size > 1

    override fun generateNearlyEquals() {
        generateFloatNearlyEquals(quantity.floatType)
    }
}
