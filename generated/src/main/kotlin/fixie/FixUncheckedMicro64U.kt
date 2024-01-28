// Generated by fixie at 28-01-2024 22:31
package fixie

import java.math.BigInteger
private const val RAW_ONE = 1048576uL

@JvmInline
value class FixUncheckedMicro64U private constructor(val raw: ULong) : Comparable<FixUncheckedMicro64U> {

	override fun toString(): String {
		val intPart = raw / RAW_ONE
		val bigFract = BigInteger.valueOf((raw % RAW_ONE).toLong()) * BigInteger.valueOf(1000000)
		val results = bigFract.divideAndRemainder(BigInteger.valueOf(RAW_ONE.toLong()))
		var fractNumber = results[0]
		if (results[1] >= BigInteger.valueOf(524288)) fractNumber += BigInteger.ONE
		var fractPart = "." + fractNumber.toString().replace("-", "")
		while (fractPart.endsWith('0')) fractPart = fractPart.substring(0 until fractPart.length - 1)
		if (fractPart == ".") fractPart = ""
		return "$intPart$fractPart"
	}

	fun toInt() = (raw / RAW_ONE).toInt()

	fun toLong() = (raw / RAW_ONE).toLong()

	fun toFloat() = toDouble().toFloat()

	fun toDouble() = raw.toDouble() / RAW_ONE.toDouble()

	operator fun plus(right: FixUncheckedMicro64U) = FixUncheckedMicro64U(this.raw + right.raw)

	operator fun plus(right: Int) = this + from(right)

	operator fun plus(right: Long) = this + from(right)

	operator fun plus(right: Float) = this + from(right)

	operator fun plus(right: Double) = this + from(right)

	operator fun minus(right: FixUncheckedMicro64U) = FixUncheckedMicro64U(this.raw - right.raw)

	operator fun minus(right: Int) = this - from(right)

	operator fun minus(right: Long) = this - from(right)

	operator fun minus(right: Float) = this - from(right)

	operator fun minus(right: Double) = this - from(right)

	operator fun times(right: FixUncheckedMicro64U): FixUncheckedMicro64U {
		val highProductBits = multiplyHigh(this.raw, right.raw)
		val lowProductBits = this.raw * right.raw
		return FixUncheckedMicro64U((lowProductBits shr 20) or (highProductBits shl 44))
	}

	operator fun times(right: Int) = FixUncheckedMicro64U(raw * right.toULong())

	operator fun times(right: Long) = FixUncheckedMicro64U(raw * right.toULong())

	operator fun times(right: Float) = this * from(right)

	operator fun times(right: Double) = this * from(right)

	operator fun div(right: FixUncheckedMicro64U): FixUncheckedMicro64U {
		val highProductBits = this.raw shr 44
		val lowProductBits = this.raw shl 20
		return if (highProductBits == 0uL) {
			FixUncheckedMicro64U(lowProductBits / right.raw)
		} else {
			val result = (uLongToBigInteger(this.raw) * uLongToBigInteger(RAW_ONE)) / uLongToBigInteger(right.raw)
			FixUncheckedMicro64U(bigIntegerToULong(result, false))
		}
	}

	operator fun div(right: Int) = FixUncheckedMicro64U(raw / right.toULong())

	operator fun div(right: Long) = FixUncheckedMicro64U(raw / right.toULong())

	operator fun div(right: Float) = this / from(right)

	operator fun div(right: Double) = this / from(right)

	override operator fun compareTo(other: FixUncheckedMicro64U) = this.raw.compareTo(other.raw)

	operator fun compareTo(other: Int) = if (other < 0) 1 else this.compareTo(from(other))

	operator fun compareTo(other: UInt) = this.compareTo(from(other))

	operator fun compareTo(other: Long) = if (other < 0) 1 else if (other > 17592186044415) -1 else this.compareTo(from(other))

	operator fun compareTo(other: ULong) = if (other > 17592186044415u) -1 else this.compareTo(from(other))
	operator fun compareTo(other: Float) = if (other < 0.0f) 1 else if (other > 1.7592186E13f) -1 else this.compareTo(from(other))
	operator fun compareTo(other: Double) = if (other < 0.0) 1 else if (other > 1.7592186044416E13) -1 else this.compareTo(from(other))

	companion object {

		val ZERO = from(0)
		val ONE = from(1)

		fun raw(rawValue: ULong) = FixUncheckedMicro64U(rawValue)

		fun from(value: Int) = FixUncheckedMicro64U(value.toULong() * RAW_ONE)

		fun from(value: Long) = FixUncheckedMicro64U(value.toULong() * RAW_ONE)

		fun from(value: UInt) = FixUncheckedMicro64U(value.toULong() * RAW_ONE)

		fun from(value: ULong) = FixUncheckedMicro64U(value * RAW_ONE)

		fun from(value: Float) = from(value.toDouble())

		fun from(value: Double) = FixUncheckedMicro64U(kotlin.math.floor(RAW_ONE.toDouble() * value + 0.5).toULong())
	}

	@JvmInline
	@OptIn(ExperimentalUnsignedTypes::class)
	value class Array private constructor(val raw: ULongArray) {

		constructor(size: Int) : this(ULongArray(size))

		constructor(size: Int, initializer: (Int) -> FixUncheckedMicro64U) : this(ULongArray(size) { index -> initializer(index).raw })

		val size: Int
			get() = raw.size

		operator fun get(index: Int) = FixUncheckedMicro64U(raw[index])

		operator fun set(index: Int, value: FixUncheckedMicro64U) {
			raw[index] = value.raw
		}

		fun fill(value: FixUncheckedMicro64U) {
			raw.fill(value.raw)
		}
	}
}

operator fun Int.plus(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) + right

operator fun Long.plus(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) + right

operator fun Float.plus(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) + right

operator fun Double.plus(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) + right

operator fun Int.minus(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) - right

operator fun Long.minus(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) - right

operator fun Float.minus(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) - right

operator fun Double.minus(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) - right

operator fun Int.times(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) * right

operator fun Long.times(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) * right

operator fun Float.times(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) * right

operator fun Double.times(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) * right

operator fun Int.div(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) / right

operator fun Long.div(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) / right

operator fun Float.div(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) / right

operator fun Double.div(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this) / right

operator fun Int.compareTo(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this).compareTo(right)

operator fun Long.compareTo(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this).compareTo(right)

operator fun Float.compareTo(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this).compareTo(right)

operator fun Double.compareTo(right: FixUncheckedMicro64U) = FixUncheckedMicro64U.from(this).compareTo(right)

fun min(a: FixUncheckedMicro64U, b: FixUncheckedMicro64U) = FixUncheckedMicro64U.raw(kotlin.math.min(a.raw, b.raw))

fun max(a: FixUncheckedMicro64U, b: FixUncheckedMicro64U) = FixUncheckedMicro64U.raw(kotlin.math.max(a.raw, b.raw))
