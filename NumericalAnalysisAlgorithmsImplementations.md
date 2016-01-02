# `Golden minimum search` #


```

def golden( func: Double => Double, p1: Double, p2: Double, tol: Double = 0.0001) = {
// Golden search for finding min of one variable nonlinear function
// Example call:  var ( f, a) = golden(func, p, tol)
// func is the user defined function
// p1, p2 define the search range
// tol is then tolearance, a is the optimum value of the function
// f is the minimum of the function, iter the number of iterations
   
   var g = (-1+sqrt(5))/2 
   var a = 0.0; var b = 0.0
  if (p1 < p2) {
       a = p1; b = p2
       }
     else {
     a = p2; b = p1
     }
     var r = b-a
     var iter = 0
    
     while ( r> tol ) {
        var x1 = a+(1-g)*r
        var x2 = a+g*r
        var y1 = func(x1)
        var y2 = func(x2)
        if (y1 < y2)
            b = x2
        else
            a = x1
        r = b-a
        iter+=1
        }
        
        var f = func(a)
        (f, a, iter)
        }
                                  
         
      
      closeAll
      def f(x: Double) = x*sin(1.2*x)
      
      fplot(f, 0, 20)
      
      var (fm, am, iter) = golden(f, 10, 18)
      
      

```