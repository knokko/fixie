package fixie.generator

import java.math.BigInteger

class IntType(
        val signed: Boolean,
        val numBytes: Int
) {

    val className: String

    init {
        val rawClassName = when (numBytes) {
            1 -> "Byte"
            2 -> "Short"
            4 -> "Int"
            8 -> "Long"
            else -> throw IllegalArgumentException("Unsupported numBytes: $numBytes")
        }
        this.className = if (signed) rawClassName else "U$rawClassName"
    }

    override fun toString() = className

    override fun equals(other: Any?) = other is IntType && this.signed == other.signed && this.numBytes == other.numBytes

    override fun hashCode() = 10 * numBytes + if (signed) 1 else 0

    fun getMinValue(): BigInteger {
        return if (signed) {
            var minValue = BigInteger.valueOf(-128)
            for (extraByte in 1 until numBytes) minValue = minValue.multiply(BigInteger.valueOf(256))
            minValue
        } else BigInteger.ZERO
    }

    fun getMaxValue(): BigInteger {
        return if (signed) -getMinValue().plus(BigInteger.ONE) else {
            var maxValue = BigInteger.valueOf(256)
            for (extraByte in 1 until numBytes) maxValue = maxValue.multiply(BigInteger.valueOf(256))
            maxValue.subtract(BigInteger.ONE)
        }
    }

    fun canRepresent(value: Long) = canRepresent(BigInteger.valueOf(value))

    fun canRepresent(value: BigInteger): Boolean {
        return value >= getMinValue() && value <= getMaxValue()
    }

    fun declareValue(nameAndModifiers: String, value: Long) = declareValue(nameAndModifiers, BigInteger.valueOf(value))

    fun declareValue(nameAndModifiers: String, value: BigInteger): String {
        if (!canRepresent(value)) throw IllegalArgumentException("Can't represent $value")
        return if (signed) {
            if (numBytes < 4) "$nameAndModifiers: $className = $value"
            else if (numBytes == 4) "$nameAndModifiers = $value"
            else "$nameAndModifiers = ${value}L"
        } else {
            if (numBytes < 4) "$nameAndModifiers: $className = ${value}u"
            else if (numBytes == 4) "$nameAndModifiers = ${value}u"
            else "$nameAndModifiers = ${value}uL"
        }
    }
}
