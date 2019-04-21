import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;

public class WordNet {

    private Digraph dg;
    private LinearProbingHashST<String, Integer> hashsynST;
    private LinearProbingHashST<Integer, String> hashidST;
    private ShortestCommonAncestor sca;
    
   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) {
       List<String> synEntries = readLines(new In(synsets)); // throws nullpointer
       
       // Add entries to HashST
       hashsynST = new LinearProbingHashST<String, Integer>();
       hashidST = new LinearProbingHashST<Integer, String>();
       synEntries.forEach(synLine -> {
           Scanner scanLine = new Scanner(synLine).useDelimiter(",");
           Integer id = scanLine.nextInt();
           String syns = scanLine.next();
           hashidST.put(id, syns);
           Scanner scanSyns = new Scanner(syns).useDelimiter("\\s+");
           while (scanSyns.hasNext())
               hashsynST.put(scanSyns.next(), id);
           scanLine.close();
           scanSyns.close();
       });
       
       // Create digraph
       dg = new Digraph(synEntries.size());
       In hypIn = new In(hypernyms); // throws nullpointer
       while (hypIn.hasNextLine()) {
           Scanner scanLine = new Scanner(hypIn.readLine()).useDelimiter(",");
           Integer v = scanLine.nextInt();
           while (scanLine.hasNext())
               dg.addEdge(v, scanLine.nextInt());
           scanLine.close();
       }
       sca = new ShortestCommonAncestor(dg);
   }
   
   private List<String> readLines(In in) {
       List<String> entries = new LinkedList<String>();
       while (in.hasNextLine())
           entries.add(in.readLine());
       return entries;
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word) {
       return hashsynST.contains(word); // throws nullpointer
   }

   // a synset (second field of synsets.txt) that is a shortest common ancestor
   // of noun1 and noun2 (defined below)
   public String sca(String noun1, String noun2) {
       if (!isNoun(noun1) || !isNoun(noun2))
           throw new IllegalArgumentException("noun1 or noun2 are not nouns. ");
       return hashidST.get(sca.ancestor(hashsynST.get(noun1), hashsynST.get(noun2)));
   }

   // distance between noun1 and noun2 (defined below)
   public int distance(String noun1, String noun2) {
       if (!isNoun(noun1) || !isNoun(noun2))
           throw new IllegalArgumentException("noun1 or noun2 are not nouns. ");
       return sca.length(hashsynST.get(noun1), hashsynST.get(noun2));
   }
   
   // all WordNet nouns
   public Iterable<String> nouns() {
       return hashsynST.keys();
   }

   // do unit testing of this class
   public static void main(String[] args) {
       WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
       System.out.println("Number of Synsets: " + wn.dg.V() + "\n" +
                          "Number of Hypernyms: " + wn.dg.E() + "\n" + 
                          "Is flack a noun? " + wn.isNoun("flack") + "\n" + 
                          "Is flubber a noun? " + wn.isNoun("flubber") + "\n" + 
                          "SCA of flack & Alabama: " + wn.sca("flack", "Alabama"));
       
   }
}