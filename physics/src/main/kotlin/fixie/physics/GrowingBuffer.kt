package fixie.physics

class GrowingBuffer<T>(private var elements: Array<T>, private val createInstance: () -> T) {

    var size = 0
    private set

    init {
        if (elements.isEmpty()) throw IllegalArgumentException()
    }

    fun add(): T {
        if (size >= elements.size) {
            elements += elements
            for (index in size until elements.size) elements[index] = createInstance()
        }
        val result = elements[size]
        size += 1
        return result
    }

    fun add(value: T) {
        if (size >= elements.size) elements += elements
        elements[size] = value
        size += 1
    }

    operator fun get(index: Int) = elements[index]

    fun clear() {
        size = 0
    }

    fun contains(element: T): Boolean {
        for (index in 0 until size) {
            if (elements[index] == element) return true
        }
        return false
    }

    companion object {
        inline fun <reified T> withMutableElements(initialCapacity: Int, noinline createInstance: () -> T) = GrowingBuffer(
                Array(initialCapacity) { _ -> createInstance() }, createInstance
        )

        inline fun <reified T> withImmutableElements(initialCapacity: Int, dummyElement: T) = GrowingBuffer(
                Array(initialCapacity) { _ -> dummyElement }
        ) { dummyElement }
    }
}
