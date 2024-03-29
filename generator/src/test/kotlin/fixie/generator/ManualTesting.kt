package fixie.generator

import fixie.generator.angle.AngleClass
import fixie.generator.angle.AngleClassGenerator
import fixie.generator.angle.AngleTestsGenerator
import fixie.generator.angle.AngleUnit
import fixie.generator.displacement.DisplacementClass
import fixie.generator.displacement.DisplacementClassGenerator
import fixie.generator.displacement.DisplacementTestsGenerator
import fixie.generator.displacement.DistanceUnit
import fixie.generator.number.IntType
import fixie.generator.number.NumberClass
import fixie.generator.number.NumberClassGenerator
import fixie.generator.number.NumberTestsGenerator
import fixie.generator.speed.SpeedClass
import fixie.generator.speed.SpeedClassGenerator
import fixie.generator.speed.SpeedTestsGenerator
import fixie.generator.speed.SpeedUnit
import java.io.File
import java.io.PrintWriter
import java.math.BigInteger

fun main() {
    generate(NumberClass(
        className = "FixMicro64",
        internalType = IntType(true, 8),
        oneValue = BigInteger.valueOf(1024L * 1024L),
        checkOverflow = true
    ))
    generate(NumberClass(
        className = "FixMicro32",
        internalType = IntType(true, 4),
        oneValue = BigInteger.valueOf(1024L * 1024L),
        checkOverflow = true
    ))
    generate(NumberClass(
        className = "FixUncheckedMicro64",
        internalType = IntType(true, 8),
        oneValue = BigInteger.valueOf(1024L * 1024L),
        checkOverflow = false
    ))
    generate(NumberClass(
        className = "FixUncheckedMicro32",
        internalType = IntType(true, 4),
        oneValue = BigInteger.valueOf(1024L * 1024L),
        checkOverflow = false
    ))
    generate(NumberClass(
        className = "FixUncheckedMicro64U",
        internalType = IntType(false, 8),
        oneValue = BigInteger.valueOf(1024L * 1024L),
        checkOverflow = false
    ))
    generate(NumberClass(
        className = "FixUncheckedMicro32U",
        internalType = IntType(false, 4),
        oneValue = BigInteger.valueOf(1024L * 1024L),
        checkOverflow = false
    ))
    generate(NumberClass(
        className = "FixDecMicro64",
        internalType = IntType(true, 8),
        oneValue = BigInteger.valueOf(1000L * 1000L),
        checkOverflow = true
    ))
    generate(NumberClass(
        className = "FixDecMicro32",
        internalType = IntType(true, 4),
        oneValue = BigInteger.valueOf(1000L * 1000L),
        checkOverflow = true
    ))
    generate(NumberClass(
        className = "FixMicro64U",
        internalType = IntType(false, 8),
        oneValue = BigInteger.valueOf(1024L * 1024L),
        checkOverflow = true
    ))
    generate(NumberClass(
        className = "FixMicro32U",
        internalType = IntType(false, 4),
        oneValue = BigInteger.valueOf(1024L * 1024L),
        checkOverflow = true
    ))
    generate(NumberClass(
        className = "FixDecMicro64U",
        internalType = IntType(false, 8),
        oneValue = BigInteger.valueOf(1000L * 1000L),
        checkOverflow = true
    ))
    generate(NumberClass(
        className = "FixDecMicro32U",
        internalType = IntType(false, 4),
        oneValue = BigInteger.valueOf(1000L * 1000L),
        checkOverflow = true
    ))
    generate(NumberClass(
            className = "FixMilli16",
            internalType = IntType(true, 2),
            oneValue = BigInteger.valueOf(1000L),
            checkOverflow = true
    ))
    generate(NumberClass(
        className = "FixMilli16U",
        internalType = IntType(false, 2),
        oneValue = BigInteger.valueOf(1000L),
        checkOverflow = true
    ))
    generate(NumberClass(
        className = "FixUncheckedMilli16",
        internalType = IntType(true, 2),
        oneValue = BigInteger.valueOf(1000L),
        checkOverflow = false
    ))
    generate(NumberClass(
        className = "FixUncheckedMilli16U",
        internalType = IntType(false, 2),
        oneValue = BigInteger.valueOf(1000L),
        checkOverflow = false
    ))
    generate(NumberClass(
        className = "FixCenti8",
        internalType = IntType(true, 1),
        oneValue = BigInteger.valueOf(30L),
        checkOverflow = true
    ))
    generate(NumberClass(
        className = "FixCenti8U",
        internalType = IntType(false, 1),
        oneValue = BigInteger.valueOf(30L),
        checkOverflow = true
    ))
    generate(NumberClass(
        className = "FixUncheckedCenti8",
        internalType = IntType(true, 1),
        oneValue = BigInteger.valueOf(30L),
        checkOverflow = false
    ))
    generate(NumberClass(
        className = "FixUncheckedCenti8U",
        internalType = IntType(false, 1),
        oneValue = BigInteger.valueOf(30L),
        checkOverflow = false
    ))

    val fixDist = NumberClass(
        "FixDisplacement",
        internalType = IntType(true, 4),
        oneValue = BigInteger.valueOf(1000 * 100),
        checkOverflow = true
    )
    generate(fixDist)
    generate(DisplacementClass(
        className = "Displacement",
        number = fixDist,
        oneUnit = DistanceUnit.METER,
        displayUnit = DistanceUnit.METER,
            speed = null,
        createNumberExtensions = true
    ))
    generate(SpeedClass(
        className = "Speed",
        number = fixDist,
        oneUnit = SpeedUnit.METERS_PER_SECOND,
        displayUnit = SpeedUnit.KILOMETERS_PER_HOUR,
            displacementClassName = null,
        createNumberExtensions = true
    ))

    generate(AngleClass(
            className = "Angle",
            internalType = IntType(false, 4),
            displayUnit = AngleUnit.DEGREES,
            createNumberExtensions = true,
            allowComparisons = false,
            allowDivisionAndMultiplication = false
    ))
}

