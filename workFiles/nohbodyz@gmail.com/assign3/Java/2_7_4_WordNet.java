/******************************************************************************
 *  Compilation:  javac-algs4 WordNet.java
 *  Execution:    java-algs4 WordNet
 *
 *  A class representing a WordNet backed by a DAG. Allows the user to find
 *  the shortest common ancestor for two words in the WordNet.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;

public class WordNet {
    private static final String FIELD_DELIMITER = ",";
    private static final String NOUN_DELIMITER = " ";
    private static final String MULTI_WORD_SEP = "_";

    /** Synsets stored using their IDs as keys */
    private final LinearProbingHashST<Integer, String> synsetsById;
    /**
     * Groups of synset IDs stored using the individual nouns as keys.
     * The same noun can be in multiple synsets so a bag is used to hold
     * all of the ID values for each noun.
     */
    private final LinearProbingHashST<String, Bag<Integer>> idsByNoun;
    /** Used to search for shortest common ancestors. */
    private final ShortestCommonAncestor sca;


    /**
     * Construct a new WordNet from a synsets file a hypernyms file.
     */
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new NullPointerException();
        }
        synsetsById = new LinearProbingHashST<>();
        processSynsets(new In(synsets));
        // The number of words will be at least equal to the # of synsets
        idsByNoun = new LinearProbingHashST<>(2 * synsetsById.size());
        Digraph dag = processHypernyms(new In(hypernyms));
        sca = new ShortestCommonAncestor(dag);
    }

    /**
     * Return an iterable containing all of the WordNet's nouns.
     */
    public Iterable<String> nouns() {
        return idsByNoun.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new NullPointerException();
        }
        return idsByNoun.contains(word);
    }

    /**
     * Find a synset (second field of synsets.txt) that is a shortest common
     * ancestor of noun1 and noun2.
     */
    public String sca(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) {
            throw new NullPointerException();
        }
        validate(noun1);
        validate(noun2);
        return synsetsById.get(sca.ancestor(idsByNoun.get(noun1), idsByNoun.get(noun2)));
    }

    /**
     * Find the shortest ancestral path between noun1 and noun2 and return
     * its length.
     */
    public int distance(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) {
            throw new NullPointerException();
        }
        validate(noun1);
        validate(noun2);
        return sca.length(idsByNoun.get(noun1), idsByNoun.get(noun2));
    }

    private void processSynsets(In input) {
        while (input.hasNextLine()) {
            String[] items = input.readLine().split(FIELD_DELIMITER);
            // Throw away the gloss for each
            synsetsById.put(Integer.parseInt(items[0]), items[1]);
        }
    }

    private Digraph processHypernyms(In input) {
        Digraph dag = new Digraph(synsetsById.size());
        while (input.hasNextLine()) {
            String[] items = input.readLine().split(FIELD_DELIMITER);
            int id = Integer.parseInt(items[0]);
            // Add edge
            for (int i = 1, n = items.length; i < n; ++i) {
                dag.addEdge(id, Integer.parseInt(items[i]));
            }
            // Parse words from corresponding synset and store them and their
            // IDs.
            for (String noun : synsetsById.get(id).split(NOUN_DELIMITER)) {
                if (!idsByNoun.contains(noun)) {
                    Bag<Integer> b = new Bag<>();
                    b.add(id);
                    idsByNoun.put(noun, b);
                }
                else {
                    idsByNoun.get(noun).add(id);
                }
            }
        }
        return dag;
    }

    private void validate(String word) {
        if (!isNoun(word)) {
            throw new IllegalArgumentException("Word is not in the WordNet: " + word);
        }
    }

    public static void main(String[] args) {
        // See $ROOT/src/test/java/WordNetTest for unit tests
    }
}