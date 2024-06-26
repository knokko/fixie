package fixie.physics.constraint

import fixie.geometry.Position
import fixie.*
import fixie.physics.Scene
import fixie.physics.SlidingWindow
import fixie.physics.Velocity
import kotlin.math.roundToInt
import kotlin.time.Duration

internal class NotMovingConstraint(
        windowDuration: Duration
) : VelocityConstraint() {

    private val age = (windowDuration / Scene.STEP_DURATION).roundToInt()
    private val velocityHistory = SlidingWindow(Array(age + 1) { Velocity.zero() })
    private val positionHistory = SlidingWindow(Array(age + 1) { Position.origin() })

    private var lowestSpeed = 100.mps

    override fun check(position: Position, velocity: Velocity) {
       if (velocityHistory.getMaximumAge() >= age)  {
           val oldPosition = positionHistory.get(age)

           val currentSpeed = abs(velocity.x) + abs(velocity.y)
           val leavingSpeed = velocityHistory.get(velocityHistory.getMaximumAge()).x
           velocityHistory.claim().x = currentSpeed
           if (currentSpeed < lowestSpeed) lowestSpeed = currentSpeed

           if (leavingSpeed == lowestSpeed) {
               lowestSpeed = currentSpeed
                for (candidateAge in 1 .. age) {
                    val candidateSpeed = velocityHistory.get(candidateAge).x
                    if (candidateSpeed < lowestSpeed) lowestSpeed = candidateSpeed
                }
           }

           val actualDistance = abs(position.x - oldPosition.x) + abs(position.y - oldPosition.y)
           val expectedDistance = lowestSpeed * Scene.STEP_DURATION * age

           if (expectedDistance > 2 * actualDistance && currentSpeed > 0.5.mps) {
               velocity.x /= 2
               velocity.y /= 2
           }
       }

        val currentPosition = positionHistory.claim()
        currentPosition.x = position.x
        currentPosition.y = position.y

        val currentVelocity = velocityHistory.claim()
        currentVelocity.x = velocity.x
        currentVelocity.y = velocity.y
    }
}
