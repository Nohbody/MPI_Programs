import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;

import java.util.*;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Stack;

public class WordNet {

    private ShortestCommonAncestor sca;         
    private RedBlackBST<String, Stack<Integer>> bst;   // For id lookup by word (bag to hold multiple word locations)
    private ArrayList<String> idArr;            // For word lookup by id
    
   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) throws IOException {
       testNull("WordNet", synsets);
       testNull("WordNet", hypernyms);
       
       // Create new bst (for id lookup)
       bst = new RedBlackBST<String, Stack<Integer>>();
       // Create new arraylist (for noun lookup)
       idArr = new ArrayList<String>();
       
       // Read in all synsets (and make BST from it)
       BufferedReader input = new BufferedReader(new FileReader(synsets));
       String line = input.readLine();
       int numSyns=0;
       while (line != null) {
           String entry[] = line.split(",");
           int synId = Integer.parseInt(entry[0]);
           idArr.add(synId, entry[1]);
           String[] synset = entry[1].split(" ");
           // Create a lookup for every noun in synset
           for (int i=0; i<synset.length; i++) {
               // if word already exists then add id to node's stack, else add node to bst
               if (bst.contains(synset[i])) {
                   bst.get(synset[i]).push(synId);
               } else {
                   //System.out.println("Initial id of " + synId);
                   Stack<Integer> idStack = new Stack<Integer>();
                   idStack.push(synId);
                   bst.put(synset[i], idStack);
               }
           }
           // Read next line from file and count the synset    
           line = input.readLine();
           numSyns++;
       }
       input.close();
       
       // Make Digraph with nodes/vertices equal to number of Synsets
       Digraph D = new Digraph(numSyns);
       
       // Read in all hypernyms (and make digraph from it)
       input = new BufferedReader(new FileReader(hypernyms));
       line = input.readLine();
       while (line != null) {
           String entry[] = line.split(",");
           int synId = Integer.parseInt(entry[0]);
           // Create an edge for every connection between synset and hypernym
           for (int i=1; i<entry.length; i++) {
               int hypId = Integer.parseInt(entry[i]);
               D.addEdge(synId, hypId);
           }
           // Read next line from file
           line = input.readLine();
       }
       input.close();
       
       // Create a SCA from resulting Digraph
       sca = new ShortestCommonAncestor(D);
   }

   // all WordNet nouns
   public Iterator<String> nouns(){
       return new WordNetIterator();
   }
   // Iterator class
   private class WordNetIterator implements Iterator<String>{
       int current;
       
       public WordNetIterator() {
           current = 0;
       }
       public String next() {
           String next = idArr.get(current);
           current++;
           return next;
       }
       public boolean hasNext() {
           return (current < idArr.size());
       }
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word) {
       testNull("isNoun", word);
       return bst.contains(word);
   }
   
   // returns the id of a noun
   public Iterable<Integer> getIdStack(String noun) {
       Stack<Integer> stack = bst.get(noun);
       return stack;
   }
   
   // returns the synset of an synID
   public String getSynset(int synId) {
       return idArr.get(synId);
   }

   // a synset (second field of synsets.txt) that is a shortest common ancestor
   // of noun1 and noun2 (defined below)
   public String sca(String noun1, String noun2) {
       testNull("sca", noun1);
       testNull("sca", noun2);
       testNoun(noun1);
       testNoun(noun2);
       
       Iterable<Integer> idStack1 = getIdStack(noun1);
       Iterable<Integer> idStack2 = getIdStack(noun2);
       int ancenstorId = sca.ancestor(idStack1, idStack2);
       return getSynset(ancenstorId);
   }

   // distance between noun1 and noun2 (defined below)
   public int distance(String noun1, String noun2) {
       testNull("distance", noun1);
       testNull("distance", noun2);
       testNoun(noun1);
       testNoun(noun2);
       
       Iterable<Integer> idStack1 = getIdStack(noun1);
       Iterable<Integer> idStack2 = getIdStack(noun2);
       int length = sca.length(idStack1, idStack2);
       return length;
   }
   
   // test if object is null
   public void testNull(String name, Object obj) {
       if (obj.equals(null)) {
           throw new NullPointerException("Error: Method '" + name + "' cannot accept a null argument...");
       }
   }
   // test if noun is in list
   public void testNoun(String noun) {
       if (isNoun(noun) == false) {
           throw new IllegalArgumentException("Error: " + noun + " is not a noun in the synset..."); 
       }
   }

   // do unit testing of this class
   public static void main(String[] args) throws IOException{
       WordNet wnet = new WordNet("synsets.txt", "hypernyms.txt");
       
       // test iterator
       Iterator<String> iter = wnet.nouns();
       for(int i=0; i<10; i++) {
           if (iter.hasNext()) {
               System.out.println(i + ": " + iter.next());
           }
       }
       
       StdOut.println("----");
       
       String word1 = "horse";
       String word2 = "zebra";
       
       // test for word1
       Iterable<Integer> nounIdStack1 = wnet.getIdStack(word1);
       Iterator<Integer> nounIdIter1 = nounIdStack1.iterator();
       StdOut.println("Noun IDs for " + word1 + ": ");
       while (nounIdIter1.hasNext()){
           int next = nounIdIter1.next();
           System.out.print(next + " ");
       }
       StdOut.println("");
       
       StdOut.println("----");
       
       // test for word2
       Iterable<Integer> nounIdStack2 = wnet.getIdStack(word2);
       Iterator<Integer> nounIdIter2 = nounIdStack2.iterator();
       StdOut.println("Noun IDs for " + word2 + ": ");
       while (nounIdIter2.hasNext()){
           int next = nounIdIter2.next();
           System.out.print(next + " ");
       }
       StdOut.println("");
       
       StdOut.println("----");
       
       // test distance and sca
       StdOut.println("Distance: " + wnet.distance(word1, word2));
       StdOut.println("SCA: " + wnet.sca(word1, word2));
   }
}