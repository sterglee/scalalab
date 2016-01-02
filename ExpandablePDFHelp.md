# Introduction #

`ScalaLab, started from July 28, has a ` **`help`** `folder, onto which PDF help files can be placed. `

`Using an internal PDF Renderer, the command ` **`help(<filename>)`** ` displays the corresponding pdf file . `

`For example to have help on ` **`svd`** ` we can execute the command`

```
help("svd")
```

`This help system is user expandable, i.e. any PDF file that the user places at the ` **`help`** `folder, is viewable with the command ` **`help()`**

`This help system also provides useful help for the Scala and Java languages, using e.g. `

```
help("scala")
help("java")
```