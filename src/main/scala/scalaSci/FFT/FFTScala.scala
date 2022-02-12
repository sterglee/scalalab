
package scalaSci.FFT

import com.nr.Complex
import java.text.DecimalFormat
import scalaExec.Interpreter.GlobalValues 


import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

import edu.emory.mathcs.utils.ConcurrencyUtils

object FFTScala {

  /**
   * Calculates the Fourier transform of a set of n real-valued data points.
   * Returns the positive frequency half of their complex Fourier transform. 
   * does not destroy data, and outputs FFT in realffts and imffts arrays
   *  n must be a power of 2. 
   */
  
  // FFT of all rows of the data array
  def fft( data: Array[Array[Double]]):(Array[Array[Double]], Array[Array[Double]]) = {
    var Nrows = data.length
    var Ncols = data(0).length
    var resultReFFT = Array.ofDim[Double](Nrows, Ncols)  // real parts of the results
    var resultImFFT = Array.ofDim[Double](Nrows, Ncols)   // imaginary parts of the results
    
    var row = 0
    while (row< Nrows) {  // perform FFT of one row
          var currentRow = data(row)  // get current row
          var (reFFT, imFFT) = fft(currentRow) 
          var col = 0
          var sizFFT = reFFT.length
          while (col < sizFFT)  { // copy to results
            resultReFFT(row)(col) = reFFT(col)
            resultImFFT(row)(col) = imFFT(col)
            col += 1
          }
        row += 1
        } // perform FFT of one row

    (resultReFFT, resultImFFT)
        }
        
      
  
  /**
   * Calculates the Fourier transform of a set of n real-valued data points.
   * Returns the positive frequency half of their complex Fourier transform. 
   * does not destroy data, and outputs FFT in realffts and imffts arrays
   *  n must be a power of 2. 
   */
  
  // parallel FFT of all rows of the data array
  def fftp( data: Array[Array[Double]]):(Array[Array[Double]], Array[Array[Double]]) = {
    var Nrows = data.length
    var Ncols = data(0).length
    var resultReFFT = Array.ofDim[Double](Nrows, Ncols)  // real parts of the results
    var resultImFFT = Array.ofDim[Double](Nrows, Ncols)   // imaginary parts of the results
    
    var nthreads = GlobalValues.numOfThreads
    nthreads =  Math.min(nthreads, Nrows)
  
    val futures = new Array[Future[_]](nthreads)
    val rowsPerThread = (Nrows / nthreads).toInt + 1  // how many rows the thread processes
 
    var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
   
      // compute range of rows handled by that thread
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows  else  firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var row = firstRow   // the first row of the matrix that this thread processes
      while (row < lastRow) {  // the last row of the matrix that this thread processes
          var currentRow = data(row)  // get current row
          var (reFFT, imFFT) = fft(currentRow) 
          var col = 0
          var sizFFT = reFFT.length
          while (col < sizFFT)  { // copy to results
            resultReFFT(row)(col) = reFFT(col)
            resultImFFT(row)(col) = imFFT(col)
            col += 1
          }
        row += 1
        } // perform FFT of one row
    }
 })

      threadId += 1
        
  }  // for all threads

   ConcurrencyUtils.waitForCompletion(futures)
 
      
   (resultReFFT, resultImFFT)
        }
        
  
  
  def fft( data: Array[Double]) = {
    var N = data.length
       
    var  paddedData = data // we will actually pad only for signal length not a power of 2
    var p2N=java.lang.Math.ceil(log2(N)).toInt
    var newN =  java.lang.Math.pow(2, p2N).asInstanceOf[Int]
    
    if (newN != N) { // not a power of two, zero pad
      paddedData = new Array[Double](newN)
     var k=0
     while  (k<N) {
       paddedData(k) = data(k);
       k += 1
     }
     // zero-pad
     while (k<newN) {
       paddedData(k) = 0.0
       k +=1
      }
    } 
         
    N = paddedData.length
    var N2 = (N/2).toInt
    val realffts = new Array[Double](N2)
    val imffts = new Array[Double](N2)
    var  cpdata =NR.Common.copy(paddedData)  // copy the input data
  /**
   * Replaces cpdata[0..2*n-1] by its discrete Fourier transform, if isign is
   * input as 1; or replaces cpdata[0..2*n-1] by n times its inverse discrete
   * Fourier transform, if isign is input as 1. data is a complex array of
   * length n stored as a real array of length 2*n. n must be an integer power of 2.
   */
    com.nr.fft.FFT.realft(cpdata, 1)  // perform the FFT
    var  cnt = 0
    var  k = 0
    while  ( k < N) {
        realffts(cnt) = cpdata(k)
        imffts(cnt) = cpdata(k+1)
        k += 2
        cnt += 1
    }
    (realffts, imffts)
  }

  
