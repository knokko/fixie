package fixie.generator.parser

import dev.gigaherz.util.gddl2.GDDL
import dev.gigaherz.util.gddl2.exceptions.ParserException
import dev.gigaherz.util.gddl2.structure.GddlDocument
import dev.gigaherz.util.gddl2.structure.GddlMap
import dev.gigaherz.util.gddl2.structure.GddlValue
import fixie.generator.acceleration.AccelerationClass
import fixie.generator.angle.AngleClass
import fixie.generator.angle.AngleUnit
import fixie.generator.area.AreaClass
import fixie.generator.area.AreaUnit
import fixie.generator.displacement.DisplacementClass
import fixie.generator.displacement.DistanceUnit
import fixie.generator.module.FixieModule
import fixie.generator.number.FloatType
import fixie.generator.number.IntType
import fixie.generator.number.NumberClass
import fixie.generator.speed.SpeedClass
import fixie.generator.speed.SpeedUnit
import fixie.generator.spin.SpinClass
import fixie.generator.spin.SpinUnit
import fixie.generator.uLongToBigInteger
import java.io.File
import java.lang.Double.parseDouble
import java.math.BigInteger
import java.util.*

@Throws(InvalidConfigException::class)
fun parseModule(document: GddlDocument): List<FixieModule> {
    val modules = ModuleParser(document, null).result
    val moduleNames = mutableSetOf<String>()
    for (module in modules) {
        if (moduleNames.contains(module.moduleName)) throw InvalidConfigException(
                "Duplicate moduleName ${module.moduleName}; you must use all variations in the moduleName to avoid this"
        )
        moduleNames.add(module.moduleName)
    }
    return modules
}

@Throws(InvalidConfigException::class)
fun parseModule(file: File) = try {
    parseModule(GDDL.fromFile(file))
} catch (invalid: ParserException) {
    throw InvalidConfigException("Failed to parse $file: ${invalid.message}")
}

@Throws(InvalidConfigException::class)
fun parseModule(body: String) = try {
    parseModule(GDDL.fromString(body))
} catch (invalid: ParserException) {
    throw InvalidConfigException("Failed to parse body: ${invalid.message}")
}

