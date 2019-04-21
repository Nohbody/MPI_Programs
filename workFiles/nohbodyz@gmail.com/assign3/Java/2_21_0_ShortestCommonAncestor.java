import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;


public class ShortestCommonAncestor {
    
    Digraph DAG;
    int root;
 // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        DAG = G;
        for(int i = 0; i<DAG.V();i++) {
            if(DAG.outdegree(i)==0) {
                root = i;
                break;
            }
        }
       
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        BreadthFirstDirectedPaths vpath = new BreadthFirstDirectedPaths(DAG, v);
        BreadthFirstDirectedPaths wpath = new BreadthFirstDirectedPaths(DAG, w);
        int Anc = ancestor(v,w);
        int LSAC = vpath.distTo(Anc) + wpath.distTo(Anc);
        return LSAC;
        
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths vpath = new BreadthFirstDirectedPaths(DAG, v);
        BreadthFirstDirectedPaths wpath = new BreadthFirstDirectedPaths(DAG, w);
        for(int i: vpath.pathTo(root)) {
            for(int j: wpath.pathTo(root)) {                
                if(i == j) return i;
            }
        }
        return 0;
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        BreadthFirstDirectedPaths vpath = new BreadthFirstDirectedPaths(DAG, subsetA);
        BreadthFirstDirectedPaths wpath = new BreadthFirstDirectedPaths(DAG, subsetB);
        int Anc = ancestor(subsetA,subsetB);
        int LSAC = vpath.distTo(Anc) + wpath.distTo(Anc);
        return LSAC;
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        BreadthFirstDirectedPaths vpath = new BreadthFirstDirectedPaths(DAG, subsetA);
        BreadthFirstDirectedPaths wpath = new BreadthFirstDirectedPaths(DAG, subsetB);
        for(int i: vpath.pathTo(root)) {
            for(int j: wpath.pathTo(root)) {                
                if(i == j) return j;
            }
        }
        return 0;
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
