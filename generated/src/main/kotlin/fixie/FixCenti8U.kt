// Generated by fixie at 28-01-2024 15:03
package fixie

import java.math.BigInteger
import java.lang.Math.*

private const val RAW_ONE: UByte = 30u

@JvmInline
value class FixCenti8U private constructor(val raw: UByte) : Comparable<FixCenti8U> {

	override fun toString(): String {
		val intPart = raw / RAW_ONE
		val bigFract = BigInteger.valueOf((raw % RAW_ONE).toLong()) * BigInteger.valueOf(10)
		val results = bigFract.divideAndRemainder(BigInteger.valueOf(RAW_ONE.toLong()))
		var fractNumber = results[0]
		if (results[1] >= BigInteger.valueOf(15)) fractNumber += BigInteger.ONE
		var fractPart = "." + fractNumber.toString().replace("-", "")
		while (fractPart.endsWith('0')) fractPart = fractPart.substring(0 until fractPart.length - 1)
		if (fractPart == ".") fractPart = ""
		return "$intPart$fractPart"
	}

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
		return FixCenti8U(toUByteExact(largeValue / RAW_ONE))
	}

	@Throws(FixedPointException::class)
	operator fun times(right: Int): FixCenti8U {
		if (right < 0) throw FixedPointException("Negative numbers are not supported")
		try {
			return FixCenti8U(toUByteExact(multiplyExact(raw.toUInt(), right.toUInt())))
		} catch (overflow: ArithmeticException) {
			throw FixedPointException("Can't represent $this * $right")
		}
	}

	@Throws(FixedPointException::class)
	operator fun times(right: Long): FixCenti8U {
		if (right < 0) throw FixedPointException("Negative numbers are not supported")
		try {
			return FixCenti8U(toUByteExact(multiplyExact(raw.toULong(), right.toULong())))
		} catch (overflow: ArithmeticException) {
			throw FixedPointException("Can't represent $this * $right")
		}
	}

	@Throws(FixedPointException::class)
	operator fun times(right: Float) = this * from(right)

	@Throws(FixedPointException::class)
	operator fun times(right: Double) = this * from(right)

	@Throws(FixedPointException::class)
	operator fun div(right: FixCenti8U): FixCenti8U {
		val largeValue = this.raw.toUShort() * RAW_ONE.toUShort()
		try {
			return FixCenti8U(toUByteExact(largeValue / right.raw))
		} catch (overflow: ArithmeticException) {
			throw FixedPointException("Can't represent $this / $right")
		}
	}

	@Throws(FixedPointException::class)
	operator fun div(right: Int): FixCenti8U {
		if (right <= 0) {
			throw FixedPointException("Can't represent $this / $right")
		}
		return FixCenti8U(toUByteExact(raw.toUInt() / right.toUInt()))
	}

	@Throws(FixedPointException::class)
	operator fun div(right: Long): FixCenti8U {
		if (right <= 0L) {
			throw FixedPointException("Can't represent $this / $right")
		}
		return FixCenti8U(toUByteExact(raw.toULong() / right.toULong()))
	}

	@Throws(FixedPointException::class)
	operator fun div(right: Float) = this / from(right)

	@Throws(FixedPointException::class)
	operator fun div(right: Double) = this / from(right)

	override operator fun compareTo(other: FixCenti8U) = this.raw.compareTo(other.raw)

	operator fun compareTo(other: Byte) = if (other < 0) 1 else if (other > 8) -1 else this.compareTo(from(other))

	operator fun compareTo(other: UByte) = if (other > 8u) -1 else this.compareTo(from(other))

	operator fun compareTo(other: Int) = if (other < 0) 1 else if (other > 8) -1 else this.compareTo(from(other))

	operator fun compareTo(other: UInt) = if (other > 8u) -1 else this.compareTo(from(other))

	operator fun compareTo(other: Long) = if (other < 0) 1 else if (other > 8) -1 else this.compareTo(from(other))

	operator fun compareTo(other: ULong) = if (other > 8u) -1 else this.compareTo(from(other))
	operator fun compareTo(other: Float) = if (other < 0.0f) 1 else if (other > 8.5f) -1 else this.compareTo(from(other))
	operator fun compareTo(other: Double) = if (other < 0.0) 1 else if (other > 8.5) -1 else this.compareTo(from(other))

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
		fun from(value: Int): FixCenti8U {
			try {
				return from(toByteExact(value))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

		@Throws(FixedPointException::class)
		fun from(value: Long): FixCenti8U {
			try {
				return from(toByteExact(value))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

		@Throws(FixedPointException::class)
		fun from(value: UByte): FixCenti8U {
			try {
				return FixCenti8U(multiplyExact(value, RAW_ONE))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

		@Throws(FixedPointException::class)
		fun from(value: UInt): FixCenti8U {
			try {
				return from(toUByteExact(value))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

		@Throws(FixedPointException::class)
		fun from(value: ULong): FixCenti8U {
			try {
				return from(toUByteExact(value))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

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

		constructor(size: Int, initializer: (Int) -> FixCenti8U) : this(UByteArray(size) { index -> initializer(index).raw })

		val size: Int
			get() = raw.size

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

@Throws(FixedPointException::class)
operator fun Int.div(right: FixCenti8U) = FixCenti8U.from(this) / right

@Throws(FixedPointException::class)
operator fun Long.div(right: FixCenti8U) = FixCenti8U.from(this) / right

@Throws(FixedPointException::class)
operator fun Float.div(right: FixCenti8U) = FixCenti8U.from(this) / right

@Throws(FixedPointException::class)
operator fun Double.div(right: FixCenti8U) = FixCenti8U.from(this) / right

operator fun Int.compareTo(right: FixCenti8U) = FixCenti8U.from(this).compareTo(right)

operator fun Long.compareTo(right: FixCenti8U) = FixCenti8U.from(this).compareTo(right)

operator fun Float.compareTo(right: FixCenti8U) = FixCenti8U.from(this).compareTo(right)

operator fun Double.compareTo(right: FixCenti8U) = FixCenti8U.from(this).compareTo(right)

fun min(a: FixCenti8U, b: FixCenti8U) = FixCenti8U.raw(min(a.raw, b.raw))

fun max(a: FixCenti8U, b: FixCenti8U) = FixCenti8U.raw(max(a.raw, b.raw))
