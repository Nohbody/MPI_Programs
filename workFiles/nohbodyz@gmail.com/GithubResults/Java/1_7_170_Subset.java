import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by zora on 2/4/16.
 */
public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        String[] kstr = new String[k];
        Deque<String> stupid = new Deque<String>();
        int counter = 0, randpos;
        try {
            while (StdIn.hasNextChar()) { // && counter < 9) {
                String nextstr = StdIn.readString(), tmp;
                counter++;
                randpos = StdRandom.uniform(counter);
                if (randpos < k) {
                    tmp = kstr[randpos];
                    kstr[randpos] = nextstr;
                    if (counter <= k && randpos < counter - 1)
                        kstr[counter - 1] = tmp;
                }
                if (counter <= k) {
                    stupid.addLast(nextstr);
                }
            }
        }
        catch (java.util.NoSuchElementException e) {
            // do nothing
            }
        for (String x: kstr) {
            StdOut.println(x);
            }
    }
}
