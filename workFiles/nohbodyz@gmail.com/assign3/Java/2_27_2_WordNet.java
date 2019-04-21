import java.util.HashMap;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;


public class WordNet {
    private final HashMap<Integer, String> id2synset;
    private final HashMap<String, Bag<Integer>> noun2ids;
    
    private final Digraph G;

    // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms)
   {
       In in = new In(synsets);
       String delimiter = ",";
      
       id2synset = new  HashMap<Integer, String>();
       noun2ids =  new HashMap<String, Bag<Integer>>();

     
       int count = 0;
       while (in.hasNextLine()){
           String synsetLine = in.readLine();
           String[] fields = synsetLine.split(delimiter);
           int id = Integer.parseInt(fields[0]);
           String synset = fields[1];
           id2synset.put(id,synset); 
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
       
       in = new In(hypernyms);
        while (in.hasNextLine()){ 
           String line = in.readLine();
           String[] fields = line.split(delimiter);
           int id = Integer.parseInt(fields[0]);
           Bag<Integer> bag = new Bag<Integer>();
                   
           for( int i=1;i<fields.length;i++){
               int f = Integer.parseInt(fields[i]);
               bag.add(new Integer(f)); 
               digraph.addEdge(id, f);
           }
           
           
        }

    }
 


   // all WordNet nouns
   public Iterable<String> nouns()
   {
       return noun2ids.keySet();
   }
   
   // is the word a WordNet noun?
   public boolean isNoun(String word)
   {
       return noun2ids.containsKey(word);
   }

   // a synset (second field of synsets.txt) that is a shortest common ancestor
   // of noun1 and noun2 (defined below)
   public String sca(String noun1, String noun2)
   {
       if(!isNoun(noun1) || !isNoun(noun2)){
           throw new java.lang.IllegalArgumentException();
       }    

       Bag<Integer> bag =  noun2ids.get(noun1);
       Bag<Integer> bag1 =  noun2ids.get(noun2);
       SCA sap = new SCA(G);
     
       return id2synset.get(sap.ancestor(bag,bag1));    
   }

   
   // distance between noun1 and noun2 (defined below)
   public int distance(String noun1, String noun2)
   {
       if(!isNoun(noun1) || !isNoun(noun2)){
           throw new java.lang.IllegalArgumentException();
           }
           SCA sap = new SCA(G);
           
           Bag<Integer> bag =  noun2ids.get(noun1);
           Bag<Integer> bag1 =  noun2ids.get(noun2);
           
           return sap.length(bag,bag1);
   }

   // do unit testing of this class
   public static void main(String[] args)
   {
       long start = System.nanoTime();
       WordNet wordnet = new WordNet(args[0], args[1]);
       Outcast outcast = new Outcast(wordnet);

       for (int t = 2; t < args.length; t++) {
           In in = new In(args[t]);
           String[] nouns = in.readAllStrings();
           StdOut.println(args[t] + ": " + outcast.outcast(nouns));
       }
       
   }
}