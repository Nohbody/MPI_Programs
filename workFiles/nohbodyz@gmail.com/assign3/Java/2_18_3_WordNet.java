import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;

import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.ST;

public class WordNet {
	
	public Digraph G;
	
	public static final String SYSPATH = "/Users/jacklewis/dsaws/WordNet/synsets.txt";
	public static final String HYPPATH = "/Users/jacklewis/dsaws/WordNet/hypernyms.txt";

	private ArrayList<String> nounsList;
	private In synsetsIn, hypernymsIn;
	
	
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		//int id = 0;
		
		if (synsets == null || hypernyms == null) {
			throw new NullPointerException();
		}
		
		synsetsIn = new In(synsets);
		hypernymsIn = new In(hypernyms);
		
		String[] synsArr = synsetsIn.readAllLines();
		String[] hypsArr = hypernymsIn.readAllLines();
	
		G = new Digraph(synsArr.length);
		
		//adding edges to the digraph
		for(int i = 0; i < hypsArr.length; i++) {
			String[] line = hypsArr[i].split(",");
			
			for(int j = 0; j < line.length; j++) {
				G.addEdge(Integer.parseInt(hypsArr[i].split(",")[0]), Integer.parseInt(hypsArr[i].split(",")[j]));
			}
		}
		
		//associating the ids in the digraph to the words that correspond to those IDs
		nounsList = new ArrayList<String>();
		for(int i = 0; i < synsArr.length; i++) {
			nounsList.add(synsArr[i].split(",")[1]);
		}
			
	}
	
	// all WordNet nouns
	public Iterable<String> nouns(){
		return nounsList;
		
	}
	
	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		if(word == null) {
			throw new NullPointerException();
		}
		
		if (nounsList.contains(word)){
			return true;
		}
		else {
			return false;
		}
		
	}
	
	ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
	// a synset (second field of synsets.txt) that is a shortest common ancestor
	// of noun1 and noun2 (defined below)
	public String sca(String noun1, String noun2) {
		
		
		if (noun1 == null || noun2 == null) {
			throw new NullPointerException();
		}
		//input nouns should be wordnet nouns
		if (isNoun(noun1) == false || isNoun(noun2) == false) {
			throw new IllegalArgumentException();
		}
		
		int ancestor = sca.ancestor(nounsList.indexOf(noun1), nounsList.indexOf(noun2));
		return nounsList.get(ancestor); 

	}
	
	// distance between noun1 and noun2 (defined below)
	public int distance(String noun1, String noun2) {
		if (noun1 == null || noun2 == null) {
			throw new NullPointerException();
		}
		//input nouns should be wordnet nouns
		if (isNoun(noun1) == false || isNoun(noun2) == false) {
			throw new IllegalArgumentException();
		}
		int distance = sca.length(nounsList.indexOf(noun1), nounsList.indexOf(noun2));
		return distance;
	
	}
	
	// unit testing
	public static void main(String[] args) {
		
		WordNet wn = new WordNet(SYSPATH, HYPPATH);
		System.out.println(wn.G.toString());
		Iterable<String> nouns = wn.nouns();
		
		for(String noun : nouns) {
			System.out.println(noun);
		}
		
		System.out.println("Distance method: " + wn.distance("1870s", "A a"));
		
		System.out.println("isNoun Test: " + wn.isNoun("1870s"));
		System.out.println("isNoun Test: " + wn.isNoun("Monkey"));
		
	}

}
