package fixie.generator.tester

import fixie.generator.angle.AngleClass
import fixie.generator.angle.AngleUnit
import fixie.generator.displacement.DisplacementClass
import fixie.generator.displacement.DistanceUnit
import fixie.generator.module.FixieModule
import fixie.generator.module.generateModule
import fixie.generator.number.IntType
import fixie.generator.number.NumberClass
import fixie.generator.speed.SpeedClass
import fixie.generator.speed.SpeedUnit
import java.io.File
import java.math.BigInteger
import kotlin.math.min

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        for (checkOverflow in arrayOf(false, true)) {
            for (signed in arrayOf(false, true)) {
                fun moduleName(numBits: Int) = "test-kit-fixie$numBits${if (signed) "" else "u"}${if (checkOverflow) "" else "-unchecked"}"
                generateModule(createModule(8, signed, checkOverflow, longArrayOf(
                        2, 10, 64, 100, Byte.MAX_VALUE.toLong()
                )), File(moduleName(8)), true)
                generateModule(createModule(16, signed, checkOverflow, longArrayOf(
                        2, 10, 300, 2048, 10_000, Short.MAX_VALUE.toLong()
                )), File(moduleName(16)), true)
                generateModule(createModule(32, signed, checkOverflow, longArrayOf(
                        2, 10, 5000, 1048576, 10_000_000, 123456789, 1_000_000_000, Int.MAX_VALUE.toLong()
                )), File(moduleName(32)), true)
                generateModule(createModule(64, signed, checkOverflow, longArrayOf(
                        2, 10, 80_000_000, 1 shl 40, Long.MAX_VALUE / 1234567, Long.MAX_VALUE
                )), File(moduleName(64)), true)
            }
        }
        generateModule(createPhysicsModule(), File("physics-kit"), true)
    } else if (!File("test-kit-fixie32u/build/test-results/test/TEST-fixie32.TestFix2.xml").exists()) {
        throw AssertionError("It looks like the test wasn't executed")
    }
}

private fun createPhysicsModule(): FixieModule {
    val numbers = listOf(NumberClass(
            className = "FixDisplacement",
            internalType = IntType(true, 4),
            oneValue = BigInteger.valueOf(1000 * 100),
            checkOverflow = true
    ))
    val speed = listOf(SpeedClass(
            className = "Speed",
            number = numbers[0],
            oneUnit = SpeedUnit.METERS_PER_SECOND,
            displayUnit = SpeedUnit.METERS_PER_SECOND,
            displacementClassName = "Displacement",
            createNumberExtensions = true
    ))
    val displacements = listOf(DisplacementClass(
            className = "Displacement",
            number = numbers[0],
            oneUnit = DistanceUnit.METER,
            displayUnit = DistanceUnit.METER,
            speed = speed[0],
            createNumberExtensions = true
    ))
    val angles = listOf(AngleClass(
            className = "Angle",
            internalType = IntType(true, 4),
            displayUnit = AngleUnit.DEGREES,
            createNumberExtensions = true,
            allowDivisionAndMultiplication = false,
            allowComparisons = false
    ))

    return FixieModule("fixie", numbers, displacements, speed, angles)
}

private fun createModule(numBits: Int, signed: Boolean, checkOverflow: Boolean, oneValues: LongArray): FixieModule {
    val numbers = mutableListOf<NumberClass>()

    for (oneValue in oneValues) {
            numbers.add(NumberClass(
                    className = if (signed) "Fix$oneValue" else "Fix$oneValue",
                    internalType = IntType(signed, numBits / 8),
                    oneValue = BigInteger.valueOf(oneValue),
                    checkOverflow = checkOverflow
            ))
    }

    val displacements = mutableListOf<DisplacementClass>()
    val speed = mutableListOf<SpeedClass>()
    val angles = mutableListOf<AngleClass>()
    for (index in arrayOf(0, oneValues.size - 1)) {
        val oneValue = oneValues[index]
        for (unit in arrayOf(SpeedUnit.MILES_PER_HOUR, SpeedUnit.KILOMETERS_PER_HOUR)) {
            speed.add(SpeedClass(
                    className = "Speed${if (unit == SpeedUnit.MILES_PER_HOUR) "M" else "K"}$oneValue",
                    number = numbers[index],
                    oneUnit = unit,
                    displayUnit = SpeedUnit.MILES_PER_HOUR,
                    displacementClassName = "Displacement2",
                    createNumberExtensions = false
            ))
        }
        displacements.add(DisplacementClass(
                className = "Displacement$oneValue",
                number = numbers[index],
                speed = if (oneValue == 2L) speed.last() else null,
                oneUnit = if (index == 0) DistanceUnit.FOOT else DistanceUnit.MILLIMETER,
                displayUnit = if (index == 0) DistanceUnit.YARD else DistanceUnit.METER,
                createNumberExtensions = false
        ))

        if (!checkOverflow) {
            angles.add(AngleClass(
                    className = "Angle$oneValue",
                    internalType = IntType(signed, numBits / 8),
                    displayUnit = AngleUnit.entries[min(index, 1)],
                    createNumberExtensions = false,
                    allowComparisons = index == 0,
                    allowDivisionAndMultiplication = index > 0
            ))
        }
    }

    return FixieModule(
            packageName = "fixie$numBits",
            numbers = numbers,
            displacements = displacements,
            speed = speed,
            angles = angles
    )
}
