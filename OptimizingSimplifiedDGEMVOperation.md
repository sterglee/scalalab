# Introduction #


`These scripts try to test the paper:  "Towards Native Performance Using Java: Optimizing a simplified DGEMV operation" by ` **`Stuart Archibald`**

`We implement both Java and Scala codes according to the paper. We run ScalaLab with `

`java -server -Xcomp -d64 -Djava.library.path=./libCUDA;.;./lib -Xss5m -Xms3000m -Xmx6500m -jar ScalaLab211.jar`

`in order the JVM to perform JITing early. With the -Xcomp option, both Java and Scala code is JITed and the improvements illustrated with the paper of Stuart Archibald are indeed realizable. Specifically, execution time related to method2, dropped from 2.32 secs to 1.7secs`


`We list the codes below`

## Scala codes and testing script ##

```


// this script tries to test paper:  "Towards Native Performance Using Java: Optimizing a simplified DGEMV operation" 
// by Stuart Archibald

def method1( A: Array[Array[Double]], x: Array[Double], m: Int, n: Int) = {
        var y = new Array[Double](m)
        var i = 0
        var j = 0
        while (i < m) {
                j = 0
                while (j < n) {
                        y(i) += A(i)(j)*x(j)
                        j += 1
                }
                i += 1
                }
                y
        }

def method2( A: Array[Double], x: Array[Double], m: Int, n: Int) = {
   var y = new Array[Double](m)
   var idx = 0
   var i = 0
   while (i < m) {
      idx = i*n
      var j = 0
      while (j < n) {
         y(i) += A(idx+j)*x(j)
         j += 1
          }
     i += 1
  }
  y
}



def  method7 ( A: Array[Double], x: Array[Double], m: Int, n: Int) = {
        var y = new Array[Double](m)

        var idx=0; var ptr=0

        val extra = n - n % 8
        val ub = ((n/8)*8)-1
        var acc=0.0
        var i = 0
        while (i < m) {
                var idx = i * n
                acc = 0.0
                var j = 0
                while (j < ub) {
                        ptr = idx + j
                        y(i) += A(ptr) * x(j)+
                                        A(ptr+1)*x(j+1)+
                                        A(ptr+2)*x(j+2)+
                                        A(ptr+3)*x(j+3)

                        acc += A(ptr+4)*x(j+4)+
                                        A(ptr+5)*x(j+5)+
                                        A(ptr+6)*x(j+6)+
                                        A(ptr+7)*x(j+7)

                  j += 8
                }
                y(i) += acc
                i += 1
        }

        var j = extra
        while (j < n) {
                y(i) += A(idx+j)*x(j)
        }

 y
}

var N = 5000
var M = 5000

var A1 = Ones(N,M)
var x1 = Ones(N)

val  RepeatTimes = 100
tic
 var r1 = method1(A1, x1, N, M)
for (k<-0 until RepeatTimes)
   r1 = method1(A1, x1, N, M)
var tm1 = toc

var A7 = Ones(N*M)
var x7 = Ones(N)

tic
 var  r2  = method2(A7, x7, N, M)
for (k<-0 until RepeatTimes)
    r2  = method2(A7, x7, N, M)
var tm2=toc

tic
 var r7 = method7(A7, x7, N, M)
for (k<-0 until RepeatTimes)
   r7 = method7(A7, x7, N, M)
var tm7=toc

tic
var r1j = scalaSci.JavaOptBLAS.method1(A1, x1, M, N)
for (k<-0 until RepeatTimes)
  r1j = scalaSci.JavaOptBLAS.method1(A1, x1, M, N)
var tm1j = toc

tic
var r2j = scalaSci.JavaOptBLAS.method2(A7, x7, M, N)
for (k<-0 until RepeatTimes)
   r2j = scalaSci.JavaOptBLAS.method2(A7, x7, M, N)
var tm2j = toc

tic
var r3j = scalaSci.JavaOptBLAS.method3(A7, x7, M, N)
for (k<-0 until RepeatTimes)
   r3j = scalaSci.JavaOptBLAS.method3(A7, x7, M, N)
var tm3j = toc

tic
var r4j = scalaSci.JavaOptBLAS.method4(A7, x7, M, N)
for (k<-0 until RepeatTimes)
   r4j = scalaSci.JavaOptBLAS.method4(A7, x7, M, N)
var tm4j = toc


tic
var r5j = scalaSci.JavaOptBLAS.method5(A7, x7, M, N)
for (k<-0 until RepeatTimes)
   r5j = scalaSci.JavaOptBLAS.method5(A7, x7, M, N)
var tm5j = toc


tic
var r6j = scalaSci.JavaOptBLAS.method6(A7, x7, M, N)
for (k<-0 until RepeatTimes)
   r6j = scalaSci.JavaOptBLAS.method6(A7, x7, M, N)
var tm6j = toc


tic
var r7j = scalaSci.JavaOptBLAS.method7(A7, x7, M, N)
for (k<-0 until RepeatTimes)
  r7j = scalaSci.JavaOptBLAS.method7(A7, x7, M, N)
var tm7j = toc        
    
```


