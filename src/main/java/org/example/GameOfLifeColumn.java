package org.example;

public class GameOfLifeColumn extends GameOfLifeSegment {
    public GameOfLifeColumn(GameOfLifeCell[][] board, int index) {
        super(board, index, false);
    }
}
