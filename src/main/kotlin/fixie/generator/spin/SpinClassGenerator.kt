package fixie.generator.spin

import fixie.generator.number.FloatType
import fixie.generator.quantity.FloatQuantityClassGenerator
import java.io.PrintWriter

class SpinClassGenerator(
        writer: PrintWriter,
        spin: SpinClass,
        packageName: String
) : FloatQuantityClassGenerator<SpinClass>(writer, spin, packageName) {

    override fun getImports() = super.getImports() + if (quantity.angleClassName != null) {
        arrayOf("kotlin.time.Duration", "kotlin.time.DurationUnit")
    } else emptyArray()

    override fun generateToDouble() {
        writer.println()
        writer.println("\tfun toDouble(unit: SpinUnit) = when(unit) {")
        for (unit in SpinUnit.entries) {
            val factor = unit.angleMax / quantity.oneUnit.angleMax
            val conversion = if (factor == 1.0) "" else " * $factor"
            val toDouble = if (quantity.floatType == FloatType.DoublePrecision) "" else ".toDouble()"
            writer.println("\t\tSpinUnit.$unit -> value$toDouble$conversion")
        }
        writer.println("\t}")
    }

    override fun generateToString() {
        writer.println()
        writer.println("\toverride fun toString() = toString(SpinUnit.${quantity.displayUnit})")
        writer.println()
        writer.println("\tfun toString(unit: SpinUnit): String {")
        writer.println("\t\tval format = if (unit == SpinUnit.DEGREES_PER_SECOND) \"%.0f\" else \"%.2f\"")
        writer.println("\t\treturn String.format(\"\$format%s\", toDouble(unit), unit.suffix)")
        writer.println("\t}")
    }

    override fun generateArithmetic() {
        super.generateArithmetic()

        if (quantity.angleClassName != null) {
            val functionName = if (quantity.oneUnit == SpinUnit.DEGREES_PER_SECOND) "degrees" else "radians"
            writer.println()
            writer.println("\toperator fun times(right: Duration) = ${quantity.angleClassName}.$functionName(value * right.toDouble(DurationUnit.SECONDS))")
        }

        // TODO Angular acceleration?
//        if (speed.acceleration != null) {
//            writer.println()
//            writer.println("\toperator fun div(right: Duration) = toDouble(SpeedUnit.METERS_PER_SECOND) * ${speed.acceleration.className}.MPS2 / right.toDouble(DurationUnit.SECONDS)")
//        }
    }

    override fun generateCompanionContent() {
        writer.println("\t\tfun raw(value: ${quantity.floatType.typeName}) = ${quantity.className}(value)")
        writer.println()
        val oneValue = if (quantity.floatType == FloatType.SinglePrecision) "1f" else "1.0"
        writer.println("\t\tval ${quantity.oneUnit} = ${quantity.className}($oneValue)")

        for (unit in SpinUnit.entries) {
            if (unit != quantity.oneUnit) {
                val factor = quantity.oneUnit.angleMax / unit.angleMax
                writer.println()
                writer.println("\t\tval $unit = ${quantity.oneUnit} * $factor")
            }
        }
    }

    override fun generateNumberUnitExtensionFunctions(typeName: String) {
        for (unit in SpinUnit.entries) {
            writer.println()
            writer.println("val $typeName.${unit.abbreviation.replace('/', 'p')}")
            writer.println("\tget() = ${quantity.className}.$unit * this")
        }
    }

    override fun generateExtensionFunctions() {
        super.generateExtensionFunctions()

        if (quantity.angleClassName != null) {
            writer.println()
            writer.println("operator fun Duration.times(right: ${quantity.className}) = right * this")
        }
    }
}
