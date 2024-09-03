package fixie.generator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.nio.file.Files
import java.util.Scanner
import kotlin.io.path.absolutePathString

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
		assertNotNull(remainingLines.find { line -> line.contains("When a directory with the same as ") })
	}

	@Test
	fun testTabsIndentation() {
		val tabsFolder = Files.createTempDirectory("")
		main(arrayOf("-d", tabsFolder.absolutePathString(), "example-configs/balls2d.gddl"))
		assertEquals(0, countOccurrences(tabsFolder.toFile(), "    "))
		assertEquals(0, countOccurrences(tabsFolder.toFile(), "  "))
		assertTrue(countOccurrences(tabsFolder.toFile(), "\t") > 100)
	}

	@Test
	fun testSpacesIndentation() {
		val spacesFolder = Files.createTempDirectory("")
		main(arrayOf("-d", spacesFolder.absolutePathString(), "-s", "4", "example-configs/balls2d.gddl"))
		assertEquals(0, countOccurrences(spacesFolder.toFile(), "\t"))
		assertTrue(countOccurrences(spacesFolder.toFile(), "    ") > 100)
	}

	private fun countOccurrences(file: File, target: String): Long {
		var result = 0L

		if (file.isFile) {
			val scanner = Scanner(file)
			while (scanner.hasNextLine()) {
				var line = scanner.nextLine()
				while (line.contains(target)) {
					result += 1
					line = line.replaceFirst(target, "")
				}
			}
			scanner.close()
		}

		if (file.isDirectory) {
			for (child in file.listFiles()) result += countOccurrences(child, target)
		}

		return result
	}
}
