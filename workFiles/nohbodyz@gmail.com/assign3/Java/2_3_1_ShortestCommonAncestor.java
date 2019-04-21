import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import java.util.*;

public class ShortestCommonAncestor {
    private Digraph D;
    private int root;
    private int[] marked;     // Simple table of nodes

   // constructor takes a rooted DAG as argument
   public ShortestCommonAncestor(Digraph D) {
       this.D = D;
       // Make table to mark visited nodes equal in size to graph
       this.marked = new int[D.V()];
       // and initialize values to -1
       for (int i=0; i<marked.length; i++) {
           marked[i] = -1;
       }
       
       // Find the root
       this.root = -1;
       /*
       for (int i=0; i<D.V(); i++) {
           int outDegree = D.outdegree(i);
           if (outDegree == 0) {
               this.root = i;
               System.out.println("The root ID is: " + i);
               break;
           }
       }
       */
   }

   // length of shortest ancestral path between v and w
   public int length(int v, int w) {
       int[] lengthAnc = lengthAndAncestor(v, w);
       return lengthAnc[0];
   }

   // a shortest common ancestor of vertices v and w
   public int ancestor(int v, int w) {
       int[] lengthAnc = lengthAndAncestor(v, w);
       return lengthAnc[1];
   }
   
   // returns the ancestor and the length of the path between v and w.
   private int[] lengthAndAncestor(int v, int w) {
       // If words are in the same synset, return 0
       if (v == w) {
           int[] lengthAnc = {0, v};
           return lengthAnc;
       };

       // Makes a list of every marked[] array index, so at the end, can revert list to initial values
       Queue<Integer> markCleaner = new Queue<Integer>();
       markCleaner.enqueue(v);
       markCleaner.enqueue(w);
       
       // Length will calc total dist
       int length = -1;
       int dist = 0;
       // Initialize queue that will take in the adjacency lists from Digraph
       Queue<Integer> adjV = new Queue<Integer>();
       adjV.enqueue(v);
       marked[v] = 0;
       boolean atRootV = false;
       
       Queue<Integer> adjW = new Queue<Integer>();
       adjW.enqueue(w);
       marked[w] = 0;
       boolean atRootW = false;

       // check if V bfs is already at the root
       while (atRootV == false) {
           dist++;
           // nextQueue will store list of all nodes adjacent to all current nodes (next level up)
           Queue<Integer> nextQueue = new Queue<Integer>();
           // If current node level is not adjacent to any other nodes, then it is the root
           if (adjV.isEmpty() == true) {
               atRootV = true;
           }
           // iterate through all nodes on adjacency list of current nodes
           while (adjV.isEmpty() == false) {
               int adjNext = adjV.dequeue();
               Iterator<Integer> iterV = D.adj(adjNext).iterator();
               // check if every adj node has been marked
               while (iterV.hasNext()) {
                   int next = iterV.next();
                   // if not, then mark it with current dist (note: if it already reached this node, prev dist will be shorter)
                   if (marked[next] == -1) {
                       marked[next] = dist;
                       // Tracks all changed indices in marked[] array
                       markCleaner.enqueue(next);
                   }
                   nextQueue.enqueue(next);
               }
           }
           // Move up one level and adjacent nodes are now the current nodes
           adjV = nextQueue;
       }
       // Now all nodes reachable by V have been marked, so check W
       dist = 0;
       while (atRootW == false) {
           dist++;
           Queue<Integer> nextQueue = new Queue<Integer>();
           // if current node level has no adjacent nodes, then we are at the root
           if (adjW.isEmpty() == true) {
               atRootW = true;
           }
           // iterate through all nodes on adjacency list
           while (adjW.isEmpty() == false) {
               int adjNext = adjW.dequeue();
               Iterator<Integer> iterW = D.adj(adjNext).iterator();
               // if node in search has no adjacent nodes, then it is the root
               
               // check if every adj node has been marked
               while (iterW.hasNext()) {
                   int next = iterW.next();
                   // if so, then calc distance and return
                   if (marked[next] > -1) {
                       length = dist + marked[next];
                       int[] lengthAnc = {length, next};
                       // Before return, clean up marked[] array
                       
                       while (markCleaner.isEmpty() == false) {
                           int scrub = markCleaner.dequeue();
                           marked[scrub] = -1;
                       }
                       return lengthAnc;
                   }
                   nextQueue.enqueue(next);
               }
           }
           adjW = nextQueue;
       }
       
       // if no common ancestors, then return -1
       StdOut.println("Error: No Common Ancestor found.");
       int [] lengthAnc = {-1, -1};
       return lengthAnc;
   }

