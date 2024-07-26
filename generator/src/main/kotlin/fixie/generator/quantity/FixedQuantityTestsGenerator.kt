package fixie.generator.quantity

import java.io.PrintWriter

abstract class FixedQuantityTestsGenerator<T : FixedQuantityClass>(
    writer: PrintWriter,
    quantity: T,
    packageName: String
) : QuantityTestsGenerator<T>(writer, quantity, packageName) {

    override fun canBeNegative() = quantity.number.internalType.signed

    protected abstract fun oneUnitName(): String

    abstract fun getFixedUnits(): Collection<QuantityUnit>

    final override fun getUnits() = getFixedUnits().sortedBy { -it.maxAmount }

    override fun generateNearlyEquals() {
        generateFixedNearlyEquals(quantity.number, oneUnitName())
    }

    override fun generateCompareToBody() {
        super.generateCompareToBody()
        generateExtraFixedCompareToChecks(quantity.number, oneUnitName())
    }

    override fun generateArithmeticBody() {
        super.generateArithmeticBody()
        generateExtraFixedArithmeticChecks(quantity.number)
    }
}
