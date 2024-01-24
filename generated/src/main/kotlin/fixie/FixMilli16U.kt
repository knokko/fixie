// Generated by fixie at 24-01-2024 22:18
package fixie

import java.lang.Math.*

private const val RAW_ONE: UShort = 1000u

@JvmInline
value class FixMilli16U private constructor(val raw: UShort) : Comparable<FixMilli16U> {

	fun toInt() = (raw / RAW_ONE).toInt()

	fun toLong() = (raw / RAW_ONE).toLong()

	fun toFloat() = toDouble().toFloat()

	fun toDouble() = raw.toDouble() / RAW_ONE.toDouble()

	@Throws(FixedPointException::class)
	operator fun plus(right: FixMilli16U): FixMilli16U {
		try {
			return FixMilli16U(addExact(this.raw, right.raw))
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
	operator fun minus(right: FixMilli16U): FixMilli16U {
		try {
			return FixMilli16U(subtractExact(this.raw, right.raw))
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
	operator fun times(right: FixMilli16U): FixMilli16U {
		val largeValue = this.raw.toUInt() * right.raw.toUInt()
		try {
			return FixMilli16U(toUShortExact(largeValue / RAW_ONE))
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

	override operator fun compareTo(other: FixMilli16U) = this.raw.compareTo(other.raw)

	companion object {

		val ZERO = from(0)
		val ONE = from(1)

		fun raw(rawValue: UShort) = FixMilli16U(rawValue)

		@Throws(FixedPointException::class)
		fun from(value: Short): FixMilli16U {
			try {
				return FixMilli16U(multiplyExact(value, RAW_ONE))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

		@Throws(FixedPointException::class)
		fun from(value: Int) = from(toShortExact(value))

		@Throws(FixedPointException::class)
		fun from(value: Long) = from(toShortExact(value))

		fun from(value: Float) = from(value.toDouble())

		@Throws(FixedPointException::class)
		fun from(value: Double): FixMilli16U {
			val doubleValue = RAW_ONE.toDouble() * value
			if (doubleValue > UShort.MAX_VALUE.toDouble() || doubleValue < UShort.MIN_VALUE.toDouble()) {
				throw FixedPointException("Can't represent $value")
			}
			return FixMilli16U(kotlin.math.floor(doubleValue + 0.5).toUInt().toUShort())
		}
	}
}

@Throws(FixedPointException::class)
operator fun Int.plus(right: FixMilli16U) = FixMilli16U.from(this) + right

@Throws(FixedPointException::class)
operator fun Long.plus(right: FixMilli16U) = FixMilli16U.from(this) + right

@Throws(FixedPointException::class)
operator fun Float.plus(right: FixMilli16U) = FixMilli16U.from(this) + right

@Throws(FixedPointException::class)
operator fun Double.plus(right: FixMilli16U) = FixMilli16U.from(this) + right

@Throws(FixedPointException::class)
operator fun Int.minus(right: FixMilli16U) = FixMilli16U.from(this) - right

@Throws(FixedPointException::class)
operator fun Long.minus(right: FixMilli16U) = FixMilli16U.from(this) - right

@Throws(FixedPointException::class)
operator fun Float.minus(right: FixMilli16U) = FixMilli16U.from(this) - right

@Throws(FixedPointException::class)
operator fun Double.minus(right: FixMilli16U) = FixMilli16U.from(this) - right

@Throws(FixedPointException::class)
operator fun Int.times(right: FixMilli16U) = FixMilli16U.from(this) * right

@Throws(FixedPointException::class)
operator fun Long.times(right: FixMilli16U) = FixMilli16U.from(this) * right

@Throws(FixedPointException::class)
operator fun Float.times(right: FixMilli16U) = FixMilli16U.from(this) * right

@Throws(FixedPointException::class)
operator fun Double.times(right: FixMilli16U) = FixMilli16U.from(this) * right
