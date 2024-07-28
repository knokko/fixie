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
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val options = Options()

    val directoryOption = Option.builder("d")
            .longOpt("directory")
            .desc("The directory where the module(s) should be generated, defaults to the parent directory of the config file")
            .hasArg()
            .type(File::class.java)
            .build()
    options.addOption("i", "internal", true, "Internal option used for Github Actions")
    options.addOption("h", "help", false, "Print help information")
    options.addOption(
            "c", "clear-existing-files", false,
            "When a directory with the same as the module already exists, it will be deleted"
    )
    options.addOption(directoryOption)

    // TODO Spaces option

    val parser = DefaultParser()
    val cmd = parser.parse(options, args)

    if (cmd.hasOption("help")) {
        // TODO Finish this
        println("Usage: java -jar fixie.jar [OPTION...] [FILE...]")
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

    for ((parent, fileModules) in modules) {
        for (module in fileModules) {
            val actualParent = if (targetDirectory == null) parent else File(targetDirectory)
            val moduleDirectory = File("$actualParent/${module.moduleName}")

            try {
                generateModule(module, moduleDirectory, cmd.hasOption("clear-existing-files"))
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
