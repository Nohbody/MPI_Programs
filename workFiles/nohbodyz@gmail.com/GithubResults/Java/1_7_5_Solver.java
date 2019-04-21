import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by zora on 2/27/16.
 */
public class Solver {
    private int moves = -1;
    private boolean solvable = false;
    private MinPQ<BoardNode> minPQBoardInitial = new MinPQ<BoardNode>();
    private MinPQ<BoardNode> minPQBoardTwin = new MinPQ<BoardNode>();
    private Stack<Board> stackBoard = new Stack<Board>();

    private class BoardNode implements Comparable<BoardNode> {
        private Board board;
        private BoardNode prev;
        private int moves;

        public int compareTo(BoardNode that) {
            if (board.manhattan() < that.board.manhattan()) return -1;
            else if (board.manhattan() > that.board.manhattan()) return 1;
            else if (moves < that.moves) return -1;
            else if (moves > that.moves) return 1;
            else return 0;
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new java.lang.NullPointerException("Board is empty");
        BoardNode boardNode = new BoardNode();
        boardNode.board = initial;
        boardNode.prev = null;
        boardNode.moves = 0;
        minPQBoardInitial.insert(boardNode);
        Board twin = initial.twin();
        boardNode = new BoardNode();
        boardNode.board = twin;
        boardNode.prev = null;
        boardNode.moves = 0;
        minPQBoardTwin.insert(boardNode);
        while ((!minPQBoardInitial.min().board.isGoal()) && !minPQBoardTwin.min().board.isGoal()) {
            BoardNode currBoardNode = minPQBoardInitial.min();
            minPQBoardInitial.delMin();
            //StdOut.println(currBoardNode.board);
            //StdOut.println(currBoardNode.board.neighbors());
            for (Board board:currBoardNode.board.neighbors()) {
                if ((currBoardNode.prev == null) || (!board.equals(currBoardNode.prev.board))) {
                    boardNode = new BoardNode();
                    boardNode.board = board;
                    boardNode.prev = currBoardNode;
                    boardNode.moves = currBoardNode.moves + 1;
                    minPQBoardInitial.insert(boardNode);
                }
            }
            currBoardNode = minPQBoardTwin.min();
            minPQBoardTwin.delMin();
            //StdOut.println(currBoardNode.board.manhattan());
            //StdOut.println(currBoardNode.board.neighbors());
            for (Board board:currBoardNode.board.neighbors()) {
                if ((currBoardNode.prev == null) || (!board.equals(currBoardNode.prev.board))) {
                    boardNode = new BoardNode();
                    boardNode.board = board;
                    boardNode.prev = currBoardNode;
                    //StdOut.println(boardNode.curr.manhattan());
                    minPQBoardTwin.insert(boardNode);
                }
            }

        }
        if (minPQBoardInitial.min().board.isGoal()) {
            solvable = true;
            moves = minPQBoardInitial.min().moves;
            BoardNode currBoardNode = minPQBoardInitial.min();
            //StdOut.println(0);
            while (currBoardNode.prev != null) {
                stackBoard.push(currBoardNode.board);
                currBoardNode = currBoardNode.prev;
            }
            stackBoard.push(initial);
            //StdOut.println(1);
        }
    }          // find a solution to the initial board (using the A* algorithm)

    public boolean isSolvable() {
        return solvable;
    }           // is the initial board solvable?

    public int moves() {
        return moves;
    }                    // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        else return stackBoard;
    }     // sequence of boards in a shortest solution; null if unsolvable

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int counter = 1;
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = Integer.parseInt(args[counter++]);
        Board initial = new Board(blocks);
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }       // solve a slider puzzle (given below)
}
