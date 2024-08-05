package fixie.generator.module

import fixie.generator.acceleration.AccelerationClassGenerator
import fixie.generator.acceleration.AccelerationTestsGenerator
import fixie.generator.angle.AngleClassGenerator
import fixie.generator.angle.AngleTestsGenerator
import fixie.generator.angle.AngleUnit
import fixie.generator.area.AreaClassGenerator
import fixie.generator.area.AreaTestsGenerator
import fixie.generator.area.AreaUnit
import fixie.generator.displacement.DisplacementClassGenerator
import fixie.generator.displacement.DisplacementTestsGenerator
import fixie.generator.displacement.DistanceUnit
import fixie.generator.number.NumberClassGenerator
import fixie.generator.number.NumberTestsGenerator
import fixie.generator.quantity.QuantityClass
import fixie.generator.quantity.QuantityClassGenerator
import fixie.generator.quantity.QuantityTestsGenerator
import fixie.generator.speed.SpeedClassGenerator
import fixie.generator.speed.SpeedTestsGenerator
import fixie.generator.speed.SpeedUnit
import fixie.generator.spin.SpinClassGenerator
import fixie.generator.spin.SpinTestsGenerator
import fixie.generator.volume.VolumeClassGenerator
import fixie.generator.volume.VolumeTestsGenerator
import fixie.generator.volume.VolumeUnit
import java.io.File
import java.io.IOException
import java.io.PrintWriter

@Throws(IOException::class)
fun generateModule(module: FixieModule, directory: File, clearExistingFiles: Boolean) {
    if (clearExistingFiles && directory.exists()) {
        if (!directory.deleteRecursively()) throw IOException("Failed to delete the directory")
    }

    if (!directory.isDirectory) {
        if (!directory.mkdirs()) throw IOException("Failed to create the directory")
    }

    val sourceDirectory = File("$directory/src/main/kotlin/${module.packageName.replace('.', '/')}")
    val testDirectory = File("$directory/src/test/kotlin/${module.packageName.replace('.', '/')}")

    if (!sourceDirectory.isDirectory && !sourceDirectory.mkdirs()) {
        throw IOException("Failed to create sources directory $sourceDirectory")
    }
    if (!testDirectory.isDirectory && !testDirectory.mkdirs()) {
        throw IOException("Failed to create tests directory $testDirectory")
    }

    fun <T> generateFiles(
            elements: List<T>, extractName: (T) -> String,
            generateSources: (PrintWriter, T) -> Unit, generateTests: (PrintWriter, T) -> Unit
    ) {
        for (element in elements) {
            generateFile(sourceDirectory, extractName(element)) { writer -> generateSources(writer, element) }
            generateFile(testDirectory, "Test" + extractName(element)) { writer -> generateTests(writer, element) }
        }
    }

    fun <Q : QuantityClass, C: QuantityClassGenerator<Q>, T: QuantityTestsGenerator<Q>> generateQuantityFiles(
        elements: List<Q>,
        classGeneratorConstructor: (PrintWriter, Q, String) -> C,
        testsGeneratorConstructor: (PrintWriter, Q, String) -> T
    ) {
        generateFiles(
            elements, { it.className },
            { writer, quantity -> classGeneratorConstructor(writer, quantity, module.packageName).generate() },
            { writer, quantity -> testsGeneratorConstructor(writer, quantity, module.packageName).generate() }
        )
    }

    generateFiles(
            module.numbers, { it.className },
            { writer, number -> NumberClassGenerator(writer, number, module.packageName).generate() },
            { writer, number -> NumberTestsGenerator(writer, number, module.packageName).generate() }
    )

    generateQuantityFiles(module.displacements, ::DisplacementClassGenerator, ::DisplacementTestsGenerator)
    generateQuantityFiles(module.areas, ::AreaClassGenerator, ::AreaTestsGenerator)
    generateQuantityFiles(module.volumes, ::VolumeClassGenerator, ::VolumeTestsGenerator)
    generateQuantityFiles(module.speed, ::SpeedClassGenerator, ::SpeedTestsGenerator)
    generateQuantityFiles(module.accelerations, ::AccelerationClassGenerator, ::AccelerationTestsGenerator)
    generateQuantityFiles(module.angles, ::AngleClassGenerator, ::AngleTestsGenerator)
    generateQuantityFiles(module.spins, ::SpinClassGenerator, ::SpinTestsGenerator)

    generateMathFile(module.numbers, module.angles, module.packageName, File("$sourceDirectory/ExtraMath.kt"))

    if (module.numbers.find { it.checkOverflow } != null) {
        generateFixedPointException(File("$sourceDirectory/FixedPointException.kt"), module.packageName)
    }

    fun <T: QuantityClass> maybeGenerateUnitEnum(elements: List<T>, unitName: String, generateUnit: (File, String) -> Unit) {
        if (elements.isNotEmpty()) generateUnit(File("$sourceDirectory/$unitName.kt"), module.packageName)
    }

    maybeGenerateUnitEnum(module.displacements, "DistanceUnit", ::generateDistanceUnit)
    maybeGenerateUnitEnum(module.areas, "AreaUnit", ::generateAreaUnit)
    maybeGenerateUnitEnum(module.volumes, "VolumeUnit", ::generateVolumeUnit)
    maybeGenerateUnitEnum(module.speed, "SpeedUnit", ::generateSpeedUnit)
    maybeGenerateUnitEnum(module.angles, "AngleUnit", ::generateAngleUnit)
    maybeGenerateUnitEnum(module.spins, "SpinUnit", ::generateSpinUnit)
}

