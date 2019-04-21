/**
 * Created by zora on 4/25/16.
 */
public class CircularSuffixArray {
    private int length;
    private String catString;
    private CirString[] cirStr;

    private class CirString {
        private int begin;
        private int index;
    }

    private void sort() {
        int R = 256;
        CirString[] auxStr = new CirString[length];
        for (int d = length - 1; d >=0; d--) {
            int[] count = new int[R + 1];
            for (int i = 0; i < length; i++)
                count[catString.substring(cirStr[i].begin, cirStr[i].begin + length).charAt(d) + 1]++;
            for (int r = 0; r < R; r++)
                count[r+1] += count[r];
            for (int i = 0; i < length; i++)
                auxStr[count[catString.substring(cirStr[i].begin, cirStr[i].begin + length).charAt(d)]++] = cirStr[i];
            for (int i = 0; i < length; i++)
                cirStr[i] = auxStr[i];
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new java.lang.NullPointerException("String cannot be empty");

        length = s.length();
        catString = s + s.substring(0, length - 1);
        cirStr = new CirString[length];

        for (int i = 0; i < length; i++) {
            cirStr[i] = new CirString();
            cirStr[i].begin = i;
            cirStr[i].index = i;
        }

        sort();

        /*
        // For test output
        for (int i = 0; i < length; i++) {
            StdOut.println(catString.substring(cirStr[i].begin, cirStr[i].begin + length));
        }
        */
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length) throw new java.lang.IndexOutOfBoundsException("Index out of range.");
        return cirStr[i].index;
    }
    public static void main(String[] args) {
        CircularSuffixArray test = new CircularSuffixArray("ABRACADABRA!");

    }
    // unit testing of the methods (optional)
}
