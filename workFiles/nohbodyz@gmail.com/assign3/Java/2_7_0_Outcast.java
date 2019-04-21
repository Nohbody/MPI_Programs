/******************************************************************************
 *  Compilation:  javac-algs4 Outcast.java
 *  Execution:    java-algs4 Outcast synsets.txt hypernyms.txt outcast5.txt \
 *      outcast8.txt outcast11.txt
 *
 *  A class for finding the outcast in a set of nouns.
 *
 *  % more outcast5.txt
 *  horse zebra cat bear table
 *
 *  % more outcast8.txt
 *  water soda bed orange_juice milk apple_juice tea coffee
 *
 *  % more outcast11.txt
 *  apple pear peach banana lime lemon blueberry strawberry mango watermelon potato
 *
 *  % java Outcast synsets.txt hypernyms.txt outcast5.txt outcast8.txt outcast11.txt
 *  outcast5.txt: table
 *  outcast8.txt: bed
 *  outcast11.txt: potato
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wn;

    public Outcast(WordNet wordnet) {
        wn = wordnet;
    }

    public String outcast(String[] nouns) {
        int n = nouns.length;
        int[] dSum = new int[n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (i == j) {
                    continue;
                }
                dSum[i] += wn.distance(nouns[i], nouns[j]);
            }
        }
        int max = 0;
        for (int i = 1; i < n; ++i) {
            if (dSum[max] < dSum[i]) {
                max = i;
            }
        }
        return nouns[max];
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