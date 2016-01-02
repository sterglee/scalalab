# ` ScalaLab: Easy and Efficient Matlab-like scientific computing in Scala ` #


**`Since Google disabled creating new downloads, new downloads can be available from: `**


http://sourceforge.net/projects/scalalab/

**`ScalaLab is migrated with Google's automatic exporter to Github: `**


https://github.com/sterglee/scalalab

## `Project Summary` ##

`The ` **`ScalaLab`** `project aims to provide an efficient scientific programming environment for the Java Virtual Machine. The scripting language is based on the` **`Scala`** `programming language enhanced with high level scientific operators and with an integrated environment that provides a MATLAB-like working style. Also, all the huge libraries of Java scientific code can be easily accessible (and many times with a more convenient syntax).  The main potential of the ScalaLab is ` **`numerical code speed`** `and` **`flexibility`**`. The statically typed Scala language can provide speeds of scripting code similar to pure Java. A major design priority of ScalaLab is its` **`user-friendly`** `interface. We like the user to enjoy writing scientific code, and with this objective we design the whole framework. `

`The MATLAB-like mathematical DSL of ScalaLab is termed ` **`ScalaSci`** `, and is developed as an internal DSL, by exploiting the superb extensibility of the Scala language. `

**`Toolboxes`** ` of Java scientific code can be easily installed, using a menu based installation procedure. Also, any .jar packed toolbox, can be directly available by placing it at the ` **`defaultToolboxes`** ` folder. `

`Many environment configuration options can be easily performed within the graphical user interface. Also, ` **`code completion`** ` features and on line help support on the contents of classes, objects, libraries etc., using Java/Scala reflection, can further facilitate the programmer. `

`ScalaLab utilizes also and native C/C++ code for some important numerical operations. Although the speed of pure Java code is generally adequate, optimized native code can provide further additional improvement. Also, the Java Native Interface (JNI) is utilized to interface with NVIDIA's CUDA technology, that provides dramatic speed improvements for many important tasks. `


## `Installation` ##

**` ScalaLab is developed with JDK8, so make sure to have JDK8 installed. `**

`Starting from the February 20 version, ScalaLab should operate well, not only on` **`Linux`** ` and ` **`Windows`**` (for which many native libraries exist), but also for` **` Mac OS X, FreeBSD and Solaris`**` (for these OSes most native libraries are missing but all the Java functionality operates fully). Native libraries obtain in some cases better performance than Java code, but are not essential. Java/Scala code generally performs very well, and many times better than native code! `


`To install and execute ScalaLab, download the` **`ScalaLabAll***.zip`** `, and unzip it. The ` **`ScalaLabAll***.zip`** `download contains both the sources and all the relevant libraries to build ScalaLab with ant. `
`(asterisks mean some date and version specifiers at the name)`

**`Important tip: Be careful the path name at which ScalaLab is placed, to not contain special characters, such as spaces, Greek letters, symbols etc. That can cause failure to load properly.`**


`Then execute the appropriate .bat script for Windows or the corresponding .sh script for UNIX users. The script configures the java.library.path and some JVM parameters.`

`On` **`Raspberry Pi 2  (i.e. the new model) `** `, you can execute ScalaLab as:`

**` java -jar -Xss10m ScalaLab211.jar `**


`To build ScalaLab is very simple: `

`1. Unzip ScalaLabAll***.zip`

`2. Go to .\ScalaLabPr folder and build with ant, i.e.`

_`ant`_

`The executable is built in the dist folder`

**`Important`** `: You should have` **`Scala 2.11`** `installed (and not other version) for ant based build to work properly`






## `ScalaLab Advantages` ##

`The main advantages of ScalaSci that equip it with great potential are: `
  1. `The flexibility and scalability of the wonderful and powerful Scala language, that offer many opportunities to implement convenient high-level scientific operators. `
  1. `The speed of Scala based scripting, that approaches the speed of native and optimized Java code, and thus is close to, or even better from C/C++ based scientific code!`
  1. `The vast Java based scientific libraries with excellent code for many application domains. These libraries are directly available from ScalaSci with the dynamic loading of the corresponding Java toolboxes.`
  1. `The user friendly MATLAB -like environment of ScalaLab and the high quality scientific plotting support. `
  1. `ScalaLab can directly call and access the results of MATLAB scripts.. `


# `Contributing in ScalaSci development` #

`The recent ScalaSci code can be found from the latest ScalaLab sources.  The easier and faster way to develop extensions to the ScalaSci classes is from within ScalaLab itself. In that way, new ScalaSci classes can be created without a cumbersome building of the sources. ` **`ScalaLab Contributors`** `are welcomed: If you develop interesting extensions, please contact me to include them within the ScalaLab sources and have your name in the ScalaLab contributors list.`

## `ScalaLab ScreenShot` ##

`ScalaLab is a simple to use environment that presents a rich Graphical User Interface to the user and a Matlab-like way of working. We present an illustrative snapshot where the FastICA toolbox has been used to separate a signal mixture by performing Independent Component Analysis (ICA).`


