// Generated by fixie at 28-01-2024 15:03
package fixie

import java.math.BigInteger
import java.lang.Math.*

private const val RAW_ONE: Byte = 30

@JvmInline
value class FixCenti8 private constructor(val raw: Byte) : Comparable<FixCenti8> {

	override fun toString(): String {
		val intPart = raw / RAW_ONE
		val bigFract = BigInteger.valueOf((raw % RAW_ONE).toLong()) * BigInteger.valueOf(10)
		val results = bigFract.divideAndRemainder(BigInteger.valueOf(RAW_ONE.toLong()))
		var fractNumber = results[0]
		if (results[1] >= BigInteger.valueOf(15)) fractNumber += BigInteger.ONE
		var fractPart = "." + fractNumber.toString().replace("-", "")
		while (fractPart.endsWith('0')) fractPart = fractPart.substring(0 until fractPart.length - 1)
		if (fractPart == ".") fractPart = ""
		val minus = if (raw < 0 && intPart == 0) "-" else ""
		return "$minus$intPart$fractPart"
	}

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
		return FixCenti8(toByteExact(largeValue / RAW_ONE))
	}

	@Throws(FixedPointException::class)
	operator fun times(right: Int): FixCenti8 {
		try {
			return FixCenti8(toByteExact(multiplyExact(raw.toInt(), right)))
		} catch (overflow: ArithmeticException) {
			throw FixedPointException("Can't represent $this * $right")
		}
	}

	@Throws(FixedPointException::class)
	operator fun times(right: Long): FixCenti8 {
		try {
			return FixCenti8(toByteExact(multiplyExact(raw.toLong(), right)))
		} catch (overflow: ArithmeticException) {
			throw FixedPointException("Can't represent $this * $right")
		}
	}

	@Throws(FixedPointException::class)
	operator fun times(right: Float) = this * from(right)

	@Throws(FixedPointException::class)
	operator fun times(right: Double) = this * from(right)

	@Throws(FixedPointException::class)
	operator fun div(right: FixCenti8): FixCenti8 {
		val largeValue = this.raw.toShort() * RAW_ONE.toShort()
		try {
			return FixCenti8(toByteExact(largeValue / right.raw))
		} catch (overflow: ArithmeticException) {
			throw FixedPointException("Can't represent $this / $right")
		}
	}

	@Throws(FixedPointException::class)
	operator fun div(right: Int): FixCenti8 {
		if (right == 0 || (raw == Byte.MIN_VALUE && right == -1)) {
			throw FixedPointException("Can't represent $this / $right")
		}
		return FixCenti8(toByteExact(raw.toInt() / right))
	}

	@Throws(FixedPointException::class)
	operator fun div(right: Long): FixCenti8 {
		if (right == 0L || (raw == Byte.MIN_VALUE && right == -1L)) {
			throw FixedPointException("Can't represent $this / $right")
		}
		return FixCenti8(toByteExact(raw.toLong() / right))
	}

	@Throws(FixedPointException::class)
	operator fun div(right: Float) = this / from(right)

	@Throws(FixedPointException::class)
	operator fun div(right: Double) = this / from(right)

	override operator fun compareTo(other: FixCenti8) = this.raw.compareTo(other.raw)

	operator fun compareTo(other: Byte) = if (other < -4) 1 else if (other > 4) -1 else this.compareTo(from(other))

	operator fun compareTo(other: Int) = if (other < -4) 1 else if (other > 4) -1 else this.compareTo(from(other))

	operator fun compareTo(other: Long) = if (other < -4) 1 else if (other > 4) -1 else this.compareTo(from(other))
	operator fun compareTo(other: Float) = if (other < -4.266667f) 1 else if (other > 4.233333f) -1 else this.compareTo(from(other))
	operator fun compareTo(other: Double) = if (other < -4.266666666666667) 1 else if (other > 4.233333333333333) -1 else this.compareTo(from(other))

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
		fun from(value: Int): FixCenti8 {
			try {
				return from(toByteExact(value))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

		@Throws(FixedPointException::class)
		fun from(value: Long): FixCenti8 {
			try {
				return from(toByteExact(value))
			} catch (overflow: ArithmeticException) {
				throw FixedPointException("Can't represent $value")
			}
		}

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

		constructor(size: Int, initializer: (Int) -> FixCenti8) : this(ByteArray(size) { index -> initializer(index).raw })

		val size: Int
			get() = raw.size

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

@Throws(FixedPointException::class)
operator fun Int.div(right: FixCenti8) = FixCenti8.from(this) / right

@Throws(FixedPointException::class)
operator fun Long.div(right: FixCenti8) = FixCenti8.from(this) / right

@Throws(FixedPointException::class)
operator fun Float.div(right: FixCenti8) = FixCenti8.from(this) / right

@Throws(FixedPointException::class)
operator fun Double.div(right: FixCenti8) = FixCenti8.from(this) / right

operator fun Int.compareTo(right: FixCenti8) = FixCenti8.from(this).compareTo(right)

operator fun Long.compareTo(right: FixCenti8) = FixCenti8.from(this).compareTo(right)

operator fun Float.compareTo(right: FixCenti8) = FixCenti8.from(this).compareTo(right)

operator fun Double.compareTo(right: FixCenti8) = FixCenti8.from(this).compareTo(right)

fun abs(value: FixCenti8) = FixCenti8.raw(abs(value.raw))

fun min(a: FixCenti8, b: FixCenti8) = FixCenti8.raw(min(a.raw, b.raw))

fun max(a: FixCenti8, b: FixCenti8) = FixCenti8.raw(max(a.raw, b.raw))
