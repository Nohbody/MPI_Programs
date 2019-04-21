import java.util.HashMap;
import java.util.Map;

import edu.princeton.cs.algs4.Digraph;

public class WordNet {
    
    private final Map<String, Integer> synsetsMap;
    private final Map<Integer, String> synsetsMapReversed;
    private final Digraph graph;
    private final ShortestCommonAncestor sca;

    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        
        
        
        //creates hashmap of all synsets
        synsetsMap = new HashMap<String, Integer>();
        synsetsMapReversed = new HashMap<Integer, String>();
        In file1 = new In(synsets);
        while(file1.hasNextLine()) {
            String[] line = file1.readLine().split(",");
            Integer id = Integer.valueOf(line[0]);
            String n = line[1];
            synsetsMap.put(n,id);
            synsetsMapReversed.put(id, n);
            
        }
        
        
        //creates digraph of hypernyms
        graph = new Digraph(82192);
        In file2 = new In(hypernyms);
        while(file2.hasNextLine()) {
            String[] line = file2.readLine().split(",");
            Integer id = Integer.valueOf(line[0]);
            for(int i = 1; i < line.length; i++) {
                Integer x = Integer.valueOf(line[i]);
                graph.addEdge(id, x);
            }
        }
        
        sca = new ShortestCommonAncestor(graph);
        
        
        
    }

    // all WordNet nouns
    public Iterable<String> nouns(){
        return synsetsMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return synsetsMap.containsKey(word);
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        if(isNoun(noun1) && isNoun(noun2)) {
            int noun1id = synsetsMap.get(noun1);
            int noun2id = synsetsMap.get(noun2);
            int ancestor = sca.ancestor(noun1id, noun2id);
            return synsetsMapReversed.get(ancestor);
        }
        return null;
        
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if(isNoun(noun1) && isNoun(noun2)) {
            int noun1id = synsetsMap.get(noun1);
            int noun2id = synsetsMap.get(noun2);
            return sca.length(noun1id, noun2id);
        }else
            return -1;
        
    }
    
    public String getWord(int i) {
        return synsetsMapReversed.get(i);
    }


    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets.txt","hypernyms.txt");
        
    }

}
