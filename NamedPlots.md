# Introduction #

`The named parameters can simplify interfaces consisting of routines with many parameters as the plotting routines are. Here we provide examples on how to use the named plotting interfaces of ScalaLab.`


# Example 1 #

```

 var t = inc(0, 0.05, 10)
 var x = sin(0.6*t)
 var y = sin(2.6*t)
 figure(1)
 subplot(2,1,1)
 nplot(xvec=t,  yvec=x)
 subplot(2,1,2)
 nplot(xvec=t,  yvec=x, lineWidth=8)
 
 figure(2)
 var td = Inc(0, 0.1, 30)
 var xd = sin(0.3*td)
 subplot(3,1,1)
 nplot(xdd=td, ydd=xd, color=Color.BLUE)
 
 
  figure(3)
 subplot(3,1,1)
 nplot(xvec=t,  yvec=x, plotType=".")
subplot(3,1,2)
 nplot(xvec=t,  yvec=x, plotType="x")
// demonstrate that we can change the order of named parameters
 subplot(3,1,3) 
 nplot(yvec=x,  xvec=t, plotType="*")

 figure(4)
 nplot2(x=t, y=x, z = y)
 

```