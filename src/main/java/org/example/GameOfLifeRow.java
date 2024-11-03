package org.example;

public class GameOfLifeRow extends GameOfLifeSegment {

    public GameOfLifeRow(GameOfLifeCell[][] board, int index) {
        super(board[0].length);
        for (int i = 0; i < board[0].length; i++) {
            segment[i] = board[index][i];
        }
    }
}
