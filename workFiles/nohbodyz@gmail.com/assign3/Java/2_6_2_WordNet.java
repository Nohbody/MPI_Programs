/******************************************************************************
 *  Compilation:  javac-algs4 WordNet.java
 *  Execution:    java-algs4 WordNet
 ******************************************************************************/
import java.util.Scanner;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import java.util.ArrayList;

public class WordNet {

    private LinearProbingHashST<String, Bag<Integer>> nouns;
    private Digraph wordNet;
    private String[] synsets = new String[0];


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new java.lang.NullPointerException();
        }
        nouns = new LinearProbingHashST<String, Bag<Integer>>();
        int graphSize = processSynsets(new In(synsets));
        processHypernyms(new In(hypernyms), graphSize);
    }

    private int processSynsets(In synData) {
        if (synData == null) {
            throw new java.lang.NullPointerException();
        }
        int count = 0;
        Scanner commaSep;
        ArrayList<String> tempSynSets = new ArrayList<String>();
        Scanner spaceSep;
        while (synData.hasNextLine()) {
            count++;
            commaSep = new Scanner(synData.readLine());
            commaSep.useDelimiter(",");
            int vertexID = commaSep.nextInt();
            String syns = commaSep.next();
            tempSynSets.add(syns);
            spaceSep = new Scanner(syns);
            spaceSep.useDelimiter(" ");
            while (spaceSep.hasNext()) {
                Bag<Integer> value;
                String key = spaceSep.next();
                if (nouns.contains(key)) {
                    value = nouns.get(key);
                }
                else {
                    value = new Bag<Integer>();
                }
                value.add(vertexID);
                nouns.put(key, value);
            }    
        }
        this.synsets = tempSynSets.toArray(this.synsets);
        return count;
    }

    private void processHypernyms(In hypernyms, int graphSize) {
        if (hypernyms == null) {
            throw new java.lang.NullPointerException();
        }
        wordNet = new Digraph(graphSize);
        Scanner commaSep;
        while (hypernyms.hasNextLine()) {
            commaSep = new Scanner(hypernyms.readLine());
            commaSep.useDelimiter(",");
            int v = commaSep.nextInt();
            while (commaSep.hasNext()) {
                int w = commaSep.nextInt();
                wordNet.addEdge(v, w);
            }
        }
    }
    // all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new java.lang.NullPointerException();
        }
        return nouns.contains(word);
    }

    // a synset (second field of synsets.txt) that is a shortest
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) {
            throw new java.lang.NullPointerException();
        }
        if (!isNoun(noun1) || !isNoun(noun2)) {
            throw new java.lang.IllegalArgumentException();
        }
        Bag<Integer> n1Verts = nouns.get(noun1);
        Bag<Integer> n2Verts = nouns.get(noun2);
        int synidx = new ShortestCommonAncestor(wordNet).ancestor(n1Verts, n2Verts);
        return synsets[synidx];
    }

    // distance between noun1 and noun2
    public int distance(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) {
            throw new java.lang.NullPointerException();
        }
        if (!isNoun(noun1) || !isNoun(noun2)) {
            throw new java.lang.IllegalArgumentException();
        }
        Bag<Integer> n1Verts = nouns.get(noun1);
        Bag<Integer> n2Verts = nouns.get(noun2);
        return new ShortestCommonAncestor(wordNet).length(n1Verts, n2Verts);
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
