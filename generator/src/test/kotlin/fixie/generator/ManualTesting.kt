package fixie.generator

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
