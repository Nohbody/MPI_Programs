import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

import edu.princeton.cs.algs4.Digraph;

public class ShortestCommonAncestor {

    private Digraph scaGraph;
    private int length;
    private BreadthFirstDirectedPaths bfsV, bfsW;
 // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) { 
        
        if (G == null) 
            throw new java.lang.NullPointerException ("Digraph is null");
        
        scaGraph = new Digraph(G);
        length = -1;
        
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) { 
        
        
        
        ancestor(v, w);
        
        return length;
        
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        
        int ancestor = -1, newDistance = -1;
        int nodes = scaGraph.V();
        
        bfsV = new BreadthFirstDirectedPaths(scaGraph, v);
        bfsW = new BreadthFirstDirectedPaths(scaGraph, w);
        
        for(int i = 0; i < nodes; i++) {
            
            if(bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                
                length = bfsV.distTo(i) + bfsW.distTo(i);
                
                if(newDistance == -1){
                    
                    newDistance = length;
                   
                }
               
                
                if(length <= newDistance) {
                    
                    length = newDistance;
                    ancestor = i;
                }
            }
            
        }
        
        return ancestor;
        
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        
        bfsV = new BreadthFirstDirectedPaths(scaGraph, subsetA);
        bfsW = new BreadthFirstDirectedPaths(scaGraph, subsetB);
        
        int ancestor = ancestor(subsetA, subsetB);
        int dist = -1;
        
        if(ancestor != -1){
            
            return -1;
          
        }
        
        else{
            
            dist = bfsV.distTo(ancestor)+bfsW.distTo(ancestor);
            
        } 
       
        return dist;
        
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        
        bfsV = new BreadthFirstDirectedPaths(scaGraph, subsetA);
        bfsW = new BreadthFirstDirectedPaths(scaGraph, subsetB);
        
        int distance = -1;
        int ancestor = -1;
     
        for(int i =0; i<scaGraph.V(); i++){
            
            if(bfsV.hasPathTo(i)&&bfsW.hasPathTo(i)) {
            
                int l = bfsV.distTo(i) + bfsW.distTo(i);
                
                if(distance == -1) {
                    
                    distance = l;
                    
                } 
                
                if(l < distance) {
                    
                    distance = l;
                    ancestor = i;
                    
                }
            }
        } 
        
       return ancestor;
        
    }

    // do unit testing of this class
    public static void main(String[] args) {
        
        In in = new In(args[0]);
        
        Digraph G = new Digraph(in.readInt());
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
