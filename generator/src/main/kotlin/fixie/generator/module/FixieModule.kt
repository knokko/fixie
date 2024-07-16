package fixie.generator.module

import fixie.generator.acceleration.AccelerationClass
import fixie.generator.angle.AngleClass
import fixie.generator.area.AreaClass
import fixie.generator.displacement.DisplacementClass
import fixie.generator.number.NumberClass
import fixie.generator.speed.SpeedClass
import fixie.generator.spin.SpinClass

class FixieModule(
        val packageName: String,
        val numbers: List<NumberClass>,
        val displacements: List<DisplacementClass> = emptyList(),
        val areas: List<AreaClass> = emptyList(),
        val speed: List<SpeedClass> = emptyList(),
        val accelerations: List<AccelerationClass> = emptyList(),
        val angles: List<AngleClass> = emptyList(),
        val spins: List<SpinClass> = emptyList()
) {

    init {
        if (packageName.contains("/")) {
            throw IllegalArgumentException("Packages should be separated by dots instead of slashes")
        }

        fun <S, T, R> checkPresent(
                sourceDescription: String, targetDescription: String,
                sourceElements: List<S>, targetElements: List<T>,
                extractSource: (S) -> R?, extractTarget: (T) -> R,
                extractSourceName: (S) -> String
        ) {
            for (element in sourceElements) {
                val sourceResult = extractSource(element) ?: continue
                if (!targetElements.map(extractTarget).contains(sourceResult)) {
                    throw IllegalArgumentException("Missing $targetDescription class for $sourceDescription class ${extractSourceName(element)}")
                }
            }
        }

        checkPresent("displacement", "number", displacements, numbers, { it.number }, { it }, { it.className })
        checkPresent("displacement", "area", displacements, areas, { it.area }, { it }, { it.className })
        checkPresent("displacement", "speed", displacements, speed, { it.speed }, { it }, { it.className })
        checkPresent("area", "displacement", areas, displacements, { it.displacementClassName }, { it.className }, { it.className })
        checkPresent("speed", "number", speed, numbers, { it.number }, { it }, { it.className })
        checkPresent("speed", "displacement", speed, displacements, { it.displacementClassName }, { it.className }, { it.className })
        checkPresent("speed", "acceleration", speed, accelerations, { it.acceleration }, { it }, { it.className })
        checkPresent("acceleration", "speed", accelerations, speed, { it.speedClassName }, { it.className }, { it.className })
        checkPresent("angle", "spin", angles, spins, { it.spinClass }, { it }, { it.className })
        checkPresent("spin", "angle", spins, angles, { it.angleClassName }, { it.className }, { it.className })

        val allClassNames = numbers.map { it.className } +
                displacements.map { it.className } + areas.map { it.className } +
                speed.map { it.className } + accelerations.map { it.className } +
                angles.map { it.className } + spins.map { it.className }

        for (className in allClassNames) {
            if (allClassNames.count { it == className } > 1) {
                throw IllegalArgumentException("Duplicate class name $className")
            }
        }
    }
}
