import java.io.FileNotFoundException;
import java.util.HashMap;

import edu.princeton.cs.algs4.Digraph;

public class ShortestCommonAncestor {

   private static Digraph G;

   WordNet myNet = new WordNet(null, null);
   
   private HashMap<Integer, String> synsetHash = new HashMap<Integer, String>();
   private HashMap<Integer, Integer> hypernymHash = new HashMap<Integer, Integer>();

	
	
// constructor takes a rooted DAG as argument
   public ShortestCommonAncestor(Digraph G) throws FileNotFoundException
   {
	  
	  
	  //useing the hashmap to make a digraph
	  
	  synsetHash = myNet.synsetFileReader();
	  hypernymHash = myNet.hypernymFileReader();
	  
	 for(int i = 0; i < hypernymHash.size(); i++)
	 {
		 int v = hypernymHash.get(i);
		 
		 G.adj(v).iterator();
	 }
   
	 //return G;
		 //System.out.println(synsetHash);
		System.out.println(G);
   }

   // length of shortest ancestral path between v and w
   public int length(int v, int w)
   {
	   
	  
	   return w;
	   
   }
   // a shortest common ancestor of vertices v and w
   public int ancestor(int v, int w)
   {
	return w;
	   
   }
   // length of shortest ancestral path of vertex subsets A and B
   public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB)
   {
	return 0;
	   
   }
   // a shortest common ancestor of vertex subsets A and B
   public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB)
   {
	return 0;
	   
   }
   
  

// do unit testing of this class
   public static void main(String[] args) throws FileNotFoundException {
	  
	  // ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
	   //System.out.println(G);
	   
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
