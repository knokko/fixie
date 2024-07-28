package fixie.generator.quantity

class QuantityUnit(
        val name: String,
        val enumName: String,
        val suffix: String,
        val extensionName: String,
        val minDelta: Double,
        val maxAmount: Double
) {
    override fun toString() = "$name($minDelta, $maxAmount)"
}
