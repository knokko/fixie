package fixie.generator.spin

import fixie.generator.quantity.FloatQuantityTestsGenerator
import fixie.generator.quantity.QuantityUnit
import java.io.PrintWriter

class SpinTestsGenerator(
        writer: PrintWriter,
        spin: SpinClass,
        packageName: String
) : FloatQuantityTestsGenerator<SpinClass>(writer, spin, packageName) {

    private val dps = "${quantity.className}.DEGREES_PER_SECOND"
    private val rps = "${quantity.className}.RADIANS_PER_SECOND"

    override fun getImports() = super.getImports() + arrayOf("kotlin.math.PI") + if (quantity.angleClassName != null) {
        arrayOf("kotlin.time.Duration.Companion.seconds")
    } else emptyArray()

    override fun getUnits() = SpinUnit.entries.map { QuantityUnit(
            it.name, "SpinUnit", it.suffix, it.abbreviation, 0.001, 1e6
    ) }

    override fun generateToDoubleBody() {
        super.generateToDoubleBody()
        writer.println("\t\tassertEquals(180.0, (Math.PI * $rps).toDouble(SpinUnit.DEGREES_PER_SECOND), 0.1)")
        writer.println("\t\tassertEquals(Math.PI, (180 * $dps).toDouble(SpinUnit.RADIANS_PER_SECOND), 0.01)")
    }

    override fun generateToStringBody() {
        writer.println("\t\tassertEquals(\"123°/s\", (123.45 * $dps).toString(SpinUnit.DEGREES_PER_SECOND))")
        writer.println("\t\tassertEquals(\"1.23rad/s\", (1.23 * $rps).toString(SpinUnit.RADIANS_PER_SECOND))")
        writer.println("\t\tassertEquals(\"-180°/s\", (-Math.PI * $rps).toString(SpinUnit.DEGREES_PER_SECOND))")
        writer.println("\t\tassertEquals(\"3.14rad/s\", (180 * $dps).toString(SpinUnit.RADIANS_PER_SECOND))")
        if (quantity.displayUnit == SpinUnit.DEGREES_PER_SECOND) {
            writer.println("\t\tassertEquals(\"123°/s\", (123.45 * $dps).toString())")
        } else {
            writer.println("\t\tassertEquals(\"3.14rad/s\", (180 * $dps).toString())")
        }
    }

    override fun generateCompareToBody() {
        super.generateCompareToBody()
        writer.println("\t\tassertTrue(55 * $dps < $rps)")
        writer.println("\t\tassertFalse(60 * $dps < $rps)")
        writer.println("\t\tassertTrue(20 * $dps > -$rps)")
        writer.println("\t\tassertFalse(20 * $dps > $rps)")
        writer.println("\t\tassertTrue(10 * $rps > 5 * $rps)")
        writer.println("\t\tassertFalse(10 * $rps > 10 * $rps)")
        writer.println("\t\tassertFalse(10 * $rps > 15 * $rps)")
        writer.println("\t\tassertFalse(10 * $rps < 5 * $rps)")
        writer.println("\t\tassertFalse(10 * $rps < 10 * $rps)")
        writer.println("\t\tassertTrue(10 * $rps < 15 * $rps)")
    }

    override fun generateArithmeticBody() {
        super.generateArithmeticBody()
        writer.println("\t\tassertEquals(120f * $dps, PI * $rps - $dps * 60)")
        writer.println("\t\tassertEquals(5.0 * PI * $rps, PI * $rps / (1.0 / 3.0) + $dps * 360L)")
        writer.println("\t\tassertEquals(-$dps, $dps / 0.1f - $dps * 11f)")
        writer.println("\t\tassertEquals(2.0, $dps * 360 / (PI * $rps), 0.001)")
        writer.println("\t\tassertNotEquals($dps, $rps)")
        if (quantity.angleClassName != null) {
            writer.println("\t\tassertEquals(150.0, (3.seconds * (50 * $dps)).toDouble(AngleUnit.DEGREES), 0.5)")
        }
    }

    override fun generateMathFunctionsBody() {
        super.generateMathFunctionsBody()
        writer.println("\t\tassertEquals(50 * $dps, min(50 * $dps, $rps))")
    }
}
