import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.LinkedQueue;

public class ShortestCommonAncestor {
    Digraph G;
    
    private class PathGroup {
        int v, w, ancestor;
    }
    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        this.G = G;
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        int ancestorID = ancestor(v, w);

        LinearProbingHashST<Integer, Integer> path_v_root = pathDistToRoot(v);
        LinearProbingHashST<Integer, Integer> path_w_root = pathDistToRoot(w);

        return (path_v_root.get(ancestorID) + path_w_root.get(ancestorID));
 
    }
    
    // -------------------------------------------------------------------
    // Function: pathDistToRoot
    //
    // This stores a hash table of distances from vertex v.  Sorry, I was
    // trying to avoid BreadthFirstSearch in order to achieve a better 
    // run-time, but I ended up using it in the subset portions
    // -------------------------------------------------------------------
    private LinearProbingHashST<Integer, Integer> pathDistToRoot(int v) {
        LinkedQueue<Integer> paths_to_test = new LinkedQueue<Integer>();
        LinearProbingHashST<Integer, Integer> vertex_dist = new LinearProbingHashST<Integer, Integer>();
        
        paths_to_test.enqueue(v);
        vertex_dist.put(v, 0);
        
        while (!paths_to_test.isEmpty() && G.outdegree(v) != 0) {
            v = paths_to_test.dequeue();
//            StdOut.println("---" + v + "---");
            
            for (int path : G.adj(v)) {                
                if (!vertex_dist.contains(path)) {
                    paths_to_test.enqueue(path);
                    vertex_dist.put(path, vertex_dist.get(v) + 1);
//                    StdOut.print(path + " - ");
                }
            }
//            StdOut.println("\n---END-------\n");
        }
        
        return vertex_dist;
    }
    
    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        int minLength = -1;
        int ancestorID = -1;
        
        LinearProbingHashST<Integer, Integer> path_v_root = pathDistToRoot(v);
        LinearProbingHashST<Integer, Integer> path_w_root = pathDistToRoot(w);
        
        for (int path : path_v_root.keys()) {
            if (path_w_root.contains(path)) {
                int pathDist = path_v_root.get(path) + path_w_root.get(path); 
                
                if (pathDist < minLength || minLength == -1) {
                    minLength = pathDist;
                    ancestorID = path;
                }
                    
            }
        }
        
        return ancestorID;
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        return 0;
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        BreadthFirstDirectedPaths bfs_a = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths bfs_b = new BreadthFirstDirectedPaths(G, subsetA);
        
        int sca = G.V() - 1;
        
        for (int i=0; i < G.V(); i++) {
            if (bfs_a.hasPathTo(i) && bfs_b.hasPathTo(i)) {
                if(bfs_a.distTo(i) < bfs_a.distTo(sca)) {
                    sca = i;
                }
            }
        }
        return sca;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph testG = new Digraph(in);
        
        ShortestCommonAncestor sca = new ShortestCommonAncestor(testG);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

}
