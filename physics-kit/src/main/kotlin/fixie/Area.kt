// Generated by fixie at 16-07-2024 20:46
package fixie

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

@JvmInline
value class Area internal constructor(val value: Double) : Comparable<Area> {

	fun toDouble(unit: AreaUnit) = value / unit.factor

	fun toString(unit: AreaUnit) = String.format("%.2f%s", toDouble(unit), unit.abbreviation)

	override fun toString() = toString(AreaUnit.SQUARE_METER)

	override operator fun compareTo(other: Area) = this.value.compareTo(other.value)

	operator fun unaryMinus() = Area(-value)

	operator fun plus(right: Area) = Area(this.value + right.value)

	operator fun minus(right: Area) = Area(this.value - right.value)

	operator fun times(right: Int) = Area(this.value * right)

	operator fun div(right: Int) = Area(this.value / right)

	operator fun times(right: Long) = Area(this.value * right)

	operator fun div(right: Long) = Area(this.value / right)

	operator fun times(right: Float) = Area(this.value * right)

	operator fun div(right: Float) = Area(this.value / right)

	operator fun times(right: Double) = Area(this.value * right)

	operator fun div(right: Double) = Area(this.value / right)

	operator fun div(right: Displacement) = Displacement.METER * value / right.toDouble(DistanceUnit.METER)

	operator fun div(right: Area) = this.value / right.value

	companion object {
		val SQUARE_MILLIMETER = Area(1.0E-6)
		val SQUARE_INCH = Area(6.4516E-4)
		val SQUARE_METER = Area(1.0)
		val HECTARE = Area(10000.0)
	}
}

operator fun Int.times(right: Area) = right * this

val Int.mm2
	get() = Area.SQUARE_MILLIMETER * this
val Int.in2
	get() = Area.SQUARE_INCH * this
val Int.m2
	get() = Area.SQUARE_METER * this
val Int.ha
	get() = Area.HECTARE * this

operator fun Long.times(right: Area) = right * this

val Long.mm2
	get() = Area.SQUARE_MILLIMETER * this
val Long.in2
	get() = Area.SQUARE_INCH * this
val Long.m2
	get() = Area.SQUARE_METER * this
val Long.ha
	get() = Area.HECTARE * this

operator fun Float.times(right: Area) = right * this

val Float.mm2
	get() = Area.SQUARE_MILLIMETER * this
val Float.in2
	get() = Area.SQUARE_INCH * this
val Float.m2
	get() = Area.SQUARE_METER * this
val Float.ha
	get() = Area.HECTARE * this

operator fun Double.times(right: Area) = right * this

val Double.mm2
	get() = Area.SQUARE_MILLIMETER * this
val Double.in2
	get() = Area.SQUARE_INCH * this
val Double.m2
	get() = Area.SQUARE_METER * this
val Double.ha
	get() = Area.HECTARE * this

fun abs(x: Area) = Area(abs(x.value))

fun min(a: Area, b: Area) = Area(min(a.value, b.value))

fun max(a: Area, b: Area) = Area(max(a.value, b.value))
fun sqrt(x: Area) = Displacement.METER * sqrt(x.value)
