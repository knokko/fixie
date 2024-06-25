// Generated by fixie at 19-06-2024 19:58
package fixie

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.DurationUnit

@JvmInline
value class Spin internal constructor(val value: Float) : Comparable<Spin> {

	fun toDouble(unit: SpinUnit) = when(unit) {
		SpinUnit.DEGREES_PER_SECOND -> value.toDouble()
		SpinUnit.RADIANS_PER_SECOND -> value.toDouble() * 0.017453292519943295
	}

	override fun toString() = toString(SpinUnit.DEGREES_PER_SECOND)

	fun toString(unit: SpinUnit): String {
		val format = if (unit == SpinUnit.DEGREES_PER_SECOND) "%.0f" else "%.2f"
		return String.format("$format%s", toDouble(unit), unit.suffix)
	}

	override operator fun compareTo(other: Spin) = this.value.compareTo(other.value)

	operator fun unaryMinus() = Spin(-value)

	operator fun plus(right: Spin) = Spin(this.value + right.value)

	operator fun minus(right: Spin) = Spin(this.value - right.value)

	operator fun times(right: Int) = Spin(this.value * right)

	operator fun div(right: Int) = Spin(this.value / right)

	operator fun times(right: Long) = Spin(this.value * right)

	operator fun div(right: Long) = Spin(this.value / right)

	operator fun times(right: Float) = Spin(this.value * right)

	operator fun div(right: Float) = Spin(this.value / right)

	operator fun times(right: Double) = Spin(this.value * right.toFloat())

	operator fun div(right: Double) = Spin(this.value / right.toFloat())

	operator fun times(right: Duration) = Angle.degrees(value * right.toDouble(DurationUnit.SECONDS))

	operator fun div(right: Spin) = this.value / right.value

	companion object {

		fun raw(value: Float) = Spin(value)

		val DEGREES_PER_SECOND = Spin(1f)

		val RADIANS_PER_SECOND = DEGREES_PER_SECOND * 57.29577951308232
	}
}

operator fun Int.times(right: Spin) = right * this

val Int.degps
	get() = Spin.DEGREES_PER_SECOND * this

val Int.radps
	get() = Spin.RADIANS_PER_SECOND * this

operator fun Long.times(right: Spin) = right * this

val Long.degps
	get() = Spin.DEGREES_PER_SECOND * this

val Long.radps
	get() = Spin.RADIANS_PER_SECOND * this

operator fun Float.times(right: Spin) = right * this

val Float.degps
	get() = Spin.DEGREES_PER_SECOND * this

val Float.radps
	get() = Spin.RADIANS_PER_SECOND * this

operator fun Double.times(right: Spin) = right * this

val Double.degps
	get() = Spin.DEGREES_PER_SECOND * this

val Double.radps
	get() = Spin.RADIANS_PER_SECOND * this

operator fun Duration.times(right: Spin) = right * this

fun abs(x: Spin) = Spin(abs(x.value))

fun min(a: Spin, b: Spin) = Spin(min(a.value, b.value))

fun max(a: Spin, b: Spin) = Spin(max(a.value, b.value))