import java.util.LinkedList;

import edu.princeton.cs.algs4.DepthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedDFS;
import edu.princeton.cs.algs4.In;

public class WordNet {

    public class Noun {
        int id;
        String Noun;

        public Noun(int id, String Noun) {
            this.id = id;
            this.Noun = Noun;
        }
    }

    DepthFirstDirectedPaths search;
    Digraph HypernymGraph;
    Digraph SynsetGraph;
    int id;
    String noun;
    String gloss;

    String[] synsetarray;
    In Synsets;
    In Hypernyms;

    LinkedList<Noun> nouns;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        Synsets = new In("./" + synsets + ".txt");
        Hypernyms = new In("./" + hypernyms + ".txt");

        SynsetGraph = new Digraph(Synsets);
        search = new DepthFirstDirectedPaths(SynsetGraph, 0);

        synsetarray = new String[SynsetGraph.V()];

        for (int i = 0; i < SynsetGraph.V(); i++) {

            while (Hypernyms.hasNextLine()) {

                String Line = Hypernyms.readLine();
                String[] Fields = Line.split(",");

                SynsetGraph.addEdge(i, Integer.parseInt(Fields[2]));
                if (!Fields[3].equals(null)) {
                    SynsetGraph.addEdge(i, Integer.parseInt(Fields[3]));
                }
            }

        }

    }

    // all WordNet nouns
   public Iterable<String> nouns(){
      
       search = new DepthFirstDirectedPaths(SynsetGraph, 0);
       search.pathTo(SynsetGraph.V());
      // Iterable<String> StringPath = Integer.toHexString(search.pathTo(SynsetGraph.V()));
    return null;
        
       
   }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        int id;
        String noun;
        Noun N;
        In nounset = new In("./synsets.txt");
        nouns = new LinkedList<Noun>();

        while (nounset.hasNextLine()) {

            String Line = nounset.readLine();
            String[] fields = Line.split(",");

            id = Integer.parseInt(fields[0]);
            noun = fields[1];
            N = new Noun(id, noun);
            nouns.add(N);
        }
        
        
        return search.hasPathTo(searchList(word));

    }

   public int searchList(String word){
       for(int i = 0; i<nouns.size(); i++){
           if(word == nouns.get(i).Noun){
               return nouns.get(i).id;
           }
          
           
       }
       return 0;
   }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
       int id1 = searchList(noun1);
       int id2 = searchList(noun2);
       
        
        
        return noun2;

    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        int counter = 0;
        int id1 = searchList(noun1);
        int id2 = searchList(noun2);
        search = new DepthFirstDirectedPaths(SynsetGraph, id1);
        Iterable<Integer> path = search.pathTo(id2);
        while(path.iterator().hasNext()){
            counter++;
        }
        
        return counter;

    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets","hypernyms");
        
        StdOut.println(wordNet.distance("happy", "glad"));
    }
}