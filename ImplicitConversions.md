# Introduction #

`The mechanism of implicit conversions is of fundamental importance for the construction of high-level mathematical operators in ScalaLab. Here, we describe the redesign of the Implicit Conversions in ScalaLab around the` _`RichNumber`_, _`RichDoubleArray`_, and _`RichDoubleDoubleArray`_ classes.


# Implicit Conversions Design #

`Initially, we have implemented implicit conversions that transformed the receiver object according to the type of the arguments in order the operation to proceed, e.g. for the code below: `
```
var a = rand(200, 300)   // create a 200 by 300 Matrix
var a2 = 2+a  // performs the addition by implicitly converted 2
```

`The 2 at the initial design was transformed to a 200X300 Matrix filled with 2, and the addition operation is performed as Matrix addition.`

`However, this design was not very elegant and also not as effective as it can be.`

`Therefore, we updated the design of the implicit conversions around the `_`RichNumber`_ `class. This class models an extended Number capable of accepting operations with all the relevant classes of ScalaLab, e.g, with `_`Mat`_, _`Matrix`_, _`EJML.Mat`_ `and generally whatever class we need to process. `

`At the example above, the 2 is transformed by the Scala compiler to a `_`RichNumber`_ `object, that has defined an operation to add a Matrix. Therefore, the operation proceeds effectively without allocating any new space (at the initial design a Matrix object was created and filled with 2s).`

`This design is more effective (about 20-30% speed increase) and (perhaps more important) simpler and more extendable. `

`We present the current code of the ` _`RichNumber`_ `class: `

