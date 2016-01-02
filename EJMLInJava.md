# Introduction #

`Since ScalaLab integrates a Java 6 compiler, any Java code can be compiled and executed even with no javac installed. Therefore, ScalaLab can also use the Efficient Java Matrix Library (EJML) routines, directly from Java. This offers maximum flexibility and speed, at the cost of not being so easy to use, as the Matlab-like wrapping of EJML implemented with Scala classes in ScalaLab. Here, we present some examples of using EJML in Java. These examples are taken from Peter Abeles, i.e. http://code.google.com/p/efficient-java-matrix-library/. To execute them, simply paste the code in the ScalaLab's Editor and press F9.`



# EJML Examples in Java #

## Compare block against other transpose for DenseMatrix64F ##
```
import org.ejml.data.BlockMatrix64F;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.RandomMatrices;
import org.ejml.alg.block.*;

import java.util.Random;


/**
 * Compare block against other transpose for DenseMatrix64F
 *
 *  @author Peter Abeles
 */
public class BenchmarkBlockTranspose {

    static Random rand = new Random(234);

    public static long transposeDenseInPlace( DenseMatrix64F mat , int numTrials) {

        long prev = System.currentTimeMillis();

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.transpose(mat);
        }
        long curr = System.currentTimeMillis();

        return curr-prev;
    }

    public static long transposeDense( DenseMatrix64F mat , int numTrials) {


        DenseMatrix64F tran = new DenseMatrix64F(mat.numCols,mat.numRows);

        long prev = System.currentTimeMillis();

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.transpose(mat,tran);
        }
        long curr = System.currentTimeMillis();

        return curr-prev;
    }

    public static long transposeBlock( DenseMatrix64F mat , int numTrials) {

        BlockMatrix64F A = new BlockMatrix64F(mat.numRows,mat.numCols);
        BlockMatrix64F A_t = new BlockMatrix64F(mat.numCols,mat.numRows);

        BlockMatrixOps.convert(mat,A);

        long prev = System.currentTimeMillis();

        for( int i = 0; i < numTrials; i++ ) {
            BlockMatrixOps.transpose(A,A_t);
        }
        long curr = System.currentTimeMillis();

        return curr-prev;
    }

    public static void main( String args[] ) {

        DenseMatrix64F A = RandomMatrices.createRandom(5000,5000,rand);

        int N = 5;

        System.out.println("In place  : "+transposeDenseInPlace(A,N));
        System.out.println("Standard  : "+transposeDense(A,N));
        System.out.println("Block     : "+transposeBlock(A,N));
    }
}

```

## Benchmark equality ##

```
import org.ejml.data.DenseMatrix64F;

import java.util.Random;
import org.ejml.ops.*;

/**
 * @author Peter Abeles
 */
public class BenchmarkEquality {

    public static long equals( DenseMatrix64F matA ,
                               DenseMatrix64F matB ,
                               int numTrials) {
        boolean args = false;
        long prev = System.currentTimeMillis();

        for( int i = 0; i < numTrials; i++ ) {
            args = MatrixFeatures.isEquals(matA,matB,1e-8);
        }

        long curr = System.currentTimeMillis();
        if( !args )
            throw new RuntimeException("don't optimize me away!");
        return curr-prev;
    }

    public static long identical( DenseMatrix64F matA ,
                                  DenseMatrix64F matB ,
                                  int numTrials) {

        boolean args = false;
        long prev = System.currentTimeMillis();

        for( int i = 0; i < numTrials; i++ ) {
            args = MatrixFeatures.isIdentical(matA,matB,1e-8);
        }

        long curr = System.currentTimeMillis();
        if( !args )
            throw new RuntimeException("don't optimize me away!");
        return curr-prev;
    }

    public static void main( String args[] ) {
        Random rand = new Random(234234);

        DenseMatrix64F A = RandomMatrices.createRandom(1000,2000,rand);
        DenseMatrix64F B = A.copy();

        int N = 1000;

        System.out.println("Equals:    "+equals(A,B,N));
        System.out.println("Identical: "+identical(A,B,N));
    }

}

```


## Benchmark multiplication and addition operations ##

