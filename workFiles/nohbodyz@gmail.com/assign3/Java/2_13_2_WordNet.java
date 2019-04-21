/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rooster
 */


import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Bag;
import java.util.ArrayList;

public class WordNet 
{
    private final ArrayList<String[]> synsets;
    private final Digraph d;
    private final RedBlackBST<String,Bag<Integer>> nouns;
    private final ShortestCommonAncestor SCA;
   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms)
   {
       if(synsets == null || hypernyms == null) throw new NullPointerException();
       
       this.synsets = new ArrayList<>();
       In in = new In(synsets);
       
       while(in.hasNextLine())
       {
           String temp = in.readLine();
           String[] t = temp.split(",");
           this.synsets.add(t[1].split("\\s+"));
       }
       
       this.synsets.trimToSize(); //Linear Space
       
       nouns = new RedBlackBST<>();
       for(int i = 0; i<this.synsets.size();i++)
       {
           for(String a : this.synsets.get(i))
           {
               if(nouns.contains(a))
               {
                   nouns.get(a).add(i);
               }
               else
               {
                    Bag<Integer> b = new Bag<>();
                    b.add(i);
                    nouns.put(a, b);
               }
           }
       }
       
       d = new Digraph(this.synsets.size());
       
       in = new In(hypernyms);
       while(in.hasNextLine())
       {
           String temp = in.readLine();
           String t[] = temp.split(",");
           int v = Integer.parseInt(t[0]);
           int e[] = new int[t.length-1];
           for(int i=1;i<t.length;i++)
           {
               e[i-1] = Integer.parseInt(t[i]);
           }
           for(int ed : e)
           {
               d.addEdge(v, ed);
           }
           
       }
      // d = new Digraph(new edu.princeton.cs.algs4.In(hypernyms));
       SCA = new ShortestCommonAncestor(d);
       
   }

   // all WordNet nouns
   public Iterable<String> nouns()
   {
       return nouns.keys();
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word)
   {
       if(word == null) throw new NullPointerException("Word not in synset");
       return (nouns.get(word) != null);
   }

   // a synset (second field of synsets.txt) that is a shortest common ancestor
   // of noun1 and noun2 (defined below)
   public String sca(String noun1, String noun2)
   {
       if(noun1 == null || noun2 == null) throw new NullPointerException();
       if(!(isNoun(noun1) && isNoun(noun2))) throw new IllegalArgumentException();
       int i = SCA.ancestor(nouns.get(noun1), nouns.get(noun2));
       String t = "";
       for(String s : synsets.get(i))
       {
           t = t + s + " ";
       }
       t = t.trim();
       return t;
   }

   // distance between noun1 and noun2 (defined below)
   public int distance(String noun1, String noun2)
   {
       if(noun1 == null || noun2 == null) throw new NullPointerException();
       if(!(isNoun(noun1) && isNoun(noun2))) throw new IllegalArgumentException();
       return SCA.length(nouns.get(noun1), nouns.get(noun2));
   }

   // do unit testing of this class
   public static void main(String[] args)
   {
       if(args.length < 2)
       {
           System.out.println("Requires the input files");
           System.out.println("Try $ java-alg4 WordNet synsets.txt hypernyms.txt");
       }
       
       try
       {
       WordNet w = new WordNet(args[0],args[1]);
      /* for(String n : w.nouns())
       {
           System.out.println(n);
       }*/
       
       edu.princeton.cs.algs4.In in = new edu.princeton.cs.algs4.In(args[0]);
        while (!StdIn.isEmpty()) 
        {
            try
            {
                String a = StdIn.readString();
                String b = StdIn.readString();
                int length = w.distance(a, b);
                String ancestor = w.sca(a, b);
                if(ancestor.equals(a) || ancestor.equals(b)) length++;
                System.out.println("length = " + length + " ancestor = " + ancestor);
            }
            
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                //e.printStackTrace();
            }
        }
       }
       
       catch(Exception e)
       {
           System.out.println("Error");
       }
   }
}
