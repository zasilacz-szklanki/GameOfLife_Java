package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class GameOfLifeBoardTest {
    @Test
    void test1() {
        // 2 kolejne wywołania konstruktora generują inny początkowy układ
        GameOfLifeBoard board1 = new GameOfLifeBoard(8, 8);
        GameOfLifeBoard board2 = new GameOfLifeBoard(8, 8);
        assert (!board1.getBoard().equals(board2.getBoard()));
    }

    @Test
    void test2() {
        boolean[][] boardBegin = {
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, true, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}
        };

        GameOfLifeBoard board = new GameOfLifeBoard(boardBegin);
        for (int i = 0; i < boardBegin.length; i++) {
            for (int j = 0; j < boardBegin[i].length; j++) {
                assert (board.getBoard().get(i).get(j).getCellValue() == boardBegin[i][j]);
            }
        }
    }

    @Test
    void test3() {
        // test gettera i settera
        boolean[][] boardBegin = {
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}
        };

        GameOfLifeBoard board = new GameOfLifeBoard(boardBegin);
        board.set(2,2, true);
        assert (board.get(2,2));
        assert (!board.get(0,0));
    }

    @Test
    void test4() {
        // test dla getColumn
        boolean[][] testBoard = {
                {false, false, false, false, false},
                {false, true, true, false, false},
                {false, true, false, false, false},
                {false, false, false, true, true},
                {false, false, false, true, true}
        };

        GameOfLifeBoard board = new GameOfLifeBoard(testBoard);
        GameOfLifeColumn col = board.getColumn(1);

        for (int i = 0; i < board.getBoard().size(); i++) {
            assert (board.getBoard().get(i).get(1).getCellValue() == col.getSegment().get(i).getCellValue());
        }
    }

    @Test
    void test5() {
        // test dla getRow
        boolean[][] testBoard = {
                {false, false, false, false, false},
                {false, true, true, false, false},
                {false, true, false, false, false},
                {false, false, false, true, true},
                {false, false, false, true, true}
        };

        GameOfLifeBoard board = new GameOfLifeBoard(testBoard);
        GameOfLifeRow row = board.getRow(1);

        for (int i = 0; i < board.getBoard().get(0).size(); i++) {
            assert (board.getBoard().get(1).get(i).getCellValue() == row.getSegment().get(i).getCellValue());
        }
    }

    @Test
    void test6() {
        // testy dla metody toString()
        boolean[][] testBoard = {
                {false, false},
                {false, true}
        };

        GameOfLifeBoard board = new GameOfLifeBoard(testBoard);
        GameOfLifeRow row = board.getRow(1);
        GameOfLifeColumn column = board.getColumn(0);
        GameOfLifeCell cell = board.getBoard().get(0).get(0);
        GameOfLifeCell cell2 = board.getBoard().get(1).get(1);
        GameOfLifeSimulator sim = new PlainGameOfLifeSimulator();

        String boardExpected = "GameOfLifeBoard[\n" +
                "dead dead \n" +
                "dead alive \n" +
                "]";
        String cellExpected = "GameOfLifeCell[cell state=dead]";
        String cell2Expected = "GameOfLifeCell[cell state=alive]";
        String plainSimExpected = "PlainGameOfLifeSimulator[class org.example.PlainGameOfLifeSimulator]";

        assertEquals(board.toString(), boardExpected);
        assertEquals(cell.toString(), cellExpected);
        assertEquals(cell2.toString(), cell2Expected);
        assertEquals(sim.toString(), plainSimExpected);
    }

    @Test
    void test7() {
        // testy dla metody equals() i hashCode()
        boolean[][] testBoard = {
                {false, false},
                {false, true}
        };
        boolean[][] testBoard2 = {
                {true, true},
                {false, false}
        };

        GameOfLifeBoard board = new GameOfLifeBoard(testBoard);
        GameOfLifeRow row = board.getRow(1);
        GameOfLifeBoard board2 = new GameOfLifeBoard(testBoard2);
        GameOfLifeBoard board3 = new GameOfLifeBoard(testBoard);
        GameOfLifeCell cell = board.getBoard().get(0).get(0);
        GameOfLifeCell cell2 = board.getBoard().get(1).get(0);

        assert(board.equals(board));
        assert(!board.equals(row));
        assert(!board.equals(null));
        assert(!board.equals(board2));

        assert(cell.equals(cell));
        assert(!cell.equals(row));
        assert(!cell.equals(null));
        assert(cell.equals(cell2));

        assert (board.hashCode() == board3.hashCode());
    }
}
