
// benchmark an operation
package scalaSci

object Bench {
  // benchmark a code fragment
  //  @param code: the function to execute
  //  @param times: how many times to repeat it
  def   bench( times: Int, code: ()=>Unit) = {
     System.gc()   // perform a garbage collection to avoid unpredictable delays
    val tmStart = System.currentTimeMillis()   // save the current time
    var k = 0
    while  (k < times) {
      code()
      k += 1
  }
   val tmEnd = System.currentTimeMillis()   // save the end time
    
    (tmEnd-tmStart)/1000.0
  }
  
  @volatile var dummy: Any = _
  def timed[T](body: => T) : Double = {
     val start = System.nanoTime
     dummy = body
     val end = System.nanoTime
     ((end-start)/100)/1000.0
  }
  
}



/*
 
 def myCode() = { 
 var a = ones(20, 20)
 var b = ones(20, 50)
  var c = a*b 
  }
 
 var N = 10
 var tm = bench(N, myCode )
 */