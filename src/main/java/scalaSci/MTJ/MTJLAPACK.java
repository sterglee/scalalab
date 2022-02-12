
package scalaSci.MTJ;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrix;
import org.netlib.blas.BLAS;

public class MTJLAPACK {

     public static DenseMatrix multBLASAdd(double alpha, DenseMatrix B, DenseMatrix C) {
 
        double[] Bd = B.getData(), Cd = C.getData();
        int CRows = C.numRows(); int cCols = C.numColumns();
        int BRows = B.numRows(); int bCols = B.numColumns();
        
        
        BLAS.getInstance().dgemm("N", "N", 
                C.numRows(), C.numColumns(), 
                B.numColumns(), alpha, 
                Bd,
                Math.max(1, B.numRows()), Bd, Math.max(1, B.numRows()), 0.0, Cd,
                Math.max(1, C.numRows()));

        return C;
    }
}
