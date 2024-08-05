package fixie.generator.volume

import fixie.generator.displacement.DistanceUnit
import fixie.generator.quantity.FloatQuantityClassGenerator
import java.io.PrintWriter

class VolumeClassGenerator(
        writer: PrintWriter,
        volume: VolumeClass,
        packageName: String
) : FloatQuantityClassGenerator<VolumeClass>(writer, volume, packageName) {

    override fun generateToDouble() {
        writer.println()
        val conversion = if (quantity.floatType.numBytes == 4) ".toDouble()" else ""
        writer.println("\tfun toDouble(unit: VolumeUnit) = value$conversion / unit.factor")
    }

    override fun generateToString() {
        writer.println()
        writer.println("\tfun toString(unit: VolumeUnit) = String.format(\"%.3f%s\", toDouble(unit), unit.abbreviation)")
        writer.println()
        writer.println("\toverride fun toString() = toString(VolumeUnit.${quantity.displayUnit})")
    }

    override fun generateArithmetic() {
        super.generateArithmetic()

        quantity.displacement?.let {displacement ->
            quantity.area?.let { area ->
                writer.println()
                writer.println("\toperator fun div(right: ${displacement.className}) = " +
                        "${area.className}.SQUARE_METER * value / right.toDouble(DistanceUnit.METER)")

                if (displacement.computeSupportedUnits().find { it.first == DistanceUnit.METER } != null) {
                    writer.println()
                    writer.println("\toperator fun div(right: ${area.className}) = " +
                            "${displacement.className}.METER * (value / right.toDouble(AreaUnit.SQUARE_METER))")
                }
            }
        }
        // TODO Multiply with density to get mass
    }

    override fun generateCompanionContent() {
        val suffix = if (quantity.floatType.numBytes == 4) "f" else ""
        for (unit in VolumeUnit.entries) {
            writer.println("\t\tval ${unit.name} = ${quantity.className}(${unit.factor}$suffix)")
        }
    }

    override fun generateNumberUnitExtensionFunctions(typeName: String) {
        writer.println()
        for (unit in VolumeUnit.entries) {
            writer.println("val $typeName.${unit.abbreviation.replace("^", "")}")
            writer.println("\tget() = ${quantity.className}.${unit.name} * this")
        }
    }
}
