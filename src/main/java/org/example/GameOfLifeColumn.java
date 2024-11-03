package org.example;

public class GameOfLifeColumn extends GameOfLifeSegment {

    public GameOfLifeColumn(GameOfLifeCell[][] board, int index) {
        super(board.length);
        for (int i = 0; i < board.length; i++) {
            segment[i] = board[i][index];
        }
    }
}
