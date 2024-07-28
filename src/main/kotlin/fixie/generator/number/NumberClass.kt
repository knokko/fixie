package fixie.generator.number

import fixie.generator.parser.InvalidConfigException
import java.math.BigInteger

class NumberClass(
    parsePath: String,
    val className: String,
    val internalType: IntType,
    val oneValue: BigInteger,
    val checkOverflow: Boolean
) {
    init {
        if (oneValue <= BigInteger.ONE) {
            throw InvalidConfigException("Expected $parsePath.oneValue to be larger than 1, but got $oneValue")
        }
        if (oneValue > internalType.getMaxValue()) {
            throw InvalidConfigException("Expected $parsePath.oneValue to be at most ${internalType.getMaxValue()}, but got $oneValue")
        }
    }

    override fun toString() = "$className(intType=$internalType, one=$oneValue)"
}
