import java.io.IOException;

public class Outcast {
   WordNet wordnet;
   // constructor takes a WordNet object
   public Outcast(WordNet wordnet) {    
       this.wordnet = wordnet;
   }
   
   // given an array of WordNet nouns, return an outcast
   public String outcast(String[] nouns) {
       int [][] distances = new int[nouns.length][nouns.length];
       int dist;
       int maxDist = -1;
       int maxId = -1;
       
       // check total distance of each pair of nouns
       for (int i=0; i<nouns.length; i++) {
           distances[i][i] = 0;
           for (int j=i+1; j<nouns.length; j++) {
               dist = wordnet.distance(nouns[i], nouns[j]);
               distances[i][j] = dist;
               distances[j][i] = dist;
           }
       }
       for (int i=0; i<nouns.length; i++) {
           dist = 0;
           // total distances for all nouns
           for (int j=0; j<nouns.length; j++) {
               dist += distances[i][j];
           }
           // find max distance of all
           if (dist > maxDist) {
               maxDist = dist;
               maxId = i;
           }
       }
       // return max found
       return nouns[maxId];
   }
   
   // Unit Test client
   public static void main(String[] args) throws IOException {
       WordNet wordnet = new WordNet(args[0], args[1]);
       Outcast outcast = new Outcast(wordnet);
       for (int t = 2; t < args.length; t++) {
           In in = new In(args[t]);
           String[] nouns = in.readAllStrings();
           StdOut.println(args[t] + ": " + outcast.outcast(nouns));
       }
   }
}