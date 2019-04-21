/**
 * Created by zora on 4/18/16.
 */
import java.util.Iterator;

public class TrieAlphabet {
    private static final int R = 26;        // extended ASCII

    private Node root;      // root of trie
    private int N;          // number of keys in trie

    // R-way trie node
    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
    }

    public TrieAlphabet() { }

    /**
     * Does the set contain the given key?
     * @param key the key
     * @return <tt>true</tt> if the set contains <tt>key</tt> and
     *     <tt>false</tt> otherwise
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public boolean contains(StringBuilder key) {
        Node x = get(root, key.toString(), 0);
        if (x == null) return false;
        return x.isString;
    }

    public boolean contains(String key) {
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }


    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c - 65], key, d+1);
    }

    /**
     * Adds the key to the set if it is not already present.
     * @param key the key to add
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void add(String key) {
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (!x.isString) N++;
            x.isString = true;
        }
        else {
            char c = key.charAt(d);
            x.next[c - 65] = add(x.next[c - 65], key, d+1);
        }
        return x;
    }

    public boolean hasPrefix(StringBuilder prefix) {
        Node x = get(root, prefix.toString(), 0);
        if (x != null) return true;
        else return false;
    }
}
