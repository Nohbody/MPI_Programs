import java.util.HashMap;


public class WordNet {
        //private final SAP paths;
        private final HashMap<Integer, String> id2synset;
        private final HashMap<String, Bag<Integer>> noun2ids;

        private final Digraph G;
// constructor takes the name of the two input files     
public WordNet(String synsets, String _hypernyms)   // the set of nouns (no duplicates), returned as an Iterable  
{
   In in = new In(synsets);
   String delimiter = ",";
  
   id2synset = new  HashMap<Integer, String>();
   noun2ids =  new HashMap<String, Bag<Integer>>();

 
   int count =0;
   //get synsets
   while (in.hasNextLine()){
       //parse 
       String synsetLine = in.readLine();
       String[] fields = synsetLine.split(delimiter);
       //get id 
       int id = Integer.parseInt(fields[0]);
       String synset = fields[1];
       id2synset.put(id,synset); 
       //parsing nouns
       String[] nouns = synset.split(" ");
   
       for(String noun : nouns){
           if(noun2ids.containsKey(noun)){
               Bag <Integer> b = noun2ids.get(noun);
               b.add(new Integer(id));
               noun2ids.put(noun,b);
           }
           else{
               Bag <Integer> b = new Bag<Integer>();
               b.add(new Integer(id));
               noun2ids.put(noun,b);
           }
        
       }
       
       
   }
   
   Digraph digraph = new Digraph(id2synset.size());
   G= digraph;
   
   in = new In(_hypernyms);
   //get hypernynms
    while (in.hasNextLine()){
       //parse 
       String line = in.readLine();
       String[] fields = line.split(delimiter);
       //get id 
       int id = Integer.parseInt(fields[0]);
       Bag<Integer> bag = new Bag<Integer>();
               
       for( int i=1;i<fields.length;i++){
           int f = Integer.parseInt(fields[i]);
           bag.add(new Integer(f)); 
           // I think we should add edges here...
           digraph.addEdge(id, f);
       }
       
       //hypernyms.put(id,bag);
    }
    
    // Check for cycles
                DirectedCycle cycle = new DirectedCycle(this.G);
                if (cycle.hasCycle()) {
                        throw new IllegalArgumentException("Not a valid DAG");
                }

                // Check if not rooted
                int rooted = 0;
                for (int i = 0; i < G.V(); i++) {
                        if (!this.G.adj(i).iterator().hasNext())
                                rooted++;
                }

                if (rooted != 1) {
                        throw new IllegalArgumentException("Not a rooted DAG");
                }
}


public Iterable<String> nouns()   {
    
    
    return noun2ids.keySet();
} 
// is the word a WordNet noun?    
public boolean isNoun(String word)  {

    
    return noun2ids.containsKey(word);
}          



// distance between nounA and nounB (defined below)
public int distance(String nounA, String nounB)    
{
    if(!isNoun(nounA) || !isNoun(nounB)){
    throw new java.lang.IllegalArgumentException();
    }
    SAP sap = new SAP(G);
    //get id of vertice
    
    Bag<Integer> bag =  noun2ids.get(nounA);
    Bag<Integer> bag1 =  noun2ids.get(nounB);
    
    return sap.length(bag,bag1);
}
  
// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB      
// in a shortest ancestral path (defined below)      
public String sap(String nounA, String nounB)      
{
    if(!isNoun(nounA) || !isNoun(nounB)){
        throw new java.lang.IllegalArgumentException();
    }    
//Here we probably need to create a SAP
    //get ancestor
    Bag<Integer> bag =  noun2ids.get(nounA);
    Bag<Integer> bag1 =  noun2ids.get(nounB);
    SAP sap = new SAP(G);
  
    return id2synset.get(sap.ancestor(bag,bag1));    
}


// for unit testing of this class      
public static void main(String[] args) {
                WordNet w = new WordNet("synsets.txt", "hypernyms.txt");
                System.out.println(w.sap("Rameses_the_Great", "Henry_Valentine_Miller")); 
                System.out.println(w.distance("Rameses_the_Great", "Henry_Valentine_Miller")); 
        }



}