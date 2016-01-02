# Introduction #

`We present some examples using routines from the jSci library.`


# Fourier Display #

```


import java.awt._
import java.awt.event._
import JSci.awt._
import JSci.maths._


var FourierDisplay = new JFrame("Fourier Display")
val N=128
var fns = new java.awt.List(4)
var inverse = new Checkbox("inverse")
var doInverse:Boolean = false
var signal = new Array[Double](1)
var xAxis = new Array[Float](N)


 //  Under transform should give something like exp(-x^2).  Real spectrum.
def  gaussian(n:Int, amplitude: Double, k: Double) = {
                var data=new Array[Double](n)
                var  x = 0.0
                for(i<-0 to n-1) {
                        x = (i-n/2) / k;
                        data(i) = amplitude*Math.exp( -x*x)
                }
                data
        }

 //  Under transform should give something like cos(x)/x. Real spectrum.
def  topHat(n: Int,  amplitude: Double) = {
                var  data = new Array[Double](n)
                var  i = 0
                while ( i < n/4)  {
                   data(i) = 0.0
                   i += 1
                   }
                while (i<3*n/4)  {
                   data(i) = amplitude
                   i += 1
                   }
                 while (i<n) {
                   data(i) = 0.0
                   i+=1
                   }
             data
        }

//  Under transform should give a delta-function at origin. Real spectrum.
def constant(n: Int, amplitude: Double) = {
       var data = new Array[Double](n)
       for (i<-0 to n-1)
           data(i) = amplitude
   data
 }
 
// Under transform should give something like i*sin(x)/x. Complex spectrum.
def square(n: Int, amplitude: Double) = {
  var  data = new Array[Double](n)
  var i=0
  while(i<n/2) {
     data(i) = -amplitude
     i += 1
       }
    while (i<n) {
      data(i) = amplitude
      i += 1
       }
   data
}

// Under transform should give something like i*sin(x)/x^2.  Complex spectrum.
def  triangle(n: Int, amplitude: Double) = {
   var data = new Array[Double](n)
   var gradient = amplitude*4.0/n
   var i=0
   while (i<n/4)  {
      data(i) = -gradient*i
      i += 1
        }
   while (i<3*n/4)  {
      data(i) = -2.0*amplitude+gradient*i
      i += 1
        }
   while (i<n) {
      data(i) = 4.0*amplitude-gradient*i
      i += 1
     }
data
}

        // Under transform should give two delta-functions at +/- frequency. Complex spectrum.
def sine(n: Int, amplitude: Double, cycles: Int) = {
      var data = new Array[Double](n)
      var w = NumericalConstants.TWO_PI*cycles/n
      for (i<-0 to n-1)
          data(i) = amplitude*Math.sin((i-n/2)*w)
   data
 }


def displayTransform = {
   var result = new Array[JSci.maths.Complex](1)
 if (doInverse)
  result = FourierMath.sort(FourierMath.inverseTransform(FourierMath.sort(signal)))
else
  result = FourierMath.sort(FourierMath.transform(FourierMath.sort(signal)))
 
var realpart = new Array[Float](N)
var imagpart = new Array[Float](N)
for (i<-0 to N-1)  {
   realpart(i) = result(i).real.asInstanceOf[Float]
   imagpart(i) = result(i).imag.asInstanceOf[Float]
  }

figure(1)
clf(1)
 plot(realpart, Color.BLUE)
 plot(imagpart, Color.RED)
}
 
for (i<-0 to N-1)
   xAxis(i) = i-N/2

fns.add("Gaussian")
fns.add("Top hat")
fns.add("Constant")
fns.add("Square")
fns.add("Triangle")
fns.add("Sine")
fns.select(5)

var itemLis = new  ItemListener() {
 override def itemStateChanged(evt: ItemEvent ) = {
          fns.getSelectedIndex match  {
  case 0 =>    signal = gaussian(N,1.0,5.0)      
  case 1 =>    signal = topHat(N,1.0)
  case 2 =>    signal = constant(N,1.0)
  case 3 =>    signal = square(N,1.0)
  case 4 =>    signal = triangle(N,1.0)
  case 5 =>    signal = sine(N,1.0,16)
  case _ =>    signal = gaussian(N, 1.0, 18.0)      
              }
     displayTransform
         }
}

fns.addItemListener( itemLis )

inverse.addItemListener( new ItemListener() {
  override def itemStateChanged(evt: ItemEvent) = {
   doInverse = ! doInverse
    displayTransform
  }
 })


var cntrl = new Panel()
cntrl.add(fns)
cntrl.add(inverse)
FourierDisplay.add(cntrl, "South")

signal =  gaussian(N, 1.0, 5.0)
displayTransform
FourierDisplay.setSize(400, 400)              
FourierDisplay.setVisible(true)
   
             

```