/******************************************************************************
 *  Compilation:  javac-alg4 WordNet.java
 *  Execution:    java-alg4 WordNet synsets.txt hypernyms.txt
 *
 *  A wordnet data-type that lets the user search through a large wordnet
 *  to find the shortest common ancestor between two nouns and the distance 
 *  between the two.
 ******************************************************************************/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.LinearProbingHashST;

public class WordNet {

   // constructor takes the name of the two input files
   private String[] synsets;
   private Digraph hypernyms;
   private ShortestCommonAncestor sca;
   LinearProbingHashST<String, Bag<Integer>> table = new LinearProbingHashST<String, Bag<Integer>>();
   
   public WordNet(String txtSynsets, String txtHypernyms) {
       
       if(txtSynsets == null || txtHypernyms == null) throw new NullPointerException("Wordnet given null args.");
       Scanner s = null;
       try {
           s = new Scanner(new File(txtSynsets));
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
       int n = 0;
       while (s.hasNextLine()) {
           s.nextLine();
           n++;
       }
       synsets = new String[n];
       try {
           s = new Scanner(new File(txtSynsets));
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
       while (s.hasNextLine()) {
           Scanner line = new Scanner(s.nextLine());
           line.useDelimiter(",");
           int index = line.nextInt();
           String synset = line.next();
           synsets[index] = synset;
           String[] words = synset.split(" ");
           for(String word : words) {
               if (!table.contains(word)) {
                   table.put(word, new Bag<Integer>());
               }
               table.get(word).add(index);
           }
           line.close();
       }
       hypernyms = new Digraph(n);
       try {
           s = new Scanner(new File(txtHypernyms));
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
       while (s.hasNextLine()) {
           Scanner line = new Scanner(s.nextLine());
           line.useDelimiter(",");
           int vertex1 = line.nextInt();
           while(line.hasNext()) {
               int vertex2 = line.nextInt();
               hypernyms.addEdge(vertex1, vertex2);
           }
           line.close();
       }
       sca = new ShortestCommonAncestor(hypernyms);
   }

   // all WordNet nouns
   public Iterable<String> nouns() {
       
       return table.keys();
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word) {   
       
       if (word == null) throw new NullPointerException("null noun given");
       return table.contains(word);
   }
   // a synset (second field of synsets.txt) that is a shortest common ancestor
   // of noun1 and noun2 (defined below)
   
   
   public String sca(String noun1, String noun2) {
       
       if (noun1 == null || noun2 == null) throw new NullPointerException("Null nouns given");
       if (!isNoun(noun1) || !isNoun(noun2)) throw new IllegalArgumentException("Invalid wordnet nouns");
       Iterable<Integer> nouns1 = table.get(noun1);
       Iterable<Integer> nouns2 = table.get(noun2);
       return synsets[sca.ancestor(nouns1, nouns2)];
   }

   // distance between noun1 and noun2 (defined below)
   public int distance(String noun1, String noun2) {
   
       if (noun1 == null || noun2 == null) throw new NullPointerException("Null nouns given");
       if (!isNoun(noun1) || !isNoun(noun2)) throw new IllegalArgumentException("Invalid wordnet nouns");
       Iterable<Integer> nouns1 = table.get(noun1);
       Iterable<Integer> nouns2 = table.get(noun2);
       return sca.length(nouns1, nouns2);
   }

   // do unit testing of this class
   public static void main(String[] args) {
       
       WordNet wordnet = new WordNet(args[0], args[1]);
       while (!StdIn.isEmpty()) {
           String v = StdIn.readString();
           String w = StdIn.readString();
           int length = wordnet.distance(v, w);
           String ancestor = wordnet.sca(v, w);
           StdOut.printf("length = %d, ancestor = %s\n", length, ancestor);
       }
   }
}
