package org.example;

import java.util.List;

public class GameOfLifeRow extends GameOfLifeSegment {
    public GameOfLifeRow(List<List<GameOfLifeCell>> board, int index) {
        super(board, index, true);
    }
}