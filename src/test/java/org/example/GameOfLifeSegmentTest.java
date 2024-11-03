package org.example;

import org.junit.jupiter.api.Test;

class GameOfLifeSegmentTest {
    @Test
    void test1() {
        boolean[][] testBoard = {
                {false, false, false, false, false},
                {false, true, true, false, false},
                {false, true, false, false, false},
                {false, false, false, true, true},
                {false, false, false, true, true}
        };

        GameOfLifeBoard board = new GameOfLifeBoard(testBoard);
        GameOfLifeColumn col = board.getColumn(1);
        GameOfLifeRow row = board.getRow(1);

        assert (col.countAliveCells() == 2);
        assert (row.countDeadCells() == 3);
        assert (row.countAliveCells() == 2);
        assert (row.countDeadCells() == 3);
    }
}