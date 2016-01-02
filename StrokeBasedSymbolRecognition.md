# Introduction #

`The purpose of this page is to describe an experiment on stroke based symbol recognition.`

`This experiment is at initial phase of development. `


## Getting stroke features ##

`The first step of the symbol recognition system, requires the painting of the symbols with the mouse. This step can be accomplished with the current ScalaLab version (February 08) with the following code: `

```
// ATTENTION!!

// DO NOT EXECUTE ALL THE CODE AT ONCE!

// EXECUTE FIRST THE STATEMENT THAT CREATES THE PAINTING WINDOW
// DRAW YOUR SYMBOLS AND CLOSE THE WINDOW
// AFTER THAT THE VARIABLE Draw.MousePaint.strokes
// MAINTAINS THE STROKES

//  use the MousePaint object to draw symbols with the mouse
// upon closing the drawing window, the strokes are copied at the Draw.MousePaint.strokes
// static variable


var pf = new Draw.MousePaint


// get the actual strokes entered 
var strokes = Draw.MousePaint.strokes  // this statement should be executed after the drawing window is closed!

// plot now the drawing
scatterPlotsOn
plot(strokes)

```