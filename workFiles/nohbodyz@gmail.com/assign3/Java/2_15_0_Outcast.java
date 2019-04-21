public class Outcast
{
    WordNet wordnet;

    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
        if (wordnet == null) throw new java.lang.NullPointerException();
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        if (nouns == null) throw new java.lang.NullPointerException();
        int maxDistance = 0;
        int distance;
        String aNoun, bNoun;
        String maxNoun = "";
        for (int a = 0; a < nouns.length; a++)
        {
            aNoun = nouns[a];
            distance = 0;
            for (int b = 0; b < nouns.length; b++)
            {
                bNoun = nouns[b];
                if (!wordnet.isNoun(aNoun) || !wordnet.isNoun(bNoun)) continue;
                distance += wordnet.distance(aNoun, bNoun);

                //System.out.println("A " + aNoun + " B " + bNoun + " Distance " + distance + " Max Distance " + maxDistance);

                if (distance > maxDistance)
                {
                    maxDistance = distance;
                    maxNoun = bNoun;
                }
            }
        }
        return maxNoun;
    }

    public static void main(String[] args)
    {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++)
        {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}