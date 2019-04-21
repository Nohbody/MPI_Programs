package assignment1;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {

    private final Digraph digraph;
    private final Map<String, List<Integer>> nouns = new HashMap<>();
    private final Map<Integer, String> synsets = new HashMap<>();

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Argument should not be null");
        }
        In in = new In(synsets);
        while (!in.isEmpty()) {
            String[] line = in.readLine().split(",");
            int index = Integer.parseInt(line[0]);
            this.synsets.put(index, line[1]);
            Arrays
                    .stream(line[1].split(" "))
                    .forEach(word -> {
                        if (!nouns.containsKey(word)) {
                            nouns.put(word, new ArrayList<>());
                        }
                        nouns.get(word).add(index);
                    });
        }
        in.close();

        digraph = new Digraph(this.synsets.size());
        in = new In(hypernyms);
        while (!in.isEmpty()) {
            String[] line = in.readLine().split(",");
            int synset = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                digraph.addEdge(synset, Integer.parseInt(line[i]));
            }
        }
        in.close();
        DirectedCycle finder = new DirectedCycle(digraph);
        if (finder.hasCycle()) {
            throw new IllegalArgumentException("Given graph is not rooted DAG");
        }
    }

    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Argument should not be null");
        }
        return nouns.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("Argument should not be null");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("One of the arguments is not assignment1.WordNet noun");
        }
        SAP sap = new SAP(digraph);
        return sap.length(numberOf(nounA), numberOf(nounB));

    }

    private Iterable<Integer> numberOf(String noun) {
        return nouns.get(noun);
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("Argument should not be null");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("One of the arguments is not assignment1.WordNet noun");
        }
        SAP sap = new SAP(digraph);
        int ancestor = sap.ancestor(numberOf(nounA), numberOf(nounB));
        if (ancestor == -1) {
            throw new IllegalArgumentException("Nouns do not have common ancestor");
        }
        return synsets.get(ancestor);
    }
}