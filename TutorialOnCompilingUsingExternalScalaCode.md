# Introduction #

`Since ScalaLab has access to the Scala compiler binary, it can be used to compile any files containing Scala code with `**`.scala`** `extension as the` _`scalac`_ `compiler does. In this tutorial we will show by means of an example how to compile and access code defined as a normal Scala source file. The description applies to the ` **`ScalaLab29`** `version.`


# Example of compiling and using external Scala code #

`Suppose that we have code placed within a Scala object that implements a mathematical function, for simplicity we consider the simple ` _`cube`_ `function implemented with the ` _`CubeObj`_ `Scala object, which is in the ` _`simpleMaths.scala`_ `file. Let the code in `_`simpleMaths.scala`_ `be the following:`

```
object CubeObj {
  def cube(x: Double) = x*x*x
}

```

`Let place ` _`simpleMaths.scala`_ `within a folder, say for example, ` _`/home/sp/testScalaLab`_ `in my Linux workstation. `

`The first step is to ` _`compile`_ `the Scala source. One way to achieve this, is to type the path ` _`/home/sp/testScalaLab`_ `in the text edit control with label "Specify Path", then pressing "Browse" displays the contents of the folder. By selecting the file ` _`simpleMaths.scala`_ `,right-clicking the mouse, and selecting "Scala -> Compile .scala file" we can compile the code directly. ScalaLab uses the integrated Scala compiler and does not require Scala installation.`

`The second step, is to append the directory ` _`/home/sp/testScalaLab`_ `to the ` **`classpath`** `of the Scala interpreter. One way to achieve this is to select the path, right-mouse-click and then select from the popup menu "Paths->Append the path to the scalaSci Paths". Subsequently, we need ` **`a new Scala Interpreter`** `that includes that path on its` **`classpath`**`. By right-mouse clicking over the` _`ScalaLabConsole`_ `area, we can select "Reset Scala Interpreter using Scalalab default impors" option. A new Scala Interpreter is created and as can be verified from the contents of the classpath that are displayed, it includes the new path.`

`Finally, we can utilize the functionality of the new compiled code from the Interpreter, i.e. we can compute the cube of number and apply our cube function to the elements of a ` _`Vec`_ `object: `

```
  // compute the cube of a number
val anum = 5.6
val anumCube = CubeObj.cube(anum)
println("cube of "+anum+" = "+anumCube)

  // apply the function to the elements of a vector
val t = linspace(0, 5, 2000)
val x = t map CubeObj.cube 
plot(t,x)
```

`We should note, that by exiting ScalaLab, the paths configuration is saved and is available at the next session. We can remove paths, using the ` **`JTree`** `control that displays user defined class paths, and by pressing the ` **`DEL`** `key over a corresponding tree node. The node is deleted from the ScalaSci classpaths, but we should create a new Scala interpreter before exiting, because it is ` **`the classpath of the running Scala interpreter`** ` that is saved upon exiting. `

