// Generated by fixie at 26-01-2024 14:35
package fixie

import java.lang.Math.*

private const val RAW_ONE: UByte = 30u

@JvmInline
value class FixCenti8U private constructor(val raw: UByte) : Comparable<FixCenti8U> {

	fun toInt() = (raw / RAW_ONE).toInt()

	fun toLong() = (raw / RAW_ONE).toLong()

	fun toFloat() = toDouble().toFloat()

	fun toDouble() = raw.toDouble() / RAW_ONE.toDouble()

	@Throws(FixedPointException::class)
	operator fun plus(right: FixCenti8U): FixCenti8U {
		try {
			return FixCenti8U(addExact(this.raw, right.raw))
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
	operator fun minus(right: FixCenti8U): FixCenti8U {
		try {
			return FixCenti8U(subtractExact(this.raw, right.raw))
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
	operator fun times(right: FixCenti8U): FixCenti8U {
		val largeValue = this.raw.toUShort() * right.raw.toUShort()
		try {
			return FixCenti8U(toUByteExact(largeValue / RAW_ONE))
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

	override operator fun compareTo(other: FixCenti8U) = this.raw.compareTo(other.raw)

	companion object {

		val ZERO = from(0)
		val ONE = from(1)

		fun raw(rawValue: UByte) = FixCenti8U(rawValue)

		@Throws(FixedPointException::class)
		fun from(value: Byte): FixCenti8U {
			try {
				return FixCenti8U(multiplyExact(value, RAW_ONE))
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
		fun from(value: Double): FixCenti8U {
			val doubleValue = RAW_ONE.toDouble() * value
			if (doubleValue > UByte.MAX_VALUE.toDouble() || doubleValue < UByte.MIN_VALUE.toDouble()) {
				throw FixedPointException("Can't represent $value")
			}
			return FixCenti8U(kotlin.math.floor(doubleValue + 0.5).toUInt().toUByte())
		}
	}

	@JvmInline
	@OptIn(ExperimentalUnsignedTypes::class)
	value class Array private constructor(val raw: UByteArray) {

		constructor(size: Int) : this(UByteArray(size))

		operator fun get(index: Int) = FixCenti8U(raw[index])

		operator fun set(index: Int, value: FixCenti8U) {
			raw[index] = value.raw
		}

		fun fill(value: FixCenti8U) {
			raw.fill(value.raw)
		}
	}
}

@Throws(FixedPointException::class)
operator fun Int.plus(right: FixCenti8U) = FixCenti8U.from(this) + right

@Throws(FixedPointException::class)
operator fun Long.plus(right: FixCenti8U) = FixCenti8U.from(this) + right

@Throws(FixedPointException::class)
operator fun Float.plus(right: FixCenti8U) = FixCenti8U.from(this) + right

@Throws(FixedPointException::class)
operator fun Double.plus(right: FixCenti8U) = FixCenti8U.from(this) + right

@Throws(FixedPointException::class)
operator fun Int.minus(right: FixCenti8U) = FixCenti8U.from(this) - right

@Throws(FixedPointException::class)
operator fun Long.minus(right: FixCenti8U) = FixCenti8U.from(this) - right

@Throws(FixedPointException::class)
operator fun Float.minus(right: FixCenti8U) = FixCenti8U.from(this) - right

@Throws(FixedPointException::class)
operator fun Double.minus(right: FixCenti8U) = FixCenti8U.from(this) - right

@Throws(FixedPointException::class)
operator fun Int.times(right: FixCenti8U) = FixCenti8U.from(this) * right

@Throws(FixedPointException::class)
operator fun Long.times(right: FixCenti8U) = FixCenti8U.from(this) * right

@Throws(FixedPointException::class)
operator fun Float.times(right: FixCenti8U) = FixCenti8U.from(this) * right

@Throws(FixedPointException::class)
operator fun Double.times(right: FixCenti8U) = FixCenti8U.from(this) * right