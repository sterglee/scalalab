package scalaSci.math.LinearAlgebra;
        
import static scalaSci.math.LinearAlgebra.LinearAlgebra.*;

import scalaSci.math.array.DoubleArray;

public class LinearAlgebraExample {
	public static void main(String[] args) {
 
		// random 4 x 3 matrix
		double[][] A = random(4, 3);
 
		// random 4 x 3 matrix
		double[][] B = random(4, 3);
 
		// random 4 x 4 matrix + Id
		double[][] C = plus(random(4, 4),identity(4));
		
		// linear algebra
		double[][] D = plus(A, B);
 
		double[][] E = plus(A, 1);
 
		double[][] F = minus(A, B);
 
		double[][] G = minus(2, A);
		
		double[][] H = times(A, transpose(B));
 
		double[][] I = times(A, 10);
 
		double[][] J = divide(C, B);
 
		double[][] K = divide(A, 10);
 
		double[][] L = inverse(C);
 
		// print matrices in command line
		System.out.println("A = \n" + DoubleArray.toString(A));
		System.out.println("B = \n" + DoubleArray.toString(B));
		System.out.println("C = \n" + DoubleArray.toString(C));
		System.out.println("D = A + B = \n" +DoubleArray.toString(D));
		System.out.println("E = A + 1 = \n" +DoubleArray.toString(E));
		System.out.println("F = A - B = \n" + DoubleArray.toString(F));
		System.out.println("G = 2 - A = \n" + DoubleArray.toString(G));
		System.out.println("H = A * t(B) = \n" + DoubleArray.toString(H));
		System.out.println("I = A * 10 = \n" + DoubleArray.toString(I));
		System.out.println("J = C / B = \n" + DoubleArray.toString(J));
		System.out.println("K = A / 10 = \n" + DoubleArray.toString(K));
		System.out.println("L = C^-1 = \n" + DoubleArray.toString(L));
 
	}
}