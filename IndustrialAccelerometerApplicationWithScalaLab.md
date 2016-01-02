# Introduction #

`The ` **`PhidgetSpatial`** `is an accelerometer that connects with a USB Cable. We develop here a framework for processing signals from that accelerometer with ScalaLab.`

`The company's page is: `


http://www.phidgets.com/products.php?category=5&product_id=1043_0

# Installation #

`To connect ScalaLab with Phidget, we need to install, two types of drivers: `

**`A. Operating System Specific Driver`**

`Here we describe the installation for Linux. For Windows, it is similar and easier, since the DLLs are already pre-build.`

`We download the C sources for the libraries, named something like, libphidget-2.1.8.201218. Then, we run ` **`configure`** `and then ` **`make install`** `as ` **`superuser.`** `The native libraries are installed in ` **`/usr/lib`** `and therefore is important to` **`update the java.library.path`** `For example, with the full ScalaLab version the ` **`RunScalaLabServerLinux64.sh`** `script needs to edited as: `

```
java -server -Djava.library.path=/usr/lib:./libBLAS/static/Linux/amd64:./libBLAS/static/Linux/amd64/sse2:./libBLAS/static/Linux/amd64/sse3:./libJava3D/LinuxAMD64:./libSmart -Xss5m -Xms1500m -Xmx1500m -jar ScalaLab210.jar

```

`In my Linux installation, it is necessary to run ScalaLab as ` **`superuser`** `in order to access the USB port. `

**`B. Installation of the Java Driver`**

`This stage is much easier since it requires only to install the ` **`phidget21.jar`** `file as a ScalaLab toolbox. `


`After the installation is performed, we can process the signals that the accelerometer provides from ScalaLab. We provide some examples. `


# A simple example that tests the Phidget connection and retrieves the corresponding signals #

