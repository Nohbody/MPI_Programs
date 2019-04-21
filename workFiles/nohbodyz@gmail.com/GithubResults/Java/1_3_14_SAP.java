public class SAP{
private Digraph digraph;
private boolean[] marked;

// constructor takes a digraph (not necessarily a DAG)     
public SAP(Digraph G)     
{
    digraph =new Digraph(G);//.copy();
    marked = new  boolean[G.V()];
}
// length of shortest ancestral path between v and w; -1 if no such path     



public int length(int v, int w)      
{
    if(v>digraph.V()-1||w>digraph.V()-1||v<0||w<0){
        throw new java.lang.IndexOutOfBoundsException();
    }
     BreadthFirstDirectedPaths bfdp = new BreadthFirstDirectedPaths(digraph, w); 
     BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(digraph, v);
   
     int dist=0;
     int ancestor = ancestor(v,w);
     if(ancestor ==-1){
         dist =-1;
     }
    else{
        dist = bfdp.distTo(ancestor)+bfdp1.distTo(ancestor);
    }
     
    return dist;  
}


// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path     
public int ancestor(int v, int w)          
{
        if(v>digraph.V()-1||w>digraph.V()-1||v<0||w<0){
        throw new java.lang.IndexOutOfBoundsException();
    }
           
     BreadthFirstDirectedPaths bfdp = new BreadthFirstDirectedPaths(digraph, w); 
     BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(digraph, v);
   
     int dist=0;
     int ancestor = -1;
     if(v==w) return w;
     
     for(int i =0; i<digraph.V(); i++){
        
         if(bfdp.hasPathTo(i)&&bfdp1.hasPathTo(i))
         {
             int l = bfdp.distTo(i) + bfdp1.distTo(i);
             if(dist==0){dist =l;} 
             if(l<=dist) {
                 dist =l;
                 ancestor = i;
                 
             }
         }
     } 
    return ancestor;
}

// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path 
public int length(Iterable<Integer> v, Iterable<Integer> w)
{
     
     BreadthFirstDirectedPaths bfdp = new BreadthFirstDirectedPaths(digraph, w); 
     BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(digraph, v);
   
     int ancestor = ancestor(v,w);
     int dist =-1;
     if(ancestor!=-1){
       return -1;  
     }
     else{
         dist = bfdp.distTo(ancestor)+bfdp1.distTo(ancestor);
     } 
    return dist;
}
// a common ancestor that participates in shortest ancestral path; -1 if no such path   
 public int ancestor(Iterable<Integer> v, Iterable<Integer> w)     
 {
     BreadthFirstDirectedPaths bfdp = new BreadthFirstDirectedPaths(digraph, w); 
     BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(digraph, v);
   
     int dist=-1;
     int ancestor = -1;
  
     for(int i =0; i<digraph.V(); i++){
         if(bfdp.hasPathTo(i)&&bfdp1.hasPathTo(i))
         {
             int l = bfdp.distTo(i) + bfdp1.distTo(i);
             if(dist==-1) {dist =l;} 
             if(l<dist) {
                 dist =l;
                 ancestor = i;
             }
         }
     } 
    return ancestor;
 }
 
// for unit testing of this class (such as the one below)      
public static void main(String[] args) {
    In in = new In(args[0]);          
    Digraph G = new Digraph(in);          
    SAP sap = new SAP(G);          
    while (!StdIn.isEmpty()) {              
        int v = StdIn.readInt();              
        int w = StdIn.readInt();              
        int length   = sap.length(v, w);              
        int ancestor = sap.ancestor(v, w);              
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor); 
    } 
}
}