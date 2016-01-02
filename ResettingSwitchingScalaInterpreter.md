# Introduction #

`Here we describe how we can reset the state of the Scala Interpreter as well as to create a new one that utilizes  perhaps different default scientific libraries. The` **`ScalaLabConsole`** `offers with its popup menu options to switch the Scala Interpreter. We will illustrate the main concepts by means of examples`.


# `Example of resetting and customizing the Scala Interpreter` #

`Let start ScalaLab and create some variables with some code, e.g. `
```
val N=2048
val t = linspace(0, 1, N)
val x = sin(0.23*t)-5.6*cos(t*9.4)
val fx = fft(x)
figure(1); subplot(2,1,1); plot(t,x, "Signal");
subplot(2,1,2); plot(fx, "FFT")
```

`Clearly, the code above creates some variables. A redefinition of a variable overlaps the old one, new compiled code refers to the new definition, but however old code parts still are bound to the old definitions. Clearly, in a large working session thinks can become confusing and large parts of memory can be wasted.`

`In these cases a viable solution is to reset the Scala Interpreter, i.e. to create a new fresh Scala Interpreter. The steps to accomplish this task are very simple: `

  1. `Right-mouse-click over the ScalaLabConsole canvas area. This opens a popup menu with many useful options`
  1. `Select the "Reset Scala Interpreter using ScalaLab default imports" option`

`Now any attempt to evaluate the formerly defined variables fails, simply because the whole context of the previous Scala Interpreter, is garbage collected by the Java Virtual Machine. `
`At this time we have two choices regarding the scientific libraries that the Scala Interpreter uses: `

  1. `The default uses a zero-indexed ` _`Mat`_ `class that is based on Scala implementation that utilizes also the JAMA Java library for some routines. The type of the matrix returned in this case is ` _`scalaSci.Mat`_ `as for example:`
```
var a = rand0(90)
var aa = a*a*sin(a)
```
  1. `The Scala Interpreter that uses EJML based zero-indexed matrices (  http://code.google.com/p/efficient-java-matrix-library/). By using the "Reset Scala Interpreter using Efficient Java Matrix Library of Peter Abeles", ScalaLab implements its zero-indexed` _`Mat`_ `class on top of the EJML machinery. Using the EJML based Interpreter the code piece above produces as matrix type: `_`scalaSci.EJML.Mat`_