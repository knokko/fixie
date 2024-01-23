package fixie.generator

import java.io.File
import java.io.PrintWriter
import java.math.BigInteger

fun main() {
    val number = NumberClass(
            className = "FixMicro64",
            internalType = IntType(true, 8),
            oneValue = BigInteger.valueOf(1024L * 1024L),
            checkOverflow = true
    )
    val writer = PrintWriter(File("src/main/kotlin/fixie/FixMicro64.kt"))
    NumberClassGenerator(writer, number).generate()
    writer.flush()
    writer.close()

    val testWriter = PrintWriter(File("src/test/kotlin/fixie/TestFixMicro64.kt"))
    NumberTestsGenerator(testWriter, number).generate()
    testWriter.flush()
    testWriter.close()
}
