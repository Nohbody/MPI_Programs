/**WordNet.java
 * Paul Wrobel 11/13/17
 * Description: Creates a Digraph (net) which uses the data from the hypernyms file to construct the digraph.
 *
 * */
import java.util.NoSuchElementException;
import java.util.Scanner;
import edu.princeton.cs.algs4.*;

public class WordNet
{
    private Digraph net; //wordnet graph
    private ShortestCommonAncestor mySca; //initialized in constructor, used for sca() and distance()
    private In synsetsFile, hypernymsFile;

    private ST<String, Integer> synsetST;

    private int ancestor;

    private Scanner lineScanner;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) //linearithmic
    {
        if(synsets == null || hypernyms == null)
            throw new NullPointerException();

        synsetsFile = new In("synsets.txt");
        hypernymsFile = new In("hypernyms.txt");

        mySca = new ShortestCommonAncestor(net);

        synsetST = new ST<>();

        try
        {
            while(hypernymsFile.hasNextLine())
            {
                lineScanner = new Scanner(hypernymsFile.readLine()).useDelimiter(","); //read each line and separate by comma

                int value = lineScanner.nextInt();
                synsetST.put(lineScanner.next(), value); //puts a vertex-word pair into the ST
            }
        }
        catch (NoSuchElementException e)
        {
            throw new IllegalArgumentException("invalid input format in Digraph constructor", e);
        }

        net = new Digraph(hypernymsFile);  //??
    }

    // all WordNet nouns
    public Iterable<String> nouns() //not used?
    {
        return synsetST.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word)//logarithmic
    {
        if(word == null)
            throw new NullPointerException();

        return synsetST.get(word) != null; //
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2)
    {
        if (noun1 == null || noun2 == null)
            throw new NullPointerException();
        if (!isNoun(noun1) || !isNoun(noun2))
            throw new IllegalArgumentException();

        ancestor = mySca.ancestor(synsetST.get(noun1),synsetST.get(noun2)); //gets the ancestor of noun1 & 2 after
                                                                                //converting String to vertex int
        return vertexToString(ancestor);
    }
    public String vertexToString(int vertex) //oh god why
    {
        for(int i = 0; i < vertex; i++)
        {
            synsetsFile.readLine();
        }
        synsetsFile.readInt();
        return synsetsFile.readString();
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2)
    {
        if(noun1 == null || noun2 == null) //corner cases
            throw new NullPointerException();
        if (!isNoun(noun1) || !isNoun(noun2)) //if either doesnt exist in digraph
            throw new IllegalArgumentException();

        return mySca.length(synsetST.get(noun1),synsetST.get(noun2)); //passes in the vertex-integer equivalent of noun1 and noun2
    }

    // do unit testing of this class
    public static void main(String[] args)
    {

    }
}