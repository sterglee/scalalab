# Introduction #

**`SciLab`** ` is a powerful open source scientific programming environment. The recent versions of SciLab have efficient built-in operations, and a rich number of libraries and toolboxes. `

`We demonstrate how the ScalaLab user can execute scripts using SciLab.`

`It is important to note that in order to avoid introducing many annotations, the same ` **`@MATLAB`** ` annotation can be used for both a MATLAB and a SciLab connection. The following example illustrates how the ` **`@MATLAB`** ` annotation is used. `


## `Installing SciLab support for Windows. ` ##

`The first step is to download and install SciLab for Windows. `

`Suppose it is installed on folder: ` _`d:\\SciLab`_ ` for example (as its is in my installation ).`

`The first step is to update the ` **`PATH`** ` environment variable, from the Control Panel, appending to it the directories:` **`d:\\SciLab\\bin`** ` and ` **`d:\\SciLab`** `(of course replacing d:\\SciLab with the directories of your own installation).`

`The second (and final!) step is to update the ScalaLab startup script by appending the same directories to the java.library.path, for example for my own system:`

`java -server  -d64  -XX:+UseNUMA -XX:+UseParallelGC -XX:+UseCompressedOops -XX:+AggressiveOpts -Djava.library.path=d://SciLab//bin;d://SciLab;./lib;./libMedia64;.;./libCUDA -Xss5m -Xms3000m -Xmx23000m -jar ScalaLab211.jar`


`After these two simple steps, ScalaLab can utilize and cooperate with the SciLab engine!!`

## Examples ##

## `SVD with SciLab` ##

`Note that the ` **`@MATLAB`** ` annotation is used for both MATLAB and SciLab connection. `

```

// perform SVD using a SciLab - ScalaLab connection 

import org.scilab.modules.types.ScilabDouble;

initSciLabConnection()    // init a connection wth SciLab


var x = rand(500,500)  // create a random vector
var s = "tic(); [ux, wx, vx] = svd(x); tmsci = toc();"    // string to evaluate in SciLab

tic
@MATLAB
scieval( s, Array("x"), Array("ux", "wx", "vx", "tmsci"))  // Array("x") is the list of input parameters from ScalaLab to SciLab
@MATLAB                                                  // Array("ux", "wx", "vx") is the list of result parameters from SciLab to ScalaLab
var tmsciExtern = toc

var uxd = ux.asInstanceOf[ScilabDouble].getRealPart()  
var wxd = wx.asInstanceOf[ScilabDouble].getRealPart()
var vxd = vx.asInstanceOf[ScilabDouble].getRealPart()

var sbz = uxd*wxd*(vxd~)-x    // should be zero
max(max(sbz))   

// test now time for Java SVD
tic
var svdx = svd(x)
var tmj = toc



```