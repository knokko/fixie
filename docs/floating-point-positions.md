# Why I dislike floating-point coordinates
This example will demonstrate what kind of weird things can happen when
floating-point numbers are used to represent coordinates. This was in
fact my initial motivation to create fixie.

## Set-up
Imagine that an object is spawned at some position (x, y), and moves
with a constant speed of 0.1 m/s in the positive X direction. This
hypothetical physics system will update at a rate of 100 updates
per second. I would expect this object to travel 1m every 10 seconds.
The code for this example can be found
[here](../src/test/kotlin/fixie/generator/FloatingPointPhysics.kt).

## When x=0
When the initial *x* is 0, the final *x* will be 0.100000024 after 1 second,
and 0.9999907 after 10 seconds. This looks very accurate!

## When x=100
When the initial *x* is 100 meters, the final *x* will be 100.099945 after
1 second, and 100.99945 after 10 seconds. The error after 10 seconds is
less than a millimeter, which I would consider to be good.

## When x=10,000
When the initial *x* is 10 kilometers, the final *x* will be 10000.098 after
1 second, and 10000.977 after 10 seconds. The error after 10 seconds is
a couple of centimeters, which may or may not be good enough for you.

## When x=40,000
When the initial *x* is 40 kilometers, the final *x* after 1 second and
10 seconds will also be 40 kilometers, so the object is no longer moving
at all! The system has reached the point where the floating-point
representation is so sparse that `40,000` and
`40,000 + timeStep * speed` are represented by the same number, thereby
stopping all movement.

## The lessons
The examples above show that floating-point numbers are great coordinates,
as long as everything stays close to the origin. When the objects move away,
the physics precision will gradually deteriorate, and at some point stop
completely. This 'stop point' depends on the:
- movement speed: a lower movement speed yields a lower 'stop x'
- update frequency: a higher update frequency yields a lower 'stop x'

When the update frequency gets larger, physics precision is normally
increased, at the cost of performance. The problem above works the other
way around: a larger update frequency will lower the 'stop x'. Thus,
increasing the update frequency will probably improve the physics precision
when *x* is small, but destroy the precision when *x* is large.

When you use `double` instead of `float`, the problem shown above will
occur when *x* is much larger. It's a pretty effective solution if you
don't mind the extra memory and don't intend to create very large worlds.

## If you use fixed-point numbers
When you use fixed-point numbers to represent your coordinates, the
precision will no longer depend on the *x*. Note that fixed-point numbers
have a maximum value, so objects can keep moving until the physics system
starts suffering from overflow. The advantage is that this maximum value
is known up-front.

Note also that very slow objects may still not be able to move in a
fixed-point physics system: they won't move if their speed is lower than
the 'minimum movement speed', which depends on:
- the update frequency, a higher frequency implies a higher minimum speed
- the *oneValue* of the fixed-point number, a higher *oneValue* implies a
lower minimum speed (but also decreases the maximum value)

This 'minimum movement speed' is quite similar to the 'stop condition' of
floating-point coordinates, but the difference is that it doesn't depend
on the *x*. This means that you can test your physics at any position,
without wondering whether it would also work at some other position.

### Precision
The average precision is expected to be slightly better since floating-point
numbers can be nearly infinitely high, which means that some values are
located outside the world border. When using fixed-point numbers, you can
configure the *oneValue* such that the maximum coordinate value is slightly
larger than the maximum coordinate that you need, which means that not many
values are wasted. For instance, if we use `oneValue = 2^15`
(which yields a maximum value of approximately 2^16 = 65km), the error after
moving 1 second is 2.34 millimeters (regardless of the initial *x*). This is
similar to the error when using floating-point coordinates and an initial
*x* of 10km.
