/******************************************************************************
 *  Compilation:  javac-algs4 ShortestCommonAncestor.java
 *  Execution:    java-algs4 ShortestCommonAncestor input.txt
 *
 *  A class for finding the shortest common ancestor of two vertices in a DAG.
 *
 *  % more digraph1.txt
 *  12
 *  11
 *   6  3
 *   7  3
 *   3  1
 *   4  1
 *   5  1
 *   8  5
 *   9  5
 *  10  9
 *  11  9
 *   1  0
 *   2  0
 *
 *  % java ShortestCommonAncestor digraph1.txt
 *  3 10
 *  length = 4, ancestor = 1
 *
 *  8 11
 *  length = 3, ancestor = 5
 *
 *  6 2
 *  length = 4, ancestor = 0
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class ShortestCommonAncestor {

    private final Digraph dag;
    private final int rootV;
    private final BFSSearch searcher;

    /**
     * Construct a ShortestCommonAncestor using a DAG.
     */
    public ShortestCommonAncestor(Digraph G) {
        if (G == null) {
            throw new NullPointerException();
        }
        int root = -1;
        int rootCount = 0;
        for (int v = 0, n = G.V(); v < n; ++v) {
            if (G.outdegree(v) == 0) {
                root = v;
                ++rootCount;
            }
        }
        if (rootCount == 0) {
            throw new IllegalArgumentException("Digraph has no root");
        }
        if (new DirectedCycle(G).hasCycle() || rootCount > 1) {
            throw new IllegalArgumentException("Digraph is not a DAG");
        }

        rootV = root;
        dag = new Digraph(G);
        searcher = new BFSSearch(dag);
    }

    /**
     * Find the length of shortest ancestral path between v and w
     */
    public int length(int v, int w) {
        validate(v, w);
        return search(v, w).length;
    }

    /**
     * Find a shortest common ancestor of vertices v and w
     */
    public int ancestor(int v, int w) {
        validate(v, w);
        return search(v, w).ancestor;
    }

    /**
     * Find the length of shortest ancestral path of vertex subsets A and B
     */
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null) {
            throw new NullPointerException();
        }
        return multiSearch(subsetA, subsetB).length;
    }

    /**
     * Find the shortest common ancestor of vertex subsets A and B
     */
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null) {
            throw new NullPointerException();
        }
        return multiSearch(subsetA, subsetB).ancestor;
    }

    private SCAResult multiSearch(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        Iterator<Integer> itA = subsetA.iterator();
        Iterator<Integer> itB = subsetB.iterator();
        if (!itA.hasNext() || !itB.hasNext()) {
            throw new IllegalArgumentException("Iterables must not be empty");
        }

        SCAResult min = new SCAResult(rootV, Integer.MAX_VALUE);
        for (int v : subsetA) {
            for (int w : subsetB) {
                SCAResult r = search(v, w);
                if (r.length < min.length) {
                    min = r;
                    if (min.length == 0) {
                        break;
                    }
                }
            }
        }
        return min;
    }

    private SCAResult search(int v, int w) {
        int a = searcher.search(v, w);
        int len = searcher.vLengthToAncestor(a) + searcher.wLengthToAncestor(a);
        searcher.reset();
        return new SCAResult(a, len);
    }

    private void validate(int v, int w) {
        validate(v);
        validate(w);
    }

    public void validate(int v) {
        if (v < 0 || v > dag.V()) {
            throw new IndexOutOfBoundsException("Invalid vertex: " + v);
        }
    }


    private class BFSSearch {
        private static final int DEFAULT = -1;

        private final boolean[] vMarked;
        private final boolean[] wMarked;
        private final int[] vDistTo;
        private final int[] wDistTo;
        private final Queue<Integer> vChanged;
        private final Queue<Integer> wChanged;
        private final Digraph dag;

        private BFSSearch(Digraph dag) {
            this.dag = dag;
            int v = dag.V();
            vMarked = new boolean[v];
            wMarked = new boolean[v];
            vDistTo = new int[v];
            wDistTo = new int[v];
            for (int i = 0; i < v; ++i) {
                vDistTo[i] = DEFAULT;
                wDistTo[i] = DEFAULT;
            }
            vChanged = new Queue<>();
            wChanged = new Queue<>();
        }

        public int search(int v, int w) {
            bfsV(v);
            return bfsW(w);
        }

        public int vLengthToAncestor(int a) {
            return vDistTo[a];
        }

        public int wLengthToAncestor(int a) {
            return wDistTo[a];
        }

        private void bfsV(int s) {
            Queue<Integer> q = new Queue<>();
            vChanged.enqueue(s);
            vMarked[s] = true;
            vDistTo[s] = 0;
            q.enqueue(s);
            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : dag.adj(v)) {
                    if (!vMarked[w]) {
                        vChanged.enqueue(w);
                        vDistTo[w] = vDistTo[v] + 1;
                        vMarked[w] = true;
                        q.enqueue(w);
                    }
                }
            }
        }

        private int bfsW(int s) {
            wMarked[s] = true;
            wDistTo[s] = 0;
            if (vMarked[s]) {
                return s;
            }
            Queue<Integer> q = new Queue<>();
            q.enqueue(s);
            wChanged.enqueue(s);
            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : dag.adj(v)) {
                    if (!wMarked[w] && !vMarked[w]) {
                        wChanged.enqueue(w);
                        wDistTo[w] = wDistTo[v] + 1;
                        wMarked[w] = true;
                        q.enqueue(w);
                    }
                    else if (vMarked[w]) {
                        wChanged.enqueue(w);
                        wDistTo[w] = wDistTo[v] + 1;
                        wMarked[w] = true;
                        return w;
                    }
                }
            }
            throw new IllegalStateException("Could not find ancestor. Digraph is not a DAG.");
        }

        public void reset() {
            while (!vChanged.isEmpty()) {
                int v = vChanged.dequeue();
                vMarked[v] = false;
                vDistTo[v] = DEFAULT;
            }
            while (!wChanged.isEmpty()) {
                int v = wChanged.dequeue();
                wMarked[v] = false;
                wDistTo[v] = DEFAULT;
            }
        }
    }


    private class SCAResult {
        private final int ancestor;
        private final int length;

        public SCAResult(int a, int len) {
            ancestor = a;
            length = len;
        }
    }


    // do unit testing of this class
    public static void main(String[] args) {
        // TODO: clean
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}