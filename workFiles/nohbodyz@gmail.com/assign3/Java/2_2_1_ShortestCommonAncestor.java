import java.util.Arrays;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.MinPQ;

public class ShortestCommonAncestor {
 BreadthFirstDirectedPaths BFS;
 Digraph G;
 Graph G2;

 // constructor takes a rooted DAG as argument
 public ShortestCommonAncestor(Digraph G) {
  this.G = G;
 }

 // length of shortest ancestral path between v and w
 public int length(int v, int w) {
  BFS = new BreadthFirstDirectedPaths(G, v);
  return BFS.distTo(w);
 }

 // a shortest common ancestor of vertices v and w
 public int ancestor(int v, int w) {
  int anc = 0;
  Bag<Integer> myBag = new Bag<Integer>();
  MinPQ<Integer> myMinPQ = new MinPQ<Integer>();
  BreadthFirstDirectedPaths search1 = new BreadthFirstDirectedPaths(G, v);
  BreadthFirstDirectedPaths search2 = new BreadthFirstDirectedPaths(G, w);
  for (int i = 0; i < G.V(); i++) {
   if (search1.hasPathTo(i) && search2.hasPathTo(i)) {
    anc = i;
    myBag.add(i);
    break;
   }
  }
  for (int each : myBag) {
   myMinPQ.insert(search1.distTo(each));
  }
  return myMinPQ.delMin();
 }

 // length of shortest ancestral path of vertex subsets A and B
 public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
  MinPQ<Integer> myMinPQ = new MinPQ<Integer>();
  for (int A : subsetA)
   for (int B : subsetB)
    myMinPQ.insert(length(A, B));
  return myMinPQ.delMin();

 }

 // a shortest common ancestor of vertex subsets A and B
 public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
  MinPQ<Integer> myMinPQ = new MinPQ<Integer>();
  for (int A : subsetA)
   for (int B : subsetB)
    myMinPQ.insert(ancestor(A, B));
  return myMinPQ.delMin();

 }

 // do unit testing of this class
 public static void main(String[] args) {
  Digraph G = new Digraph(1000);
  G.addEdge(0, 1);
  G.addEdge(1, 25);
  G.addEdge(100, 50);
  G.addEdge(50, 25);
  G.addEdge(25, 1);
  ShortestCommonAncestor an = new ShortestCommonAncestor(G);
  StdOut.print(an.ancestor(0, 100));
 }
}