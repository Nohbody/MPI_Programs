
public class Outcast {
	private WordNet wordnet;
	
	//constructor
	public Outcast(WordNet wordnet) {
		if(wordnet == null) {
			throw new NullPointerException();
		}
		this.wordnet = wordnet;
		
	}
	//given an array of wordnet nouns, return an outcast
	public String outcast(String[] nouns) {
		if (nouns == null) {
			throw new NullPointerException();
		}
		String outcast = null;
		int maxDist = 0;
		
		//To identify an outcast, compute the sum of the distances between each noun and every other one
		for(int i = 0; i< nouns.length; i++) {
			int distance = 0;
			
			for(int j = 0; j< nouns.length; j++) {
				System.out.println(nouns[i]+" "+nouns[j]);
				if(!nouns[i].equals((nouns[j])))
				{
					distance = distance + wordnet.distance(nouns[i], nouns[j]);
				}
			
			}
			//and return the noun with the greatest distance
			if (distance > maxDist) {
				maxDist = distance;
				outcast = nouns[i];
			}
			
		}
		return outcast;
		
	}

	public static void main(String[] args) {
		String SYSPATH = "/Users/jacklewis/dsaws/WordNet/synsets.txt";
		String HYPPATH = "/Users/jacklewis/dsaws/WordNet/hypernyms.txt";
		WordNet wn = new WordNet(SYSPATH, HYPPATH);
		
		Outcast oc = new Outcast(wn);
		
		String[] arr = {"zymase", "1830s", "decade decennary decennium"};
		
		System.out.println(oc.outcast(arr));
		
		
	}

}
