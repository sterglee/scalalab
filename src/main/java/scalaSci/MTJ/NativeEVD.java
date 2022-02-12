
package scalaSci.MTJ;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.NotConvergedException;
import org.jblas.NativeBlas;
import org.netlib.util.intW;

public class NativeEVD {

/**
 * Computes eigenvalue decompositions of general matrices
 */
    /**
     * Double work array
     */
    private final double[] work;

    /**
     * Size of the matrix
     */
    private final int n;

    /**
     * Job to do on the left and right eigenvectors
     */
    private final JobEigEnum  jobLeft, jobRight;

    /**
     * Contains the real and imaginary parts of the eigenvalues
     */
    private final double[] Wr, Wi;

    /**
     * Contains the left and the right eigenvectors
     */
    private final DenseMatrix Vl, Vr;

    /**
     * Creates an empty eigenvalue decomposition which will compute all the
     * eigenvalues and eigenvectors (left and right)
     * 
     * @param n
     *            Size of the matrix
     */
    public NativeEVD(int n) {
        this(n, true, true);
    }

    /**
     * Creates an empty eigenvalue decomposition
     * 
     * @param n
     *            Size of the matrix
     * @param left
     *            Whether to compute the left eigenvectors or not
     * @param right
     *            Whether to compute the right eigenvectors or not
     */
    public NativeEVD(int n, boolean left, boolean right) {
        this.n = n;
        this.jobLeft = left ? JobEigEnum.All : JobEigEnum.Eigenvalues;
        this.jobRight = right ? JobEigEnum.All : JobEigEnum.Eigenvalues;

        // Allocate space for the decomposition
        Wr = new double[n];
        Wi = new double[n];

        if (left)
            Vl = new DenseMatrix(n, n);
        else
            Vl = null;

        if (right)
            Vr = new DenseMatrix(n, n);
        else
            Vr = null;

        // Find the needed workspace
        double[] worksize = new double[1];
        intW info = new intW(0);
        //NativeBlas.dgeev(jobLeft.netlib(), jobRight.netlib(), n, new double[0],
          //      LAPACKUtils.ld(n), new double[0], new double[0], new double[0], 
            //    LAPACKUtils.ld(n),  new double[0], LAPACKUtils.ld(n), worksize, -1, info);

        // Allocate workspace
        int lwork = 0;
        if (info.val != 0) {
            if (jobLeft == JobEigEnum.All || jobRight == JobEigEnum.All)
                lwork = 4 * n;
            else
                lwork = 3 * n;
        } else
            lwork = (int) worksize[0];

        lwork = Math.max(1, lwork);
        work = new double[lwork];
    }

    /**
     * Convenience method for computing the complete eigenvalue decomposition of
     * the given matrix
     * 
     * @param A
     *            Matrix to factorize. Not modified
     * @return Newly allocated decomposition
     * @throws NotConvergedException
     */
    public static NativeEVD factorize(DenseMatrix A) throws NotConvergedException {
        return new NativeEVD(A.numRows()). factor(new DenseMatrix(A));
    }

  
    /**
     * Computes the eigenvalue decomposition of the given matrix
     * 
     * @param A
     *            Matrix to factorize. Overwritten on return
     * @return The current decomposition
     * @throws NotConvergedException
     */
    public NativeEVD factor(DenseMatrix A) throws NotConvergedException {
        if (!A.isSquare())
            throw new IllegalArgumentException("!A.isSquare()");
        else if (A.numRows() != n)
            throw new IllegalArgumentException("A.numRows() != n");

        intW info = new intW(0);
        //NativeBlas.dgeev(jobLeft.netlib(), jobRight.netlib(), n, A.getData(),
          //      LAPACKUtils.ld(n), Wr, Wi, jobLeft == JobEigEnum.All ? Vl.getData() : new double[0],
            //    LAPACKUtils.ld(n), jobRight == JobEigEnum.All ? Vr.getData() : new double[0], LAPACKUtils.ld(n),
              //  work, work.length, info);

        if (info.val > 0)
            throw new NotConvergedException(
                    NotConvergedException.Reason.Iterations);
        else if (info.val < 0)
            throw new IllegalArgumentException();

        return this;
    }

    /**
     * Gets the left eigenvectors, if available
     */
    public DenseMatrix getLeftEigenvectors() {
        return Vl;
    }

    /**
     * Gets the right eigenvectors, if available
     */
    public DenseMatrix getRightEigenvectors() {
        return Vr;
    }

    /**
     * Gets the real part of the eigenvalues
     */
    public double[] getRealEigenvalues() {
        return Wr;
    }

    /**
     * Gets the imaginary part of the eigenvalues
     */
    public double[] getImaginaryEigenvalues() {
        return Wi;
    }

    /**
     * True if the left eigenvectors have been computed
     */
    public boolean hasLeftEigenvectors() {
        return Vl != null;
    }

    /**
     * True if the right eigenvectors have been computed
     */
    public boolean hasRightEigenvectors() {
        return Vr != null;
    }

}


