package fixie.generator.area

import fixie.generator.displacement.DistanceUnit
import fixie.generator.quantity.FloatQuantityTestsGenerator
import fixie.generator.quantity.QuantityUnit
import java.io.PrintWriter
import java.math.BigInteger

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

        quantity.displacement?.let { displacement ->
            val inchPair = displacement.computeSupportedUnits().find { it.first == DistanceUnit.INCH }
            if (inchPair != null && displacement.computeSupportedUnits().find { it.first == DistanceUnit.METER } != null) {
                val maxInches = displacement.number.internalType.getMaxValue() / inchPair.second
                if (maxInches > BigInteger.TEN) {
                    val margin = if (inchPair.second > BigInteger.valueOf(500)) 0.01 else 0.5
                    writer.println("\t\tassertEquals(4.0, ((10 * ${quantity.className}.SQUARE_INCH) / (2.5 * ${quantity.displacementClassName}.INCH)).toDouble(DistanceUnit.INCH), $margin)")
                }
            }

            val meterPair = displacement.computeSupportedUnits().find { it.first == DistanceUnit.METER }
            if (quantity.volumeClassName != null && meterPair != null && meterPair.second > BigInteger.valueOf(15)) {
                writer.println("\t\tassertEquals(234.0, (0.234 * ${quantity.className}.SQUARE_METER * ${displacement.className}.METER).toDouble(VolumeUnit.LITER), 30.0)")
            }
        }
    }

    override fun generateMathFunctionsBody() {
        super.generateMathFunctionsBody()

        writer.println("\t\tassertEquals(3 * ${quantity.className}.SQUARE_METER, max(5 * ${quantity.className}.SQUARE_INCH, 3 * ${quantity.className}.SQUARE_METER))")

        quantity.displacement?.let { displacement ->
            val meterPair = displacement.computeSupportedUnits().find { it.first == DistanceUnit.METER }
            if (meterPair != null) {
                val maxMeters = displacement.number.internalType.getMaxValue() / meterPair.second
                if (maxMeters > BigInteger.TWO) {
                    val margin = if (meterPair.second > BigInteger.valueOf(500)) 0.01 else 0.25
                    writer.println("\t\tassertEquals(2.0, sqrt(${quantity.className}.SQUARE_METER * 4).toDouble(DistanceUnit.METER), $margin)")
                }
            }
        }
    }
}
