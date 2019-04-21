/**
 * Created by zora on 3/27/16.
 */
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private Digraph diGraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new java.lang.NullPointerException ("Digraph cannot be null");
        diGraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0 || v > diGraph.V() || w > diGraph.V()) throw new java.lang.IndexOutOfBoundsException ("Vertex not exist");
        return find_SAP(v, w, "Length");
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0 || v > diGraph.V() || w > diGraph.V()) throw new java.lang.IndexOutOfBoundsException ("Vertex not exist");
        return find_SAP(v, w, "Ancestor");
    }

    private int find_SAP(int v, int w, String flag) {
        BreadthFirstDirectedPaths dist2w = new BreadthFirstDirectedPaths(diGraph, w);
        int minLen = INFINITY;
        int minAncestor = -1;
        Boolean[] marked = new Boolean[diGraph.V()];
        int[] distTo = new int[diGraph.V()];
        for (int i = 0; i < diGraph.V(); i++) {
            marked[i] = false;
            distTo[i] = INFINITY;
        }
        Queue<Integer> qv = new Queue<Integer>();
        marked[v] = true;
        distTo[v] = 0;
        qv.enqueue(v);
        while (!qv.isEmpty()) {
            int curr = qv.dequeue();
            if (minLen < distTo[curr]) break;
            if (dist2w.hasPathTo(curr) && (dist2w.distTo(curr) + distTo[curr] < minLen)) {
                minLen = dist2w.distTo(curr) + distTo[curr];
                minAncestor = curr;
            }
            for (int next: diGraph.adj(curr)) {
                if (!marked[next]) {
                    distTo[next] = distTo[curr] + 1;
                    marked[next] = true;
                    qv.enqueue(next);
                }
            }
        }

        if (flag.equals("Length")) {
            if (minLen < INFINITY) return minLen;
            else return -1;
        }
        else return minAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new java.lang.NullPointerException ("Digraph cannot be null");
        for (int vertex: v) {
            if (vertex < 0 || vertex > diGraph.V()) throw new java.lang.IndexOutOfBoundsException ("Vertex not exist");
        }
        for (int vertex: w) {
            if (vertex < 0 || vertex > diGraph.V()) throw new java.lang.IndexOutOfBoundsException ("Vertex not exist");
        }
        return find_SAP(v, w, "Length");
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new java.lang.NullPointerException ("Digraph cannot be null");
        for (int vertex: v) {
            if (vertex < 0 || vertex > diGraph.V()) throw new java.lang.IndexOutOfBoundsException ("Vertex not exist");
        }
        for (int vertex: w) {
            if (vertex < 0 || vertex > diGraph.V()) throw new java.lang.IndexOutOfBoundsException ("Vertex not exist");
        }
        return find_SAP(v, w, "Ancestor");
    }

    private int find_SAP(Iterable<Integer> v, Iterable<Integer> w, String flag) {
        BreadthFirstDirectedPaths dist2w = new BreadthFirstDirectedPaths(diGraph, w);
        int minLen = INFINITY;
        int minAncestor = -1;
        Boolean[] marked = new Boolean[diGraph.V()];
        int[] distTo = new int[diGraph.V()];
        for (int i = 0; i < diGraph.V(); i++) {
            marked[i] = false;
            distTo[i] = INFINITY;
        }
        Queue<Integer> qv = new Queue<Integer>();
        for (int vertex: v) {
            marked[vertex] = true;
            distTo[vertex] = 0;
            qv.enqueue(vertex);
        }
        while (!qv.isEmpty()) {
            int curr = qv.dequeue();
            if (minLen < distTo[curr]) break;
            if (dist2w.hasPathTo(curr) && (dist2w.distTo(curr) + distTo[curr] < minLen)) {
                minLen = dist2w.distTo(curr) + distTo[curr];
                minAncestor = curr;
            }
            for (int next: diGraph.adj(curr)) {
                if (!marked[next]) {
                    distTo[next] = distTo[curr] + 1;
                    marked[next] = true;
                    qv.enqueue(next);
                }
            }
        }

        if (flag.equals("Length")) {
            if (minLen < INFINITY) return minLen;
            else return -1;
        }
        else return minAncestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
