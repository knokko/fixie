// Generated by fixie at 26-01-2024 14:35
package fixie

import java.lang.Math.*

private const val RAW_ONE = 1000000

@JvmInline
value class FixDecMicro32 private constructor(val raw: Int) : Comparable<FixDecMicro32> {

	fun toInt() = raw / RAW_ONE

	fun toLong() = (raw / RAW_ONE).toLong()

	fun toFloat() = toDouble().toFloat()

	fun toDouble() = raw.toDouble() / RAW_ONE.toDouble()

	@Throws(FixedPointException::class)
	operator fun unaryMinus() = if (raw != Int.MIN_VALUE)
		FixDecMicro32(-raw) else throw FixedPointException("Can't negate MIN_VALUE")

	@Throws(FixedPointException::class)
	operator fun plus(right: FixDecMicro32): FixDecMicro32 {
		try {
			return FixDecMicro32(addExact(this.raw, right.raw))
		} catch (overflow: ArithmeticException) {
			throw FixedPointException("Tried to compute $this + $right")
		}
	}

	@Throws(FixedPointException::class)
	operator fun plus(right: Int) = this + from(right)

	@Throws(FixedPointException::class)
	operator fun plus(right: Long) = this + from(right)

	@Throws(FixedPointException::class)
	operator fun plus(right: Float) = this + from(right)

	@Throws(FixedPointException::class)
	operator fun plus(right: Double) = this + from(right)

	@Throws(FixedPointException::class)
	operator fun minus(right: FixDecMicro32): FixDecMicro32 {
		try {
			return FixDecMicro32(subtractExact(this.raw, right.raw))
		} catch (overflow: ArithmeticException) {
			throw FixedPointException("Tried to compute $this - $right")
		}
	}

	@Throws(FixedPointException::class)
	operator fun minus(right: Int) = this - from(right)

	@Throws(FixedPointException::class)
	operator fun minus(right: Long) = this - from(right)

	@Throws(FixedPointException::class)
	operator fun minus(right: Float) = this - from(right)

	@Throws(FixedPointException::class)
	operator fun minus(right: Double) = this - from(right)

	@Throws(FixedPointException::class)
	operator fun times(right: FixDecMicro32): FixDecMicro32 {
		val largeValue = this.raw.toLong() * right.raw.toLong()
		try {
			return FixDecMicro32(toIntExact(largeValue / RAW_ONE))
		} catch (overflow: ArithmeticException) {
			throw FixedPointException("Can't represent $this * $right")
		}
	}

	@Throws(FixedPointException::class)
	operator fun times(right: Int) = this * from(right)

	@Throws(FixedPointException::class)
	operator fun times(right: Long) = this * from(right)

	@Throws(FixedPointException::class)
	operator fun times(right: Float) = this * from(right)

	@Throws(FixedPointException::class)
	operator fun times(right: Double) = this * from(right)

	override operator fun compareTo(other: FixDecMicro32) = this.raw.compareTo(other.raw)

	companion object {

		val ZERO = from(0)
		val ONE = from(1)

		fun raw(rawValue: Int) = FixDecMicro32(rawValue)

		@Throws(FixedPointException::class)
		fun from(value: Int): FixDecMicro32 {
			try {
				return FixDecMicro32(multiplyExact(value, RAW_ONE))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

		@Throws(FixedPointException::class)
		fun from(value: Long) = from(toIntExact(value))

		fun from(value: Float) = from(value.toDouble())

		@Throws(FixedPointException::class)
		fun from(value: Double): FixDecMicro32 {
			val doubleValue = RAW_ONE.toDouble() * value
			if (doubleValue > Int.MAX_VALUE.toDouble() || doubleValue < Int.MIN_VALUE.toDouble()) {
				throw FixedPointException("Can't represent $value")
			}
			return FixDecMicro32(round(doubleValue).toInt())
		}
	}

	@JvmInline
	value class Array private constructor(val raw: IntArray) {

		constructor(size: Int) : this(IntArray(size))

		operator fun get(index: Int) = FixDecMicro32(raw[index])

		operator fun set(index: Int, value: FixDecMicro32) {
			raw[index] = value.raw
		}

		fun fill(value: FixDecMicro32) {
			raw.fill(value.raw)
		}
	}
}

@Throws(FixedPointException::class)
operator fun Int.plus(right: FixDecMicro32) = FixDecMicro32.from(this) + right

@Throws(FixedPointException::class)
operator fun Long.plus(right: FixDecMicro32) = FixDecMicro32.from(this) + right

@Throws(FixedPointException::class)
operator fun Float.plus(right: FixDecMicro32) = FixDecMicro32.from(this) + right

@Throws(FixedPointException::class)
operator fun Double.plus(right: FixDecMicro32) = FixDecMicro32.from(this) + right

@Throws(FixedPointException::class)
operator fun Int.minus(right: FixDecMicro32) = FixDecMicro32.from(this) - right

@Throws(FixedPointException::class)
operator fun Long.minus(right: FixDecMicro32) = FixDecMicro32.from(this) - right

@Throws(FixedPointException::class)
operator fun Float.minus(right: FixDecMicro32) = FixDecMicro32.from(this) - right

@Throws(FixedPointException::class)
operator fun Double.minus(right: FixDecMicro32) = FixDecMicro32.from(this) - right

@Throws(FixedPointException::class)
operator fun Int.times(right: FixDecMicro32) = FixDecMicro32.from(this) * right

@Throws(FixedPointException::class)
operator fun Long.times(right: FixDecMicro32) = FixDecMicro32.from(this) * right

@Throws(FixedPointException::class)
operator fun Float.times(right: FixDecMicro32) = FixDecMicro32.from(this) * right

@Throws(FixedPointException::class)
operator fun Double.times(right: FixDecMicro32) = FixDecMicro32.from(this) * right
fun abs(value: FixDecMicro32) = FixDecMicro32.raw(kotlin.math.abs(value.raw))
fun min(a: FixDecMicro32, b: FixDecMicro32) = FixDecMicro32.raw(kotlin.math.min(a.raw, b.raw))
fun max(a: FixDecMicro32, b: FixDecMicro32) = FixDecMicro32.raw(kotlin.math.max(a.raw, b.raw))
