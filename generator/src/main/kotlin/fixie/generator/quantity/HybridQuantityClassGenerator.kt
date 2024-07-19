package fixie.generator.quantity

import fixie.generator.number.FloatType
import java.io.PrintWriter

abstract class HybridQuantityClassGenerator<T : HybridQuantityClass>(
    writer: PrintWriter,
    quantity: T,
    packageName: String
) : QuantityClassGenerator<T>(writer, quantity, packageName) {

    override fun getInternalTypeName() = quantity.number?.className ?: quantity.floatType!!.typeName

    override fun getImports() = if (quantity.floatType != null) {
        arrayOf("kotlin.math.abs", "kotlin.math.min", "kotlin.math.max")
    } else emptyArray<String>()

    private fun getMultiplicationTypes() = arrayOf("Int", "Long", "Float", "Double") + if (quantity.number != null)
        arrayOf(quantity.number.className) else emptyArray()

    override fun generateArithmetic() {
        fun next() {
            writer.println()
            if (quantity.number?.checkOverflow == true) writer.println("\t@Throws(FixedPointException::class)")
        }

        writer.println()
        writer.println("\toverride operator fun compareTo(other: ${quantity.className}) = this.value.compareTo(other.value)")

        if (quantity.number?.internalType?.signed != false) {
            next()
            writer.println("\toperator fun unaryMinus() = ${quantity.className}(-value)")
        }

        next()
        writer.println("\toperator fun plus(right: ${quantity.className}) = ${quantity.className}(this.value + right.value)")
        next()
        writer.println("\toperator fun minus(right: ${quantity.className}) = ${quantity.className}(this.value - right.value)")

        for (typeName in getMultiplicationTypes()) {
            val rightSuffix = if (typeName == "Double" && quantity.floatType == FloatType.SinglePrecision) ".toFloat()" else ""
            next()
            writer.println("\toperator fun times(right: $typeName) = ${quantity.className}(this.value * right$rightSuffix)")
            next()
            writer.println("\toperator fun div(right: $typeName) = ${quantity.className}(this.value / right$rightSuffix)")
        }

        next()
        writer.println("\toperator fun div(right: ${quantity.className}) = this.value / right.value")
    }

    override fun generateCompanionContent() {
        if (quantity.number != null) {
            writer.println("\t\tfun raw(value: ${quantity.number.internalType}) = ${quantity.className}(${quantity.number.className}.raw(value))")
        } else {
            writer.println("\t\tfun raw(value: ${quantity.floatType!!.typeName}) = ${quantity.className}(value)")
        }
        writer.println()
    }

    protected abstract fun generateNumberUnitExtensionFunctions(typeName: String)

    override fun generateExtensionFunctions() {
        for (typeName in getMultiplicationTypes()) {
            writer.println()
            if (quantity.number?.checkOverflow == true) writer.println("@Throws(FixedPointException::class)")
            writer.println("operator fun $typeName.times(right: ${quantity.className}) = right * this")

            if (quantity.createNumberExtensions) generateNumberUnitExtensionFunctions(typeName)
        }
    }

    override fun generateMathFunctions() {
        if (quantity.number?.internalType?.signed != false) {
            writer.println()
            if (quantity.number?.checkOverflow == true) writer.println("@Throws(FixedPointException::class)")
            writer.println("fun abs(x: ${quantity.className}) = ${quantity.className}(abs(x.value))")
        }

        writer.println()
        writer.println("fun min(a: ${quantity.className}, b: ${quantity.className}) = ${quantity.className}(min(a.value, b.value))")
        writer.println()
        writer.println("fun max(a: ${quantity.className}, b: ${quantity.className}) = ${quantity.className}(max(a.value, b.value))")
    }
}
