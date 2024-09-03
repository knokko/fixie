package fixie.generator.mass

import fixie.generator.quantity.FloatQuantityTestsGenerator
import java.io.PrintWriter
import java.math.BigInteger
import kotlin.math.max

class MassTestsGenerator(
	writer: PrintWriter,
	quantity: MassClass,
	packageName: String
) : FloatQuantityTestsGenerator<MassClass>(writer, quantity, packageName) {



    override fun generateToDoubleBody() {
        super.generateToDoubleBody()
        writer.println("\t\tassertEquals(25000.0, (25 * ${quantity.className}.KILOGRAM).toDouble(MassUnit.GRAM), 0.1)")
        writer.println("\t\tassertEquals(283.5, (10 * ${quantity.className}.OUNCE).toDouble(MassUnit.GRAM), 0.1)")
    }

    override fun generateToStringBody() {
        writer.println("\t\tassertEquals(\"2.345t\", (${quantity.className}.KILOGRAM * 2345).toString(MassUnit.TON))")
        writer.println("\t\tassertEquals(\"11.023lbs\", (5.0 * ${quantity.className}.KILOGRAM).toString(MassUnit.POUND))")
        writer.println("\t\tassertEquals(\"0.123${quantity.displayUnit.abbreviation}\", (0.1234 * ${quantity.className}.${quantity.displayUnit.name}).toString())")
    }

    override fun generateCompareToBody() {
        super.generateCompareToBody()
        writer.println("\t\tassertTrue(${quantity.className}.TON > ${quantity.className}.KILOGRAM * 200)")
        writer.println("\t\tassertTrue(${quantity.className}.POUND < ${quantity.className}.KILOGRAM / 2)")
        writer.println("\t\tassertFalse(${quantity.className}.POUND < ${quantity.className}.KILOGRAM / 3)")
    }

    override fun generateArithmeticBody() {
        super.generateArithmeticBody()
        writer.println("\t\tassertEquals(${quantity.className}.KILOGRAM, 1000 * ${quantity.className}.GRAM)")
        writer.println("\t\tassertNotEquals(${quantity.className}.KILOGRAM, ${quantity.className}.POUND)")

        quantity.density?.let { density ->
            val canRepresent10 = density.number == null || density.number.internalType.getMaxValue() / density.number.oneValue > BigInteger.TEN
            if (quantity.volume != null && canRepresent10) {
                val margin = if (density.number == null) 0.001 else max(0.001, 5.0 / density.number.oneValue.toDouble())
                writer.println("\t\tassertEquals(4.0, (8000 * ${quantity.className}.GRAM / (2 * ${quantity.volumeClassName}.LITER)).toDouble(), $margin)")
                writer.println("\t\tassertEquals(2.5, (25 * ${quantity.className}.KILOGRAM / (10 * ${density.className}.KGPL)).toDouble(VolumeUnit.LITER), $margin)")
            }
        }
    }

    override fun generateMathFunctionsBody() {
        super.generateMathFunctionsBody()

        writer.println("\t\tassertEquals(${quantity.className}.TON, max(800 * ${quantity.className}.KILOGRAM, ${quantity.className}.TON))")
    }
}