```
import com.phidgets._
import com.phidgets.event._

 import graph._
 import java.awt._
        
  
  object myValues {
  	import graph._
     var  maxSamples = 5000
     var  winLen = 2^^^7
    // analyzing window length
     var  accs = new RichDouble2DArray(3, maxSamples)
     var  times = new Array[Double](maxSamples)   
     var  scnt = 0 // sample count
     val   plotPerSamples = 10
     var  plotCnt = 0
     var prev: GraphPoint = null
     var current: GraphPoint = null
     var plotFrame = new JFrame("Plot of Phidget data") 
     var  plotGraph = new ScatterPlot()
     var gcnt = 0
  
  }

  closeAll
  
   myValues.plotFrame.setSize(300,300);  myValues.plotFrame.setLocation(40, 40)
   myValues.plotFrame.setLayout(new BorderLayout())
   myValues.plotGraph.setXStart(0.0);   myValues.plotGraph.setXEnd(myValues.maxSamples.asInstanceOf[Double])
   myValues.plotGraph.setYStart(-1.0);   myValues.plotGraph.setYEnd(1.0)
         
   myValues.plotFrame.add(myValues.plotGraph)
   myValues.plotFrame.setVisible(true)

                println(Phidget.getLibraryVersion())
        
        var spatial = new SpatialPhidget()
               
        
        spatial.addAttachListener(new AttachListener() {
                        def  attached(ae: AttachEvent ) = {
                                 println("attachment of " + ae)
                              try  
                                {
                                        var sae = ae.getSource().asInstanceOf[SpatialPhidget]
                                        sae.setDataRate(296)   // set data rate to 196ms
                                }
                                catch {
                                  case pe:PhidgetException =>                           
                                        println("Problem setting data rate!")
                                  case _: Exception => println("General exception")
                                  }     
                                }
                        })

                        
                spatial.addDetachListener(new DetachListener() {
                        def detached(ae: DetachEvent ) =  {
                                 println("detachment of " + ae)
                        }
                })
                
                spatial.addErrorListener(new ErrorListener() {
                        def  error(ee: ErrorEvent ) =  {
                                 println("error event for " + ee)
                        }
                })
                
                spatial.addSpatialDataListener(new SpatialDataListener() {
                        def  data(sde: SpatialDataEvent) =  {
                                for(j<-0  until  sde.getData().length)
                                {
                                	    var currTime = sde.getData()(j).getTime()
                                	    
                                        var  out ="Data packet ("+j+") Timestamp: "+  sde.getData()(j).getTime()
                                        if (sde.getData()(j).getAcceleration().length>0)
                                        {
                                                // out = out + "\n Acceleration: "
                                                myValues.scnt += 1
                            
                                                for ( i <- 0 until  sde.getData()(j).getAcceleration().length)  {
                                                       var  haveMore = if (i==sde.getData()(j).getAcceleration().length-1) ":" else ","
                                        
                                                       var currAcc = sde.getData()(j).getAcceleration()(i)
                                       if (myValues.scnt < myValues.maxSamples) {
                                	      myValues.times(myValues.scnt) = currTime
                                	   	 myValues.accs(i, myValues.scnt) = currAcc
                                     
                                	}   	
                                       out = out + currAcc + haveMore


                            myValues.current = myValues.plotGraph.addPoint(myValues.scnt.asInstanceOf[Double], currAcc, "l","g1", false, myValues.prev)
                            myValues.prev = myValues.current
                            myValues.plotCnt +=1 
                            if (myValues.plotCnt == myValues.plotPerSamples) {
                            	myValues.plotCnt = 0
 
                            	myValues.plotGraph.repaint
                                        	
                            	myValues.plotFrame.setVisible(true)
                            }
                        }
                        
                                          }
                                        
                                  //       println(out)
                                
                                }
                        
                        }  // data
                } // SpatialDataListener
                                )

                
                spatial.openAny()
                println("waiting for Spatial attachment...")
                spatial.waitForAttachment()

                var closeDevice = 0
                while (closeDevice == 0) {
                        closeDevice = getInt("Give 1 to close the device", 0)
                        if (closeDevice == 1) {
                       spatial.close()
     			   
                        }                   
                       
                }

                       var t = myValues.times
                        // take the signals
                       var x = myValues.accs(0, ::).getv  
                       var y = myValues.accs(1, ::).getv
                       var z = myValues.accs(2, ::).getv
                       figure(1);  subplot(3,1,1); plot(t,  x.getv); title("x signal"); subplot(3,1,2); plot(t, y.getv); title("y signal"); subplot(3,1,3); plot(t, z.getv); title("z signal")
                       
                       // perform FFT 
     	import com.nr.sp._
   // M : data segments will have length M and are processed in pairs of length 2*M with overlap
   		var bartlett = new BartlettWin
		var  mySpecx =  new Spectolap(myValues.winLen)
     	mySpecx.addlongdata(x.getv, bartlett)
		var psdx = mySpecx.spectrum()
     	var freqx = mySpecx.frequencies()

		var  mySpecy =  new Spectolap(myValues.winLen)
     	mySpecy.addlongdata(y.getv, bartlett)
		var psdy = mySpecy.spectrum()
     	var freqy = mySpecy.frequencies()

		var  mySpecz =  new Spectolap(myValues.winLen)
     	mySpecz.addlongdata(z.getv, bartlett)
		var psdz = mySpecz.spectrum()
     	var freqz = mySpecz.frequencies()

	figure(2); 
 	subplot(3,1,1); plot(freqx, psdx, Color.RED, "Power Spectral Density x")
	subplot(3,1,2); plot(freqy, psdy, Color.GREEN, "Power Spectral Density y")
	subplot(3,1,3); plot(freqz, psdz, Color.BLUE, "Power Spectral Density z")


// use the JTransforms to perform FFT
var NJ = x.length
val dfft = new edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D(NJ )
val cpdata = new Array[Double](NJ)
// copy it
for (k<-0 until NJ)
  cpdata(k) = x(k)
  
  // perform a real FFT
dfft.realForward(x)  
figure(3); plot(x)



// separate and save collected data for further processing
  var   NS = myValues.scnt   // the collected samples
  var   timeStamps = myValues.times(0, NS-1)    // the time stamps
  var   xvalues = myValues.accs(0, ::)(0, NS-1)  // x-values from the accelerometer
  var   yvalues = myValues.accs(1, ::)(1, NS-1)  // y-values from the accelerometer
  var   zvalues = myValues.accs(2, ::)(2, NS-1)  // z-values from the accelerometer

  timeStamps.save("timeStamps.dat")
  xvalues.save("xvalues.dat")
  yvalues.save("yvalues.dat")
  zvalues.save("zvalues.dat")
 

// test that we can load the saved data

var tloaded =   readD1Ascii("timeStamps.dat")
var xloaded = readD1Ascii("xvalues.dat")
var yloaded = readD1Ascii("yvalues.dat")
var zloaded = readD1Ascii("zvalues.dat")

             
```