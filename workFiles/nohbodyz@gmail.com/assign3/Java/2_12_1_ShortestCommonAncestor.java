

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;


public class ShortestCommonAncestor {
    
    private Digraph G;
    int v;
    int w;
    

   // constructor takes a rooted DAG as argument
   public ShortestCommonAncestor(Digraph G) {
       if(G == null) {
           throw new NullPointerException();
       }
       
       this.G = G;
       
   }

   // length of shortest ancestral path between v and w
   public int length(int v, int w) {
       
       BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G,v);
       BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G,w);
       
       int shortestPath = Integer.MAX_VALUE; //set the shortest path to infinity so any other shortest path will be shorter.
       
       
       for (int i = 0; i < G.V(); i++) {
           
           if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
               
               if((bfsV.distTo(i) + bfsW.distTo(i)) < shortestPath) {
                   
                   shortestPath = (bfsV.distTo(i) + bfsW.distTo(i));
                   
               }
           }
       }
       if(shortestPath == Integer.MAX_VALUE) {
           shortestPath = -1;
       }
       return shortestPath;
       
   }

   // a shortest common ancestor of vertices v and w
   public int ancestor(int v, int w) {
       
       
       
       BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G,v);
       BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G,w);
       
       int shortestPath = Integer.MAX_VALUE; //set the shortest path to infinity so any other shortest path will be shorter.
       int ancestor = -1;
       
       for (int i = 0; i < G.V(); i++) {
           
           if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
               
               if((bfsV.distTo(i) + bfsW.distTo(i)) < shortestPath) {
                   
                   shortestPath = (bfsV.distTo(i) + bfsW.distTo(i));
                   ancestor = i;
               }
           }
       }
       
       return ancestor;
       
   }

   // length of shortest ancestral path of vertex subsets A and B
   public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
       
       if(subsetA == null || subsetB == null) {
           throw new NullPointerException();
       }
       
       BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G,subsetA);
       BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G,subsetB);
       
       int shortestPath = Integer.MAX_VALUE; //set the shortest path to infinity so any other shortest path will be shorter.
       
       for (int i = 0; i < G.V(); i++) {
           
           if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
               
               if((bfsV.distTo(i) + bfsW.distTo(i)) < shortestPath) {
                   
                   shortestPath = (bfsV.distTo(i) + bfsW.distTo(i));
                   
               }
           }
       }
       if(shortestPath == Integer.MAX_VALUE) {
           shortestPath = -1;
       }
       return shortestPath;
       
   }

   // a shortest common ancestor of vertex subsets A and B
   public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
       
       if(subsetA == null || subsetB == null) {
           throw new NullPointerException();
       }
       
       BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G,subsetA);
       BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G,subsetB);
       
       int shortestPath = Integer.MAX_VALUE; //set the shortest path to infinity so any other shortest path will be shorter.
       int ancestor = -1;
       
       for (int i = 0; i < G.V(); i++) {
           
           if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
               
               if((bfsV.distTo(i) + bfsW.distTo(i)) < shortestPath) {
                   
                   shortestPath = (bfsV.distTo(i) + bfsW.distTo(i));
                   ancestor = i;
               }
           }
       }
       
       return ancestor;
       
   }

   public static void main(String[] args) {
       In in = new In(args[0]);
       Digraph G = new Digraph(12);
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

