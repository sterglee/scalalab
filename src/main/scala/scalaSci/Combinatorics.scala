
package scalaSci

import scala.math._
import util.Error

/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/**
 * This trait provides several common combinatorics functions.
 */
trait Combinatorics extends Error
{
    /** Sqaure root of Pi
     */
    val SQRT_PI = sqrt (Pi)

    /** Tolerance for real number comparisons
     */
    val EPSILON = 1E-9

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /**
     * Return true if x == y approximately.
     * @param x  the first value to compare
     * @param y  the second value to compare
     */
    def approx (x: Double, y: Double): Boolean =
    {
        abs (x - y) < EPSILON
    } // approx

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /**
     * Compute k! (k factorial).
     * @param k  the argument to the factorial function
     */
    def fac (k: Long): Long =
    {
        var prod: Long = 1
        for (i <- 2.asInstanceOf [Long] to k) prod *= i
        prod
    } // fac

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /**
     * Compute permuations of k items selected from n total items.
     * @param n  the total number of items
     * @param k  the of items selected
     */
    def perm (n: Long, k: Long): Long =
    {
        var prod: Long = 1
        for (i <- n until n-k by -1l) prod *= i
        prod
    } // perm

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /**
     * Compute n choose k (combinations).
     * @param n  the total number of items
     * @param k  the of items to choose
     */
    def choose (n: Long, k: Long): Long =
    {
        if (n == 1) 1 else choose (n-1, k-1) + choose (n-1, k)
    	//perm (n, k) / fac (k)
    } // choose

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /**
     * Compute the gamma function for the following two cases:
     * (1) when k is an integer and (2) when k is an integer + 1/2.
     * @param k  the argument to the gamma function
     */
    def gammaF (k: Double): Double =
    {
        if (k <= 0) flaw ("gammaF", "only handle positive cases")
        var prod = 1.0
        val kInt: Long = (floor (k)).asInstanceOf [Long]
        val frac = k - floor (k)
        if (frac < EPSILON) {
            prod = fac (kInt - 1)
        } else if (approx (frac, 0.5)) {
            for (i <- 2.asInstanceOf [Long] to kInt) prod *= 2 * i - 1
            prod *= SQRT_PI / pow (2, kInt)
        } else {
            flaw ("gammaF", "only handle positive integer and halves cases")
        } // if
        prod
    } // gammaF

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /**
     * Compute the beta function for the following two cases:
     * (1) when k1, k2 are integers and (2) when k1, k2 are integers + 1/2.
     * @param k1  the first argument to the beta function
     * @param k2  the second argument to the beta function
     */
    def betaF (k1: Double, k2: Double): Double =
    {
        gammaF (k1) * gammaF (k2) / gammaF (k1 + k2)
    } // betaF

} // Combinatorics trait

/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/**
 * This objects test the methods in the Combinatorics trait.
 */
/*object CombinatoricsTest extends Application with Combinatorics
{
    println ("\nTest Combinatorics functions")
    println ("approx (5, 5)   = " + approx (5, 5))
    println ("approx (5, 5.1) = " + approx (5, 5.1))
    println ("fac (5)         = " + fac (5))
    println ("fac (10)        = " + fac (10))
    println ("perm (5, 2)     = " + perm (5, 2))
    println ("perm (10, 3)    = " + perm (10, 3))
    println ("gammaF (5)      = " + gammaF (5))
    println ("gammaF (5.5)    = " + gammaF (5.5))
    println ("betaF (5, 6)    = " + betaF (5, 6))
    println ("betaF (5.5, 6)  = " + betaF (5.5, 6))
    println ("perm (22, 10)   = " + perm (22, 10))
    println ("choose (22, 10) = " + choose (22, 10))

    println ("\nBuild Pascal's Triangle using choose (n, k)")
    for (n <- 0 to 10) {
        for (i <- 1 to (10 - n) / 2) print ("\t")
        for (k <- 0 to n) {
            if (n % 2 == 1) print ("    ")
            print (choose (n, k) + "\t")
        } // for
        println ()
    } // for

} // CombinatoricsTest object
*/
