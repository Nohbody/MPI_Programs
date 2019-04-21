/******************************************************************************
 *  Compilation:  javac-alg4 ShortestCommonAncestor.java
 *  Execution:    java-alg4 ShortestCommonAncestor synsets.txt hypernyms.txt
 *
 *  A Shortest Common Ancestor data type that takes a large Digraph, and given
 *  two points/subsets and finds the shortest root between those two nodes and
 *  the distance between them.
 ******************************************************************************/
import java.util.Iterator;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Topological;

public class ShortestCommonAncestor {

    // constructor takes a rooted DAG as argument
    private Digraph g;
    private int root = -1;
    private boolean[] marked;
    private Queue<Integer> qvVisited;
    private int distFromV[];
    public ShortestCommonAncestor(Digraph G) {
        
        if (G == null) throw new NullPointerException("Null graph");
        Topological top = new Topological(G);
        if(!top.hasOrder()) throw new IllegalArgumentException("Not a rooted DAG");
        this.g = G;
        qvVisited = new Queue<Integer>();
        marked = new boolean[g.V()];
        distFromV = new int[g.V()];
        for (int v = 0; v < g.V(); v++) {
            if (G.outdegree(v) == 0 && root == -1) {
                root = v;
            } else if (G.outdegree(v) == 0 && root != -1) {
                throw new IllegalArgumentException("Not a rooted DAG");
            }
        }
    }
    // length of shortest ancestral path between v and w    
    private void bfs (int v) {
               
        while (!qvVisited.isEmpty()) {
            int key = qvVisited.dequeue();
            marked[key] = false;
            distFromV[key] = 0;
        }
        Queue<Integer> q = new Queue<Integer>();
        int dist = 0;
        q.enqueue(v);
        while (q.size() > 0) {
            int numItems = q.size();
            for (int i = 0; i < numItems; i++) {
                int key = q.dequeue();
                qvVisited.enqueue(key);
                marked[key] = true;
                distFromV[key] = dist;
                for (int j : g.adj(key)) {
                    q.enqueue(j);
                }
            }
            dist++;
        }
    }
    
    public int length(int v, int w) {
               
        if (v < 0 || v > g.V() || w < 0 || w > g.V()) throw new IndexOutOfBoundsException("vertice index out of range");
        bfs(v);
        Queue<Integer> qw = new Queue<Integer>();
        qw.enqueue(w);
        int length = 0;
        while (qw.size() > 0) {
            int numItems = qw.size();
            for (int i = 0; i < numItems; i++) {
                int key = qw.dequeue();
                if (marked[key]) {
                    return length + distFromV[key];
                }
                for(int j : g.adj(key)) {
                    qw.enqueue(j);
                }
            }
            length++;
        }
        return -1;
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
       
        if (v < 0 || v > g.V() || w < 0 || w > g.V()) throw new IndexOutOfBoundsException("vertice index out of range");
        bfs(v);
        Queue<Integer> qw = new Queue<Integer>();
        qw.enqueue(w);
        while (qw.size() > 0) {
            int numItems = qw.size();
            for (int i = 0; i < numItems; i++) {
                int key = qw.dequeue(); 
                if (marked[key]) {
                    return key;
                }
                for(int j : g.adj(key)) {
                    qw.enqueue(j);
                }
            }
        }
        return -1;
    }
 
    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
    
        Iterator<Integer> itrA = subsetA.iterator();
        Iterator<Integer> itrB = subsetB.iterator();
        if(!itrA.hasNext() || !itrB.hasNext()) throw new IllegalArgumentException("Empty subset");
        int minDistance = -1;
        for (int i : subsetA) {
            for(int j : subsetB) {            
                if (i < 0 || i > g.V() || j < 0 || j > g.V()) throw new IndexOutOfBoundsException("vertice index out of range");
                int temp = length(i, j);
                if(minDistance > temp || minDistance == -1) {
                    minDistance = temp;   
                }
            }
        }
        return minDistance;  
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {

        Iterator<Integer> itrA = subsetA.iterator();
        Iterator<Integer> itrB = subsetB.iterator();
        if(!itrA.hasNext() || !itrB.hasNext()) throw new IllegalArgumentException("Empty subset");
        int minDistance = 1000000;
        int ancestor = 0;
        for (int i : subsetA) {
            for(int j : subsetB) {
                if (i < 0 || i > g.V() || j < 0 || j > g.V()) throw new IndexOutOfBoundsException("vertice index out of range");
                int temp = length(i, j);
                if(minDistance > temp) {
                    minDistance = temp;
                    ancestor = ancestor(i, j);
                }
            }
        }
        return ancestor;
    }
    
    public static void main(String[] args) { 
        
        edu.princeton.cs.algs4.In in = new edu.princeton.cs.algs4.In(args[0]);
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
