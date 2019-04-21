
public class Outcast {
    private WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    
    public String outcast(String[] nouns) {
        int maxDist = 0;
        String outcast = "";
        for (String n1: nouns) {
            int distSum = 0;
            for (String n2: nouns) {
                int dist = wordnet.distance(n1, n2);
                distSum += dist;
            }
            if (distSum > maxDist) {
                maxDist = distSum;
                outcast = n1;
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
