import java.util.Iterator;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class ShortestCommonAncestor {
    
    //private RedBlackBST<Integer, Integer> tree;
    private Digraph G;

   // constructor takes a rooted DAG as argument
   public ShortestCommonAncestor(Digraph G) {
       this.G = G;
   }

   // length of shortest ancestral path between v and w
   public int length(int v, int w) {
       int x = ancestor(v, w);
       return Math.min(new BreadthFirstDirectedPaths(G, v).distTo(x), 
               new BreadthFirstDirectedPaths(G, w).distTo(x));
   }

   // a shortest common ancestor of vertices v and w
   public int ancestor(int v, int w) {
       validateVertex(v, w);
       return ancestor(new BreadthFirstDirectedPaths(G, v), w);
   }
   
   private int ancestor(BreadthFirstDirectedPaths bfs, int w) {
       validateVertex(w);
       if (bfs.hasPathTo(w))
           return w;
       for (Integer a: G.adj(w))
           return ancestor(bfs, a);
       throw new RuntimeException("Graph is not connected, rooted, and/or acyclic. ");
   }

   // length of shortest ancestral path of vertex subsets A and B
   public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
       if (subsetA == null || subsetB == null )
           throw new NullPointerException();
       if (!subsetA.iterator().hasNext() ||
               !subsetB.iterator().hasNext())
           throw new IllegalArgumentException();
       
       int minLength = Integer.MAX_VALUE;
       Iterator<Integer> itrA = subsetA.iterator(),
               itrB = subsetB.iterator();
       
       while (itrA.hasNext()) {
           Integer a = itrA.next();
           if (a == null)
               throw new NullPointerException();
           validateVertex(a);
           
           while (itrB.hasNext()) {
               Integer b = itrB.next();
               if (b == null)
                   throw new NullPointerException();
               validateVertex(b);
               int length = length(a, itrB.next());
               if (length < minLength)
                   minLength = length;
           }
           
       }
       return minLength;
   }
   
   private void validateVertex(int... vs) {
       for (int v: vs)
           if (v < 0 || v > G.V())
               throw new IndexOutOfBoundsException();
   }

   // a shortest common ancestor of vertex subsets A and B
   public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
       if (subsetA == null || subsetB == null )
           throw new NullPointerException();
       if (!subsetA.iterator().hasNext() ||
               !subsetB.iterator().hasNext())
           throw new IllegalArgumentException();
       
       int minLength = Integer.MAX_VALUE;
       int ancestor = -1;
       Iterator<Integer> itrA = subsetA.iterator(),
               itrB = subsetB.iterator();
       
       while (itrA.hasNext()) {
           Integer a = itrA.next();
           if (a == null)
               throw new NullPointerException();
           validateVertex(a);
           
           while (itrB.hasNext()) {
               Integer b = itrB.next();
               if (b == null)
                   throw new NullPointerException();
               validateVertex(b);
               int x = ancestor(a, b);
               int length = length(a, x) + length(b, x);
               if (length < minLength) {
                   minLength = length;
                   ancestor = x;
               }
           }
       }
       return ancestor;
   }

   // do unit testing of this class
   public static void main(String[] args) {
       In in = new In("digraph1.txt");
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