# Introduction #

`JFreeChart provides a powerful charting library that can be used directly from ScalaLab, as the following examples illustrate. `


# Example 1 #

```


import javax.swing.JFrame

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.PiePlot3D
import org.jfree.data.general.DefaultPieDataset
import org.jfree.data.general.PieDataset
import org.jfree.util.Rotation


class PieChart(applicationTitle: String, chartTitle: String)  extends JFrame(applicationTitle) {

    // This will create the dataset 
        var  dataset = createDataset()
        // based on the dataset we create the chart
        var  chart = createChart(dataset, chartTitle)
        // we put the chart into a panel
        var  chartPanel = new ChartPanel(chart)
        // default size
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270))
        // add it to our application
        setContentPane(chartPanel)

    
    
    /**
     * Creates a sample dataset 
     */
    def  createDataset()   = {
        var result = new DefaultPieDataset()
        result.setValue("Linux", 29)
        result.setValue("Mac", 20)
        result.setValue("Windows", 51)
        result
        
    }
    
    /**
     * Creates a chart
     */
    def createChart(dataset: PieDataset,  title: String) =  {
        
        var chart = ChartFactory.createPieChart3D(
            title,  				// chart title
            dataset,                // data
            true,                   // include legend
            true,
            false
        );

        var  plot = chart.getPlot().asInstanceOf[PiePlot3D]
        plot.setStartAngle(290)
        plot.setDirection(Rotation.CLOCKWISE)
        plot.setForegroundAlpha(0.5f)
        chart
        
    }
    
}

var myChart  = new PieChart("test Application", "test Chart")
myChart.setSize(600, 600)
myChart.setVisible(true)


```