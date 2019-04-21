import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import edu.princeton.cs.algs4.Digraph;

public class WordNet {

	HashMap <String,Integer> noun_map;
	HashMap <Integer,String> noun_map_rev;
	Digraph g;
	   // constructor takes the name of the two input files
	   public WordNet(String synsets, String hypernyms)
	   {
			HashMap <Integer,Integer[]> graph = new HashMap<Integer,Integer[]>();
		   this.noun_map = new HashMap<String,Integer>();
		   this.noun_map_rev = new HashMap<Integer,String>();
		   
		   	String csvFile = synsets;
			BufferedReader br = null;
			String line = "";
			String SplitBy = ",";
			
			try {
			
			    br = new BufferedReader(new FileReader(csvFile));
			    while ((line = br.readLine()) != null) {
			
			        // use comma as separator
			        String[] tokens = line.split(SplitBy);
			
//			        System.out.println("Noun [index= " + tokens[0] + " , nouns=" + tokens[1] + " , def=" + tokens[2] + "]");
			        Integer id = new Integer(tokens[0]); 
			        String noun = tokens[1];
			        this.noun_map_rev.put(id,noun);
			        tokens = noun.split(" ");
			        for(String token:tokens)
			        	{
			        		this.noun_map.put(token, id);
			        	}
			    }
			
			} catch (FileNotFoundException e) {
			    e.printStackTrace();
			} catch (IOException e) {
			    e.printStackTrace();
			} finally {
			    if (br != null) {
			        try {
			            br.close();
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			    }
			}
			
			

		   	csvFile = hypernyms;
			br = null;
			line = "";
			SplitBy = ",";
			int last= 0;
			try {
			
			    br = new BufferedReader(new FileReader(csvFile));
			    while ((line = br.readLine()) != null) {
			
			        // use comma as separator
			        String[] tokens = line.split(SplitBy);
			
//			        System.out.println("Noun [index= " + tokens[0] + " , parent=" + tokens[1] + "]");
			        Integer id = new Integer(tokens[0]);
			        last = id.intValue();
			        if(tokens.length>1){
//				        System.out.println("Noun [index= " + tokens[0] + " , parent=" + tokens[1] + "]");
				        Integer temp[] = new Integer[tokens.length-1];
				        int i = 0;
				        for(String token:tokens)
				        	{
				        		Integer t = new Integer(token);
				        		if(!t.equals(id))
				        			temp[i++] = t;
				        	}
				        graph.put(id, temp);
			        }
			    }
			
			} catch (FileNotFoundException e) {
			    e.printStackTrace();
			} catch (IOException e) {
			    e.printStackTrace();
			} finally {
			    if (br != null) {
			        try {
			            br.close();
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			    }
			}
//			while(last>0)
//				System.out.println(last);
			this.g = new Digraph(last+1);
			for(Integer key: graph.keySet())
			   {
					for(Integer i :graph.get(key))
					{
						//System.out.println(i);
						g.addEdge(key.intValue(),i.intValue());
					}
			   }
			
	   }
	   // all WordNet nouns
	   public Iterable<String> nouns()
	   {
		   ArrayList<String> nouns = new ArrayList<String>();
		   for(String key: this.noun_map.keySet())
		   {
			   nouns.add(key);
		   }
		   return nouns;
	   }
	   // is the word a WordNet noun?
	   public boolean isNoun(String word)
	   {
		   return this.noun_map.containsKey(word);
	   }
	   // a synset (second field of synsets.txt) that is a shortest common ancestor
	   // of noun1 and noun2 (defined below)
	   public String sca(String noun1, String noun2)
	   {
		   if(!isNoun(noun1))
		   {
			   System.out.println("Please enter valid nouns, noun1 is invalid!");
			   return "";
			   
		   }
			if(!isNoun(noun2))
		   {
			   System.out.println("Please enter valid nouns,noun1 is invalid!");
			   return "";
		   }
		   String s = "";
		   int v = this.noun_map.get(noun1);
		   int w = this.noun_map.get(noun2);
		   int ancestor = new ShortestCommonAncestor(this.g).ancestor(v, w);
		   s = this.noun_map_rev.get(ancestor);
		   return s;
	   }
	   // distance between noun1 and noun2 (defined below)
	   public int distance(String noun1, String noun2)
	   {
		   if(!isNoun(noun1))
		   {
			   System.out.println("Please enter valid nouns, noun1 is invalid!");
			   return -1;
			   
		   }
			if(!isNoun(noun2))
		   {
			   System.out.println("Please enter valid nouns,noun1 is invalid!");
			   return -1;
		   }
		   int v = this.noun_map.get(noun1);
		   int w = this.noun_map.get(noun2);
		   int length = new ShortestCommonAncestor(this.g).length(v, w);
		   return length;
	   }
	   // do unit testing of this class
	   public static void main(String[] args)
	   {
		   WordNet w = new WordNet("synsets.txt","hypernyms.txt");
//		   System.out.println(w.nouns());
//		   System.out.println(w.noun_map);
//		   System.out.println(w.noun_map_rev);
//		   System.out.println(w.g.toString());
		   System.out.println(w.sca("logic_gate", "AND_gate"));
		   System.out.println(w.distance("logic_gate", "AND_gate"));
		   
	   }
	   
}