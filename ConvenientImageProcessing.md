# Introduction #

`We develop a Scala class that can be used for image processing tasks, easier than Java. The current code is displayed below. `


```

// SImage is a Scala class for performing more conveniently Image Processing with BufferedImage Java  images

import java.awt._
import java.awt.event._
import java.awt.image.BufferedImage
import javax.swing._
import java.io.File
import javax.imageio.ImageIO

object SImage  {
	def read(imFile: String) = {
		new SImage(ImageIO.read(new File(imFile)))
	}
}

class SImage(x: Int, y: Int) extends BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB) {

// initialize using a Java BufferedImage
    def this(bi: BufferedImage) = {
      this(bi.getWidth(), bi.getHeight())
    }

	
	// gets the raster for accessing and modifying pixels of this buffered image
	var rasterIm = this.getRaster()
	var mi = this  // used for drawing the image in display
	
	// returns the sample values for a raster point, or a rectangle of raster points,
	// in an newly allocated array whose length depends on the color model
	def apply(x: Int, y: Int): Array[Int]  = rasterIm.getPixel(x, y, null) 
	def apply(x: Int, y: Int, width: Int, height: Int): Array[Int]  = rasterIm.getPixels(x, y, width, height, null) 

	// sets the sample data for a raster point. data is an array filled with the sample data
	// for a pixel. Its element type and length depend on the model
	def update(x: Int, y: Int, data: Array[Int]) = 
	   rasterIm.setPixel(x, y, data)
	   
	def update(x: Int, y: Int, v: Int) = {
		rasterIm.setPixel(x, y, Array(v, v, v, v))
	}
	def update(x: Int, y: Int, v: Color) = {
		rasterIm.setPixel(x, y, Array(v.getRed(), v.getBlue(), v.getGreen(), v.getAlpha()))
	}

	//def read( imageFile: String) = { }
	
	def display = { 
		var xf = new JFrame()
		xf.add(new JComponent()
         {
            override def paintComponent(g:Graphics)    =        {
                setSize(x, y)
                g.drawImage(mi, 0, 0, null)
            }
         });
		xf.setSize(x, y)
		xf.setVisible(true)
	}
}

var rx = Color.RED //  new Color(30,40, 77, 7)
rx.getRed
var xi = new SImage(300, 400)

for (k<-0 until 300)
 for (l<-0 until 400)
 xi(k, l)= rx //(rand*60).toInt

 xi.display
 xi(17,7, 3, 7)

```