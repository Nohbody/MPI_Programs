import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;


/**
 * Created by zora on 4/25/16.
 */
public class MoveToFront {
    private static final int MAX = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        int[] code = new int[MAX];
        for (int i = 0; i < MAX; i++ ) code[i] = i;

        int counter = 0;
        int temp, next;
        char readChar;
        while (true) {
            try {
                readChar = BinaryStdIn.readChar();
                //StdOut.println(readChar);
                if (code[0] == readChar) {
                    StdOut.println(readChar);
                    BinaryStdOut.write(0, 8);
                } else {
                    next = code[0];
                    for (byte i = 1; i < MAX; i++) {

                        if (code[i] != readChar) {
                            temp = code[i];
                            code[i] = next;
                            next = temp;
                        } else {
                            BinaryStdOut.write(i, 8);
                            code[0] = code[i];
                            code[i] = next;
                            break;
                        }
                    }
                }
                //if (++counter == 13) break;
            } catch (RuntimeException err) { break; }
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        int[] code = new int[MAX];
        for (int i = 0; i < MAX; i++ ) code[i] = i;

        int counter = 0;
        int readByte, first;
        while (true) {
            try {
                readByte = BinaryStdIn.readByte();
                first = code[readByte];
                // StdOut.println(first);
                BinaryStdOut.write(first, 8);
                for (int i = readByte; i > 0; i--) code[i] = code[i - 1];
                code[0] = first;
                // if (++counter == 12) break;
            } catch (RuntimeException err) { break; }
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
    }
}
