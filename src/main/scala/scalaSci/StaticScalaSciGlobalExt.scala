package scalaSci


import org.ejml.data.DenseMatrix64F
import org.ejml.ops.CommonOps
/*
  Defines some additional global operations available in ScalaSci, independent of the library that the interpreter uses.
 This object provides additional functionality by defining useful routines. 
 These routines use a RichDouble2DArray as a Matrix type,
 in order to be library independent
  */

object StaticScalaSciGlobalExt  {

/**
     * Creates a randomly generated set of orthonormal vectors.  At most it can generate the same
     * number of vectors as the dimension of the vectors.
     * This is done by creating random vectors then ensuring that they are orthogonal
     * to all the ones previously created with reflectors.
     * NOTE: This employs a brute force O(N<sup>3</sup>) algorithm.
     * @param dimen dimension of the space which the vectors will span.
     * @param numVectors How many vectors it should generate.
     * @param rand Used to create random vectors.
     * @return Array of N random orthogonal vectors of unit length.
     */
     def createSpan( dimen: Int, numVectors: Int) = 
     {
     	val randg = new java.util.Random()
   //   use the EJML library to create DenseMatrix64Fs  	
     	var dmSpan = org.ejml.ops.RandomMatrices.createSpan( dimen, numVectors, randg)
    // copy to RichDouble2DArray 	
     	
     	 
     	 var  rdda = new Array[RichDouble2DArray](numVectors)
     	 var k=0
     	 while  (k <  numVectors) {
     		rdda(k) = new RichDouble2DArray(dimen, 1)
     		var dm64F = dmSpan(k)
     		var row = 0
     		while (row < dm64F.numRows) {
     			 rdda(k)(row, 0) = dm64F.get(row,0)
     			 row += 1
     		}
     	k += 1
     	 }
     	 
 rdda
 
}  



}