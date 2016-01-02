## Support for Matlab's .mat file format ##

`ScalaLab provides some support for loading/saving Matlab .mat files. Specifically scalar values and arrays can be handled. Class` _`scalaSci.math.io.MatIO`_ `implements some Matlab .mat compatibility functionality. Specifically, the routines provided by this class are: `

```

 // writes to the Matlab .mat file the contents of the variable variableNameToSave of type double [] of the scalaSci workspace
 public static  boolean  save(String fileName, double []  varValues, String varName) 

// writes to the Matlab .mat file the contents of the variable variableNameToSave of type double [][] of the scalaSci workspace
 public static  boolean  save(String fileName, double [][]  varValues, String varName) 
 
 
// writes to the Matlab .mat file the contents of the variable varName of type Vec  of the scalaSci workspace
  public static  boolean  save(String fileName, scalaSci.Vec  vecValues, String varName) 
    
 // save a Mat
public static  boolean  save(String fileName, scalaSci.Mat  matValue, String varName) 

// save a Matrix
public static  boolean  save(String fileName, scalaSci.Matrix  matValue, String varName) 

/ save an EJML.Mat
public static  boolean  save(String fileName, scalaSci.EJML.Mat  matValue, String varName) 


// save an MTJ.Mat
public static  boolean  save(String fileName, scalaSci.MTJ.Mat  matValue, String varName) 


// save an CommonMaths.Mat
public static  boolean  save(String fileName, scalaSci.CommonMaths.Mat  matValue, String varName) 

// save a JBLAS .Mat
public static  boolean  save(String fileName, scalaSci.JBLAS.Mat  matValue, String varName) 

// save a RichDoubleDoubleArray 
public static  boolean  save(String fileName, scalaSci.RichDoubleDoubleArray  matValue, String varName) 

// loads the Matlab .mat file contents to scalaSci workspace
public static  int  load(String fileName) 


    
```

`When loading the contents of Matlab .mat files two hashtables are very useful for keeping the names of the Matlab variables and the corresponding data. These variables are: `

```

static public Hashtable<String, Double> scalarValuesFromMatlab = new Hashtable<String, Double>();

static public Hashtable<String, double[][]> arrayValuesFromMatlab = new Hashtable<String, double[][]>();
```
`The ` _`load()`_ `routines use these hashtables in order to permit the recovery of the Matlab variables and their accompanied values.`

`In order to demonstrate the basic relevant routines by examples, we provide one .zip file at the Downloads section that has zipped some Matlab mat files, obtained from the programming exercises accompanying the excellent Simon Haykin's "Neural Networks and Learning Machines" book. Therefore, you can download and run the following examples using the latest ScalaLab210.`

`The following code loads the .mat files `

```


load("gridseq.mat")
load("c.mat")
load("C.mat")
load("nCor_ekf.mat")
load("seq.mat")

```

`Then we can obtain a list of the names of the variables loaded from the .mat files using:`

```
matVars
```

`The above command displays separately the names of the scalars and of the matrices readed from the .mat files. Using these names, and remembering that the values are kept with a hash tables, it is easy for the ` _`getMatArray()`_ `routine to recover the arrays.:`

```

val data_shuffled_ini = getMatArray("data_shuffled_ini")
val seq = getMatArray("seq")
val nCor_ekf = getMatArray("nCor_ekf")
val gridseq = getMatArray("gridseq")
val C = getMatArray("C")

```

`The ` _`whos`_ `Matlab-like command, can be used to display the user defined variables and their types at the ScalaLab workspace, i.e. `

```
whos
```

`Also,  the` _`whosv`_ `command, displays in addition and the values of the variables, i.e. `

```
whosv
```



