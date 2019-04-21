/******************************************************************************
 *  Compilation:  javac-algs4 Outcast.java
 *  Execution:    java-algs4 Outcast
 ******************************************************************************/
public class Outcast {
    private WordNet w;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        w = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int outcast = -1;
        int maxDist = -1;
        for (int i = 0; i < nouns.length; i++) {
            int di = 0;
            for (String noun : nouns) {
                di += w.distance(noun, nouns[i]);
            }
            if (di > maxDist) {
                maxDist = di;
                outcast = i;
            }
        }
        return nouns[outcast];
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
