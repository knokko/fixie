// Generated by fixie at 26-01-2024 14:35
package fixie

import java.lang.Math.*

private const val RAW_ONE: Byte = 30

@JvmInline
value class FixCenti8 private constructor(val raw: Byte) : Comparable<FixCenti8> {

	fun toInt() = raw / RAW_ONE

	fun toLong() = (raw / RAW_ONE).toLong()

	fun toFloat() = toDouble().toFloat()

	fun toDouble() = raw.toDouble() / RAW_ONE.toDouble()

	@Throws(FixedPointException::class)
	operator fun unaryMinus() = if (raw != Byte.MIN_VALUE)
		FixCenti8((-raw).toByte()) else throw FixedPointException("Can't negate MIN_VALUE")

	@Throws(FixedPointException::class)
	operator fun plus(right: FixCenti8): FixCenti8 {
		try {
			return FixCenti8(addExact(this.raw, right.raw))
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
	operator fun minus(right: FixCenti8): FixCenti8 {
		try {
			return FixCenti8(subtractExact(this.raw, right.raw))
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
	operator fun times(right: FixCenti8): FixCenti8 {
		val largeValue = this.raw.toShort() * right.raw.toShort()
		try {
			return FixCenti8(toByteExact(largeValue / RAW_ONE))
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

	override operator fun compareTo(other: FixCenti8) = this.raw.compareTo(other.raw)

	companion object {

		val ZERO = from(0)
		val ONE = from(1)

		fun raw(rawValue: Byte) = FixCenti8(rawValue)

		@Throws(FixedPointException::class)
		fun from(value: Byte): FixCenti8 {
			try {
				return FixCenti8(multiplyExact(value, RAW_ONE))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

		@Throws(FixedPointException::class)
		fun from(value: Int) = from(toByteExact(value))

		@Throws(FixedPointException::class)
		fun from(value: Long) = from(toByteExact(value))

		fun from(value: Float) = from(value.toDouble())

		@Throws(FixedPointException::class)
		fun from(value: Double): FixCenti8 {
			val doubleValue = RAW_ONE.toDouble() * value
			if (doubleValue > Byte.MAX_VALUE.toDouble() || doubleValue < Byte.MIN_VALUE.toDouble()) {
				throw FixedPointException("Can't represent $value")
			}
			return FixCenti8(round(doubleValue).toByte())
		}
	}

	@JvmInline
	value class Array private constructor(val raw: ByteArray) {

		constructor(size: Int) : this(ByteArray(size))

		operator fun get(index: Int) = FixCenti8(raw[index])

		operator fun set(index: Int, value: FixCenti8) {
			raw[index] = value.raw
		}

		fun fill(value: FixCenti8) {
			raw.fill(value.raw)
		}
	}
}

@Throws(FixedPointException::class)
operator fun Int.plus(right: FixCenti8) = FixCenti8.from(this) + right

@Throws(FixedPointException::class)
operator fun Long.plus(right: FixCenti8) = FixCenti8.from(this) + right

@Throws(FixedPointException::class)
operator fun Float.plus(right: FixCenti8) = FixCenti8.from(this) + right

@Throws(FixedPointException::class)
operator fun Double.plus(right: FixCenti8) = FixCenti8.from(this) + right

@Throws(FixedPointException::class)
operator fun Int.minus(right: FixCenti8) = FixCenti8.from(this) - right

@Throws(FixedPointException::class)
operator fun Long.minus(right: FixCenti8) = FixCenti8.from(this) - right

@Throws(FixedPointException::class)
operator fun Float.minus(right: FixCenti8) = FixCenti8.from(this) - right

@Throws(FixedPointException::class)
operator fun Double.minus(right: FixCenti8) = FixCenti8.from(this) - right

@Throws(FixedPointException::class)
operator fun Int.times(right: FixCenti8) = FixCenti8.from(this) * right

@Throws(FixedPointException::class)
operator fun Long.times(right: FixCenti8) = FixCenti8.from(this) * right

@Throws(FixedPointException::class)
operator fun Float.times(right: FixCenti8) = FixCenti8.from(this) * right

@Throws(FixedPointException::class)
operator fun Double.times(right: FixCenti8) = FixCenti8.from(this) * right
