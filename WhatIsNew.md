## `August 09 -Starting ScalaLab  from native executable ` ##

`For both Linux64 and Windows64 you can start ScalaLab from the supplied native executables, i.e. for Linux64 ` **`LinuxScalaLab`** ` and for Windows64` **`WinScalaLab.exe`**.

`However, for Linux64, the ` **`LD_LIBRARY_PATH`** ` variable should be updated properly to include the path: ` **`$JDK_HOME/jre/lib/amd64/server`**, ` e.g. for my installation I have in .bashrc: `

`export LD_LIBRARY_PATH=$CUDA_HOME/lib64:/usr/lib:$JDK_HOME/jre/lib/amd64/server/:$LD_LIBRARY_PATH`


## `April 21` ##


**`Shift-F6`** ` and ` **`Shift- F8`** ` execute ScalaSci and GrovySci scripts within a seperate thread (i.e. non-blocking execution.) `

`Since multithreaded execution sometimes causes difficult to fix errors, as e.g. Java heap memory overflow, the old behavior is restored, i.e. editor is blocked until execution of the script finishes. As usually, this is accomplished with the ` **`F6`** ` and ` **`F8`** ` respectively.`

`A mechanism for cancelling submitted threads (with Shift-F6 and Shift-F8 keystrokes) is implemented, threads can be cancelled from the Scala Interpreter menu of ScalaInterpreterPane and from the CancelPendingTasks menu of RSyntaxArea editor. `

## `April 14` ##
`Improvement of ScalaInterpreterPane code completion (F7 key)`


## `April 11 ` ##
`Variables are displayed (with mouse over variable motion and double click), depending on whether we are in ScalaSci mode (last execution keystroke is F6), or GroovySci mode (last execution keystroke is F8) `

## `April 07` ##

**`ScalaLab started to support and GroovyLab's functionality!!.`**

`We can execute GroovyLab code with the Groovy Shell using ` **`F8`**

`GroovyLab has at its classpath all the default Java scientific libraries, i.e. the same libraries as ScalaLab. `

`Limited transfer of bindings from the Groovy Shell to the Scala interpreter is supported using ` **`Ctrl-F8`**


## `February 23` ##
`Better browsing of examples based on JTree`

## `February 14` ##

` More multithreaded operations for class ` **`RichDouble2DArray`**
`and ` **`scalaSciMatrix`**


`Specifically: `

**`pmap`** `: the parallel version of the ` **`map`**  ` method`

**`pfilter`** `: the parallel version of the ` **`filter`** ` method`

**`pexists`** `: the parallel version of the` **`exists`** ` method`

**`pforall`** `: the parallel version of the` **`forall`** ` method`


## `February 10` ##
`The ` **`encog`** ` neural network library of Jeff Heaton, available as a ScalaLab toolbox, file: encog.jar at the SourceForge downloads. `

## `December 03` ##
`Watch variables window can be displayed by using  menu` **`"Configuration"`** ` and then ` **`"Control watch variables state ... "`** ` submenu. `

`When watch variables is ON, after each Scala interpreter execution, the workspace contents are displayed with a table. ScalaLab saves the state of watch variables flag, and restores it for the next session. `

## `December 01` ##

`With Ctrl-B, the current variables of the workspace are printed conveniently`

## `November 12 ` ##

`Discrete Wavelet Transform using CUDA (currently Win 64 support only). For example: `

```

// the in-development CUDA supported signal processing operations object
var dwtObj = scalaExec.Interpreter.GlobalValues.cudaSigObj

var N = 2 << 18  // signal length
var x = new Array[Float](N)

var freq=0.000124
var k=0
while (k< N) { 
  x(k) = (0.56*sin(freq*k)+rand).toFloat
  k+=1
  }
  
var dwtx = new Array[Float](N)

tic
dwtObj.cudadwt(x, N, dwtx) 
var tm=toc
  
  figure(1); subplot(2,1,1); plot(x, "Signal");
  subplot(2,1,2); plot( dwtx, "Wavelet Transform")
  
  
```

