# The config format of fixie
The configuration file determines which fixed-point numbers and quantities should be generated,
and dictates their properties. It should be a JSON file or 
[GDDL2](https://github.com/gigaherz/GDDL-Java) file, and many examples
can be found in [the example-configs directory](../example-configs). All properties are
documented here.

## Root properties
The root object **requires**:
- A `moduleName` property, which is the name of the (gradle) module that will be generated.
- A `packageName` property, which is the dot-separated package in which all generated code
will be placed.

By default, the generated classes will be put in 
`parentDirectoryOfConfigFile/moduleName/src/main/kotlin/dot/separated/package/name/`,
and the unit tests will be put in `...../src/test/kotlin/.....` instead. For instance, if
the following (nearly empty) configuration file is used:
```json
{
	"moduleName": "physics-quantities",
	"packageName": "generated.physics.quantities"
}
```
The code would be stored in `physics-quantities/src/main/kotlin/generated/physics/quantities`.

## Numbers
To add 1 or more fixed-point numbers to the config, you should add a `"numbers"` property
to the root object, like:
```json
{
	"moduleName": "physics-quantities",
	"packageName": "generated.physics.quantities",
	"numbers": [
		{ ... },
		{ ... }
	]
}

```
where each number should have the following properties:

### className
The name of the fixed-point number class. This must be a valid class name in the Kotlin
programming language.

### internalType
The underlying integer type of the fixed-point number. This determines whether the
number can be negative, and how much memory it occupies. Together with the
`oneValue`, it determines the maximum value. The supported internal types are:
- `Byte`
- `UByte`
- `Short`
- `UShort`
- `Int`
- `UInt`
- `Long`
- `ULong`

### oneValue
The integer value that corresponds to the real value of 1. For instance, when
the `internalType` is `Byte` and the `oneValue` is 10:
- The maximum real value is 127 / 10 = 12.7
- The smallest positive number that can be represented is 1 / 10 = 0.1
- The fixed-point number can represent all multiples of 0.1 between -12.8 and 12.7

Generally, using a larger `internalType` allows increases the maximum value that can
be represented by the fixed-point number. Increasing the `oneValue` *decreases*
the maximum value, but *increases* the precision. When you choose `Int` or `Long`
as internal type, you can use large `oneValue`s while still getting a decent
minimum and maximum value.

You can specify the `oneValue` in the configuration file either:
- as a JSON number with an integer value
- as a JSON string that can be parsed to an integer

You could for instance specify it like 1e7 if you don't like typing 7 zeros.

### checkOverflow
You can choose whether you want the number to automatically check for 'integer'
overflow. If you set `checkOverflow` to true, the number will check whether it
overflowed after every arithmetic operation, and throw a `FixedPointException`
if it did. When you set `checkOverflow` to false, the fixed-point numbers
can overflow, just like regular integers.

Note that the API of the number does **not** depend on whether you enable
`checkOverflow`, so you can use overflow checks in development, and
regenerate the code without overflow checks before you release. If you
don't notice performance penalties from the overflow checks, you could
also just keep them on at all times, which may reduce your time spent
debugging.

### Example
An example configuration with a number is shown below:
```json
{
	"moduleName": "example-number",
	"packageName": "fixie.example",
	"numbers": [{
		"className": "FixedInt",
		"internalType": "Int",
		"oneValue": 10000,
		"checkOverflow": true
	}]
}
```

## Acceleration classes
Acceleration classes represent accelerations in 1 direction (the speed at which
an entity can increase or decrease its speed). Currently, they must use
floating-point numbers and can't use fixed-point numbers, because I can't imagine
a situation where fixed-point precision would be more useful than floating-point
precision.

### Arithmetic types
- Multiplying an *acceleration* by a *duration* yields a *speed*

### className
The `className` property of the acceleration is simply the name of the class that
will be generated for it.

### floatType
The `floatType` is the floating-point type that will be used to represent it. It
must be either `Float` or `Double`.

### speed
The `className` of the *speed* that should be linked to the acceleration, which
is optional. If you specify a *speed*, the corresponding class will be the result
type when you multiple the acceleration with a *duration*.

### createNumberExtensions
Whether extension functions should be generated on primitive integer types and
floating-point types. If you enable this, you can e.g. use 5.mps2 to specify
5 m/s^2. If you have multiple acceleration classes, at most 1 of them can enable
this.

### Example
```json
{
	"moduleName": "example-acceleration",
	"packageName": "fixie.example",
	"accelerations": [{
		"className": "Acceleration",
		"floatType": "Double",
		"speed": "SomeSpeedClassName",
		"createNumberExtensions": true
	}],
	"speed": [{
		"className": "SomeSpeedClassName",
		...
	}]
}
```

## Angles
Angle classes represent angles (like 180 degrees). Currently, all angles must
be fixed-point numbers. Angles are a good fit for fixed-point numbers because:
- The difference between e.g. 2 and 3 degrees is typically equally important
as the difference between 299 and 300 degrees.
- Integer overflow is convenient and natural since 400 degrees = 360 + 40
degrees = 40 degrees.

### Arithmetic
- An *angle* divided by a *duration* yields a *spin*

  Note that this is only allowed when you set
  `allowDivisionAndFloatMultiplication` to true

### className
The Kotlin class name of the class that will represent the Angle.

### intType
The primitive integer that will be used to represent the angle, must be
either `Byte`, `Short`, `Int`, `UByte`, etc...

Note that unlike regular fixed-point numbers, you can **not** specify a
`oneValue` for the angle because it is automatically chosen by the
generator.
- If the `intType` is signed, the 'oneValue' will be chosen such that
the maximum value is 180 degrees.
- If the `intType` is unsigned, the `oneValue` will be chosen such that
the maximum value is 360 degrees.

When you use `Short` or `UShort`, the smallest positive angle that
can be represented is 360 / 2^16 = 0.0055 degrees, which is probably
precise enough for most applications. Therefor, it's usually not
needed to use more than 2 bytes of memory to represent an angle.

### displayUnit
The `displayUnit` determines whether the `toString()` method of the
angle will show the value in degrees, or in radians. The `displayUnit`
must be either `Degrees` or `Radians`.

Note that the angle class will also have a `toString(AngleUnit)`
method that you can use to display its value in the given unit,
**regardless** of the `displayUnit`.

### createNumberExtensions
Whether extension functions should be generated on the primitive
data types. When you enable this, you can use e.g. 1.2.degrees to
denote 1.2 degrees.

### allowDivisionAndFloatMultiplication
Whether the angle class can be divided by numbers, or multiplied
by floating-point numbers. This is optional because these operations
are not very well-defined. For instance, what should the result of
300 degrees / 2 be?
- You could simply say this is 300 / 2 = 150 degrees
- But you could also argue that 300 degrees = -60 degrees and
that the result should therefor be -30 degrees = 330 degrees.

The output will depend on whether the `intType` is signed
or unsigned. The signed result will be 330 degrees whereas
the unsigned result will be 150 degrees. If you don't want
this kind of confusing behavior, you can turn
`allowDivisionAndFloatMultiplication` off.

When you multiply an angle by a non-integer number, you can get
similar problems (like 300 degrees * 0.5).

Multiplication with integers is always allowed because it is
always well-defined. Consider for instance 100 degrees +
300 degrees:
- You can simply say that 100 + 300 = 400 degrees = 40 degrees
- You can also say that 100 + 300 degrees = 100 + -60 degrees
= also 40 degrees

### allowComparisons
Just like division, comparisons are not always well-defined either.
And for the same reason, you can choose whether you want to
enable it, or not.

An example of an ill-defined problem is
`300 degrees > 100 degrees`. You can simply say that this
should be `true` because `300 > 100`, but you can also argue
that 300 degrees = -60 degrees and that `-60 < 100`. The
answer of the generated code again depends on whether `intType`
is signed or unsigned. When `intType` is unsigned, the result
will be `true`. When `intType` is signed, the result will
be `false`.

### spin
Optionally, you can link a *spin* class to the angle class. If
you do this, you can divide the angle class by a *duration* to
obtain a *spin*.

### Example
```json
{
	"moduleName": "example-angle",
	"packageName": "fixie.example",
	"angles": [{
		"className": "Angle",
		"intType": "Short",
		"displayUnit": "Degrees",
		"createNumberExtensions": true,
		"allowDivisionAndFloatMultiplication": false,
		"allowComparisons": true,
		"spin": "MySpinClassName"
	}],
	"spins": [{
		"className": "MySpinClassName",
		...
	}]
}
```

## Angular acceleration classes
Angular acceleration classes represent *angular accelerations*: the
speed at which the spin/angular velocity of an object is changed.
All angular acceleration classes must use floating-point numbers.

### Arithmetic
- The product of an *angular acceleration* and a *duration* yields a *spin*
  (angular velocity)

### className
The Kotlin class name of the class that should represent the angular
acceleration.

### floatType
The floating-point type that will represent the angular acceleration,
must be either `Float` or `Double`.

### spin
The optional class name of the *spin* that should be linked to this
angular acceleration. When specified, instances of this class can be
multiplied by instances of *Duration* to obtain an instance of the
spin class.

### createNumberExtensions
When you enable `createNumberExtensions`, you can use e.g. `10.radps2` to
obtain 10 rad/s^2.

### Example
```json
{
	"moduleName": "example-angular-acceleration",
	"packageName": "fixie.angle.acceleration",
	"angularAccelerations": [{
		"className": "AngularAcceleration",
		"floatType": "Double",
		"spin": "SpinClassName",
		"createNumberExtensions": true
	}],
	"spins": [{
		"className": "SpinClassName",
		...
	}]
}
```

## Area classes
Area classes represent areas (like 10 square meter or 10 square miles).
Currently, all area classes must use floating-point numbers, because
I think that makes most sense: areas are usually the product of two
distances/displacements, and the square root of such areas are often
used to get displacements back. This square root behavior is non-linear,
which makes it a poor fit for fixed-point precision.

### Arithmetic
- The product of a *displacement* and an *area* yields a *volume*
- The quotient of an *area* and a *displacement* yields another *displacement*
- The square root of an *area* yields a *displacement*

### className
The Kotlin class name of the class that should represent the area.

### floatType
The floating-point type that will represent the area, must be either
`Float` or `Double`.

### displayUnit
The `displayUnit` determines which unit the `toString()` method of the
area will use to display the value.

Note that the area class will also have a `toString(AreaUnit)`
method that you can use to display its value in the given unit,
**regardless** of the `displayUnit`.

### displacement
The optional name of the displacement class that you want to link to the
area. When specified, you can use `sqrt(area)` or `area / displacement`
to get an instance of the linked displacement class.

### volume
The optional name of the volume class that you want to link to the area.
When both the volume class and displacement class are specified,
you can multiply areas by instances of the displacement class to obtain
instances of the volume class.

### createNumberExtensions
When you enable `createNumberExtensions`, you can use e.g. `123.m2` to
obtain 123 m^2.

### Example
```json
{
	"moduleName": "example-area",
	"packageName": "fixie.example",
	"angles": [{
		"className": "Area",
		"floatType": "Double",
		"displayUnit": "Square inch",
		"displacement": "MyDisplacementClass",
		"volume": "MyVolumeClass",
		"createNumberExtensions": true
	}],
	"displacements": [{
		"className": "MyDisplacementClass",
		...
	}],
	"volumes": [{
		"className": "MyVolumeClass",
		...
	}]
}

```

## Density classes
Density classes represent densities of materials (like 1 kg/liter for water).
Density classes can be represented by either fixed-point numbers or
floating-point numbers.

### Arithmetic
- The product of a *density* and a *volume* yields a *mass*

### className
The Kotlin class name of the class that will represent the density.

### number
When you want to use a fixed-point density, the `number` should be the
class name of the *number* class that will represent the density.

### floatType
When you want to use a floating-point density, the `floatType` is
the type of floating-point number that will be used, which must be
either `Float` or `Double`.

### volume
The class name of the volume class that you want to link to the density
class (optional).

### mass
The class name of the mass class that you want to link to the density
class (optional). If both a `volume` and `mass` are specified,
densities can be multiplied by instances of the `volume` class to
obtain an instance of the `mass` class.

### createNumberExtensions
Whether number extension functions should be generated for the primitive
types. If you enable this, you can e.g. use 1.kgpl to denote the
density of water.

### Example
```json
{
	"moduleName": "example-density",
	"packageName": "fixie.example",
	"numbers": [{
		"className": "FixDensity",
		"internalType": "UShort",
		"oneValue": 1000,
		"checkOverflow": true
	}],
	"densities": [{
		"className": "Density",
		"number": "FixDensity",
		"volume": "MyVolumeClass",
		"mass": "MyMassClass",
		"createNumberExtensions": true
	}],
	"masses": [{
		"className": "MyMassClass",
		...
	}],
	"volumes": [{
		"className": "MyVolumeClass",
		...
	}]
}

```

## Displacement classes
Displacement classes represent displacements/distances in 1 direction. All
displacement classes must be fixed-point numbers, because I believe that
fixed-point numbers make more sense than floating-point numbers (see 
[this explanation](./floating-point-positions.md)).

### Arithmetic
- The quotient of a *displacement* and a *duration* yields
a *speed*.
- The multiplication of two *displacement*s yields an
*area*.
- The multiplication of a *displacement* and an *area*
yields a *volume*.

### className
The Kotlin class name of the class that will represent
the displacement.

### number
The `number` is the class name of the fixed-point number
class that will represent the displacement.

### oneUnit
The internal distance unit of the displacement. For
instance, when the one unit is kilometer, and
the real value of the fixed-point number is 1.0, the
value of the displacement would be 1 kilometer.

### displayUnit
The `displayUnit` determines which unit the `toString()`
method of the displacement will use to display the value
This is usually the same as the `oneUnit`, but this is
not a requirement.

Note that the displacement class will also have a
`toString(DistanceUnit)` method that you can use to
display its value in the given unit, **regardless** 
of the `displayUnit`.
 
### speed
The name of the speed class to which the displacement
should be linked (optional). When specified, instances
of the displacement class can be divided by a duration
(a class that is part of the Kotlin standard library)
to obtain an instance of the speed class.

### area
The name of the area class to which the displacement
should be linked (optional). When specified, instances
of the displacement class can be multiplied by other
instances to obtain an instance of the area class.

### volume
The name of the volume class to which the displacement
should be linked (optional). When both the area and
volume are specified, instances of the displacement
class can be multiplied by instances of the area
class to obtain an instance of the volume class.

### createNumberExtensions
Whether extension functions should be generated on
the primitive number types. When this is enabled,
you can e.g. use 1.5.km to obtain 1.5 kilometers.

### Example
```json
{
	"moduleName": "example-displacement",
	"packageName": "fixie.example",
	"numbers": [{
		"className": "FixDisplacement",
		"internalType": "Int",
		"oneValue": 1e5,
		"checkOverflow": true
	}],
	"displacements": [{
		"className": "Displacement",
		"number": "FixDisplacement",
		"oneUnit": "Meter",
		"displayUnit": "Mile",
		"speed": "SpeedClass",
		"area": "AreaName",
		"volume": "MyVolumeClass",
		"createNumberExtensions": true
	}],
	"speed": [{
		"className": "SpeedClass",
		...
	}],
	"areas": [{
		"className": "AreaName",
		...
	}],
	"volumes": [{
		"className": "MyVolumeClass",
		...
	}]
}

```

## Duration classes
The fixie generator can't generate duration classes
because they are already part of the Kotlin standard
library.

## Mass classes
Mass classes represent a mass (like 10 pounds or 10 kilograms).
They must be represented by floating-point numbers.

### Arithmetic
- The product of a *mass* and a *speed* yields a *momentum*
- The quotient of a *mass* and a *volume* yields a *density*
- The quotient of a *mass* and a *density* yields a *volume*

### className
The Kotlin class name of the class that will represent the mass.

### floatType
The type of floating-point number that will be used, which must be
either `Float` or `Double`.

### displayUnit
The `displayUnit` determines which unit the `toString()`
method of the mass will use to display the value.

Note that the mass class will also have a
`toString(MassUnit)` method that you can use to
display its value in the given unit, **regardless**
of the `displayUnit`.

### density
The density class that should be linked to the mass class.

### volume
The volume class that should be linked to the mass class. When both
a density and volume are specified, instances of the mass class can
be divided by instances of the density class or volume class to
obtain an instance of the volume class or density class.

### speed
The speed class that should be linked to the mass class.

### momentum
The momentum class that should be linked to the mass class. When both
a speed and momentum are specified, instances of the mass class can
be multiplied by instances of the speed class, to obtain an instance
of the momentum class.

### createNumberExtensions
Whether extension functions should be generated on
the primitive number types. When this is enabled,
you can e.g. use 2.kg to obtain 2 kilograms.

### Example
```json
{
	"moduleName": "example-mass",
	"packageName": "fixie.example.mass",
	"masses": [{
		"className": "Mass",
		"floatType": "Double",
		"displayUnit": "Milligram",
		"density": "DensityClass",
		"volume": "VolumeClass",
		"speed": "SpeedClass",
		"momentum": "MomentumClass",
		"createNumberExtensions": true
	}],
	"densities": [{
		"className": "DensityClass",
		...
	}],
	"volumes": [{
		"className": "VolumeClass",
		...
	}],
	"speed": [{
		"className": "SpeedClass",
		...
	}],
	"momenta": [{
		"className": "MomentumClass",
		...
	}]
}
```

## Momentum classes
Momentum classes represent a momentum in 1 direction, which is
the product of a *speed* and a *mass*. They must use floating-point
numbers.

### Arithmetic
- The quotient of a *momentum* and a *speed* yields a *mass*
- The quotient of a *momentum* and a *mass* yields a *speed*
- The product of a *momentum* with another *momentum* yields a 
  'square momentum'

### className
The Kotlin class name of the class that will represent the mass.

### floatType
The type of floating-point number that will be used, which must be
either `Float` or `Double`.

### square
The optional name of the 'square momentum' class that should be linked to the
momentum class. When specified, you can use e.g. `sqrt(momentum1 * momentum2)`
to obtain the geometric mean of the two momenta.

### speed
The optional name of the *speed* class that should be linked to the
momentum class.

### mass
The optional name of the *mass* class that should be linked to the
momentum class. When both a *speed* and *mass* are specified, instances of
the momentum class can be divided by instances of the *speed* class or
*mass* class to obtain an instance of the *mass* class or *speed* class.

### createNumberExtensions
Whether extension functions should be generated on
the primitive number types. When this is enabled,
you can e.g. use 2.kg to obtain 2 kilograms.

### Example
```json
{
	"moduleName": "example-momentum",
	"packageName": "fixie.example",
	"momenta": [{
		"className": "Momentum",
		"floatType": "Float",
		"square": "SquareMomentum",
		"speed": "SpeedClass",
		"mass": "MassClass",
		"createNumberExtensions": true
	}],
	"squareMomenta": [{
		"className": "SquareMomentum",
		"floatType": "Double",
		"momentum": "Momentum",
		"createNumberExtensions": false
	}],
	"speed": [{
		"className": "SpeedClass",
		...
	}],
	"masses": [{
		"className": "MassClass",
		...
	}]
}
```

## Speed classes
Speed classes represent a speed in 1 direction. They can be
represented by either fixed-point numbers or floating-point numbers.

### Arithmetic
- The product of a *speed* and a *duration* yields
a *displacement*.
- The product of a *speed* and a *mass* yields a
*momentum*.
- The quotient of a *speed* and a *duration* yields
an *acceleration*.
- The product of a *speed* and another *speed* yields a 'square speed'

### className
The Kotlin class name of the class that will represent the speed.

### number
When you want to use a fixed-point speed, the `number` should be the
class name of the *number* class that will represent the speed.

### floatType
When you want to use a floating-point peed, the `floatType` is
the type of floating-point number that will be used, which must be
either `Float` or `Double`.

### oneUnit
The internal speed unit of the speed class. For
instance, when the one unit is kilometers per hour,
and the real value of the fixed/floating-point number
is 1.0, the value of the speed would be 1 kilometer
per hour.

### displayUnit
The `displayUnit` determines which unit the `toString()`
method of the speed will use to display the value.
This is usually the same as the `oneUnit`, but this is
not a requirement.

Note that the speed class will also have a
`toString(SpeedUnit)` method that you can use to
display its value in the given unit, **regardless** 
of the `displayUnit`.

### square
The optional 'square speed' class that should be linked to the
speed class. When specified, you can multiply two speeds by
each other to obtain a *square speed*, and you can use
`sqrt(squareSpeed)` to obtain a *speed*.

### displacement
The optional displacement class that should be linked to
this speed class. When specified, you can multiply
instances of this speed class with a *duration*, to
obtain an instance of the displacement class.

### acceleration
The optional acceleration class that should be linked to
this speed class. When specified, you can divide
instances of this class by a *duration* to obtain an
instance of the acceleration class.

### spin
The optional spin class that should be linked to this
speed class. When both a *displacement* and *spin* are
specified, the speed class will get a `toSpin(radius)`
method that will convert the speed to a *spin* of a
ball with the given `radius`.

Consider an example speed of 10m/s and an example
circle with a circumference of 5 meters (and thus
a radius of 2.5/PI meters). Executing
`10.mps.toSpin((2.5 / PI).m)` would yield a spin of
2 turns per second = 720 degrees per second.

### mass
The optional mass class that should be linked to this
speed class.

### momentum
The optional momentum class that should be linked to this
speed class. When both a *mass* and a *momentum* are
specified, instances of this speed class can be multiplied
by instances of the *mass* class to obtain an instance of
the *momentum* class.

### createNumberExtensions
Whether extension functions should be generated on
the primitive number types. When this is enabled,
you can e.g. use 2.mps to obtain 2.5 meters per second.

### Example
```json
{
	"moduleName": "example-speed",
	"packageName": "fixie.example.speed",
	"numbers": [{
		"className": "FixSpeed",
		"internalType": "Int",
		"oneValue": 1e6,
		"checkOverflow": false
	}],
	"speed": [{
		"className": "Speed",
		"number": "FixSpeed",
		"oneUnit": "Meters per second",
		"displayUnit": "Miles per hour",
		"square": "SquareSpeed",
		"displacement": "DisplacementClass",
		"acceleration": "AccelerationClass",
		"spin": "SpinClass",
		"mass": "MassClass",
		"momentum": "MomentumClass",
		"createNumberExtensions": true
	}],
	"squareSpeed": [{
		"className": "SquareSpeed",
		"floatType": "Double",
		"speed": "Speed",
		"createNumberExtensions": false
	}],
	"displacements": [{
		"className": "DisplacementClass",
		...
	}],
	"accelerations": [{
		"className": "AccelerationClass",
		...
	}],
	"spins": [{
		"className": "SpinClass",
		...
	}],
	"masses": [{
		"className": "MassClass",
		...
	}],
	"momenta": [{
		"className": "MomentumClass",
		...
	}]
}
```

## Spin classes
Spin classes represent a rotation speed, for instance
50 degrees per second. To keep the name short, I call
them *spin* classes. Currently, spin classes can only
be represented by floating-point numbers, but I might
change this in the future.

### Arithmetic
- The product of a *spin* and a *duration* is an *angle*.
- The quotient of a *spin* and a *duration* yields an *angular acceleration*

### className
The Kotlin class name of the class that will represent the spin.

### floatType
The floating-point type that will represent the spin,
must be either `Float` or `Double`.

### oneUnit
The internal spin unit of the spin class. For
instance, when the one unit is radians per second,
and the floating-point value is 1.0, the value of
the spin would be 1 radian per second.

### displayUnit
The `displayUnit` determines which unit the `toString()`
method of the spin will use to display the value.
This is usually the same as the `oneUnit`, but this is
not a requirement.

Note that the spin class will also have a
`toString(SpinUnit)` method that you can use to
display its value in the given unit, **regardless** 
of the `displayUnit`.

### angle
The optional class name of the *angle* class to which
this spin class should be linked. When linked, you can
multiply instances of this spin class with a *duration*
to obtain an instance of the angle class.

### acceleration
The optional class name of the *angular acceleration* class
to which this spin class should be linked. When linked, you
can divide instances of this spin class by a *duration*
to obtain an instance of the angular acceleration class.

### displacement
The optional displacement class that should be linked to
this spin class.

### speed
The optional speed class that should be linked to this
spin class. When both a *displacement* and *speed* are
specified, the spin class will get a `toSpeed(radius)`
method that will convert the spin for a ball with the
given `radius` to a *speed*.

Consider an example spin of 90 degrees per second and 
an example circle with a circumference of 5 meters (and thus
a radius of 2.5/PI meters). Executing
`90.degps.toSpeed((2.5 / PI).m)` would yield a speed of
1.25 meters per second.

### createNumberExtensions
Whether extension functions should be generated on primitive
integer types and floating-point types. If you enable this,
you can e.g. use 100.degps to specify 100 degrees per second.
If you have multiple spin classes, at most 1 of them
can enable this.

### Example
```json
{
	"moduleName": "example-spin",
	"packageName": "fixie.example",
	"spins": [{
		"className": "Spin",
		"floatType": "Double",
		"oneUnit": "Radians per second",
		"displayUnit": "Degrees per second",
		"angle": "AngleClass",
		"acceleration": "AngularAccelerationClass",
		"displacement": "DisplacementClass",
		"speed": "SpeedClass",
		"createNumberExtensions": true
	}],
	"angles": [{
		"className": "AngleClass",
		...
	}],
	"angularAccelerations": [{
		"className": "AngularAccelerationClass",
		...
	}],
	"displacements": [{
		"className": "DisplacementClass",
		...
	}],
	"speed": [{
		"className": "SpeedClass"
	}]
}
```

## Volume classes
Volume classes represent volumes (like 1 liter or 5 cubic meter).
All volume classes must be represented by floating-point numbers,
for the same reason as area classes.

### Arithmetic
- The quotient of a *volume* and a *displacement* yields an *area*
- The quotient of a *volume* and an *area* yields a *displacement*
- The product of a *volume* and a *density* yields a *mass*

### className
The `className` property of the volume is simply the name of the class that
will be generated for it.

### floatType
The `floatType` is the floating-point type that will be used to represent it. It
must be either `Float` or `Double`.

### displayUnit
The `displayUnit` determines the unit that will be used in the `toString()`
method of the volume.

Note that the angle class will also have a `toString(VolumeUnit)`
method that you can use to display its value in the given unit,
**regardless** of the `displayUnit`.

### displacement
The optional displacement class that should be linked to this volume class.

### area
The optional area class that should be linked to this volume class. When
both *displacement* and *area* are specified, you can:
- divide an instance of this volume class by an instance of the
displacement class, to get an instance of the area class
- divide an instance of this volume class by an instance of the
area class, to get an instance of the displacement class

### density
The optional density class that should be linked to this volume class.

### mass
The optional mass class that should be linked to this volume class. When
both *density* and *mass* are specified, you can multiply an instance of
this volume class with an instance of the density class, to get an
instance of the mass class.

### createNumberExtensions
Whether extension functions should be generated on the primitive
data types. When you enable this, you can use e.g. 1.2.m3 to
denote 1.2 cubic meters.

###
```json
{
	"moduleName": "example-volume",
	"packageName": "fixie.example",
	"displacements": [{
		"className": "Displacement",
		...
	}],
	"areas": [{
		"className": "Area",
		...
	}],
	"densities": [{
		"className": "Density",
		...
	}],
	"masses": [{
		"className": "Mass",
		...
	}],
	"volumes": [{
		"className": "Volume",
		"floatType": "Double",
		"displayUnit": "Liter",
		"displacement": "Displacement",
		"area": "Area",
		"density": "Density",
		"mass": "Mass",
		"createNumberExtensions": true
	}]
}
```

## Variations
Variations allow you to specify that multiple variations of the same
module should be generated. This is very useful internally since it can
be used to generate a very large set of modules to test in the GitHub
Actions flow. Perhaps it would also be useful for other developers.

### Structure
To use variations, assign a `variations` array to the configuration file.
It must be a JSON list/array of *variation objects*.

Each *variation object* can have any number of keys + values. All values
should be a JSON list, and all values of the same *variation object* must
have the same length. For instance, the following would be valid:
```json
{
	"variations": [
		{
			"hello": [ "world", "fixie" ],
			"code": [ 1, 2 ]
		},
		{
			"test": [ 1234 ]
		}
	]
}
```
but the following would be invalid because the length of `[ 1234 ]` is 1, whereas
the other lengths are 2:
```json
{
	"variations": [
		{
			"hello": [ "world", "fixie" ],
			"code": [ 1, 2 ],
			"test": [ 1234 ]
		}
	]
}
```

### Combining the variation objects
Each *variation object* will be zipped, and the *full variation list* will be
the cartesian product of all *variation object*s. In the first example, the
first *variation object* will be zipped to
```json
[
	{
		"hello": "world",
		"code": 1
	},
	{
		"hello": "fixie",
		"code": 2
	}
]
```
and the second object will be zipped to `[{ "test": 1234 }]`. Continuing this
example, the *full variation list* would be:
```json
[
	{
		"hello": "world",
		"code": 1,
		"test": 1234
	},
	{
		"hello": "fixie",
		"code": 2,
		"test": 1234
	}
]
```

### Using the variation objects
The generator will generate 1 module per *variation entry* in the
*full variation list*. In the example above, 2 modules would be
generated.

For each module to be generated, all occurrences of the *keys* of the
*variation entry* in the property values of the rest of the configuration file
will be substituted by the corresponding *value* of the *variation entry*.
Consider for instance the following configuration file:
```json
{
	"moduleName": "int8-fixed-%OVERFLOW%-%ONE%",
	"packageName": "generated",
	"numbers": [
		{
			"className": "Fixed",
			"internalType": "Byte",
			"oneValue": "%ONE%",
			"checkOverflow": "%OVERFLOW%"
		}
	],
	"variations": [
		{
			"%OVERFLOW%": [
				true,
				false
			]
		},
		{
			"%ONE%": [
				2,
				30,
				127
			]
		}
	]
}
```
The generator will generate 6 modules named `int8-fixed-true-2`,
`int8-fixed-true-30`, `int8-fixed-true-127`, `int8-fixed-false-2`,
`int8-fixed-false-30`, and `int8-fixed-false-127`.

Each generated module will have 1 signed 8-bit fixed-point number named
"Fixed". The 3 `int8-fixed-true-x` modules will check for overflow, and
the other 3 won't. The 2 `int8-fixed-x-2` modules will use a *oneValue* of
2, the 2 `int8-fixed-x-30` modules will use a *oneValue* of 30, etc.

### Considerations
When you add *variation object*s with a value length > 1, the number of
generated modules will grow exponentially, so you probably shouldn't do
that. This behavior is great for the GitHub Actions workflow though.

When you add *variation object*s with a value length of 1, you are basically
adding indirection to the config file, which would be useful.

Note: since all modules are required to have a distinct name, you must use
a variation in the `moduleName`.