import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by zora on 4/17/16.
 */

public class BoggleSolver {
    private TrieAlphabet dictTrie, solutionTrie;
    private StringBuilder word;
    private int[][] aux;
    private boolean[][]visited;
    private Bag<String> solution;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        // Use 26-way Trie to save dictionary
        dictTrie = new TrieAlphabet();
        for (String entry : dictionary) {
            dictTrie.add(entry);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {

        // Save all possible word in iterable Bag variable solution
        solution = new Bag<>();
        solutionTrie = new TrieAlphabet();
        visited = new boolean[board.rows()][board.cols()];

        // Save 8-way moves in aux array
        int size_aux_0 = 8, size_aux_1 = 2;
        aux = new int[size_aux_0][size_aux_1];
        for (int i = 0; i < size_aux_0; i++)
            for (int j = 0; j < size_aux_1; j++)
                if (i % 2 == 0)
                    aux[i][j] = 0;
                else
                    aux[i][j] = 1;
        aux[0][0] = -1;
        aux[1][0] = -1;
        aux[1][1] = -1;
        aux[2][1] = -1;
        aux[3][1] = -1;
        aux[4][0] = 1;
        aux[6][1] = 1;
        aux[7][0] = -1;

        // Mark all graid unvisited
        for (int i = 0; i < board.rows(); i++)
            for (int j = 0; j < board.cols(); j++)
                visited[i][j] = false;

        // Start new word
        word = new StringBuilder();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                addLetter(board.getLetter(i, j), i, j);
                // recursion
                findWords(board, i, j);
                removeLetter(i, j);
            }
        }
        return solution;
    }

    private void findWords(BoggleBoard board, int i, int j) {
        for (int k = 0; k < 8; k++) {
            int new_i = i + aux[k][0], new_j = j + aux[k][1];


            if (new_i >= 0 && new_i < board.rows() && new_j >= 0 && new_j < board.cols()
                    && !visited[new_i][new_j]) {

                addLetter(board.getLetter(new_i, new_j), new_i, new_j);

                if (dictTrie.contains(word) && word.length() > 2 && !solutionTrie.contains(word)) {
                    solution.add(word.toString());
                    solutionTrie.add(word.toString());
                }

                if (dictTrie.hasPrefix(word)) {
                    findWords(board, new_i, new_j);
                }

                removeLetter(new_i, new_j);
            }
        }
    }

    private void addLetter(char letter, int i, int j) {
        if (letter == 'Q')
            word.append("QU");
        else
            word.append(letter);
        visited[i][j] = true;
    }

    private void removeLetter(int i, int j) {
        try {
            if (word.charAt(word.length() - 2) == 'Q')
                word.delete(word.length() - 2, word.length());
            else
                word.delete(word.length() - 1, word.length());
        }
        catch (StringIndexOutOfBoundsException error) {
            word.delete(word.length() - 1, word.length());
        }
        visited[i][j] = false;
    }



    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (dictTrie.contains(word))
            switch (word.length()) {
                case 0:
                    return 0;
                case 1:
                    return 0;
                case 2:
                    return 0;
                case 3:
                    return 1;
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
                    return 5;
                default:
                    return 11;
            }
        else return 0;
    }

    public static void main(String[] args) {
        In inDict = new In(args[0]);
        String[] dict;
        Bag<String> bagDict = new Bag<>();

        while (inDict.hasNextLine())
            bagDict.add(inDict.readLine());
        dict = new String[bagDict.size()];
        int i = 0;
        for (String entry : bagDict) {
            dict[i++] = entry;
        }

        BoggleSolver boggleSolver = new BoggleSolver(dict);

/*
        StdOut.println(boggleSolver.containWord("ACCESS"));
        StdOut.println(boggleSolver.containWord("ACC"));
        StdOut.println(boggleSolver.containWord("ABS"));
        StdOut.println(boggleSolver.containWord("ABSOLUTELY"));
*/

        BoggleBoard board = new BoggleBoard(args[1]);
        for (String word: boggleSolver.getAllValidWords(board)) {
            StdOut.printf(word + " %d\n", boggleSolver.scoreOf(word));
        }
    }
}