## `October 31` ##

`Support for ` **`CUDAMat`** `matrix type, that aims to facilitate operations exploiting the CUDA parallel computing framework.`

## `October 30` ##

`Computation of the appropriate matrix size for switching automatically to multithreaded matrix multiplication. `

## `October 22` ##

`Multithreading is used automatically at the multiplication of large Array[Array[Double]] and RichDouble2DArrays. For small matrices serial implementation is used.`

`However, multithreading does not improve significantly the addition/subtraction of large matrices (for small ones, the execution time becomes worse). Therefore, we use multithreading only for multiplication. `


## `October 08` ##

`Operators / and \ work as in MATLAB, e.g. `

```

// illustration of / and \ operators

// illustration of Slash or right matrix divide
// A / B is the matrix division of B into A, which is A*inv(B)
var A = rand0(3,3)
var B = rand0(3,3)
var C = A / B
A*inv(B)-C  // should be zero

// illustration of Backslash or left matrix divide
// A \ B is the matrix division of A into B, which is inv(A)*B
var D = A \ B
A*D-B    // should be zero 


```

## `October 05` ##

`Displaying of formulas with LaTeX format, e.g.`
```

var  expr = "D[Sin[x^3],x]"
sym(expr)
```

`The apperance of the pretty Latex formula displaying window is controlled from the ` _`ComputerAlgebra`_ `menu `, `submenu: ` _`Latex displaying is ON`_

## `September 30` ##

`Better integration of the Symbolic Algebra system. For example, we can write:`

```
  // a symbolic expression
  var  expr = "Expand[(AX^2+BX)^2]"
    // evaluate the symbolic expression
  var  result = sym(expr)
```


## `September 28` ##

`Display option on popup menu for displaying matrix contents`

`Matrix sizes are also displayed when mouse cursor is over a variable`

`Updated full ScalaLab with VISAD and jzy3d support`

## `September 26` ##

`New EJML v. 0.23 is used`

`EJML supports optimized C native multiplication, with the operator ` _`cc`_ `However the difference with Java multiplication is not significant.`

# `New convenient constructors for matrices and RichDouble2DArrays (July31)` #

`RichDouble2DArrays can be constructed with $$ by specifying their rows, separating each row with null, e.g. `

```
  var xx = 8.3
  var am = $$(xx, 1-xx, cos(xx), null, xx+0.3*xx, 5.6, -3.4)
```

`Similarly, zero based matrices can be constructed with $, e.g `

```
  var xx = 8.3
  var am = $(xx, 1-xx, cos(xx), null, xx+0.3*xx, 5.6, -3.4)
```


`You can also use as elements any matrices, if however they are of the same size, e.g. `

```
  var xx = rand(2,3)
// append the matrix xx, two times as columns, three times as rows
  var yy = $(xx, xx, null, xx, xx, null, xx, xx) 
```

# `Adaptive combination of Java matrix libraries for solving basic linear algebra problems (July09)` #

`We started to work on adaptively using the best library to accomplish basic linear algebra routines depending on the properties of the ` _`RichDouble2DArray`_

`For example we can compare SVD decompositions: `
```
// SVD test

// test Apache Common Maths
var N=500; var M=300
var x = rand(N, M)
tic
var acx = asvd(x)  // perform SVD
var tmac = toc()

var shouldBeZeroAC = acx.U*diag(acx.W)*(acx.V~) - x
var shouldBeIdentityAC = acx.V*(acx.V~)  // matrix V is orthogonal

// test Numerical Recipes
tic
var nrx = nrsvd(x)  // perform SVD
var tmnr = toc()

var shouldBeZeroNR = nrx.U*diag(nrx.W)*(nrx.V~) - x
var shouldBeIdentityNR = nrx.V*(nrx.V~)  // matrix V is orthogonal

```