// evaluate using the DSP library
  
  def dfft( data: Array[Double]) = {
    var N = data.length
       
    var  paddedData = data // we will actually pad only for signal length not a power of 2
    var p2N=java.lang.Math.ceil(log2(N)).toInt
    var newN =  java.lang.Math.pow(2, p2N).asInstanceOf[Int]
    
    if (newN != N) { // not a power of two, zero pad
      paddedData = new Array[Double](newN)
     var k=0
     while  (k<N) {
       paddedData(k) = data(k);
       k += 1
     }
     // zero-pad
     while (k<newN) {
       paddedData(k) = 0.0
       k +=1
      }
    } 
         
    N = paddedData.length
    var N2 = (N/2).toInt
    val realffts = new Array[Double](N2+1)
    val imffts = new Array[Double](N2)
    var  cpdata =NR.Common.copy(paddedData)  // copy the input data
  
    var lN = log2(N).toInt
    var dspfft = new DSP.fft.RDFT(lN)
    dspfft.evaluate(paddedData, cpdata)

    realffts(0) = cpdata(0)
    imffts(0) = 0.0
    var  k = 1
    while  ( k < N2) {
        realffts(k) = cpdata(k)
        imffts(k) = cpdata(N-k)
        k += 1
    }
    realffts(N2) = cpdata(N2)
    
    (realffts, imffts)
  }

  
  // compute FFT using Apache Common Library
  def afft(data: Array[Double]) = {
     scalaSci.FFT.ApacheFFT.fft(data)
  }

  
  /**
   * Calculates the sine transform of a set of n real-valued data points stored
   * in array y[0..n-1]. The number n must be a power of 2. On exit, y is
   * replaced by its transform. This program, without changes, also calculates
   * the inverse sine transform, but in this case the output array should be
   * multiplied by 2/n.
   */
  def sinft( data: Array[Double]) = {
    var N = data.length
       
    var  paddedData = data // we will actually pad only for signal length not a power of 2
    var p2N=java.lang.Math.ceil(log2(N)).toInt
    var newN =  java.lang.Math.pow(2, p2N).asInstanceOf[Int]
    
    if (newN != N) { // not a power of two, zero pad
      paddedData = new Array[Double](newN)
     var k=0
     while  (k<N) {
       paddedData(k) = data(k);
       k += 1
     }
     // zero-pad
     while (k<newN) {
       paddedData(k) = 0.0
       k +=1
      }
    } 
         
    N = paddedData.length
    var N2 = (N/2).toInt
    val realffts = new Array[Double](N2)
    val imffts = new Array[Double](N2)
    var  cpdata =NR.Common.copy(paddedData)  // copy the input data
  /**
   * Replaces cpdata[0..2*n-1] by its discrete Fourier transform, if isign is
   * input as 1; or replaces cpdata[0..2*n-1] by n times its inverse discrete
   * Fourier transform, if isign is input as 1. data is a complex array of
   * length n stored as a real array of length 2*n. n must be an integer power of 2.
   */
    com.nr.fft.FFT.sinft(cpdata)  // perform the FFT
    var  cnt = 0
    var  k = 0
    while  ( k < N) {
        realffts(cnt) = cpdata(k)
        imffts(cnt) = cpdata(k+1)
        k += 2
        cnt += 1
    }
    (realffts, imffts)
  }

  /**
   * Calculates the cosine transform of a set y[0..n] of real-valued data
   * points. The transformed data replace the original data in array y. n must
   * be a power of 2. This program, without changes, also calculates the inverse
   * cosine transform, but in this case the output array should be multiplied by
   * 2/n.
   */

    def cosft( data: Array[Double]) = {
    var N = data.length
       
    var  paddedData = data // we will actually pad only for signal length not a power of 2
    var p2N=java.lang.Math.ceil(log2(N)).toInt
    var newN =  java.lang.Math.pow(2, p2N).asInstanceOf[Int]
    
    if (newN != N) { // not a power of two, zero pad
      paddedData = new Array[Double](newN)
     var k=0
     while  (k<N) {
       paddedData(k) = data(k);
       k += 1
     }
     // zero-pad
     while (k<newN) {
       paddedData(k) = 0.0
       k +=1
      }
    } 
         
    N = paddedData.length
    var N2 = (N/2).toInt
    val realffts = new Array[Double](N2)
    val imffts = new Array[Double](N2)
    var  cpdata =NR.Common.copy(paddedData)  // copy the input data
  /**
   * Replaces cpdata[0..2*n-1] by its discrete Fourier transform, if isign is
   * input as 1; or replaces cpdata[0..2*n-1] by n times its inverse discrete
   * Fourier transform, if isign is input as 1. data is a complex array of
   * length n stored as a real array of length 2*n. n must be an integer power of 2.
   */
    com.nr.fft.FFT.cosft1(cpdata)  // perform the FFT
    var  cnt = 0
    var  k = 0
    while  ( k < N) {
        realffts(cnt) = cpdata(k)
        imffts(cnt) = cpdata(k+1)
        k += 2
        cnt += 1
    }
    (realffts, imffts)
  }


  
  // return also the frequency axis, when the Sampling Frequency is passed
  def fft(data: Array[Double], SFreq: Double) = {
    var N = data.length
    
    var paddedData = data  //  we will actually pad only for signal length not a power of 2 
    var p2N = java.lang.Math.ceil(log2(N)).toInt
    var newN = java.lang.Math.pow(2, p2N).asInstanceOf[Int]
    
    if (newN != N)  { // not a power of two, zero pad
        paddedData = new Array[Double](newN)
        var k=0
        while (k<N)  {
          paddedData(k) = 0.0
          k += 1
          }
       }
   
    N = paddedData.length
    val N2 = (N/2).toInt
    val Delta = 1.0/SFreq  // the sampling interval
    val ND = N * Delta
    val freqs = new Array[Double](N2)  // the positive frequency axis half of the complex Fourier Transform
    val realffts = new Array[Double](N2)
    val imffts = new Array[Double](N2)
    var  cpdata =NR.Common.copy(data)  // copy the input data
  
    /**
   * Replaces cpdata[0..2*n-1] by its discrete Fourier transform, if isign is
   * input as 1; or replaces cpdata[0..2*n-1] by n times its inverse discrete
   * Fourier transform, if isign is input as 1. data is a complex array of
   * length n stored as a real array of length 2*n. n must be an integer power of 2.
   */
    com.nr.fft.FFT.realft(cpdata, 1)  // perform the FFT

      
    var  cnt = 0
    var  k = 0
    //positive frequencies
    while  ( k <= N2) {
        realffts(cnt) = cpdata(k)
        imffts(cnt) = cpdata(k+1)
        freqs(cnt) = cnt/ND
        k += 2
        cnt += 1
    }
/*
realffts(cnt) =  cpdata(k)
imffts(cnt) = cpdata(k+1)
freqs(cnt) = 1.0/(2.0*Delta)

  cnt += 1
// negative frequencies
    var currNegFreq  = N2-1
    while  (currNegFreq>=1) {
        realffts(cnt) = cpdata(k)
        imffts(cnt) = cpdata(k+1)
        freqs(cnt) = -currNegFreq/ND
        currNegFreq -= 1
        cnt += 1
        k += 2
    }
   */ 
    (realffts, imffts, freqs)
  }

  
     
     
  /*

   import scalaSci.FFT.FFTScala._
   var N = 2^^^12
var low = 0.0; var up = 1.0;   // limits of interval
var  Delta  = (up-low)/N   // sampling interval
var Nf = 0.5/Delta  // the Nyquist frequency
var t = linspace(0, 1, N)
var F = 2 * PI * Nf/3.0   
var x = sin(F*t)

var (reF, imF, freqF)  = fft(x, Nf)
plot(freqF, reF) 
   */