```

// serves for implicitly converted to that type when certain operations cannot be performed, 
// e.g 2+Mat
package scalaSci


class RichNumber(v: Double) {
private val value = v // the RichNumber includes a Double field that corresponds to its value


def +(that: Array[Double]): Array[Double] = {
var N = that.length
var result = new Array[Double](N)
for (r <-0 until N) 
result(r) = that(r) + value
result
}

def -(that: Array[Double]): Array[Double] = {
var N = that.length
var result = new Array[Double](N)
for (r <-0 until N) 
result(r) = -that(r) + value
result
}

def *(that: Array[Double]): Array[Double] = {
var N = that.length
var result = new Array[Double](N)
for (r <-0 until N) 
result(r) = that(r) * value
result
}


def /( that: Array[Double]): Array[Double]= {
var N = that.length
var result = new Array[Double](N)
var r=0
while (r < N) {
var tmp = that(r)
if (tmp != 0)
result(r) = value / tmp
else
result(r) = 0.0
r += 1
}
result
}



def +(that: Array[Array[Double]]): Array[Array[Double]] = {
var N = that.length
var M = that(0).length

var result = new Array[Array[Double]](N,M)
for (r <-0 until N) 
for (c <-0 until M) 
result(r)(c) = that(r)(c) + value
result
}

def -(that: Array[Array[Double]]): Array[Array[Double]] = {
var N = that.length
var M = that(0).length

var result = new Array[Array[Double]](N,M)
for (r <-0 until N) 
for (c <-0 until M) 
result(r)(c) = -that(r)(c) + value
result
}
def *(that: Array[Array[Double]]): Array[Array[Double]] = {
var N = that.length
var M = that(0).length

var result = new Array[Array[Double]](N,M)
for (r <-0 until N) 
for (c <-0 until M) 
result(r)(c) = that(r)(c) * value
result
}

def /( that: Array[Array[Double]]): Array[Array[Double]]= {
var N = that.length
var M = that(0).length

var result = new Array[Array[Double]](M, N)
var r=0; var c=0
while (r < N) {
c = 0
while (c < M) {
var tmp = that(r)(c)
if (tmp != 0)
result(r)(c) = value / tmp
else
result(r)(c) = 0.0
c += 1
}
r += 1
}
result
}

// addition of RichNumber and Vec
def +( that: Vec ): Vec = {
var N = that.length
var result = new Vec(N)
var r=0
while (r < N) {
result(r) = that(r) + value
r += 1
}
result 
}

// subtraction of Vec from a RichNumber 
def -( that: Vec ): Vec = {
var N = that.length
var result = new Vec(N)
var r=0
while (r < N) {
result(r) = value - that(r) 
r += 1
}
result
}

// multiplication of RichNumber and Vec
def *( that: Vec ): Vec = {
var N = that.length
var result = new Vec(N)
var r=0
while (r < N) {
result(r) = that(r) * value
r += 1
}
result
}

// division of a RichNumber with a Vec
def /( that: Vec ): Vec = {
var N = that.length
var result = new Vec(N)
var r=0
while (r < N) {
var tmp = that(r)
if (tmp!=0)
result(r) = value / tmp
else
result(r) = 0.0
r += 1
}
result
}

def +(that: Matrix): Matrix = {
var N = that.Nrows
var M = that.Ncols

var result = new Array[Array[Double]](N,M)
for (r <-0 until N) 
for (c <-0 until M) 
result(r)(c) = that(r,c) + value
new Matrix(result, true)
}
// subtraction of a Matrix from a RichNumber
def -(that: Matrix): Matrix = {
var N = that.Nrows
var M = that.Ncols

var result = new Array[Array[Double]](N,M)
for (r <-0 until N) 
for (c <-0 until M) 
result(r)(c) = value - that(r,c) 
new Matrix(result, true)
}

def *(that: Matrix): Matrix = {
var N = that.Nrows
var M = that.Ncols

var result = new Array[Array[Double]](N,M)
for (r <-0 until N) 
for (c <-0 until M) 
result(r)(c) = that(r,c) * value
new Matrix(result, true)
}

// division of a RichNumber with a Matrix. Divides the corresponding number with each of the Matrix's element'
def /( that: Matrix ): Matrix = {
var N = that.Nrows
var M = that.Ncols

var result = new Matrix(N, M)
var r=0; var c=0
while (r < N) {
c = 0
while (c < M) {
var tmp = that(r, c)
if (tmp != 0)
result(r, c) = value / tmp
else
result(r, c) = 0.0
c += 1
}
r += 1
}
result
}

def +(that: Mat): Mat = {
var N = that.Nrows
var M = that.Ncols

var result = new Array[Array[Double]](N,M)
for (r <-0 until N) 
for (c <-0 until M) 
result(r)(c) = that(r,c) + value
new Mat(result, true)
}
// subtraction of a Matrix from a RichNumber
def -(that: Mat): Mat = {
var N = that.Nrows
var M = that.Ncols

var result = new Array[Array[Double]](N,M)
for (r <-0 until N) 
for (c <-0 until M) 
result(r)(c) = value - that(r,c) 
new Mat(result, true)
}

def *(that: Mat): Mat = {
var N = that.Nrows
var M = that.Ncols

var result = new Array[Array[Double]](N,M)
for (r <-0 until N) 
for (c <-0 until M) 
result(r)(c) = that(r,c) * value
new Mat(result, true)
}

// division of a RichNumber with a Matrix. Divides the corresponding number with each of the Matrix's element'
def /( that: Mat ): Mat = {
var N = that.Nrows
var M = that.Ncols

var result = new Mat(N, M)
var r=0; var c=0
while (r < N) {
c = 0
while (c < M) {
var tmp = that(r, c)
if (tmp != 0)
result(r, c) = value / tmp
else
result(r, c) = 0.0
c += 1
}
r += 1
}
result
}

// addition of RichNumber and an EJML.Mat
def +( that: EJML.Mat ): EJML.Mat = {
var N = that.Nrows
var M = that.Ncols

var result = new EJML.Mat(N, M)
var r=0; var c=0
while (r < N) {
c = 0
while (c < M) {
result(r, c) = that(r,c) + value
c += 1
}
r += 1
}
result
}

// subtraction of an EJML.Mat from a RichNumber
def -( that: EJML.Mat ): EJML.Mat = {
var N = that.Nrows
var M = that.Ncols

var result = new EJML.Mat(N, M)
var r=0; var c=0
while (r < N) {
c = 0
while (c < M) {
result(r, c) = -that(r,c) + value
c += 1
}
r += 1
}
result
}

// multiplication of a RichNumber with a Mat
def *( that: EJML.Mat ): EJML.Mat = {
var N = that.Nrows
var M = that.Ncols

var result = new EJML.Mat(N, M)
var r=0; var c=0
while (r < N) {
c = 0
while (c < M) {
result(r, c) = that(r,c) * value
c += 1
}
r += 1
}
result
}

// division of a RichNumber with a Mat. Divides the corresponding number with each of the Mat's element'
def /( that: EJML.Mat ): EJML.Mat = {
var N = that.Nrows
var M = that.Ncols

var result = new EJML.Mat(N, M)
var r=0; var c=0
while (r < N) {
c = 0
while (c < M) {
var tmp = that(r, c)
if (tmp != 0)
result(r, c) = value / tmp
else
result(r, c) = 0.0
c += 1
}
r += 1
}
result
}

}


```


# `The RichDoubleArray and RichDoubleDoubleArray classes` #

`Similarly, the classes ` _`RichDoubleArray`_ and _`RichDoubleDoubleArray`_ `wrap the `_`Array[Double]`_ `and ` _`Array[Array[Double]]`_ `Scala classes in order to allow convenient operations as e.g addition and multiplication of ` _`Array[Array[Double]]`_ `types. `

`As `_`RichNumber`_ `enriches simple numeric types, `_`RichDoubleArray`_ `enhances the `_`Array[Double]`_ `type and ` _`RichDoubleDoubleArray`_ `the ` _`Array[Array[Double]]`_`type. Therefore, for example, the following code becomes valid: `

```
var a = Ones(9, 10) // an Array[Array[Double]] filled with 1s
var b = a+10 // add the value 10 to all the elements returning b as an Array[Array[Double]]
var c = b + a*89.7 // similarly using implicit conversions this computation proceeds normally
```

`The implementation of the ` _`RichDoubleArray.scala`_ `and ` _`RichDoubleDoubleArray.scala`_ `classes can be obtained from the sources of ScalaLab.`