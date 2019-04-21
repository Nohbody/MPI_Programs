package assignment1;

import edu.princeton.cs.algs4.*;

public class SAP {

    private final Digraph G;

    public SAP(Digraph G) {
        if (G == null) {
            throw new NullPointerException("Argument should not be null");
        }
        this.G = new Digraph(G);
    }

    public int length(int v, int w) {
        return bfs(v, w)[1];
    }

    public int ancestor(int v, int w) {
        return bfs(v, w)[0];
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("Argument should not be null");
        }
        return bfs(v, w)[1];
    }

    private int[] bfs(int v, int w) {
        return bfs(new BreadthFirstDirectedPaths(G, v), new BreadthFirstDirectedPaths(G, w));
    }

    private int[] bfs(Iterable<Integer> v, Iterable<Integer> w) {
        return bfs(new BreadthFirstDirectedPaths(G, v), new BreadthFirstDirectedPaths(G, w));
    }

    private int[] bfs(BreadthFirstDirectedPaths bfs1, BreadthFirstDirectedPaths bfs2) {
        int minDistance = Integer.MAX_VALUE;
        int ancestor = 0;
        for (int i = 0; i < G.V(); i++) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                int distance = bfs1.distTo(i) + bfs2.distTo(i);
                if (distance < minDistance) {
                    minDistance = distance;
                    ancestor = i;
                }
            }
        }
        if (minDistance == Integer.MAX_VALUE) {
            return new int[]{-1, -1};
        }
        return new int[]{ancestor, minDistance};
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("Argument should not be null");
        }
        return bfs(v, w)[0];
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
