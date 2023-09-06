package fixie

import java.math.BigInteger
import kotlin.math.absoluteValue
import kotlin.math.roundToLong

private const val RAW_ONE = 1024L * 1024L * 1024L

@JvmInline
value class FixNano64 private constructor(val raw: Long): Comparable<FixNano64> {

    @Throws(ArithmeticException::class)
    fun toInt() = Math.toIntExact(toLong())

    fun toLong() = raw / RAW_ONE

    fun toFloat() = toDouble().toFloat()

    fun toDouble() = raw / RAW_ONE.toDouble()

    @Throws(FixedPointException::class)
    operator fun unaryMinus() = if (raw != Long.MIN_VALUE) FixNano64(-raw) else throw FixedPointException("negating Long.MIN_VALUE")

    @Throws(FixedPointException::class)
    operator fun plus(other: FixNano64): FixNano64 {
        try {
            return FixNano64(Math.addExact(raw, other.raw))
        } catch (overflow: ArithmeticException) {
            throw FixedPointException("Tried to compute $this + $other")
        }
    }

    @Throws(FixedPointException::class)
    operator fun minus(right: FixNano64): FixNano64 {
        try {
            return FixNano64(Math.subtractExact(raw, right.raw))
        } catch (overflow: ArithmeticException) {
            throw FixedPointException("Tried to compute $this - $right")
        }
    }

    operator fun times(other: Int) = this * other.toLong()

    operator fun times(other: Long): FixNano64 {
        try {
            return FixNano64(Math.multiplyExact(raw, other))
        } catch (overflow: ArithmeticException) {
            throw FixedPointException("Tried to compute $this * $other")
        }
    }

    operator fun times(other: Float) = this * fromFloat(other)

    operator fun times(other: Double) = this * fromDouble(other)

    operator fun times(other: FixNano64): FixNano64 {
        /*
         * Theoretically:
         *   this = this.raw * 2^-30
         *   other = other.raw * 2^-30
         *   result = this * other = this.raw * other.raw * 2^-60 -> result.raw = this.raw * other.raw * 2^-30
         *
         * Practically, computing this.raw * other.raw could overflow, so we need a more complicated approach...
         */

        val highProductBits = Math.multiplyHigh(this.raw, other.raw)
        if (highProductBits < -(1 shl 29) || highProductBits >= (1 shl 29)) {
            throw FixedPointException("Attempted to compute $this * $other")
        }

        val lowProductBits = this.raw * other.raw

        return FixNano64((lowProductBits ushr 30) or (highProductBits shl 34))
    }

    operator fun div(other: Int) = this / other.toLong()

    operator fun div(other: Long): FixNano64 {
        if (other == -1L && raw == Long.MIN_VALUE) throw FixedPointException("Attempted to compute $this / -1")
        if (other == 0L) throw FixedPointException("Attempted to compute $this / 0")
        return FixNano64(raw / other)
    }

    operator fun div(other: Float) = this / other.toDouble()

    operator fun div(other: Double): FixNano64 {
        val doubleResult = this.raw.toDouble() / other
        if (doubleResult >= Long.MIN_VALUE.toDouble() && doubleResult <= Long.MAX_VALUE.toDouble()) {
            return FixNano64(doubleResult.roundToLong())
        } else {
            throw FixedPointException("Attempted to compute $this / $other")
        }
    }

    operator fun div(other: FixNano64): FixNano64 {
        /*
         * Theoretically:
         *   this = this.raw * 2^-30
         *   other = other.raw * 2^-30
         *   result = this / other = this.raw / other.raw -> result.raw = 2^30 * this.raw / other.raw
         *
         * Practically, computing 2^30 * this.raw could overflow, so we need a more complicated approach...
         * Unfortunately, I couldn't find a way that avoids BigInteger, so this will be rather slow.
         */

        // TODO Optimize case when this.raw * 2^30 doesn't overflow
        val product = BigInteger.valueOf(this.raw) shl 30
        val bigResult = product / BigInteger.valueOf(other.raw)
        try {
            return FixNano64(bigResult.longValueExact())
        } catch (overflow: ArithmeticException) {
            throw FixedPointException("Attempted to compute $this / $other")
        }
    }

    override fun compareTo(other: FixNano64) = raw.compareTo(other.raw)

    override fun toString(): String {
        if (raw == Long.MIN_VALUE) return "MIN"
        if (raw == Long.MAX_VALUE) return "MAX"

        var remaining = (raw.absoluteValue.toBigInteger() * (1000L * 1000L * 1000L).toBigInteger() shr 30).toLong()

        val signString = if (raw < 0) "-" else ""

        if (remaining < 1000) return signString + remaining + "n"

        var numShifts = 0
        while (remaining / 1_000_000L != 0L) {
            val shouldRoundUp = (remaining % 1000) >= 500
            remaining /= 1000
            if (shouldRoundUp) remaining += 1
            numShifts += 1
        }

        val integerPart = (remaining / 1000) % 1000
        val fractionalPart = remaining % 1000

        val unitString = when (numShifts) {
            0 -> "u"
            1 -> "m"
            2 -> ""
            3 -> "k"
            4 -> "M"
            5 -> "G"
            else -> throw Error("Unexpected value for numShifts: $numShifts")
        }

        var fractionalString = if (fractionalPart > 0) {
            if (fractionalPart < 10) ".00$fractionalPart"
            else if (fractionalPart < 100) ".0$fractionalPart"
            else ".$fractionalPart"
        } else ""
        while (fractionalString.endsWith("0")) fractionalString = fractionalString.substring(0, fractionalString.length - 1)
        return signString + integerPart + fractionalString + unitString
    }

    companion object {

        val ZERO = fromInt(0)

        fun raw(rawValue: Long) = FixNano64(rawValue)

        fun fromInt(value: Int) = FixNano64(value.toLong() * RAW_ONE)

        fun fromLong(value: Long): FixNano64 {
            try {
                return FixNano64(Math.multiplyExact(value, RAW_ONE))
            } catch (overflow: ArithmeticException) {
                throw FixedPointException("Can't convert $value to FixNano64")
            }
        }

        fun fromFloat(value: Float) = fromDouble(value.toDouble())

        fun fromDouble(value: Double): FixNano64 {
            val doubleValue = RAW_ONE.toDouble() * value
            if (doubleValue > Long.MAX_VALUE.toDouble() || doubleValue < Long.MIN_VALUE.toDouble()) {
                throw FixedPointException("Can't represent $value")
            }
            return FixNano64(doubleValue.roundToLong())
        }

        // Note that overflow is impossible since RAW_ONE == 2^30 and |numerator| < 2^31
        fun fraction(numerator: Int, denominator: Int) = FixNano64(RAW_ONE * numerator / denominator)

        fun fraction(numerator: Long, denominator: Long): FixNano64 {
            // TODO Optimize common case when RAW_ONE * numerator doesn't overflow
            try {
                return FixNano64(
                    (BigInteger.valueOf(RAW_ONE) * BigInteger.valueOf(numerator) / BigInteger.valueOf(
                        denominator
                    )).longValueExact()
                )
            } catch (finalOverflow: ArithmeticException) {
                throw FixedPointException("Can't represent $numerator / $denominator")
            }
        }
    }
}

operator fun Int.times(other: FixNano64) = other * this

operator fun Long.times(other: FixNano64) = other * this

operator fun Float.times(other: FixNano64) = other * this

operator fun Double.times(other: FixNano64) = other * this

operator fun Int.div(other: FixNano64) = FixNano64.fromInt(this) / other

operator fun Long.div(other: FixNano64) = FixNano64.fromLong(this) / other

operator fun Float.div(other: FixNano64) = FixNano64.fromFloat(this) / other

operator fun Double.div(other: FixNano64) = FixNano64.fromDouble(this) / other

@Throws(AssertionError::class)
fun assertEquals(expected: FixNano64, actual: FixNano64, margin: Double) {
    if ((expected.toDouble() - actual.toDouble()).absoluteValue > margin) {
        throw AssertionError("Expected $expected, but got $actual (difference is ${expected - actual})")
    }
}
