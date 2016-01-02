# Ordering of imports can be significant #

`In ` _`scalaSci`_  `the trait ` _`scalaSci.StaticScalaSciGlobal`_ `defines some global static operations available in scalaSci, independent of the library that the interpreter uses. `

`Other static operations exist for example in objects` _`RichDoubleArray`_, _`RichDoubleDoubleArray`_, _`Vec`_, _`Matrix`_, _`Sparse`_, _`CCMatrix`_ `which are always imported.`

`The basic problem that this trait solves is to import properly overloaded versions of a method. For example the method`  _`sin()`_ `has some fixed signatures independent of the particular library, which at the time of writing are (of course we can extend the list with other matrix types, for example sparse matrices): `

```
  sin(x: Double)
  sin(x: Array[Double])
  sin(x: Array[Array[Double]])
  sin(x: Matrix)
  sin(x: Vec) 
```


`All the` _`Scala Objects for Static Math Operations (SOSMOs)`_ `should mixin this trait in order to have the common functionality.  Additionally, each SOSMO should implement its  library specific routines.`

`For example the` _`StaticMathsEJML`_ `object,  by mixing the` _`StaticScalaSciGlobal`_ `trait, acquires all the implementations of the ` _`sin()`_ `for the library independent types.`

`Then, it implements the` _`sin()`_ `for the EJML matrix as:`
```
   def sin(x: Mat) = {
      scalaSci.EJML.Mat.sin(x)
 }
```

**` IMPORTANT: `**

`  New import statements can overwrite symbols with the same name acquired from previous  imports. Thefore, usually imports of the SOSMOs should be placed after objects containing  symbols with the same names.`

`For example, if we write:`

```
      import  scalaSci.EJML.StaticMathsEJML._
      import scalaSci.EJML.Mat._
```

` then the statement:`
```
   var  x = sin(3.4)  
```
` fails,   while the statements:`
```
      var g = scalaSci.EJML.StaticMathsEJML.rand0(2,3)  // g is EJML matrix
      sin(g)
```
> `succeed.`

` The problem is that the second import statement (i.e. import` _`scalaSci.EJML.Mat._`_ `) imports the `_`sin()`_ `from the `_`scalaSci.EJML.Mat`_ `class that is defined only  for` _`scalaSci.EJML.Mat`_`, and overwrites the` _`sin()`_ `from the` _`scalaSci.EJML.StaticMathsEJML`_`,that defines many more overloaded versions of` _`sin()`_ `. So, the proper order of the imports is:`
```
    import scalaSci.EJML.Mat._
    import  scalaSci.EJML.StaticMathsEJML._
```

`