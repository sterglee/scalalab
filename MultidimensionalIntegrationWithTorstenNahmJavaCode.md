# A First Example #

`Let's start with calculation of a simple integral.`

```

import de.torstennahm.integrate._
import de.torstennahm.integrate.sparse._
import de.torstennahm.math._

class Circle extends Function {
    def dimension() = 1
    override def sEvaluate(x: Array[Double]) = {
        checkArgument(x)
        
        4 * Math.sqrt(1-x(0)*x(0))
    }

   override def inputDimension() = 1
   }     


var integrator = new DefaultSparseIntegrator

var function = new Circle

var result = integrator.integrateAbsTol(function, 1e-6)

println(result.value)

```

`As can be seen, the above code consists of two parts. One is concerned with initializing the integrator (class ` _`AdaptiveSparseIntegrator`_ `), the other defines the function that is to be integrated (class ` _`Circle`_ `). The function defined is ` _`f(x) = 4sqrt(1-x^2).`_ `It is integrated on the interval [0,1], yielding π as the integral value.`

`Now let try something more interesting. We continue the code above with the following code:`

```


class Product extends Function {
    override def sEvaluate(x: Array[Double]) = {
        checkArgument(x)
        
        var p = 1.0
        for (i<-0 until x.length) p *= x(i)
        p
    }
        
    override def inputDimension() = 8
    }
    
   var function2 = new Product
   var result2 = integrator.integrateAbsTol(function2, 1e-6)
   
   println(result2.value)

```

`The result is 0.00390625, which is equal tp 1/256. Indeed, we have integrated the 8-dimensional function ` _`x_1*x_2*...*x_8`_ `on the unit cube, giving us the result (1/2)^8. `

`As you can see, we specify the dimension in the method ` _`inputDimension()`_ `of a ` _`Function.`_ `In evaluate, we calculate the value of the function at the specified vector ` _`x.`_ `The call to ` _`checkArgument(x)`_ `ensures that the dimension of the vector ` _`x`_ `is the same as the dimension of the function, otherwise an error will be generated. Here we see one advantage of object oriented programming. Since the dimension of the function is specified in the function itself, we do not need to tell the integrator about it. We simply call` _`integrator.integrateAbsTol(function, tol)`_ ` , without needing to supply the dimension, and the integrator obtains the dimension from the function itself. By default, the integratr will always integrate with the Lesbegue measure on the multidimensional unit cube. `

`We are now ready to test the integrator on our functions. `

# `Ways to Integrate` #

`You will hae noticed that we have called the method ` _`integrateAbsTol`_ ` to do our integration. This integrates until the absolute tolerance of the integral value (that is, its estimated difference from the real integral value) is judged to be less than the specified tolerance. In addition to integrating to an absolute tolerance, there are several other possibilities. You can look at the ` _`AdaptiveSparseIntegrator`_ `implementation (included in ScalaLab sources). You will notice, that it is also possible to integrate until a relative tolerance is reached or until the function has been evaluated a minimum number of times. `

`More relevant information can be obtained from`

http://www.torstennahm.de/java/sparse_doc.html
