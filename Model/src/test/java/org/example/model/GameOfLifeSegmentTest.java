package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void test2() {
        boolean[][] testBoard = {
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, true, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
        };

        GameOfLifeBoard board = new GameOfLifeBoard(testBoard);
        GameOfLifeSimulator simulator = new PlainGameOfLifeSimulator();
        GameOfLifeColumn col = board.getColumn(2);
        GameOfLifeRow row = board.getRow(2);

        assert (col.getSegment().get(2).getCellValue() == board.get(2, 2));
        assert (row.getSegment().get(2).getCellValue() == board.get(2, 2));

        board.doSimulationStep(simulator);

        assert (col.getSegment().get(2).getCellValue() == board.get(2, 2));
        assert (row.getSegment().get(2).getCellValue() == board.get(2, 2));
    }

    @Test
    void test3() {
        boolean[][] testBoard = {
                {true, false},
                {false, true}
        };

        GameOfLifeBoard board = new GameOfLifeBoard(testBoard);
        GameOfLifeRow row = board.getRow(1);
        GameOfLifeColumn column = board.getColumn(0);

        String rowExpected = "GameOfLifeRow[\n" +
                "dead alive \n" +
                "]";
        String colExpected = "GameOfLifeColumn[\n" +
                "alive\n" +
                "dead\n" +
                "]";
        assertEquals(row.toString(), rowExpected);
        assertEquals(column.toString(), colExpected);

        GameOfLifeRow row2 = board.getRow(1);
        GameOfLifeColumn column2 = board.getColumn(0);

        assert (row.equals(row));
        assert (!row.equals(column));
        assert (!row.equals(null));
        assert (row.equals(row2));
    }
}