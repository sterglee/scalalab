
package scalaSci

// some NUMAL related utility routines
object NUMAL {
  // convert a zero indexed array to a one-indexed one 
  def  AA1( a: Array[Array[Double]]) =  {
     val  N = a.size
     val M = a(0).size
     var a1 = Array.ofDim[Double](N+1, M+1)
     var r = 0
     while  (r< N) {
       var c = 0
       while  (c< M) {
         a1(r+1)(c+1) = a(r)(c)
         c += 1
       }
      r += 1
     }
    a1
   }
   
    def  A1( a: Array[Double]) =  {
     val  N = a.size
     var a1 = new Array[Double](N+1)
     var k = 0
     while  (k < N) {
         a1(k+1) = a(k)
         k += 1
     }
    a1
   }
 
     
}