// !!new
def ifft(realffts: Array[Double], imffts: Array[Double]) =  {
  val M = realffts.length
  val N = M*2
  val rd = new Array[Double](N)
  var k = 0
  var k2 = 0
  while (k < M) {
    rd(k2) = realffts(k)
    rd(k2+1) = imffts(k)
    k += 1
    k2+=2
  }
  com.nr.fft.FFT.realft(rd, -1)   // perform the inverse FFT
  
    // normalize properly
  val Nd2 = N/2
  k=0
  while (k<N) {
    rd(k) /= Nd2
    k += 1
  }
  rd
}
  
def  four1(data: Array[Double],  n: Int,  isign: Int) =  {
  com.nr.fft.FFT.four1(data, n, isign)
}

 def  four1(data: Array[Double],  isign: Int) = {
    com.nr.fft.FFT.four1(data, isign)
  }

def   four1(data: Array[Complex], isign: Int) = {
    com.nr.fft.FFT.four1(data, isign)
}

  /**
   * Calculates the Fourier transform of a set of n real-valued data points.
   * Replaces these data (which are stored in array data[0..n-1]) by the
   * positive frequency half of their complex Fourier transform. The real-valued
   * first and last components of the complex transform are returned as elements
   * data[0] and data[1], respectively. n must be a power of 2. This routine
   * also calculates the inverse transform of a complex data array if it is the
   * transform of real data. (Result in this case must be multiplied by 2/n.)
   */
