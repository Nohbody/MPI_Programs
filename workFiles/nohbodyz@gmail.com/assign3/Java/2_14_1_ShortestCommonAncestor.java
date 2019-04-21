import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.Iterator;

public class ShortestCommonAncestor {
   private Digraph dg;
   
   // constructor takes a rooted DAG as argument
   public ShortestCommonAncestor(Digraph G) {
       if(G == null) throw new java.lang.NullPointerException("Null argument provided.");
       DirectedCycle dc = new DirectedCycle(G);
       if(dc.hasCycle()) throw new java.lang.IllegalArgumentException("Digraph has cycle.");
       
       dg = G;
   }

   // length of shortest ancestral path between v and w
   public int length(int v, int w) {
       int distance = 0;
       Integer vertex;
       Queue<Integer> queue = new Queue<Integer>();  
       LinearProbingHashST<Integer, Integer> reachable = reachable(w);
       queue.enqueue(v);
       
       while(!queue.isEmpty()) {
           vertex = queue.dequeue();
           if (reachable.contains(vertex)) return reachable.get(vertex) + distance;
           
           for(int i : dg.adj(vertex)) {
               queue.enqueue(i);
           }
           
           distance += 1;
       }
       
       return -1;
   }
   
   private LinearProbingHashST<Integer, Integer> reachable(int w){
       LinearProbingHashST<Integer, Integer> ST = new LinearProbingHashST<Integer, Integer>();
       Queue<Integer> queue = new Queue<Integer>();  
       int vertex;
       int distance = 0;
       queue.enqueue(w);
       
       while(!queue.isEmpty()) {
           vertex = queue.dequeue();
           ST.put(vertex, distance);
           distance++;
           
           if (dg.outdegree(vertex) == 0) continue;
           
           for(int i : dg.adj(vertex)) {
               queue.enqueue(i);
           }
       }
       
       return ST;
   }

   // a shortest common ancestor of vertices v and w
   public int ancestor(int v, int w) {
       Queue<Integer> queue = new Queue<Integer>();  
       LinearProbingHashST<Integer, Integer> reachable = reachable(w);
       Integer vertex;
       queue.enqueue(v);
       
       while(!queue.isEmpty()) {
           vertex = queue.dequeue();
           if (reachable.contains(vertex)) return vertex;
           
           for(int i : dg.adj(vertex)) {
               queue.enqueue(i);
           }
       }
       
       return -1;
   }

   // length of shortest ancestral path of vertex subsets A and B
   public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
       if(subsetA == null || subsetB == null) throw new java.lang.NullPointerException("Null argument provided.");
       int shortestPath = Integer.MAX_VALUE; 
       
       Iterator<Integer> iterA = subsetA.iterator();
       
       while(iterA.hasNext()) {
           Iterator<Integer> iterB = subsetB.iterator();
           int v = iterA.next();
           while(iterB.hasNext()) {
               int w = iterB.next();
               int l = length(v, w);
               if(l < shortestPath) shortestPath = l;
           }
       }
       
       return shortestPath;
   }

   // a shortest common ancestor of vertex subsets A and B
   public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
       if(subsetA == null || subsetB == null) throw new java.lang.NullPointerException("Null argument provided.");
       int shortestPath = Integer.MAX_VALUE; 
       int ancestor = 0;
       
       Iterator<Integer> iterA = subsetA.iterator();
       
       while(iterA.hasNext()) {
           Iterator<Integer> iterB = subsetB.iterator();
           int v = iterA.next();
           while(iterB.hasNext()) {
               int w = iterB.next();
               int l = length(v, w);
               if(l < shortestPath) {
                   shortestPath = l;
                   ancestor = ancestor(v, w);
               }
           }
       }
       
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