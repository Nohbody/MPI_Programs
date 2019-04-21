import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.ResizingArrayQueue;

public class WordNet {
    LinearProbingHashST <Integer, ResizingArrayQueue<String>> key_words;
    LinearProbingHashST <String, Integer> words_key;
    
    Digraph G;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In syn_file = new In(synsets);
        In hyp_file = new In(hypernyms);
        
        key_words = new LinearProbingHashST <Integer, ResizingArrayQueue<String>>();
        words_key = new LinearProbingHashST <String, Integer>();
        
        while (syn_file.hasNextLine()) {
            String[] line = syn_file.readLine().split(",");
            
            int id = Integer.parseInt(line[0]);
                        
            String[] nouns = line[1].split(" ");
            ResizingArrayQueue<String> temp_nouns = new ResizingArrayQueue<String>();
            
            for (String noun : nouns) {
                temp_nouns.enqueue(noun);
                words_key.put(noun, id);
            }
            
            key_words.put(id, temp_nouns);
        }
        
        G = new Digraph(key_words.size());
        
        while (hyp_file.hasNextLine()) {
            String[] line = hyp_file.readLine().split(",");
            
            int id = Integer.parseInt(line[0]);

            for (int i = 1; i < line.length; i++) {
                G.addEdge(id, Integer.parseInt(line[i]));
            }
        }
        
    }
    
 // all WordNet nouns
    public Iterable<String> nouns() {
   /*      String[] words = new String[key_words.size()];
         
         for (int key : key_words.keys()) {
             String id;
             ResizingArrayQueue<String> temp = key_words.get(key);
             
             id = temp.dequeue();
                     
             for (String noun : temp)
                 words[key] += noun + " ";
             
         }
         
         return words;*/
        StdOut.println(G);
        return words_key.keys();
    }
 
    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        int noun1ID = this.words_key.get(noun1);
        int noun2ID = this.words_key.get(noun2);
        
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        
        return key_words.get(sca.ancestor(noun1ID, noun2ID)).dequeue();
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        int noun1ID = this.words_key.get(noun1);
        int noun2ID = this.words_key.get(noun2);
        
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        
        return sca.length(noun1ID, noun2ID);
    }
    
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (words_key.get(word) != null)
            return true;
        
        return false;
    }
    public static void main (String[] args) {
        WordNet wn = new WordNet ("./synsets.txt", "./hypernyms.txt");
        int newLine = 0;
        
        for (String noun : wn.nouns()) {
            newLine++;
            StdOut.print(newLine + ": " + noun + " -- ");
            
            if (newLine % 5 == 0)
                StdOut.println();
            
            if (newLine >= 200)
                break;
        }
    }
}
