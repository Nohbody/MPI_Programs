import edu.princeton.cs.algs4.DepthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;

import java.util.Arrays;
import java.util.LinkedList;

public class ShortestCommonAncestor
{
    Digraph graph;

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G)
    {
        if (G == null) throw new java.lang.NullPointerException();
        graph = G;
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w)
    {
        Integer[] a = {v};
        Iterable<Integer> subsetA = Arrays.asList(a);
        Integer[] b = {w};
        Iterable<Integer> subsetB = Arrays.asList(b);
        int[] results = search(subsetA, subsetB);
        return results[3];
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w)
    {
        Integer[] a = {v};
        Iterable<Integer> subsetA = Arrays.asList(a);
        Integer[] b = {w};
        Iterable<Integer> subsetB = Arrays.asList(b);
        int[] results = search(subsetA, subsetB);
        return results[2];
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB)
    {
        if (subsetA == null || subsetB == null) throw new java.lang.NullPointerException();
        int[] results = search(subsetA, subsetB);
        return results[3];
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB)
    {
        if (subsetA == null || subsetB == null) throw new java.lang.NullPointerException();
        int[] results = search(subsetA, subsetB);
        return results[2];
    }

    public int[] search(Iterable<Integer> subsetA, Iterable<Integer> subsetB)
    {

        RedBlackBST<Integer, LinkedList<Integer>> aTable = new RedBlackBST<>();
        DepthFirstDirectedPaths aDFS;
        DepthFirstDirectedPaths bDFS;
        for (int a : subsetA)
        {
            aDFS = new DepthFirstDirectedPaths(graph, a);
            for (int v = 0; v < graph.V(); v++)
            {
                if (aDFS.hasPathTo(v))
                {
                    if (aTable.contains(v))
                    {
                        LinkedList value = aTable.get(v);
                        value.add(a);
                        aTable.put(v, value);
                    } else
                    {
                        LinkedList value = new LinkedList();
                        value.add(a);
                        aTable.put(v, value);
                    }
                }
            }
        }
        for (int b : subsetB)
        {
            bDFS = new DepthFirstDirectedPaths(graph, b);
            LinkedList<Integer> vKeys = new LinkedList<>();
            for (int v : aTable.keys()) vKeys.add(v);
            for(int v : vKeys)
            {
                if (!bDFS.hasPathTo(v))
                {
                    aTable.delete(v);
                }
            }
        }

        Iterable<Integer> path;
        int minA = -1;
        int minB = -1;
        int minAncestor = -1;
        int aLength = -1;
        int bLength = -1;
        int abLength = -1;
        int minLength = graph.V();

        for (int v : aTable.keys())
        {
            for (int a : subsetA)
            {
                aDFS = new DepthFirstDirectedPaths(graph, a);
                if (!aDFS.hasPathTo(v)) continue;
                aLength = 0;
                path = aDFS.pathTo(v);
                for (int i : path)
                {
                    aLength++;
                }
                for (int b : subsetB)
                {
                    bDFS = new DepthFirstDirectedPaths(graph, b);
                    if (!bDFS.hasPathTo(v)) continue;
                    bLength = 0;
                    path = bDFS.pathTo(v);
                    for (int i : path)
                    {
                        bLength++;
                    }
                    abLength = aLength + bLength;
                    if (abLength > minLength) continue;
                    minA = a;
                    minB = b;
                    minAncestor = v;
                    minLength = abLength;
                }
            }
        }
        int[] results = {minA, minB, minAncestor, minLength - 2};
        return results;
    }

    // do unit testing of this class
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty())
        {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}