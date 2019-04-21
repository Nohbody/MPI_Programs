import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;


public class ShortestAncestor
{
	private Digraph digraph;

	public ShortestAncestor(Digraph G)     
	{
		digraph = new Digraph(G);
	}
	
	
	
	public int length(int v, int w)      
	{
		if(v > digraph.V() -1 || w > digraph.V() -1 || v < 0 || w < 0)
		{
			throw new java.lang.IndexOutOfBoundsException();
		}
		BreadthFirstDirectedPaths breadthPath = new BreadthFirstDirectedPaths(digraph, w); 
		BreadthFirstDirectedPaths breadthPath1 = new BreadthFirstDirectedPaths(digraph, v);

		int distance = 0;
		int ancestor = ancestor(v,w);
		if(ancestor == -1)
		{
			distance = -1;
		}
		else
		{
			distance = breadthPath.distTo(ancestor)+breadthPath1.distTo(ancestor);
		}

		return distance;  
	}

	public int ancestor(int v, int w)          
	{
		if(v > digraph.V() -1 ||w > digraph.V() -1 || v < 0 || w < 0)
		{
			throw new java.lang.IndexOutOfBoundsException();
		}

		BreadthFirstDirectedPaths breadthPath = new BreadthFirstDirectedPaths(digraph, w); 
		BreadthFirstDirectedPaths breadthPath1 = new BreadthFirstDirectedPaths(digraph, v);

		int distance = 0;
		int ancestor = -1;
		if(v == w) 
		{
			return w;
		}

		for(int i =0; i < digraph.V(); i++)
		{

			if(breadthPath.hasPathTo(i)&&breadthPath1.hasPathTo(i))
			{
				int l = breadthPath.distTo(i) + breadthPath1.distTo(i);
				if(distance == 0)
				{
					distance = l;
				} 
				if(l<=distance) {
					distance =l;
					ancestor = i;

				}
			}
		} 
		return ancestor;
	}

	public int length(Iterable<Integer> v, Iterable<Integer> w)
	{

		BreadthFirstDirectedPaths breadthPath = new BreadthFirstDirectedPaths(digraph, w); 
		BreadthFirstDirectedPaths breadthPath1 = new BreadthFirstDirectedPaths(digraph, v);

		int ancestor = ancestor(v,w);
		int distance = -1;
		if(ancestor != -1)
		{
			return -1;  
		}
		else
		{
			distance = breadthPath.distTo(ancestor)+breadthPath1.distTo(ancestor);
		} 
		return distance;
	}
	
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w)     
	{
		BreadthFirstDirectedPaths breadthPath = new BreadthFirstDirectedPaths(digraph, w); 
		BreadthFirstDirectedPaths breadthPath1 = new BreadthFirstDirectedPaths(digraph, v);

		int distance = -1;
		int ancestor = -1;

		for(int i = 0; i < digraph.V(); i++)
		{
			if(breadthPath.hasPathTo(i) && breadthPath1.hasPathTo(i))
			{
				int length = breadthPath.distTo(i) + breadthPath1.distTo(i);
				if(distance == -1) 
				{
					distance = length;
				} 
				if(length < distance) 
				{
					distance = length;
					ancestor = i;
				}
			}
		} 
		return ancestor;
	}
	
	public static void main(String[] args) {
	    In in = new In(args[0]);
	    Digraph G = new Digraph(in);
	    ShortestAncestor sc = new ShortestAncestor(G);
	    while (!StdIn.isEmpty()) {
	        int v = StdIn.readInt();
	        int w = StdIn.readInt();
	        int length   = sc.length(v, w);
	        int ancestor = sc.ancestor(v, w);
	        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	    }
	}
}