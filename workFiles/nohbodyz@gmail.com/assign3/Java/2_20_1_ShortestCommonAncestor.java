import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class ShortestCommonAncestor {

		Digraph dg;
	   // constructor takes a rooted DAG as argument
	   public ShortestCommonAncestor(Digraph G)
	   {
		   this.dg = new  Digraph(G);
	   }

	   // length of shortest ancestral path between v and w
	   public int length(int v, int w)
	   {

			HashMap <Integer,Integer>visited = new HashMap<Integer,Integer>();
		   
	        // Create a queue for BFS
	        LinkedList<Integer> queue = new LinkedList<Integer>();
	 
	        // Mark the current node as visited and enqueue it
	        visited.put(v,0);
	        queue.add(v);
	 
	        while (queue.size() != 0)
	        {
	            // Dequeue a vertex from queue and print it
	            v= queue.poll();
	            //System.out.print(v+" a");
	 
	            // Get all adjacent vertices of the dequeued vertex s
	            // If a adjacent has not been visited, then mark it
	            // visited and enqueue it
	            Iterator<Integer> i = dg.adj(v).iterator();
	            while (i.hasNext())
	            {
	                int n = i.next();
		            //System.out.print(n+" al");
	                int lvl = visited.get(v);
	                if (!visited.containsKey(n))
	                {
	                    visited.put(n, lvl+1);
	                    queue.add(n);
	                }
	            }
	        }

	        if(visited.containsKey(w))
	        {
	        	//System.out.println("here");
	        	return visited.get(w) ;
	        }
	       	visited.put(w,0);
	        queue.add(w);

           //System.out.print(w+" ab");
	        while (queue.size() != 0)
	        {
	            // Dequeue a vertex from queue and print it
	            w= queue.poll();
	            //System.out.print(w+" ");

	            // Get all adjacent vertices of the dequeued vertex s
	            // If a adjacent has not been visited, then mark it
	            // visited and enqueue it

               int lvl = visited.get(w);
	            Iterator<Integer> i = dg.adj(w).iterator();
	            while (i.hasNext())
	            {
	                int n = i.next();
	                if (visited.containsKey(n))
	                {
	                	return visited.get(n) + lvl + 1 ;
	                }
	                else
	                {
	                    visited.put(n, lvl+1);
	                    queue.add(n);
	                }
	            }
	        }
	        return -1;
	   }
	   // a shortest common ancestor of vertices v and w
	   public int ancestor(int v, int w)
	   {

			HashMap <Integer,Integer>visited = new HashMap<Integer,Integer>();
		   
	        // Create a queue for BFS
	        LinkedList<Integer> queue = new LinkedList<Integer>();
	 
	        // Mark the current node as visited and enqueue it
	        visited.put(v,0);
	        queue.add(v);
	 
	        while (queue.size() != 0)
	        {
	            // Dequeue a vertex from queue and print it
	            v= queue.poll();
	            //System.out.print(v+" a");
	 
	            // Get all adjacent vertices of the dequeued vertex s
	            // If a adjacent has not been visited, then mark it
	            // visited and enqueue it
	            Iterator<Integer> i = dg.adj(v).iterator();
	            while (i.hasNext())
	            {
	                int n = i.next();
		            //System.out.print(n+" al");
	                int lvl = visited.get(v);
	                if (!visited.containsKey(n))
	                {
	                    visited.put(n, lvl+1);
	                    queue.add(n);
	                }
	            }
	        }

	        if(visited.containsKey(w))
	        {
	        	//System.out.println("here");
	        	return w;
	        }
	       	visited.put(w,0);
	        queue.add(w);

            //System.out.print(w+" ab");
	        while (queue.size() != 0)
	        {
	            // Dequeue a vertex from queue and print it
	            w= queue.poll();
	            //System.out.print(w+" ");

	            // Get all adjacent vertices of the dequeued vertex s
	            // If a adjacent has not been visited, then mark it
	            // visited and enqueue it

                int lvl = visited.get(w);
	            Iterator<Integer> i = dg.adj(w).iterator();
	            while (i.hasNext())
	            {
	                int n = i.next();
	                if (visited.containsKey(n))
	                {
	                	return n;
	                }
	                else
	                {
	                    visited.put(n, lvl+1);
	                    queue.add(n);
	                }
	            }
	        }
	        return -1;
	   }
	   // length of shortest ancestral path of vertex subsets A and B
	   public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB)
	   {
		   int length =-1;
           Iterator<Integer> i = subsetA.iterator();
           while (i.hasNext())
           {
               int n = i.next();
               Iterator<Integer> j = subsetB.iterator();
               while (j.hasNext())
               {
                   int m = j.next();
                   if(length == -1)
                	   length = length(n,m);
                   else
                   {
                	   length = Math.min(length,length(n,m));
                   }
               }
           }
           return length;
           
	   }
	   // a shortest common ancestor of vertex subsets A and B
	   public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB)
	   {
		   int length =-1,n_final=-1,m_final=-1;
           Iterator<Integer> i = subsetA.iterator();
           while (i.hasNext())
           {
               int n = i.next();
               Iterator<Integer> j = subsetB.iterator();
               while (j.hasNext())
               {
                   int m = j.next();
                   if(length == -1)
                   {
                	   length = length(n,m);
                	   n_final =n;
                	   m_final = m;
                   }
                   else
                   {
                	   length = Math.min(length,length(n,m));
                	   n_final =n;
                	   m_final = m;
                   }
               }
           }
           return ancestor(n_final,m_final);
	   }
	   // do unit testing of this class
	   public static void main(String[] args)
	   {
		   In in = new In("dig.txt");
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