private fun generateFile(directory: File, name: String, write: (PrintWriter) -> Unit) {
    generateFile(File("$directory/$name.kt"), write)
}

private fun generateFile(file: File, write: (PrintWriter) -> Unit) {
    val writer = PrintWriter(file)
    write(writer)
    writer.flush()
    writer.close()
}

private fun generateKotlinClass(file: File, packageName: String, generateClass: (PrintWriter) -> Unit) {
    generateFile(file) { writer ->
        writer.println("package $packageName")
        writer.println()
        generateClass(writer)
    }
}

private fun generateFixedPointException(file: File, packageName: String) {
    generateKotlinClass(file, packageName) { it.println("class FixedPointException(message: String): RuntimeException(message)") }
}

private fun <T : Enum<*>> generateUnitClass(
    file: File, packageName: String, entries: List<T>, firstLine: String, getConstructorParameters: (T) -> String) {
    generateKotlinClass(file, packageName) { writer ->
        writer.println(firstLine)

        for (unit in entries) {
            writer.print("\t${unit.name}(${getConstructorParameters(unit)})")
            if (unit == entries.last()) writer.println(";") else writer.println(",")
        }

        writer.println("}")
    }
}

private fun generateDistanceUnit(file: File, packageName: String) {
    generateUnitClass(
        file, packageName, DistanceUnit.entries,
        "enum class DistanceUnit(val abbreviation: String, val isMetric: Boolean, val divisor: Long) {"
    ) { unit -> "\"${unit.abbreviation}\", ${unit.isMetric}, ${unit.divisor}"}
}

private fun generateAreaUnit(file: File, packageName: String) {
    generateUnitClass(
        file, packageName, AreaUnit.entries,
        "enum class AreaUnit(val abbreviation: String, val factor: Double) {"
    ) { unit -> "\"${unit.abbreviation}\", ${unit.factor}" }
}

private fun generateVolumeUnit(file: File, packageName: String) {
    generateUnitClass(
            file, packageName, VolumeUnit.entries,
            "enum class VolumeUnit(val abbreviation: String, val factor: Double) {"
    ) { unit -> "\"${unit.abbreviation}\", ${unit.factor}" }
}

private fun generateSpeedUnit(file: File, packageName: String) {
    generateUnitClass(
        file, packageName, SpeedUnit.entries,
        "enum class SpeedUnit(val abbreviation: String, val factor: Double) {"
    ) { unit -> "\"${unit.abbreviation}\", ${unit.factor}" }
}

private fun generateAngleUnit(file: File, packageName: String) {
    generateUnitClass(
        file, packageName, AngleUnit.entries,
        "enum class AngleUnit(val suffix: String, val maxValue: Double) {"
    ) { unit -> "\"${unit.suffix}\", ${unit.maxValue}" }
}

private fun generateSpinUnit(file: File, packageName: String) {
    generateKotlinClass(file, packageName) { writer ->
        writer.println("import kotlin.math.PI")
        writer.println()
        writer.println("enum class SpinUnit(val suffix: String, val abbreviation: String, val angleMax: Double) {")
        writer.println("\tDEGREES_PER_SECOND(\"°/s\", \"degps\", 360.0),")
        writer.println("\tRADIANS_PER_SECOND(\"rad/s\", \"radps\", 2 * PI)")
        writer.println("}")
    }
}
