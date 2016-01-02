# Introduction #

`This page adapts some examples from Peter Abeles with Scala implementations for better scriptability.`


# Kalman Filter #

`The following Scala code translated from the Java code demonstrated by Peter Abeles in his EJML project, can be executed with pasting it over the ScalaLab editor, selecting the code, and pressing F6`

```

import org.ejml.data.DenseMatrix64F
import org.ejml.ops.CommonOps

import java.util.ArrayList
import java.util.List

import org.ejml.example._
// Compares how fast the filters all run relative to each other.
// adapted from Peter Abeles
 
class BenchmarkKalmanPerformance  {

    val   NUM_TRIALS = 200
    val   MAX_STEPS = 1000
    val   T = 1.0

    val   measDOF = 8

    var  filters = new ArrayList[KalmanFilter]();

    def  run() =  {
        var  priorX = new DenseMatrix64F(9,1, true, 0.5, -0.2, 0, 0, 0.2, -0.9, 0, 0.2, -0.5)
        var  priorP = CommonOps.identity(9)

        var  trueX = new DenseMatrix64F(9,1, true, 0, 0, 0, 0.2, 0.2, 0.2, 0.5, 0.1, 0.6)

        var  meas = createSimulatedMeas(trueX)

        var   F = createF(T)
        var   Q = createQ(T, 0.1)
        var   H = createH()

        var   iterator = filters.iterator()
        while (iterator.hasNext()) {
           var f = iterator.next();
            var  timeBefore = System.currentTimeMillis()

            f.configure(F,Q,H)

            for( trial <- 0  until  NUM_TRIALS) {
                f.setState(priorX,priorP)
                processMeas(f,meas)
            }

            var  timeAfter = System.currentTimeMillis()

            println("Filter = "+f.getClass().getSimpleName())
            println("Elapsed time: "+(timeAfter-timeBefore))

            System.gc()
        }
    }

    
def    createSimulatedMeas( x: DenseMatrix64F) = {
        var  ret = new ArrayList[DenseMatrix64F]()
        var  F = createF(T)
        var  H = createH()

//        UtilEjml.print(F);
//        UtilEjml.print(H);

        var  x_next = new DenseMatrix64F(x)
        var  z = new DenseMatrix64F(H.numRows,1)
        for( i <- 0 until MAX_STEPS ) {
            CommonOps.mult(F,x,x_next)
            CommonOps.mult(H,x_next,z)
            ret.add(z.copy())
            x.set(x_next)
        }
        ret
    }


    def  processMeas( f: KalmanFilter,  meas: List[DenseMatrix64F]  )
    {
        var  R = CommonOps.identity(measDOF)
        var iterator = meas.iterator()
        while (iterator.hasNext()) {
            var z = iterator.next
            f.predict()
            f.update(z,R)
        }
    }


    def   createF( T: Double ) =  {
        var a = Array( 1, 0 , 0 , T , 0 , 0 , 0.5*T*T , 0 , 0 ,
                0, 1 , 0 , 0 , T , 0 , 0 , 0.5*T*T , 0 ,
                0, 0 , 1 , 0 , 0 , T , 0 , 0 , 0.5*T*T ,
                0, 0 , 0 , 1 , 0 , 0 , T , 0 , 0 ,
                0, 0 , 0 , 0 , 1 , 0 , 0 , T , 0 ,
                0, 0 , 0 , 0 , 0 , 1 , 0 , 0 , T ,
                0, 0 , 0 , 0 , 0 , 0 , 1 , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 0 , 1 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 1 )

        new DenseMatrix64F(9,9, true, a: _*);
    }

    def  createQ( T: Double , vr: Double ) = {
        var  Q = new DenseMatrix64F(9,9)

        var  a00 = (1.0/4.0)*T*T*T*T*vr
        var  a01 = (1.0/2.0)*T*T*T*vr
        var  a02 = (1.0/2.0)*T*T*vr
        var  a11 = T*T*vr
        var  a12 = T*vr
        var  a22 = vr

        for( i <- 0 until 3 ) {
            Q.set(i,i,a00)
            Q.set(i,3+i,a01)
            Q.set(i,6+i,a02)
            Q.set(3+i,3+i,a11)
            Q.set(3+i,6+i,a12)
            Q.set(6+i,6+i,a22)
        }

        for( y <- 1 until 9 ) {
            for(  x <- 0 until  y ) {
                Q.set(y,x, Q.get(x,y))
            }
        }

        Q
    }

    def  createH() = {
        var  H = new DenseMatrix64F(measDOF, 9)
        for( i <- 0 until  measDOF ) {
            H.set(i,i,1.0)
        }

        H
    }
}

     // execute now the benchmark
  var  benchmark = new BenchmarkKalmanPerformance()

  benchmark.filters.add( new KalmanFilterAlg() )
  benchmark.filters.add( new KalmanFilterOps())
  benchmark.filters.add( new KalmanFilterSimple())


 benchmark.run()
 

```