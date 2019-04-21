public class Outcast {
    private WordNet wn;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        wn = wordnet;
    }
    public String outcast(String[] nouns) {
        ShortestCommonAncestor sca = new ShortestCommonAncestor(wn.G);

        String outcastValue = "";
        int outcastLength = 0;


        for (String noun : nouns) {
            int tempLength = 0;

            for (String noun2 : nouns) {
                tempLength += sca.length(wn.words_key.get(noun), wn.words_key.get(noun2));
            }

            if (tempLength > outcastLength) {
                outcastValue = noun;
                outcastLength = tempLength;
            }

        }
        // given an array of WordNet nouns, return an outcast
        
        return outcastValue;
    }
    
    public static void main(String[] args) {
        // see test client below
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

}