/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rooster
 */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DepthFirstDirectedPaths;

public class ShortestCommonAncestor 
{
   private final Digraph G;
   // constructor takes a rooted DAG as argument
   public ShortestCommonAncestor(Digraph G)
   {
       if(G == null) throw new NullPointerException();
       
       this.G = G;
   }

   // length of shortest ancestral path between v and w
   public int length(int v, int w)
   {
       if(v == w) return 0; //degeneracy handling
       DepthFirstDirectedPaths dfp = new DepthFirstDirectedPaths(G,v);
       Iterable<Integer> path = recursiveL(dfp, G.adj(w));    
       if(path == null) return -1;
       int count = 0; //don't count the starting nodes
       for(int i : path)
       {
           if(i==v || i==w) continue;
           count++;
       }
       dfp = new DepthFirstDirectedPaths(G,w);
       path = recursiveL(dfp, G.adj(v));    
       if(path == null) return -1;
       for(int i : path)
       {
           if(i==v || i==w) continue;
           count++;
       }       
       return count;
   }
   
   private Iterable<Integer> recursiveL(DepthFirstDirectedPaths d, Iterable<Integer> t)
   {
       for(int i : t)
       {
           if(d.hasPathTo(i)) return d.pathTo(i);
           return recursiveL(d,G.adj(i));
       }
       
       return null;
   }

   // a shortest common ancestor of vertices v and w
   public int ancestor(int v, int w)
   {
       if(v==w) return v; //degeneracy handling
       DepthFirstDirectedPaths dfp = new DepthFirstDirectedPaths(G,v);
       int p = recursiveA(dfp, G.adj(w));
       Iterable<Integer> path = dfp.pathTo(p);   
       for(int i : path)
       {
           //Handles a degeneracy
           if(i == w) return recursiveA(new DepthFirstDirectedPaths(G,w), G.adj(v));
       }
       
       return p;
   }
   
   private int recursiveA(DepthFirstDirectedPaths d, Iterable<Integer> t)
   {
       for(int i : t)
       {
           if(d.hasPathTo(i)) return i;
           return recursiveA(d,G.adj(i));
       }
       
       return -1;
   }

   // length of shortest ancestral path of vertex subsets A and B
   public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB)
   {
       if(subsetA == null || subsetB == null) throw new NullPointerException();
       for(Integer i : subsetA) if(i == null) throw new NullPointerException();
       for(Integer i : subsetB) if(i == null) throw new NullPointerException();
       
       
       int temp = Integer.MAX_VALUE;
       
       for(Integer a : subsetA)
       {
           for(Integer b : subsetB)
           {
               int t = length(a,b);
               temp = (temp < t) ? temp : t;
           }
       }
       
       return temp;
   }
   
   // a shortest common ancestor of vertex subsets A and B
   public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB)
   {
       if(subsetA == null || subsetB == null) throw new NullPointerException();
       for(Integer i : subsetA) if(i == null) throw new NullPointerException();
       for(Integer i : subsetB) if(i == null) throw new NullPointerException();
       
       
       int temp = Integer.MAX_VALUE;
       int tempA = 0;
       int tempB = 0;
       
       for(Integer a : subsetA)
       {
           for(Integer b : subsetB)
           {
               int t = length(a,b);
               if(t < temp)
               {
                   temp = t;
                   tempA = a;
                   tempB = b;
               }
           }
       }
       
       return ancestor(tempA,tempB);
   }

   // do unit testing of this class
   public static void main(String[] args)
   {
        edu.princeton.cs.algs4.In in = new edu.princeton.cs.algs4.In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) 
        {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
   }
   
   public boolean isRootedDAG(Digraph d)
   {
       // Check if it is a DAG
       edu.princeton.cs.algs4.DirectedCycleX dc = new edu.princeton.cs.algs4.DirectedCycleX(d);
       if(dc.hasCycle()) return false;
       
       //Check if it is rooted
       int count = 0;
       for(int i=0;i<d.V();i++)
       {
           if(d.outdegree(i) == 0) count++;
       }
       //Count is 1 if and only if the DAG is rooted
       return (count == 1);
   }
}