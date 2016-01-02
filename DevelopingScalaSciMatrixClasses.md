# `Introduction` #

`ScalaLab is a large system, therefore it is not convenient to develop with all of its source code. Therefore, for developing ` **`ScalaSci`** `code that interfaces with Java Matrix libraries we separated the ` **`ScalaSciMatrixDev.zip`** `download. This download contains the source code of the relevant ScalaSci classes and the required libs to compile them with a Netbeans project. `

`From now on, development of the Matrix ScalaSci classes will be performed using the ` **`ScalaSciMatrixDev`** `codebase. Afterwards, the improvements and additions will be transferred to the GUI ScalaLab. `


**`The major motivation for that separation is to allow other developers with strong Numerical Computing experience to contribute easily in ScalaSci development.`**


`Below we describe how to develop ScalaSci code with the DevelopingScalaSciMatrixClasses codebase. `


# `Developing ScalaSci Matrix code` #

`The required steps to develop ScalaSci Matrix code are: `

  1. `Download the ScalaSciMatrixDev.zip and unzip it, at a new folder. The extracted folder ` **`ScalaSciMatrixDevSrc`** `contains the relevant ScalaSci source code and the folder ` **`lib`** `the required Compile/Run time libraries.`
  1. `Create a Netbeans project, e.g. ` **`ScalaSciMatrixDev`** `using the extracted sources and libs and compile it. This is all, and we can develop further the ScalaSci classes with the Netbeans. `
  1. `We can use the command line scala REPL for scripting the ScalaSci classes. To perform this, copy the file ScalaSciMatrixDev.jar that is obtained after the compilation, at a place directly above the lib folder. Then typing ` **`scala`** `at that folder starts the Scala (Scala 2.9.2 is tested) REPL.  Now, the contents of the file ` **`classpath.txt`** `are useful, since they are the .jars that should be placed at the classpath of the Scala interpreter. Afterwards, we can perform scripting with ScalaSci classes, much like as with ScalaLab. `

