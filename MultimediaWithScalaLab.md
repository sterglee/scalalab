# Introduction #

`ScalaLab has a version, i.e. ` **`ScalaLabMediaReady`** ` ,  that comes with preinstalled the basic Java libraries required for multimedia work. We present here some examples.`


# Examples #

## `DFT Tester with Java Advanced Imaging` ##

`To run this example you should have the file:  fruits.png, at the root  directory of ScalaLab. You can of course have any .png file as sample. You can easily find the fruits.png image file by performing a Google search. Then save it, at the directory where ScalaLab starts.`


```


import java.awt._
import javax.swing._
import java.io._
import javax.media.jai.JAI
import javax.media.jai.operator.DFTDescriptor
import javax.media.jai.KernelJAI
import javax.media.jai.RenderedOp
import javax.media.jai.RenderedImageList
import java.awt.image.renderable.ParameterBlock
import java.awt.image._
import java.awt.geom._

//    DFTTester -- displays the provided image along side its DFT

class ch6Display(image: RenderedImage)  extends JPanel {
	var source: RenderedImage = null
	
	source = image
	setPreferredSize(new Dimension(source.getWidth(), source.getHeight()))

	 override def paintComponent(g: Graphics) = 
	 {
	 	var g2d = g. asInstanceOf[Graphics2D]

	 	   var insets = getInsets()
	 	   var tx = insets.left - source.getMinX()
	 	   var ty = insets.top - source.getMinY()

	 	   var af = AffineTransform.getTranslateInstance(tx, ty)

	 	   g2d.drawRenderedImage(source, af)

	 }

	    
}


    def loadImage(filename: String ) =  {
	var param = new ParameterBlock()
	param.add(filename)
	 JAI.create("Fileload", param)
    }

    /**
       perform Discrete Fourier Transform
    */
    def computeDFT(ro: RenderedOp ) =  {
	var  param =  new ParameterBlock()
	param.addSource(ro)
	param.add(DFTDescriptor.SCALING_NONE)
	param.add(DFTDescriptor.REAL_TO_COMPLEX)
	JAI.create("DFT", param)
    }

    /**
       computes the magnitude image from a supplied complex image.  
       The number of bands will be decreased by 2
    */
    def computeMagnitudes(ro: RenderedOp) = {
	var  param =  new ParameterBlock()
	param.addSource(ro)
	JAI.create("Magnitude", param)
    }

    /**
       performs log(pixelValue+1)
    */
    def computeLogImage(ro: RenderedOp) =  {
	
	var param =  new ParameterBlock()
	param.addSource(ro)
	var  constant = Array( 1.0 )  
	param.add(constant)
	var  tmp = JAI.create("addConst", param)

	param =  new ParameterBlock()
	param.addSource(tmp)
	JAI.create("log", param)
    }


    /**
       1. scales input image so that the maximum value is 255
       2. shifts scaled image so DC frequency is in center
       3. formats shifted image to a datatype of byte
    */
    def  formatForDisplay(ro: RenderedOp) = {
	var param=  new ParameterBlock()
     param.addSource(ro)
     param.add(null)
     param.add(1)
     param.add(1)
     var  statsImage = JAI.create("Extrema", param)
	var  maximum = (statsImage.getProperty("maximum")). asInstanceOf [ Array[Double]]

	param = new ParameterBlock()
	param.addSource(ro)
	var scale = Array( 255.0/maximum(0) )
	param.add(scale)
	var offset = Array(0.0)
	param.add(offset)
	var rescaledImage = JAI.create("Rescale", param)

	param = new ParameterBlock()
	param.addSource(rescaledImage)
	param.add(new Integer(rescaledImage.getWidth()/2))
	param.add(new Integer(rescaledImage.getHeight()/2))
	var shiftedImage = JAI.create("PeriodicShift", param);

	param = new ParameterBlock()
	param.addSource(shiftedImage)
	param.add(java.awt.image.DataBuffer.TYPE_BYTE)
	JAI.create("format", param)
    }

   
var filename = "fruits.png"

var DFTTester = new JFrame("DFT Tester")

var inputImage = loadImage(filename)
var dftComplexImage = computeDFT(inputImage)
var dftMagnitudeImage = computeMagnitudes(dftComplexImage)
var dftLogImage = computeLogImage(dftMagnitudeImage)
var formattedLogImage = formatForDisplay(dftLogImage)

DFTTester.setLayout(new GridLayout(1,2))
DFTTester.add(new ch6Display(inputImage))
DFTTester.add(new ch6Display(formattedLogImage))

	DFTTester.pack()
	DFTTester.setVisible(true)
    


 

```