def  realft(data: Array[Double], isign: Int) = {
  com.nr.fft.FFT.realft(data, isign)
}

  
  
  
  /**
   * Calculates the "staggered" cosine transform of a set y[0..n-1] of
   * real-valued data points. The transformed data replace the original data in
   * array y. n must be a power of 2. Set isign to C1 for a transform, and to 1
   * for an inverse transform. For an inverse transform, the output array should
   * be multiplied by 2/n.
   */
  def cosft2(y: Array[Double], isign:  Int) =  {
    com.nr.fft.FFT.cosft2(y, isign)
  }
    
  
  /**
   * Replaces data by its ndim-dimensional discrete Fourier transform, if isign
   * is input as 1. nn[0..ndim-1] is an integer array containing the lengths of
   * each dimension (number of com- plex values), which must all be powers of 2.
   * data is a real array of length twice the product of these lengths, in which
   * the data are stored as in a multidimensional complex array: real and
   * imaginary parts of each element are in consecutive locations, and the
   * rightmost index of the array increases most rapidly as one proceeds along
   * data. For a two-dimensional array, this is equivalent to storing the array
   * by rows. If isign is input as 1, data is replaced by its inverse transform
   * times the product of the lengths of all dimensions.
   * 
   */
 
  def fourn(data: Array[Double], nn: Array[Int], isign: Int ) = {
    com.nr.fft.FFT.fourn(data, nn, isign)
  }
  
  /**
   * Given a three-dimensional real array data[0..nn1-1][0..nn2-1][0..nn3-1]
   * (where nn1 D 1 for the case of a logically two-dimensional array), this
   * routine returns (for isign=1) the complex fast Fourier transform as two
   * complex arrays: On output, data contains the zero and positive frequency
   * values of the third frequency component, while speq[0..nn1-1][0..2*nn2-1]
   * con- tains the Nyquist critical frequency values of the third frequency
   * component. First (and sec- ond) frequency components are stored for zero,
   * positive, and negative frequencies, in standard wraparound order. See text
   * for description of how complex values are arranged. For isign=-1, the
   * inverse transform (times nn1*nn2*nn3/2 as a constant multiplicative factor)
   * is performed, with output data (viewed as a real array) deriving from input
   * data (viewed as complex) and speq. For inverse transforms on data not
   * generated first by a forward transform, make sure the complex input data
   * array satisfies property (12.6.2). The dimensions nn1, nn2, nn3 must always
   * be integer powers of 2.
   * 
   */
  
  def rlft3(data: Array[Double], speq: Array[Double], isign: Int, nn1: Int, nn2: Int, nn3: Int) = {
    com.nr.fft.FFT.rlft3(data, speq, isign, nn1, nn2, nn3)
  }
  
  def rlft3(data:  Array[Array[Array[Double]]], speq: Array[Array[Double]], isign: Int) = {
    com.nr.fft.FFT.rlft3(data, speq, isign)
  }
  
 def rlft3(data: Array[Array[Double]], speq: Array[Double], isign: Int) = {
   com.nr.fft.FFT.rlft3(data, speq, isign)
 }  
 
  
  /**
   * 
   * @param file
   * @param nn
   * @param isign
   * @throws IOException
   */
  def fourfs( file: Array[java.nio.channels.FileChannel], nn: Array[Int], isign: Int ) = {
    com.nr.fft.FFT.fourfs(file, nn, isign)
  }
  

