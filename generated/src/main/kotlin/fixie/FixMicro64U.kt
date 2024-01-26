// Generated by fixie at 26-01-2024 14:35
package fixie

import java.lang.Math.*

private const val RAW_ONE = 1048576uL

@JvmInline
value class FixMicro64U private constructor(val raw: ULong) : Comparable<FixMicro64U> {

	@Throws(FixedPointException::class)
	fun toInt(): Int {
		val uncheckedResult = raw / RAW_ONE
		if (uncheckedResult > Int.MAX_VALUE.toUInt()) throw FixedPointException("$uncheckedResult to too large")
		return uncheckedResult.toInt()
	}

	fun toLong() = (raw / RAW_ONE).toLong()

	fun toFloat() = toDouble().toFloat()

	fun toDouble() = raw.toDouble() / RAW_ONE.toDouble()

	@Throws(FixedPointException::class)
	operator fun plus(right: FixMicro64U): FixMicro64U {
		try {
			return FixMicro64U(addExact(this.raw, right.raw))
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
	operator fun minus(right: FixMicro64U): FixMicro64U {
		try {
			return FixMicro64U(subtractExact(this.raw, right.raw))
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
	operator fun times(right: FixMicro64U): FixMicro64U {
		val highProductBits = multiplyHigh(this.raw, right.raw)
		val lowProductBits = this.raw * right.raw
		if (highProductBits >= (1u shl 20)) {
			throw FixedPointException("Can't represent $this * $right")
		}
		return FixMicro64U((lowProductBits shr 20) or (highProductBits shl 44))
	}

	@Throws(FixedPointException::class)
	operator fun times(right: Int) = this * from(right)

	@Throws(FixedPointException::class)
	operator fun times(right: Long) = this * from(right)

	@Throws(FixedPointException::class)
	operator fun times(right: Float) = this * from(right)

	@Throws(FixedPointException::class)
	operator fun times(right: Double) = this * from(right)

	override operator fun compareTo(other: FixMicro64U) = this.raw.compareTo(other.raw)

	companion object {

		val ZERO = from(0)
		val ONE = from(1)

		fun raw(rawValue: ULong) = FixMicro64U(rawValue)

		@Throws(FixedPointException::class)
		fun from(value: Int): FixMicro64U {
			try {
				return FixMicro64U(multiplyExact(value, RAW_ONE))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

		@Throws(FixedPointException::class)
		fun from(value: Long): FixMicro64U {
			try {
				return FixMicro64U(multiplyExact(value, RAW_ONE))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

		fun from(value: Float) = from(value.toDouble())

		@Throws(FixedPointException::class)
		fun from(value: Double): FixMicro64U {
			val doubleValue = RAW_ONE.toDouble() * value
			if (doubleValue > ULong.MAX_VALUE.toDouble() || doubleValue < ULong.MIN_VALUE.toDouble()) {
				throw FixedPointException("Can't represent $value")
			}
			return FixMicro64U(kotlin.math.floor(doubleValue + 0.5).toULong())
		}
	}

	@JvmInline
	@OptIn(ExperimentalUnsignedTypes::class)
	value class Array private constructor(val raw: ULongArray) {

		constructor(size: Int) : this(ULongArray(size))

		operator fun get(index: Int) = FixMicro64U(raw[index])

		operator fun set(index: Int, value: FixMicro64U) {
			raw[index] = value.raw
		}

		fun fill(value: FixMicro64U) {
			raw.fill(value.raw)
		}
	}
}

@Throws(FixedPointException::class)
operator fun Int.plus(right: FixMicro64U) = FixMicro64U.from(this) + right

@Throws(FixedPointException::class)
operator fun Long.plus(right: FixMicro64U) = FixMicro64U.from(this) + right

@Throws(FixedPointException::class)
operator fun Float.plus(right: FixMicro64U) = FixMicro64U.from(this) + right

@Throws(FixedPointException::class)
operator fun Double.plus(right: FixMicro64U) = FixMicro64U.from(this) + right

@Throws(FixedPointException::class)
operator fun Int.minus(right: FixMicro64U) = FixMicro64U.from(this) - right

@Throws(FixedPointException::class)
operator fun Long.minus(right: FixMicro64U) = FixMicro64U.from(this) - right

@Throws(FixedPointException::class)
operator fun Float.minus(right: FixMicro64U) = FixMicro64U.from(this) - right

@Throws(FixedPointException::class)
operator fun Double.minus(right: FixMicro64U) = FixMicro64U.from(this) - right

@Throws(FixedPointException::class)
operator fun Int.times(right: FixMicro64U) = FixMicro64U.from(this) * right

@Throws(FixedPointException::class)
operator fun Long.times(right: FixMicro64U) = FixMicro64U.from(this) * right

@Throws(FixedPointException::class)
operator fun Float.times(right: FixMicro64U) = FixMicro64U.from(this) * right

@Throws(FixedPointException::class)
operator fun Double.times(right: FixMicro64U) = FixMicro64U.from(this) * right
fun min(a: FixMicro64U, b: FixMicro64U) = FixMicro64U.raw(kotlin.math.min(a.raw, b.raw))
fun max(a: FixMicro64U, b: FixMicro64U) = FixMicro64U.raw(kotlin.math.max(a.raw, b.raw))