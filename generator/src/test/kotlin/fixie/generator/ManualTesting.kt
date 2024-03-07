package fixie.generator

import fixie.AngleUnit
import fixie.DistanceUnit
import fixie.SpeedUnit
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
        createNumberExtensions = true
    ))
    generate(SpeedClass(
        className = "Speed",
        number = fixDist,
        oneUnit = SpeedUnit.METERS_PER_SECOND,
        displayUnit = SpeedUnit.KILOMETERS_PER_HOUR,
        createNumberExtensions = true
    ))

    val fixAngle = NumberClass(
            className = "FixAngle",
            internalType = IntType(false, 4),
            oneValue = BigInteger.TWO.pow(24),
            checkOverflow = false
    )
    generate(fixAngle)
    generate(AngleClass(
            className = "Angle",
            number = fixAngle,
            displayUnit = AngleUnit.DEGREES,
            createNumberExtensions = true,
            allowComparisons = false,
            allowDivisionAndMultiplication = false
    ))
}

private fun generate(angle: AngleClass) {
    val writer = PrintWriter(File("generated/src/main/kotlin/fixie/${angle.className}.kt"))
    AngleClassGenerator(writer, angle).generate()
    writer.flush()
    writer.close()

    val testWriter = PrintWriter(File("generated/src/test/kotlin/fixie/Test${angle.className}.kt"))
    AngleTestsGenerator(testWriter, angle).generate()
    testWriter.flush()
    testWriter.close()
}

private fun generate(speed: SpeedClass) {
    val writer = PrintWriter(File("generated/src/main/kotlin/fixie/${speed.className}.kt"))
    SpeedClassGenerator(writer, speed).generate()
    writer.flush()
    writer.close()

    val testWriter = PrintWriter(File("generated/src/test/kotlin/fixie/Test${speed.className}.kt"))
    SpeedTestsGenerator(testWriter, speed).generate()
    testWriter.flush()
    testWriter.close()
}

private fun generate(displacement: DisplacementClass) {
    val writer = PrintWriter(File("generated/src/main/kotlin/fixie/${displacement.className}.kt"))
    DisplacementClassGenerator(writer, displacement).generate()
    writer.flush()
    writer.close()

    val testWriter = PrintWriter(File("generated/src/test/kotlin/fixie/Test${displacement.className}.kt"))
    DisplacementTestsGenerator(testWriter, displacement).generate()
    testWriter.flush()
    testWriter.close()
}

private fun generate(number: NumberClass) {
    val writer = PrintWriter(File("generated/src/main/kotlin/fixie/${number.className}.kt"))
    NumberClassGenerator(writer, number).generate()
    writer.flush()
    writer.close()

    val testWriter = PrintWriter(File("generated/src/test/kotlin/fixie/Test${number.className}.kt"))
    NumberTestsGenerator(testWriter, number).generate()
    testWriter.flush()
    testWriter.close()
}
