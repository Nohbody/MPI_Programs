public class Outcast 
{
    private final WordNet net;
    public Outcast(WordNet wordnet) 
    {
        net = wordnet;  
    }
    public String outcast(String[] nouns) 
    {
        String oc = null;
        int max = 0;

        for (String noun1 : nouns) 
        {
            int distance = 0;
            for (String noun2 : nouns) 
            {
                if (!noun1.equals(noun2)) 
                {
                    distance += net.distance(noun1, noun2);
                }
            }
            if (distance > max) 
            {
                max = distance;
                oc = noun1;
            }
        }
        return oc;
    }

    public static void main(String[] args) 
    {
        long t0 = System.nanoTime();
        WordNet wn = new WordNet(args[0], args[1]);
        Outcast oc = new Outcast(wn);

        for (int t = 2; t < args.length; t++) 
        {
            In test = new In(args[t]);
            String[] nouns = test.readAllStrings();
            StdOut.println(args[t] + ": " + oc.outcast(nouns));
        }
        long t1 = System.nanoTime();
        System.out.println("Duration: " + (t1 - t0) / 1000000);
    }
}
