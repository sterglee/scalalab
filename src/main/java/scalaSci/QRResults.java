package scalaSci;

import static scalaSci.PrintFormatParams.*;

public class QRResults {

    public double  [][] Q;
    public double [][] R;
    
    
  public final int  length() { return 2*(Q.length*Q[0].length); }
  public final int  size() { return length(); }

  
  @Override
  public  String toString() {
      StringBuilder sb = new StringBuilder();
      String QStr = "Q matrix: "+ printArray(Q);
      String RStr = "R matrix: "+ printArray(R);
      
      sb.append(QStr); sb.append(RStr); 
      return sb.toString();
  }

}
