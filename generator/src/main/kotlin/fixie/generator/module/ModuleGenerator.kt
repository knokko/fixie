package fixie.generator.module

import fixie.generator.angle.AngleClassGenerator
import fixie.generator.angle.AngleTestsGenerator
import fixie.generator.angle.AngleUnit
import fixie.generator.displacement.DisplacementClassGenerator
import fixie.generator.displacement.DisplacementTestsGenerator
import fixie.generator.displacement.DistanceUnit
import fixie.generator.number.NumberClassGenerator
import fixie.generator.number.NumberTestsGenerator
import fixie.generator.speed.SpeedClassGenerator
import fixie.generator.speed.SpeedTestsGenerator
import fixie.generator.speed.SpeedUnit
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

    generateFiles(
            module.numbers, { it.className },
            { writer, number -> NumberClassGenerator(writer, number, module.packageName).generate() },
            { writer, number -> NumberTestsGenerator(writer, number, module.packageName).generate() }
    )

    generateFiles(
            module.displacements, { it.className },
            { writer, displacement -> DisplacementClassGenerator(writer, displacement, module.packageName).generate() },
            { writer, displacement -> DisplacementTestsGenerator(writer, displacement, module.packageName).generate() }
    )

    generateFiles(
            module.speed, { it.className },
            { writer, speed -> SpeedClassGenerator(writer, speed, module.packageName).generate() },
            { writer, speed -> SpeedTestsGenerator(writer, speed, module.packageName).generate() }
    )

    generateFiles(
            module.angles, { it.className },
            { writer, angle -> AngleClassGenerator(writer, angle, module.packageName).generate() },
            { writer, angle -> AngleTestsGenerator(writer, angle, module.packageName).generate() }
    )

    generateMathFile(module.numbers, module.angles, module.packageName, File("$sourceDirectory/ExtraMath.kt"))

    if (module.numbers.find { it.checkOverflow } != null) {
        generateFixedPointException(File("$sourceDirectory/FixedPointException.kt"), module.packageName)
    }

    if (module.displacements.isNotEmpty()) {
        generateDistanceUnit(File("$sourceDirectory/DistanceUnit.kt"), module.packageName)
    }

    if (module.speed.isNotEmpty()) {
        generateSpeedUnit(File("$sourceDirectory/SpeedUnit.kt"), module.packageName)
    }

    if (module.angles.isNotEmpty()) {
        generateAngleUnit(File("$sourceDirectory/AngleUnit.kt"), module.packageName)
    }
}

private fun generateFile(directory: File, name: String, write: (PrintWriter) -> Unit) {
    val writer = PrintWriter(File("$directory/$name.kt"))
    write(writer)
    writer.flush()
    writer.close()
}

private fun generateFixedPointException(file: File, packageName: String) {
    val writer = PrintWriter(file)
    writer.println("package $packageName")
    writer.println()
    writer.println("class FixedPointException(message: String): RuntimeException(message)")

    writer.flush()
    writer.close()
}

private fun generateDistanceUnit(file: File, packageName: String) {
    val writer = PrintWriter(file)
    writer.println("package $packageName")
    writer.println()
    writer.println("enum class DistanceUnit(val abbreviation: String, val isMetric: Boolean, val divisor: Long) {")

    for (unit in DistanceUnit.entries) {
        writer.print("    ${unit.name}(\"${unit.abbreviation}\", ${unit.isMetric}, ${unit.divisor})")
        if (unit == DistanceUnit.entries.last()) writer.println(";") else writer.println(",")
    }

    writer.println("}")
    writer.flush()
    writer.close()
}

private fun generateSpeedUnit(file: File, packageName: String) {
    val writer = PrintWriter(file)
    writer.println("package $packageName")
    writer.println()
    writer.println("enum class SpeedUnit(val abbreviation: String, val factor: Double) {")

    for (unit in SpeedUnit.entries) {
        writer.print("    ${unit.name}(\"${unit.abbreviation}\", ${unit.factor})")
        if (unit == SpeedUnit.entries.last()) writer.println(";") else writer.println(",")
    }

    writer.println("}")
    writer.flush()
    writer.close()
}

private fun generateAngleUnit(file: File, packageName: String) {
    val writer = PrintWriter(file)
    writer.println("package $packageName")
    writer.println()
    writer.println("enum class AngleUnit(val suffix: String, val maxValue: Double) {")

    for (unit in AngleUnit.entries) {
        writer.print("    ${unit.name}(\"${unit.suffix}\", ${unit.maxValue})")
        if (unit == AngleUnit.entries.last()) writer.println(";") else writer.println(",")
    }

    writer.println("}")
    writer.flush()
    writer.close()
}
