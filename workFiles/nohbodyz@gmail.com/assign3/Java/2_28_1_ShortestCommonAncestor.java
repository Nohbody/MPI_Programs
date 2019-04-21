/*
* Name: William Behunin
*
* Filename: ShortestCommonAncestor.java
*
* Description: This class data type takes in a DAG digraph and provides methods for
* the shortest ancestral path, the shortest common ancestor, and the shrotest ancestral
* path and common ancestor for the vertex subsets.
*
* Known bugs: Unknown
*/
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

public class ShortestCommonAncestor {

    private Digraph graph;
    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph g) {
        DirectedCycle cycleCheck = new DirectedCycle(g);
        if(!cycleCheck.hasCycle())
        {
            this.graph = new Digraph(g);
        }
        else
        {
            throw new IllegalArgumentException("The graph is not a Directed Acyclic Graph.");
        }
        
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        long startTime = System.nanoTime();
        BreadthFirstDirectedPaths Vsearch = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths Wsearch = new BreadthFirstDirectedPaths(graph, w);

        int shortPath = -1;
        for (int i = 0; i < graph.V(); i++) {
            if (Vsearch.hasPathTo(i) && Wsearch.hasPathTo(i)) {
                int dist = Vsearch.distTo(i) + Wsearch.distTo(i);
                if (shortPath == -1 || dist < shortPath) {
                    shortPath = dist;
                }
            }
        }
        long endTime = System.nanoTime();
        StdOut.println("Duration: " + (endTime - startTime));
        return shortPath;
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        long startTime = System.nanoTime();
        BreadthFirstDirectedPaths Vsearch = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths Wsearch = new BreadthFirstDirectedPaths(graph, w);

        int shortPath = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < graph.V(); i++) {
            if (Vsearch.hasPathTo(i) && Wsearch.hasPathTo(i)) {
                int dist = Vsearch.distTo(i) + Wsearch.distTo(i);
                if (dist < shortPath) {
                    shortPath = dist;
                    ancestor = i;
                }
            }
        }
        long endTime = System.nanoTime();
        StdOut.println("Duration: " + (endTime - startTime));
        return ancestor;
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        long startTime = System.nanoTime();
        BreadthFirstDirectedPaths subA = new BreadthFirstDirectedPaths(graph, subsetA);
        BreadthFirstDirectedPaths subB = new BreadthFirstDirectedPaths(graph, subsetB);

        int shortest = -1;
        for (int i = 0; i < graph.V(); i++) {
            if (subA.hasPathTo(i) && subB.hasPathTo(i)) {
                int dist = subA.distTo(i) + subB.distTo(i);
                if (shortest == -1 || dist < shortest) {
                    shortest = dist;
                }
            }
        }
        long endTime = System.nanoTime();
        StdOut.println("Duration: " + (endTime - startTime));
        return shortest;
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        long startTime = System.nanoTime();
        BreadthFirstDirectedPaths Vsearch = new BreadthFirstDirectedPaths(graph, subsetA);
        BreadthFirstDirectedPaths Wsearch = new BreadthFirstDirectedPaths(graph, subsetB);

        int shortPath = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < graph.V(); i++) {
            if (Vsearch.hasPathTo(i) && Wsearch.hasPathTo(i)) {
                int dist = Vsearch.distTo(i) + Wsearch.distTo(i);
                if (dist < shortPath) {
                    shortPath = dist;
                    ancestor = i;
                }
            }
        }
        long endTime = System.nanoTime();
        StdOut.println("Duration: " + (endTime - startTime));
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}