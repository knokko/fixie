package fixie.generator.quantity

import java.io.PrintWriter

abstract class HybridQuantityTestsGenerator<T : HybridQuantityClass>(
    writer: PrintWriter,
    quantity: T,
    packageName: String
) : QuantityTestsGenerator<T>(writer, quantity, packageName) {

    override fun canBeNegative() = quantity.number == null || quantity.number.internalType.signed

    protected abstract fun oneUnitName(): String

    override fun generateNearlyEquals() {
        if (quantity.number != null) generateFixedNearlyEquals(quantity.number, oneUnitName())
        else generateFloatNearlyEquals(quantity.floatType!!)
    }

    override fun generateCompareToBody() {
        super.generateCompareToBody()
        if (quantity.number != null) generateExtraFixedCompareToChecks(quantity.number, oneUnitName())
    }

    override fun generateArithmeticBody() {
        super.generateArithmeticBody()
        if (quantity.number != null) generateExtraFixedArithmeticChecks(quantity.number)
    }
}
