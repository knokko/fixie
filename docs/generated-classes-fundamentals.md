## Fundamentals for generated classes

### Performance
All number classes and quantity classes will be **inline** classes,
which means that no objects will be allocated on the heap when you
create them. Furthermore, object allocation will be avoided whenever
that is reasonably possible. The exceptions are:
- The `toString()` methods: first of all, `String`s are objects,
so it's inevitable. Also, some objects may be allocated for
formatting. This should normally be fine since you shouldn't
call `toString()` in extremely performance-sensitive code anyway.
- Sometimes multiplication and division of 64-bit fixed-point
numbers: to deal with intermediate overflow in multiplication
and division of fixed-point numbers, a temporary larger
number is sometimes needed. Since Java and Kotlin don't
have 128-bit primitive numbers, it may be required to use
`BigInteger`. Fixie will avoid this whenever it can
(e.g. bitshifts when the *one value* is a power of 2),
but this is not always possible.

In most cases, the performance of fixed-point numbers will be
slightly worse than that of floating-point numbers. 
Note however that you need pretty weird cases/benchmarks 
to notice this since modern processors are much more often 
limited by control flow or memory latency than by arithmetic.
In some cases, you would not even notice that multiplications
and divisions with large numbers allocate objects.

Overflow checks for fixed-point numbers are optional: you
can either enable or disable them in the configuration file.
When they are disabled, they won't even appear in the
generated code. Conveniently, numbers with and without
overflow checks have the same API, so you can use overflow
checks during development and easily ditch them when you
release.

### Convenience
Performance may be important, but it's certainly not
everything. Development convenience is also important,
or you would hate your life/work.

#### Operator overloading
The most important convenience feature is operator
overloading. All fixed-point numbers support the 
arithmetic operators (+, -, \*, /) with its own
class, as well as `Int`, `Long`, `Float`,
and `Double`. Some fixed-point numbers also
support arithmetic with unsigned integers
and/or smaller integers types.

All quantity classes support multiplication
with and division by `Int`, `Long`, `Float`,
and `Double` (e.g. `3 meter * 5 = 15 meter`). 
They do **not** support addition and 
subtraction with those types
(what would `2 meter + 5` mean anyway?).
They do support addition and subtraction
with their own class 
(e.g. `1 meter + 1 foot`).

Multiplication and division between
quantity classes is sometimes
supported: it depends on the quantities.
For instance, 
`6 meter / 2 seconds = 3 meter/second`,
but `6 meter * 2 seconds` is not defined
(it will be a compile error).

#### Extension functions
Extension functions are also important
for the quality-of-life. While operator
overloading allows you to do
`1 meter * 5`, extension functions are
needed to allow `5 * 1 meter`.

Furthermore, extension functions
allow you to use `2.degrees` to specify
2 degrees. Without extension functions,
you would always need to do something
like `Angle.degrees(2)` or
`2 * Displacement.METER`.

#### Comparisons and equality
Fixed-point numbers and quantities can be compared
using `==`, `!=`, `<`, `<=`, `>`, and `>=`. Note however
that using `==` is questionable, just like it's
questionable to use on floating-point numbers
(e.g. the classical 0.1 + 0.2 != 0.3).

#### toString
All fixed-point classes and quantity classes will
override the `toString()` method. For fixed-point
classes, the number of digits shown after the dot
will depend on the *one value*. For quantity
classes, the number of digits after the dot usually
depends on the *display unit* (which you can configure).

### Including in other projects
To make the code convenient to include in applications,
it has only 1 dependency, which is junit5, and only
required by the generated unit tests.

#### Code location
The configuration file includes a *moduleName* and
a *packageName*. The generator will store the
generated classes in
`moduleName/src/main/kotlin/path/to/package`,
and the generated unit tests in
`moduleName/src/test/kotlin/path/to/package`.
The idea is that the user puts an
`implementation project(":moduleName")` in the
`dependencies` section of the project in their
`build.gradle`.

#### Dependencies
The generated classes don't have any dependencies,
which is convenient. The generated unit tests only
need junit5.

### Unit tests
When there are bugs in the generated code, the
application that uses the code will probably
also get bugs. Since these bugs can be pretty
annoying to debug, it's important that the
quality of the generated code is good.

To improve the quality, the code generator will
also generate unit tests. In Github Actions,
the code generator will generate code and unit
tests for *a lot* of different configuration
files. By running all these unit tests, most
code generation bugs are found early.

However, since there are infinitely many
possible configurations to try, not
everything can be tested in Github Actions.
Therefor, I recommend to also run the
generated unit tests for your generated code.
If the generated code does not compile, or
if a unit test fails, this should be
considered a bug in the code generator.
In such a case, you can create an issue
and attach the configuration file.