private fun generate(angle: AngleClass) {
    val writer = PrintWriter(File("generated/src/main/kotlin/fixie/${angle.className}.kt"))
    AngleClassGenerator(writer, angle, "fixie").generate()
    writer.flush()
    writer.close()

    val testWriter = PrintWriter(File("generated/src/test/kotlin/fixie/Test${angle.className}.kt"))
    AngleTestsGenerator(testWriter, angle, "fixie").generate()
    testWriter.flush()
    testWriter.close()
}

private fun generate(speed: SpeedClass) {
    val writer = PrintWriter(File("generated/src/main/kotlin/fixie/${speed.className}.kt"))
    SpeedClassGenerator(writer, speed, "fixie").generate()
    writer.flush()
    writer.close()

    val testWriter = PrintWriter(File("generated/src/test/kotlin/fixie/Test${speed.className}.kt"))
    SpeedTestsGenerator(testWriter, speed, "fixie").generate()
    testWriter.flush()
    testWriter.close()
}

private fun generate(displacement: DisplacementClass) {
    val writer = PrintWriter(File("generated/src/main/kotlin/fixie/${displacement.className}.kt"))
    DisplacementClassGenerator(writer, displacement, "fixie").generate()
    writer.flush()
    writer.close()

    val testWriter = PrintWriter(File("generated/src/test/kotlin/fixie/Test${displacement.className}.kt"))
    DisplacementTestsGenerator(testWriter, displacement, "fixie").generate()
    testWriter.flush()
    testWriter.close()
}

private fun generate(number: NumberClass) {
    val writer = PrintWriter(File("generated/src/main/kotlin/fixie/${number.className}.kt"))
    NumberClassGenerator(writer, number, "fixie").generate()
    writer.flush()
    writer.close()

    val testWriter = PrintWriter(File("generated/src/test/kotlin/fixie/Test${number.className}.kt"))
    NumberTestsGenerator(testWriter, number, "fixie").generate()
    testWriter.flush()
    testWriter.close()
}
