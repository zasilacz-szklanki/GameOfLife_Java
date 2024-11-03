package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class GameOfLifeBoardTest {
    @Test
    void test1() {
        // 2 kolejne wywołania konstruktora generują inny początkowy układ
        GameOfLifeBoard board1 = new GameOfLifeBoard(8, 8);
        GameOfLifeBoard board2 = new GameOfLifeBoard(8, 8);
        assert (!Arrays.deepEquals(board1.getBoard(), board2.getBoard()));
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
                assert (board.getBoard()[i][j].getCellValue() == boardBegin[i][j]);
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

        for (int i = 0; i < board.getBoard().length; i++) {
            assert (board.getBoard()[i][1].getCellValue() == col.getSegment()[i].getCellValue());
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

        for (int i = 0; i < board.getBoard()[0].length; i++) {
            assert (board.getBoard()[1][i].getCellValue() == row.getSegment()[i].getCellValue());
        }
    }
}
