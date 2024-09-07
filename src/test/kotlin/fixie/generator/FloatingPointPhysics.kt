package fixie.generator

import fixie.generator.floatvsfixed.Displacement

fun main() {
	val initialX = 0f // In meters

	val speed = 0.1f // In meters per second

	val updateFrequency = 100 // The number of update steps per second

	fun simulateFloat(seconds: Int) {
		var x = initialX

		for (second in 0 until seconds) {
			for (counter in 0 until updateFrequency) {
				x += speed / updateFrequency
			}
		}

		println("x is $x after $seconds seconds")
	}

	fun simulateFixed(seconds: Int) {
		var x = Displacement.METER * initialX

		for (second in 0 until seconds) {
			for (counter in 0 until updateFrequency) {
				x += Displacement.METER * speed / updateFrequency
			}
		}

		println("x is $x after $seconds seconds")
	}

	simulateFixed(1)
	simulateFixed(10)
}
