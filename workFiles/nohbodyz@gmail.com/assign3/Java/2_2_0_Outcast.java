import java.io.FileNotFoundException;

public class Outcast {
 public Outcast(WordNet wordnet) {
 } // constructor takes a WordNet object

 public String outcast(String[] nouns) {
  for (String each : nouns) {
   
  }
 } // given an array of WordNet nouns, return an outcast

 public static void main(String[] args) throws FileNotFoundException {
  WordNet wordnet = new WordNet(args[0], args[1]);
  Outcast outcast = new Outcast(wordnet);
  for (int t = 2; t < args.length; t++) {
   In in = new In(args[t]);
   String[] nouns = in.readAllStrings();
   StdOut.println(args[t] + ": " + outcast.outcast(nouns));
  }
 } // see test client below
}