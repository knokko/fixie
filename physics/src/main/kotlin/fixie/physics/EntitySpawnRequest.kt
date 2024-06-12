package fixie.physics

import fixie.*
import java.util.*

class EntitySpawnRequest(
    val x: Displacement,
    val y: Displacement,
    val properties: EntityProperties,
    val velocityX: Speed = 0.mps,
    val velocityY: Speed = 0.mps,
    val angle: Angle = 0.degrees
) {
    var id: UUID? = null

    @Volatile
    var processed = false
}
