public class Outcast {
	private WordNet wordN;
	
   public Outcast(WordNet wordn) { 
	   this.wordN = wordn;
   }        
   
   public String outcast(String[] nouns) { // Takes in a set of wordnet nouns
	   int nounsNum = nouns.length; //counts the number of nouns in the wordnet.
	   int[] dis = new int[nounsNum];	//creates an array to hold the distance of every node to every other node
	   for (int i = 0; i < nounsNum-1; i++) {
		   for (int j = i+1; j < nounsNum; j++) {
			   int tempDis = this.wordN.distance(nouns[i], nouns[j]);//
			   dis[i] += tempDis;
			   dis[j] += tempDis;
		   }
	   }
	   int maxDis = 0;
	   int maxIndex = 0;
	   for (int k = 0; k < nounsNum; k++) {//searches through the array to find the node with the highest distance from every other node
		   if (dis[k] > maxDis) {
			   maxDis = dis[k];
			   maxIndex = k;
		   }
	   }
	   return nouns[maxIndex];//returns the node that is most distant from every other node.
   }  
   
   public static void main(String[] args) {
	    WordNet wordnet = new WordNet(args[0], args[1]);
	    Outcast outcast = new Outcast(wordnet);
	    for (int t = 2; t < args.length; t++) {
	        In in = new In(args[t]);
	        String[] nouns = in.readAllStrings();
	        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
	    }
	} 
}