   // length of shortest ancestral path of vertex subsets A and B
   public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
       Iterator<Integer> iterA = subsetA.iterator();
       Iterator<Integer> iterB;
       boolean hasLength = false;
       int minLength = -1;
       
       // Iterate through every pair of A and B, and find shortest length of each.
       while (iterA.hasNext()) {
           int nextA = iterA.next();
           // Start new B iterator
           iterB = subsetB.iterator();
           while(iterB.hasNext()) {
               int nextB = iterB.next();
               int length = length(nextA, nextB);
               
               if (hasLength == true) {
                   if (length < minLength) {
                       minLength = length;
                   }
               } else {
                   hasLength = true;
                   minLength  = length;
               }
           }
       }
       // Output shortest length of all pairs
       return minLength;
   }

   // a shortest common ancestor of vertex subsets A and B
   public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
       Iterator<Integer> iterA = subsetA.iterator();
       Iterator<Integer> iterB;
       boolean hasSCA = false;
       int minLength = -1;
       int sca = -1;
       
       // Iterate through every pair of A and B, and find shortest length and ancestor of each.
       while (iterA.hasNext()) {
           int nextA = iterA.next();
           // Start new B iterator
           iterB = subsetB.iterator();
           while(iterB.hasNext()) {
               int nextB = iterB.next();
               int[] lenAnc = lengthAndAncestor(nextA, nextB);
               
               if (hasSCA == true) {
                   if (lenAnc[0] < minLength) {
                       sca = lenAnc[1];
                       minLength = lenAnc[0];
                   }
               } else {
                   hasSCA = true;
                   sca = lenAnc[1];
                   minLength = lenAnc[0];
               }
           }
       }
       // Output shortest common ancestor of all pairs
       return sca;
   }

   // for unit testing
   public Digraph getDigraph() {
       return D;
   }
   
   // do unit testing of this class
   public static void main(String[] args) {
       if (args.length < 1) {
           personalUnitTest();
       } else {
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
   
   // Unit test made by me
   public static void personalUnitTest() {
    // Basic tree test
       Digraph d1 = new Digraph(6);
       d1.addEdge(1, 0);
       d1.addEdge(2, 0);
       d1.addEdge(3, 1);
       d1.addEdge(4, 1);
       d1.addEdge(5, 2);
       
       /*
        *             0
        *          /      \
        *         1        2
        *        / \      / \
        *       3   4    5 
        */
       
       ShortestCommonAncestor sca = new ShortestCommonAncestor(d1);
       
       int i=0;
       // 1: (5,5) Should be length=0 and ancestor=5
       i++;
       StdOut.println("Testing Case: " + i);
       StdOut.println("length: " + sca.length(5, 5));
       StdOut.println("ancestor: " + sca.ancestor(5, 5));  
       // 2: (0,2) Should be length=1 and ancestor=0
       i++;
       StdOut.println("Testing Case: " + i);
       StdOut.println("length: " + sca.length(0, 2));
       StdOut.println("ancestor: " + sca.ancestor(0, 2));
       // 3: (5,1) Should be length=3 and ancestor=0
       i++;
       StdOut.println("Testing Case: " + i);
       StdOut.println("length: " + sca.length(5, 1));
       StdOut.println("ancestor: " + sca.ancestor(5, 1));
       // 4: (1,5) Should be length=3 and ancestor=0
       i++;
       StdOut.println("Testing Case: " + i);
       StdOut.println("length: " + sca.length(1, 5));
       StdOut.println("ancestor: " + sca.ancestor(1, 5));
       // 5: (3,4) Should be length=2 and ancestor=1
       i++;
       StdOut.println("Testing Case: " + i);
       StdOut.println("length: " + sca.length(3, 4));
       StdOut.println("ancestor: " + sca.ancestor(3, 4));
       
       // ({3,4},{2,5}) should be length=3 and ancestor=0
       Queue<Integer> qV = new Queue<Integer>();
       qV.enqueue(3);
       qV.enqueue(4);
       
       Queue<Integer> qW = new Queue<Integer>();
       qW.enqueue(2);
       qW.enqueue(5);
       
       i++;
       StdOut.println("Testing Case: " + i);
       StdOut.println("length: " + sca.length(qV, qW));
       StdOut.println("ancestor: " + sca.ancestor(qV, qW));
   }
}