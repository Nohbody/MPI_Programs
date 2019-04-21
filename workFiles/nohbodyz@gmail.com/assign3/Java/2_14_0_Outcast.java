public class Outcast {
   private WordNet wn;
   // constructor takes a WordNet object
   public Outcast(WordNet wordnet) {
       wn = wordnet;
   }
   
   // given an array of WordNet nouns, return an outcast
   public String outcast(String[] nouns) {
       int maxDistance = 0;
       int distance;
       String outcast = "";
       
       for(String v : nouns) {
           if (!wn.isNoun(v)) throw new java.lang.IllegalArgumentException("'" + v + "' is not in WordNet.");
           distance = 0;
           
           for(String w : nouns) {
               if(v == w) continue;
               distance += wn.distance(v, w);
           }
           
           if (distance > maxDistance) {
               maxDistance = distance;
               outcast = v;
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