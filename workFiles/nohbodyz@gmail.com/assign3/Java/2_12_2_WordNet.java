import java.util.ArrayList;
import java.util.HashMap;

import edu.princeton.cs.algs4.Digraph;

public class WordNet {
    
    //create two symbol tables
    HashMap<Integer, String> syns; //ids and their associated synsets
    HashMap<String, ArrayList<Integer>> nouns; //nouns and their ids
    
    int counter = 0;
    int lineCounter;
    
    ShortestCommonAncestor sca;
    
    

   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) {
       
       if(synsets == null || hypernyms == null) {
           throw new NullPointerException();
       }
       
       //read synsets text file
       In Synsets = new In(synsets);
       while(Synsets.hasNextLine()) {
           
           ArrayList<Integer> myList;
           
           String line = Synsets.readLine();
           String[] dividedLine = line.split(",");
           
           //first number is key and rest is the value
           syns.put(Integer.parseInt(dividedLine[0]), dividedLine[1]);
           
           for (String word : dividedLine[1].split(" ")) {
               
               myList = nouns.get(word);
               
               if(myList == null) {
                   myList = new ArrayList<Integer>();
                   myList.add(Integer.parseInt(dividedLine[0]));
                   nouns.put(word, myList);
               }
               
               else {
                   myList.add(Integer.parseInt(dividedLine[0]));
               }
               
           }
           
           counter++;
                
       }
       
       //read hypernyms text file and create digraph
       Digraph digraph = new Digraph(counter);
       In Hypernyms = new In(hypernyms);
       
       while(Hypernyms.hasNextLine()) {
           String line = Hypernyms.readLine();
           String[] dividedLine = line.split(",");
           
           //add an edge for every number and all its subsequent numbers.
           for(int i=0; i<dividedLine.length; i++) {
               digraph.addEdge(Integer.parseInt(dividedLine[0]), Integer.parseInt(dividedLine[i+1]));
           }
       }
       
       //create sca with hypernyms digraph
       sca = new ShortestCommonAncestor(digraph);
       
   }

   // all WordNet nouns
   public Iterable<String> nouns() {
       
       if(nouns == null) {
           throw new NullPointerException();
       }
       
       return nouns.keySet();
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word) {
       
       if(word == null) {
           throw new NullPointerException();
       }
       
       return nouns.containsKey(word);
       
   }

   // a synset (second field of synsets.txt) that is a shortest common ancestor
   // of noun1 and noun2 (defined below)
   public String sca(String noun1, String noun2) {
       
       if(noun1 == null || noun2 == null) {
           throw new NullPointerException();
       }
       
       if(isNoun(noun1) == false || isNoun(noun2) == false) {
           throw new IllegalArgumentException();
       }
       
       //uses sca
       return syns.get(sca.ancestor(nouns.get(noun1), nouns.get(noun2)));
       
   }

   // distance between noun1 and noun2 (defined below)
   public int distance(String noun1, String noun2) {
       
       if(noun1 == null || noun2 == null) {
           throw new NullPointerException();
       }
       
       if(isNoun(noun1) == false || isNoun(noun2) == false) {
           throw new IllegalArgumentException();
       }
       
       //uses sca
       return sca.length(nouns.get(noun1), nouns.get(noun2));
   }

   // do unit testing of this class
   public static void main(String[] args) {
       
   }
}