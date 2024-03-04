package fixie.physics.constraint

import fixie.*
import fixie.geometry.Position
import fixie.physics.Scene
import fixie.physics.SlidingWindow
import fixie.physics.Velocity
import kotlin.math.roundToInt
import kotlin.time.Duration

internal class NotStuckConstraint(
        private val threshold: Speed,
        private val windowDuration: Duration,
        private val setNotStuck: () -> Unit
) : VelocityConstraint() {

    private val age = (windowDuration / Scene.STEP_DURATION).roundToInt()
    private val positionHistory = SlidingWindow(Array(age + 1) { Position.origin() })

    override fun check(position: Position, velocity: Velocity) {
        if (positionHistory.getMaximumAge() >= age) {
            val old = positionHistory.get(age)

            val deltaX = position.x - old.x
            val deltaY = position.y - old.y
            val speed = (abs(deltaX) + abs(deltaY)) / windowDuration

            if (speed > threshold) setNotStuck()
        }

        val nextPosition = positionHistory.claim()
        nextPosition.x = position.x
        nextPosition.y = position.y
    }
}
