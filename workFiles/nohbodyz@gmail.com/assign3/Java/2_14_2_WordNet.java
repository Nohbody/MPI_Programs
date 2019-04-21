import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Bag;

public class WordNet {
    private Digraph dg;
    private RedBlackBST<String, Bag<Integer>> nouns = new RedBlackBST<String, Bag<Integer>>();
    private LinearProbingHashST<Integer, String> synsets = new LinearProbingHashST<Integer, String>();
    private int v = 0;
    private ShortestCommonAncestor sca;

   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) {
       if (synsets == null || hypernyms == null) throw new java.lang.NullPointerException("Please provide 2 arguments");
       
       String[] lineArray;
       String[] nounArray;
       Integer synID;
       
       In in = new In(synsets);
       
       while(in.hasNextLine()) {
           lineArray = in.readLine().split(",");
           nounArray = lineArray[1].split(" ");
           synID = Integer.parseInt(lineArray[0]);
           
           this.synsets.put(synID, lineArray[1]);
           
           for(String s : nounArray) {
               Bag<Integer> bag;
               if (nouns.contains(s)) {
                   bag = nouns.get(s);
               } else {
                   bag = new Bag<Integer>();
               }
               bag.add(synID);
               nouns.put(s, bag);
           }
           
           v++;
       }
       
       dg = new Digraph(v);
       sca = new ShortestCommonAncestor(dg);
       
       in = new In(hypernyms);
       
       while(in.hasNextLine()) {
           lineArray = in.readLine().split(",");
           for(String i : lineArray) {
               if(i == lineArray[0]) continue;
               dg.addEdge(Integer.parseInt(lineArray[0]), Integer.parseInt(i));
           }
       }
   }

   // all WordNet nouns
   public Iterable<String> nouns(){
       return nouns.keys();
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word) {
       if (word == null) throw new java.lang.NullPointerException("Requires String argument 'word.'");
       return nouns.contains(word);
   }

   // a synset (second field of synsets.txt) that is a shortest common ancestor
   // of noun1 and noun2 (defined below)
   public String sca(String noun1, String noun2) {
       if (noun1 == null || noun2 == null) throw new java.lang.NullPointerException("Requires String arguments 'noun1' and 'noun2'");
       if (!isNoun(noun1) || !isNoun(noun2)) throw new java.lang.IllegalArgumentException("Argument(s) not in WordNet.");
       int ancestor = sca.ancestor(nouns.get(noun1), nouns.get(noun2));
       return synsets.get(ancestor);
   }

   // distance between noun1 and noun2 (defined below)
   public int distance(String noun1, String noun2) {
       if (noun1 == null || noun2 == null) throw new java.lang.NullPointerException("Requires String arguments 'noun1' and 'noun2'");
       if (!isNoun(noun1) || !isNoun(noun2)) throw new java.lang.IllegalArgumentException("Argument(s) not in WordNet.");
       return sca.length(nouns.get(noun1), nouns.get(noun2));
   }

   // do unit testing of this class
   public static void main(String[] args) {
         WordNet wn = new WordNet(args[0], args[1]);
         
         while (!StdIn.isEmpty()) {
             String noun1 = StdIn.readString();
             String noun2 = StdIn.readString();
             
             if(wn.isNoun(noun1) && wn.isNoun(noun2)) {
                 int length   = wn.distance(noun1, noun2);
                 String ancestor = wn.sca(noun1, noun2);
                 StdOut.printf("length = %d, ancestor = %s\n", length, ancestor);    
             } else {
                 StdOut.println("One of the provided arguments is not in WordNet.");
             }
         }
   }
}