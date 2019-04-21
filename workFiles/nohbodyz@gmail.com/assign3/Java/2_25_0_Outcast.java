import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

	private WordNet word;
	
	// constructor takes a WordNet object
	public Outcast(WordNet wordnet){
		this.word = wordnet;
	}
 
	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns){
		
		//  table to store the total distance with noun
		ST<Integer, String> st = new ST<>();
		
		// Max priority queue to keep the biggest distance noun
		MaxPQ<Integer> maxPQ = new MaxPQ<>();
		
		for(int i=0; i<nouns.length; i++){
			int distance = 0;
			for(int j=0; j<nouns.length; j++){
				distance += word.distance(nouns[i], nouns[j]);
			}
			
			maxPQ.insert(distance);
			st.put(distance, nouns[i]);
		}
		
		// Returns  noun with the greatest distance
		return st.get(maxPQ.max());
	}
   
	// see test client below
	public static void main(String[] args){
		
		String[] nounList = {"Batman", "Flash", "Aquaman", "Superman", "Cyborg"};
		WordNet wordnet = new WordNet("hypernyms.txt", "synsets.txt");
		
	    Outcast outcast = new Outcast(wordnet);
	    
	    for (int t = 0; t < nounList.length; t++) {
	    	
	        StdOut.println(nounList[t] + ": " + outcast.outcast(nounList));
	    }
	}
}