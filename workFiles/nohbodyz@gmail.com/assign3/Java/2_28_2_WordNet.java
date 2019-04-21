/*
* Name: William Behunin
*
* Filename: WordNet.java
*
* Description: This data type builds a WordNet digraph, which is a rooted DAG, storing
* synsets and hypernyms.
*
* Known bugs: Unknown
*/
import java.util.HashMap;
import java.util.Map;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;

public class WordNet {
    //Creates the maps for synsets and nouns
    private final Map<Integer, String> toSynset;
    private final Map<String, Bag<Integer>> nounsToID;
    private final Digraph graph;
    private String del = ","; //Delimeter for the files, using commas


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        toSynset = new HashMap<>();
        nounsToID = new HashMap<>();
        readSynsets(synsets); //Reads the synsets file using readSynsets() void method
        graph = readHypernyms(hypernyms); //Reads the hypernyms using readHypernyms() method
    }
    //Helper method to read the synsets
    private void readSynsets(String synset)
    {
        Bag<Integer> bag;
        In in = new In(synset);
        while (in.hasNextLine()){

            String synsetLine = in.readLine();
            String[] fields = synsetLine.split(del);

            int id = Integer.parseInt(fields[0]);
            synset = fields[1];
            toSynset.put(id, synset); 
            String[] nouns = synset.split(" ");

            for(String noun : nouns){
                if(nounsToID.containsKey(noun)){
                    bag = nounsToID.get(noun);

                    bag.add(new Integer(id));

                    nounsToID.put(noun, bag);
                }
                else{
                    bag = new Bag<Integer>();

                    bag.add(new Integer(id));

                    nounsToID.put(noun, bag);
                }

            }
        }
    }
    //Helper method to read the hypernyms
    private Digraph readHypernyms(String hypernyms)
    {
        Digraph graph = new Digraph(toSynset.size());
        In hypernymFile = new In(hypernyms);
        while (hypernymFile.hasNextLine()) {

            String[] line = hypernymFile.readLine().split(",");

            Integer synID = Integer.valueOf(line[0]);

            for (int i = 1; i < line.length; i++) {

                Integer id = Integer.valueOf(line[i]);
                graph.addEdge(synID, id);
            }
        }

        return graph;
    }
    // all WordNet nouns
    public Iterable<String> nouns() {
        return nounsToID.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nounsToID.containsKey(word);
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {

        if(!isNoun(noun1) || !isNoun(noun2)){
            throw new java.lang.IllegalArgumentException("At least one of the inputs is not a noun.");
        }
        Bag<Integer> bag1 =  nounsToID.get(noun1); //Stores the first noun into a bag of type Integer
        Bag<Integer> bag2 =  nounsToID.get(noun2); //Stores the second noun into a bag of type Integer
        ShortestCommonAncestor sca = new ShortestCommonAncestor(graph);

        return toSynset.get(sca.ancestor(bag1, bag2)); //Obtains the shortest common ancestor between the two nouns
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if (!isNoun(noun1) || !isNoun(noun2)) {
            throw new IllegalArgumentException("At least one of the inputs is not a noun.");
        }
        Bag<Integer> bag1 =  nounsToID.get(noun1);
        Bag<Integer> bag2 =  nounsToID.get(noun2);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(graph);
        //Obtains the length between the two nouns
        return sca.length(bag1, bag2);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int i = 2; i < args.length; i++) {
            In in = new In(args[i]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[i] + ": " + outcast.outcast(nouns));
        }
    }

}