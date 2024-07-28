package fixie.generator.module

import fixie.generator.acceleration.AccelerationClass
import fixie.generator.angle.AngleClass
import fixie.generator.area.AreaClass
import fixie.generator.displacement.DisplacementClass
import fixie.generator.number.NumberClass
import fixie.generator.parser.InvalidConfigException
import fixie.generator.quantity.QuantityClass
import fixie.generator.speed.SpeedClass
import fixie.generator.spin.SpinClass

class FixieModule(
    val moduleName: String,
    val packageName: String,
    val numbers: List<NumberClass>,
    val accelerations: List<AccelerationClass> = emptyList(),
    val angles: List<AngleClass> = emptyList(),
    val areas: List<AreaClass> = emptyList(),
    val displacements: List<DisplacementClass> = emptyList(),
    val speed: List<SpeedClass> = emptyList(),
    val spins: List<SpinClass> = emptyList()
) {

    private inline fun <reified S : QuantityClass, reified T : QuantityClass> resolve(
        sources: List<S>, targets: List<T>, extractTargetName: (S) -> String?, assignTargetValue: (S, T) -> Unit
    ) {
        val sourceDescription = S::class.simpleName
        val targetDescription = T::class.simpleName
        for (source in sources) {
            val targetName = extractTargetName(source)
            if (targetName != null) {
                val target = targets.find { it.className == targetName }
                if (target != null) assignTargetValue(source, target)
                else throw InvalidConfigException("Can't find $targetDescription $targetName for $sourceDescription ${source.className}")
            }
        }
    }

    private inline fun <reified S : QuantityClass, reified T, R> checkPresent(
        sourceElements: List<S>, targetElements: List<T>, extractSource: (S) -> R?
    ) {
        val sourceDescription = S::class.simpleName
        val targetDescription = T::class.simpleName

        for (element in sourceElements) {
            val sourceResult = extractSource(element) ?: continue

            val extractTarget = if (QuantityClass::class.java.isAssignableFrom(T::class.java)) {
                quantity: T -> (quantity as QuantityClass).className
            } else {
                target: T -> target
            }

            if (!targetElements.map(extractTarget).contains(sourceResult)) {
                throw InvalidConfigException("Missing $targetDescription $sourceResult for $sourceDescription class ${element.className}")
            }
        }
    }

    init {
        if (packageName.contains("/")) {
            throw IllegalArgumentException("Packages should be separated by dots instead of slashes")
        }

        checkPresent(accelerations, speed) { it.speedClassName }
        resolve(angles, spins, { it.spinClassName }) { angle, spin -> angle.spinClass = spin }
        checkPresent(areas, displacements) { it.displacementClassName }
        checkPresent(displacements, numbers) { it.number }
        resolve(displacements, speed, { it.speedClassName }) { displacement, speed -> displacement.speed = speed }
        checkPresent(displacements, areas) { it.area }
        checkPresent(speed, numbers) { it.number }
        resolve(speed, displacements, { it.displacementClassName }) { speed, displacement -> speed.displacementClass = displacement }
        resolve(speed, accelerations, { it.accelerationClassName }) { speed, acceleration -> speed.accelerationClass = acceleration }
        checkPresent(spins, angles) { it.angleClassName }

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
