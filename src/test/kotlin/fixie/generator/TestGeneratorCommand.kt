package fixie.generator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.Scanner

class TestGeneratorCommand {

	@Test
	fun testHelp() {
		val outputBuffer = ByteArrayOutputStream()
		val testOut = PrintStream(outputBuffer)

		val oldOut = System.out
		System.setOut(testOut)

		main(arrayOf("-h"))

		System.setOut(oldOut)

		val scanner = Scanner(ByteArrayInputStream(outputBuffer.toByteArray()))
		assertEquals("Usage: java -jar fixie.jar [OPTION]... [CONFIG FILE]...", scanner.nextLine())

		val remainingLines = ArrayList<String>()
		while (scanner.hasNextLine()) remainingLines.add(scanner.nextLine())
		scanner.close()

		assertNotNull(remainingLines.find { line -> line.contains(" -d, --directory=path/to/directory ") })
		assertNotNull(remainingLines.find { line -> line.contains( "When a directory with the same as ") })
	}
}
