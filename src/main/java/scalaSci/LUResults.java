package scalaSci;

import static scalaSci.PrintFormatParams.*;

public class LUResults {

    public double  [][] L;
    public double [][] U;
    public int [] Pi;  // pivot indexes
    public double [][] getL() { return L; }
    public double [][] getU() { return U; }
    public int [] getP() { return Pi; }
    
      
  public final int  length() { return 2*(L.length*L[0].length); }
  public final int  size() { return length(); }
  
  
  @Override
  public  String toString() {
      StringBuilder sb = new StringBuilder();
      String LStr = "L matrix: "+ printArray(L);
      String UStr = "U matrix: "+ printArray(U);
      
      sb.append(LStr); sb.append(UStr); 
      return sb.toString();
  }
  
}
