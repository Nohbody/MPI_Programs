//==========================
//Karsten Pease ShortestCommonAncestor.java
//
//This is to find the shortest common ancestor in a tree 
//==========================

import edu.princeton.cs.algs4.BreadthFirstPaths;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class ShortestCommonAncestor {
    //Many methods have been reused on this page beacause they all act very similar. 
    Graph G;
    BreadthFirstPaths[] BFS;
    int ancestor;
    
    
    public ShortestCommonAncestor(Graph g){
      // pass in the digraph and assigen it DG. 
        G = g;
        BFS = new BreadthFirstPaths[G.V()];
     //The digraph with its number of vertices is .V
    }
    
    public int length(int v, int w){
        ErrorCheck(v);
        ErrorCheck(w);
        //methods to check and see if they are able to work. 
        
        if(BFS[v] == null){
            BFS[v] = new BreadthFirstPaths(G,v);
        }
        if(BFS[w] == null){
            BFS[w] = new BreadthFirstPaths(G,w);
        }
        //If the vertices on the specific BFS graph is empty make a new one.  
       
        int size = Integer.MAX_VALUE; // biggest value that an integer can hold.
        for (int i =0; i <G.V(); i++){
            if(BFS[v].hasPathTo(i) && BFS[w].hasPathTo(i)){
                int temp = BFS[v].distTo(i) + BFS[w].distTo(i);
                if(temp < size){
                    size = temp;
                }
            }
        }
       
        //The above area says while the size of the Digraph vertices is being iterated through
        //As long as there is a path for the specific spot and w or v then the temporary varable 
        // is increased by the distance from the specific spot of w and v combined together;

       return size; 
    }
    
    public int ancestor(int v, int w){
       
        ErrorCheck(v);
        ErrorCheck(w);
        if(BFS[v] == null){
            BFS[v] = new BreadthFirstPaths(G,v);
        }
        if(BFS[w] == null){
            BFS[w] = new BreadthFirstPaths(G,w);
        }
        
        //copied method from top. 
        
        int size = Integer.MAX_VALUE;
        ancestor =-1; //do this so it starts at 0, like array counting. 
        for (int i =0; i <G.V(); i++){
            if(BFS[v].hasPathTo(i) && BFS[w].hasPathTo(i)){
                int temp = BFS[v].distTo(i) + BFS[w].distTo(i);
                if(temp < size){
                    size = temp;
                    ancestor =i;
                }
            }
        }
        // this will be almost the same method as about be it will be dealing with where the location of teh specific 
        // ancestor is and returns that. 
        return ancestor;
    }
    
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB){
        if(subsetA == null ){
            throw new NullPointerException();
        }
        if(subsetB == null){
            throw new NullPointerException();
        }
        int size = Integer.MAX_VALUE;

        for(int i : subsetA){
            for(int j : subsetB){
                int temp = length(i,j);
                if(temp != -1 && temp < size){
                    size = temp;
                }
            }
        }
         
        return size;
    }
    
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB){
        if(subsetA == null ){
            throw new NullPointerException();
        }
        if(subsetB == null){
            throw new NullPointerException();
        }
        
        int size = Integer.MAX_VALUE;
        for(int i : subsetA){
            for(int j : subsetB){  
                int temp = length(i,j);
                if(temp != -1 && temp < size){
                    size = temp;
                    ancestor = ancestor(i,j);
                }
            }
        }
        
        return ancestor;
    }
    
    public void ErrorCheck(int v){
        if(v < 0){
            throw new IndexOutOfBoundsException("Vertex out of bounds.");
        }
        if(v > G.V() -1 ){
            throw new IndexOutOfBoundsException("Vertex out of bounds.");
            //saving room and making sure that the verticies is bigger than 0 and not in the negatives.
            // also helped clean up code a bit. 
        }
    }
  
    
   
    public static void main(String[] args) {
  //I have it like this because I am lazy and can run it from eclips. 
        In in = new In("./digraph1.txt");
        Graph G = new Graph(in);
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
