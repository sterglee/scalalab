package scalaSci.MTJ;

public class LAPACKUtils {

    
	/**
	 * <code>max(1, M)</code> provided as a convenience for 'leading dimension' calculations.
	 * 
	 * @param n
	 */
public static int ld(int n) {
  return Math.max(1, n);
    }

}
