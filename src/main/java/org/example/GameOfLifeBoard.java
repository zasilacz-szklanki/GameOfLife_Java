package org.example;

import java.util.Random;

public class GameOfLifeBoard {
    private boolean[][] board;

    public boolean[][] getBoard() {
        //return this.board;//unsafe
        boolean[][] boardCopy = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardCopy[i][j] = board[i][j];
            }
        }
        return boardCopy;
    }

    public GameOfLifeBoard(boolean[][] board) {
        //this.board = board;
        this.board = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    public GameOfLifeBoard(int n, int m) {
        Random rand = new Random();
        this.board = new boolean[n][m];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                this.board[i][j] = rand.nextBoolean();
            }
        }
    }

    public boolean get(int x, int y) {
        return board[x][y];
    }

    public void set(int x, int y, boolean value) {
        board[x][y] = value;
    }

    // jako parametr przekazujemy obiekt symulatora
    public void doSimulationStep(GameOfLifeSimulator simulation) {
        simulation.doStep(this);
    }
}
