/******************************************************************************
 *  Compilation:  javac Outcast.java
 *  Execution:    java  Outcast
 *  Dependencies: edu.princeton.cs.algs4.*;
 *
 *  @author(s)          Scott McKay
 *  @collaborator(s)    None
 *  @course             Data Structures
 *  @homework           Programming Homework 3: WordNet
 *  @copyright          None
 *  @date_created       Wednesday, November 8th, 2017 @8:26 p.m. MST
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
 *  NOTE: 
 *
 *  % java Outcast
 *
 ******************************************************************************/

import java.io.FileNotFoundException;


public class Outcast 
{
    private WordNet wordNet;
    //Outcast should only contain valid WordNet nouns
    public Outcast(WordNet wordNet)
    {
        this.wordNet = wordNet;
    }
    
    //Given an array of WordNet nouns, return an outcast.
    public String outcast(String[] nouns)
    {   
        for (int index = 0; index < nouns.length; index++)
        {
            if (!wordNet.isNoun(nouns[index]))
            {
                throw new java.lang.IllegalArgumentException(nouns[index] + "is not a WordNet noun");
            }
        }
        
        int max = 0;
        int indexOfMax = 0;
        for (int index_a = 0; index_a < nouns.length; index_a++)
        {
            int length = 0;
            for (int index_b = 0; index_b < nouns.length; index_b++)
            {
                length += wordNet.distance(nouns[index_a], nouns[index_b]);
            }
            if (max < length)
            {
                max = length;
                indexOfMax = index_a;
            }
        }  
        return nouns[indexOfMax];
    }
    
    public static void main(String[] args) throws FileNotFoundException 
    {       
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) 
        {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
