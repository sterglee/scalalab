# Introduction #

`The Web camera toolbox` **`WebCam.jar`**

https://sourceforge.net/projects/scalalab/files/WebCam.jar/download

`is based on project`

`https://github.com/sarxos/webcam-capture`

`Using the Web Camera is very simple:`

`1. Install ` **`WebCam.jar`** ` as ScalaLab toolbox. An easy way to installation,  is to copy the WebCam.jar toolbox in the "defaultToolboxes" ScalaLab folder`

`2. Start the Web camera viewing by executing`

```
var wcam = new com.github.sarxos.webcam.WebcamViewer
```


`The Java programming interface of the project mentioned above, can be utilized from ScalaLab, to perform for example tasks as image capturing and motion detection.`


## `Motion Detection with ScalaLab` ##

`The following code snippet detects motion using the Web camera.`

```


import java.io.IOException

import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.WebcamMotionDetector
import com.github.sarxos.webcam.WebcamMotionEvent
import com.github.sarxos.webcam.WebcamMotionListener


    var detector = new WebcamMotionDetector(Webcam.getDefault())
    detector.setInterval(100)  // one check per 100 ms
    var motionListenerObj = new MotionListener   // the motion listener object
    detector.addMotionListener(motionListenerObj)
    detector.start()
	

class MotionListener extends AnyRef with  WebcamMotionListener {
	override def motionDetected( wme: WebcamMotionEvent) {
		//println("Detected motion I, alarm turn on you have");
  //java.awt.Toolkit.getDefaultToolkit().beep()
  tone(400,1000)

	}
```


## `Motion Detection displaying also the camera view` ##

```

import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.SwingUtilities
import com.github.sarxos.webcam._

var motionDetector = new DetectMotionExample

class DetectMotionExample extends JFrame with  Runnable with WebcamMotionListener {

   var INTERVAL = 100  // ms


   var  webcam = Webcam.getDefault()
	
	  var  updater = new Thread(this, "updater-thread")
	  updater.setDaemon(true)
	  updater.start()
             
                
           setTitle("Motion Detector")
	setLayout(new FlowLayout())

	webcam.setViewSize(new Dimension(640, 480))

	var panel = new WebcamPanel(webcam)

	add(panel)

	pack()
	setVisible(true)
	
	override def  run() = {

		var  detector = new WebcamMotionDetector(webcam)
		detector.setInterval(INTERVAL)
                      detector.addMotionListener(this)
		detector.start()

			
		}

  override def motionDetected( wme: WebcamMotionEvent) {
                //println("Detected motion I, alarm turn on you have");
  //java.awt.Toolkit.getDefaultToolkit().beep()
   tone(400,1000)

        }

	}


```

## `Taking a single image from the Web camera` ##

```

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import com.github.sarxos.webcam.Webcam
//  Example of how to take single picture.
// adapted from  Bartosz Firyn (SarXos)


var webcam = Webcam.getDefault()
webcam.open()
// get image
var  image = webcam.getImage()
// save image to PNG file
ImageIO.write(image, "PNG", new File("test.png"))



```