package fixie.physics

import kotlin.math.min

internal class SlidingWindow<T>(private val buffer: Array<T>) {

    private var writeIndex = 0L

    init {
        if (buffer.isEmpty()) throw IllegalArgumentException("Buffer can't be empty")
    }

    fun get(age: Int): T {
        if (age >= writeIndex) throw IllegalArgumentException("Age is $age, but only $writeIndex elements were claimed")
        if (age >= buffer.size) throw IllegalArgumentException("Age is $age, but buffer size is ${buffer.size}")
        return buffer[((writeIndex - age - 1) % buffer.size).toInt()]
    }

    fun getMaximumAge() = (min(writeIndex, buffer.size.toLong()) - 1L).toInt()

    fun claim(): T {
        val element = buffer[(writeIndex % buffer.size.toLong()).toInt()]
        writeIndex += 1
        return element
    }
}
