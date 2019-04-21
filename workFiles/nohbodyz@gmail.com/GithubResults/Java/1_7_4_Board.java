import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by zora on 2/27/16.
 */
public class Board {
    private int dimension;
    private int[][] blocks;

    public Board(int[][] blocks) {
        dimension = blocks.length;
        this.blocks = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.blocks[i][j] = blocks[i][j];
            }
        }
    }          // construct a board from an N-by-N array of blocks
               // (where blocks[i][j] = block in row i, column j)

    public int dimension() {
        return dimension;
    }                // board dimension N

    public int hamming() {
        int ham_score = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] != i * dimension + j + 1) ham_score++;
            }
        }
        ham_score--;  // Remove empty block(0)'s value - will always be 1
        return ham_score;
    }                  // number of blocks out of place

    public int manhattan() {
        int temp, mht_score = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] != 0) {
                    temp = absolute(j - (blocks[i][j] - 1) % dimension) + absolute(i - (blocks[i][j] - 1) / dimension);
                    mht_score += temp;
                    // StdOut.println(i + " " + j + " " + temp);
                }
            }
        }
        return mht_score;
    }               // sum of Manhattan distances between blocks and goal

    public boolean isGoal() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] != i * dimension + j + 1) {
                    if (blocks[i][j] != 0) return false;
                }
            }
        }
        return true;
    }               // is this board the goal board?

    public Board twin() {
        Board twinBoard = new Board(blocks);
        if (twinBoard.blocks[0][0] == 0) swap(twinBoard, 0, 1, 1, 1);       //*********Why can I visit it? Yes cuz private/public is for class (abstract) not for instance
        else if (twinBoard.blocks[0][1] == 0) swap(twinBoard, 0, 0, 1, 0);
        else swap(twinBoard, 0, 0, 0, 1);
        return twinBoard;
    }                   // a board that is obtained by exchanging any pair of blocks

    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] != that.blocks[i][j]) return false;
            }
        }
        return true;
    }      // does this board equal y?

    public Iterable<Board> neighbors() {            //************Implement Iterable???
        Queue<Board> iterBoard = new Queue<Board>();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] == 0) {
                    if (i > 0) {
                        Board neighborBoard = new Board(blocks);
                        swap(neighborBoard, i, j, i - 1, j);
                        iterBoard.enqueue(neighborBoard);
                    }
                    if (i < dimension - 1) {
                        Board neighborBoard = new Board(blocks);
                        swap(neighborBoard, i, j, i + 1, j);
                        iterBoard.enqueue(neighborBoard);
                    }
                    if (j > 0) {
                        Board neighborBoard = new Board(blocks);
                        swap(neighborBoard, i, j, i, j - 1);
                        iterBoard.enqueue(neighborBoard);
                    }
                    if (j < dimension - 1) {
                        Board neighborBoard = new Board(blocks);
                        swap(neighborBoard, i, j, i, j + 1);
                        iterBoard.enqueue(neighborBoard);
                    }
                }
            }
        }
        return iterBoard;
    }    // all neighboring boards

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
        /*
        String ans = dimension + "\n";
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                ans = ans + ' ' + blocks[i][j];
            }
            ans += '\n';
        }

        return ans;
    }              // string representation of this board (in the output format specified below)
    */

    private int absolute(int a) {
        if (a < 0) return -a;
        else return a;
    }

    private void swap(Board twinBoard, int x1, int y1, int x2, int y2) {
        int temp = twinBoard.blocks[x1][y1];
        twinBoard.blocks[x1][y1] = twinBoard.blocks[x2][y2];
        twinBoard.blocks[x2][y2] = temp;
    }

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int counter = 1;
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = Integer.parseInt(args[counter++]);
        Board initial = new Board(blocks);
        Board twin = initial.twin();
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = Integer.parseInt(args[counter++]);
        Board last = new Board(blocks);

        /*
        StdOut.println(initial);
        StdOut.println(initial.isGoal());
        StdOut.println(initial.equals(initial));
        StdOut.println(initial.manhattan());
        StdOut.println("--------------");
        StdOut.println(twin);
        StdOut.println(twin.isGoal());
        StdOut.println(twin.equals(initial));
        StdOut.println(last.manhattan());
        StdOut.println("--------------");
        */
        StdOut.println(last);
        StdOut.println(last.isGoal());
        StdOut.println(last.equals(initial));
        StdOut.println(last.manhattan());
        StdOut.println("--------------");

        for (Board board : initial.neighbors()) {
            StdOut.println(board);
            StdOut.println(board.manhattan());
            StdOut.println("--------------");
        }
    }           // unit tests (not graded)
}
