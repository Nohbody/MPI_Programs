import edu.princeton.cs.algs4.*;
import java.lang.*;
import edu.princeton.cs.algs4.In;

public class ShortestCommonAncestor {
    public Digraph g;

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
    
        this.g = new Digraph(g);
    } // length of shortest ancestral path between v and w
    public int length(int v, int w) {
    BreadthFirstDirectedPaths searchV = new BreadthFirstDirectedPaths(g, v);
    BreadthFirstDirectedPaths searchW = new BreadthFirstDirectedPaths(g, w);

    int shortest = -1;
    for (int s = 0; s < g.V(); ++s) {
        if (searchV.hasPathTo(s) && searchW.hasPathTo(s)) {
            int dist = searchV.distTo(s) + searchW.distTo(s);
            if (shortest == -1 || dist < shortest) {
                shortest = dist;
            }
        }
    }

    return shortest;
}
    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
    BreadthFirstDirectedPaths searchV = new BreadthFirstDirectedPaths(g, v);
    BreadthFirstDirectedPaths searchW = new BreadthFirstDirectedPaths(g, w);

    int shortest = Integer.MAX_VALUE;
    int ancestor = -1;
    for (int s = 0; s < g.V(); ++s) {
        if (searchV.hasPathTo(s) && searchW.hasPathTo(s)) {
            int dist = searchV.distTo(s) + searchW.distTo(s);
            if (dist < shortest) {
                shortest = dist;
                ancestor = s;
            }
        }
    }

    return ancestor;
}
    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        BreadthFirstDirectedPaths searchA = new BreadthFirstDirectedPaths(g, subsetA);
        BreadthFirstDirectedPaths searchB = new BreadthFirstDirectedPaths(g, subsetB);

        int shortest = -1;
        for (int s = 0; s < g.V(); ++s) {
        if (searchA.hasPathTo(s) && searchB.hasPathTo(s)) {
            int dist = searchA.distTo(s) + searchB.distTo(s);
            if (shortest == -1 || dist < shortest) {
                shortest = dist;
            }
        }
    }

    return shortest;
}

    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
    BreadthFirstDirectedPaths searchA = new BreadthFirstDirectedPaths(g, subsetA);
    BreadthFirstDirectedPaths searchB = new BreadthFirstDirectedPaths(g, subsetB);

    int shortest = Integer.MAX_VALUE;
    int ancestor = -1;
    for (int s = 0; s < g.V(); ++s) {
        if (searchA.hasPathTo(s) && searchB.hasPathTo(s)) {
            int dist = searchA.distTo(s) + searchB.distTo(s);
            if (dist < shortest) {
                shortest = dist;
                ancestor = s;
            }
        }
    }

    return ancestor;
}
    // do unit testing of this class
    public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph g = new Digraph(in);
    ShortestCommonAncestor sca = new ShortestCommonAncestor(g);
    while (!StdIn.isEmpty()) {
        int v = StdIn.readInt();
        int w = StdIn.readInt();
        int length = sca.length(v, w);
        int ancestor = sca.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
}


