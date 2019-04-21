import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.sound.sampled.Line;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Stack;

public class WordNet {
 public File synsetsFile, hypernymsFile;
 int vertices;
 Digraph digraph;
 LinearProbingHashST<Integer, Synset> allSynsets;
 Synset[] all;

 public WordNet(String synsets, String hypernyms) throws FileNotFoundException {
  allSynsets = new LinearProbingHashST<Integer, Synset>();
  vertices = 0;
  synsetsFile = new File(synsets);
  hypernymsFile = new File(hypernyms);
  Scanner fileScanner = new Scanner(synsetsFile);
  Scanner initialScan = new Scanner(synsetsFile);
  while (initialScan.hasNextLine()) {
   vertices++;
   initialScan.nextLine();
  }
  all = new Synset[vertices];
  digraph = new Digraph(vertices);
  Scanner hScan = new Scanner(hypernymsFile);
  int i = 0;
  while (hScan.hasNextLine()) {
   String line = hScan.nextLine();
   Scanner lineScan = new Scanner(line).useDelimiter(",");
   String vString = lineScan.next();
   int v = Integer.parseInt(vString);
   while (lineScan.hasNext()) {
    String wString = lineScan.next();
    int w = Integer.parseInt(wString);
    digraph.addEdge(v, w);
   }
  }
  int j = 0;
  while (fileScanner.hasNextLine()) {
   String line = fileScanner.nextLine();
   Scanner lineScan = new Scanner(line).useDelimiter(",");
   String id = lineScan.next();
   int index = Integer.parseInt(id);
   Stack<String> set = new Stack<String>();
   String sets = lineScan.next();
   Scanner setScan = new Scanner(sets).useDelimiter(" ");
   while (setScan.hasNext())
    set.push(setScan.next());
   String gloss = lineScan.next();
   Synset synset = new Synset(index, set, gloss);
   all[j++] = synset;
   allSynsets.put(index, synset);
  }
 }

 private class Synset {
  protected int id;
  protected Stack<String> set;
  protected String gloss;

  public Synset(int id, Stack<String> set, String gloss) {
   this.id = id;
   this.set = set;
   this.gloss = gloss;
  }

  public String toString() {
   return id + "|||" + set + "|||" + gloss;
  }
 }

 public String sca(String noun1, String noun2) {
  if (isNoun(noun1) || isNoun(noun2)) {
   Bag<String> noun1Bag = new Bag<String>(), noun2Bag = new Bag<String>();
   int i = 0;
   int j = 0;
   String returnThis = "These words do not share a common ancestor";
   for (Synset s : all) {
    for (String z : s.set)
     if (z.equalsIgnoreCase(noun1)) {
      i = s.id;
      break;
     }
   }
   for (Synset s : all) {
    for (String z : s.set)
     if (z.equalsIgnoreCase(noun2)) {
      j = s.id;
      break;
     }
   }
   BreadthFirstDirectedPaths p1 = new BreadthFirstDirectedPaths(digraph, i);
   BreadthFirstDirectedPaths p2 = new BreadthFirstDirectedPaths(digraph, j);
   int k = 0;
   int y = 0;
   while (!p1.hasPathTo(k))
    k++;
   while (!p2.hasPathTo(y))
    y++;
   for (int t : p1.pathTo(k)) {
    for (String s : allSynsets.get(t).set)
     noun1Bag.add(s);
   }
   for (int t : p2.pathTo(y)) {
    for (String s : allSynsets.get(t).set)
     noun2Bag.add(s);
   }
   for (String noun1String : noun1Bag) {
    for (String noun2String : noun2Bag) {
     if (noun1String.equalsIgnoreCase(noun2String)) {
      returnThis = noun1String;
      break;
     }
    }
   }
   return returnThis;
  } else
   return "One or more of the words entered is not in the list of synsets";

 }

 public boolean isNoun(String noun) {
  boolean z = false;
  for (String test : nouns()) {
   if (noun.contains(test)) {
    z = true;
    break;
   } else
    z = false;
  }
  return z;
 }

 public Iterable<String> nouns() {

  return new Iterable<String>() {

   @Override
   public Iterator<String> iterator() {
    return new Iterator<String>() {
     Synset[] synsetArray = all;
     int i = 0;

     @Override
     public boolean hasNext() {
      return i <= all.length;
     }

     @Override
     public String next() {
      try {
       return synsetArray[i++].set.peek();
      } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      }
      return null;
     }

    };
   }
  };

 }

 public static void main(String[] args) throws FileNotFoundException {
  WordNet x = new WordNet("./synsets", "./hypernyms");
  StdOut.print(x.sca("university", "building"));

 }
}