`and QR decompositions`
```
// QR test

// test Apache Common Maths
var N=500; var M=N; 
var x = rand(N, M)
tic
var acx = aqr(x)  // perform QR-Decomposition
var tmac = toc()

 var zorthAC = acx.Q* (acx.Q~)-eye(N, N)  // matrix Q is orthogonal, should be 0
 var shouldBeZeroAC = x-(acx.Q)*acx.R  // matrix Q is stored transposed by Apache Commons
 

// test Numerical Recipes
tic
var nrx = nrqr(x)  // perform SVD
var tmnr = toc()

var zorthNR = nrx.Q*(nrx.Q~) - eye(N,N)
var shouldBeZeroNR = x-(nrx.Q~)*nrx.R  // matrix V is orthogonal
```



# `Two useful code completion modes for RSyntaxArea editor (June 20)` #

`The ` **`RSyntaxArea`** ` editor supports code completion (using ` **`CONTROL-SPACE`**` ) in two modes, that are switchable with a menu option of the ` _`Completion`_ `menu. These modes are: `

  1. **`Global completion mode. `** ` This mode is useful to remind the global ScalaLab methods, e.g. the many overloaded versions of ` _`plot().`_ `The global completion list can be extended, using library routines, that are detected using Java reflection. These libraries are available at the ` _`Completion`_ `menu. `
  1. **`Scala completion mode. `** ` This mode works by exploiting the information acquired from the ` _`Scala Completer. `_ ` It is very useful to perform ` _`package completion`_ ` and ` _`field and method`_ `completions.`

# `Embedded Systems applications framework (June 17) ` #

`It is easy to run script code as standalone application. See the` **`EmdeddedApplicationsWithScalaLab.pdf`** `download. Works currently with ScalaLab211 at which some improvements in ScalaSci organization were made. `

# `ComplexMatrix class (June 11) (work in progress, Not finished ) ` #
`The ` _`ComplexMatrix`_ ` class aims to support efficiently operations with large complex matrices. It stores the real and imaginary part of a complex number in consecutive locations of the same row. In this way the overhead of creating many ` _`Complex`_ ` objects is avoided.`

`We should emphasize that operations that work on ` _`Complex`_ ` objects are only about 70-80% slower than those that use plain arrays to retrieve real and imaginary parts.`

`However, by creating many thousands of objects for large Complex matrices, we usually have heap overflows .`

`The ` _`ComplexMatrix`_ `supports already many operations.`

`The following script illustrates some:`

```

var N = 10
var x = new ComplexMatrix(N, N)
var y2 = x+100
var xx = x*x
var xxp = x+x
var xxr = x(1::2, 2::3)

var xsin = sin(x)
var xcos = cos(x)

var xcrange = x(::, 1::2)


var xc = crand(3,4)  // creates a random complex matrix

var xcr = real(xc)  // get the real parts
var xci = imag(xc)   // get the imaginary parts

var xco = cones(5,6)  // a complex matrix of ones
var xcz = czeros(2,3) // a complex matrix of zeros

```


# Integrated Support for Complex Numbers / Matrices (work in progress, June 06 -- Not finished ) #
`We can define a complex number as: `
```
var x = 3.4+4.5*i
```

`We can perform many operations on Complex numbers, e.g. `
```
var xx = x+x
val y = x*x+88
val y = 1.2-8.9*xx
var gg = sin(0.2*xx)
var ff = log(xx)
```


# User interface improvements (June 04) #

`The ` _`ScalaLabExplorer`_ ` and the ` _`toolbar`_ ` window, do not open by default in a fresh installation. We can open these windows using options from the ` _`Configuration`_ ` menu and the state is remembered for the next session (it is written in ` _`scalaLab.props`_ ` file. )`

# Support for MATLAB like :: operator (May 28) #

`We can replace the ` **`inc(xstart, xinc, xend)`** ` routine, with the more convenient syntactically, ` **`xstart :: xinc :: xend `**, ` for example`

