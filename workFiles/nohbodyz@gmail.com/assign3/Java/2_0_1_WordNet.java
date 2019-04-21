import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.LinearProbingHashST;


public class WordNet {
    
    Digraph G;
    Scanner synsetsScan, hypernymsScan;
    LinearProbingHashST<String, Integer> wordToID;
    LinearProbingHashST<Integer, String> idToWord;
    ShortestCommonAncestor sca;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
       if (synsets == null || hypernyms == null) {
            throw new java.lang.NullPointerException("arguments cannot be null");
       }
       
       File synsetsFile = new File(synsets);
       File hypernymsFile = new File(hypernyms);
       wordToID = new LinearProbingHashST<String, Integer>();
       idToWord = new LinearProbingHashST<Integer, String>();
       
       try { synsetsScan = new Scanner(synsetsFile); }
       catch (FileNotFoundException e) { StdOut.println("Synsets file not found"); }
       
       while (synsetsScan.hasNextLine()) {
           String line = synsetsScan.nextLine();
           Scanner lineScan = new Scanner(line);
           lineScan.useDelimiter(",");
           int id = lineScan.nextInt();
           String word = lineScan.next();
           lineScan.next(); // get rid of definition
           wordToID.put(word, id);
           idToWord.put(id, word);
       }

       try { hypernymsScan = new Scanner(hypernymsFile); }
       catch (FileNotFoundException e) { StdOut.println("Hypernyms file not found"); }
       
       int V = 0; 
       while (hypernymsScan.hasNextLine()) V++;
       G = new Digraph(V);
       
       while (hypernymsScan.hasNextLine()) {
           String line = hypernymsScan.nextLine();
           Scanner lineScan = new Scanner(line);
           lineScan.useDelimiter(",");
           int s = lineScan.nextInt();
           while (lineScan.hasNextInt()) G.addEdge(s, lineScan.nextInt());
       }
       
       sca = new ShortestCommonAncestor(G);
    }

    // all WordNet nouns
    public Iterable<String> nouns() {
        return wordToID.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new java.lang.NullPointerException("arguments cannot be null");
        }
        return wordToID.contains(word);
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) {
            throw new java.lang.NullPointerException("arguments cannot be null");
        }
        if (!isNoun(noun1) || !isNoun(noun2)) {
           throw new java.lang.IllegalArgumentException("one of those words isn't in the WordNet");
       }
       int v = wordToID.get(noun1);
       int w = wordToID.get(noun2);
       int ancestorInt = sca.ancestor(v, w);
       return idToWord.get(ancestorInt);
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) {
            throw new java.lang.NullPointerException("arguments cannot be null");
        }
        if (!isNoun(noun1) || !isNoun(noun2)) {
            throw new java.lang.IllegalArgumentException("one of those words isn't in the WordNet");
        }
        int v = wordToID.get(noun1);
        int w = wordToID.get(noun2);
        return sca.length(v, w);
    }

    // do unit testing of this class
    public static void main(String[] args) {
       WordNet wn = new WordNet("./synsets.txt", "./hypernyms.txt");
       StdOut.println(wn.wordToID.get("1530s"));
       StdOut.println(wn.idToWord.get(1));
    }
}
