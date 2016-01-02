# Introduction #

`We instroduce the` _`varf`_ ` and ` _`valf`_ ` keywords of ScalaLab, that translate to ` _`private[this]`_ ` i.e. they make the corresponding variables ` _`object private `_

`We observe significant speedup using such declarations, that can be 2 to 3 times faster. However, variables declared with ` _`valf`_ ` and ` _`varf `_ `are no longer visible to the ScalaLab's workspace. `

`For example we implement the Ikeda chaotic map using ` _`varf`_ ` and ` _`valf`_

```


// Ikeda map
close("all")
valf R = 1; valf C1 = 0.4; valf C2 = 0.9; valf C3 = 6


tic;
varf N=2500000;

varf x = new Array[Double]( N)
varf y = new Array[Double]( N)
x(0)=0.12; y(0) = 0.2;

varf k = 1;
 varf km = 0;
varf tau=0.0; varf sintau=0.0; varf costau=0.0
while (k< N) {
  km=k-1
  tau = C1-C3/(1+x(km)*x(km)+y(km)*y(km))
  sintau = sin(tau); costau = cos(tau);
  x(k) = R+C2*(x(km)*costau-y(km)*sintau)
  y(k) = C2*(x(km)*sintau+y(km)*costau)
 k += 1

 }

var tm = toc();
scatterPlotsOn();  // display points only, not the connecting lines
plot(x, y);
title("Scala Time for Ikeda map "+tm);




```