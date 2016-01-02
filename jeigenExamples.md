# Introduction #

`We demonstrate examples using ` _`jeigen`_ ` Java wrapper for the ` _`eigen`_ ` high performance C++ matrix library. These examples are currently tested on a ` **`Linux64`** ` platform. `


# Example 1 #

```


import jeigen.Shortcuts._

import jeigen.TicToc._


import jeigen.Timer

var K=1500
var N = 1000
var A = rand(N, K)
var B = rand(K, N)

var timer = new Timer
var C = B mmul A
timer.printTimeCheckMilliseconds    // print time to multiply with the jeigen library


var B2 = spzeros(2, 2)

var A2 = sprand(1000, 900)

println("created A")

B2 = A2.add(0)  // make copy
println("created B")

B2.sortFast()



var Bt = A2.t   // transpose A2



```