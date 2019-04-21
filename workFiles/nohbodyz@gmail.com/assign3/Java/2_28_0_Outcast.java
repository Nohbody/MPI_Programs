/*
* Name: William Behunin
*
* Filename: Outcast.java
*
* Description: Outcast.java is a data type to obtain any outcasts from a WordNet, using a String
* array.
*
* Known bugs: Unknown
*/
public class Outcast {
    
   private final WordNet net;
   
   public Outcast(WordNet wordnet) {         // constructor takes a WordNet object
       net = wordnet;
   }
   public String outcast(String[] nouns) {  // given an array of WordNet nouns, return an outcast
       String outcast = "";
       int max = 0;
       //Reads through the nouns array to obtain the distance of the outcast, and return
       //the outcast.
       for(String noun1 : nouns) {
           int distance = 0;
           for(String noun2 : nouns) {
               if(!noun1.equals(noun2)) {
                   distance = distance + net.distance(noun1, noun2);
               }
           }
           if(distance > max) {
               max = distance;
               outcast = noun1;
           }
       }
       return outcast;
   }
   public static void main(String[] args) { // see test client below
       WordNet wordnet = new WordNet(args[0], args[1]);
       Outcast outcast = new Outcast(wordnet);
       for (int i = 2; i < args.length; i++) {
           In in = new In(args[i]);
           String[] nouns = in.readAllStrings();
           StdOut.println(args[i] + ": " + outcast.outcast(nouns));
       }
   }
}
