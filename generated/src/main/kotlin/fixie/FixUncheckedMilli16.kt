// Generated by fixie at 01-02-2024 23:28
package fixie

import java.math.BigInteger
import java.lang.Math.*

private const val RAW_ONE: Short = 1000

@JvmInline
value class FixUncheckedMilli16 private constructor(val raw: Short) : Comparable<FixUncheckedMilli16> {

	override fun toString(): String {
		val intPart = raw / RAW_ONE
		var fractPart = (raw % RAW_ONE).toString().replace("-", "")
		while (fractPart.length < 3) fractPart = "0$fractPart"
		fractPart = ".$fractPart"
		while (fractPart.endsWith('0')) fractPart = fractPart.substring(0 until fractPart.length - 1)
		if (fractPart == ".") fractPart = ""
		val minus = if (raw < 0 && intPart == 0) "-" else ""
		return "$minus$intPart$fractPart"
	}

	fun toInt() = raw / RAW_ONE

	fun toLong() = (raw / RAW_ONE).toLong()

	fun toFloat() = toDouble().toFloat()

	fun toDouble() = raw.toDouble() / RAW_ONE.toDouble()

	operator fun unaryMinus() = FixUncheckedMilli16((-raw).toShort())

	operator fun plus(right: FixUncheckedMilli16) = FixUncheckedMilli16((this.raw + right.raw).toShort())

	operator fun plus(right: Int) = this + from(right)

	operator fun plus(right: Long) = this + from(right)

	operator fun plus(right: Float) = this + from(right)

	operator fun plus(right: Double) = this + from(right)

	operator fun minus(right: FixUncheckedMilli16) = FixUncheckedMilli16((this.raw - right.raw).toShort())

	operator fun minus(right: Int) = this - from(right)

	operator fun minus(right: Long) = this - from(right)

	operator fun minus(right: Float) = this - from(right)

	operator fun minus(right: Double) = this - from(right)

	operator fun times(right: FixUncheckedMilli16): FixUncheckedMilli16 {
		val largeValue = this.raw.toInt() * right.raw.toInt()
		return FixUncheckedMilli16((largeValue / RAW_ONE).toShort())
	}

	operator fun times(right: Int) = FixUncheckedMilli16((raw * right).toShort())

	operator fun times(right: Long) = FixUncheckedMilli16((raw * right).toShort())

	operator fun times(right: Float) = this * from(right)

	operator fun times(right: Double) = this * from(right)

	operator fun div(right: FixUncheckedMilli16): FixUncheckedMilli16 {
		val largeValue = this.raw.toInt() * RAW_ONE.toInt()
		return FixUncheckedMilli16((largeValue / right.raw).toShort())
	}

	operator fun div(right: Int) = FixUncheckedMilli16((raw / right).toShort())

	operator fun div(right: Long) = FixUncheckedMilli16((raw / right).toShort())

	operator fun div(right: Float) = this / from(right)

	operator fun div(right: Double) = this / from(right)

	override operator fun compareTo(other: FixUncheckedMilli16) = this.raw.compareTo(other.raw)

	operator fun compareTo(other: Short) = if (other < -32) 1 else if (other > 32) -1 else this.compareTo(from(other))

	operator fun compareTo(other: Int) = if (other < -32) 1 else if (other > 32) -1 else this.compareTo(from(other))

	operator fun compareTo(other: Long) = if (other < -32) 1 else if (other > 32) -1 else this.compareTo(from(other))
	operator fun compareTo(other: Float) = if (other < -32.768f) 1 else if (other > 32.767f) -1 else this.compareTo(from(other))
	operator fun compareTo(other: Double) = if (other < -32.768) 1 else if (other > 32.767) -1 else this.compareTo(from(other))

	companion object {

		val ZERO = from(0)
		val ONE = from(1)

		fun raw(rawValue: Short) = FixUncheckedMilli16(rawValue)

		fun from(value: Short) = FixUncheckedMilli16((value * RAW_ONE).toShort())

		fun from(value: Int) = from(value.toShort())

		fun from(value: Long) = from(value.toShort())

		fun from(value: Float) = from(value.toDouble())

		fun from(value: Double) = FixUncheckedMilli16(round(RAW_ONE.toDouble() * value).toShort())
	}

	@JvmInline
	value class Array private constructor(val raw: ShortArray) {

		constructor(size: Int) : this(ShortArray(size))

		constructor(size: Int, initializer: (Int) -> FixUncheckedMilli16) : this(ShortArray(size) { index -> initializer(index).raw })

		val size: Int
			get() = raw.size

		operator fun get(index: Int) = FixUncheckedMilli16(raw[index])

		operator fun set(index: Int, value: FixUncheckedMilli16) {
			raw[index] = value.raw
		}

		fun fill(value: FixUncheckedMilli16) {
			raw.fill(value.raw)
		}
	}
}

operator fun Int.plus(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this) + right

operator fun Long.plus(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this) + right

operator fun Float.plus(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this) + right

operator fun Double.plus(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this) + right

operator fun Int.minus(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this) - right

operator fun Long.minus(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this) - right

operator fun Float.minus(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this) - right

operator fun Double.minus(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this) - right

operator fun Int.times(right: FixUncheckedMilli16) = right * this

operator fun Long.times(right: FixUncheckedMilli16) = right * this

operator fun Float.times(right: FixUncheckedMilli16) = right * this

operator fun Double.times(right: FixUncheckedMilli16) = right * this

operator fun Int.div(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this) / right

operator fun Long.div(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this) / right

operator fun Float.div(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this) / right

operator fun Double.div(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this) / right

operator fun Int.compareTo(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this).compareTo(right)

operator fun Long.compareTo(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this).compareTo(right)

operator fun Float.compareTo(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this).compareTo(right)

operator fun Double.compareTo(right: FixUncheckedMilli16) = FixUncheckedMilli16.from(this).compareTo(right)

fun abs(value: FixUncheckedMilli16) = if (value.raw != Short.MIN_VALUE) FixUncheckedMilli16.raw(abs(value.raw))
		else throw FixedPointException("Can't represent abs of min value")

fun min(a: FixUncheckedMilli16, b: FixUncheckedMilli16) = FixUncheckedMilli16.raw(min(a.raw, b.raw))

fun max(a: FixUncheckedMilli16, b: FixUncheckedMilli16) = FixUncheckedMilli16.raw(max(a.raw, b.raw))
