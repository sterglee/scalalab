# Introduction #

`ScalaLab always integrates the most recent stable Scala distribution and also provides a user friendly means of writing Scala code. Therefore, it is an effective tool for learning and developing Scala programming skills. Here, we present some tutorial examples that can help in practicing Scala programming techniques.`


# Case Classes #

`We work with the examples presented in "`**`Programming in Scala`**`", book of Martin Odersky et. al. The book describes these examples thoroughly, here we trace some aspects of the code.`

`Let define some case classes, for a library that manipulates arithmetic expressions:`
```
abstract class Expr

case class Var(name: String)  extends  Expr

case class Number(num: Double) extends Expr

case class UnOp(operator: String, arg: Expr)  extends Expr

case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

```

`The Scala Interpreter defines the following classes:`
```
defined class Expr
defined class Var
defined class UnOp
defined class BinOp
```

`With the case modifier the Scala compiler adds some syntactic conveniences to your class.`

`First, it adds a factory method with the name of the class.`
```
val v = Var("x")
val op = BinOp("+", Number(1), v)
```

`The second syntactic convenience is that all arguments in the parameter list of a case class implicitly get a` _`val`_ `prefix, so they are maintained as fields: `

```
v.name
op.left
```

> `Third, the compiler adds "natural" implementations of methods` _`toString`_, _`hashCode`_, `and ` _`equals`_ `to your class.`

```
println(op)
op.right == Var("x")
```

`Finally, the compiler adds a ` _`copy`_ `method to your class for making modified copies. This method is useful for making a new instance of the class that is the same as another except that one or two attributes are different. As an example, here is how you can make an operation just like op except that the operator has changed:`

```
op.copy(operator="-")
```

`However, the biggest advantage of case classes is that they support pattern matching.`

## Pattern matching ##

`Suppose you want to simplify the arithmetic expressions presented above. There is a multitude of possible simplification rules. The following three rules just serve as an illustration: `
```
def simplifyTop(expr: Expr): Expr = expr match {
   case UnOp("-", UnOp("-", e)) => e  // Double negation
   case BinOp("+", e, Number(0)) => e   // Adding zero
   case BinOp("*", e, Number(1)) => e // Multiplying by one
   case _ => expr
}

```

`We can use now the` _`simplifyTop`_ function to simplify expressions, e.g. `

```
simplifyTop(UnOp("-", UnOp("-", Var("x"))))
```


`A pattern match includes a sequence of ` _`alternatives`_,  `each starting with the keyword case. Each alternative includes a ` _`pattern`_ `and one or more expressions, which will be evaluated if the pattern matches. An arrow symbol => separates the pattern from the expressions.`

`A `_`match`_ `expression is evaluated by trying each of the patterns in the order they are written. The first pattern that matches is selected, and the part following the arrow is selected and executed.`

`A ` _`constant pattern`_ `like "+" or 1 matches values that are equal to the constant with respect to ==. A `  _`variable pattern`_ `like e matches every value. The variable then refers to that value in the right hand side of the case clause. In this example, note that the first three examples evaluate to e, a variable that is bound within the associated pattern. The ` _`wildcard pattern (_)`_ `also matches every value, but it does not introduce a variable name to refer to that value.`
