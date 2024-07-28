package fixie.generator.area

import fixie.generator.displacement.DistanceUnit
import fixie.generator.quantity.FloatQuantityClassGenerator
import java.io.PrintWriter

class AreaClassGenerator(
        writer: PrintWriter,
        area: AreaClass,
        packageName: String
) : FloatQuantityClassGenerator<AreaClass>(writer, area, packageName) {

    override fun getImports() = super.getImports() + if (quantity.displacementClassName != null) arrayOf("kotlin.math.sqrt") else emptyArray()

    override fun generateToDouble() {
        writer.println()
        val conversion = if (quantity.floatType.numBytes == 4) ".toDouble()" else ""
        writer.println("\tfun toDouble(unit: AreaUnit) = value$conversion / unit.factor")
    }

    override fun generateToString() {
        writer.println()
        writer.println("\tfun toString(unit: AreaUnit) = String.format(\"%.2f%s\", toDouble(unit), unit.abbreviation)")
        writer.println()
        writer.println("\toverride fun toString() = toString(AreaUnit.${quantity.displayUnit})")
    }

    private fun displacementSupportsMeter(): Boolean {
        quantity.displacement?.let { displacement ->
            return displacement.computeSupportedUnits().find { it.first == DistanceUnit.METER } != null
        }
        return false
    }

    override fun generateArithmetic() {
        super.generateArithmetic()

        if (displacementSupportsMeter()) {
            writer.println()
            writer.println("\toperator fun div(right: ${quantity.displacementClassName}) = " +
                    "${quantity.displacementClassName}.METER * value / right.toDouble(DistanceUnit.METER)")
        }
        // TODO Volume class
    }

    override fun generateCompanionContent() {
        val suffix = if (quantity.floatType.numBytes == 4) "f" else ""
        for (unit in AreaUnit.entries) {
            writer.println("\t\tval ${unit.name} = ${quantity.className}(${unit.factor}$suffix)")
        }
    }

    override fun generateNumberUnitExtensionFunctions(typeName: String) {
        writer.println()
        for (unit in AreaUnit.entries) {
            writer.println("val $typeName.${unit.abbreviation.replace("^", "")}")
            writer.println("\tget() = ${quantity.className}.${unit.name} * this")
        }
    }


    override fun generateMathFunctions() {
        super.generateMathFunctions()

        if (displacementSupportsMeter()) {
            writer.println("fun sqrt(x: ${quantity.className}) = ${quantity.displacementClassName}.METER * sqrt(x.value)")
        }
    }
}
