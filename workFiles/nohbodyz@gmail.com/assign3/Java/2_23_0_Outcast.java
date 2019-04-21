public class Outcast {
    WordNet net;
   public Outcast(WordNet wordnet) {
       // constructor takes a WordNet object
       this.net = wordnet;
   }
   public String outcast(String[] nouns) {
       // given an array of WordNet nouns, return an outcast
       int max = 0;
       String noun = "";
       for (String n: nouns) {
           int runningsum=0;
           for(String w: nouns) {
               if(!n.equals(w)) {
                   int x = net.distance(n, w);
                   runningsum += x;
               }
               if (runningsum>max)
                   max = runningsum;
               noun = n;
           }
       }
       return noun;
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