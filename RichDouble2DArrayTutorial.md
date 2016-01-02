# Introduction #

`The ` _`RichDouble2DArray`_ `is the more general Matrix class of ScalaLab, i.e. it is independent of the library that the current Scala interpreter. Also, `  _`RichDouble2DArray`_ ` aims to provide a lot of alternative implementations of the basic numerical analysis problems, with the default using the best performing one. For example, for the ` _`QR decomposition`_ `we utilize, e.g. Numerical Recipes, Apache Common Maths etc, implementations. The default QR switches to that implementation that performs best depending and on the matrix size. We should note that we started to work on that feature and we gradually improve that work on upcoming ScalaLab versions (it requires a lot of work!). `

`Let now start with the tutorial.`


## _`RichDouble2DArray`_ `creation` ##

`We can create a ` _`RichDouble2DArray`_ `from a Scala 2-D array, e.g.`

```

var x = Array(Array(3.4, -1.2, 0.3),
            Array(-0.34, -0.12, 0.7),
            Array(4.3, 1.2, 10.3))
            
var rx = new RichDouble2DArray(x)            
```

`An ` _`RichDouble2DArray`_ `initialized to zeros, can be created from its dimensions, e.g. `

```
var zx = new RichDouble2DArray(5,8)
```

`We can also use an one-dimensional Scala array, e.g. `
```
var da = Array(0.23, 0.56, -0.23, 9.2)
var rda = new RichDouble2DArray(da)
```

`There are also constructors that build RichDouble2DArrays from the other ScalaLab Matrix types. `

`A very useful constructor is the ` _`RD2D`_ `that constructs from the values, e.g. `

```
var y = 4.5
var ry = RD2D( 2,3,  // i.e.  2 rows by 3 columns
              sin(y), -3.4*cos(9.8*y), log(y),
              -1.2*y, -22.8*y, y*y)
```

`Also, very useful is the ` **`$$`** `method, that constructs a RichDouble2DArray from a comma separating list of doubles, where ` **`null`** `seperates the rows, e.g. `

```
var f=9.7
var xx = $$(3.4, -2.3, null,  // start new row
    0.01*sin(0.8*f), -1.2, null,
    -1.2*exp(f), f-3.4*sin(f))
```

## `Numerical Analysis routines` ##

`