# Introduction #
`The purpose of this page is to demonstrate image processing using ScalaLab.`

## Mandelbrot set ##
```
import java.awt._
import java.awt.image._
import javax.swing._

    val XMIN = -2.0; val XMAX=2.0; val YMIN= -2.0; val  YMAX=2.0; 
    val MAX_ITERATIONS = 16
    
     // the sizes of the Mandelbrot image
	var width = 1000;  	var height= 1000   

	def escapesToInfinity(a: Double, b: Double) =  {
		var x=0.0; var y=0.0
		var iterations=0
		while (x<=2 && y<=2 && iterations < MAX_ITERATIONS) {
			var xnew = x*x-y*y+a
			var ynew = 2*x*y+b
			x = xnew
			y = ynew
			iterations += 1
		}
		x > 2 || y > 2
	}

		// construct a BufferedImage object
	val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
	val raster = image.getRaster  // get it's raster
	val model = image.getColorModel   // the color model of the image
	val fractalColor = Color.red
	val argb = fractalColor.getRGB  // convert to RGB color model
	val colorData = model.getDataElements(argb, null)

	var i=0; var j=0
	while (i < width) {
		j=0
		while (j<height) {
			var a = XMIN+i*(XMAX-XMIN)/width
			var b = YMIN+j*(YMAX-YMIN)/height
			if (escapesToInfinity(a, b))  raster.setDataElements(i, j, colorData)
			j += 1
		}
		i += 1
	}


var mframe = new JFrame("Mandelbrot")
mframe.add(new JLabel(new ImageIcon(image)))
mframe.setSize(width, height)
mframe.setVisible(true)

```

## `MandelBrot set with a slighly different approach` ##

```

import java.awt._  // for the Graphics class

class mandelBrot  extends JFrame  {
        setTitle("Mandelbrot Set")
        var XSIZ = 256; var YSIZ = 256;
        setSize(XSIZ, YSIZ);

    override def paint (g: Graphics): Unit =  {
        var  w= XSIZ;  var  h= YSIZ;

        var  pix = new Array[Int](w*h)
        var  index = 0
        var  a = 0.0; var b = 0.0;  var  p = 0.0; var q = 0.0;
        var xmax = size.width; var  ymax = size.height
        g.clearRect(0,0, xmax, ymax)

    var  psq=0.0; var qsq=0.0; var pnew=0.0; var qnew=0.0;
    var breakLoop=false
    var y = 0
    while (y<h)  {
            b =  (y-128.0)/64.0
            var x = 0
            while (x< w)
            {
                a = ((x-128.0))/64.0
                p=0.0;  q=0.0
                var iter = 0
                while (iter < 32 && breakLoop==false) {
                    psq = p*p; qsq = q*q
                    breakLoop = false
                    if (psq+qsq >= 4.0) breakLoop = true
                    if (breakLoop == false) {
                     pnew = psq - qsq + a
                     qnew = 2*p*q + b
                     p = pnew
                     q = qnew
                     
                }
                iter+=1
              }
              breakLoop = false
                if (iter == 32) {
                    pix(index) = 255 << 24 | 255;
                    }
                index+=1
                x += 1
              }
              y += 1
        }

        var img = createImage(new  java.awt.image.MemoryImageSource(w, h, pix, 0, w))
        g.drawImage(img, 0, 0, null);
    } // paint
}


    var  f = new mandelBrot()
    f.setVisible(true)
    

```