```


var xstart = 0.5
var inc = 0.01
var xend = 60
var xx = xstart :: inc :: xend
plot(sin(0.67*xx))

```
`And an example of MATLAB like access/update operations:`
```

// create a random matrix
var xx = rand(20)

var rowS = 1; var rowE = 16; var rowI = 2
var colS = 1; var colE = 16; var colI = 2

// select a row range
var ff = xx(rowS::rowE, ::)
// update that row range
xx(rowS::rowE, ::) = 8.8

// select a row range with increment rowI
var fff = xx(rowS::rowI::rowE, ::)
// update that row range
xx(rowS::rowI::rowE, ::) = 55.4

 
// select a column range
var ffc = xx(::, colS::colE)
// update that column range
xx(::, colS::colE)= -99.9

 
// select a column range with increment colI
var fffc = xx(::, colS::colI::colE)
// update that column range
xx(::, colS::colI::colE)= 33

// some more select operations
var sel1 = xx(rowS::2::rowE, 3::2::colE)+5.7
var sel2 = xx(rowS::rowE, 3::2::colE)+57.9
var sel3 = xx(rowS::rowE, 3::colE)
var sel4 = xx(rowS::rowE, 3::2::colE)
var sel5 = xx(rowS::rowE, ::)
var sel6 = xx(::, 3::colE)

var sel7 = xx(2, ::)  // 2nd row
var sel8 = xx(::, 2) // 2nd column
var sel9 = xx(2, 2::3) // 2nd row, elements 2 up to 3
var sel10 = xx(2, 2::2::10)   // 2nd row, elements 2 up to 10 by step 2
var sel11 = xx(3::6, 3)  // 3nd column, rows 3 up to 6
var sel12 = xx(2::3::15, 4) // 4th column, rows 2 to 15 by step 3


// And some update operations:

var x = rand0(20); x(1::2::6, 2::3)= 150.4
var x2 = rand0(20); x2(3::5, 2::2::12)= 50.4
var x3 = rand0(9); x3(2::3, 5::6)=88

x(2,::) = 22.3   // all elements of row 2 to 22.3
x(::, 3) = 12   // all elements of column 3 to 12
x(2, 3::4) = 44 // elements 3 up to 4 of row 2 to 44
x(2, 3::2::12) = 122.2  // elements 3 up to 12 by step 2 of row 2 to 122.2
x(::, 3) = 33.3  // all elements of column 3 to 33.3
x(2::5, 3) = -4.3  // elements of rows 2 up to 5 of column 3 to -4.3
x(2::3::15, 3) = sin(x(1,1)) // elements of rows 2 to 15 by step 3 to sin(x(1,1))




```

# Reduced Row Echelon Form (rref) (May 23) #

`A routine ` _`rref` `is added to compute the `_` Reduced Row Echelon Form `_` for all scalaSci matrix types. `_


# Adaptive Functional Plotting (May 22) #

`Generally, we can improve the plot of a function significantly by adjusting the sampling density according to the rate of function change. The ` _`faplot() ` `method is a first attempt towards adaptive functional plotting. We illustrate it by means of an example: `_

```
// Example: the function sin(x*x) changes generally more rapidly as x increases,
//  however, as can be seen from its derivative x*x*cos(x*x),
//  the rate of change oscilates also with increasing frequency as x increases 
 // Example: the function sin(x*x) changes generally more rapidly as x increases,
//  however, as can be seen from its derivative x*x*cos(x*x),
//  the rate of change oscilates also with increasing frequency as x increases 
 
def f(x: Double) = sin(x*x)

closeAll
var Npoints = 200
figure(1)
subplot(2, 1, 1)
fplot(f, 0, 10, nP = Npoints )
xlabel("Fixed sampled x")
subplot(2, 1, 2)
var (ax, ay) = faplot(f, 0, 10, nP = Npoints)
xlabel("Adaptively sampled x")

```

# Numerical recipes based default FFT  (May 21) #

`The Numerical Recipes based implementation is used by default, with the ` _`fft`_ ` and  ` _`ifft`_ `routines. `

`We present convenient Scala interfaces to these routines, with the object ` _`scalaSci.FFT.FFTScala`_

