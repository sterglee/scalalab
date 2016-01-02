# Introduction #

`ScalaLab can generate random numbers using sometimes elaborate routines of the many Java scientific libraries that are installed by default. However, the more important routines are available at the standard setup, without the need to perform any import statement. The page introduces these basic routines by means of examples.`


# Random number, vector, matrix generation #

`We can generate a single uniformly distributed random number simply as:`
```
var x = rand
```

`Generating a uniformly distributed random vector (at the interval (0, 1)) is also very easy`
```
var v = vrand(2000)
plot(v)
```

`Similarly, we can generate normally (Gaussian) distributed vectors, obtained from a Gaussian distribution of mean 0 and standard deviation 1`
```
var vn = vrandn(2000)
plot(vn)
```

`The routines rand(n: Int, m: Int), randn(n: Int, m: Int) generate the corresponding random matrices, e.g. `

```
var m0 = rand(10, 20) // uniformly distributed in (0, 1)
var m0n = randn(10, 20) // normally distributed, mean 0.0, std 1.0
```

`For more complex distributions we can use routines implemented in Java from the ` _`scalaSci.math.array.StatisticSample`_ `class. These routines return double [] or double [][] arrays, but is easy to convert them to the ScalaSci type we want. For example: `

```
 
var M=2000
var rn = randomNormal(M, 10, 4.8)
plot(rn)

```

`For the convenience of the user we list the commented implementation of these static routines below: `

