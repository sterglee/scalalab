# Introduction #

`ScalaLab aims to provide support for logical programming, based on the Java tuProlog implementation of Prolog (http://alice.unibo.it/xwiki/bin/view/Tuprolog/). The tuProlog system is provided as a ScalaLab toolbox, tuProlog.jar, that can be installed as any other toolbox.`

`We plan to provide integrated support for logical programming within ScalaLab code, but for now the ScalaLab user can experiment and learn Prolog by launching the Graphical User Interface window of tuProlog by simply executing the statement (of course after installing the tuProlog.jar toolbox) : `

```
alice.tuprologx.ide.GUILauncher.main(null)
```

# Examples of using the GUI of tuProlog #


## Example 1 ##
`Type in a file, e.g. vowel.pl, the following Prolog code.`

```
  vowel(X):- member(X,[a,e,i,o,u]).
 
  nr_vowel([],0).
  nr_vowel([X|T],N):- vowel(X),nr_vowel(T,N1),N is N1+1,!.
  nr_vowel([X|T],N):- nr_vowel(T,N).
```

`After loading the theory of the file vowel.pl, you can execute as a goal:`

```
nr_vowel([a, e], X).
```


## Example 2 ##
```

my_write([]).
my_write([X|R]) :- write(X), nl, my_write(R).

```

```
?my_write([a,b,c,d]).
```


## Example 3 ##

```
member(X, [X|_]).
member(X, [_|T]) :- member(X, T).
```


```
?member(c, [a,b,c,d]).
?member(x, [m, gh, r]).
```