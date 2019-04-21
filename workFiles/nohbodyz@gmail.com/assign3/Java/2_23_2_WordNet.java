import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.LinearProbingHashST;

public class WordNet {
    Digraph wordNet;
    Scanner scan;
    Scanner lineScanner;
    LinearProbingHashST<String, Integer> hash;
    LinearProbingHashST<Integer, String> hasha;
    ShortestCommonAncestor sca;
    

    public WordNet(String synsets, String hypernyms) {
      LinkedList<String> list = reader(synsets);
      int size = list.size();
      hash = new LinearProbingHashST<String, Integer>(size);
      hasha = new LinearProbingHashST<Integer, String>(size);
      wordNet = new Digraph(size);
      In hyp = new In(hypernyms);
      while (hyp.hasNextLine()) {
        scan = new Scanner(hyp.readLine()).useDelimiter(",");
        int x = scan.nextInt();
        while (scan.hasNext()) {
            wordNet.addEdge(x, scan.nextInt());  
        }
      }
      Iterator<String> it = list.iterator();
      while (it.hasNext()) {
          scan = new Scanner(it.next()).useDelimiter(",");
          int v = scan.nextInt();
          hasha.put(v, lineScanner.next());
          lineScanner = new Scanner(scan.next()).useDelimiter("//s");
          while (lineScanner.hasNext()) {
              hash.put(lineScanner.next(), v);
          }     
      }
    }
    
    private LinkedList<String> reader(String fileName){
        LinkedList<String> list = new LinkedList<String>();
        In syn = new In(fileName);
        while (syn.hasNextLine()) {
            list.add(syn.readLine());
        }
        return list;
    }
    
    public boolean isNoun(String word) {
        return hash.contains(word);
    }
    
 // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        return hasha.get(sca.ancestor(hash.get(noun1), hash.get(noun2)));
    }
    
    public int distance(String noun1, String noun2) {
        return sca.length(hash.get(noun1), hash.get(noun2));
    }
    
    public Iterable<String> Nouns() {
        return hash.keys();
    }
    
    

    public static void main(String[] args) {
        WordNet net = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println(net.wordNet);
        System.out.println("Is twak a word:" + net.isNoun("twak"));
        System.out.println("Is work a word:" + net.isNoun("work"));
    }

}