private class ModuleParser(
        private val document: GddlDocument,
        private val moduleVariation: SpecificVariation?
) {

    val result by lazy { parseModule() }

    lateinit var numbers: List<NumberClass>

    private fun substitute(text: String): String {
        if (moduleVariation == null) return text

        var replacement = text
        for ((key, value) in moduleVariation.map) {
            replacement = replacement.replace(key, value)
        }
        return replacement
    }

    private fun requiredString(parent: GddlMap, key: String, path: String): String {
        val value = parent.get(key) ?: throw InvalidConfigException("Missing required string property $path.$key")
        if (!value.isString) throw InvalidConfigException("Expected property $path.$key to be a string, but got $value")
        return substitute(value.stringValue())
    }

    private fun optionalString(parent: GddlMap, key: String, path: String): String? {
        val value = parent.get(key) ?: return null
        if (value.isNull) return null
        if (!value.isString) throw InvalidConfigException("Expected property $path.$key to be a string, but got $value")
        return substitute(value.stringValue())
    }

    private fun requiredBoolean(parent: GddlMap, key: String, path: String): Boolean {
        val value = parent.get(key) ?: throw InvalidConfigException("Missing required boolean property $path.$key")
        if (value.isBoolean) return value.booleanValue()
        if (value.isString) {
            val stringValue = substitute(value.stringValue())
            if (stringValue == "true") return true
            if (stringValue == "false") return false
            throw InvalidConfigException("Expected property $path.$key to be a boolean, but got $stringValue")
        }
        throw InvalidConfigException("Expected property $path.$key to be a boolean, but got $value")
    }

    private fun requiredInteger(parent: GddlMap, key: String, path: String): BigInteger {
        var value = parent.get(key) ?: throw InvalidConfigException("Missing required integer property $path.$key")
        if (value.isInteger) return BigInteger.valueOf(value.toLong())
        if (value.isString) {
            val stringValue = substitute(value.stringValue())
            try {
                return BigInteger(stringValue)
            } catch (noBigInteger: NumberFormatException) {
                try {
                    val doubleValue = parseDouble(stringValue)
                    value = GddlValue.of(doubleValue)
                } catch (noDouble: NumberFormatException) {
                    throw InvalidConfigException("Property $path.$key ($stringValue) is not a valid integer")
                }
            }
        }
        if (value.isDouble) {
            val doubleValue = value.toDouble()
            return if (doubleValue > 0.0) {
                val longValue = doubleValue.toULong()
                if (longValue.toDouble() != doubleValue) {
                    throw InvalidConfigException("Property $path.$key ($doubleValue) can't be converted to ULong exactly")
                }

                uLongToBigInteger(longValue)
            } else {
                val longValue = doubleValue.toLong()
                if (longValue.toDouble() != doubleValue) {
                    throw InvalidConfigException("Property $path.$key ($doubleValue) can't be converted to Long exactly")
                }
                BigInteger.valueOf(longValue)
            }
        }

        throw InvalidConfigException("Expected property $path.$key to be an integer, but got $value")
    }

    private fun <T> mapList(parent: GddlMap, key: String, path: String, loadElement: (GddlMap, String) -> T): List<T> {
        val value = parent.get(key) ?: return emptyList()
        if (!value.isList) throw InvalidConfigException("Expected property $path.$key to be a list, but got $value")
        return value.asList().mapIndexed { index, element ->
            if (element == null || !element.isMap) {
                throw InvalidConfigException("Expected all elements of $path.$key to be maps, but found a $element at index $index")
            }
            loadElement(element.asMap(), "$path.$key[$index]")
        }
    }

    private fun parseIntType(name: String) = when (name) {
        "Byte" -> IntType(true, 1)
        "UByte" -> IntType(false, 1)
        "Short" -> IntType(true, 2)
        "UShort" -> IntType(false, 2)
        "Int" -> IntType(true, 4)
        "UInt" -> IntType(false, 4)
        "Long" -> IntType(true, 8)
        "ULong" -> IntType(false, 8)
        else -> null
    }

    private fun requiredIntType(parent: GddlMap, key: String, path: String): IntType {
        val name = requiredString(parent, key, path)
        return parseIntType(name) ?: throw InvalidConfigException("Unknown IntType $name at $path.$key")
    }

    private fun <T: Enum<*>> requiredUnit(parent: GddlMap, key: String, path: String, entries: List<T>): T {
        val name = requiredString(parent, key, path)
        return entries.find { it.name == name.uppercase(Locale.ROOT).replace(' ', '_') } ?: throw InvalidConfigException(
            "Invalid value for $path.$key: $name, expected one of ${entries.map { it.name }}"
        )
    }

    private fun requiredNumber(parent: GddlMap, key: String, path: String): NumberClass {
        val name = requiredString(parent, key, path)
        return numbers.find { it.className == name } ?: throw InvalidConfigException(
            "Expected $path.$key ($name) to match the className of a number, but no such number was found"
        )
    }

    private fun optionalNumber(parent: GddlMap, key: String, path: String): NumberClass? {
        val name = optionalString(parent, key, path) ?: return null
        return numbers.find { it.className == name } ?: throw InvalidConfigException(
            "Expected $path.$key ($name) to match the className of a number, but no such number was found"
        )
    }

    private fun requiredFloatType(parent: GddlMap, key: String, path: String) = when (requiredString(parent, key, path)) {
        "Float" -> FloatType.SinglePrecision
        "Double" -> FloatType.DoublePrecision
        else -> throw InvalidConfigException("Expected $path.$key to be either Float or Double")
    }

    private fun optionalFloatType(parent: GddlMap, key: String, path: String) = when (optionalString(parent, key, path)) {
        "Float" -> FloatType.SinglePrecision
        "Double" -> FloatType.DoublePrecision
        null -> null
        else -> throw InvalidConfigException("Expected $path.$key to be either Float or Double")
    }

    private fun loadNumber(properties: GddlMap, path: String) = NumberClass(
        parsePath = path,
        className = requiredString(properties, "className", path),
        internalType = requiredIntType(properties, "internalType", path),
        oneValue = requiredInteger(properties, "oneValue", path),
        checkOverflow = requiredBoolean(properties, "checkOverflow", path)
    )

    private fun loadAcceleration(properties: GddlMap, path: String) = AccelerationClass(
        className = requiredString(properties, "className", path),
        floatType = requiredFloatType(properties, "floatType", path),
        speedClassName = optionalString(properties, "speed", path),
        createNumberExtensions = requiredBoolean(properties, "createNumberExtensions", path)
    )

    private fun loadAngle(properties: GddlMap, path: String) = AngleClass(
        className = requiredString(properties, "className", path),
        internalType = requiredIntType(properties, "intType", path),
        displayUnit = requiredUnit(properties, "displayUnit", path, AngleUnit.entries),
        createNumberExtensions = requiredBoolean(properties, "createNumberExtensions", path),
        allowDivisionAndFloatMultiplication = requiredBoolean(properties, "allowDivisionAndFloatMultiplication", path),
        allowComparisons = requiredBoolean(properties, "allowComparisons", path),
        spinClassName = optionalString(properties, "spin", path)
    )

    private fun loadArea(properties: GddlMap, path: String) = AreaClass(
        className = requiredString(properties, "className", path),
        floatType = requiredFloatType(properties, "floatType", path),
        displayUnit = requiredUnit(properties, "displayUnit", path, AreaUnit.entries),
        displacementClassName = optionalString(properties, "displacement", path),
        createNumberExtensions = requiredBoolean(properties, "createNumberExtensions", path)
    )

    private fun loadDisplacement(properties: GddlMap, path: String) = DisplacementClass(
        className = requiredString(properties, "className", path),
        number = requiredNumber(properties, "number", path),
        speedClassName = optionalString(properties, "speed", path),
        areaClassName = optionalString(properties, "area", path),
        oneUnit = requiredUnit(properties, "oneUnit", path, DistanceUnit.entries),
        displayUnit = requiredUnit(properties, "displayUnit", path, DistanceUnit.entries),
        createNumberExtensions = requiredBoolean(properties, "createNumberExtensions", path)
    )

    private fun loadSpeed(properties: GddlMap, path: String) = SpeedClass(
        className = requiredString(properties, "className", path),
        number = optionalNumber(properties, "number", path),
        floatType = optionalFloatType(properties, "floatType", path),
        oneUnit = requiredUnit(properties, "oneUnit", path, SpeedUnit.entries),
        displayUnit = requiredUnit(properties, "displayUnit", path, SpeedUnit.entries),
        displacementClassName = optionalString(properties, "displacement", path),
        accelerationClassName = optionalString(properties, "acceleration", path),
        createNumberExtensions = requiredBoolean(properties, "createNumberExtensions", path)
    )

    private fun loadSpin(properties: GddlMap, path: String) = SpinClass(
        className = requiredString(properties, "className", path),
        floatType = requiredFloatType(properties, "floatType", path),
        oneUnit = requiredUnit(properties, "oneUnit", path, SpinUnit.entries),
        displayUnit = requiredUnit(properties, "displayUnit", path, SpinUnit.entries),
        angleClassName = optionalString(properties, "angle", path),
        createNumberExtensions = requiredBoolean(properties, "createNumberExtensions", path)
    )

    private fun loadVariation(properties: GddlMap, path: String): Variation {
        val rawMap = mutableMapOf<String, List<String>>()
        for ((key, value) in properties.entries) {
            if (!value.isList) throw InvalidConfigException("Expected value of $path.$key to be a list, but got $value")
            rawMap[key] = value.asList().map {
                val stringValue = it.toString()
                if (stringValue.startsWith("\"") && stringValue.endsWith("\"")) {
                    stringValue.substring(1 until stringValue.length - 1)
                } else stringValue
            }
        }
        return Variation(path, rawMap)
    }

    private fun parseModule(): List<FixieModule> {
        val config = document.root
        if (!config.isMap) throw InvalidConfigException("Expected root to be a map")
        val root = config.asMap()

        if (moduleVariation == null) {
            val variations = mapList(root, "variations", "(root)", ::loadVariation)
            if (variations.isNotEmpty()) {
                val specificVariations = Variation.allCombinations(variations)
                return specificVariations.flatMap { variation -> ModuleParser(document, variation).result }
            }
        }

        val moduleName = requiredString(root, "moduleName", "(root)")
        val packageName = requiredString(root, "packageName", "(root)")
        this.numbers = mapList(root, "numbers", "(root)", ::loadNumber)

        val accelerations = mapList(root, "accelerations", "(root)", ::loadAcceleration)
        val angles = mapList(root, "angles", "(root)", ::loadAngle)
        val areas = mapList(root, "areas", "(root)", ::loadArea)
        val displacements = mapList(root, "displacements", "(root)", ::loadDisplacement)
        val speed = mapList(root, "speed", "(root)", ::loadSpeed)
        val spins = mapList(root, "spins", "(root)", ::loadSpin)

        return listOf(FixieModule(
            moduleName = moduleName,
            packageName = packageName,
            numbers = numbers,
            accelerations = accelerations,
            angles = angles,
            areas = areas,
            displacements = displacements,
            speed = speed,
            spins = spins
        ))
    }
}
