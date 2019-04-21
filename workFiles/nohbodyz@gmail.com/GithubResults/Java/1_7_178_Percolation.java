import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf, ufBackWash;
    private int tt, dim, source, sink;
    private boolean[] opened;

    public Percolation(int N) {
        if (N <= 0) throw new java.lang.IllegalArgumentException("Invalid N");
        dim = N;
        tt = N * N + 2;
        source = tt - 2;
        sink = tt - 1;
        uf = new WeightedQuickUnionUF(tt);
        ufBackWash = new WeightedQuickUnionUF(tt - 1);
        opened = new boolean[tt];
        for (int i = 0; i < tt; i++) opened[i] = false;
        opened[source] = true;
        opened[sink] = true;
    }               // create N-by-N grid, with all sites blocked

    private boolean inRange(int k) {
        return (k > 0 && k <= dim);
    }

    private void print() {
        for (int i = 1; i <= dim; i++) {
            for (int j = 1; j <= dim; j++) {
                if (isFull(i, j)) {
                    System.out.print(2);
                    System.out.print(' ');
                }
                else if (isOpen(i, j)) {
                    System.out.print(1);
                    System.out.print(' ');
                }
                else {
                    System.out.print(0);
                    System.out.print(' ');
                }
            }
            System.out.print('\n');
        }
        System.out.print('\n');
    }
    public void open(int i, int j) {
        if (!inRange(i))
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (!inRange(j))
            throw new IndexOutOfBoundsException("row index j out of bounds");

        if (!opened[(i - 1) * dim + (j - 1)]) {
            opened[(i - 1) * dim + (j - 1)] = true;

            if (i > 1) {
                if (isOpen(i - 1, j)) {
                    uf.union((i - 2) * dim + (j - 1), (i - 1) * dim + (j - 1));
                    ufBackWash.union((i - 2) * dim + (j - 1), (i - 1) * dim + (j - 1));
                }
            }
            else {
                uf.union(source, (i - 1) * dim + (j - 1));
                ufBackWash.union(source, (i - 1) * dim + (j - 1));
            }
            if (i < dim) {
                if (isOpen(i + 1, j)) {
                    uf.union(i * dim + (j - 1), (i - 1) * dim + (j - 1));
                    ufBackWash.union(i * dim + (j - 1), (i - 1) * dim + (j - 1));
                }
            }
            else {
                uf.union(sink, (i - 1) * dim + (j - 1));
            }

            if (j > 1 && isOpen(i, j - 1)) {
                uf.union((i - 1) * dim + (j - 2), (i - 1) * dim + (j - 1));
                ufBackWash.union((i - 1) * dim + (j - 2), (i - 1) * dim + (j - 1));
            }
            if (j < dim && isOpen(i, j + 1)) {
                uf.union((i - 1) * dim + j, (i - 1) * dim + (j - 1));
                ufBackWash.union((i - 1) * dim + j, (i - 1) * dim + (j - 1));
            }
        }
    }          // open site (row i, column j) if it is not open already

    public boolean isOpen(int i, int j)
    {
        if (!inRange(i))
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (!inRange(j))
            throw new IndexOutOfBoundsException("row index j out of bounds");
        return (opened[(i - 1) * dim + (j - 1)]);
    }// is site (row i, column j) open?

    public boolean isFull(int i, int j) {
        if (!inRange(i))
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (!inRange(j))
            throw new IndexOutOfBoundsException("row index j out of bounds");
        return ufBackWash.connected(source, (i - 1) * dim + (j - 1));
    }     // is site (row i, column j) full?

    public boolean percolates() {
        return uf.connected(source, sink);
    }             // does the system percolate?

    public static void main(String[] args) {
    }
}
