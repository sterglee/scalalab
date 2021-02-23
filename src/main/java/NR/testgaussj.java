package NR;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class testgaussj {
    public static void main(String [] args) {
      
        double [][] A =  {{2, 3, 2.3}, {3.4, 9, 8}, {3.4, -1.2, 0.4}};
        double [][] b = {{2}, {3.2}, {2.3}};
        
        gaussj.gaussj(A, b);
        
        for (int r = 0; r < A.length; r++)
            for (int c=0; c < A[0].length; c++)
                System.out.println("A["+r+","+c+"] = "+A[r][c]);
        
        System.out.println("Solution:");
        for (int r = 0; r < b.length; r++)
            for (int c=0; c < b[0].length; c++)
                System.out.println("b["+r+","+c+"] = "+b[r][c]);
      
    }
}
