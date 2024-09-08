# Fixie
## Kotlin code generator for fixed-point numbers and a system of units

### Representing (physics) quantities
In physics 'engines', there are many quantities that need to be tracked
(e.g. distances, speed, and mass). They are typically represented by 'raw'
(floating-point) numbers. While this approach is simple, it is not always
clear what the *units* of the quantities are (kg or pounds? m/s or km/h or mi/h?).

Furthermore, there won't be any type safety since e.g. speed typically has the same
type as coordinates and accelerations. I believe the situation can be improved
by creating nice wrapper classes for quantities, which is exactly what fixie does.
With the right configuration, the following code would be type-safe, and actually
compile:
```kotlin
var positionX = 10.m // Start at x = 10 meters
positionX += 5.ft // move 5 feet to the right

val speed = 20.km / 4.hours // = 5 kilometers per hour = 5.kmph
positionX -= 30.seconds * speed // move left at a speed of 5 km/h, for 30 seconds long
```
Due to the type safety, the following lines would **not** compile:
```kotlin
positionX += 5.seconds
positionX -= 5.kmph
```
The possibilities and precision will depend on a configuration file.
For instance, the configuration file below should allow you to compile
the code above.
```json
{
	"moduleName": "example-displacement",
	"packageName": "example.displacement",
	"numbers": [{
		"className": "Fixed",
		"internalType": "Int",
		"oneValue": 1e5,
		"checkOverflow": true
	}],
	"displacements": [{
		"className": "Displacement",
		"number": "Fixed",
		"oneUnit": "Meter",
		"displayUnit": "Yard",
		"speed": "Speed",
		"createNumberExtensions": true
	}],
	"speed": [{
		"className": "Speed",
		"number": "Fixed",
		"oneUnit": "Kilometers per hour",
		"displayUnit": "Kilometers per hour",
		"displacement": "Displacement",
		"createNumberExtensions": true
	}]
}
```
A more advanced config that I use for my 2d physics engine can be found
[here](./example-configs/balls2d.gddl).

### Floating-point numbers vs fixed-point numbers
Floating-point numbers are almost always used to represent real numbers on a computer.
Their floating behavior allows them to represent both very small numbers and very large numbers,
and their precision is greatest when working with small numbers, where it is usually needed the most.

Fixed-point numbers provide an alternative way to represent real numbers: they are basically integers,
where some integer value N represents the real value 1. In this representation, the precision
of small numbers is exactly the same as the precision of large numbers.

In most cases, the precision of small numbers is much more important than the precision of large numbers
(e.g. the difference between 0.10 and 0.11 is more important than the difference between 100.00 and 100.01).
This is why floating-point numbers are used by almost all programs and supported by almost all processors.

While this floating-point behavior is almost always great, there are cases where it is questionable.
An example is a coordinate system: it is usually not desirable that the precision of the system deteriorates
when objects move further away from the origin. When fixed-point coordinates are used instead, the precision
of the system is the same at every location (until the point where it overflows, which would be the boundary).
This example is explained in more detail [here](docs/floating-point-positions.md). It was in fact my
motivation to start this project!

### The fixie generator
This project is a code generator for quantity classes and fixed-point
number classes. Given a configuration file, it will spit out a kotlin module
with all the requested classes, plus some unit tests.
Some quantity classes must be represented by fixed-point numbers, and some
must be represented by floating-point numbers. For some quantities, you can
choose it yourself. (Future versions of fixie might allow you to choose
between fixed-point and floating-point for any quantity.)

All generated classes are designed to be efficient (inline classes),
nice to work with (e.g. operator overloading), and convenient to
include in projects (minimal dependencies). See
[this page](./docs/generated-classes-fundamentals.md) for details.

## Usage
### Using fixie.jar
The recommended way to use fixie is by downloading fixie.jar from the releases,
and running `java -jar fixie.jar path/to/config.json`, where `config.json` is
your [configuration file](./docs/config.md), which should be located in the
root directory of your project. Generating the code requires Java 17 or later,
but compiling the code is also possible with Java 11 (and sometimes even Java 8).
Run `java -jar fixie.jar --help` to see the supported command-line arguments.

At the time of writing this, fixie.jar is smaller than 300KB, so you could
simply add it to your GitHub repository. Each release also contains a
fixie-debug.jar, which is the unminified version of fixie.jar

### Adding the fixie generator as dependency
It is also possible to add fixie as a dependency, and call the code generator via
your own project. If you want to do this, you can use Jitpack to add fixie as
dependency. This approach is not recommended, and more likely to break after
updates, but it should work.

## Bugs?
When fixie generates a module, it's supposed to compile, and its unit tests are
supposed to pass. When this is not the case, you can consider it a bug in the
generator. If you want this to be fixed, you can create an issue containing the
corresponding config file.

When a config file is invalid, fixie should report an error, and **not** generate
a module.

## Supported quantities
- [x] acceleration
- [x] angle
- [x] angular acceleration
- [x] angular speed (spin)
- [ ] angular momentum
- [x] area
- [x] density
- [x] displacement
- [ ] energy
- [ ] force
- [ ] frequency
- [x] mass
- [x] momentum
- [x] speed
- [ ] torque
- [x] volume