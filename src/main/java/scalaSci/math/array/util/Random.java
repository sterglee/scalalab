package scalaSci.math.array.util;

import edu.cornell.lassp.houle.RngPack.RandomSeedable;
import edu.cornell.lassp.houle.RngPack.Ranmar;


public class Random {

	public static RandomSeedable RandEngine = new Ranmar();

	/**
	 * Generate a random number between 0 and 1.
	 * maybe changed for a better random  number generator if needed.
	 * @return A double between 0 and 1.
	 */
	public static double raw() {
		return RandEngine.raw();
	}

	/**
	 * Generate a random integer.
	 * 
	 * @param i0
	 *            Min of the random variable.
	 * @param i1
	 *            Max of the random variable.
	 * @return An int between i0 and i1.
	 */
	public static int randInt(int i0, int i1) {
		double x = raw();
		int i = i0 + new Double(Math.floor((i1 - i0 + 1) * x)).intValue();
		return i;
	}

	/**
	 * Generate a random number from a uniform random variable.
	 * 
	 * @param min
	 *            Min of the random variable.
	 * @param max
	 *            Max of the random variable.
	 * @return A double.
	 */
	public static double uniform(double min, double max) {
		double x = min + (max - min) * raw();
		return x;
	}

	/**
	 * Generate a random number from a discrete random variable.
	 * 
	 * @param values
	 *            Discrete values.
	 * @param prob
	 *            Probability of each value.
	 * @return A double.
	 */
	public static double dirac(double[] values, double[] prob) {
		double[] prob_cumul = new double[values.length];
		prob_cumul[0] = prob[0];
		for (int i = 1; i < values.length; i++) {
			prob_cumul[i] = prob_cumul[i - 1] + prob[i];
		}
		double y = raw();
		double x = 0;
		for (int i = 0; i < values.length - 1; i++) {
			if ((y > prob_cumul[i]) && (y < prob_cumul[i + 1])) {
				x = values[i];
			}
		}
		return x;
	}

	/**
	 * Generate a random number from a Gaussian (Normal) random variable.
	 * 
	 * @param mu
	 *            Mean of the random variable.
	 * @param sigma
	 *            Standard deviation of the random variable.
	 * @return A double.
	 */
	public static double normal(double mu, double sigma) {
		double x = mu + sigma * Math.cos(2 * Math.PI * raw()) * Math.sqrt(-2 * Math.log(raw()));
		return x;
	}

	/**
	 * Generate a random number from a Chi-2 random variable.
	 * 
	 * @param n
	 *            Degrees of freedom of the chi2 random variable.
	 * @return A double.
	 */
	public static double chi2(int n) {
		double x = 0;
		for (int i = 0; i < n; i++) {
			double norm = normal(0, 1);
			x += norm * norm;
		}
		return x;
	}

	/**
	 * Generate a random number from a LogNormal random variable.
	 * 
	 * @param mu
	 *            Mean of the Normal random variable.
	 * @param sigma
	 *            Standard deviation of the Normal random variable.
	 * @return A double.
	 */
	public static double logNormal(double mu, double sigma) {
		double x = mu + sigma * Math.cos(2 * Math.PI * raw()) * Math.sqrt(-2 * Math.log(raw()));
		return Math.exp(x);
	}

	/**
	 * Generate a random number from an exponantial random variable (Mean =
	 * 1/lambda, variance = 1/lambda^2).
	 * 
	 * @param lambda
	 *            Parmaeter of the exponential random variable.
	 * @return A double.
	 */
	public static double exponential(double lambda) {
		double x = -1 / lambda * Math.log(raw());
		return x;
	}

	/**
	 * Generate a random number from a symetric triangular random variable.
	 * 
	 * @param min
	 *            Min of the random variable.
	 * @param max
	 *            Max of the random variable.
	 * @return A double.
	 */
	public static double triangular(double min, double max) {
		double x = min / 2 + (max - min) * raw() / 2 + min / 2 + (max - min) * raw() / 2;
		return x;
	}

	/**
	 * Generate a random number from a non-symetric triangular random variable.
	 * 
	 * @param min
	 *            Min of the random variable.
	 * @param med
	 *            Value of the random variable with max density.
	 * @param max
	 *            Max of the random variable.
	 * @return A double.
	 */
	public static double triangular(double min, double med, double max) {
		double y = raw();
		// if min < x < med, y = (x-min)�/(max-min)(med-min), else, med < x <
		// max, and y = 1-(max-x)�/(max-min)(max-med)
		double x = (y < ((med - min) / (max - min))) ? (min + Math.sqrt(y * (max - min) * (med - min)))
				: (max - Math.sqrt((1 - y) * (max - min) * (max - med)));
		return x;
	}

	/**
	 * Generate a random number from a beta random variable.
	 * 
	 * @param a
	 *            First parameter of the Beta random variable.
	 * @param b
	 *            Second parameter of the Beta random variable.
	 * @return A double.
	 */
	public static double beta(double a, double b) {
		double try_x;
		double try_y;
		do {
			try_x = Math.pow(raw(), 1 / a);
			try_y = Math.pow(raw(), 1 / b);
		} while ((try_x + try_y) > 1);
		return try_x / (try_x + try_y);
	}

	/**
	 * Generate a random number from a Cauchy random variable (Mean = Inf, and
	 * Variance = Inf).
	 * 
	 * @param mu
	 *            Median of the Weibull random variable
	 * @param sigma
	 *            Second parameter of the Cauchy random variable.
	 * @return A double.
	 */
	public static double cauchy(double mu, double sigma) {
		double x = sigma * Math.tan(Math.PI * (raw() - 0.5)) + mu;
		return x;
	}

	/**
	 * Generate a random number from a Weibull random variable.
	 * 
	 * @param lambda
	 *            First parameter of the Weibull random variable.
	 * @param c
	 *            Second parameter of the Weibull random variable.
	 * @return A double.
	 */
	public static double weibull(double lambda, double c) {
		double x = Math.pow(-Math.log(1 - raw()), 1 / c) / lambda;
		return x;
	}

	/**
	 * Generate a random number from a random variable definied by its density
	 * function, using the rejection technic. !!! WARNING : this simulation
	 * technic can take a very long time !!!
	 * 
	 * @param fun
	 *            Density function (may be not normalized) of the random
	 *            variable.
	 * @param maxFun
	 *            Max of the function.
	 * @param min
	 *            Min of the random variable.
	 * @param max
	 *            Max of the random variable.
	 * @return A double.
	 */
	public static double rejection(Function fun, double maxFun, double min, double max) {
		double try_x;
		double try_y;
		do {
			try_x = min + raw() * (max - min);
			try_y = raw() * maxFun;
		} while (fun.f(try_x) < try_y);
		return try_x;
	}

}