![http://scalalab.googlecode.com/files/ScalaLabScreenShot.jpeg](http://scalalab.googlecode.com/files/ScalaLabScreenShot.jpeg)




## `The most recent updates (25 - May - 15 - see Sourceforge downloads) ` ##

**`May 27 - cScalaLabInterface.zip - Demonstrates how to call ScalaLab routines from C code `**

**`May 25 - Version that supports CUDA 7 for Win64 and Linux64`**

**`May 14 - ScalaLab based on Scala 2.12`**

**`May 11 - Minor improvements`**

**`May 3 - CMath native routines compiled with clang++ on Linux, clang++ produces faster code than gc++ !`**

**`Apr 21 - "Pure" ScalaLab without the auxiliary GroovyLab scripting `**

**`Apr 19 - New Apache Common Maths library`**

**`Apr 10 - A prototype socket based interface with the GNU scientific library`**

**`Mar 25 - Asynchronous and remote server based  computations. `**

**`Mar 15 - New Apache Common Maths library `**

**`Mar 08 - Run scripts for 32 bit platforms (i.e. Win32 and Linux32) are corrected  `**

**`Feb 27 - Faster CCMath based native math operations for Linux64. Raspberry Pi 2 support improved, run as: java -jar -Xss10m ScalaLab211.jar`**

**`Feb 22 - CCOps native routine supports FreeBSD also`**

**`Feb 20 - A ScalaLab version that supports and FreeBSD, MacOSX, Solaris and generally platforms for which some native libraries may not function properly`**


**`Feb 14 - More multithreaded operations on matrices`**

**`Feb 11 - Asynchronous execution of the current command (i.e. with F6) using a Future`**

**`Feb 09 - A ScalaLab version that integrates Spark libraries`**

**`Feb 05 - Improvements on code completion with CTRL-SPACE for jsyntaxpane`**

**`Jan 28 - Apache Spark bundled as .jar binaries for ScalaLab toolbox (putting the .jar files into the defaultToolboxes folder works). Also, Breeze as .jar library. However, these libraries can have additional dependencies, and some functionality may not work. `**

**`Jan 25 - Starting to using GSL for speeding some operations on ScalaLab matrices, currently eigenvalue computation based on GSL works for RichDouble2DArray and EJML matrices`**

**`Jan 13 - ScalaXY Streams and optimized for-loop are  bundled as ScalaLab default toolboxes`**



## `Documentation for ScalaLab` ##

`ScalaLab provides a lot of on-line help and demo examples. Also, there exist some material in the Downloads section in .pdf format that document some aspects of ScalaLab. The philosophy of ScalaLab is not to re-implement everything in Scala, but instead to exploit high quality Java scientific software either with Java, or preferably by wrapping the code with Scala classes. Although any Java scientific library can be utilized as toolbox, ScalaLab includes by default some excellent Java scientific libraries. Work is in progress to utilize more effectively these libraries by designing Scala based high level interfaces. These integrated libraries are:`
  1. `The NUMAL library described in the book: ` _A Numerical Library in Java for Scientists & Engineers_, `Hang T. Lau, Chapman & Hall/CRC, 2004 ` **`(In my opinion, it is one of the  best libraries to perform serious numerical work in ScalaLab. )`**
  1. `The Numerical Recipes, 3nd edition book, with the corresponding translation in Java. The book, covers very well many aspects of Scientific Computing, and has routines that can be used from ScalaLab. A strong advantage of the Numerical recipes code, is that the reader can understand the mathematics involving behind the code, by studing the book.`
  1. `The Efficient Matrix Java Library, http://code.google.com/p/efficient-java-matrix-library/`
  1. `The Matrix Toolkit for Java, http://code.google.com/p/matrix-toolkits-java/ `
  1. `The Parallel Colt Library, http://sites.google.com/site/piotrwendykier/software/parallelcolt`
  1. `The JBLAS Library, http://jblas.org/`
  1. `The Apache Common Maths, http://commons.apache.org/math/  One older version of the Apache Common Maths, is integrated within the Java Algebra System, a newer one with packages having math3 naming, is included with ScalaLab210, ` **`the new Apache Common Maths is significantly improved and seems much faster for some operations! `**
  1. `Jsci - A Science API for Java, http://jsci.sourceforge.net/, provides mostly Wavelet Analysis Routines`
  1. `The JFreeChart plotting system, http://www.jfree.org/jfreechart/, provides plots, for some of which already exists Matlab-like interface`

## `Useful Books to use with ScalaLab` ##

`ScalaLab can be utilized and as an educational tool for Numerical Analysis, Computational Intelligence and Engineering courses. Since the ScalaLab editor can execute directly Java code, many Java code chunks can be easily executed. Of course, Scala offers a much more effective programming framework. The following are some excellent scientific books that can be studied using ScalaLab for programming exercises:`
  1. _Numerical Recipes in C++, Second (2002) and Third editions (2007),_ `William H. Press, Saul A. Teukolsky, William T. Vetterling, Brian P. Flannery, Cambridge University Press, 2002, A classic book on numerical analysis and programming. Contains plenty of material on numerical methods that is explained very clearly. In my opinion it is a unique book to anyone practicing scientific programming, and I strongly recommend it. ` **`Numerical Recipes Code is translated in Java by Huang Wen Hui, therefore can be used from ScalaLab, see wiki page for examples. `**
  1. `The NUMAL library described in the book: ` _A Numerical Library in Java for scientists & Engineers_, `Hang T. Lau, Chapman & Hall/CRC, 2004 `
  1. `Numerical Analysis :`  _`Object-Oriented implementation of Numerical Methods: An Introduction with Java and Smalltalk`_, `Didier H. Besset, Morgan Kauffmann, 2000`, `An excellent introductory book on numerical analysis methods, the routines of the book are available within the core of ScalaLab`
  1. `Expert Systems:` _Constructing Intelligent Agents with Java, J. Bigus et.al, John Wiley and Sons, 1997_, `A very good book on artificial intelligence techniques with Java on which the JFES ScalaLab toolbox is based `
  1. `Data Mining:` _Data Mining, Practical Machine Learning Tools and Techniques_, Ian H. Witten, Eibe Frank, Mark A. Hall, Morgan Kauffman, 2011, `An excellent book for developing data mining techniques using WEKA as a vehicle, WEKA is tested and provided as ScalaLab toolbox`
  1. `Neural Networks:` _Programming Neural Networks with ENCOG 2 in Java_,` Jeff Heaton, Heaton Research, 2011, An excellent book for practicing neural network techniques with the ENCOG system, that is tested and provided as ScalaLab toolbox`
  1. `Direct Methods for Sparse Linear Systems: ` _`Timothy A. Davis`_` SIAM publishing 2006, Describes the CSparse algorithms and implementation on which the Sparse ScalaLab class is based`

`Also, some excellent Scala books can be used with ScalaLab, since ScalaLab builds upon the full Scala distribution. The classic book for Scala is the Odersky's book (listed first), but also the other ones are very good books and can complement the reader's skills:`
  1. `Martin Odersky, Lex Spoon, Bill Venners, Programming in Scala, Artima Second Edition, 2010`
  1. `Scala for Machine Learning,   Patrick R. Nicolas, PACKT, 2014, An excellent book for working on Data Mining, Big Data, Artificial Intelligence, with Scala and ScalaLab`
  1. `Learning Concurrent Programming in Scala, Aleksandar Prokopec, PACKT, 2014`
  1. `Alvin Alexander, Scala Cookbook, O'Reily, 2013`
  1. `Dean Wampler & Alex Payne, Programming Scala, 2nd edition,  O'Reily, 2014`
  1. `SCALA for the Impatient, Cay S. Horstmann, Addison-Wesley, 2012`
  1. `Venkat Subramaniam, Programming Scala – Tackle Multicore Complexity on the Java Virtual Machine, Pragmatic Bookself  2009`
  1. `David Pollak, Beginning Scala, APress 2009`
  1. `Christos Loverdos, Apostolos Syropoulos, Steps in Scala, Cambridge University Press, 2010`

## `Attention ` ##

`The default stack and heap size of the JVM are sometimes not adequate. Thus it is better to execute ScalaLab with something like:`

```
java -Xss20m -Xms3200m -Xmx9000m -jar scalalab.jar 
```

`something that the RunScalaLab.bat and RunScalaLab.sh scripts perform.`



## `ScalaLab Toolboxes` ##

`Any Java Library can be used as a ScalaLab Toolbox. However, for some of them work is in progress in order to provide better syntax with the superb facilities of the Scala language. You can find some of these toolboxes at the downloads section.`

## `ScalaLab Development discussion group Mailing List` ##
> http://groups.google.com/group/scalalab-dev-group

## `ScalaLab in the open source community` ##
`ScalaLab is a widely used open-source tool for scientific programming. It has been tested by ` **`SOFTPEDIA`** `and has received many awards, e.g.: `

<a href='http://www.bestvistadownloads.com/'><img src='http://www.bestvistadownloads.com/templates/BVD/images/award_5.gif' alt='Best Vista Download' border='0' /></a>


<a href='http://www.top4download.com/'><img src='http://www.top4download.com/img/award_5.gif' alt='Top 4 Download' border='0' /></a>


<a href='http://www.x64bitdownload.com/'><img src='http://www.x64bitdownload.com/img/award_5.gif' alt='X 64-bit Download' border='0' /></a>


<a href='http://www.downloadtyphoon.com/'><img src='http://www.downloadtyphoon.com/templates/downty/images/award_5.gif' alt='Download Typhoon' border='0' /></a>




## `ScalaLab Developer` ##

```
    Stergios  Papadimitriou 
    Technology Education Institute of East Macedonia and Thraki
    Dept of Computer and Informatics Engineering 
    Greece 
    email: sterg@teikav.edu.gr

```