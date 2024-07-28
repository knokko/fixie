package fixie.generator.number

enum class FloatType(val numBytes: Int) {

    SinglePrecision(4),
    DoublePrecision(8);

    init {
        if (numBytes != 4 && numBytes != 8) {
            throw IllegalArgumentException("Floats must use 4 or 8 bytes, but got $numBytes")
        }
    }

    val typeName: String
        get() = if (numBytes == 4) "Float" else "Double"

    override fun toString() = "Float${numBytes * 8}"
}
