package fixie.generator

import java.math.BigInteger

class NumberClass(
        val className: String,
        val internalType: IntType,
        val oneValue: BigInteger,
        val checkOverflow: Boolean
)
