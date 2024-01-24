package fixie.generator

import java.io.File
import java.io.PrintWriter
import java.math.BigInteger

fun main() {
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
}

private fun generate(number: NumberClass) {
    val writer = PrintWriter(File("src/main/kotlin/fixie/${number.className}.kt"))
    NumberClassGenerator(writer, number).generate()
    writer.flush()
    writer.close()

    val testWriter = PrintWriter(File("src/test/kotlin/fixie/Test${number.className}.kt"))
    NumberTestsGenerator(testWriter, number).generate()
    testWriter.flush()
    testWriter.close()
}
