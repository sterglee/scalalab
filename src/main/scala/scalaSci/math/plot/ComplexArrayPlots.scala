package scalaSci.math.plot

// object to plot Complex numbers
object  ComplexArrayPlots {
  import java.awt.Color
  import scalaSci.math.plot.plot._

  // Apache Commons plotting
  def plotApacheCommonsComplexArray(y: Array[org.apache.commons.math3.complex.Complex],  yname: String = "Real Values",  plotType: String = "l", 
           color: Color = Color.GREEN, lineWidth: Int = 1, plotName: String = "Plotting of Real Parts" ): scalaSci.math.plot.PlotPanel =
             {
               // construct time axis
               var N = y.length 
               var t = new Array[Double](N)
               var k = 0
               while (k < N)  { t(k) = k; k+=1 }
               // construct array of real values
               var yreals = new Array[Double](N)
               k = 0
               while  (k <  N)  {
                  yreals(k) = y(k).getReal()
                  k += 1
               }
        scalaSci.math.plot.plotTypes.plot(t, yreals,  color, plotType,  plotName, lineWidth) 
      }


// ScalaSci complex plotting
  def plotScalaSciComplexArray(y: Array[scalaSci.Complex],  yname: String = "Real Values",  plotType: String = "l", 
           color: Color = Color.GREEN, lineWidth: Int = 1, plotName: String = "Plotting of Real Parts" ): scalaSci.math.plot.PlotPanel =
             {
               // construct time axis
               var N = y.length 
               var t = new Array[Double](N)
               var k = 0 
               while  (k < N) { t(k) = k; k += 1 }
               // construct array of real values
               var yreals = new Array[Double](N)
               k = 0
               while  (k < N)  {
                  yreals(k) = y(k).getReal()
                  k += 1
                }

              scalaSci.math.plot.plotTypes.plot(t, yreals,  color, plotType,  plotName, lineWidth) 
             }




}

/*
var x =  vrand(1024)
var y = afft(x)

scalaSci.math.plot.ComplexArrayPlots.plotApacheCommonsComplexArray(y)

*/

