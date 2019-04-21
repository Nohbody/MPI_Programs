public class Outcast {
    
    private WordNet wordnet;
    
   public Outcast(WordNet wordnet) { // constructor takes a WordNet object
       this.wordnet = wordnet;
   }
   
   public String outcast(String[] nouns) { // given an array of WordNet nouns, return an outcast
       
       String outcast = null;
       int max = 0;
       
       for(String noun : nouns) {
           
           int totalDistance = 0;
           
           
           //check all of the distances and find the largest
           for(String otherNoun : nouns) {
               
               int distance = wordnet.distance(noun, otherNoun);
               
               totalDistance += distance;
               
           }
           
           //compare to max distance and create new max
           if(totalDistance > max) {
               max = totalDistance;
               outcast = noun;
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