`Also, we support automatic zero padding for signal lengths not a power of 2.`


# `Factory methods for Matrix construction (May 17) ` #

`You can construct matrices as `
```
var A = Mat(5, 8)
```
`instead of `
```
var A = new Mat(5, 8)
```


# `Customization of the pop-up menus of ScalaLab editors (May 15)` #

`In order ScalaLab to be even more convenient and user friendly, we started to customize the popup menus. `

`Started from May 15 version, we have a ` _`plot`_ `popup menu option, that directly plots the signal under the caret position. That facility works, both at the` _`jsyntaxpane`_ `and the` _`rsyntaxarea`_ `based editors. `

# `Code Completion with F4 key and Shift-F4 (May 08) ` #
**`F4`** ` provides a code completion list for an identifier, using Java reflection. This feature, is useful (at least for me), to remind the names of data members and methods. `

`A replacement feature is implemented by pressing ` **` ENTER `** ` on the selected item. Instead of ` **`ENTER `** `we can ` **`double click`** `the required entry. The completion list is cleared using ` **` ESC`**


`The autocompletion feature can be very useful, for example, suppose that we want to use the ` _`NormalDistribution`_ ` class of the Apache Common Maths library as follows: `
```

var x = new org.apache.commons.math3.distribution.NormalDistribution
```

`Now we can easily reveal the contents of that class by pressing ` **`F4`** ` above x`, ` or by typing a substring, as method name, e.g. x.sa`

// inspect information about the object using Java reflection

**`Shift-F4`** ` provides similar information for class names, e.g. javax.swing.JFrame`

`It is important to note that ` **`static`** ` members are displayed ` **`boldfaced.`** `In Scala is not allowed to call static methods by using a Java instance. Therefore, the completion system, for static types, inserts a call using the fully qualified class name. For example: `

```
 var vv = vrand(5)  // a scalaSci.Vec type
 vv.i  // suppose we press F4 after i
```
`The code completion  displays the ` **`inc(double, double, double)`** `static method in boldface  and the ` _`iterator`_ `instance method in plain text.`

`If we select the static method we get` **`scalaSci.Vec.inc(`** ` as a completion text.`



# `Abbreviations (May 04) ` #

`ScalaLab editors (both jsyntaxpane and rsyntaxtextarea based) support abbreviations. This can be a useful feature. When you like to use an abbreviation, type the text of the abbreviation and then` **`F11`**`. Then the short abbreviated text is replaced with the full one.`

`For example, if we type ` _`aad`_ `and then ` **`F11`** ` we have the replacement ` _`Array[Array[Double]]`_

`File ` **`Abbreviations.txt`** `defines a` **` comma `** `separated list of abbreviations. This file can be edited from the user to define the preferred abbreviations.`



# `Illustration of some useful shortcuts for frequent names (Apr 14)` #

`Types as e.g., ` _`RichDouble2DArray`_ `are rather verbose. Therefore, ScalaLab provides some useful short name equivalents, that can be convenient, and reduce some typo effort. The following script, illustrates shortcut names by examples: `

```


// demonstrate some useful shortcuts 
var  xx = D1D(8.7, -8.5)  // same as Array(..)
var  xx2 = Array(8.7, -8.5)    // the same as above

var  yy = D2D(2, 2,  // dimensions of Array
                        4.5, 5.6,  // first row
                        5.6, 7.8)  // second row
var yy2 = Array(Array(4.5, 5.6), Array(5.6, 7.8))  // the same as above


var xxa = new A1D(40)
var xxa2 = new Array[Double](40)   // same as above

var yya = new A2D(20)
var yya2 = new Array[Array[Double]](20)  // same as above

var xxelems = RD1D(5.6, -6.7, -0.66)   // construct from elements
var xxr = new R1D(9)
var xxr2 = new RichDouble1DArray(9)   // same as above

// construct from elements
var yyelems = RD2D(2, 2,  // dimensions of Array
               4.5, -0.04,  // first row
               -0.55, 5.6)   // second row
var yyr = new R2D(2,3)
var yyr2 = new RichDouble2DArray(2,3)   // same as above

var v = Vec(3.4, -0.5, 0.66)   // construct a vector from its elements


```





