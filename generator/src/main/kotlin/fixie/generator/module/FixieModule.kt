package fixie.generator.module

import fixie.generator.acceleration.AccelerationClass
import fixie.generator.angle.AngleClass
import fixie.generator.displacement.DisplacementClass
import fixie.generator.number.NumberClass
import fixie.generator.speed.SpeedClass

class FixieModule(
        val packageName: String,
        val numbers: List<NumberClass>,
        val displacements: List<DisplacementClass> = emptyList(),
        val speed: List<SpeedClass> = emptyList(),
        val accelerations: List<AccelerationClass> = emptyList(),
        val angles: List<AngleClass> = emptyList()
) {

    init {
        if (packageName.contains("/")) {
            throw IllegalArgumentException("Packages should be separated by dots instead of slashes")
        }

        fun <T> checkPresent(
                description: String, elements: List<T>,
                extractName: (T) -> String, extractNumber: (T) -> NumberClass
        ) {
            for (element in elements) {
                if (!numbers.contains(extractNumber(element))) {
                    throw IllegalArgumentException("Missing number class for $description class ${extractName(element)}")
                }
            }
        }

        checkPresent("displacement", displacements, { it.className }) { it.number }
        checkPresent("speed", speed.filter { it.number != null }, { it.className }) { it.number!! }
    }
}
