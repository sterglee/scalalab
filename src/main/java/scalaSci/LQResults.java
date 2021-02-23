package scalaSci;

import static scalaSci.PrintFormatParams.*;

public class LQResults {

    public double  [][] L;
    public double [][] getL() { return  L; }
    public double [][] Q;
    public double [][] getQ() { return Q; }
    
    
  public final int  length() { return 2*(L.length*L[0].length); }
  public final int  size() { return length(); }
  
  
  @Override
  public  String toString() {
      StringBuilder sb = new StringBuilder();
      String LStr = "L matrix: "+ printArray(L);
      String QStr = "Q matrix: "+ printArray(Q);
      
      sb.append(LStr); sb.append(QStr); 
      return sb.toString();
  }
  
}
