/******************************************************************************
 *  Compilation:  javac-algs4 ShortestCommonAncestor.java
 *  Execution:    java-algs4 ShortestCommonAncestor
 ******************************************************************************/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.LinearProbingHashST;
import java.util.ArrayList;
public class ShortestCommonAncestor {
    private Digraph dag;
    
    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null) {
            throw new java.lang.NullPointerException();
        }
        ArrayList<Integer> roots = findRoots(G);
        if (roots.size() == 0 || !isAcyclic(roots, G)) {
            throw new java.lang.IllegalArgumentException();
        }
        this.dag = G;
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        if (v < 0 || v >= dag.V() || w < 0 || w >= dag.V()) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int distance = 2*dag.V(); // should be a suitable upper bound for a rootedDAG
        LinearProbingHashST<Integer, Integer> vt = bfsTable(v);
        LinearProbingHashST<Integer, Integer> wt = bfsTable(w);
        for (int key : vt.keys()) {
            if (wt.contains(key) && dist(key, vt, wt) < distance) {
                distance = dist(key, vt, wt);
            }
        }
        return distance;
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        if (v < 0 || v >= dag.V() || w < 0 || w >= dag.V()) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int distance = 2*dag.V(); // should be a suitable upper bound for a rooted DAG
        int k = -1;
        LinearProbingHashST<Integer, Integer> vt = bfsTable(v);
        LinearProbingHashST<Integer, Integer> wt = bfsTable(w);
        for (int key : vt.keys()) {
            if (wt.contains(key) && dist(key, vt, wt) < distance) {
                distance = dist(key, vt, wt);
                k = key;
            }
        }
        return k;
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null) {
            throw new java.lang.NullPointerException();
        }
        if (!subsetA.iterator().hasNext() || !subsetB.iterator().hasNext()) {
            throw new java.lang.IllegalArgumentException();
        }
        int l = 2*dag.V();
        for (Integer a : subsetA) {
            if (a == null) {
                throw new java.lang.NullPointerException();
            }
            for (Integer b : subsetB) {
                if (b == null) {
                    throw new java.lang.NullPointerException();
                }
                int lTemp = length(a, b);
                if (lTemp < l) {
                    l = lTemp;
                }
            }
        }
        return l;
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null) {
            throw new java.lang.NullPointerException();
        }
        if (!subsetA.iterator().hasNext() || !subsetB.iterator().hasNext()) {
            throw new java.lang.IllegalArgumentException();
        }
        int l = 2*dag.V();
        int aVal = -1;
        int bVal = -1;
        for (Integer a : subsetA) {
            if (a == null) {
                throw new java.lang.NullPointerException();
            }
            for (Integer b : subsetB) {
                if (b == null) {
                    throw new java.lang.NullPointerException();
                }
                int lTemp = length(a, b);
                if (lTemp < l) {
                    l = lTemp;
                    aVal = a;
                    bVal = b;
                }
            }
        }
        return ancestor(aVal, bVal);
    }

    private LinearProbingHashST<Integer, Integer> bfsTable(int v) {
        LinearProbingHashST<Integer, Integer> vReachable = new LinearProbingHashST<Integer, Integer>();
        vReachable.put(v, 0);
        int dist = 1;
        ArrayList<Integer> vertices = new ArrayList<Integer>();
        vertices.add(v);
        while (vertices.size() > 0) {
            ArrayList<Integer> tempVerts = new ArrayList<Integer>();
            for (int vtx : vertices) {
                for (int w : dag.adj(vtx)) {
                    tempVerts.add(w);
                    if (!vReachable.contains(w)) {
                        vReachable.put(w, dist);
                    }
                }
            }
            vertices = tempVerts;
            dist++;
        }
        return vReachable;
    }

    private int dist(Integer key, LinearProbingHashST<Integer, Integer> v, LinearProbingHashST<Integer, Integer> w) {
        return v.get(key)+w.get(key);
    }

    private ArrayList<Integer> findRoots(Digraph G) {
        ArrayList<Integer> roots = new ArrayList<Integer>();
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) {
                roots.add(i);
            }
        }
        return roots;
    }

    private boolean isAcyclic(ArrayList<Integer> roots, Digraph G) {
        boolean[] marked = new boolean[G.V()];
        boolean[] active = new boolean[G.V()];
        boolean cycles = false;
        for (int r : roots) {
            cycles = hasCycles(r, marked, active, G);
            if (cycles == true) {
                return false;
            }
        }
        return true;
    }

    private boolean hasCycles(int v, boolean[] marked, boolean[] active, Digraph G) {
        active[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                if (active[w]) {
                    return true;
                }
                else if (hasCycles(w, marked, active, G)) {
                    return true;
                }
            }
        }
        active[v] = false;
        marked[v] = true;
        return false;
    }





    // do unit testing of this class
    public static void main(String[] args) {
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
