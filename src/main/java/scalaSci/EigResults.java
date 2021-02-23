package scalaSci;

import static scalaSci.PrintFormatParams.*;
        
// for accumulating eigenanalysis results in JLAPACK related routines

public class EigResults {
  public double [] realEvs;
  public  double [] imEvs;
  public  double [][] realEvecs;
  public double [][] imEvecs;
  
  public final int  length() { return realEvs.length+imEvs.length+2*(realEvecs.length*imEvecs[0].length); }
  public final int  size() { return length(); }
  
public EigResults() {
    
}
  // allocate space for n eigenvalues/eigenvectors
  public EigResults(int n) {
      realEvs = new double[n];
      imEvs = new double[n];
      realEvecs = new double[n][n];
      imEvecs = new double[n][n];
  }
  
  @Override
  public  String toString() {
      StringBuilder sb = new StringBuilder();
      String realEvsStr = "real eigenvalues: "+ printArray(realEvs);
      String imEvsStr = "imaginary eigenvalues: "+ printArray(realEvs);
      String realEvecsStr = "real eigenvectors: "+ printArray(realEvecs);
      String imEvecsStr = "right eigenvectors: "+printArray(imEvecs);
      
      sb.append(realEvsStr); sb.append(imEvsStr); sb.append(realEvecsStr); sb.append(imEvecsStr);
      return sb.toString();
  }
  
}
