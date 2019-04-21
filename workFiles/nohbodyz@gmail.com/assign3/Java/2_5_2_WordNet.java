import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
 
public class WordNet {

	private Digraph G; // Graph
	private SCA sca; // sca 

	private int graphLength = 0;// Length of the graph
	
	private Map<String, ArrayList<Integer>> nouns = new HashMap<String, ArrayList<Integer>>();// List of nouns <noun, List<synsetIds>>
	private Map<Integer, String> synsets = new HashMap<Integer, String>(); // Key value relationship between 
	private Map<Integer, ArrayList<Integer>> edges = new HashMap<Integer, ArrayList<Integer>>(); // Edges between vertices

	public WordNet(String synsets, String hypernyms) {


		processSynsets(synsets); //
		processHypernyms(hypernyms);
		
		// Construct the graph
		this.G = new Digraph(this.graphLength);

		for (Map.Entry<Integer, ArrayList<Integer>> entry : edges.entrySet()) {
			for (Integer w : entry.getValue()) {
				this.G.addEdge(entry.getKey(), w);
			}
		}



		

		this.sca = new SCA(this.G);
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return this.nouns.keySet();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return this.nouns.containsKey(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		
		
		return this.sca.length(this.nouns.get(nounA), this.nouns.get(nounB));
	}

	public String sca(String nounA, String nounB) {
		
		
		int ancestor = this.sca.ancestor(this.nouns.get(nounA),	this.nouns.get(nounB));

		return this.synsets.get(ancestor);
	}

	
	private void processSynsets(String syn) {
		
		In in = new In(syn);
		String line = null;

		ArrayList<Integer> currentNounsList = null;
		String currentSynsetNouns = null;
		
		while ((line = in.readLine()) != null) {

			if (line.equals("")) {
				continue;	
				}

			
			String[] Fields = line.split(","); // Separate each field by the comma
			String[] nouns = Fields[1].split(" "); // get the second field.
			int Alpha = Integer.parseInt(Fields[0]); // get the first field

			for (String n : nouns) {
				// check if noun exists in list
				if (this.nouns.containsKey(n)) { 
					currentNounsList = this.nouns.get(n);
				} 
				else {
					currentNounsList = new ArrayList<Integer>(); 
				}
				
				// check if synsetId exists in list
				if (this.synsets.containsKey(Alpha)) { 
					currentSynsetNouns = this.synsets.get(Alpha);
				} 
				else {
					currentSynsetNouns = new String(); 
				}
				
				
				currentNounsList.add(Alpha); 
				currentSynsetNouns = Fields[1];

				this.nouns.put(n, currentNounsList);
				this.synsets.put(Alpha, currentSynsetNouns);
			}
			
			this.graphLength++;
		}
	}

	private void processHypernyms(String hypernym) {
		
		In in = new In(hypernym);
	
		String line = null;
		
		ArrayList<Integer> edgeList;
		
		while ((line = in.readLine()) != null) {

			if (line.equals("")) {
				continue;	
				}


			String[] lineElements = line.split(",");//split line

			if (edges.get(Integer.parseInt(lineElements[0])) != null) {
				edgeList = edges.get(Integer.parseInt(lineElements[0]));
			} 
			else {
				edgeList = new ArrayList<Integer>();
			}

			for (int i = 1; i < lineElements.length; i++) {
				edgeList.add(Integer.parseInt(lineElements[i]));
			}

			edges.put(Integer.parseInt(lineElements[0]), edgeList);

		}
	}

	public static void main(String[] args) {
		WordNet w = new WordNet("synsets.txt", "hypernyms.txt");
		System.out.println("Boss and baby: " + w.sca("free", "hat")); 
		System.out.println("Boss and baby: " + w.distance("free", "hat")); 

		System.out.println("baby and stand: " + w.sca("baby", "stand")); 
		System.out.println("baby and stand: " + w.distance("baby", "stand")); 
	}

}
