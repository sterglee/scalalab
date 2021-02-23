package scalaSci.MTJ;

enum  JobEigEnum {
    /** The job the eigenvectors solvers are to do */
	/** Compute eigenvalues and eigenvectors */
	All,

	/** Only compute the eigenvalues */
	Eigenvalues;
	
	/**
	 * @return the netlib character version of this designation, for use with F2J.
	 */
	public String netlib() {
		if (this == All)
			return "V";
		return "N";
	}
}
