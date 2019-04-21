import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SeparateChainingHashST;

public class WordNet {
	
	SeparateChainingHashST<String, Queue<Integer>> wordIntMap;
	SeparateChainingHashST<Integer, String> intWordMap;
	
	Digraph graph;
	ShortestCommonAncestor ShortestCommonAncestor;
	
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		
		if (synsets == null || hypernyms == null) throw new NullPointerException("Field cannot be empty");
		
			wordIntMap = new SeparateChainingHashST<>();
			intWordMap = new SeparateChainingHashST<>();
			int vertices = 0;
			
		In in = new In(synsets);
		
		while (in.hasNextLine()) {
			
			vertices++;
			String[] line = in.readLine().split(",");
			String[] words = line[1].split(" ");
			Integer number = Integer.valueOf(line[0]);
			intWordMap.put(Integer.valueOf(line[0]), line[1]);
			
			for (int i = 0; i < words.length; i++) {
				Queue<Integer> wordIntMapQueue = wordIntMap.get(words[i]);
				
				if (wordIntMapQueue == null) {
					wordIntMapQueue = new Queue<>();
					wordIntMapQueue.enqueue(number);
					wordIntMap.put(words[i], wordIntMapQueue);
				}
				
				else {
					
					if (!contains(wordIntMapQueue, number)) {
						wordIntMapQueue.enqueue(number);
					}
				}
			}
		}
		
		graph = new Digraph(vertices);
		in = new In(hypernyms);
		
		while (in.hasNextLine()) {
			String[] line = in.readLine().split(",");
			for (int i = 1; i < line.length; i++)
				graph.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[i]));
		}
		
		ShortestCommonAncestor = new ShortestCommonAncestor(graph);
		if (!ShortestCommonAncestor.isRooteddirectedAcyclicGraph()) throw new IllegalArgumentException("hypernyms must be a rooted directed Acyclic Graph");
	}
	
	private <Item> boolean contains (Iterable<Item> iterable, Item item) {
		
		for (Item query : iterable)
			
			if (query == item) return true;
			return false;
	}

	// all WordNet nouns
	public Iterable<String> nouns() {
		
		return wordIntMap.keys(); 
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		
		if (word == null) throw new NullPointerException("Field cannot be empty");
		
			return wordIntMap.contains(word);
	}


	 // a synset (second field of synsets.txt) that is a shortest common ancestor
	 // of noun1 and noun2 (defined below)
	public int distance(String nounA, String nounB) {
		
		if (nounA == null || nounB == null) throw new NullPointerException("Field cannot be empty");
		
		if (wordIntMap.get(nounA) == null || wordIntMap.get(nounB) == null)
			
			throw new IllegalArgumentException("Nouns must be contained in WordNet");
		
			Iterable<Integer> integerA = wordIntMap.get(nounA);
			Iterable<Integer> integerB = wordIntMap.get(nounB);
			return ShortestCommonAncestor.length(integerA, integerB);
	}

	// distance between noun1 and noun2 (defined below)
	public String ShortestCommonAncestor(String nounA, String nounB) {
		
		if (nounA == null || nounB == null) throw new NullPointerException("Field cannot be empty");
		
		if (wordIntMap.get(nounA) == null || wordIntMap.get(nounB) == null)
			throw new IllegalArgumentException("There must be nouns in the WordNet");
		
			Iterable<Integer> integerA = wordIntMap.get(nounA);
			Iterable<Integer> integerB = wordIntMap.get(nounB);
			return intWordMap.get(ShortestCommonAncestor.ancestor(integerA, integerB));
	}

	   // do unit testing of this class	
	public static void main(String[] args) {
		
		WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
		String word = "Montana";
		Integer number = 65;
		int graphNumber = 32;
		
		System.out.print(word + ":");
		
		for (Integer i : wordnet.wordIntMap.get(word))
			System.out.print(" " + i);
		
		System.out.println();
		
		System.out.println(number + ": " + wordnet.intWordMap.get(number));
		
		System.out.println("\nGraph Vertices: " + wordnet.graph.V() + ", Expected: 74510");
		System.out.println("Graph Edges: " + wordnet.graph.E() + ", Expected: 35624");
		
		System.out.print(graphNumber + ":");
		for (int num : wordnet.graph.adj(graphNumber))
			System.out.print(" " + num);
		
		System.out.println("\n\n\n");
		System.out.println(wordnet.ShortestCommonAncestor("car", "door"));
	}

}