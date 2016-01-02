# Introduction #

`We present benchmarks of operations using Scala functions, and Java 8 lambdas. Scala functions are easier to use and more expressive. However, Java 8 lambdas seem to be slightly faster. `

## `Parallel Map benchmark` ##

```

var N=4000
var x = rand(N, N)
import java.util.function.DoubleUnaryOperator

def f(x: Double) = x*x*x

class af extends AnyRef with java.util.function.DoubleUnaryOperator {
   def  applyAsDouble(x: Double) = x*x*x
   }

var afo = new af

tic
var xx = x psmap afo
for (k<-0 until 10)
  xx = x psmap afo
 var tmLamdas = toc  // 0.35 sec



tic
var xxs = x pmap f
for (k<-0 until 10)
  xxs = x pmap f
 var tmScalaFunctions = toc  // 0.42 sec


```