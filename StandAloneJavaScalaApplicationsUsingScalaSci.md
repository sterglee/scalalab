# Introduction #

`It is easy to construct independent applications in Java/Scala that use ScalaLab functionality (i.e. "scalaSci"). We describe here the required steps for the Netbeans framework, similar will be the process for other IDEs.`

`The first step is to download the archive "ScalaSciFromJavaScalaLibsAndExamplesScalaLab292.zip" or a similar name that contain the required .jar library files for ScalaLab292 and to extract these files.`

`The next and final step is to create a Netbeans Java / Scala project that uses the extracted .jar files as Compile/Run time libraries. `

`Then we can develop Java / Scala applications that use scalaSci. We present below an example simple Java and a Scala one. `

`The archives contain also the sources of simple examples and a .jar executable that can be executed with "java -jar [jarFileName]" `

## Java scalaSci example ##

`The following example illustrates the use of scalaSci from Java. Use all the .jar files extracted from "ScalaSciFromJavaScalaLibs210.zip" (or "ScalaSciFromJavaScalaLibs282.zip") as Compile/Run time libraries, and define simplePlot as the project's Main Class. Then you can execute the application with `

` java -jar simplePlot.jar `

```


import scalaSci.math.plot.plot;

import scalaSci.Vec;
import static scalaSci.StaticMaths.*;  
import static scalaSci.math.plot.plot.*;
import static scalaSci.math.plot.plotTypes.*;



public class simplePlot {
    public static void main(String [] args) {
        // scalaSci.Vec v = new scalaSci.Vec(100);
        int N = 2000;
        Vec v2 = vrand(N);
        double[] vfft = getReParts(fft(v2));
        figure(); subplot(3,1,1);    plot(v2);  title("random");
        subplot(3,1,2); plot(vfft); title("fft");

        Vec taxis = linspace(0, 2.0, N);
        double [] y = new double[N];
        for (int k=0; k<N; k++)
            y[k] = sin(3.4*taxis.get(k))+6.7*cos(3.6*taxis.get(k));
        subplot(3,1,3); plot(y); latexLabel("y=sin(3.4*x)+6.7*cos(3.6*cos(x))");
            
            }
    
}

```


## Scala scalaSci example ##

`The following example illustrates the use of scalaSci from a Scala Application object. Use all the .jar files extracted from "ScalaSciFromJavaScalaLibs210.zip" (or "ScalaSciFromJavaScalaLibs282.zip") as Compile/Run time libraries, and define testScalaSciFromScala as the project's Main Class. Then you can execute the application with `

` java -jar testScalaSciFromScala.jar `


```



import scalaSci.Vec

import scalaSci.math.plot.plot._
import scalaSci.math.plot.plotTypes._


import scalaSci.StaticMaths._

  /**
   * @param args the command line arguments
   */
  object testFFT  {
  
 def main(args: Array[String]): Unit = {
    var t = inc(0, 0.01, 100)
    var x = sin(0.56*t)+ cos(2.3*t)
    plot(t,x)
    var y = fft(x)

    var r = ifft(y)

    var rr = new Vec(getReParts(r))
    var im = new Vec(getImParts(r))

    figure(2); 
    var N = length(t)-1
    subplot(3,1,1); plot(x(0,N), "Original Signal"); xlabel("x"); 
    subplot(3,1,2); plot(rr(0,N), "reconstructed real part"); xlabel("rr");  
    subplot(3,1,3); plot(im(0, N), "reconstructed real part"); xlabel("im");  

  }
}

```

`And another Scala example`
```



import scalaSci.Vec;
import scalaSci.StaticMaths._
import scalaSci.math.plot.plot._
import scalaSci.math.plot.plotTypes._

object  testScalaSci {

  
  def main( args: Array[String] ): Unit =
          {
        
        
            var data_td = new Vec(Array( 1.0, 1.3, 1.5, 2.0, 0.9,  3.0) )
            figure( 1 )
            plot( data_td )
        
        
          }   //  main()
        
}


```