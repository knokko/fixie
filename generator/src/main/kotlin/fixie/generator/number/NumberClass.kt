package fixie.generator.number

import java.math.BigInteger

class NumberClass(
        val className: String,
        val internalType: IntType,
        val oneValue: BigInteger,
        val checkOverflow: Boolean
) {
    init {
        if (oneValue <= BigInteger.ONE) throw IllegalArgumentException("oneValue must be larger than 1")
        if (oneValue > internalType.getMaxValue()) {
            throw IllegalArgumentException("oneValue must not exceed the maximum value of the internal type $internalType")
        }
    }

    override fun toString() = "$className(intType=$internalType, one=$oneValue)"
}
