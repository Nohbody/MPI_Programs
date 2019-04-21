/**
 * Created by zora on 3/27/16.
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Bag;

public class WordNet {
    private Digraph diGraph;
    // word::IDs symbol table
    private ST<String, Bag<Integer> > synsetsWordIndex;
    // ID::word_string symbol table
    private ST<Integer, String > synsetsIDIndex;
    // Shortest ancestor path class
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In inSynsets = new In(synsets);
        In inHypernyms = new In(hypernyms);

        this.synsetsWordIndex = new ST<>();
        this.synsetsIDIndex = new ST<>();

        // Instantiate synsetsWordIndex and synsetsIDIndex from Synsets file
        while (inSynsets.hasNextLine()) {
            String[] splitSynsets = inSynsets.readLine().split(",");

            // Create synsetsWordIndex bag values & generate <Key, Value> pair
            for (String noun : splitSynsets[1].split(" ")) {
                Bag<Integer> id = new Bag<Integer>();
                if (synsetsWordIndex.contains(noun)) id = synsetsWordIndex.get(noun);
                id.add(Integer.parseInt(splitSynsets[0]));
                synsetsWordIndex.put(noun, id);
            }
            // generate synsetsIDIndex <Key, Value> pair
            synsetsIDIndex.put(Integer.parseInt(splitSynsets[0]), splitSynsets[1]);
        }

        // Instantiate DiGraph class from Hypernyms file
        diGraph = new Digraph(synsetsIDIndex.size());
        while (inHypernyms.hasNextLine()) {
            String[] splitHypernyms = inHypernyms.readLine().split(",");
            int source = Integer.parseInt(splitHypernyms[0]);
            for (int i = 1; i < splitHypernyms.length; i++) {
                diGraph.addEdge(source, Integer.parseInt(splitHypernyms[i]));
            }
        }

        // Instantiate SAP class with diGraph info
        sap = new SAP(diGraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetsWordIndex;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return synsetsWordIndex.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || ! isNoun(nounB)) throw new java.lang.IllegalArgumentException ("Noun is not in WordNet");
        Bag<Integer> idA = synsetsWordIndex.get(nounA);
        Bag<Integer> idB = synsetsWordIndex.get(nounB);
        return sap.length(idA, idB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || ! isNoun(nounB)) throw new java.lang.IllegalArgumentException ("Noun is not in WordNet");
        Bag<Integer> idA = synsetsWordIndex.get(nounA);
        Bag<Integer> idB = synsetsWordIndex.get(nounB);
        if (sap.ancestor(idA, idB) == -1) { return "None"; }
        else {
            return synsetsIDIndex.get(sap.ancestor(idA, idB));
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
