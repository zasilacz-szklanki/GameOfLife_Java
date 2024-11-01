package org.example;

import java.util.Random;

public class GameOfLifeBoard {
    private final GameOfLifeCell[][] board;

    public GameOfLifeCell[][] getBoard() {
        GameOfLifeCell[][] boardCopy = new GameOfLifeCell[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardCopy[i][j] = board[i][j];
            }
        }
        return boardCopy;
    }

    public GameOfLifeBoard(boolean[][] newBoard) {
        this.board = new GameOfLifeCell[newBoard.length][newBoard[0].length];
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                this.board[i][j] = new GameOfLifeCell(newBoard[i][j]);
            }
        }
        final int n = this.board.length;
        final int m = this.board[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                this.board[i][j].initNeighbours(board,i,j);
            }
        }
    }

    public GameOfLifeBoard(int n, int m) {
        Random rand = new Random();
        this.board = new GameOfLifeCell[n][m];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                this.board[i][j] = new GameOfLifeCell(rand.nextBoolean());
            }
        }

    }

    public GameOfLifeCell get(int x, int y) {
        return board[x][y];
    }

    public void set(int x, int y, boolean value) {
        board[x][y].updateState(value);
    }

    // jako parametr przekazujemy obiekt symulatora
    public void doSimulationStep(GameOfLifeSimulator simulation) {
        simulation.doStep(this);
    }
}
