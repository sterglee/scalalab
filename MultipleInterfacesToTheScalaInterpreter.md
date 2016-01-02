# Introduction #

`This page describes how we can effectively work with multiple complementary interfaces to the ScalaInterpreter in ScalaLab29. We should note that the interface in ScalaLab29 has been improved and therefore some features are not applicable to the previous ScalaLab281 version`.


# `Multiple complementary interfaces to the Scala Interpreter in ScalaLab29` #

`ScalaLab29 has three interfaces to work with the Scala Interpreter`:

  1. `The `**`ScalaInterpreterPane`**` interface`
  1. `The `**`ScalaLabConsole`** `interface`
  1. `The `**`ScalaCodeCells`** `interface`

`It is important that the` **`ScalaInterpreterPane`** `and` **`ScalaLabConsole`** `use` _`the same`_ `Scala Interpreter, while the `**`ScalaCodeCells`** `creates a different one. Therefore, the workspace of variables for the later is distinct.`

`The ScalaInterpreterPane interface is the most convenient one and therefore is the default. However, the ScalaLabConsole interface is convenient when we have to execute small one line scripts. Even more important, the popup menu of the ScalaLabConsole has many useful operations, as to reset the Scala Interpreter or to create a new Scala Interpreter that uses different numerical libraries, e.g. the EJML (Efficient Java Matrix Library).`

`Let present some examples of working with these interfaces. Type the following piece of code within ScalaInterpreter pane: `
```
var  a = ones0(20, 30)
var  b = ones0(30, 50)
var  c = a * b
```

`We can execute this code either by selecting it and pressing F6, or line-by-line by pressing F6 at each line, or with F2 that executes the text from the previously executed position up to the current cursor position. `

`Now we can go to the ScalaLabConsole window, and we can type the name of the matrix type` _`Mat`_ `variable `_`a`_`. We observe that the contents of the matrix are printed therefore the `**`ScalaLabConsole`** `shares the `**`same Scala Interpreter`** `with the `**`ScalaInterpreterPane`**`. This design decision allows to exploit effectively both interfaces. `

`Now let us define a new computation as: `
```
var sc = size(c); var rc = rand0(sc(0), sc(1))
```

`The line above takes the size of matrix c, and then creates a new random matrix of the same size. We can execute this line at the ScalaLabConsole at this time. `

`Now we can return to the ScalaInterpreterPane, and we can type: `
```
plot(rc(1))
```

`Pressing F6 while we are at the same line, or by selecting the text and pressing F6 we can readily obtain a plot of the second line of the random matrix ` _`rc`_

`The ` **`ScalaCodeCells`** `interface has its own Scala Interpreter and therefore operates in a different non-interfering workspace. We can open this interface from ` **`"Scala Code Cells"`** `top-level menu option. The ScalaCodeCells interface operates differently, it consists of code cells, within which we can write Scala code and we can execute them with Shift-Enter.`

`Now we can type in a Scala Code Cell the expression: `
```
var cc = 100+c
```
`in an attempt to add 100 to each element of the matrix ` _`c`_

`This expression fails to execute, since the ScalaCodeCells environment has its own Scala Interpreter, and therefore cannot see the variable ` _`c`_ `that is defined with the Scala Interpreter shared with ScalaInterpreterPane and ScalaLabConsole interfaces.`

`However, we can redefine the variable ` _`c`_ `as for example: `

```

val c = rand0(5,6)  // a 5X6 random zero-indexed matrix
val cc = 4-0.5*sin(6.7*c+7.8)+cos(c-9+cos(0.34*c)) // a rather complex synthetic expression
```

`The expression above is evaluated since all its variables are autonomously defined.`