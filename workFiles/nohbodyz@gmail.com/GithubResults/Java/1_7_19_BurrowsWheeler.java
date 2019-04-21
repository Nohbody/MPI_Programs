import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;

/**
 * Created by zora on 4/25/16.
 */
public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        StringBuilder s = new StringBuilder();
        int counter = 0;
        while (true)
            try {
                s = s.append(BinaryStdIn.readChar());
                //if (++counter == 12) break;
            } catch (RuntimeException err) { break; }

        CircularSuffixArray csa = new CircularSuffixArray(s.toString());

        for (int i = 0; i < csa.length(); i++)
            if (csa.index(i) == 0) BinaryStdOut.write(i);

        for (int i = 0; i < csa.length(); i++)
            try {
                BinaryStdOut.write(s.charAt((csa.index(i) - 1)));
            } catch (StringIndexOutOfBoundsException err) {
                BinaryStdOut.write(s.charAt((csa.length() + csa.index(i) - 1)));
            }

        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int first = BinaryStdIn.readInt();
        int[] codeBegin = new int[R];
        //Queue codeEnd = (Queue<Integer>) new Queue[R];
        ArrayList<Queue<Integer> > codeEnd = new ArrayList<Queue<Integer> >();
        for (int i = 0; i < R; i++) {
            codeBegin[i] = 0;
            codeEnd.add(new Queue<Integer>());
        }

        /*
         Read in t[] array (last letter array) in StringBuilder
         Use codeBegin[] array for reconstruction of begin[] array
         Use codeEnd[] bag array to write down the current order of each input
        */
        StringBuilder s = new StringBuilder();
        //String s = BinaryStdIn.readString();
        char readChar;
        int counter = 0;
        while (true)
            try {
                readChar = BinaryStdIn.readChar();
                s = s.append(readChar);
                codeBegin[readChar]++;
                codeEnd.get(readChar).enqueue(counter++);
                //if (counter == 12) break;
            } catch (RuntimeException err) { break; }


        /*
        // Check codeBegin[] and codeEnd[] array
        for (int i = 0; i < 256; i++) {
            if (codeBegin[i] != 0 || !codeEnd[i].isEmpty()) {
                StdOut.printf("%d   %d  ", i, codeBegin[i]);
                StdOut.println(codeEnd[i]);
            }
        }
        */

        /*
         Initialize arrays using length of the read-in char array
         Reconstruct begin[] array using counting information in codeBegin[] array
         Reconstruct next[] array based on frequency of char as shown in codeBegin[] array
        */
        int len = s.length();
        char[] begin = new char[len];
        int[] next = new int[len];

        int j = 0; // Iterate through N
        for (int i = 0; i < 256; i++) {
                // Construct begin[] and next[] array
                for (int k = 0; k < codeBegin[i]; k++) {
                    next[j] = codeEnd.get(i).dequeue();
                    begin[j++] = (char) i;
                }
        }

        // Reconstruct origin array from next[] array and first
        j = first; // Iterate through N based on next[] array
        for (int i = 0; i < len; i++) {
            BinaryStdOut.write(begin[j]);
            j = next[j];
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
    }
}
