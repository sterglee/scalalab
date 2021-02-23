# scalalab

## Easy and Efficient Matlab-like scientific computing in Scala ##


## Project Summary

`The ScalaLab project aims to provide an efficient scientific programming environment for the Java Virtual Machine. The scripting language is based on the Scala programming language enhanced with high level scientific operators and with an integrated environment that provides a MATLAB-like working style. Also, all the huge libraries of Java scientific code can be easily accessible (and many times with a more convenient syntax). The main potential of the ScalaLab is numerical code speed and flexibility. The statically typed Scala language can provide speeds of scripting code similar to pure Java. A major design priority of ScalaLab is its user-friendly interface. We like the user to enjoy writing scientific code, and with this objective we design the whole framework.`

`The MATLAB-like mathematical DSL of ScalaLab is termed ScalaSci , and is developed as an internal DSL, by exploiting the superb extensibility of the Scala language.`

`Toolboxes of Java scientific code can be easily installed, using a menu based installation procedure. Also, any .jar packed toolbox, can be directly available by placing it at the defaultToolboxes folder.`

`Many environment configuration options can be easily performed within the graphical user interface. Also,  code completion features and on line help support on the contents of classes, objects, libraries etc., using Java/Scala reflection, can further facilitate the programmer.`

`ScalaLab utilizes also and native C/C++ code for some important numerical operations. Although the speed  of pure Java code is generally adequate, optimized native code can provide further additional improvement. Also, the Java Native Interface (JNI) is utilized to interface with NVIDIA's CUDA technology, that provides dramatic speed improvements for many important tasks.`

`However, a pure JVM version of ScalaLab is also provided with the project:`

https://github.com/sterglee/PureJVMScalaLab

`This version is lighter, and more portable, since it is based only on JVM components.`


`Useful toolboxes for ScalaLab are provided with the project: `

https://github.com/sterglee/ScalaLabPrebuiltToolboxes

`ScalaLab supports Java 8 and Java 9. However, Java 9 although is faster, has some minor problems with the jsyntaxPane completion`

## Documentation

The recently published book:

### Scientific Computing with ScalaLab at the Java Platform
### Scholars' Press (2017-01-11 )

https://www.morebooks.de/store/gb/book/scientific-computing-with-scalalab-at-the-java-platform/isbn/978-3-659-84599-4

describes ScalaLab in detail.
Also,  the Amazon link:

https://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Dstripbooks&field-keywords=scalalab

and the Amazon smile link:

https://smile.amazon.com/Scientific-Computing-ScalaLab-Java-Platform/dp/365984599X/ref=sr_1_1?s=books&ie=UTF8&qid=1486391462&sr=1-1




## Installation

`ScalaLab is developed with JDK8, so make sure to have JDK8 installed.`

`To install and execute ScalaLab, download the .zip , and unzip it.  `

`The .zip download contains both the sources and all the relevant libraries to build  ScalaLab with sbt.`

`Then execute the appropriate .bat script for Windows or the corresponding .sh script for UNIX users. The script configures the java.library.path and some JVM parameters.`



`To build ScalaLab with SBT:`


`1. Unzip the .zip file`

`2. Type` 

`sbt`

`then at the SBT prompt, type `

`clean`

`and then`

`package`

`In order to compile and package ScalaLab with sbt, you should set the variable SBT_OPTS="-Xmx1536M  -Xss13M" `

`The UNIX script runSbt.sh accomplishes that, but for Windows you should set the variable using the Control Panel.`


`Also, in order to run the produced .jar executable from the sbt build,
using the providing scripts, you should rename it to ScalaLab.jar.`

## `ScalaLab most useful commands`


`Press F6 executes the selected text or the current line (within a seperate thread)`

`Press Ctrl-F6 cancels a pending task started with F6`

`press F7 to use code completion features of the Scala Interpreter.`

`CTRL-SPACE Code Completion`

`Press TAB expands the abbreviation, e.g. bo<TAB> expands to Boolean`

`Mouse Double Click on an identifier: displays the current value of the identifier`

`Mouse cursor over an identifier displays information for that identifier`


`Select a keyword (e.g. "fft") and press F1 for obtaining global help for the libraries where that identifier can be defined using Java reflection (e.g. we can have multiple “ffts”, thus multiple results)`

`Press F2 executes code up to the cursor location. The first F2 has start position the beginning of text, subsequently the start position is updated to the end position of the previous F2`

`Press F11: Expands Abbreviations`

`Press F4: within an identifier (either selected or not) presents a completion list for the type of the identifier using Java reflection`

`Press SHIFT-F4: within an identifier (either selected or not) presents information for the class of the identifier using Java reflection (e.g. for the javax.swing.JFrame class) with a JTree`

`In order to build ScalaLab with sbt, you should set the variable SBT_OPTS="-Xmx1536M  -Xss13M”. The UNIX script runSbt.sh accomplishes this, but for Windows you should set 
the variable using Control Panel.`

## ScalaLab Advantages

`The main advantages of ScalaSci that equip it with great potential are:`

`The flexibility and scalability of the wonderful and powerful Scala language, that offer many opportunities to implement convenient high-level scientific operators.`

`The speed of Scala based scripting, that approaches the speed of native and optimized Java code, and thus is close to, or even better from C/C++ based scientific code!`

`The vast Java based scientific libraries with excellent code for many application domains.  These libraries are directly available from ScalaSci with the dynamic loading of the corresponding Java toolboxes.`

`The user friendly MATLAB -like environment of ScalaLab and the high quality scientific plotting support. ScalaLab can directly call and access the results of MATLAB scripts.`

## Contributing in ScalaSci development

`The recent ScalaSci code can be found from the latest ScalaLab sources. The easier and faster way to develop extensions to the ScalaSci classes is from within ScalaLab itself. In that way, new ScalaSci classes can be created without a cumbersome building of the sources. ScalaLab Contributors are welcomed: If you develop interesting extensions, please contact me to include them within the ScalaLab sources and have your name in the ScalaLab contributors list`
 
