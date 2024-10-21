package org.example;

import java.util.Random;

public class GameOfLifeBoard {
    private boolean[][] board;

    public boolean[][] getBoard() {
        //return this.board;//unsafe
        boolean[][] boardCopy = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                boardCopy[i][j] = board[i][j];
            }
        }
        return boardCopy;
    }

    public GameOfLifeBoard(boolean[][] board) {
        this.board = board;
    }

    public GameOfLifeBoard(int n, int m) {
        Random rand = new Random();
        /*board= new boolean[][]{
                {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,true ,false,true ,true ,false,false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,true ,true ,true ,false,false,false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false}
        };
        */
        this.board = new boolean[n][m];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                this.board[i][j] = rand.nextBoolean();
            }
        }
    }

    private int countNeighbour(int i, int j) {
        int count = 0;
        final int n = board.length;
        final int m = board[0].length;

        count += board[(i + 1 + n) % n][(j + 1 + m) % m] ? 1 : 0;
        count += board[(i + 1 + n) % n][(j - 1 + m) % m] ? 1 : 0;
        count += board[(i + 1 + n) % n][j % m] ? 1 : 0;

        count += board[i % n][(j - 1 + m) % m] ? 1 : 0;
        count += board[i % n][(j + 1 + m) % m] ? 1 : 0;

        count += board[(i - 1 + n) % n][(j + 1 + m) % m] ? 1 : 0;
        count += board[(i - 1 + n) % n][(j - 1 + m) % m] ? 1 : 0;
        count += board[(i - 1 + n) % n][j % m] ? 1 : 0;
        return count;
    }

    public void doStep() {
        int neighbour;
        boolean[][] next = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                neighbour = countNeighbour(i, j);
                next[i][j] = false;
                if (board[i][j] && (neighbour == 2 || neighbour == 3)) {
                    next[i][j] = true;
                } else if (neighbour == 3) {
                    next[i][j] = true;
                }
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                this.board[i][j] = next[i][j];
            }
        }
    }

    public void printBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] ? "X" : ".");
            }
            System.out.println();
        }
    }

}
