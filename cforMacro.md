# Introduction #
`ScalaLab adapts from the ` **`ScalaXY`** ` project `

https://github.com/ochafik/Scalaxy

`the ` **`optimized for `** `which is convenient and very fast.`

`We present as an example, the Ikeda chaotic map implemented with the optimized for loop.`


```

import scalaxy.loops._
    
    
// Ikeda map
close("all")
val R = 1; val C1 = 0.4; val C2 = 0.9; val C3 = 6


tic;
var N=2500000;

var x = new Array[Double]( N)
var y = new Array[Double]( N)
x(0)=0.12; y(0) = 0.2;

 var km = 0;
var tau=0.0; var sintau=0.0; var costau=0.0

for ( k<-1 until N optimized) {

  km= k -1
  tau = C1-C3/(1+x(km)*x(km)+y(km)*y(km))
  sintau = sin(tau); costau = cos(tau);
  x(k) = R+C2*(x(km)*costau-y(km)*sintau)
  y(k) = C2*(x(km)*sintau+y(km)*costau)
 
 }
 

var tm = toc();
scatterPlotsOn();  // display points only, not the connecting lines
plot(x, y);
title("Scala Time for Ikeda map "+tm);



```

`ScalaLab uses the C style for loop macro of Spire to have a fast for loop. The speed,  is the same as the while loop. `

`We present as an example also, the Ikeda chaotic map implemented with the C style for loop.`

```


    
    
    
// Ikeda map
closeAll
val R = 1; val C1 = 0.4; val C2 = 0.9; val C3 = 6


tic
var N=2500000

var x = new Array[Double]( N)
var y = new Array[Double]( N)
x(0)=0.12; y(0) = 0.2;

var km = 0;
var tau=0.0; var sintau=0.0; var costau=0.0

// C-style for loop for ScalaLab
// The implementation is adapted from Spire

cfor (1) ( _<N, _ + 1)  { k =>  // k is the loop variable

  km= k -1
  tau = C1-C3/(1+x(km)*x(km)+y(km)*y(km))
  sintau = sin(tau); costau = cos(tau);
  x(k) = R+C2*(x(km)*costau-y(km)*sintau)
  y(k) = C2*(x(km)*sintau+y(km)*costau)
 
 }
 

var tm = toc
scatterPlotsOn()  // display points only, not the connecting lines
plot(x, y)
title("cfor loop time for Ikeda map "+tm);



```