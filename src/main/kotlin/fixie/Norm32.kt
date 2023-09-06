package fixie

import java.util.*
import kotlin.math.roundToLong

@JvmInline
value class Norm32 private constructor(val raw: Int) : Comparable<Norm32> {

    init {
        if (raw == Int.MIN_VALUE) throw IllegalStateException("raw value must not be Int.MIN_VALUE")
    }

    operator fun unaryMinus() = Norm32(-raw)

    @Throws(FixedPointException::class)
    operator fun plus(other: Norm32): Norm32 {
        val rawResult = this.raw.toLong() + other.raw.toLong()

        if (rawResult > Int.MAX_VALUE) {

            // Prevent overflow exceptions due to small rounding errors
            if (rawResult < Int.MAX_VALUE + 1000L) return ONE
            throw FixedPointException("$this + $other would overflow")
        }

        if (rawResult < Int.MIN_VALUE) {

            // Prevent underflow exceptions due to small rounding errors
            if (rawResult > -Int.MAX_VALUE - 1000L) return -ONE
            throw FixedPointException("$this + $other would underflow")
        }

        return Norm32(rawResult.toInt())
    }

    @Throws(FixedPointException::class)
    operator fun minus(right: Norm32) = this + -right

    fun toFloat() = toDouble().toFloat()

    fun toDouble() = raw.toDouble() / Int.MAX_VALUE.toDouble()

    override fun compareTo(other: Norm32) = raw.compareTo(other.raw)

    override fun toString(): String {
        val originalString = String.format(Locale.ROOT, "%.4f", toDouble())

        var prefix: String
        var valueString: String
        if (originalString.startsWith("-")) {
            prefix = "-"
            valueString = originalString.substring(1)
        } else {
            prefix = ""
            valueString = originalString
        }

        while (valueString.length > 1 && (valueString.endsWith("0") || valueString.endsWith("."))) {
            valueString = valueString.substring(0 until valueString.length - 1)
        }

        if (valueString == "0") prefix = ""

        return prefix + valueString
    }

    companion object {

        val ZERO = Norm32(0)
        val ONE = Norm32(Int.MAX_VALUE)

        fun fromFloat(value: Float) = fromDouble(value.toDouble())

        @Throws(FixedPointException::class)
        fun fromDouble(value: Double): Norm32 {
            val longValue = (value * Int.MAX_VALUE).roundToLong()
            if (longValue < -Int.MAX_VALUE || longValue > Int.MAX_VALUE) throw FixedPointException("Can't normalize $value")

            return Norm32(longValue.toInt())
        }

        fun createRaw(rawValue: Int): Norm32 {
            if (rawValue == Int.MIN_VALUE) throw FixedPointException("Using Int.MIN_VALUE is forbidden")
            return Norm32(rawValue)
        }
    }
}
