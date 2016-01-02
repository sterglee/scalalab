# Introduction #

`VISAD is a powerful data visualization framework that has its own native interface documented very well in the project's page: http://www.ssec.wisc.edu/~billh/visad.html. `
`VISAD is available as standard ScalaLab's library. It is important to note that in order to use VISAD the user should install ` **`Java3D`** `which is freely available from http://www.oracle.com/technetwork/java/javase/tech/index-jsp-138252.html.`

`However, the native interface of VISAD is rather difficult. In ScalaLab we have wrapped most of VISAD's functionality with an easy to use Matlab-like interface, that allows the user to perform directly 2-D or 3-D plots of the data, using the VISAD rather impressive functionality. We describe in this page the utilization of VISAD routines by means of examples.`

# Matlab-like Interface for VISAD library #

`We present examples of plotting data using the Matlab-like interface for the VISAD library of ScalaLab. We should note that the implementation of this interface is a process in work, and thus the latest ScalaLab29 version will be required to execute successfully all the examples.`

`Let create first some numerical data: `

```
 val t=inc(0, 0.01, 10) 
 val x = sin(3.4*t)
 val y = sin(0.48*t)
 val z = sin(5.7*x+0.2*y)
```

`We can open a frame capable of displaying VISAD plots, wnd we can plot in it the vectors, ` _`x`_, _`y`_, _`z`_ `with:`
```

 vfigure(1)
 vsubplot(3,1,1)
 vplot(x, "t", "x")
 vsubplot(3,1,2)
 vplot(x, y, z)
 vsubplot(3,1,3)
 vplot(x, y, sin(9.8*z))

```


`We can now open a new VISAD figure and plot at its 2,2,1 subpanel the x-signal, with 8-point size: `
```
  vfigure(2)
  vsubplot(2, 2, 1)
  vplot(x, 8)  // plot with 8 point line
```

`Continuing we can perform some other plotting operations: `

```
  vsubplot(2, 2, 2)  
  vplot(x)
  vaddplot(y)   // add the plot of signal y, without erasing any previous plots
  vsubplot(2, 2, 3)
  vplot(x,y)
  val  zz = z+cos(0.8*z)
  vsubplot(2, 2, 4)
  vplot(z)
  vaddplot(zz)   // add the plot without erasing previous, i.e. in "hold on" state
  val zzz = zz+ sin(3.4*zz)
  vaddplot(zzz, "zzx", "zzy", 5) 
```


`And also, some other examples: `
```
  vfigure(3)    
  vsubplot(2, 2, 1);   vplotPoint(x);  // plot with 8 point line
  vsubplot(2, 2, 2);   vplot(x)
  vsubplot(2, 2, 3);   vplotXYPoints(x, y, "x", "y", 8)
  vsubplot(2, 2, 4);  vplot(z)
  vaddplot(zz)   // add the plot without erasing previous, i.e. in "hold on" state
  vaddplot(zzz, "zzx", "zzy", 5)
```

