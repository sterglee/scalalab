package scalaSci.jplot

import org.jfree.chart.renderer.xy.XYItemRenderer
import java.awt.{Paint, Stroke}

/**
 * A Series is anything that can be added to a [[scalaSci.jplot.Plot]]
 * and plotted
 
 */
trait Series {
  /**
   * returns data needed by jfreechart.
   * @param defaultName series index => default name
   * @param defaultColor series index => default color
   * @param defaultStroke
   * @return
   */
  def getChartStuff(defaultName: Int=>String,
              defaultColor: Int=>Paint,
              defaultStroke: Int=>Stroke): (org.jfree.data.xy.XYDataset,XYItemRenderer)
}


object Plotting {

}
