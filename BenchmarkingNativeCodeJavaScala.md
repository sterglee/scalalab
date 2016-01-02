# Introduction #

`I tried to speed up matrix multiplication using native C code. However, I abandoned this effort simply because the C code was much slower than Java/Scala, about 4-5 times slower!! `

`However, by performing maximum optimization the C code finally runs only slightly faster than Java/Scala code, with a very insignificant difference that cannot justify the loss of portability and the large effort to build the native code. `

`I present here the code fragments that I was tested, both with the Linux gcc compiler and the Microsoft C compiler for Windows Vista 32-bit. The behavior of the two C compilers was similar, i.e. there was much slower from Java/Scala (about 4-5 times slower) and with full optimization their code become only slightly faster!!`

# Code Fragments #
`The C code fragment that performed the matrix multiplication is`

```


JNIEXPORT void JNICALL Java_nativeOps_muld   (JNIEnv *env, jclass cl, jdoubleArray a, jint m, jint n, 
	 jdoubleArray b, jint k, jdoubleArray c) {
	double *aa = (*env)->GetDoubleArrayElements(env, a, NULL);
	double *bb = (*env)->GetDoubleArrayElements(env, b, NULL);
	double *cc = (*env)->GetDoubleArrayElements(env, c, NULL);
	
        int  i, j, l;
        double  temp;
   
    
    
//Form C :=  A*B 
  for (j = 0; j < n; j++) {
    for (l = 0; l < k; l++) {
            temp =  bb[l + j * k];
            for (i = 0; i < m; i++) {
          cc[i + j * m] +=  temp * aa[i + l * m];
	}
     }
  }


	(*env)->ReleaseDoubleArrayElements(env, a, aa, 0);
	(*env)->ReleaseDoubleArrayElements(env, b, bb, 0);
	(*env)->ReleaseDoubleArrayElements(env, c, cc, 0);
	
	
}

```

`The Java equivalent is `

```

 public static void  jmult(double [] a,int m, int n, double [] b, int k, double []  c) {
	
        int  i, j, l;
        double  temp;
   
    
    
//Form C :=  A*B 
  for (j = 0; j < n; j++) {
    for (l = 0; l < k; l++) {
            temp =  b[l + j * k];
            for (i = 0; i < m; i++) {
          c[i + j * m] +=  temp * a[i + l * m];
	}
     }
  }

}

```

`The Scala equivalent both with for based and while based implementation is `

```
def smult(a: Array [Double], m: Int, n: Int , b: Array [Double],  k: Int, c: Array [Double] ) {
	
        var temp = 0.0
   
    
    
//Form C :=  A*B 
  for (j <- 0 until  n) {
    for (l <-  0 until  k) {
            temp =  b(l + j * k)
            for (i <- 0 until  m) {
          c(i + j * m) +=  temp * a(i + l * m)
	}
     }
  }

}

  def smultWhile(a: Array [Double], m: Int, n: Int , b: Array [Double],  k: Int, c: Array [Double] ) {
	
        var temp = 0.0
   
    var i=0; var j=0;  var l=0;
    
//Form C :=  A*B 
  while (j<n) {
    l=0
    while (l<k) {
            temp =  b(l + j * k)
            i = 0
            while (i<m) {
          c(i + j * m) +=  temp * a(i + l * m)
          i +=1
	}
        l +=1
     }
     j+=1
  }

}

```

`Finally, the testing code and some results from my relatively old Windows Vista 32 based PC `

```
var n = 1000
 var m = 1500
 var k = 1500
 var a = new Array [Double](n*m)
 for (k<-0 until n*m) a(k)=1.0
 var b = new Array [Double](m*k)
 for (k<-0 until m*k) b(k)=1.0
 var c = new Array [Double](n*k)
 b
 
 
 tic
 nativeOps.muld(a, n, m, b, k, c)
 var tm = toc
 c
 
  tic
 javaMult.jmult(a, n, m, b, k, c)
 var tmJava = toc
 c
 
  tic
 ScalaMult.smult(a, n, m, b, k, c)
 var tmScala = toc
 c

  tic
 ScalaMult.smultWhile(a, n, m, b, k, c)
 var tmScalaWhile = toc
 c

 //  tmNative = 3.588
 //  tmScala = 3.635
 //  tmScalaWhile = 3.557
 //  tmJava = 3.807
 
```

`As we can see from the times both the optimized native C code and Java/Scala have similar performances with rather insignifiant differences.`

`The results that are took from my newer Linux based PC with the gcc compiler are similar, only the times are scaled by 0.5 since this computer is about 2 times faster, therefore I do not repeat the experiments. `

