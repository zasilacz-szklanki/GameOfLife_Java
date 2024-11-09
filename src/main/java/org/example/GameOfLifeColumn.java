package org.example;

import java.util.List;

public class GameOfLifeColumn extends GameOfLifeSegment {
    public GameOfLifeColumn(List<List<GameOfLifeCell>> board, int index) {
        super(board, index, false);
    }
}
