package fixie.physics

import fixie.*
import java.util.*

class EntitySpawnRequest(
    val x: Displacement,
    val y: Displacement,
    val properties: EntityProperties,
    val velocityX: Displacement = 0.m,
    val velocityY: Displacement = 0.m
) {
    var id: UUID? = null

    @Volatile
    var processed = false
}
