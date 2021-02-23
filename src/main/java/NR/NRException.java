
package NR;


	/**
	 * This exception is thrown when errors in the computation of matrix-related
	 * solutions, their eigenvalues or eigenvectors. The message stored in the
	 * exception indicates where the computation went wrong and should be
	 * instructive to the user hoping to re-complete the calculation
	 */
	@SuppressWarnings("serial")
 public  class NRException extends Exception {
		/**
		 * Constructor for NRException
		 */
	public NRException() {
	  super();
	}

	/**
	* Constructor for NRException with message
	*/
	public NRException(String message) {
	 super(message);
	 }

   
}
