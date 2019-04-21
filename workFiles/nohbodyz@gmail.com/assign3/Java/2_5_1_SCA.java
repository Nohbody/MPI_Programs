import java.util.ArrayDeque;
import java.util.Deque;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph; 
 
public class SCA {

    private Digraph G; 
    
    public SCA(Digraph G) {
        this.G = new Digraph(G);
    }
        
    private boolean isValid(int v) {
        return (v >= 0 && v <= this.G.V() - 1);
    }
    
    private boolean isValid(Iterable<Integer> v, Iterable<Integer> w) {

        for (Integer integer : w) {
            if (!isValid(integer)) {
                return false;
            }
        }

        for (Integer integer : v) {
            if (!isValid(integer)) {
                return false;
            }
        }

        return true;
    }


    public int length(int Alpha, int Bravo) {

        

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, Alpha); //Breadth First Directed Path finds the shortest path from one vertex to every other vertex in the tree
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, Bravo);

        int ancestor = ancestor(Alpha, Bravo); //finds the closets common ancestor between two vertices 
        int pathLength;
        if (ancestor == - 1) {//if there is no path
            pathLength = -1;//return false
        } else {
            pathLength = bfsV.distTo(ancestor) + bfsW.distTo(ancestor);//Find the distance from each value to their common ancestor and add them together. 
        }

        return pathLength;
    }



    public int ancestor(int Charlie, int Delta) {

        
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, Charlie);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, Delta);
        int Alpha = -1; //if there is no shortest ancestor, return false
        int Bravo = 10000; //set this to a high number that the paths can't reach.
        Deque<Integer> ancestors = new ArrayDeque<Integer>(); //create a deque to store the path

        for (int i = 0; i < this.G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) { //if they both have a path to an ancestor, 
                ancestors.push(i); //push the ancestor onto the deque
            }
        }

        for (Integer integer : ancestors) { //for each ancestor
            if ((bfsV.distTo(integer) + bfsW.distTo(integer)) < Bravo) { //if this ancestor is closer than the others
                Alpha = integer; //this is the shortest ancestor
               	Bravo = (bfsV.distTo(integer) + bfsW.distTo(integer));//make it the shortest ancestor so far, 
            }
        }
        return Alpha;//return the closest ancestor 
    }

    // This code is the same as above, it is just iterable
    public int length(Iterable<Integer> v, Iterable<Integer> w) {

        if (!isValid(v, w)) {
            throw new IndexOutOfBoundsException();
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        int ancestor = ancestor(v, w);
        int pathLength;
        if (ancestor == - 1) {
            pathLength = -1;
        } else {
            pathLength = bfsV.distTo(ancestor) + bfsW.distTo(ancestor); 
        }


        return pathLength;

    }


    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int Bravo = 10000;
        Deque<Integer> ancestors = new ArrayDeque<Integer>();
        int Alpha = -1;

        if (!isValid(v, w)) {
            throw new IndexOutOfBoundsException();
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        for (int i = 0; i < this.G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                ancestors.push(i);
            }
        }

        for (Integer integer : ancestors) {
            if ((bfsV.distTo(integer) + bfsW.distTo(integer)) < Bravo) {
                Bravo = (bfsV.distTo(integer) + bfsW.distTo(integer));
                Alpha = integer;
            }
        }
        return Alpha;

    }
 

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SCA sca = new SCA(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
