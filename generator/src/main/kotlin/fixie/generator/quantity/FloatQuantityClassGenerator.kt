package fixie.generator.quantity

import fixie.generator.number.FloatType
import java.io.PrintWriter

abstract class FloatQuantityClassGenerator<T : FloatQuantityClass>(
        writer: PrintWriter,
        quantity: T,
        packageName: String
) : QuantityClassGenerator<T>(writer, quantity, packageName) {

    override fun getInternalTypeName() = quantity.floatType.typeName

    override fun getImports() = arrayOf("kotlin.math.min", "kotlin.math.max", "kotlin.math.abs")

    override fun generateArithmetic() {
        writer.println()
        writer.println("\toverride operator fun compareTo(other: ${quantity.className}) = this.value.compareTo(other.value)")

        writer.println()
        writer.println("\toperator fun unaryMinus() = ${quantity.className}(-value)")

        writer.println()
        writer.println("\toperator fun plus(right: ${quantity.className}) = ${quantity.className}(this.value + right.value)")
        writer.println()
        writer.println("\toperator fun minus(right: ${quantity.className}) = ${quantity.className}(this.value - right.value)")
        writer.println()
        writer.println("\toperator fun div(right: ${quantity.className}) = this.value / right.value")

        for (typeName in arrayOf("Int", "Long", "Float", "Double")) {
            val rightSuffix = if (typeName == "Double" && quantity.floatType == FloatType.SinglePrecision) ".toFloat()" else ""
            writer.println()
            writer.println("\toperator fun times(right: $typeName) = ${quantity.className}(this.value * right$rightSuffix)")
            writer.println()
            writer.println("\toperator fun div(right: $typeName) = ${quantity.className}(this.value / right$rightSuffix)")
        }
    }

    protected abstract fun generateNumberUnitExtensionFunctions(typeName: String)

    override fun generateExtensionFunctions() {
        for (typeName in arrayOf("Int", "Long", "Float", "Double")) {
            writer.println()
            writer.println("operator fun $typeName.times(right: ${quantity.className}) = right * this")

            if (quantity.createNumberExtensions) generateNumberUnitExtensionFunctions(typeName)
        }
    }

    override fun generateMathFunctions() {
        writer.println()
        writer.println("fun abs(x: ${quantity.className}) = ${quantity.className}(abs(x.value))")
        writer.println()
        writer.println("fun min(a: ${quantity.className}, b: ${quantity.className}) = ${quantity.className}(min(a.value, b.value))")
        writer.println()
        writer.println("fun max(a: ${quantity.className}, b: ${quantity.className}) = ${quantity.className}(max(a.value, b.value))")
    }
}
