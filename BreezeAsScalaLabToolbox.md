# Introduction #

`Breeze is a capable Scala mathematical library. We can use it from ScalaLab by installing the appropriate .jar files as ScalaLab toolboxes.`

`Actually, the Breeze binaries exist within the Apache Spark libraries, thus if we install the Apache Spark bundle we can have also the Breeze functionality. Also, we can install seperately the Breeze binaries bundled in a different .zip file at the ScalaLab's Sourceforge downloads`

`We present examples using Breeze, taken from: `


https://github.com/scalanlp/breeze/wiki/Quickstart

# Example 1 #
```
import breeze.optimize._


val f = new DiffFunction[DenseVector[Double]] {
                    def calculate(x: DenseVector[Double]) = {
                      (norm((x - 3.0) :^ 2.0, 1.0),(x * 2.0) - 6.0);
                    }
                  }
                  
                  
f.valueAt(DenseVector(3,3,3))


f.gradientAt(DenseVector(3,0,1))                                    

f.calculate(DenseVector(0,0))


def g(x: DenseVector[Double]) = (x - 3.0):^ 2.0 sum

g(DenseVector(0.0,0.0,0.0))


```