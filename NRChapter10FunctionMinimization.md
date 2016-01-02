# Minimization and Maximization of Functions #

`We provide some examples of function minimization and maximization with Numerical Recipes Routines.`


## Powell method. Example 1 ##

```



import com.nr.RealValueFun
import com.nr.min.Powell

// function to minimize with Powell method
// we define a function of two  variables
class funcToMinimize extends RealValueFun {
	def funk( x: Array[Double]) = {
		cos(x(0)*x(0))-sin(x(1)*x(1))- 8.7
	   }
	}

  var  p = new Array[Double](2)
    
    
    // Test Powell
   var funcToMinimizeObj = new funcToMinimize()   //  create an instance of the function to minimize
   var   pow1 = new Powell(funcToMinimizeObj)  // use the Powell minimizer for that particular function
    p(0) = 2.0;  p(1) = 5.0;  //  a starting initial condition
    var p0 = pow1.minimize(p)  //  minimize that with Powell method
    var minPowell = funcToMinimizeObj.funk(p0)

   
   // define the  function being optimized, conveniently  for plotiing
   def f(x: Double, y: Double) = {
   	var xa =  new Array[Double](2)
   	xa(0) = x
   	xa(1) = y
     funcToMinimizeObj.funk(xa)	
   }

      // plot now the function in order the minima to be evident
close("all")
figure3d; fplot2d(f, 3.0, 3.5,  4.2, 4.8, Color.RED, true);
title("minimum at "+p0(0) +" , "+p0(1))
	
  
```

## Powell method - Example 2 ##
```


/* optimize the function 
        f(x, y, z) = 1/2 - J_0[(x-1)^2+(y-2)^2+(z-3)^2]
    with the Powell's method
  The script provides   powell with a starting point P of (3/2, 3//2, 5/2) and a set of initial directions
  here chosen to be the unit directions (1, 0, 0), (0, 1, 0) and (0, 0, 1)
  powell performs its one-dimensional minimizations with linmin */

// function to minimize with Powell method
// we define a function of two  variables

import com.nr.RealValueFun

class funcToMinimize extends RealValueFun {
	def funk( x: Array[Double]) = {
import com.nr.sf.Bessjy
val bj = new Bessjy // construct a Bessel object

   0.5-bj.j0( (x(0)-1.0)*(x(0)-1.0)+(x(1)-2.0)*(x(1)-2.0)+(x(2)-3.0)*(x(2)-3.0))
	   }
	}

	
    // Test Powell
   var funcToMinimizeObj = new funcToMinimize()   //  create an instance of the function to minimize
   import com.nr.min.Powell

   var   pow1 = new Powell(funcToMinimizeObj)  // use the Powell minimizer for that particular function


  var NDIM = 3
 // this matrix defines the initial set of directions
  var xi = eye(NDIM)

var p = new AD(3)

 var p0 = pow1.minimize(p, xi)  //  minimize that with Powell method
 println("iterations performed  = "+ pow1.iter)


```