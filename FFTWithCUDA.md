# `Very fast FFT using CUDA ` #

`ScalaLab performs FFT very fast using JCUFFT. For example, the following FFT is executed in 0.0084 sec vs 0.022 sec of the recent MATLAB R2012 b.`

```
var N = 2 << 20
var x = vrand(N).getv

tic
var y = jcufft(x)
var tm = toc
```

# `Parallel FFT Transforms with CUDA in ScalaLab` #

`The script below illustrates a first attempt to integrate CUDA FFT library in ScalaLab. The speedup is significant, on newer NVIDIA cards with compute capability from 3.0 and above, CUDA FFT runs more than 20 times faster than Java or C FFTs. `

```


var N = 2^^^22 // signal length
var x = new Array[Float](N)
var dx = new Array[Double](N)

// cfor is in replacement of
// for (k<-0 until N)
// which is rather slow
cfor (0) ( _ < N, _+1) { k=>
  x(k) = (4.5*sin(0.03*k)+rand).toFloat 
  dx(k) = x(k)
  }
  
var realImffts = new Array[Float](N)

var co = new CUDAOps.KernelOps

// perform FFT with CUDA
tic
co.cudafft(x, N, realImffts)
var tmCUDAfft = toc


// seperate real and imaginary coefficients
var N2 = N/2
var realffts = new Array[Float](N2)
var imffts = new Array[Float](N2)

var cnt=0; var elem=0 
while (cnt<N) {
  realffts(elem) = realImffts(cnt)
  cnt+=1
  imffts(elem) = realImffts(cnt)
  cnt+=1
  elem+=1
  }
    
    
    figure(1);
    subplot(3,1,1); plot(x, "signal")
    subplot(3,1,2); plot(realffts, "real coefficients of FFT");
    subplot(3,1,3); plot(imffts, "imaginary coefficients of FFT");
    
      // Java FFT
    tic
    var (jfft_real, jfft_im) = fft(dx)
    var tmJAVAfft = toc

```

## Using JCUDA with support for high level operations (Nov 04 version and after) ##

`We adapt here the example from the JCUDA site`

```


import java.util.Random
import jcuda.jcufft._
import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D

//  Creates an array of the specified size, containing some random data
     
    def  createRandomFloatData( x: Int) = 
    {
        var random = new Random(1)
        var  a = new Array[Float](x)
        var i = 0
        while (i < x)
        {
                a(i) =  random.nextFloat()
                i += 1
        }
        a
    }



    // Compares the given result against a reference, and returns whether the
    // error norm is below a small epsilon threshold
  def  isCorrectResult(result: Array[Float], reference: Array[Float]) =
    {
        var errorNorm = 0.0f
        var refNorm = 0.0f
        var i = 0
        while (i < result.length)
        {
                var diff = reference(i) - result(i)
          errorNorm += diff * diff
          refNorm += reference(i) * result(i)
          i += 1 
        }
        errorNorm = Math.sqrt(errorNorm).asInstanceOf[Float]
        refNorm =  Math.sqrt(refNorm).asInstanceOf[Float]
        if (Math.abs(refNorm) < 1e-6)
        {
            false
        }
        else  {        
                if (errorNorm / refNorm < 1e-6f) true else false 
                }
    
}

// Test the 1D C2C transform with the given size.

        var size = 1 << 20
        println("Creating input data...")
        var  input = createRandomFloatData(size * 2)

        println("Performing 1D C2C transform with JTransforms...")
        var  outputJTransforms = input.clone()
        var fftj = new FloatFFT_1D(size)
        tic
        fftj.complexForward(outputJTransforms)
        var tmjtransforms = toc
        var doubleData = new Array[Double](size)
        var k=0
        while (k<size) { doubleData(k) = outputJTransforms(k); k+=1}
        
        tic 
        var xfft = fft(doubleData)
        var tmScala = toc
        
        println("Performing 1D C2C transform with JCufft...")
        var  outputJCufft = input.clone()
        var  plan = new cufftHandle()
        JCufft.cufftPlan1d(plan, size, cufftType.CUFFT_C2C, 1)
        tic
        JCufft.cufftExecC2C(plan, outputJCufft, outputJCufft, JCufft.CUFFT_FORWARD)
        var tmfftcuda = toc
        
        JCufft.cufftDestroy(plan)


        var  passed = isCorrectResult(outputJTransforms, outputJCufft)
        println("passed = "+ passed+ ", time JTransforms = "+tmjtransforms+", timeScala = "+tmScala+", time FFT CUDA = "+tmfftcuda)

 // use now the JCUBLAS
tic
var xxf = jcufft(input)
var tmjcublas = toc
    

```