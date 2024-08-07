package fixie.generator.quantity

import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class QuantityClassGenerator<T: QuantityClass>(
        protected val writer: PrintWriter,
        protected val quantity: T,
        protected val packageName: String
) {
    open fun generate() {
        generateClassPrefix()
        generateToDouble()
        generateToString()
        generateArithmetic()
        generateCompanionObject()
        writer.println("}")
        generateExtensionFunctions()
        generateMathFunctions()
    }

    protected abstract fun getInternalTypeName(): String

    protected abstract fun getImports(): Array<String>

    open fun generateClassHeader() {
        writer.println("@JvmInline")
        writer.println("value class ${quantity.className} internal constructor(val value: ${getInternalTypeName()}) " +
                ": Comparable<${quantity.className}> {")
    }

    protected open fun generateClassPrefix() {
        val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
        writer.println("// Generated by fixie at $currentTime")
        writer.println("package $packageName")

        val imports = getImports()
        if (imports.isNotEmpty()) {
            writer.println()
            for (import in imports) writer.println("import $import")
        }
        writer.println()
        generateClassHeader()
    }

    protected abstract fun generateToDouble()

    protected abstract fun generateToString()

    abstract fun generateArithmetic()

    protected abstract fun generateCompanionContent()

    protected open fun generateCompanionObject() {
        writer.println()
        writer.println("\tcompanion object {")
        generateCompanionContent()
        writer.println("\t}")
    }

    protected abstract fun generateExtensionFunctions()

    abstract fun generateMathFunctions()
}
