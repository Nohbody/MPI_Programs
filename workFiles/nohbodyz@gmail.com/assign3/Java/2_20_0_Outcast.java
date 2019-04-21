import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
	
		WordNet w;
	   public Outcast(WordNet wordnet)         // constructor takes a WordNet object
	   {
		   this.w = wordnet;
	   }
	   
	   public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
	   {
		   int dist_max = -1;
		   String ans = "";
		   for(String noun:nouns)
		   {
			   int dist = 0;
			   for(String noun1:nouns)
			   {
				   if(!noun.equals(noun1))
					   {

					   		int dis = this.w.distance(noun, noun1);
					   		dist +=dis;
					   		//System.out.println(dis + " " +noun+" "+ noun1 );
					   }
			   }
			   dist_max = Math.max(dist_max, dist);
			   //System.out.println(dist + " "+ noun);
			   if(dist_max == dist)
				   ans = noun;
		   }
		   return ans;
	   }
	   
	   public static void main(String[] args)  // see test client below
	   {
		   WordNet wordnet = new WordNet("synsets.txt","hypernyms.txt");
		    Outcast outcast = new Outcast(wordnet);
//		    for (int t = 2; t < args.length; t++) {
		        In in = new In("test.txt");
		        String[] nouns = in.readAllStrings();
		        StdOut.println("test.txt" + ": " + outcast.outcast(nouns));
//		    }
	   }
	}