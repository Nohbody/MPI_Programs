/******************************************************************************
 *  Compilation:  javac WordNet.java
 *  Execution:    java  WordNet
 *  Dependencies: edu.princeton.cs.algs4.*;
 *
 *  @author(s)          Scott McKay
 *  @collaborator(s)    Jack Cummings 
 *  @course             Data Structures
 *  @homework           Programming Homework 3: WordNet
 *  @copyright          None
 *  @date_created       Tuesday, November 7th, 2017 @3:52 p.m. MST
 *
 *     Interface for acyclic tree of words.  
 *
 *     *
 *
 *     *
 *
 *     *
 *
 *  BUG:
 *
 *  FEATURE:
 *
 *  NOTE: Collaborator Jack Cummings gave a hint on how to instantiate a directional graph of hypernyms.txt when the amount of vertices and edges isn't specified.
 *
 *  % java WordNet
 *
 ******************************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.ST;

public class WordNet 
{
    private ShortestCommonAncestor sca;
    private ST<String, Integer>    stSynsets;
    private ST<Integer, String[]>  reverseStSynsets;
    
    //Constructor takes the name of the two input files.
    public WordNet(String synsets, String hypernyms) throws FileNotFoundException
    {   
        //If file-path of synsets or hypernyms is null.
        if ( (synsets == null) || (hypernyms == null) )
        {
            throw new java.lang.NullPointerException("Path to synsets or hypernyms cannot be null.");
        }
        
        //Create Symbol Table
        initST(synsets);
        
        //Create DiGraph and Properties.
        initSCA(hypernyms);
    }
    
    //Initialize Shortest Common Ancestor
    private void initSCA(String hypernyms) throws FileNotFoundException
    {
        File    hypernymsPath = new File(hypernyms);
        Scanner fileScanner   = new Scanner(hypernymsPath);
        Digraph digraph = new Digraph(stSynsets.size());
        
        //Construct the directional graph from the file.
        while (fileScanner.hasNext())
        {
            String  currentLine = fileScanner.nextLine();
            Scanner lineScanner = new Scanner(currentLine);
            lineScanner.useDelimiter(",");
            
            int currentVertex = lineScanner.nextInt();
            
            //While the current vertex has adjacent vertices...
            while(lineScanner.hasNext())
            {
                //Get that adjacent vertex and add it to the adjacency list of that vertex:
                int adjacentVertex = lineScanner.nextInt();
                digraph.addEdge(currentVertex, adjacentVertex);
            }
            lineScanner.close();
        }
        fileScanner.close();
        
        sca = new ShortestCommonAncestor(digraph);
    }
    
    private void initST(String path) throws FileNotFoundException
    {
        File    synsetsPath = new File(path);
        Scanner fileScanner = new Scanner(synsetsPath);
        
        stSynsets        = new ST<String, Integer>();
        reverseStSynsets = new ST<Integer, String[]>();
     
        //Construct the Symbol Table
        while(fileScanner.hasNext())
        {
            String currentLine = fileScanner.nextLine();
            Scanner lineScanner = new Scanner(currentLine);
            lineScanner.useDelimiter(",");
            
            int  synsetID  = lineScanner.nextInt();
            String token   = lineScanner.next();
            
            //Split any string that has multiple words with the same definition.
            //Example: "logic_gate" constitutes as one word but "logic gate" constitutes as two different words, but with same key.
            String[] keys = token.split("\\s+");
            
            //Iterate through the array of split words, add each word (key) to the symbol table with its value.
            for (int index = 0; index < keys.length; index++)
            {
                //System.out.println("Adding Key: " + keys[index] + " with value: " + value);
                stSynsets.put(keys[index].toLowerCase(), synsetID);
            }
            
            reverseStSynsets.put(synsetID, keys);
            lineScanner.close();
        }
        fileScanner.close();
        //System.out.println("Yellowcake Value: " + stSynsets.get("yellowcake"));
    }
    
    //Get all the words associated with a value.
    private String getAssociatedWords(int value)
    {
        if (value < 0 || value > reverseStSynsets.size())
        {
            throw new java.lang.IndexOutOfBoundsException();
        }
        
        String[] associatedWordsArr = reverseStSynsets.get(value);
        String associatedWords = "";
        
        for (int index = 0; index < associatedWordsArr.length; index++)
        {
            //If-else statement to ensure that no comma is added to the beginning of the associated words list.
            if (index == 0)
            {
                associatedWords = associatedWordsArr[index];
            }
            else
            {
                associatedWords = associatedWords + "," + associatedWordsArr[index];
            }
        }
        return associatedWords;
    }
    
    //All WordNet nouns
    public Iterable<String> nouns()
    {
        return stSynsets.keys();
    }
    
    //Is the word a WordNet noun
    public boolean isNoun(String word)
    {
        if (word == null)
        {
            throw new java.lang.NullPointerException("Word cannot be null");
        }

        return stSynsets.contains(word);
    }
    
    //A synset (second field of synsets.txt) that is the shortest common ancestor of noun1 and noun2
    public String sca(String noun1, String noun2) //sca == shortest common ancestor
    {
        //Requirements:
        //  Must make exactly one call to length() and ancestor() methods in ShortestCommonAncestor
        if ((noun1 == null) || (noun2 == null))
        {
            throw new java.lang.NullPointerException("Noun1 or noun2 cannot be null.");
        }
        //If noun1 and noun2 are a WordNet noun, throw an illegal argument exception.
        if (!isNoun(noun1) || !isNoun(noun2))
        {
            throw new java.lang.IllegalArgumentException();
        }

        //Get the ID's of noun1 and noun2
        int noun1ID = stSynsets.get(noun1.toLowerCase());
        int noun2ID = stSynsets.get(noun2.toLowerCase());
        
        //Find the ID of the shortest common ancestor of noun1 and noun2
        int ancestorVal = sca.ancestor(noun1ID, noun2ID);
        
        //Return all words associated with the ID of that shortest common ancestor.
        return getAssociatedWords(ancestorVal);
    }
    
    //Distance between noun1 and noun2
    public int distance(String noun1, String noun2)
    {
        //Requirements:
        //  Must make exactly one call to length() and ancestor() methods in ShortestCommonAncestor
        if ((noun1 == null) || (noun2 == null))
        {
            throw new java.lang.NullPointerException("Noun1 or noun2 cannot be null.");
        }
        //If noun1 and noun2 are a WordNet noun, throw an illegal argument exception.
        if (!isNoun(noun1) || !isNoun(noun2))
        {
            throw new java.lang.IllegalArgumentException();
        }
        
        return sca.length(stSynsets.get(noun1.toLowerCase()), stSynsets.get(noun2.toLowerCase()));
    }
    
    //Unit Testing
    public static void main(String[] args) throws FileNotFoundException 
    {
        StdOut.println("WordNet: Programmed by Scott McKay");
        //WordNet wordNet = new WordNet("synsets.txt","hypernyms.txt");
        WordNet wordNet = new WordNet(args[0], args[1]);
        Scanner input = new Scanner(System.in);
        
        StdOut.println("Enter \"-1\" to exit program. (Quotes Emitted)");
        while (true)
        {
            //Get noun1 from user:
            StdOut.println("Enter noun1: ");
            String noun1 = input.nextLine();
            while (!wordNet.isNoun(noun1) && !noun1.equals("-1"))
            {
                StdOut.println(noun1 + " is not a WordNet noun.  Please try again.");
                noun1 = input.nextLine();
            }
            if (noun1.equals("-1"))
                break;
            
            //Get noun2 from user:
            StdOut.println("Enter noun2: ");
            String noun2 = input.nextLine();
            while (!wordNet.isNoun(noun2) && !noun2.equals("-1"))
            {
                StdOut.println(noun2 + " is not a WordNet noun.  Please try again.");
                noun2 = input.nextLine();
            }
            if (noun2.equals("-1"))
                break;
            StdOut.println("You entered: '" + noun1 + "' and '" + noun2 + "'");
            
            //Print the shortest common ancestor and length of noun1 and noun2:
            String ancestor   = wordNet.sca(noun1, noun2);
            int distance = wordNet.distance(noun1, noun2);
            StdOut.println("Distance is: " + distance + " and shortest common ancestor(s) are: " + ancestor);
            System.out.println("\n");
        }
        input.close();
        
        System.out.println("Exit Success!");
    }
}