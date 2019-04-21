import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.DepthFirstDirectedPaths;
import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;


public class SCA {
    public Digraph g;
    // constructor takes a rooted DAG as argument
    public SCA(Digraph G) {
        
            this.g = new Digraph(G);
    } 
    
    public boolean isDAG(){
        return !new DirectedCycle(g).hasCycle();
    }
    
    public boolean isRootedDAG(){
        if(!isDAG()) return false;
        long start = System.nanoTime();
        DepthFirstOrder dfo = new DepthFirstOrder(this.g);
        Integer rootVertex = dfo.post().iterator().next();
       
        
        DepthFirstDirectedPaths dfdp = new DepthFirstDirectedPaths(g.reverse(), rootVertex);
        for(int i=0; i<g.V(); i++){
            if(!dfdp.hasPathTo(i)) return false;
        }
        long end = System.nanoTime();
        System.out.println("Duration: " + (end - start));
        return true;
    }
    
    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
    long start = System.nanoTime();
    BreadthFirstDirectedPaths searchV = new BreadthFirstDirectedPaths(g, v);
    BreadthFirstDirectedPaths searchW = new BreadthFirstDirectedPaths(g, w);

    int shortest = -1;
    for (int s = 0; s < g.V(); ++s) {
        if (searchV.hasPathTo(s) && searchW.hasPathTo(s)) {
            int dist = searchV.distTo(s) + searchW.distTo(s);
            if (shortest == -1 || dist < shortest) {
                shortest = dist;
            }
        }
    }
    long end = System.nanoTime();
    System.out.println("Duration: " + (end - start));
    return shortest;
    }


    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
    long start = System.nanoTime();

    BreadthFirstDirectedPaths searchV = new BreadthFirstDirectedPaths(g, v);
    BreadthFirstDirectedPaths searchW = new BreadthFirstDirectedPaths(g, w);

    int shortest = Integer.MAX_VALUE;
    int ancestor = -1;
    for (int s = 0; s < g.V(); ++s) {
        if (searchV.hasPathTo(s) && searchW.hasPathTo(s)) {
            int dist = searchV.distTo(s) + searchW.distTo(s);
            if (dist < shortest) {
                shortest = dist;
                ancestor = s;
            }
        }
    }
    long end = System.nanoTime();
    System.out.println("Duration: " + (end - start));
    return ancestor;
    }
    
    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        long start = System.nanoTime();

        BreadthFirstDirectedPaths searchA = new BreadthFirstDirectedPaths(g, subsetA);
        BreadthFirstDirectedPaths searchB = new BreadthFirstDirectedPaths(g, subsetB);

        int shortest = -1;
        for (int s = 0; s < g.V(); ++s) {
        if (searchA.hasPathTo(s) && searchB.hasPathTo(s)) {
            int dist = searchA.distTo(s) + searchB.distTo(s);
            if (shortest == -1 || dist < shortest) {
                shortest = dist;
            }
        }
    }
    long end = System.nanoTime();
    System.out.println("Duration: " + (end - start));
    return shortest;
    }
    
    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        long start = System.nanoTime();
        BreadthFirstDirectedPaths searchA = new BreadthFirstDirectedPaths(g, subsetA);
        BreadthFirstDirectedPaths searchB = new BreadthFirstDirectedPaths(g, subsetB);

        int shortest = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int s = 0; s < g.V(); ++s) {
            if (searchA.hasPathTo(s) && searchB.hasPathTo(s)) {
                int dist = searchA.distTo(s) + searchB.distTo(s);
                if (dist < shortest) {
                    shortest = dist;
                    ancestor = s;
                }
            }
        }
    long end = System.nanoTime();
    System.out.println("Duration: " + (end - start));
    return ancestor;
    }
    
    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph g = new Digraph(in);
        SCA sca = new SCA(g);
        
        while (!StdIn.isEmpty()) {
            System.out.println("Started");
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            System.out.println(v + "     " + w);
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
  
}