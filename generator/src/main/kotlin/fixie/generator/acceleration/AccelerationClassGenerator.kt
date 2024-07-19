package fixie.generator.acceleration

import fixie.generator.quantity.FloatQuantityClassGenerator
import java.io.PrintWriter

internal class AccelerationClassGenerator(
        writer: PrintWriter,
        acceleration: AccelerationClass,
        packageName: String
) : FloatQuantityClassGenerator<AccelerationClass>(writer, acceleration, packageName) {

    override fun getImports() = super.getImports() + arrayOf("kotlin.time.Duration", "kotlin.time.DurationUnit")

    override fun generateToDouble() {
        writer.println()
        writer.println("\t/** Gets the acceleration value, in m/s^2 */")
        val conversion = if (quantity.floatType.numBytes == 4) ".toDouble()" else ""
        writer.println("\tfun toDouble() = value$conversion")
    }

    override fun generateToString() {
        writer.println()
        writer.println("\toverride fun toString() = String.format(\"%.2f%s\", value, \"m/s^2\")")
    }

    override fun generateArithmetic() {
        super.generateArithmetic()

        if (quantity.speedClassName != null) {
            writer.println()
            writer.println("\toperator fun times(right: Duration) = value * ${quantity.speedClassName}.METERS_PER_SECOND * right.toDouble(DurationUnit.SECONDS)")
        }
    }

    override fun generateCompanionContent() {
        val suffix = if (quantity.floatType.numBytes == 4) "f" else ".0"
        writer.println("\t\tval MPS2 = ${quantity.className}(1$suffix)")
    }

    override fun generateNumberUnitExtensionFunctions(typeName: String) {
        writer.println()
        writer.println("val $typeName.mps2")
        writer.println("\tget() = ${quantity.className}.MPS2 * this")
    }
}
