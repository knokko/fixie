// Generated by fixie at 01-02-2024 23:28
package fixie

import java.math.BigInteger
import java.lang.Math.*

private const val RAW_ONE = 1000000u

@JvmInline
value class FixDecMicro32U private constructor(val raw: UInt) : Comparable<FixDecMicro32U> {

	override fun toString(): String {
		val intPart = raw / RAW_ONE
		var fractPart = (raw % RAW_ONE).toString().replace("-", "")
		while (fractPart.length < 6) fractPart = "0$fractPart"
		fractPart = ".$fractPart"
		while (fractPart.endsWith('0')) fractPart = fractPart.substring(0 until fractPart.length - 1)
		if (fractPart == ".") fractPart = ""
		return "$intPart$fractPart"
	}

	fun toInt() = (raw / RAW_ONE).toInt()

	fun toLong() = (raw / RAW_ONE).toLong()

	fun toFloat() = toDouble().toFloat()

	fun toDouble() = raw.toDouble() / RAW_ONE.toDouble()

	@Throws(FixedPointException::class)
	operator fun plus(right: FixDecMicro32U): FixDecMicro32U {
		try {
			return FixDecMicro32U(addExact(this.raw, right.raw))
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
	operator fun minus(right: FixDecMicro32U): FixDecMicro32U {
		try {
			return FixDecMicro32U(subtractExact(this.raw, right.raw))
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
	operator fun times(right: FixDecMicro32U): FixDecMicro32U {
		val largeValue = this.raw.toULong() * right.raw.toULong()
		try {
			return FixDecMicro32U(toUIntExact(largeValue / RAW_ONE))
		} catch (overflow: ArithmeticException) {
			throw FixedPointException("Can't represent $this * $right")
		}
	}

	@Throws(FixedPointException::class)
	operator fun times(right: Int): FixDecMicro32U {
		if (right < 0) throw FixedPointException("Negative numbers are not supported")
		try {
			return FixDecMicro32U(multiplyExact(raw, right.toUInt()))
		} catch (overflow: ArithmeticException) {
			throw FixedPointException("Can't represent $this * $right")
		}
	}

	@Throws(FixedPointException::class)
	operator fun times(right: Long): FixDecMicro32U {
		if (right < 0) throw FixedPointException("Negative numbers are not supported")
		try {
			return FixDecMicro32U(toUIntExact(multiplyExact(raw.toULong(), right.toULong())))
		} catch (overflow: ArithmeticException) {
			throw FixedPointException("Can't represent $this * $right")
		}
	}

	@Throws(FixedPointException::class)
	operator fun times(right: Float) = this * from(right)

	@Throws(FixedPointException::class)
	operator fun times(right: Double) = this * from(right)

	@Throws(FixedPointException::class)
	operator fun div(right: FixDecMicro32U): FixDecMicro32U {
		val largeValue = this.raw.toULong() * RAW_ONE.toULong()
		try {
			return FixDecMicro32U(toUIntExact(largeValue / right.raw))
		} catch (overflow: ArithmeticException) {
			throw FixedPointException("Can't represent $this / $right")
		}
	}

	@Throws(FixedPointException::class)
	operator fun div(right: Int): FixDecMicro32U {
		if (right <= 0) {
			throw FixedPointException("Can't represent $this / $right")
		}
		return FixDecMicro32U(raw / right.toUInt())
	}

	@Throws(FixedPointException::class)
	operator fun div(right: Long): FixDecMicro32U {
		if (right <= 0L) {
			throw FixedPointException("Can't represent $this / $right")
		}
		return FixDecMicro32U(toUIntExact(raw.toULong() / right.toULong()))
	}

	@Throws(FixedPointException::class)
	operator fun div(right: Float) = this / from(right)

	@Throws(FixedPointException::class)
	operator fun div(right: Double) = this / from(right)

	override operator fun compareTo(other: FixDecMicro32U) = this.raw.compareTo(other.raw)

	operator fun compareTo(other: Int) = if (other < 0) 1 else if (other > 4294) -1 else this.compareTo(from(other))

	operator fun compareTo(other: UInt) = if (other > 4294u) -1 else this.compareTo(from(other))

	operator fun compareTo(other: Long) = if (other < 0) 1 else if (other > 4294) -1 else this.compareTo(from(other))

	operator fun compareTo(other: ULong) = if (other > 4294u) -1 else this.compareTo(from(other))
	operator fun compareTo(other: Float) = if (other < 0.0f) 1 else if (other > 4294.9673f) -1 else this.compareTo(from(other))
	operator fun compareTo(other: Double) = if (other < 0.0) 1 else if (other > 4294.967295) -1 else this.compareTo(from(other))

	companion object {

		val ZERO = from(0)
		val ONE = from(1)

		fun raw(rawValue: UInt) = FixDecMicro32U(rawValue)

		@Throws(FixedPointException::class)
		fun from(value: Int): FixDecMicro32U {
			try {
				return FixDecMicro32U(multiplyExact(value, RAW_ONE))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

		@Throws(FixedPointException::class)
		fun from(value: Long): FixDecMicro32U {
			try {
				return from(toIntExact(value))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

		@Throws(FixedPointException::class)
		fun from(value: UInt): FixDecMicro32U {
			try {
				return FixDecMicro32U(multiplyExact(value, RAW_ONE))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

		@Throws(FixedPointException::class)
		fun from(value: ULong): FixDecMicro32U {
			try {
				return from(toUIntExact(value))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

		fun from(value: Float) = from(value.toDouble())

		@Throws(FixedPointException::class)
		fun from(value: Double): FixDecMicro32U {
			val doubleValue = RAW_ONE.toDouble() * value
			if (doubleValue > UInt.MAX_VALUE.toDouble() || doubleValue < UInt.MIN_VALUE.toDouble()) {
				throw FixedPointException("Can't represent $value")
			}
			return FixDecMicro32U(kotlin.math.floor(doubleValue + 0.5).toUInt())
		}
	}

	@JvmInline
	@OptIn(ExperimentalUnsignedTypes::class)
	value class Array private constructor(val raw: UIntArray) {

		constructor(size: Int) : this(UIntArray(size))

		constructor(size: Int, initializer: (Int) -> FixDecMicro32U) : this(UIntArray(size) { index -> initializer(index).raw })

		val size: Int
			get() = raw.size

		operator fun get(index: Int) = FixDecMicro32U(raw[index])

		operator fun set(index: Int, value: FixDecMicro32U) {
			raw[index] = value.raw
		}

		fun fill(value: FixDecMicro32U) {
			raw.fill(value.raw)
		}
	}
}

@Throws(FixedPointException::class)
operator fun Int.plus(right: FixDecMicro32U) = FixDecMicro32U.from(this) + right

@Throws(FixedPointException::class)
operator fun Long.plus(right: FixDecMicro32U) = FixDecMicro32U.from(this) + right

@Throws(FixedPointException::class)
operator fun Float.plus(right: FixDecMicro32U) = FixDecMicro32U.from(this) + right

@Throws(FixedPointException::class)
operator fun Double.plus(right: FixDecMicro32U) = FixDecMicro32U.from(this) + right

@Throws(FixedPointException::class)
operator fun Int.minus(right: FixDecMicro32U) = FixDecMicro32U.from(this) - right

@Throws(FixedPointException::class)
operator fun Long.minus(right: FixDecMicro32U) = FixDecMicro32U.from(this) - right

@Throws(FixedPointException::class)
operator fun Float.minus(right: FixDecMicro32U) = FixDecMicro32U.from(this) - right

@Throws(FixedPointException::class)
operator fun Double.minus(right: FixDecMicro32U) = FixDecMicro32U.from(this) - right

@Throws(FixedPointException::class)
operator fun Int.times(right: FixDecMicro32U) = right * this

@Throws(FixedPointException::class)
operator fun Long.times(right: FixDecMicro32U) = right * this

@Throws(FixedPointException::class)
operator fun Float.times(right: FixDecMicro32U) = right * this

@Throws(FixedPointException::class)
operator fun Double.times(right: FixDecMicro32U) = right * this

@Throws(FixedPointException::class)
operator fun Int.div(right: FixDecMicro32U) = FixDecMicro32U.from(this) / right

@Throws(FixedPointException::class)
operator fun Long.div(right: FixDecMicro32U) = FixDecMicro32U.from(this) / right

@Throws(FixedPointException::class)
operator fun Float.div(right: FixDecMicro32U) = FixDecMicro32U.from(this) / right

@Throws(FixedPointException::class)
operator fun Double.div(right: FixDecMicro32U) = FixDecMicro32U.from(this) / right

operator fun Int.compareTo(right: FixDecMicro32U) = FixDecMicro32U.from(this).compareTo(right)

operator fun Long.compareTo(right: FixDecMicro32U) = FixDecMicro32U.from(this).compareTo(right)

operator fun Float.compareTo(right: FixDecMicro32U) = FixDecMicro32U.from(this).compareTo(right)

operator fun Double.compareTo(right: FixDecMicro32U) = FixDecMicro32U.from(this).compareTo(right)

fun min(a: FixDecMicro32U, b: FixDecMicro32U) = FixDecMicro32U.raw(kotlin.math.min(a.raw, b.raw))

fun max(a: FixDecMicro32U, b: FixDecMicro32U) = FixDecMicro32U.raw(kotlin.math.max(a.raw, b.raw))
