
import java.util.Iterator;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.In;

public class ShortestCommonAncestor {
    RedBlackBST<Integer, Integer> tree;
    Digraph G;
    
    public ShortestCommonAncestor(Digraph G) {
        this.G = G;
        
        tree = new RedBlackBST<Integer, Integer>();
        for (int i=0; i<G.V();i++) {
            tree.put(i,i);
            Iterable<Integer> adj = G.adj(i);
            for (Integer a: adj)
                tree.put(a, a);
        }
    }
    
    public int length(int v, int w)
    {
        int ancestor = ancestor(v,w);
        return Math.min(new BreadthFirstDirectedPaths(G, v).distTo(w), new 
                BreadthFirstDirectedPaths(G, w).distTo(ancestor));
    }
    
 // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null)
            throw new NullPointerException();
        if (!subsetA.iterator().hasNext() || !subsetB.iterator().hasNext()) 
            throw new IllegalArgumentException();
        Iterator<Integer> ita = subsetA.iterator();
        Iterator<Integer> itb = subsetA.iterator();
        int min = Integer.MAX_VALUE;
        while (ita.hasNext())
        {
            Integer x = ita.next();
            while(itb.hasNext())
            {
                if (x==null || itb==null) 
                    throw new IllegalArgumentException();
               int length = length(x,itb.next());
                 if (length < min)
                    min = length;
            }
        }
        return min;
        }
    

    
 // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w)
    {    
        return ancestor(new BreadthFirstDirectedPaths(G,v),w );
    }
    
    private int ancestor(BreadthFirstDirectedPaths bfs, int w) 
    {
        if (bfs.hasPathTo(w))
            return w;
        for (Integer s: G.adj(w))
           return ancestor(bfs,s);
        throw new RuntimeException("Graph is not rooted DmG.");
    }
    
 // a shortest common ancestor of vertex subsets A and B
    public int  ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB)
    {
        if (subsetA == null || subsetB == null)
            throw new NullPointerException();
        if (!subsetA.iterator().hasNext() || !subsetB.iterator().hasNext()) 
            throw new IllegalArgumentException();
        Iterator<Integer> ita = subsetA.iterator();
        Iterator<Integer> itb = subsetA.iterator();
        int min = Integer.MAX_VALUE;
        int ancestor = 0;
        while (ita.hasNext())
        {
            Integer a = ita.next();
            while(itb.hasNext())
            { 
                Integer b = itb.next();
                if (a==null || b==null) 
                    throw new IllegalArgumentException();
                int x = ancestor(a,b);
                int length = length(a,x) + length(b,x);
                if (length < min)
                    min = length;
                    ancestor = x;
            }       
         }
        return ancestor;
        }
        
    




    public static void main(String[] args) {
        In in = new In("digraph1.txt");
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