```
import org.ejml.alg.dense.mult.MatrixMatrixMult;
import org.ejml.data.DenseMatrix64F;

import java.util.Random;

import org.ejml.ops.*;



/**
 * @author Peter Abeles
 */
public class BenchmarkMultAndAddOps {

    static Random rand = new Random(234234);

    static int TRIALS_MULT = 4000000;
    static int TRIALS_ADD = 100000000;

    public static long mult( DenseMatrix64F matA , DenseMatrix64F matB , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numRows,matB.numCols);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.mult(matA,matB,results);
//            MatrixMatrixMult.mult_small(matA,matB,results);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long mult_alpha( DenseMatrix64F matA , DenseMatrix64F matB , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numRows,matB.numCols);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.mult(2.0,matA,matB,results);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long mult_alt( DenseMatrix64F matA , DenseMatrix64F matB , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numRows,matB.numCols);

        for( int i = 0; i < numTrials; i++ ) {
            MatrixMatrixMult.mult_reorder(matA,matB,results);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long multTranA( DenseMatrix64F matA , DenseMatrix64F matB , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numCols,matB.numCols);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.multTransA(matA,matB,results);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long multTranA_alpha( DenseMatrix64F matA , DenseMatrix64F matB , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numCols,matB.numCols);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.multTransA(2.0,matA,matB,results);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long multTranB( DenseMatrix64F matA , DenseMatrix64F matB , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numRows,matB.numRows);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.multTransB(matA,matB,results);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long multTranAB( DenseMatrix64F matA , DenseMatrix64F matB , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numCols,matB.numRows);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.multTransAB(matA,matB,results);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long multAdd( DenseMatrix64F matA , DenseMatrix64F matB , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numRows,matB.numCols);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.multAdd(matA,matB,results);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long multAddTranA( DenseMatrix64F matA , DenseMatrix64F matB , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numCols,matB.numCols);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.multAddTransA(matA,matB,results);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long multAddTranB( DenseMatrix64F matA , DenseMatrix64F matB , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numRows,matB.numRows);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.multAddTransB(matA,matB,results);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long multAddTranAB( DenseMatrix64F matA , DenseMatrix64F matB , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numCols,matB.numRows);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.multAddTransAB(matA,matB,results);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long addEquals( DenseMatrix64F matA , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numRows,matA.numCols);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.addEquals(results,matA);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long add( DenseMatrix64F matA , DenseMatrix64F matB , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numRows,matA.numCols);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.add(matA,matB,results);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long add_a_b( DenseMatrix64F matA , DenseMatrix64F matB , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numRows,matA.numCols);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.add(1.5,matA,3.4,matB,results);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long addEqualBeta( DenseMatrix64F matA , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numRows,matA.numCols);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.addEquals(results,2.5,matA);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long minusEquals( DenseMatrix64F matA , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numRows,matA.numCols);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.subEquals(results,matA);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long minus( DenseMatrix64F matA , DenseMatrix64F matB , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F results = new DenseMatrix64F(matA.numRows,matA.numCols);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.sub(matA,matB,results);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static void performMultTests( DenseMatrix64F matA , DenseMatrix64F matB ,
                                         DenseMatrix64F matC , DenseMatrix64F matD ,
                                         int numTrials )
    {
        System.out.printf("Mult:                  = %10d\n",
                mult(matA,matB,numTrials));
        System.out.printf("Mult Alpha:            = %10d\n",
                mult_alpha(matA,matB,numTrials));
        System.out.printf("Tran A Mult:           = %10d\n",
                multTranA(matD,matB,numTrials));
        System.out.printf("Tran A Mult Alpha:     = %10d\n",
                multTranA_alpha(matD,matB,numTrials));
        System.out.printf("Tran B Mult:           = %10d\n",
                multTranB(matA,matC,numTrials));
        System.out.printf("Tran AB Mult:          = %10d\n",
                multTranAB(matD,matC,numTrials));
        System.out.printf("Mult Add:              = %10d\n",
                multAdd(matA,matB,numTrials));
        System.out.printf("Tran A Mult Add:       = %10d\n",
                multAddTranA(matD,matB,numTrials));
        System.out.printf("Tran B Mult Add:       = %10d\n",
                multAddTranB(matA,matC,numTrials));
        System.out.printf("Tran AB Mult Add:      = %10d\n",
                multAddTranAB(matD,matC,numTrials));
    }

    public static void performAddTests( DenseMatrix64F matA , DenseMatrix64F matB ,
                                         DenseMatrix64F matC , DenseMatrix64F matD ,
                                         int numTrials )
    {
        System.out.printf("Add Equal:             = %10d\n",
                addEquals(matA,numTrials));
        System.out.printf("Add Equals b:          = %10d\n",
                addEqualBeta(matA,numTrials));
        System.out.printf("Add:                   = %10d\n",
                add(matA,matC,numTrials));
        System.out.printf("Add a b:               = %10d\n",
                add(matA,matC,numTrials));
        System.out.printf("Minus Equal:           = %10d\n",
                minusEquals(matA,numTrials));
        System.out.printf("Minus:                 = %10d\n",
                minus(matA,matC,numTrials));
    }

    public static void main( String args[] ) {
        System.out.println("Small Matrix Results:") ;
        int N = 2;
        DenseMatrix64F matA = RandomMatrices.createRandom(N,N,rand);
        DenseMatrix64F matB = RandomMatrices.createRandom(N,N,rand);
        DenseMatrix64F matC,matD;

        performMultTests(matA,matB,matB,matA,TRIALS_MULT*10);
        performAddTests(matA,matB,matB,matA,TRIALS_ADD);


        System.out.println();
        System.out.println("Large Matrix Results:") ;
        matA = RandomMatrices.createRandom(1000,1000,rand);
        matB = RandomMatrices.createRandom(1000,1000,rand);

        performMultTests(matA,matB,matB,matA,1);
        performAddTests(matA,matB,matB,matA,500);

        System.out.println();
        System.out.println("Large Not Square Matrix Results:") ;
        matA = RandomMatrices.createRandom(600,1000,rand);
        matB = RandomMatrices.createRandom(1000,600,rand);
        matC = RandomMatrices.createRandom(600,1000,rand);
        matD = RandomMatrices.createRandom(1000,600,rand);

        performMultTests(matA,matB,matC,matD,1);
        performAddTests(matA,matB,matC,matD,1000);
    }
}

```

