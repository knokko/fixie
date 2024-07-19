package fixie.generator.speed

import fixie.generator.number.FloatType
import fixie.generator.quantity.HybridQuantityClassGenerator
import java.io.PrintWriter
import java.math.BigDecimal
import java.math.BigInteger

internal class SpeedClassGenerator(
        writer: PrintWriter,
        speed: SpeedClass,
        packageName: String
) : HybridQuantityClassGenerator<SpeedClass>(writer, speed, packageName) {

    private var unitConstants = mutableListOf(speed.oneUnit)

    override fun getImports() = super.getImports() + arrayOf("kotlin.time.Duration", "kotlin.time.DurationUnit")

    override fun generateToDouble() {
        writer.println()
        writer.println("\tfun toDouble(unit: SpeedUnit) = when(unit) {")
        for (unit in SpeedUnit.entries) {
            val factor = unit.factor / quantity.oneUnit.factor
            val conversion = if (factor == 1.0) "" else " * $factor"
            val toDouble = if (quantity.floatType == FloatType.DoublePrecision) "" else ".toDouble()"
            writer.println("\t\tSpeedUnit.$unit -> value$toDouble$conversion")
        }
        writer.println("\t}")
    }

    override fun generateToString() {
        writer.println()
        writer.println("\toverride fun toString() = toString(SpeedUnit.${quantity.displayUnit})")
        writer.println()
        writer.println("\tfun toString(unit: SpeedUnit): String {")
        writer.println("\t\treturn String.format(\"%.4f%s\", toDouble(unit), unit.abbreviation)")
        writer.println("\t}")
    }

    override fun generateArithmetic() {
        super.generateArithmetic()

        if (quantity.displacementClassName != null) {
            writer.println()
            if (quantity.displacementNumber!!.checkOverflow) writer.println("\t@Throws(FixedPointException::class)")
            writer.println("\toperator fun times(right: Duration) = toDouble(SpeedUnit.METERS_PER_SECOND) * " +
                    "${quantity.displacementClassName}.METER * right.toDouble(DurationUnit.SECONDS)")
        }
        if (quantity.acceleration != null) {
            writer.println()
            writer.println("\toperator fun div(right: Duration) = toDouble(SpeedUnit.METERS_PER_SECOND) * " +
                    "${quantity.acceleration.className}.MPS2 / right.toDouble(DurationUnit.SECONDS)")
        }
    }

    override fun generateCompanionContent() {
        super.generateCompanionContent()

        val oneValue = if (quantity.number != null) quantity.number.className + ".ONE"
        else if (quantity.floatType == FloatType.SinglePrecision) "1f" else "1.0"
        writer.println("\t\tval ${quantity.oneUnit} = ${quantity.className}($oneValue)")

        for (unit in SpeedUnit.entries) {
            if (unit != quantity.oneUnit) {
                val factor = quantity.oneUnit.factor / unit.factor

                val canRepresent = if (quantity.number != null) {
                    val rawValue = BigDecimal.valueOf(quantity.number.oneValue.toDouble()) * BigDecimal.valueOf(factor)
                    quantity.number.internalType.canRepresent(rawValue.toBigInteger() + BigInteger.TWO)
                } else true

                if (canRepresent) {
                    writer.println()
                    writer.println("\t\tval $unit = ${quantity.oneUnit} * $factor")
                    unitConstants.add(unit)
                }
            }
        }
    }

    override fun generateNumberUnitExtensionFunctions(typeName: String) {
        for (unit in unitConstants) {
            writer.println()
            writer.println("val $typeName.${unit.abbreviation.replace('/', 'p')}")
            writer.println("\tget() = ${quantity.className}.$unit * this")
        }
    }

    override fun generateExtensionFunctions() {
        super.generateExtensionFunctions()

        if (quantity.displacementNumber != null) {
            writer.println()
            if (quantity.displacementNumber.checkOverflow) writer.println("@Throws(FixedPointException::class)")
            writer.println("operator fun Duration.times(right: ${quantity.className}) = right * this")
        }
    }
}
