package org.example;

public class GameOfLifeRow extends GameOfLifeSegment {
    public GameOfLifeRow(GameOfLifeCell[][] board, int index) {
        super(board, index, true);
    }
}