## Benchmark Various Operations ##

```

import org.ejml.data.DenseMatrix64F;

import java.util.Random;

import org.ejml.ops.*;
/**
 * @author Peter Abeles
 */
public class BenchmarkVariousOps {

    static Random rand = new Random(0xffff);

    static int TRIALS_TRANSPOSE = 20000000;
    static int TRIALS_SCALE = 30000000;
    static int TRIALS_NORM = 10000000;
    static int TRIALS_DETERMINANT = 20000000;

    public static long transposeEml( DenseMatrix64F mat , int numTrials) {
        long prev = System.currentTimeMillis();

        DenseMatrix64F tran = new DenseMatrix64F(mat.numCols,mat.numRows);

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.transpose(mat,tran);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

//    public static long transposeMtj( DenseMatrix64F orig , int numTrials) {
//        DenseMatrix mat = UtilMatrixToolkitsJava.convertToMtj(orig);
//
//        long prev = System.currentTimeMillis();
//
//        DenseMatrix tran = new DenseMatrix(mat.numColumns(),mat.numRows());
//
//        for( int i = 0; i < numTrials; i++ ) {
//            mat.transpose(tran);
//        }
//
//        long curr = System.currentTimeMillis();
//        return curr-prev;
//    }

    public static long scale( DenseMatrix64F mat , int numTrials) {
        long prev = System.currentTimeMillis();

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.scale(10,mat);
            CommonOps.scale(0.1,mat);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long scale2( DenseMatrix64F mat , int numTrials) {
        DenseMatrix64F result = mat.copy();

        long prev = System.currentTimeMillis();

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.scale(10,mat,result);
            CommonOps.scale(0.1,mat,result);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

//    public static long scaleMtj( DenseMatrix64F orig , int numTrials) {
//        DenseMatrix mat = UtilMatrixToolkitsJava.convertToMtj(orig);
//
//        long prev = System.currentTimeMillis();
//
//        for( int i = 0; i < numTrials; i++ ) {
//            mat.scale(10);
//            mat.scale(0.1);
//        }
//
//        long curr = System.currentTimeMillis();
//        return curr-prev;
//    }

    public static long normEml( DenseMatrix64F mat , int numTrials) {
        long prev = System.currentTimeMillis();

        for( int i = 0; i < numTrials; i++ ) {
            NormOps.normF(mat);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

    public static long determinant( DenseMatrix64F mat , int numTrials) {
        long prev = System.currentTimeMillis();

        for( int i = 0; i < numTrials; i++ ) {
            CommonOps.det(mat);
        }

        long curr = System.currentTimeMillis();
        return curr-prev;
    }

//    public static long normMtj( DenseMatrix64F orig , int numTrials) {
//        DenseMatrix mat = UtilMatrixToolkitsJava.convertToMtj(orig);
//
//        long prev = System.currentTimeMillis();
//
//        for( int i = 0; i < numTrials; i++ ) {
//            mat.norm(Matrix.Norm.Frobenius);
//        }
//
//        long curr = System.currentTimeMillis();
//        return curr-prev;
//    }

    public static void main( String args[] ) {
        System.out.println("Small Matrix Results:") ;
        DenseMatrix64F mat = RandomMatrices.createRandom(4,4,rand);

//        System.out.printf("Transpose:         eml = %10d\n",
//                transposeEml(mat,TRIALS_TRANSPOSE));
        System.out.printf("Scale:             eml = %10d\n",
                scale(mat,TRIALS_SCALE));
        System.out.printf("Scale2:            eml = %10d\n",
                scale2(mat,TRIALS_SCALE));
//        System.out.printf("Norm:              eml = %10d\n",
//                normEml(mat,TRIALS_NORM));
//        System.out.printf("Determinant:       eml = %10d\n",
//                determinant(mat,TRIALS_DETERMINANT));

        System.out.println();
        System.out.println("Large Matrix Results:") ;
        mat = RandomMatrices.createRandom(2000,2000,rand);
        System.out.printf("Transpose:         eml = %10d\n",
                transposeEml(mat,100));
//        System.out.printf("Scale:             eml = %10d\n",
//                scaleEml(mat,100));
//        System.out.printf("Norm:              eml = %10d\n",
//                normEml(mat,100));
//        System.out.printf("Determinant:       eml = %10d\n",
//                determinant(mat,1));
    }
}

```