# `Commands whos, whosv, whoc (10 Apr) ` #

`The command ` _`whos`_ `displays the current user variables, ` _`whosv`_ `displays their values also, and ` _`whoc`_ `displays values in more compact form. These commands are simple, but they are also useful and convenient. `

# `Breeze like plotting system (31 Mar)` #

`In parallel with the ` _`JMathPlot`_ `based plotting of ScalaLab, we start to develop and a pure Scala plotting system based on ` _**`Breeze`**_

`Advanced features of the Scala language as ` _`implicit conversions`_ `and ` _`default values`_ `produce more elegant and compact code. `

`With the current Breeze-like plotting system, we can write code as: `

```


import scalaSci.jplot.plot._
import scalaSci.jplot.Figure

var jj = Figure("jj")
val p = jj.subplot(2, 1, 0)
var N=2000
var x = vrand(N)
val xax = linspace(0, 1,  N)
p += plot(xax, x)

val p2 = jj.subplot(2,1,1)

p2 += plot(xax.getv, x)

```

# `Shortcut for RichDouble2DArray constructor (Mar 31)` #
`We can write the following shortcut `
```
var xx = 3.4
var a = RD2D( 2, 4,  // specify the size first
   3.4, 5.6, -6.7, -xx,
   -6.1,  2.4, -0.5, cos(0.45*xx)) 
```

# `New Convenient Constructors for Vec, RichDouble2DArrays, Matrices (29 Mar) ` #
`It is possible to construct Vectors (` _`scalaSci.Vec`_ `) directly with, e.g. `
```
var x = 9.6  // some value
var v = Vec(0.6, x, -0.3, cos(0.6*x))  // we use arbitrary expressions inside
```
`Similarly for ` _`RichDouble2DArray`_ `, but we must specify as the first two elements the size of the array, e.g. : `

```
var xx = 3.4
var a = RichDouble2DArray( 2, 4,  // specify the size first
   3.4, 5.6, -6.7, -xx,
   -6.1,  2.4, -0.5, cos(0.45*xx)) 
```

`That constructor pattern is also used for ` _`Mat`_ `and ` _`Matrix`_ `types implemented with the full ` **`ScalaLab`** ` and ` **`ScalaLabLight`**` versions, e.g.`
```
var xx = 3.4
var m = Mat( 2, 4,  // specify the size first, a zero-indexed Matrix
   3.4, 5.6, -6.7, -xx,
   -6.1,  2.4, -0.5, cos(0.45*xx)) 
var m1 = Matrix( 3, 4,  // specify the size first, a one-indexed Matrix
   3.4, 5.6, -6.7, -xx,
   -6.1,  2.4, -0.5, cos(0.45*xx),
   0.2, cos(xx+sin(0.5*xx)), -log(xx), exp(xx)
) 

```


# `Convenient Reading and Saving Double Arrays to ASCII files (24 Feb) ` #

`We can save and read the contents of a ` **`Vec,`** **`RichDouble1DArray`** `and ` **`RichDouble2DArray`** `with the ` **`read`** `and ` **`save`** `routines, e.g. `

```

var x = vrand(200)  // create a random vector
x.save("x200.dat")  // save it to the file "x200.dat"

```

`Also we can read and write ` **`RichDouble1DArrays`** ` and ` **`RichDouble2DArrays`** `with the ` **`saveAscii`** `and ` **`readD1Ascii`** `and ` **`readD2Ascii`** `static routines, e.g.`

```

var x1 = vrand(500).getv  // get as RichDouble1DArray
var x2 = rand(20,20) 

saveAscii(x1, "x1.dat")  // save array x1
saveAscii(x2, "x2.dat")  // save array x2

var x1loaded = readD1Ascii("x1.dat")  // load array x1
var x2loaded = readD2Ascii("x2.dat")  // load array x2

```


