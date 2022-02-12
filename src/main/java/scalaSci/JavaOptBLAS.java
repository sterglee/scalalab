
package scalaSci;

public class JavaOptBLAS {
    
    public final static double [] method1(double [][]A, double[] x, int m, int n) {
        double [] y = new double [m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                y[i] += A[i][j]*x[j];
            }
        }
        return y;
    }
    
    public final static double [] method2(double [] A, double [] x, int m, int n) {
        double [] y = new double [m];
        int idx;
        for (int i=0; i < m; i++) {
            idx = i*n;
            for (int j=0; j<n; j++) {
                y[i]+= A[idx+j]*x[j];
            }
           }
        return y;
        }
    
    public final static double [] method3( double [] A, double [] x, int m, int n) {
        double [] y = new double [m];
        int idx, ptr;
        final int extra = n - n % 4;
        final int ub = ((n/4)*4)-1;
        for (int i=0; i < m; i++) {
            idx = i*n;
            for (int j=0; j<ub; j+=4) {
                ptr = idx+j;
                y[i] += A[ptr]*x[j];
                y[i] += A[ptr+1]*x[j+1];
                y[i] += A[ptr+2]*x[j+2];
                y[i] += A[ptr+3]*x[j+3];
            }
            for (int j = extra; j < n; j++) {
                y[i] += A[idx+j]*x[j];
            }
        }
        return y;
        }
    
    
    public final static double [] method4(double []A, double [] x, int m, int n) {
        double [] y = new double[m];
        int idx, ptr;
        final int extra = m - m%2;
        final int ub = ((n/2)*2)-1;
        for (int i=0; i < m; i++) {
            idx = i*n;
            for (int j=0; j<ub; j+=2) {
                ptr = idx+j;
                y[i] += A[ptr]*x[j]+A[ptr+1]*x[j+1];
            }
            for (int j = extra; j<n; j++) {
                y[i] += A[idx+j]*x[j];
            }
        }
        return y;
    }
    
    public final static double [] method5(double [] A, double [] x, int m, int n) {
        double [] y = new double [m];
        int idx, ptr;
        final int extra = n - n % 4;
        final int ub = ((n/4)*4) - 1;
        for (int i = 0; i < m; i++) {
            idx = i*n;
            for (int j = 0; j < ub; j += 4) {
                ptr = idx+j;
                y[i] +=  A[ptr]*x[j]
                           +A[ptr+1]*x[j+1]
                            +A[ptr+2]*x[j+2]
                            +A[ptr+3]*x[j+3];
            }
            for (int j = extra; j< n; j++) {
                y[i] += A[idx+j]*x[j];
            }
        }
        return y;
    }
    
    
    public final static double [] method6(double [] A, double [] x, int m, int n) {
        double [] y = new double [m];
        int idx, ptr;
        final int extra = n - n%8;
        final int ub = ((n/8)*8)-1;
        for (int i=0; i <m; i++) {
            idx = i*n;
            for (int j=0; j<ub; j+=8) {
              ptr = idx+j;
              y[i] += A[ptr]*x[j]
                      + A[ptr+1]*x[j+1]
                      + A[ptr+2]*x[j+2]
                      + A[ptr+3]*x[j+3]
                      + A[ptr+4]*x[j+4]
                      + A[ptr+5]*x[j+5]
                      + A[ptr+6]*x[j+6]
                      + A[ptr+7]*x[j+7];
            }
            for (int j = extra; j<n; j++) {
                y[i] += A[idx+j]*x[j];
            }
        }
        return y;
    }
    
    
                
            
            
    public final static double [] method7(double [] A, double [] x, int m, int n) {
        double [] y = new double [m];
        int idx, ptr;
        final int extra = n - n % 8;
        final int ub = ((n/8)*8)-1;
        double acc;
        for (int i = 0; i < m; i++ ) {
            idx = i*n;
            acc = 0;
            for (int j=0; j < ub; j+=8) {
                ptr = idx+j;
                y[i] += A[ptr]*x[j]
                          +A[ptr+1]*x[j+1]
                          +A[ptr+2]*x[j+2]
                          +A[ptr+3]*x[j+3];
                acc += A[ptr+4]*x[j+4]
                          +A[ptr+5]*x[j+5]
                          +A[ptr+6]*x[j+6]
                          +A[ptr+7]*x[j+7];
            }
            y[i] += acc;
            for (int j = extra; j<n; j++) {
                y[i] += A[idx+j]*x[j];
              }
            }
            return y;
        }
        
        
            }
        
    
