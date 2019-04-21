import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Stack;

public class ShortestCommonAncestor {
    
    Digraph G;
    
    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        this.G = new Digraph(G);
        
        
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        BreadthFirstDirectedPaths a = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths b = new BreadthFirstDirectedPaths(G, w);
        
        Stack<Integer> ancestors = new Stack<Integer>();
        
        for(int i = 0; i < G.V(); i++) {
            if(a.hasPathTo(i) && b.hasPathTo(i)) {
                ancestors.push(i);
            }
        }
        int minLength = G.V();
        while(!ancestors.isEmpty()) {
            int current = ancestors.pop();
            int dist = a.distTo(current) + b.distTo(current);
            if(dist < minLength) minLength = dist;
            
        }
        
        return minLength;
       
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        
        BreadthFirstDirectedPaths a = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths b = new BreadthFirstDirectedPaths(G, w);
        
        Stack<Integer> ancestors = new Stack<Integer>();
        
        for(int i = 0; i < G.V(); i++) {
            if(a.hasPathTo(i) && b.hasPathTo(i)) {
                ancestors.push(i);
            }
        }
        int minLength = G.V();
        int sca = 0;
        while(!ancestors.isEmpty()) {
            int current = ancestors.pop();
            int dist = a.distTo(current) + b.distTo(current);
            if(dist < minLength) {
                minLength = dist;
                sca = current;
            }
            
        }
        
        return sca;
        
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        BreadthFirstDirectedPaths a = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths b = new BreadthFirstDirectedPaths(G, subsetB);
        
        Stack<Integer> ancestors = new Stack<Integer>();
        
        for(int i = 0; i < G.V(); i++) {
            if(a.hasPathTo(i) && b.hasPathTo(i)) {
                ancestors.push(i);
            }
        }
        int minLength = G.V();
        while(!ancestors.isEmpty()) {
            int current = ancestors.pop();
            int dist = a.distTo(current) + b.distTo(current);
            if(dist < minLength) minLength = dist;
            
        }
        
        return minLength;
        
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        BreadthFirstDirectedPaths a = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths b = new BreadthFirstDirectedPaths(G, subsetB);
        
        Stack<Integer> ancestors = new Stack<Integer>();
        
        for(int i = 0; i < G.V(); i++) {
            if(a.hasPathTo(i) && b.hasPathTo(i)) {
                ancestors.push(i);
            }
        }
        int minLength = G.V();
        int sca = 0;
        while(!ancestors.isEmpty()) {
            int current = ancestors.pop();
            int dist = a.distTo(current) + b.distTo(current);
            if(dist < minLength) {
                minLength = dist;
                sca = current;
            }
            
        }
        
        return sca;
    }
    
    

}
