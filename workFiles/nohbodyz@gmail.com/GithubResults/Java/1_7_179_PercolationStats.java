import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Created by zora on 1/25/16.
 */
public class PercolationStats {
    private Percolation pr;
    private int[] counter;
    private int times, dim;

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) throw new IllegalArgumentException("N or T invalid");
        times = T;
        dim = N;
        counter = new int[times];
        for (int k = 0; k < times; k++) {
            int i, j;
            Percolation pr;
            pr = new Percolation(dim);
            counter[k] = 0;
            while (!pr.percolates()) {
                i = StdRandom.uniform(dim) + 1;
                j = StdRandom.uniform(dim) + 1;
                //System.out.printf("%d %d\n", i, j);
                if (!pr.isOpen(i, j)) {
                    counter[k]++;
                    pr.open(i, j);
                }
            }
        }

    }                    // perform T independent experiments on an N-by-N grid
    public double mean() {
        return (StdStats.mean(counter) / (dim * dim));
    }                    // sample mean of percolation threshold

    public double stddev() {
        return (StdStats.stddev(counter) / (dim * dim));
    }                    // sample standard deviation of percolation threshold
    public double confidenceLo() {
        return (mean() - 1.96 * stddev() / Math.sqrt(times));
    }              // low  endpoint of 95% confidence interval

    public double confidenceHi() {
        return (mean() + 1.96 * stddev() / Math.sqrt(times));
    }              // high endpoint of 95% confidence interval

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats ps;
        ps = new PercolationStats(N, T);
        System.out.printf("mean                    = %f\n", ps.mean());
        System.out.printf("stddev                  = %f\n", ps.stddev());
        System.out.printf("95%% confidence interval = %f, %f\n",
                ps.confidenceLo(), ps.confidenceHi());
    }    // test client (described below)
}

