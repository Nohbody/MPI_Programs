import java.util.Arrays;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.Queue;

public class ShortestCommonAncestor {

    Digraph G;
    boolean[] visited, markedV, markedW;
    int[] edgeToV, edgeToW, distToV, distToW; 
    
   // constructor takes a rooted DAG as argument
   public ShortestCommonAncestor(Digraph G) {
       if (G == null) {
           throw new java.lang.NullPointerException("digraph cannot be null");
       }
       this.G = G;
       visited = new boolean[G.V()];
       markedV = new boolean[G.V()];
       markedW = new boolean[G.V()];
       edgeToV = new int[G.V()];
       edgeToW = new int[G.V()];
       distToV = new int[G.V()];
       distToW = new int[G.V()];
   }

   private int bfs(int v, int w) { 
       Arrays.fill(markedV, false);
       Arrays.fill(markedW, false);
       Arrays.fill(visited, false);
       Queue<Integer> qV = new Queue<Integer>();
       Queue<Integer> qW = new Queue<Integer>();
       markedV[v] = true;
       distToV[v] = 0;
       markedW[w] = true;
       distToW[w] = 0;
       qV.enqueue(v);
       qW.enqueue(w);
       
       while (!qW.isEmpty()) {
           int x = qV.dequeue();
           visited[x] = true;
           for (int i : G.adj(x)) {
               if (!markedV[i]) {
                   edgeToV[i] = x;
                   distToV[i] = distToV[x] + 1;
                   markedV[i] = true;
                   qV.enqueue(i);
               }
           }
           
           int y = qW.dequeue();
           if (visited[y] = true) return y;
           for (int i : G.adj(y)) {
               if (!markedV[i]) {
                   edgeToV[i] = x;
                   distToV[i] = distToV[y] + 1;
                   markedV[i] = true;
                   qW.enqueue(i);
               }
           }
       }
       
       return -1; // no ancestor exists
   }
   // length of shortest ancestral path between v and w
   public int length(int v, int w) {
       int a = bfs(v, w);
       return distToV[a] + distToW[a];
   }

   // a shortest common ancestor of vertices v and w
   public int ancestor(int v, int w) {
       return bfs(v, w);
   }

   // length of shortest ancestral path of vertex subsets A and B
   public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
       MaxPQ<Integer> lengths = new MaxPQ<Integer>();
       for (int i : subsetA) {
           for (int j : subsetB) {
               int a = bfs(i, j);
               lengths.insert(distToV[a] + distToW[a]);
           }
       }
       return lengths.delMax();
   }

   // a shortest common ancestor of vertex subsets A and B
   public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
       if (subsetA == null || subsetB == null) {
           throw new java.lang.NullPointerException("subsets cannot be null");
       }
       
       MaxPQ<Integer> lengths = new MaxPQ<Integer>();
       int ancestor;
       
       for (int i : subsetA) {
           for (int j : subsetB) {
               int a = bfs(i, j);
               int dist = distToV[a] + distToW[a];
               if (dist > lengths.max()) ancestor = a;
               lengths.insert(dist);
           }
       }
       return lengths.delMax();
   }

   // do unit testing of this class
   public static void main(String[] args) {
       Digraph test = new Digraph(5);
       test.addEdge(0, 2);
       test.addEdge(1, 2);
       test.addEdge(2, 4);
       test.addEdge(3, 4);
       ShortestCommonAncestor sca = new ShortestCommonAncestor(test);
       StdOut.println(sca.length(0, 3));
       StdOut.println(sca.length(0, 1));
       StdOut.println(sca.ancestor(0, 3));
       StdOut.println(sca.ancestor(0, 1));

   }
}
