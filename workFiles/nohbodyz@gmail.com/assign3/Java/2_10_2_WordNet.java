import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import edu.princeton.cs.algs4.Digraph;



public class WordNet {

	
	private String noun;
	private int index;
	
	private int hyperIndex;
	private int hyperNumber;
	
	private Digraph G;
	
	//BreadthFirstDirectedGraph (digraph, w) make two one with w the other with v
	
	
	private HashMap<Integer, String> synsetHash = new HashMap<Integer, String>();
	private HashMap<Integer, Integer> hypernymHash = new HashMap<Integer, Integer>();
	
	private ArrayList myArray = new ArrayList();
	//for hypernym hashMap <integer, arrayList<int>>
	//while the hash is being made then the digraph should be made at the same time
	//use add() for the array list section THIS MUST TAKE IN ALL VALUES WITH IN HYPernym
	//id = tokens[0]
	
	public HashMap synsetFileReader() throws FileNotFoundException
	{
		
	    File synsetFile = new File("C:/Users/Owner/workspaceCSCI232/WordNet/synsets.txt");
		Scanner synsetScan = new Scanner(synsetFile);
		
		while(synsetScan.hasNextLine())
		{
			String lineScanner = synsetScan.nextLine();
			Scanner synsetLineScan = new Scanner(lineScanner);
			synsetLineScan.useDelimiter(",");
		
			while(synsetLineScan.hasNext())
			{
				String indexS = synsetLineScan.next();
				int index = Integer.parseInt(indexS);
				
				String noun = synsetLineScan.next();
				
				
				
				String definiton = synsetLineScan.next();
				
				if(synsetLineScan.hasNext())
				{
					synsetLineScan.nextLine();
				}
				
				synsetHash.put(index, noun);
				//System.out.println("index: " + index + " noun: " + noun);
			
			}
				
			
		}
		//System.out.println(synsetHash.get(50000));
		return synsetHash;
		
	}
	
	
	public HashMap hypernymFileReader() throws FileNotFoundException
	{
		File hypernymFile = new File("C:/Users/Owner/workspaceCSCI232/WordNet/hypernyms.txt");
		Scanner hypernymScan = new Scanner(hypernymFile);
	
		while(hypernymScan.hasNextLine())
		{
			String lineScanner = hypernymScan.nextLine();
			Scanner hypernymLineScan = new Scanner(lineScanner);
			hypernymLineScan.useDelimiter(",");
		
			while(hypernymLineScan.hasNext())
			{
				
				String hyperIndexS = hypernymLineScan.next();
				
				int hyperIndex = Integer.parseInt(hyperIndexS);
				
				String hyperNumbersS = hypernymLineScan.next();
				
				int hyperNumbers = Integer.parseInt(hyperNumbersS);
				
				hypernymHash.put(hyperIndex, hyperNumbers);
				
				
				
				if(hypernymLineScan.hasNext())
				{
					hypernymLineScan.nextLine();
				}
				
				
				
				//38003 I changed
				//System.out.println(hyperIndex + " " + hyperNumber);
			
			}
		
		}
		return hypernymHash;
	}
	
	public Digraph wordNetDigraph()
	{
		for(int i = 0; i < hypernymHash.size(); i++)
		 {
			
			
			int v = hypernymHash.get(i);
			 //System.out.println(i);
			 G.addEdge(v, i);
		 }
		
		//G.addEdge(1, 2);
		//G.addEdge(2, 3);
		return G;
		
		
	}
	
   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms) throws FileNotFoundException
   {
	
	   
	 // synsets = synsetFileReader();
	  
	   //hypernyms = hypernymFileReader();
	   
	   //ask how to import the two files correctly
   
	   //create hashmap synsets<index,noun> USE THEM TO READ THE FILE IN
	   
	   //create hashmap hypernyms<Index, listof#>
	   
	   //function Readhypernyms
	   
	   //read in files however it works
	   
   
   }
   
   // all WordNet nouns
   public Iterable<String> nouns()
   {
	for(int i = 0; i < synsetHash.size(); i++)
	{
		String nouns = synsetHash.get(i);
		
	
	}
	   //create an iterator?
	   return nouns();
	   
   }
   
   // is the word a WordNet noun?
   public boolean isNoun(String word)
   {
	
	   for(int i = 0; i < synsetHash.size(); i++)
	{
		String nouns = synsetHash.get(i);
		   if(word.equals(nouns))
		{
			return true;
		}
	}
	return false;
	  
	   
   }
   
   // a synset (second field of synsets.txt) that is a shortest common ancestor
   // of noun1 and noun2 (defined below)
   public String sca(String noun1, String noun2)
   {
	return noun2;
	   
   }
   
   // distance between noun1 and noun2 (defined below)
   public int distance(String noun1, String noun2)
   {
	return 0;
	   
   }
   
  
   
   
   // do unit testing of this class
   public static void main(String[] args) throws FileNotFoundException
   {
	  
	   WordNet myNet = new WordNet(null, null);
	 myNet.synsetFileReader();
	   myNet.hypernymFileReader();
	 // myNet.wordNetDigraph();
	 System.out.println(myNet.synsetFileReader());
	   System.out.println(myNet.hypernymFileReader());
	  // System.out.println(myNet.isNoun("coating"));
	   //needs a check
	  // System.out.println(myNet.wordNetDigraph());
	   // read in the terms from a file
	   /*String filename = args[0];
	    In in = new In(filename);
	    int N = in.readInt();
	    Term[] terms = new Term[N];
	    for (int i = 0; i < N; i++) {
	        long weight = in.readLong();           // read the next weight
	        in.readChar();                         // scan past the tab
	        String query = in.readLine();          // read the next query
	        terms[i] = new Term(query, weight);    // construct the term
   */}
}