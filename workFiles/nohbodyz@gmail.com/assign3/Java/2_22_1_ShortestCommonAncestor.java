/**ShortestCommonAncestor.java
 * Paul Wrobel 11/13/17
 * Description: find the shortest common ancestor from two vertices v and w, calculate length.
 * */
import edu.princeton.cs.algs4.*;

public class ShortestCommonAncestor
{
    private Digraph dag;
    private Queue<Integer> queue; //BFS

    private final int HASHSIZE = 100; //array size of hash table (try for the max length of the graph)
    private Bag<Integer>[] hashTable = new Bag[HASHSIZE]; //to look up a vertex in linear time

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G)
    {
        if(G == null)
            throw new NullPointerException();
        dag = G;

        //is graph a DAG??
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w)
    {
        queue = new Queue(); //queue for breadth-first search
        queue.enqueue(v);

        while(!queue.isEmpty()) //bfs, not necessary for the first iteration
        {
            int temp = queue.dequeue();
            hashTable[temp % HASHSIZE].add(temp); //places each vertex in hash table
            for(int verticy : dag.adj(temp)) //adds each connecting edge to queue
            {
                queue.enqueue(verticy);
            }
        }

        queue.enqueue(w); //insert w into empty queue, same as above but bfs is necessary to find the closest path
        while(!queue.isEmpty())
        {
            int temp = queue.dequeue(); //check vertex
            for(int vertex : hashTable[temp % HASHSIZE]) //search hash table for the vertex
            {
                if(temp == vertex)
                    return directLengthTo(v, temp, 0) + directLengthTo(w, temp, 0); //returns shortest length between v and w
            }
            for(int vertex : dag.adj(temp))
            {
                queue.enqueue(vertex);
            }
        }
        return -1; // if no root is found... shouldnt happen
    }

    private int directLengthTo(int v, int w, int length) //uses depth-first search to calculate distance because it is easy to keep track of the length
    {
        if(v == w) //if ancestor found
            return length;

        int shortest = Integer.MAX_VALUE, temp;

        for(int vertex : dag.adj(v)) //iterate through adjacent vertices
        {
            temp = directLengthTo(vertex, w, length + 1);
            if(temp < shortest)
                shortest = temp;
        }
        return shortest; //return the shortest path
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w)
    {
        queue = new Queue<Integer>(); //queue for breadth-first search
        queue.enqueue(v);

        while(!queue.isEmpty()) //bfs, not necessary for the first iteration
        {
            int temp = queue.dequeue();
            hashTable[temp % 100].add(temp); //places each vertex in hash table
            for(int verticy : dag.adj(temp)) //adds each connecting edge to queue
            {
                queue.enqueue(verticy);
            }
        }

        queue.enqueue(w); //insert w into empty queue, same as above but bfs is necessary to find the closest path
        while(!queue.isEmpty())
        {
            int temp = queue.dequeue();
            for(int vertex : hashTable[temp % 100]) //search hash table for the vertex
            {
                if(temp == vertex)
                    return temp; //returns the closest ancestor
            }
            for(int vertex : dag.adj(temp))
            {
                queue.enqueue(vertex);
            }
        }
        return -1; // if no root is found... shouldnt happen
    }

    // length of shortest ancestral path of vertex subsets A and B
    /*public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB)
    {
        if(subsetA == null || subsetB == null)
            throw new NullPointerException();
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB)
    {
        return ;
    }*/

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in); //??
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