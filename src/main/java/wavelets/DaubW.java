package wavelets;

import java.lang.Math.*;

/**
   Daubechies D4 wavelet transform (D4 denotes four coefficients)
 */
public class DaubW {
   protected final double sqrt_3 = Math.sqrt( 3 );
   protected final double denom = 4 * Math.sqrt( 2 );
   //
   // forward transform scaling (smoothing) coefficients
   //
   protected final double h0 = (1 + sqrt_3)/denom;
   protected final double h1 = (3 + sqrt_3)/denom; 
   protected final double h2 = (3 - sqrt_3)/denom; 
   protected final double h3 = (1 - sqrt_3)/denom;
   //
   // forward transform wavelet coefficients
   //
   protected final double g0 =  h3;
   protected final double g1 = -h2;
   protected final double g2 =  h1;
   protected final double g3 = -h0;

   //
   // Inverse transform coefficients for smoothed values
   //
   protected final double Ih0 = h2;
   protected final double Ih1 = g2;  // h1
   protected final double Ih2 = h0;
   protected final double Ih3 = g0;  // h3
   //
   // Inverse transform for wavelet values
   //
   protected final double Ig0 = h3;
   protected final double Ig1 = g3;  // -h0
   protected final double Ig2 = h1;
   protected final double Ig3 = g1;  // -h2

   /**
     <p>
     Forward wavelet transform.
     </p>
     <p>
     Note that at the end of the computation the
     calculation wraps around to the beginning of
     the signal.
     </p>
    */
   protected void transform( double a[], int n )
   {
      if (n >= 4) {
         int i, j;
         int half = n >> 1;
         
	 double tmp[] = new double[n];

	 i = 0;
         for (j = 0; j < n-3; j = j + 2) {
            tmp[i]      = a[j]*h0 + a[j+1]*h1 + a[j+2]*h2 + a[j+3]*h3;
            tmp[i+half] = a[j]*g0 + a[j+1]*g1 + a[j+2]*g2 + a[j+3]*g3;
	    i++;
         }

         tmp[i]      = a[n-2]*h0 + a[n-1]*h1 + a[0]*h2 + a[1]*h3;
         tmp[i+half] = a[n-2]*g0 + a[n-1]*g1 + a[0]*g2 + a[1]*g3;

         for (i = 0; i < n; i++) {
            a[i] = tmp[i];
         }
      }
   } // transform


   protected void invTransform( double a[], int n )
   {
      if (n >= 4) {
	int i, j;
	int half = n >> 1;
	int halfPls1 = half + 1;

	double tmp[] = new double[n];

	//      last smooth val  last coef.  first smooth  first coef
	tmp[0] = a[half-1]*Ih0 + a[n-1]*Ih1 + a[0]*Ih2 + a[half]*Ih3;
	tmp[1] = a[half-1]*Ig0 + a[n-1]*Ig1 + a[0]*Ig2 + a[half]*Ig3;
	j = 2;
	for (i = 0; i < half-1; i++) {
	  //     smooth val     coef. val       smooth val    coef. val
	  tmp[j++] = a[i]*Ih0 + a[i+half]*Ih1 + a[i+1]*Ih2 + a[i+halfPls1]*Ih3;
	  tmp[j++] = a[i]*Ig0 + a[i+half]*Ig1 + a[i+1]*Ig2 + a[i+halfPls1]*Ig3;
	}
	for (i = 0; i < n; i++) {
	  a[i] = tmp[i];
	}
      }
   }


   /**
     Forward Daubechies D4 transform
    */
   public void daubTrans( double s[] )
   {
      final int N = s.length;
      int n;
      for (n = N; n >= 4; n >>= 1) {
         transform( s, n );
      }
   }


   /**
     Inverse Daubechies D4 transform
    */
   public void invDaubTrans( double coef[])
   {
      final int N = coef.length;
      int n;
      for (n = 4; n <= N; n <<= 1) {
         invTransform( coef, n );
      }
   }

} // daub

