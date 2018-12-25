import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private double[] fracOpenSites;
	private double totalSites;
	private int trials;

	public PercolationStats(int n, int trials) {

		if (n <= 0 || trials <= 0) {
			throw new IllegalArgumentException();
		}

		this.trials = trials;
		totalSites = n * n;
		fracOpenSites = new double[trials];

		for (int i = 0; i < trials; i++) {
			Percolation p = new Percolation(n);

			while (!p.percolates()) {
				int row = StdRandom.uniform(1, n + 1);
				int col = StdRandom.uniform(1, n + 1);
				p.open(row, col);
			}
			double fractionOpenSites = ((double) p.numberOfOpenSites() / totalSites);

			fracOpenSites[i] = fractionOpenSites;
		}
	}

	public double mean() {
		return StdStats.mean(fracOpenSites);
	}

	public double stddev() {
		return StdStats.stddev(fracOpenSites);
	}

	public double confidenceLo() {
		double lo = mean() - ((1.96 * stddev()) / Math.sqrt(trials));
		return lo;
	}

	public double confidenceHi() {
		double hi = mean() + ((1.96 * stddev()) / Math.sqrt(trials));
		return hi;
	}

	public static void main(String[] args) {
		int n = StdIn.readInt();
		int trials = StdIn.readInt();
		PercolationStats ps = new PercolationStats(n, trials);
		System.out.println("mean = " + ps.mean());
		System.out.println("stddev = " + ps.stddev());
		System.out.println("95% confidence interval = [" + ps.confidenceLo() + "," + ps.confidenceHi() + "]");
	}
}
