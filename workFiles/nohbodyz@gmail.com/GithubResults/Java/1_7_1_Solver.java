import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

/**
 * Created by zora on 2/27/16.
 */
public class Solver {
    private int moves = 0;
    private boolean solvable = false;
    private MinPQ<BoardNode> minPQBoardInitial = new MinPQ<BoardNode>();
    private MinPQ<BoardNode> minPQBoardTwin = new MinPQ<BoardNode>();
    private Queue<Board> queueBoard = new Queue<Board>();

    private class BoardNode implements Comparable<BoardNode>{
        private Board curr;
        private Board prev;

        public int compareTo(BoardNode that) {
            if (curr.manhattan() < that.curr.manhattan()) return -1;
            else if (curr.manhattan() > that.curr.manhattan()) return 1;
            else return 0;
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new java.lang.NullPointerException("Board is empty");
        BoardNode boardNode = new BoardNode();
        boardNode.curr = initial;
        boardNode.prev = null;
        minPQBoardInitial.insert(boardNode);
        Board twin = initial.twin();
        boardNode = new BoardNode();
        boardNode.curr = twin;
        boardNode.prev = null;
        minPQBoardTwin.insert(boardNode);
        while ((!minPQBoardInitial.min().curr.isGoal()) && !minPQBoardTwin.min().curr.isGoal()) {
            BoardNode currBoardNode = minPQBoardInitial.min();
            minPQBoardInitial.delMin();
            queueBoard.enqueue(currBoardNode.curr);
            //StdOut.println(currBoardNode.curr);
            //StdOut.println(currBoardNode.curr.neighbors());
            for (Board board:currBoardNode.curr.neighbors()) {
                if (!board.equals(currBoardNode.prev) ) {
                    boardNode = new BoardNode();
                    boardNode.curr = board;
                    boardNode.prev = currBoardNode.curr;
                    minPQBoardInitial.insert(boardNode);
                }
            }
            currBoardNode = minPQBoardTwin.min();
            minPQBoardTwin.delMin();
            //StdOut.println(currBoardNode.curr.manhattan());
            //StdOut.println(currBoardNode.curr.neighbors());
            for (Board board:currBoardNode.curr.neighbors()) {
                if (!board.equals(currBoardNode.prev)) {
                    boardNode = new BoardNode();
                    boardNode.curr = board;
                    boardNode.prev = currBoardNode.curr;
                    //StdOut.println(boardNode.curr.manhattan());
                    minPQBoardTwin.insert(boardNode);
                }
            }
            moves++;
        }
        if (minPQBoardInitial.min().curr.isGoal()) solvable = true;

    }          // find a solution to the initial board (using the A* algorithm)
    public boolean isSolvable() {
        return solvable;
    }           // is the initial board solvable?

    public int moves() {
        return moves;
    }                    // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution() {
        return queueBoard;
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
    }// solve a slider puzzle (given below)
}
