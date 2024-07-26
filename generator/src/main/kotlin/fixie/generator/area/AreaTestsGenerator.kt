package fixie.generator.area

import fixie.generator.quantity.FloatQuantityTestsGenerator
import fixie.generator.quantity.QuantityUnit
import java.io.PrintWriter

internal class AreaTestsGenerator(
        writer: PrintWriter,
        area: AreaClass,
        packageName: String
) : FloatQuantityTestsGenerator<AreaClass>(
    writer, area, packageName
) {
    override fun getUnits() = AreaUnit.entries.map { QuantityUnit(
        it.name, "AreaUnit", it.abbreviation, it.abbreviation.replace("^", ""), 0.001, 1e6
    ) }

    override fun generateToDoubleBody() {
        super.generateToDoubleBody()
        writer.println("\t\tassertEquals(25000.0, (2.5 * ${quantity.className}.HECTARE).toDouble(AreaUnit.SQUARE_METER), 0.1)")
        writer.println("\t\tassertEquals(0.1, (1000 * ${quantity.className}.SQUARE_METER).toDouble(AreaUnit.HECTARE), 0.001)")
    }

    override fun generateToStringBody() {
        writer.println("\t\tassertEquals(\"2.34ha\", (${quantity.className}.HECTARE * 2.34).toString(AreaUnit.HECTARE))")
        writer.println("\t\tassertEquals(\"5.67in^2\", (5.67 * ${quantity.className}.SQUARE_INCH).toString(AreaUnit.SQUARE_INCH))")
        writer.println("\t\tassertEquals(\"8.91m^2\", (8.91 * ${quantity.className}.SQUARE_METER).toString(AreaUnit.SQUARE_METER))")
        writer.println("\t\tassertEquals(\"0.12${quantity.displayUnit.abbreviation}\", (0.1234 * ${quantity.className}.${quantity.displayUnit.name}).toString())")
    }

    override fun generateCompareToBody() {
        super.generateCompareToBody()
        writer.println("\t\tassertTrue(${quantity.className}.SQUARE_METER > ${quantity.className}.SQUARE_INCH * 200)")
        writer.println("\t\tassertTrue(${quantity.className}.SQUARE_METER < ${quantity.className}.HECTARE / 500)")
        writer.println("\t\tassertTrue(${quantity.className}.SQUARE_INCH > -${quantity.className}.HECTARE)")
    }

    override fun generateArithmeticBody() {
        super.generateArithmeticBody()
        writer.println("\t\tassertEquals(${quantity.className}.SQUARE_METER, 1000 * ${quantity.className}.SQUARE_MILLIMETER * 1000L)")
        writer.println("\t\tassertNotEquals(${quantity.className}.SQUARE_METER, ${quantity.className}.SQUARE_INCH)")
        writer.println("\t\tassertNotEquals(${quantity.className}.SQUARE_METER, ${quantity.className}.HECTARE)")
        writer.println("\t\tassertNotEquals(${quantity.className}.SQUARE_METER, ${quantity.className}.SQUARE_MILLIMETER)")
        writer.println("\t\tassertEquals(100 * 100 * ${quantity.className}.SQUARE_METER, ${quantity.className}.HECTARE)")
        writer.println("\t\tassertEquals(${quantity.className}.HECTARE, 100 * 100 * ${quantity.className}.SQUARE_METER)")
        if (quantity.displacementClassName != null) {
            writer.println("\t\tassertEquals(4.0, ((10 * ${quantity.className}.SQUARE_INCH) / (2.5 * ${quantity.displacementClassName}.INCH)).toDouble(DistanceUnit.INCH), 0.01)")
        }
        // TODO Volume
    }

    override fun generateMathFunctionsBody() {
        super.generateMathFunctionsBody()
        writer.println("\t\tassertEquals(3 * ${quantity.className}.SQUARE_METER, max(5 * ${quantity.className}.SQUARE_INCH, 3 * ${quantity.className}.SQUARE_METER))")
        if (quantity.displacementClassName != null) {
            writer.println("\t\tassertEquals(2.0, sqrt(${quantity.className}.SQUARE_METER * 4).toDouble(DistanceUnit.METER), 0.01)")
        }
    }
}
