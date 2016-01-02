# Introduction #

`Operations for concatenating matrices are improved with ScalaLab210. The basic operations are ` _`RA`_ `for appending rows, ` _`RP`_ `for prepending rows`, _`CA`_ `for appending columns and ` _`CP`_ `for prepending columns. Also, there exist some symbolic aliases to these operations, but perhaps it is more difficult to remember them. `

`We can use these operations for both ` _`RichDoubleDoubleArrays, `_ `zero-indexed matrices and one indexed matrices. The utilization of these operations is straightforward as the following script illustrates: `

## `Example illustrating matrix appending/prepending functionality` ##

```
var xones = ones(3, 5)
var yrandC = rand(3, 2)
var yrandR = rand(2, 5)

// test row-oriented operations
var xyRA1 = xones RA yrandR  // append yrandR  to the rows of xones
var xyRA2 = xones >> yrandR   // xones is at the "front",  yrandR follows

var xyRP1 = xones RP yrandR  // prepend yrandR to the rows of xones
var xyRP2 = xones <<  yrandR  // yrandR is at the "front", xones follows


var v = vrand(5)  // create a random  vector
var xvRA1 = xones RA v   // append v to the rows of xones
var xvRA2 = xones >> v  // xones is at the "front", v follows

var xvRP1 = xones RP v   // prepend v to the rows of xones
var xvRP2 = xones <<  v   // v is at the "front", xones follows


// test column-oriented operations
var xonest = xones~   // transpose xones
var yrandCt = yrandC~   // transpose yrandC
var yrandRt = yrandR~ // transpose yrandR

var xyCA1 = xonest CA yrandRt  // append yrandRt to the columns of xonest
var xyCA2 = xonest >>> yrandRt  // xonest is at the "front", yrandRt follows

var xyCP1 = xonest CP yrandRt   // prepend yrandRt to the columns of xonest
var xyCP2 = xonest <<< yrandRt  // yrandRt is at the "front", xonest follows

var xvCA1 = xonest CA v   // append v to the columns of xonest
var xvCA2 = xonest >>> v  // xonest is at the "front", v follows

var xvCP1 = xonest CP v  // prepend v to the columns of xonest
var xvCP2 = xonest <<< v // v is at the "front", xonest follows






```