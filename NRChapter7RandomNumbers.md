# Introduction #

`Chapter 7 of Numerical Recipes 3nd edition deals with Random Numbers. Here, we provide some examples in ScalaSci.`


## Generating Logistic Deviates ##
```



    var mmu = 2.0; var ssig=5.5
    var ldobject = new com.nr.ran.Logisticdev(mmu, ssig, 33)
   
   var N=60000  // number of points to evaluate the plot
   var v = new Vec(N)

   // generate the logistic deviates
  for (k<-0 until N)
    v(k) =  ldobject.dev()    

    figure(1); 
    plot(v, "Some logistic deviates")
      // plot them
    var p = new scalaSci.math.plot.Plot2DPanel("SOUTH");
    p.addHistogramPlot("Logistic deviates distribution", v, 600);
    new scalaSci.math.plot.FrameView(p);
  
```


## Normal Deviates by Transformation (Box-Muller) ##

```


    var mmu = 2.0; var ssig=5.5
    var seed = 22l   // an arbitrary seed
    var ndobject = new com.nr.ran.Normaldev_BM(mmu, ssig, seed)
   
   var N=60000  // number of points to evaluate the plot
   var v = new Vec(N)

   // generate the logistic deviates
  for (k<-0 until N)
    v(k) =  ndobject.dev()    

    figure(1); plot(v, "Some normal deviates")
      // plot them
    var p = new scalaSci.math.plot.Plot2DPanel("SOUTH");
    p.addHistogramPlot("normal deviates distribution", v, 600);
    new scalaSci.math.plot.FrameView(p);
 
```

## Poisson deviates ##

```


    var lambda = 5.0;
    var seed = 22l   // an arbitrary seed
    var  poissonObject = new com.nr.ran.Poissondev(lambda, seed)
   
   var N=60000  // number of points to evaluate the plot
   var v = new Vec(N)

   // generate the Poisson deviates
  for (k<-0 until N)
    v(k) =  poissonObject.dev()    

    figure(1); plot(v, "Some Poisson deviates")
      // plot them
    var p = new scalaSci.math.plot.Plot2DPanel("SOUTH");
    p.addHistogramPlot("normal deviates distribution", v, 600);
    new scalaSci.math.plot.FrameView(p);
 
```