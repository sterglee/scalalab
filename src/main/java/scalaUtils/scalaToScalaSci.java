
package scalaUtils;

public class scalaToScalaSci {


      public static String afterDefaultImports(String inString)
      {
          int idx = inString.lastIndexOf("import");
          int len = inString.length();
          if (idx != -1)  {
              while (inString.charAt(idx)!='\n' && idx < len-1) idx++;
              String res = inString.substring(idx, len);
            return res;
           }
          
           return inString;
 
      }

}
