# Introduction #

`The standard Scala Collections provide a lot of useful general purpose operations. The class Vec of ScalaSci supports all the set of standard operations using the mechanism described in the classical` **`"Programming in Scala", 2nd edition,  book of Martin Odersky et. al.`**

`Below we describe some functionality of the Vec class related to the general Scala Collections framework.`


# The Vec class and Scala Collections #

`The Vec class supports the full Scala Collection framework. Below we present some examples.`

`Let create a random vector:`
```
var v= vrand(10)
```

`We define a function that multiples with 10 and apply it, to all the vector elements.`
```
def f10(x:Double) = 10*x
var v10 = v map f10
```

`Convert v10 to array, to list, to iterable, to sequence, to indexed sequence, to stream, to set: `
```
v10 toArray
v10 toList
v10 toIterable
v10 toSeq
v10 toIndexedSeq
v10 toSet
```

`Explore some size info:`
```
v10 isEmpty
v10 nonEmpty
v10 size
v10 hasDefiniteSize
```

`Perform some element retrieval operations:`
```

v10 headOption
v10 last
v10 lastOption
v10 find (_ > 2)
```

`Explore subcollections: `
```

v10 tail
v10 init
v10 slice (2, 6)
v10 take 3
v10 drop 4
v10 takeWhile (_ > 3)
v10 dropWhile (_ < 2)
v10 filter (_ > 2)
v10 withFilter (_ > 2)
v10 filterNot (_ < 2)
```

`Subdivisions:`
```

v10 splitAt 3
v10 span (_ < 2)
v10 partition (_ > 5)
v10 groupBy (_ > 5)
```

`There are also and other operations that can be applied. All are described very clearly in the superb book ` **`Programming in Scala, 2nd edition, of Martin Odersky, Lex Spoon, Bill Venners, Artima, 2010`**

## Scala Collections operations for the class Mat ##

`Similarly, the two-dimensional ScalaSci classes support the generic Scala collections framework. Here are some examples for the ScalaSci Mat class: `

```


var mm = rand0(2,3)

var mmh = mm head

var mmt = mm tail

var mms = mm map sin



var mma = mm toArray
var mml = mm toList
var mmi = mm toIterable
var mms = mm toSeq
var mmis = mm toIndexedSeq
var mmstream = mm toStream
var mmset = mm toSet

mm isEmpty
mm nonEmpty
mm size
mm hasDefiniteSize


mm slice(2, 8)
mm init
mm take 10
mm drop 5
mm takeWhile(_>0.6)
mm dropWhile(_< 0.5)
mm filter (_ < 0.9)
mm exists(_ > 0.7)
mm forall (_ > 0)
mm product
mm sum
mm min
mm max




```