```

    //random create methods

	/**
	 * Create an m x n matrix of uniformly distributed random numbers between
	 * two bounds.
	 * @param m Number of rows in matrix
	 * @param n Number of columns in matrix
	 * @param i0 Lowest value any element can be.
	 * @param i1 Largest value any element can be.
	 */
    protected static int[][] randomInt(int m, int n, int i0, int i1) {
        int[][] A = new int[m][n];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = Random.randInt(i0, i1);
        return A;
    }

	/**
	 * Create an array of uniformly distributed random numbers between
	 * two bounds.
	 * @param m Number of elements in the array
	 * @param i0 Lowest value any element can be.
	 * @param i1 Largest value any element can be.
	 */
    protected static int[] randomInt(int m, int i0, int i1) {
        int[] A = new int[m];
        for (int i = 0; i < A.length; i++)
            A[i] = Random.randInt(i0, i1);
        return A;
    }

	/**
	 * Create an m x n matrix of uniformly distributed random numbers between
	 * two bounds.
	 * @param m Number of rows in matrix
	 * @param n Number of columns in matrix
	 * @param min Lowest value any element can be.
	 * @param max Largest value any element can be.
	 */
    public static double[][] randomUniform(int m, int n, double min, double max) {
        double[][] A = new double[m][n];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = Random.uniform(min, max);
        return A;
    }

	/**
	 * Create an array of uniformly distributed random numbers between
	 * two bounds.
	 * @param m Number of elements in the array
	 * @param min Lowest value any element can be.
	 * @param max Largest value any element can be.
	 */
    public static double[] randomUniform(int m, double min, double max) {
        double[] A = new double[m];
        for (int i = 0; i < A.length; i++)
            A[i] = Random.uniform(min, max);
        return A;
    }

    public static double[][] randomDirac(int m, int n, double[] values, double[] prob) {
        double[][] A = new double[m][n];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = Random.dirac(values, prob);
        return A;
    }

    public static double[] randomDirac(int m, double[] values, double[] prob) {
        double[] A = new double[m];
        for (int i = 0; i < A.length; i++)
            A[i] = Random.dirac(values, prob);
        return A;
    }

	/**
	 * Create an m x n matrix of normally (Gaussian) distributed random numbers.
	 * @param m Number of rows in matrix
	 * @param n Number of columns in matrix
	 * @param mu Mean value of probability distribution function.
	 * @param sigma Standard deviation of probability distribution function.
	 */
    public static double[][] randomNormal(int m, int n, double mu, double sigma) {
        double[][] A = new double[m][n];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = Random.normal(mu, sigma);
        return A;
    }

	/**
	 * Create an array of normally (Gaussian) distributed random numbers.
	 * @param m Number of elements in the array
	 * @param mu Mean value of probability distribution function.
	 * @param sigma Standard deviation of probability distribution function.
	 */
    public static double[] randomNormal(int m, double mu, double sigma) {
        double[] A = new double[m];
        for (int i = 0; i < A.length; i++)
            A[i] = Random.normal(mu, sigma);
        return A;
    }

    public static double[][] randomChi2(int m, int n, int d) {
        double[][] A = new double[m][n];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = Random.chi2(d);
        return A;
    }

    public static double[] randomChi2(int m, int d) {
        double[] A = new double[m];
        for (int i = 0; i < A.length; i++)
            A[i] = Random.chi2(d);
        return A;
    }

    public static double[][] randomLogNormal(int m, int n, double mu, double sigma) {
        double[][] A = new double[m][n];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = Random.logNormal(mu, sigma);
        return A;
    }

    public static double[] randomLogNormal(int m, double mu, double sigma) {
        double[] A = new double[m];
        for (int i = 0; i < A.length; i++)
            A[i] = Random.logNormal(mu, sigma);
        return A;
    }

    public static double[][] randomExponential(int m, int n, double lambda) {
        double[][] A = new double[m][n];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = Random.exponential(lambda);
        return A;
    }

    public static double[] randomExponential(int m, double lambda) {
        double[] A = new double[m];
        for (int i = 0; i < A.length; i++)
            A[i] = Random.exponential(lambda);
        return A;
    }

    public static double[][] randomTriangular(int m, int n, double min, double max) {
        double[][] A = new double[m][n];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = Random.triangular(min, max);
        return A;
    }

    public static double[] randomTriangular(int m, double min, double max) {
        double[] A = new double[m];
        for (int i = 0; i < A.length; i++)
            A[i] = Random.triangular(min, max);
        return A;
    }

    public static double[][] randomTriangular(int m, int n, double min, double med, double max) {
        double[][] A = new double[m][n];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = Random.triangular(min, med, max);
        return A;
    }

    public static double[] randomTriangular(int m, double min, double med, double max) {
        double[] A = new double[m];
        for (int i = 0; i < A.length; i++)
            A[i] = Random.triangular(min, med, max);
        return A;
    }

    public static double[][] randomBeta(int m, int n, double a, double b) {
        double[][] A = new double[m][n];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = Random.beta(a, b);
        return A;
    }

    public static double[] randomBeta(int m, double a, double b) {
        double[] A = new double[m];
        for (int i = 0; i < A.length; i++)
            A[i] = Random.beta(a, b);
        return A;
    }

    public static double[][] randomCauchy(int m, int n, double mu, double sigma) {
        double[][] A = new double[m][n];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = Random.cauchy(mu, sigma);
        return A;
    }

    public static double[] randomCauchy(int m, double mu, double sigma) {
        double[] A = new double[m];
        for (int i = 0; i < A.length; i++)
            A[i] = Random.cauchy(mu, sigma);
        return A;
    }

    public static double[][] randomWeibull(int m, int n, double lambda, double c) {
        double[][] A = new double[m][n];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = Random.weibull(lambda, c);
        return A;
    }

    public static double[] randomWeibull(int m, double lambda, double c) {
        double[] A = new double[m];
        for (int i = 0; i < A.length; i++)
            A[i] = Random.weibull(lambda, c);
        return A;
    }

    public static double[][] randomRejection(int m, int n, Function fun, double maxFun, double min, double max) {
        double[][] A = new double[m][n];
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[i].length; j++)
                A[i][j] = Random.rejection(fun, maxFun, min, max);
        return A;
    }

    public static double[] randomRejection(int m, Function fun, double maxFun, double min, double max) {
        double[] A = new double[m];
        for (int i = 0; i < A.length; i++)
            A[i] = Random.rejection(fun, maxFun, min, max);
        return A;
    }

    // Statistics sample methods

    public static double mean(double[] v) {
        double mean = 0;
        int m = v.length;
        for (int i = 0; i < m; i++)
            mean += v[i];
        mean /= (double) m;
        return mean;
    }

    public static double[] mean(double[][] v) {
        int m = v.length;
        int n = v[0].length;
        double[] mean = new double[n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                mean[j] += v[i][j];
        for (int j = 0; j < n; j++)
            mean[j] /= (double) m;
        return mean;
    }

    public static double stddeviation(double[] v) {
        return Math.sqrt(variance(v));
    }

    public static double variance(double[] v) {
        double var;
        int degrees = (v.length - 1);
        int m = v.length;
        double c;
        double s;
        c = 0;
        s = 0;
        for (int k = 0; k < m; k++)
            s += v[k];
        s = s / m;
        for (int k = 0; k < m; k++)
            c += (v[k] - s) * (v[k] - s);
        var = c / degrees;
        return var;
    }

    public static double[] stddeviation(double[][] v) {
        double[] var = variance(v);
        for (int i = 0; i < var.length; i++)
            var[i] = Math.sqrt(var[i]);
        return var;
    }

    public static double[] variance(double[][] v) {
        int m = v.length;
        int n = v[0].length;
        double[] var = new double[n];
        int degrees = (m - 1);
        double c;
        double s;
        for (int j = 0; j < n; j++) {
            c = 0;
            s = 0;
            for (int k = 0; k < m; k++)
                s += v[k][j];
            s = s / m;
            for (int k = 0; k < m; k++)
                c += (v[k][j] - s) * (v[k][j] - s);
            var[j] = c / degrees;
        }
        return var;
    }

    public static double covariance(double[] v1, double[] v2) {
        int m = v1.length;
        double X;
        int degrees = (m - 1);
        double c;
        double s1;
        double s2;
        c = 0;
        s1 = 0;
        s2 = 0;
        for (int k = 0; k < m; k++) {
            s1 += v1[k];
            s2 += v2[k];
        }
        s1 = s1 / m;
        s2 = s2 / m;
        for (int k = 0; k < m; k++)
            c += (v1[k] - s1) * (v2[k] - s2);
        X = c / degrees;
        return X;
    }

    public static double[][] covariance(double[][] v1, double[][] v2) {
        int m = v1.length;
        int n1 = v1[0].length;
        int n2 = v2[0].length;
        double[][] X = new double[n1][n2];
        int degrees = (m - 1);
        double c;
        double s1;
        double s2;
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n2; j++) {
                c = 0;
                s1 = 0;
                s2 = 0;
                for (int k = 0; k < m; k++) {
                    s1 += v1[k][i];
                    s2 += v2[k][j];
                }
                s1 = s1 / m;
                s2 = s2 / m;
                for (int k = 0; k < m; k++)
                    c += (v1[k][i] - s1) * (v2[k][j] - s2);
                X[i][j] = c / degrees;
            }
        }
        return X;
    }

    public static double[][] covariance(double[][] v) {
        int m = v.length;
        int n = v[0].length;
        double[][] X = new double[n][n];
        int degrees = (m - 1);
        double c;
        double s1;
        double s2;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c = 0;
                s1 = 0;
                s2 = 0;
                for (int k = 0; k < m; k++) {
                    s1 += v[k][i];
                    s2 += v[k][j];
                }
                s1 = s1 / m;
                s2 = s2 / m;
                for (int k = 0; k < m; k++)
                    c += (v[k][i] - s1) * (v[k][j] - s2);
                X[i][j] = c / degrees;
            }
        }
        return X;
    }

    public static double corrD(double[] v1, double[] v2) { 
        return correlation(v1, v2);
    }
    
    public static double correlation(double[] v1, double[] v2) {
        return covariance(v1, v2) / Math.sqrt(variance(v1) * variance(v2));
    }

    public static double[][] correlation(double[][] v1, double[][] v2) {
        double[] Varv1 = variance(v1);
        double[] Varv2 = variance(v2);
        double[][] cov = covariance(v1, v2);
        for (int i = 0; i < cov.length; i++)
            for (int j = 0; j < cov[i].length; j++)
                cov[i][j] = cov[i][j] / Math.sqrt(Varv1[i] * Varv2[j]);
        return cov;
    }

    public static double[][] correlation(double[][] v) {
        int m = v.length;
        int n = v[0].length;
        double[][] X = new double[n][n];
        double[][] V = new double[n][n];
        int degrees = (m - 1);
        double c;
        double s1;
        double s2;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c = 0;
                s1 = 0;
                s2 = 0;
                for (int k = 0; k < m; k++) {
                    s1 += v[k][i];
                    s2 += v[k][j];
                }
                s1 = s1 / m;
                s2 = s2 / m;
                for (int k = 0; k < m; k++)
                    c += (v[k][i] - s1) * (v[k][j] - s2);
                V[i][j] = c / degrees;
            }
        }
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                X[i][j] = V[i][j] / Math.sqrt(V[i][i] * V[j][j]);
        return X;
    }

//  histograms functions

    public static double[][] histogram_classes(double[] values, double[] bounds) {
        return mergeColumns(centers(bounds), histogram(values, bounds));
    }

    public static double[][] histogram_classes(double[] values, double min, double max, int n) {
        double[] bounds = bounds(values, min, max, n);
        return mergeColumns(centers(bounds), histogram(values, bounds));
    }

    public static double[][] histogram_classes(double[] values, int n) {
        double[] bounds = bounds(values, n);
        return mergeColumns(centers(bounds), histogram(values, bounds));
    }

    public static double[] histogram(double[] values, double[] bounds) {
        double[] h = new double[bounds.length - 1];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < h.length; j++) {
                if (((bounds[j + 1] - values[i]) * (bounds[j] - values[i]) < 0) || ((bounds[j] == values[i]))) {
                    h[j]++;
                    break;
                }
            }
        }
        return h;
    }

    public static double[] histogram(double[] values, double min, double max, int n) {
        double[] bounds = bounds(values, min, max, n);
        return histogram(values, bounds);
    }

    public static double[] histogram(double[] values, int n) {
        return histogram(values, n);
    }

    private static double[] bounds(double[] values, int n) {
        double min = min(values);
        double max = max(values);
        return bounds(values, min, max, n);
    }

    private static double[] bounds(double[] values, double min, double max, int n) {
        double[] bounds = new double[n + 1];
        for (int i = 0; i < bounds.length; i++) {
            bounds[i] = min + (max - min) * i / (double) n;
        }
        return bounds;
    }
    
    private static double[] centers(double[] bounds) {
        double[] center = new double[bounds.length-1];
        for (int i = 0; i < center.length; i++) 
            center[i] = (bounds[i]+bounds[i+1])/2;
        return center;
    }

```
`