// print all  the FFT results
def  printft( data: Array[Double]) = {
    var  fiveDigit = new DecimalFormat("0.00000E0")
    var L = data.length
    var Ld2 = (L/2).toInt  // half the data length, since each frequency occupies two slots
    
    println("\n\nFreq       Real Part                Imaginary Part\n")
    //  positive frequencies are described in data[0..Ld2-1] in increasing magnitudes
    //  and with two slots for each frequency (for the real and imaginary part of each coefficient)
    var  magnitude = 0
    var k = 0
    while  (k < Ld2) {  
        println(""+ (magnitude) +"               "+ fiveDigit.format(data(k)+"            "+fiveDigit.format(data(k+1))))
        k += 2
        magnitude += 1
         }

    // negative frequencies are described in data[L-1..Ld2]  in increasing magnitudes
    //  and with two slots for each frequency (for the real and imaginary part of each coefficient)
    magnitude = 1
    k = L-1
    while (k > Ld2) {
println(""+(-magnitude) +"              "+ fiveDigit.format(data(k-1)+"            "+fiveDigit.format(data(k))))
magnitude += 1
k -= 2
    }
    
    }

// print from  the FFT results the first NFreqs
def  printft(data: Array[Double], nfreqs: Int) =  {
    var  fiveDigit = new DecimalFormat("0.00000E0")
    var  L = data.length
    var  Ld2 = (L/2).toInt  // half the data length, since each frequency occupies two slots
    
    println("\n\nFreq       Real Part                Imaginary Part\n")
    //  positive frequencies are described in data[0..Ld2-1] in increasing magnitudes
    //  and with two slots for each frequency (for the real and imaginary part of each coefficient)
    var  magnitude = 0
    var k = 0
    var breakLoop = false
    while (k<Ld2 && breakLoop == false)  {
      println(""+ (magnitude) +"               "+ fiveDigit.format(data(k)+"            "+fiveDigit.format(data(k+1))))
      k += 2
      magnitude += 1
      if  (magnitude == nfreqs )  breakLoop = true
                    
    }
    
    
    // negative frequencies are described in data[L-1..Ld2]  in increasing magnitudes
    //  and with two slots for each frequency (for the real and imaginary part of each coefficient)
    magnitude = 1
    k = L-1
    breakLoop = false
    while (k>Ld2 && breakLoop == false)  {
         println(""+(-magnitude) +"              "+ fiveDigit.format(data(k-1)+"            "+fiveDigit.format(data(k))))
         k -= 2
         magnitude += 1
         if (magnitude==nfreqs)  breakLoop = true
       }

  }
  

 def log2(x: Double) = {
    var conv = java.lang.Math.log(2.0)
    java.lang.Math.log(x)/conv
}

    
}


/*  Example:
 var N = 1024
 var t = linspace(0, 1, N)
 var x = cos(3.4*t)
 var (fxR, fxI) = com.nr.fft.FFTScala.fft(x)
 */
