package fixie.generator.module

import fixie.generator.acceleration.AccelerationClass
import fixie.generator.angle.AngleClass
import fixie.generator.area.AreaClass
import fixie.generator.density.DensityClass
import fixie.generator.displacement.DisplacementClass
import fixie.generator.number.NumberClass
import fixie.generator.parser.InvalidConfigException
import fixie.generator.quantity.QuantityClass
import fixie.generator.speed.SpeedClass
import fixie.generator.spin.SpinClass
import fixie.generator.volume.VolumeClass

class FixieModule(
    val moduleName: String,
    val packageName: String,
    val numbers: List<NumberClass>,
    val accelerations: List<AccelerationClass> = emptyList(),
    val angles: List<AngleClass> = emptyList(),
    val areas: List<AreaClass> = emptyList(),
    val volumes: List<VolumeClass> = emptyList(),
    val displacements: List<DisplacementClass> = emptyList(),
    val speed: List<SpeedClass> = emptyList(),
    val spins: List<SpinClass> = emptyList(),
    val densities: List<DensityClass> = emptyList()
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

        resolve(densities, volumes, { it.volumeClassName }) { density, volume -> density.volume = volume }
        resolve(accelerations, speed, { it.speedClassName }) { acceleration, speed -> acceleration.speed = speed }
        resolve(angles, spins, { it.spinClassName }) { angle, spin -> angle.spinClass = spin }
        resolve(volumes, densities, { it.densityClassName }) { volume, density -> volume.density = density }
        resolve(volumes, areas, { it.areaClassName }) { volume, area -> volume.area = area }
        resolve(volumes, displacements, { it.displacementClassName }) { volume, displacement -> volume.displacement = displacement }
        resolve(areas, volumes, { it.volumeClassName }) { area, volume -> area.volume = volume }
        resolve(areas, displacements, { it.displacementClassName }) { area, displacement -> area.displacement = displacement }
        checkPresent(displacements, numbers) { it.number }
        resolve(displacements, volumes, { it.volumeClassName }) { displacement, volume -> displacement.volume = volume }
        resolve(displacements, areas, { it.areaClassName }) { displacement, area -> displacement.area = area }
        resolve(displacements, speed, { it.speedClassName }) { displacement, speed -> displacement.speed = speed }
        checkPresent(speed, numbers) { it.number }
        resolve(speed, displacements, { it.displacementClassName }) { speed, displacement -> speed.displacementClass = displacement }
        resolve(speed, accelerations, { it.accelerationClassName }) { speed, acceleration -> speed.accelerationClass = acceleration }
        resolve(spins, angles, { it.angleClassName }) { spin, angle -> spin.angle = angle }

        val allClassNames = numbers.map { it.className } +
                displacements.map { it.className } + areas.map { it.className } + volumes.map { it.className } +
                speed.map { it.className } + accelerations.map { it.className } +
                angles.map { it.className } + spins.map { it.className } + densities.map { it.className }

        for (className in allClassNames) {
            if (allClassNames.count { it == className } > 1) {
                throw IllegalArgumentException("Duplicate class name $className")
            }
        }
    }
}