## Java Codes ##

`These are the Java codes suggested in paper:  "Towards Native Performance Using Java: Optimizing a simplified DGEMV operation" by `**`Stuart Archibald`**

```

package scalaSci;

public class JavaOptBLAS {
    
    public final static double [] method1(double [][]A, double[] x, int m, int n) {
        double [] y = new double [m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                y[i] += A[i][j]*x[j];
            }
        }
        return y;
    }
    
    public final static double [] method2(double [] A, double [] x, int m, int n) {
        double [] y = new double [m];
        int idx;
        for (int i=0; i < m; i++) {
            idx = i*n;
            for (int j=0; j<n; j++) {
                y[i]+= A[idx+j]*x[j];
            }
           }
        return y;
        }
    
    public final static double [] method3( double [] A, double [] x, int m, int n) {
        double [] y = new double [m];
        int idx, ptr;
        final int extra = n - n % 4;
        final int ub = ((n/4)*4)-1;
        for (int i=0; i < m; i++) {
            idx = i*n;
            for (int j=0; j<ub; j+=4) {
                ptr = idx+j;
                y[i] += A[ptr]*x[j];
                y[i] += A[ptr+1]*x[j+1];
                y[i] += A[ptr+2]*x[j+2];
                y[i] += A[ptr+3]*x[j+3];
            }
            for (int j = extra; j < n; j++) {
                y[i] += A[idx+j]*x[j];
            }
        }
        return y;
        }
    
    
    public final static double [] method4(double []A, double [] x, int m, int n) {
        double [] y = new double[m];
        int idx, ptr;
        final int extra = m - m%2;
        final int ub = ((n/2)*2)-1;
        for (int i=0; i < m; i++) {
            idx = i*n;
            for (int j=0; j<ub; j+=2) {
                ptr = idx+j;
                y[i] += A[ptr]*x[j]+A[ptr+1]*x[j+1];
            }
            for (int j = extra; j<n; j++) {
                y[i] += A[idx+j]*x[j];
            }
        }
        return y;
    }
    
    public final static double [] method5(double [] A, double [] x, int m, int n) {
        double [] y = new double [m];
        int idx, ptr;
        final int extra = n - n % 4;
        final int ub = ((n/4)*4) - 1;
        for (int i = 0; i < m; i++) {
            idx = i*n;
            for (int j = 0; j < ub; j += 4) {
                ptr = idx+j;
                y[i] +=  A[ptr]*x[j]
                           +A[ptr+1]*x[j+1]
                            +A[ptr+2]*x[j+2]
                            +A[ptr+3]*x[j+3];
            }
            for (int j = extra; j< n; j++) {
                y[i] += A[idx+j]*x[j];
            }
        }
        return y;
    }
    
    
    public final static double [] method6(double [] A, double [] x, int m, int n) {
        double [] y = new double [m];
        int idx, ptr;
        final int extra = n - n%8;
        final int ub = ((n/8)*8)-1;
        for (int i=0; i <m; i++) {
            idx = i*n;
            for (int j=0; j<ub; j+=8) {
              ptr = idx+j;
              y[i] += A[ptr]*x[j]
                      + A[ptr+1]*x[j+1]
                      + A[ptr+2]*x[j+2]
                      + A[ptr+3]*x[j+3]
                      + A[ptr+4]*x[j+4]
                      + A[ptr+5]*x[j+5]
                      + A[ptr+6]*x[j+6]
                      + A[ptr+7]*x[j+7];
            }
            for (int j = extra; j<n; j++) {
                y[i] += A[idx+j]*x[j];
            }
        }
        return y;
    }
    
    
                
            
            
    public final static double [] method7(double [] A, double [] x, int m, int n) {
        double [] y = new double [m];
        int idx, ptr;
        final int extra = n - n % 8;
        final int ub = ((n/8)*8)-1;
        double acc;
        for (int i = 0; i < m; i++ ) {
            idx = i*n;
            acc = 0;
            for (int j=0; j < ub; j+=8) {
                ptr = idx+j;
                y[i] += A[ptr]*x[j]
                          +A[ptr+1]*x[j+1]
                          +A[ptr+2]*x[j+2]
                          +A[ptr+3]*x[j+3];
                acc += A[ptr+4]*x[j+4]
                          +A[ptr+5]*x[j+5]
                          +A[ptr+6]*x[j+6]
                          +A[ptr+7]*x[j+7];
            }
            y[i] += acc;
            for (int j = extra; j<n; j++) {
                y[i] += A[idx+j]*x[j];
              }
            }
            return y;
        }
        
        
            }
        
    

```