# `Displaying the values of variables when mouse cursor is over them` #

`The displaying of the current value of an identifier when the mouse cursor is over it is a convenient feature that can facilitate the user's work. `

`ScalaLab can retrieve directly the`  **`types`** `of the ` _`defined`_ ` variables from the Scala interpreter. Also, their current` **`values`** `can be retrieved. The later requires a little more effort, since a synthetic command is constructed that is executed quietly by the Scala Interpreter in order to extract the value.`

`In some detail, assuming` _` wordAtCursor`_ `is the identifier over which the cursor is placed. The important parts of the  chunk of code that is used are: `
```

...
var $$dummy = ""+wordAtCursor  
      //  construct command to extract the value of the variable,
      //  e.g. var $$dummy = aa
      var execString = "var $$dummy = "+$$dummy 
      sI.quietRun(execString)  // execute quitely, the required value is
assigned to the synthetic variable $$dummy


 ..
// now the value of $$dummy is taken from the Scala IMain interpreter
var valueOfId =
scalaExec.Interpreter.GlobalValues.globalInterpreter.valueOfTerm("$$dummy").getOrElse("none")
       

...
```



`In words,  a command is executed quietly that assigns the value of the variable to the synthetic variable ` _`$$dummy`_`, and then the value of` _`$$dummy`_ `, is used and displayed at the mouse cursor position.`

`That works, however  displaying values for types that their` _`toString`_ `method, can produce a very large String (e,g. very large Lists) is avoided since that causes problems to the` _`setToolTipText`_ `routine that presents the variable's value at the mouse cursor position.`

`Values are presented for the ` _`primitive types`_ **`Char, Int, Long, Short, Double, Float, String `** `and for all the types of ` **`scalaSci,`** ` since the later types are designed to produce short Strings with their ` _`toString`_ `routine.`

`Displaying the values for all the types, can cause troubles with objects that are very large, e.g. lists with thousands of items. Therefore, by default we selected to display the values only for the selected types, we just mentioned. However, we have an option, at the ` **`Configuration`** `menu to enable the displaying of values of all the types. This can be useful for programs dealing with small data structures, but: ` **`large objects can cause problems, or even stall ScalaLab`**, `so, be careful at the decision to enable displaying of any value.`

`However, for the convenience of the user, the feature of displaying information for variables with the` _`MouseMotionListener`_ `can be configured for both the` _`jsyntaxpane`_ `and the` _`rsyntaxarea`_ `based ScalaLab editors. That configuration can be done from the ` **`Configuration`** ` menu, with the following submenus: `

**`Toggle Mouse Motion triggering for identifiers for jsyntax editor (current states is true|false)`**

**`Toggle Mouse Motion triggering for identifiers for jsyntax editor (current states is true|false)`**

`The user preferences are retained for the next session. `



# `The task oriented ScalaLab wizards` #

`ScalaLab utilizes many effective Java numerical libraries. Although the user should first be familiar with the mathematics implemented by each library, the environment can facilitate the quick and efficient utilization of each library by providing Graphical User Interface based Wizards. `

`In ScalaLab we avoid generally to design complex mathematical objects, and the most useful type is the ` **`RichDoubleDoubleArray`** `type, that wraps the Array[Array[Double]] type`.

`Personally, I believe that the most important task is to understand well the mathematics behind each routine, and in this way I appreciate much the ` **`Numerical Recipes`** `book, from which I can study the algorithms behind each routine.`

`However, after understanding the algorithms, an environment like ScalaLab can facilitate the user, in order not to loose much time searching how to interface the mathematical routines of each library. It is in this spirit that the Wizards of ScalaLab are designed, in a task oriented way, and by providing simple examples, that with small changes can be customized to fit the particular applications. `


## `The Linear Systems Equations Wizard (started at 04-01-2013) ` ##


`This wizard provides examples on how to interface ` **`RichDoubleDoubleArray`** `arrays with important libraries, as the Apache Common Maths, EJML, MTJ and Numerical Recipes. `
