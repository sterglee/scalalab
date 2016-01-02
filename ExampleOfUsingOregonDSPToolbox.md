# `Introduction` #

`The Oregon DSP toolbox is one of the best Java open-source DSP libraries. Below we present an example of using that toolbox from ScalaSci toolbox. The first step of course is to download the OregonDSP.jar toolbox and to install it in ScalaLab. Then we can run the code that we provide below.`


`The Oregon DSP toolbox was developed for ScalaLab by ` **`Darwin  Airola`**

`Currently, ScalaLab292 only supports the new Darwin's DSP routines.`

# `ScalaSci Code using the OregonDSP (Use ScalaLab292)` #

```

import scalaSci.Vec                              //  ScalaSci Vector class
import scalaSci.darwin.Util._                             //  Darwin's utility class

////////////////////////////////////////////////////////////////////////////////


object Main
{

  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]): Unit =
  {

    //  method related imports
    import com.oregondsp.signalProcessing.filter.fir.equiripple.EquirippleBandpass
    import scalaSci.math.plot.plot._
    import scalaSci.math.plot.plotTypes._
    
    // instantiate filter - this constructor calls the filter design routines
    // Arguments: EquirippleBandpass(
    //                   int N, double OmegaS1, double Ws1, double OmegaP1,
    //                   double OmegaP2, double Wp, double OmegaS2, double Ws2 )
    //  N:        the order of the filter (filter will end up having an impulse
    //            response of 2*N+1 samples long
    //  OmegaS1:  the upper cutoff of the first stop band (specified as a
    //            fraction of 1.0, the folding, or Nyquist, frequency)
    //  Ws1:      the weight for the first stop band in the design algorithm
    //            (Parks-McClellan)
    //  OmegaP1:  the lower cutoff of the passband
    //  OmegaP2:  the upper cutoff of the passband
    //  Wp:       the design weight for the passband
    //  OmegaS2:  the lower cutoff of the upper stop band
    //  Ws2:      the design weight for the upper stop band

    val EBP:EquirippleBandpass = new EquirippleBandpass( 50, 0.07, 1.0, 0.12, 0.38, 1.0, 0.43, 1.0 )

    //  Note:  there must be transition bands between the passband and the two
    //  stop bands.  In this example, the transition bandwidth is 0.05
    //  normalized frequency units wide (5% of the folding frequency).  The
    //  narrower the transition band, the larger the filter order N must be to
    //  get a reasonable response (small ripples).  Here the nominal passband
    //  cutoffs are 0.12 and 0.38, so the stop band edges are at 0.07 and 
    //  0.43.  To understand how to choose the transition bandwidth and filter
    //  order have a look at:
    
    //  Unified Approach to the Design of Optimum Linear Phase FIR Digital
    //  Filters, James H. McClellan and Thomas W. Parks (1973),
    //  IEEE Transactions on Circuit Theory, Vol. CT-20, No. 6, pp. 697-701.
    //
    //  and:
    //
    //  FIR Digital Filter Design Techniques Using Weighted Chebyshev
    //  Approximation, Lawrence R. Rabiner, James H. McClellan and Thomas W.
    //  Parks (1975) PROCEEDINGS OF THE IEEE, VOL. 63, NO. 4, pp. 595-610.

    var x = new Array[Float]( 400 )  //  x contains the sequence to be filtered
    x(100) = 1.0f                    //  impulse at sample 101

    // filter data
    
    val y = EBP.filter( x )    // y contains the filtered result

    figure( 1 )
    subplot( 2, 1, 1 ) ; plot( y )
    title( "Impulse Response" )
    ylabel( "signal level" ) ; xlabel( "time [s]" )
    latexLabel( "Impulse Response" )

    val filterCoefficientsFloat = EBP.getCoefficients()
    val filterCoefficients = new Array[Double](filterCoefficientsFloat.length)
    for ( i <- 0 until filterCoefficients.length )
    {
      filterCoefficients(i) = filterCoefficientsFloat(i).asInstanceOf[Double]
    }
    //val freqResponse = abs( fft(filterCoefficients) )  //  FFT magnitude

    val b = new Vec( filterCoefficients )
    val a = new Vec( Array( 1.0 ) )
    val (h,w) = freqz( b, a, b.length, 44100.0 )


    //figure( 2 )
    subplot( 2, 1, 2 ) ; plot( w, abs(h) )
    title( "Frequency Response" )
    ylabel( "magnitude" ) ; xlabel( "frequency [Hz]" )
    latexLabel( "Frequency Response" )

  }   //  main()


}   //  object Main

// now execute the main() of object Main
var args = new Array[String](1)
args(0)=""
Main.main(args)

```