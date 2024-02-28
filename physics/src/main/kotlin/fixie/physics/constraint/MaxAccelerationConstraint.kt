package fixie.physics.constraint

import fixie.*
import fixie.geometry.Position
import fixie.physics.SlidingWindow
import fixie.physics.Velocity
import kotlin.math.roundToInt
import kotlin.time.Duration

internal class MaxAccelerationConstraint(
        windowSize: Duration,
        stepDuration: Duration,
        private val threshold: Speed
) : VelocityConstraint() {

    private val age = (windowSize / stepDuration).roundToInt()
    private val velocityHistory = SlidingWindow(Array(age + 1) { Velocity.zero() })

    override fun check(position: Position, velocity: Velocity) {
        if (velocityHistory.getMaximumAge() >= age) {
            val reference = velocityHistory.get(age)
            val dx = velocity.x - reference.x
            val dy = velocity.y - reference.y

            velocity.x = reference.x + max(-threshold, min(threshold, dx))
            velocity.y = reference.y + max(-threshold, min(threshold, dy))
        }

        val currentVelocity = velocityHistory.claim()
        currentVelocity.x = velocity.x
        currentVelocity.y = velocity.y
    }
}
