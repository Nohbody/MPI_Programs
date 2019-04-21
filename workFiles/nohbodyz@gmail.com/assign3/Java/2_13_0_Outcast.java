/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rooster
 */



public class Outcast 
{
    private final WordNet w;
   public Outcast(WordNet wordnet)         // constructor takes a WordNet object
   {
       w = wordnet;
   }
   public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
   {
       int d[] = new int[nouns.length];
       int t = 0;
       for(int i=0; i<nouns.length; i++)
       {
           d[i] = 0;
           for(String n2 : nouns)
           {
               d[i] += w.distance(nouns[i], n2);
           }
       }
       
       for(int i = 1; i<d.length; i++)
       {
           if(d[t] < d[i]) t = i;
       }
       
       return nouns[t];
   }
   public static void main(String[] args)  // see test client below
   {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
        In in = new In(args[t]);
        String[] nouns = in.readAllStrings();
        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }

   }
}