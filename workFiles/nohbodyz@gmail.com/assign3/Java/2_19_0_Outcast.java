/******************************************************************************
 *  Compilation:  javac-alg4 Outcast.java
 *  Execution:    java-alg4 Outcast synsets.txt hypernyms.txt outcast8.txt
 *
 *  An outcast data-type that uses the WordNet class to out of a group of words
 *  Identify the word that is least related to all the other words.
 ******************************************************************************/
public class Outcast {
    
   private WordNet wordnet;
   public Outcast(WordNet wordnet) {
       // constructor takes a WordNet object
       this.wordnet = wordnet;
   }
   public String outcast(String[] nouns) {
       // given an array of WordNet nouns, return an outcast
       String outcast = "";
       int maxDiff = 0;
       for (String noun1 : nouns) {
           int diff = 0;
           for (String noun2 : nouns) {
               int distance = wordnet.distance(noun1, noun2);
               diff += distance;
               String sca = wordnet.sca(noun1, noun2);
           }
           if (diff > maxDiff) {
               outcast = noun1;
               maxDiff = diff;
           }
       }
       return outcast;
   }
   public static void main(String[] args) {
       WordNet wordnet = new WordNet(args[0], args[1]);
       Outcast outcast = new Outcast(wordnet);
       for (int t = 2; t < args.length; t++) {
           In in = new In(args[t]);
           String[] nouns = in.readAllStrings();
           StdOut.println(args[t] + ": " + outcast.outcast(nouns));
       }
   }
}
