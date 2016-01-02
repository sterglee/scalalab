# Introduction #

`LaTex produced formulas are nice and can explain better the plots. ScalaLab291 provides the facility to display mathematical formulas on its figures. `

`The methodology is to have the LaTex formula in a Scala String and then call one of the following overloaded ` **`latexLabel`** `routines.`

```

    def   latexLabel(latex: String): Unit 

// displays at screen coordinates coordx, coordy
    def   latexLabel(latex: String, coordx: Int, coordy: Int): Unit 
        
//  displays at screen coordinates coordx, coordy, size paremeter controls 
//  latex label size
   def  latexLabel(latex: String, size: Int, coordx: Int, coordy: Int): Unit
        
// displays at logical coordinates coordx, coordy

   def latexLabel(latex: String, coordx: Double, coordy: Double): Unit

// displays at logical coordinates coordx, coordy,  size paremeter controls 
//  latex label size
        
   def latexLabel(latex: String, size: Int,  coordx: Double, coordy: Double): Unit


```

`We present an example of displaying a LaTex formula: `

```

// demonstrates plotting with LaTex labels

var t = inc(0, 0.01, 10)
var x = exp(-0.12*t)*log(1+t)
plot(t,x)

// the formula to be displayed 
var lformula = "f(x) = \\exp^{-0.12*t} \\cdot \\log(1+t)"

// the size of the formula
var labelSize = 20
// logical coordinates where LaTex formula will be displayed
var labelx = 0.0 ; var labely = 0.8   

// display the LaTex formula using logical coordinates
latexLabel(lformula, labelSize, labelx, labely)

```

`We can also display a LaTex formula in a 3-D plot window. This is achieved using the ` **`latexLabel3d`**  `that uses either logical coordinates with the prototype: `
```
def  latexLabel3d( latex: String, nw1: Double, nw2: Double, nw3: Double, se1: Double, se2: Double, se3: Double, sw1: Double, sw2: Double, sw3: Double): Unit   
```
`or the simpler call that uses display coordinates to place the LaTex formula:`
```
def latexLabel3d( latex: string, xcoord: Int, ycoord: Int): Unit
```

`For example: `
```

 var t = inc(0, 0.01, 10); var x =cos(9.7*t); var y = sin(4.5*t);
 plot(t,x,y);
 var latex = """cos(9.7*x+sin(4.5*t)"""
 var len = t.length-1
 latexLabel3d(latex, 100, 100)

```