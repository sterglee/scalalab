package scalaSci;

public class MatrixConv {

    
        // convert row major double [] array  to Matrix
    public static double [][]  rowMajor(double [] a, int nr, int nc)     {
        double [][]  rm = new double [nr][nc];
      int elem=0;
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++) {
              rm[r][c] = a[elem];
              elem++;
          }
      
      return rm;
  
  }

      //// convert double [][] to JBLAS DoubleMatrix 
    public static double []   rowMajor1DFrom2D(double [][] dm)     {
      int nr = dm.length;
      int nc = dm[0].length;
      double [] dd = new double [nr*nc];
      int cnt=0;
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++) 
              dd[cnt++] =  dm[r][c];
      
      return dd;
    }
      
      

        // convert column major double [] array  to Matrix
    public static double [][]columnMajor(double [] a, int nr, int nc)     {
      double [][] rm = new double [nr][nc];
      int elem=0;
      for (int c=0; c<nc; c++)
          for (int r=0; r<nr; r++) {
              rm[r][c] = a[elem];
              elem++;
          }
      
      return rm;
  
  }
    
    // JBLAS conversion routines
    // convert JBLAS DoubleMatrix to Matrix
    public static double [][] JBLAS2Matrix(org.jblas.DoubleMatrix dm)     {
      int nr = dm.rows;
      int nc = dm.columns;
      double [][] rm = new double [nr][nc];
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++)
              rm[r][c] = dm.get(r, c);
      
      return rm;
  
  }
  
    // convert Matrix to JBLAS DoubleMatrix
    public static org.jblas.DoubleMatrix  Matrix2JBLAS(double [][]   dA) {
        org.jblas.DoubleMatrix   dm = new org.jblas.DoubleMatrix(dA);  // construct a JBLAS DoubleMatrix
        return dm;
    }

  

    // convert JBLAS DoubleMatrix to Double Array
    public static double[][] JBLAS2DoubleArray(org.jblas.DoubleMatrix dm)     {
      int nr = dm.rows;
      int nc = dm.columns;
      double [][] rm = new double[nr][nc];
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++)
              rm[r][c] = dm.get(r, c);
      
      return rm;
  
  }
    
        
    
    // EJML conversion routines
    // convert EJML DenseMatrix64F to Matrix
    public static double [][] EJML2Matrix(org.ejml.data.DenseMatrix64F  dm)     {
      int nr = dm.numRows;
      int nc = dm.numCols;
      double [][]  rm = new double [nr][nc];
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++)
              rm[r][c] = dm.get(r, c);
      
      return rm;
  
  }
  
  // convert EJML  DenseMatrix64F to Double Array
    public static double[][] EJML2DoubleArray(org.ejml.data.DenseMatrix64F  dm)     {
      int nr = dm.numRows;
      int nc = dm.numCols;
      double [][] rm = new double[nr][nc];
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++)
              rm[r][c] = dm.get(r, c);
      
      return rm;
  
  }
    
    // convert double [][] to EJML DenseMatrix64F
    public static org.ejml.data.DenseMatrix64F   Matrix2EJML(double [][]  dm)     {
      int nr = dm.length;
      int nc = dm[0].length;
      org.ejml.data.DenseMatrix64F  rm = new org.ejml.data.DenseMatrix64F(nr, nc);
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++)
              rm.set(r, c, dm[r][c]);
      
      return rm;
  
  }
  
  //// convert double [][] to JBLAS DoubleMatrix 
    public static org.ejml.data.DenseMatrix64F  DoubleArray2EJML(double [][] dm)     {
      int nr = dm.length;
      int nc = dm[0].length;
      org.ejml.data.DenseMatrix64F  rm = new org.ejml.data.DenseMatrix64F(nr, nc);
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++)
              rm.set(r, c, dm[r][c]);
      
      return rm;
  
  }
    

    
    // MTJ conversion routines
    // convert MTJ DenseMatrix to double [][]
    public static double [][] MTJ2Matrix(no.uib.cipr.matrix.DenseMatrix   dm)     {
      int nr = dm.numRows();
      int nc = dm.numColumns();
      double [][]  rm = new  double [nr][nc];
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++)
              rm[r][c] = dm.get(r, c);
      
      return rm;
  
  }
  
  // convert MTJ DenseMatrix to Double Array
    public static double[][] MTJ2DoubleArray(no.uib.cipr.matrix.DenseMatrix  dm)     {
      int nr = dm.numRows();
      int nc = dm.numColumns();
      double [][] rm = new double[nr][nc];
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++)
              rm[r][c] = dm.get(r, c);
      
      return rm;
  
  }
    
    // convert double [][]  to MTJ DenseMatrix
    public static no.uib.cipr.matrix.DenseMatrix   Matrix2MTJ(double [][]  dm)     {
      int nr = dm.length;
      int nc = dm[0].length;
      no.uib.cipr.matrix.DenseMatrix  rm = new no.uib.cipr.matrix.DenseMatrix(nr, nc);
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++)
              rm.set(r, c, dm[r][c]);
      
      return rm;
  
  }
  
  //// convert double [][] to MTJ DenseMatrix
    public static no.uib.cipr.matrix.DenseMatrix  DoubleArray2MTJ(double [][] dm)     {
      int nr = dm.length;
      int nc = dm[0].length;
      no.uib.cipr.matrix.DenseMatrix  rm = new no.uib.cipr.matrix.DenseMatrix(nr, nc);
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++)
              rm.set(r, c, dm[r][c]);
      
      return rm;
  
  }
    
    
    
    
    // Apache Commons conversion routines
    // convert Apache Commons to double [][]
    public static double [][] AC2Matrix(org.apache.commons.math3.linear.Array2DRowRealMatrix  dm)     {
      int nr = dm.getRowDimension();
      int nc = dm.getColumnDimension();
      double [][]  rm = new double [nr][nc];
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++)
              rm[r][c] = dm.getEntry(r, c);
      
      return rm;
  
  }
  
  // convert Apache Commons to Double Array
    public static double[][] AC2DoubleArray(org.apache.commons.math3.linear.Array2DRowRealMatrix  dm)     {
      int nr = dm.getRowDimension();
      int nc = dm.getColumnDimension();
      double [][] rm = new double[nr][nc];
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++)
              rm[r][c] = dm.getEntry(r, c);
      
      return rm;
  
  }
    
    // convert double [][]  to Apache Commons
    public static org.apache.commons.math3.linear.Array2DRowRealMatrix   Matrix2AC(double [][] dm)     {
      int nr = dm.length;
      int nc = dm[0].length;
      org.apache.commons.math3.linear.Array2DRowRealMatrix  rm = new org.apache.commons.math3.linear.Array2DRowRealMatrix(nr, nc);
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++)
              rm.setEntry(r, c, dm[r][c]);
      
      return rm;
  
  }
  
  //// convert double [][] to Apache Commons
    public static org.apache.commons.math3.linear.Array2DRowRealMatrix  DoubleArray2AC(double [][] dm)     {
      int nr = dm.length;
      int nc = dm[0].length;
      org.apache.commons.math3.linear.Array2DRowRealMatrix  rm = new org.apache.commons.math3.linear.Array2DRowRealMatrix(nr, nc);
      for (int r=0; r<nr; r++)
          for (int c=0; c<nc; c++)
              rm.setEntry(r, c, dm[r][c]);
      
      return rm;
  
  }
    
    
    
}
