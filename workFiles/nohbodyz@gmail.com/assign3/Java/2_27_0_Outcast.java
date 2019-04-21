public class Outcast {
    private final WordNet net;
    
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        net = wordnet;  
    }
    
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast = null;
        int max = 0;

        for (String noun1 : nouns) {
            int dist = 0;
            for (String noun2 : nouns) {
                if (!noun1.equals(noun2)) {
                    dist += net.distance(noun1, noun2);
                }
            }
            if (dist > max) {
                max = dist;
                outcast = noun1;
            }
        }
        return outcast;
    }

    // see test client below
    public static void main(String[] args) {
        long start = System.nanoTime();
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);

        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            System.out.println(args[t]);
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
        long end = System.nanoTime();
        System.out.println("Duration: " + (end - start) / 1000000);
    }
}