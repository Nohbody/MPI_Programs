import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import edu.princeton.cs.algs4.*;
import java.lang.*;

public class WordNet {
    private final HashMap<Integer, String> id2synset;
    private final HashMap<String, Bag<Integer>> noun2ids;
    private final Digraph G;
    // constructor takes the name of the two input files

    public WordNet(String synsets, String hypernyms) {
        In in = new In(synsets);
        String delimiter = ",";

        id2synset = new  HashMap<Integer, String>();
        noun2ids =  new HashMap<String, Bag<Integer>>();


        int count = 0;
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

        in = new In(hypernyms);
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

        
        int rooted = 0;//check the root
        for (int i = 0; i < G.V(); i++) {
            if (!this.G.adj(i).iterator().hasNext())
                rooted++;
        }

        if (rooted != 1) {
            throw new IllegalArgumentException("Not a rooted DAG");
        }
    }

    // all WordNet nouns
    public Iterable<String> nouns(){
        return noun2ids.keySet();
    } // is the word a WordNet noun?

    public boolean isNoun(String word) {
        return noun2ids.containsKey(word);
    }
    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        
            if(!isNoun(noun1) || !isNoun(noun2)){
                throw new java.lang.IllegalArgumentException();
            }    
            //Here we probably need to create a SCA
            //get ancestor
            Bag<Integer> bag =  noun2ids.get(noun1);
            Bag<Integer> bag1 =  noun2ids.get(noun2);
            ShortestCommonAncestor sca = new ShortestCommonAncestor(G);

            return id2synset.get(sca.ancestor(bag,bag1));    
        }
    
 // distance between noun1 and noun2 (defined below)
        public int distance(String noun1, String noun2) {
            
                if(!isNoun(noun1) || !isNoun(noun2)){
                    throw new java.lang.IllegalArgumentException();
                }
                ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
                //get id of vertice

                Bag<Integer> bag =  noun2ids.get(noun1);
                Bag<Integer> bag1 =  noun2ids.get(noun2);

                return sca.length(bag,bag1);
            }
        
            // do unit testing of this class
           public static void main(String[] args) {
               WordNet wordnet = new WordNet(args[0], args[1]);
               Outcast outcast = new Outcast(wordnet);
               for (int t = 2; t < args.length; t++) {
                   In in = new In(args[t]);
                   String[] nouns = in.readAllStrings();
                   StdOut.println(args[t] + ": " + outcast.outcast(nouns));
               }
           }



        }

