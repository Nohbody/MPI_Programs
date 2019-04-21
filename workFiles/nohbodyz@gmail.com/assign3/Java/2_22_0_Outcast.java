public class Outcast
{
    private WordNet wordnet;

    public Outcast(WordNet wordnet)  // constructor takes a WordNet object
    {
        this.wordnet = wordnet;
    }
    public String outcast(String[] nouns)  // given an array of WordNet nouns, return an outcast
    {
        int[] dist = new int[nouns.length];
        int temp = 0;

        int high = 0;
        for(int i = 0; i < nouns.length; i++)
        {
            for(int j = 0; j < i; j++)
            {
                temp = wordnet.distance(nouns[i], nouns[j]);

                if(temp < 0)
                    throw new IllegalArgumentException();

                dist[i] += temp;
                dist[j] += temp;
            }
        }
        for(int i = 0; i < nouns.length; i++)
        {
            if(dist[i] > high)
            {
                high = dist[i];
                temp = i;
            }
        }
        return wordnet.vertexToString(temp); //vertex to string
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