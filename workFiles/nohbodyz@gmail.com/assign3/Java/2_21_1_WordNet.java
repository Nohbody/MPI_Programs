import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Stack;

public class WordNet 
{
    String syn, hyp;
    In synin, hypin;
    LinearProbingHashST<String, Integer> nouns;
    
        public WordNet(String synsets, String hypernyms)
        {
            syn = synsets;
            hyp = hypernyms;
            synin = new In(syn);
            hypin = new In(hyp);
            nouns = new LinearProbingHashST<String,Integer>(); 
            int i=0;
            while(synin.hasNextLine())
            {
                nouns.put(synin.readLine(), i);
                i++;
            }
        }
        
        public Iterable<String> nouns(){
  
            return nouns.keys();
            
        }
        
        //Word Must have ancestor of Noun
        public boolean isNoun(String word){
            
            return nouns.contains(word);
        }
        
        //Shortest common ancestor
        public String sca(String noun1, String noun2) {
            
            return "";
        }
        
        public int distance(String noun1, String noun2) {
            return 0;
        }
        
        public static void main(String[] args) {
            WordNet WN = new WordNet("synsets.txt", "hypernyms.txt");
            StdOut.println(WN.isNoun("hello"));
            StdOut.println(WN.nouns.contains("0,'hood,(slang) a neighborhood  \r\n" + 
                    ""));

        }
}
