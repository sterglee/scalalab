# `Transform scripts to Scala Objects in order to run them as standalone applications (27 Feb)` #

`One of the important advantages of ScalaLab is that the scripts can be converted easily to standalone applications, i.e. applications that run on the plain Java platform, without requiring the ScalaLab environment. We illustrate the way to achieve that by means of an example. `

`Suppose that we have the following script for SimpleScalaLab: `

```

import _root_.scalaSci.math.plot.plot._
import _root_.scalaSci.math.plot.plotTypes._
    
import  _root_.scalaSci.StaticScalaSciGlobal
import  _root_.scalaSci.StaticScalaSciGlobal._
 
 var  t = inc(0, 0.01, 10)
 var x = sin(0.23*t)+9.8*cos(1.12*t)
 plot(t,x)
```

`For ScalaLabLight and ScalaLab the imports are a little different: `
```

import _root_.scalaSci.math.plot.plot._
import _root_.scalaSci.math.plot.plotTypes._
    
import  _root_.scalaSci.StaticMaths._
 
 var  t = inc(0, 0.01, 10)
 var x = sin(0.23*t)+9.8*cos(1.12*t)
 plot(t,x)
```

`We want to run that as a standalone application.`

`The ` **`first`**  `step is to choose from the ` **`Compile`** `menu the ` **`Transform a Script to standalone application`**

`We are asked for a name for our standalone application object, suppose that we write ` **`plotSines`** `ScalaLab automatically creates the Scala object that will execute the script in its ` **`main`** `method. `

`The ` **` second `** ` step ` `is to ` **`compile and execute `** `our script, creating the corresponding Scala classes on disk and also a ` **`shell script file`** `named with an extension .sh on Unix like systems and .bat on Windows. That file can be used to execute the application directly from the operating system without ScalaLab. `

`In our particular case the script file for Unix-like systems will be named ` **`plotSines.sh`** `Then we can run our application with: `

```
sh plotSines.sh
```

