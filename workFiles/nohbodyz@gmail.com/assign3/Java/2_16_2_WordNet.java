import java.util.HashMap;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.DirectedCycle;


public class WordNet 
{
	private final HashMap<Integer, String> synsetMap;
	private final HashMap<String, Bag<Integer>> idMap;

	private final Digraph G;   
	public WordNet(String syn, String hyp)
	{
		In in = new In(syn);
		String delimiter = ",";

		synsetMap = new  HashMap<Integer, String>();
		idMap =  new HashMap<String, Bag<Integer>>();

		while (in.hasNextLine())
		{
			String synsetLine = in.readLine();
			String[] fields = synsetLine.split(delimiter);
			int id = Integer.parseInt(fields[0]);
			String synset = fields[1];
			synsetMap.put(id,synset); 
			String[] nouns = synset.split(" ");

			for(String noun : nouns)
			{
				if(idMap.containsKey(noun))
				{
					Bag <Integer> b = idMap.get(noun);
					b.add(new Integer(id));
					idMap.put(noun,b);
				}
				else
				{
					Bag <Integer> b = new Bag<Integer>();
					b.add(new Integer(id));
					idMap.put(noun,b);
				}
			}
		}

		Digraph digraph = new Digraph(synsetMap.size());
		G = digraph;

		in = new In(hyp);
		while (in.hasNextLine())
		{ 
			String line = in.readLine();
			String[] fields = line.split(delimiter);
			int id = Integer.parseInt(fields[0]);
			Bag<Integer> bag = new Bag<Integer>();

			for(int i = 1; i < fields.length; i++)
			{
				int f = Integer.parseInt(fields[i]);
				bag.add(new Integer(f)); 
				digraph.addEdge(id, f);
			}
		}

		DirectedCycle cycle1 = new DirectedCycle(this.G);
		if (cycle1.hasCycle()) 
		{
			throw new IllegalArgumentException("Not a valid DAG");
		}
		
		int dag = 0;
		for (int i = 0; i < G.V(); i++) 
		{
			if (!this.G.adj(i).iterator().hasNext())
				dag++;
		}

		if (dag != 1) 
		{
			throw new IllegalArgumentException("Not a rooted DAG");
		}
	}


	public Iterable<String> nouns()   
	{    
		return idMap.keySet();
	}   
	public boolean isNoun(String word)  
	{
		return idMap.containsKey(word);
	}          

	public int distance(String firstNoun, String secondNoun)    
	{
		if(!isNoun(firstNoun) || !isNoun(secondNoun))
		{
			throw new java.lang.IllegalArgumentException();
		}
		ShortestAncestor sap = new ShortestAncestor(G);
		Bag<Integer> bag =  idMap.get(firstNoun);
		Bag<Integer> bag1 =  idMap.get(secondNoun);

		return sap.length(bag, bag1);
	}
    
	public String sap(String firstNoun, String secondNoun)      
	{
		if(!isNoun(firstNoun) || !isNoun(secondNoun))
		{
			throw new java.lang.IllegalArgumentException();
		}    

		Bag<Integer> bag =  idMap.get(firstNoun);
		Bag<Integer> bag1 =  idMap.get(secondNoun);
		ShortestAncestor sap = new ShortestAncestor(G);

		return synsetMap.get(sap.ancestor(bag,bag1));    
	}
}