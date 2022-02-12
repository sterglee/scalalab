package scalaExec.gui;

import java.util.*;

import java.util.regex.*;



public class AutoCompletionWorkspace
{
    static public int numCompletionItems = 100;
    static public Vector <String>  scanVars = new Vector(numCompletionItems);
    static String []   listOfAllVars;
    
    
    public AutoCompletionWorkspace()
    {
        Collections.sort(AutoCompletionWorkspace.scanVars, new Comparator()  {
             public int compare(Object v1, Object v2) {
                 return ((String)v1).compareToIgnoreCase(((String)v2));
             }
        });
        
        
        // construct a view of the sorted AutoCompletionScala Vector as an array of Strings
        int countVars = AutoCompletionWorkspace.scanVars.size();
        listOfAllVars  = new String[countVars];
        for (int k=0; k<countVars; k++)
            listOfAllVars[k] = (String) AutoCompletionWorkspace.scanVars.elementAt(k);
      }
        
    
     
    /**
     * Return those functions starting with the prefix.

     * @param prefix Prefix of the function name.
     * @return An array of Strings with autocompletion information
     * returns null
     */
    public String[] getMatched(String prefix)
    {
          if (prefix.equals(""))  // all methods match to a null string
        {
            return listOfAllVars;
        }
        int i = firstIndexOfMatchedString(prefix);
        int j = lastIndexOfMatchedString(prefix, i);
        String[] matches = new String[j-i];

        for (int k = 0; k < matches.length; k++)        {    // collect all matches
            matches[k] = listOfAllVars[i+k];
         }
        return matches;
    }


    public String[] getMatchedRegEx(String prefix) 
     {
          if (prefix.equals(""))  {
              return listOfAllVars;
          }
          Pattern commandPattern = Pattern.compile(prefix);
          Vector vmatches = new Vector();
          int cntMatches = 0;  // count of matches
          for (int k=0; k<listOfAllVars.length; k++) {
              String currentDescription = listOfAllVars[k];
              if (currentDescription != null) {
               Matcher exprMatcher = commandPattern.matcher(currentDescription);
               if (exprMatcher.matches()) {  // one more match
                  vmatches.add(currentDescription);
                  cntMatches++;
               }
              }
          }
     String [] matches;
          if (cntMatches > 0) {    
            matches = new String [cntMatches];
          for (int k=0; k<cntMatches;k++)
              matches[k] =  vmatches.elementAt(k).toString();
          }
          else {
              matches = new String[1];
              matches[0] = "";
          }
          
          return matches;
   }   



    private int firstIndexOfMatchedString(String prefix)
    {
        int up = 1;
        int low = 0;
        int ce;  // currently examined element
        int prefLen = prefix.length();
        int currentVarCntScalaSci = AutoCompletionWorkspace.scanVars.size();
        do
        {
            low += up/2;
            up = 1;
            ce = up+low-1;
            while (ce < currentVarCntScalaSci)
            {
                String currentDescription = listOfAllVars[ce];
                int cLen = currentDescription.length();
                
                int k = (cLen < prefLen) ? cLen : prefLen;
                int m = currentDescription.substring (0, k).compareToIgnoreCase (prefix);
                if (cLen >= prefLen && m >= 0)
                {
                    low  += up/2;
                    if (up == 1) { break; }
                    up = 1;
                }
                else
                {
                    up *= 2;
                }
                ce = up+low-1;
            
            }
        }
        while (up != 1);

        return low;
    }



    private int lastIndexOfMatchedString(String prefix, int startingPoint)
    {
     
        int up = 1;
        int low = startingPoint;
        int ce;  // curerntly examined element
        int prefLen = prefix.length();
        int currentVarCnti = AutoCompletionWorkspace.scanVars.size();
        do
        {
            low += up/2;
            up = 1;
            ce = up+low-1;
            while (ce < currentVarCnti)
            {
                String currentDescription = listOfAllVars[ce];
                int cLen = currentDescription.length();
                
                int k = (cLen < prefLen) ? cLen : prefLen;
                int m = currentDescription.substring (0, k).compareToIgnoreCase (prefix);
                if (cLen >= prefLen && m > 0)
                {
                    low  += up/2;
                    if (up == 1) { break; }
                    up = 1;
                }
                else
                {
                    up *= 2;
                }
                ce = up+low-1;
            
            }
        }
        while (up != 1);

        return low;
    }

}


