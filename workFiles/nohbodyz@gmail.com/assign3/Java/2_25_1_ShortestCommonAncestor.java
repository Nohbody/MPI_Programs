import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.DepthFirstDirectedPaths;
import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class ShortestCommonAncestor {
	
	private Digraph graph;

	 // constructor takes a rooted directedAcyclicGraph as argument
    public ShortestCommonAncestor(Digraph G){
    	if (G == null) throw new NullPointerException("Digraph G can't be empty");
	    graph = new Digraph(G);
    }

    //Is it a directed acyclic graph?
    public boolean isdirectedAcyclicGraph(){
    		    return !new DirectedCycle(graph).hasCycle();
    }

    
    //Is the digraph a rooted directedAcyclicGraph?
    public boolean isRooteddirectedAcyclicGraph(){
    	
	    // Checks if graph is a directedAcyclicGraph
	    if(!isdirectedAcyclicGraph()) return false;
	   
	    // Finds the  'root' to use
	    DepthFirstOrder dfo = new DepthFirstOrder(this.graph);
	    Integer rootVertex = dfo.post().iterator().next();
	   
	    // Checks if all vertices have a path to the root
	    DepthFirstDirectedPaths dfdp = new DepthFirstDirectedPaths(graph.reverse(), rootVertex);

	    //will come back false if no path
	    for(int i=0; i<graph.V(); i++){
	    	
		    if(!dfdp.hasPathTo(i)) return false;
	    }
	   
	    return true;
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w){
    	
    	Stack<Integer> vStack = new Stack<>();
    	vStack.push(v);
    	
    	Stack<Integer> wStack = new Stack<>();
    	wStack.push(w);
    	
	    return ancestorAndLength(vStack, wStack)[1];
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w){
    	Stack<Integer> vStack = new Stack<>(
    			);
    	vStack.push(v);
    	
    	Stack<Integer> wStack = new Stack<>();
    	wStack.push(w);
    	
	    return ancestorAndLength(vStack, wStack)[0];
    }
   
    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> v, Iterable<Integer> w){
	    return ancestorAndLength(v, w)[1];
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
	    return ancestorAndLength(v, w)[0];
    }

    private int[] ancestorAndLength(Iterable<Integer> v, Iterable<Integer> w){
	    
    	// BFS to find the paths
	    BreadthFirstDirectedPaths vPaths = new BreadthFirstDirectedPaths(graph, v);
	    BreadthFirstDirectedPaths wPaths = new BreadthFirstDirectedPaths(graph, w);
	   
	    // DepthFirstOrder in a graph
	    DepthFirstOrder DFO = new DepthFirstOrder(graph);
	        
	    // Closest ancestor and length
	    int ancestor = -1;
	    int length = -1;
	   
	    // Starts with revserse order for the DFO
	    for(int i: DFO.reversePost()){
	    	
		    if(vPaths.hasPathTo(i) && wPaths.hasPathTo(i)){
			    int currentLength = vPaths.distTo(i) + wPaths.distTo(i);
			   
			    // See if it's closer than the current
			    if(currentLength < length || ancestor == -1){
				    ancestor = i;
				    length = currentLength;
			    }else break;
		    }
	    }
	   
	    // array return with ancestor and length 
	    int[] ancestorAndLength = {ancestor, length};
	    return ancestorAndLength;
    }


    // do unit testing of this class
    public static void main(String[] args){
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
