package fixie.generator

import fixie.generator.module.generateModule
import fixie.generator.parser.InvalidConfigException
import fixie.generator.parser.parseModule
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.lang.Integer.parseInt
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val options = Options()

    val directoryOption = Option.builder("d")
            .longOpt("directory")
            .desc("The directory where the module(s) should be generated, defaults to the parent directory of the config file")
            .hasArg()
            .type(File::class.java)
            .build()
    val spacesOption = Option.builder("s")
            .longOpt("spaces")
            .desc("Use N spaces for indentation")
            .hasArg()
            .type(Int::class.java)
            .build()
    val internalOption = Option.builder("i")
            .longOpt("internal")
            .desc("Internal option used for GitHub Actions")
            .hasArg()
            .type(Int::class.java)
            .build()
    val helpOption = Option.builder("h")
            .longOpt("help")
            .desc("Prints the help page")
            .build()
    val clearExistingOption = Option.builder("c")
            .longOpt("clear-existing-files")
            .desc("When a directory with the same as the module already exists, it will be deleted")
            .build()
    options.addOption(directoryOption)
    options.addOption(spacesOption)
    options.addOption(internalOption)
    options.addOption(helpOption)
    options.addOption(clearExistingOption)

    val parser = DefaultParser()
    val cmd = parser.parse(options, args)

    if (cmd.hasOption("help")) {
        println("Usage: java -jar fixie.jar [OPTION]... [CONFIG FILE]...")
        println("Generates the code for fixie config file(s)")
        println()
        println("OPTIONS:")

        fun printOption(option: Option, value: String) {
            val headerLength = 40
            var header = "  -${option.opt}, --${option.longOpt}$value"
            while (header.length < headerLength) header += " "

            val descriptionLength = 50
            val description = mutableListOf<String>()

            var descriptionOffset = 0
            while (descriptionOffset < option.description.length) {
                var nextOffset = descriptionOffset + descriptionLength
                if (nextOffset >= option.description.length) {
                    nextOffset = option.description.length
                } else {
                    while (option.description[nextOffset] != ' ') nextOffset -= 1
                    nextOffset += 1
                }
                description.add(option.description.substring(descriptionOffset until nextOffset))
                descriptionOffset = nextOffset
            }

            println(header + description[0])
            for (index in 1 until description.size) {
                for (x in 0 until headerLength) print(' ')
                println(description[index])
            }
        }

        printOption(directoryOption, "=path/to/directory")
        printOption(spacesOption, "=4")
        printOption(internalOption, "=2")
        printOption(helpOption, "")
        printOption(clearExistingOption, "")

        return
    }

    if (cmd.getOptionValue("internal") == "2") {
        val moduleNames = arrayOf("fixed-point", "angles-UShort-Double", "int8-Double-connections-40")
        for (moduleName in moduleNames) {
            val testReport = File("$moduleName/build/reports/tests/test/index.html")
            if (!testReport.exists()) {
                println("Error: expected ${testReport.absolutePath} to exist")
                exitProcess(10)
            }
            if (testReport.length() < 100) {
                println("Error: expected ${testReport.absolutePath} to be at least 100 bytes")
                exitProcess(11)
            }
        }
        return
    }

    val targetDirectory = cmd.getOptionValue(directoryOption)
    val files = cmd.args.map(::File)

    if (files.isEmpty()) {
        println("Error: not a single file was specified")
        exitProcess(1)
    }

    val modules = files.map { configFile ->
        try {
            val parent = configFile.absoluteFile.parentFile
            Pair(parent, parseModule(configFile))
        } catch (ioFailure: IOException) {
            println("Failed to open $configFile: ${ioFailure.message}")
            exitProcess(2)
        } catch (invalidConfig: InvalidConfigException) {
            println("Failed to parse $configFile: ${invalidConfig.message}")
            exitProcess(3)
        }
    }

    val spaces = if (cmd.hasOption(spacesOption)) {
        try {
            parseInt(cmd.getOptionValue(spacesOption))
        } catch (invalid: NumberFormatException) {
            println("Expected the --spaces option value to be an integer, but got ${cmd.getOptionValue(spacesOption)}")
            return
        }
    } else null

    for ((parent, fileModules) in modules) {
        for (module in fileModules) {
            val actualParent = if (targetDirectory == null) parent else File(targetDirectory)
            val moduleDirectory = File("$actualParent/${module.moduleName}")

            try {
                generateModule(
                    module, moduleDirectory, cmd.hasOption("clear-existing-files"), spaces
                )
            } catch (ioFailure: IOException) {
                println("Failed to generate module ${module.moduleName}: ${ioFailure.message}")
                exitProcess(4)
            }
        }
    }

    if (cmd.getOptionValue("internal") == "1") {
        val writer = PrintWriter("settings.gradle")
        writer.println("rootProject.name = \"fixie\"")
        writer.println()

        var isFirst = true
        for ((_, fileModules) in modules) {
            for (module in fileModules) {
                if (isFirst) {
                    writer.print("include \"${module.moduleName}\"")
                    isFirst = false
                } else {
                    writer.println(",")
                    writer.print("\t\t\"${module.moduleName}\"")
                }
            }
        }
        writer.println()
        writer.flush()
        writer.close()
    }
}
