package fixie.generator.spin

import fixie.generator.displacement.DistanceUnit
import fixie.generator.quantity.FloatQuantityClassGenerator
import fixie.generator.speed.SpeedUnit
import java.io.PrintWriter

class SpinClassGenerator(
	writer: PrintWriter,
	spin: SpinClass,
	packageName: String
) : FloatQuantityClassGenerator<SpinClass>(writer, spin, packageName) {

	override fun getImports() = super.getImports() + if (quantity.angle != null || quantity.acceleration != null) {
		arrayOf("kotlin.time.Duration", "kotlin.time.DurationUnit")
	} else emptyArray()

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

        if (quantity.acceleration != null) {
            writer.println()
            writer.println("\toperator fun div(right: Duration) = toDouble(SpinUnit.RADIANS_PER_SECOND) * " +
					"${quantity.accelerationClassName}.RADPS2 / right.toDouble(DurationUnit.SECONDS)")
        }

		quantity.speed?.let { speed ->
			if (speed.computeSupportedUnits().find { it.first == SpeedUnit.METERS_PER_SECOND } != null) {
				quantity.displacement?.let { displacement ->
					if (displacement.computeSupportedUnits().find { it.first == DistanceUnit.METER } != null) {
						writer.println()
						writer.println("\tfun toSpeed(radius: ${quantity.displacementClassName}) = " +
								"${quantity.speedClassName}.METERS_PER_SECOND * (toDouble(SpinUnit.RADIANS_PER_SECOND) * " +
								"radius.toDouble(DistanceUnit.METER))")
					}
				}
			}
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
