import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.LinearProbingHashST;

public class WordNet {

    private Digraph dag;
    private LinearProbingHashST<Integer, String> synsets;
    private LinearProbingHashST<String, Integer> nouns;
    private int size;
    private ShortestCommonAncestor sca;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        
        if(synsets == null || hypernyms == null)
            throw new NullPointerException("You are entering a null value!");
       
        this.synsets = new LinearProbingHashST<Integer, String>();
        this.nouns = new LinearProbingHashST<String, Integer>();
        
       In inputSynset = new In(synsets);
       In inputHypernyms = new In(hypernyms);
       size = 0;
       
        while(inputSynset.hasNextLine()) {
              
            size++;
            
            String[] synsetArgs = inputSynset.readLine().split(",");
            
            int id = Integer.parseInt(synsetArgs[0]);
            this.synsets.put(id, synsetArgs[1]);
            
            String[] noun = synsetArgs[1].split(" ");
            
            for(int i = 0; i < noun.length; i++) {
                
                nouns.put(noun[i], id);
                
            }

        }
        
        dag = new Digraph(size);
        
        while(inputHypernyms.hasNextLine()) {
        
            String[] hypersArgs = inputHypernyms.readLine().split(",");
            
            int syn = Integer.parseInt(hypersArgs[0]);
            
            for(int i = 1; i < hypersArgs.length; i++) {
                
                int hypernym = Integer.parseInt(hypersArgs[i]);
                
                dag.addEdge(syn, hypernym);
                
            }
            
        }
        
        this.sca = new ShortestCommonAncestor(dag);
        
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        
        if(word == null)
            throw new NullPointerException("You are entering a null value!");
        
        return this.nouns.contains(word);
        
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        
        if(!this.isNoun(noun1) || !this.isNoun(noun2))
            throw new IllegalArgumentException("You need to two WordNet nouns!");
        
        Integer v = nouns.get(noun1);
        Integer w = nouns.get(noun2);
        int ancestor = sca.ancestor(v, w);
        
        return synsets.get(ancestor) ;
        
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        
        if(!this.isNoun(noun1) || !this.isNoun(noun2))
            throw new IllegalArgumentException("You need two WordNet nouns!");
        
        Integer v = nouns.get(noun1);
        Integer w = nouns.get(noun2);
        
        return sca.length(v, w);
        
    }
    
    // all WordNet nouns
    public Iterable<String> nouns() {
        
        return this.nouns.keys();
        
    }

    // do unit testing of this class
    public static void main(String[] args) {
        
        WordNet w = new WordNet("synsets", "hypernyms");
        System.out.println(w.sca("1830s", "AIDS")); 
        System.out.println(w.distance("1830s", "AIDS"));
        
    }

}
