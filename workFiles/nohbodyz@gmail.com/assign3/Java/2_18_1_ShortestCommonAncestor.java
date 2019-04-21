import java.util.ArrayList;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Cycle;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Stack;

public class ShortestCommonAncestor {
	
	public static Digraph Graph;
	public DirectedCycle finder;
	
	// constructor takes a rooted DAG as argument
	public ShortestCommonAncestor(Digraph G) {
		
		if(isDag(G) == false) {
			throw new IllegalArgumentException();
		}
		this.Graph = G;
	}
	
	//helper method for determing if a graph is a rooted DAG
	public boolean isDag(Digraph G) {
		int numberOfRoots = 0;
		boolean isRooted = true;
		
		//checking above every vertex of the graph, if nothing is there
		//then it is a root
		for(int i = 0; i <G.V(); i++) {
			ArrayList<Integer> adj = (ArrayList<Integer>)G.adj(i);
			if(adj.isEmpty() == true) {
				numberOfRoots ++;
			}
		}
		if(numberOfRoots != 1) {
			isRooted = false;
		}else {
			isRooted = true;
		}
		
		//if a graph has a cycle then it is not a DAG, using DirectedCycle class to determine if so
		finder = new DirectedCycle(G); 
		if(!finder.hasCycle() && isRooted == true) {
			return false;
		}
		else {
			return false;
		}
		
	}
	
	// length of shortest ancestral path between v and w
	public int length(int v, int w) {
		
		int length = 0;
		
		BreadthFirstDirectedPaths bfs0 = new BreadthFirstDirectedPaths(Graph, v);
		BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(Graph, w);

		int ancestor = ancestor(v,w);

		for(int i = 0; i < Graph.V(); i++)
			if(bfs0.hasPathTo(ancestor) && bfs1.hasPathTo(ancestor)) {
				length = bfs0.distTo(ancestor) + bfs1.distTo(ancestor);
			}
		return length;
		
	}
	
	// a shortest common ancestor of vertices v and w
	public int ancestor(int v, int w) {
		
		BreadthFirstDirectedPaths bfs0 = new BreadthFirstDirectedPaths(Graph, v);
		BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(Graph, w);
		int ancestor = -1;
		int length = -1;
	
		
		for(int i = 0; i < Graph.V(); i++)
			if(bfs0.hasPathTo(i) && bfs1.hasPathTo(i)) {
				
				int x = bfs0.distTo(i) + bfs1.distTo(i);
				
				if(length == -1) {
					length = x;
				}
				
				if(x < length){
					length = x;
					ancestor = i;
				}
			}
	
		return ancestor;
		
	}
	
	// length of shortest ancestral path of vertex subsets A and B
	public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
		int length = 0;
		
		BreadthFirstDirectedPaths bfs0 = new BreadthFirstDirectedPaths(Graph, subsetA);
		BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(Graph, subsetB);

		int ancestor = ancestor(subsetA,subsetB);

		for(int i = 0; i < Graph.V(); i++)
			if(bfs0.hasPathTo(ancestor) && bfs1.hasPathTo(ancestor)) {
				length = bfs0.distTo(ancestor) + bfs1.distTo(ancestor);
			}
		return length;
		
	}
	
	// a shortest common ancestor of vertex subsets A and B
	public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
		
		BreadthFirstDirectedPaths bfs0 = new BreadthFirstDirectedPaths(Graph, subsetA);
		BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(Graph, subsetB);
		int ancestor = -1;
		int length = -1;
	
		
		for(int i = 0; i < Graph.V(); i++)
			if(bfs0.hasPathTo(i) && bfs1.hasPathTo(i)) {
				
				int x = bfs0.distTo(i) + bfs1.distTo(i);
				
				if(length == -1) {
					length = x;
				}
				
				if(x < length){
					length = x;
					ancestor = i;
				}
			}
	
		return ancestor;
		
	}
	
	//unit testing
	 public static void main(String[] args) {
//		 In in = new In(mySyns.txt);
		 Digraph G = new Digraph(in);
		 
		 ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
		
		 int length = sca.length(5,10);
		 
		 int ancestor = sca.ancestor(5, 10);
		 
		 
		 System.out.println("Length method:" + length);
		 System.out.println("ancestor method: " + ancestor);
		 
	 }
}
