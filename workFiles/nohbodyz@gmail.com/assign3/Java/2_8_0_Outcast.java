
public class Outcast {
    
    WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    
    public String outcast(String[] nouns) {
        int max = 0;
        String theOutcast = null;
       // System.out.println(nouns.length);
        for(int i = 0; nouns.length > i; i++) {
            for(int j = 0; nouns.length > j; j++) {
                if(wordnet.distance(nouns[i], nouns[j]) > max) {
                    
                    max = wordnet.distance(nouns[i], nouns[j]);
                    theOutcast = nouns[i];
                    
                   // System.out.println(max);
                   // System.out.print(theOutcast);
                }
            }
        }
        return theOutcast;
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
