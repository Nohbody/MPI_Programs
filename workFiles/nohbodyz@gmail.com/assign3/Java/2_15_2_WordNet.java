import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.RedBlackBST;

import java.util.LinkedList;

public class WordNet
{
    // constructor takes the name of the two input files
    RedBlackBST synsetTable;
    RedBlackBST nounTable;
    Digraph graph;
    ShortestCommonAncestor ancestor;

    public WordNet(String synsets, String hypernyms)
    {
        if (synsets == null || hypernyms == null) throw new java.lang.NullPointerException();
        // Load input
        In synsetsIn = new In(synsets);
        In hypernymsIn = new In(hypernyms);

        // Make the synset and noun tables
        String line;
        String[] split;
        int id;
        String word;
        String[] words;
        LinkedList<Integer> ids;

        synsetTable = new RedBlackBST<Integer, String[]>();
        nounTable = new RedBlackBST<String, LinkedList>();

        while (synsetsIn.hasNextLine())
        {
            line = synsetsIn.readLine();
            if (line == null) break;
            split = line.split(",");
            id = Integer.parseInt(split[0]);
            words = split[1].split(" ");

            synsetTable.put(id, words);

            for (String noun : words)
            {
                if (nounTable.contains(noun))
                {
                    Object value = nounTable.get(noun);
                    ids = (LinkedList<Integer>) value;
                    ids.add(id);
                    nounTable.put(noun, ids);
                } else
                {
                    ids = new LinkedList<>();
                    ids.add(id);
                    nounTable.put(noun, ids);
                }
            }
        }

        // Make graph
        graph = new Digraph(synsetTable.size());

        int i;
        int parentID;
        while (hypernymsIn.hasNextLine())
        {
            line = hypernymsIn.readLine();
            if (line == null) break;
            split = line.split(",");
            id = Integer.parseInt(split[0]);
            for (i = 1; i < split.length; i++)
            {
                parentID = Integer.parseInt(split[i]);
                graph.addEdge(id, parentID);
            }
        }
        ancestor = new ShortestCommonAncestor(graph);
    }

    // all WordNet nouns
    public Iterable<String> nouns()
    {
        return nounTable.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        if (word == null) throw new java.lang.NullPointerException();
        return nounTable.contains(word);
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2)
    {
        if (noun1 == null || noun2 == null) throw new java.lang.NullPointerException();
        if (!isNoun(noun1) || !isNoun(noun2)) throw new java.lang.IllegalArgumentException();

        Object value1 = nounTable.get(noun1);
        LinkedList<Integer> noun1List = (LinkedList) value1;

        Object value2 = nounTable.get(noun2);
        LinkedList<Integer> noun2List = (LinkedList) value2;

        int ancestorID = ancestor.ancestor(noun1List, noun2List);
        Object synsetObject = synsetTable.get(ancestorID);
        String[] synsetArray = (String[]) synsetObject;
        String synsetString = String.join(" ", synsetArray);
        return synsetString;
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2)
    {
        if (noun1 == null || noun2 == null) throw new java.lang.NullPointerException();
        if (!(isNoun(noun1) && isNoun(noun2))) throw new java.lang.IllegalArgumentException();

        Object value1 = nounTable.get(noun1);
        LinkedList<Integer> noun1IDs = (LinkedList<Integer>) value1;

        Object value2 = nounTable.get(noun2);
        LinkedList<Integer> noun2IDs = (LinkedList<Integer>) value2;

        int minimum = ancestor.length(noun1IDs, noun2IDs);
        return minimum;
    }

    // do unit testing of this class
    public static void main(String[] args)
    {
        String synsets = args[0];
        String hypernyms = args[1];
        WordNet wordNet = new WordNet(synsets, hypernyms);

        // Test nouns() and isNoun()
        for (String word : wordNet.nouns())
        {
            System.out.println(
                    word + " is in the table " +
                            wordNet.isNoun(word) +
                            " and NOTINTABLE is in the table " +
                            wordNet.isNoun("NOTINTABLE"));

        }
        System.out.println(wordNet.sca("miracle", "event"));
    }
}