package fixie.generator.quantity

import java.io.PrintWriter

abstract class FixedQuantityClassGenerator<T : FixedQuantityClass>(
        writer: PrintWriter,
        quantity: T,
        packageName: String
) : QuantityClassGenerator<T>(writer, quantity, packageName) {

    override fun getInternalTypeName() = quantity.number.className

    override fun getImports() = emptyArray<String>()

    override fun generateArithmetic() {
        fun next() {
            writer.println()
            if (quantity.number.checkOverflow) writer.println("\t@Throws(FixedPointException::class)")
        }

        writer.println()
        writer.println("\toverride operator fun compareTo(other: ${quantity.className}) = this.value.compareTo(other.value)")

        if (quantity.number.internalType.signed) {
            next()
            writer.println("\toperator fun unaryMinus() = ${quantity.className}(-value)")
        }

        next()
        writer.println("\toperator fun plus(right: ${quantity.className}) = ${quantity.className}(this.value + right.value)")
        next()
        writer.println("\toperator fun minus(right: ${quantity.className}) = ${quantity.className}(this.value - right.value)")

        for (typeName in arrayOf("Int", "Long", quantity.number.className)) {
            next()
            writer.println("\toperator fun times(right: $typeName) = ${quantity.className}(this.value * right)")
            next()
            writer.println("\toperator fun div(right: $typeName) = ${quantity.className}(this.value / right)")
        }
        for (typeName in arrayOf("Float", "Double")) {
            next()
            writer.println("\toperator fun times(right: $typeName) = ${quantity.className}(${quantity.number.className}.from(this.value.toDouble() * right))")
            next()
            writer.println("\toperator fun div(right: $typeName) = ${quantity.className}(${quantity.number.className}.from(this.value.toDouble() / right))")
        }

        writer.println()
        writer.println("\toperator fun div(right: ${quantity.className}) = this.value.toDouble() / right.value.toDouble()")
    }

    override fun generateCompanionContent() {
        writer.println()
        writer.println("\t\tfun raw(value: ${quantity.number.internalType}) = ${quantity.className}(${quantity.number.className}.raw(value))")
    }

    protected abstract fun generateNumberUnitExtensionFunctions(typeName: String)

    override fun generateExtensionFunctions() {
        for (typeName in arrayOf("Int", "Long", "Float", "Double", quantity.number.className)) {
            writer.println()
            if (quantity.number.checkOverflow) writer.println("@Throws(FixedPointException::class)")
            writer.println("operator fun $typeName.times(right: ${quantity.className}) = right * this")

            if (quantity.createNumberExtensions) generateNumberUnitExtensionFunctions(typeName)
        }
    }

    override fun generateMathFunctions() {
        if (quantity.number.internalType.signed) {
            writer.println()
            if (quantity.number.checkOverflow) writer.println("@Throws(FixedPointException::class)")
            writer.println("fun abs(x: ${quantity.className}) = ${quantity.className}(abs(x.value))")
        }

        writer.println()
        writer.println("fun min(a: ${quantity.className}, b: ${quantity.className}) = ${quantity.className}(min(a.value, b.value))")
        writer.println()
        writer.println("fun max(a: ${quantity.className}, b: ${quantity.className}) = ${quantity.className}(max(a.value, b.value))")
    }
}
