
import java.util.LinkedList;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.In;


public class ShortestCommonAncestor {
    
    private Digraph DAG;
    private BreadthFirstDirectedPaths BreadthSearch;
    
    private LinkedList Vpaths;
    private LinkedList Wpaths;
    
    private MaxPQ Vheap;
    private MaxPQ Wheap;
    
    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G){
        this.DAG = G;
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w){
        int distance =0;
        
        
        BreadthSearch = new BreadthFirstDirectedPaths(DAG, ancestor(v,w));
        distance += BreadthSearch.distTo(v);
        distance += BreadthSearch.distTo(w);
        return distance;
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w){
        Iterable<Integer> pathToV;
        Iterable<Integer> pathToW;
        
        
        BreadthSearch = new BreadthFirstDirectedPaths(DAG, 0);
        pathToV = BreadthSearch.pathTo(v);
        pathToW = BreadthSearch.pathTo(w);
        
        Vheap = new MaxPQ();
        Wheap = new MaxPQ();
        
        
        while(pathToV.iterator().hasNext()){
            Vheap.insert((Object) pathToV.iterator().next());
            Wheap.insert((Object) pathToW.iterator().next());
        }
        
        while(!Vheap.isEmpty() && !Wheap.isEmpty()){
            if(Vheap.max() != Wheap.max()){
                return (int) Vheap.delMax();
            }
        }
        return -1;
       
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB){
        return 0;
        //ran out of time :(
        
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB){
        BreadthSearch = new BreadthFirstDirectedPaths(DAG, subsetA);
      //ran out of time :(
        return 0;
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
