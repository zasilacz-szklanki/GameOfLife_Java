package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class GameOfLifeBoardTest {

    @Test
    void test1() {
        //wszystkie komórki są martwe, więc żadna komórka nie stanie się żywa
        boolean[][] boardBegin = {
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}
        };
        boolean[][] boardEnd = {
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}
        };
        GameOfLifeBoard golb = new GameOfLifeBoard(boardBegin);
        golb.doStep();
        assert (Arrays.deepEquals(golb.getBoard(), boardEnd));
    }

    @Test
    void test2() {
        //komórka umiera z samotnośći
        boolean[][] boardBegin = {
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, true, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}
        };
        GameOfLifeBoard golb = new GameOfLifeBoard(boardBegin);
        golb.doStep();
        assert (!boardBegin[2][2]);
    }

    @Test
    void test3() {
        //komórka ożywa ponieważ ma 3 żywych sąsiadów
        boolean[][] boardBegin = {
                {false, false, false, false, false},
                {false, true, true, false, false},
                {false, true, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}
        };
        GameOfLifeBoard golb = new GameOfLifeBoard(boardBegin);
        golb.doStep();
        assert (golb.getBoard()[2][2]);
    }

    @Test
    void test4() {
        //komórka [2][1] pozostaje nadal żywa ponieważ ma 2 żywych sąsiadów
        //komórka [2][1] pozostaje nadal żywa ponieważ ma 3 żywych sąsiadów
        boolean[][] boardBegin = {
                {false, false, false, false, false},
                {false, true, true, false, false},
                {false, true, false, false, false},
                {false, false, false, true, true},
                {false, false, false, true, true}
        };
        GameOfLifeBoard golb = new GameOfLifeBoard(boardBegin);
        golb.doStep();
        assert (golb.getBoard()[2][1]);
        assert (golb.getBoard()[3][3]);
    }

    @Test
    void test5() {
        //komórka [1][1] umiera z samotności (ma 1 sąsiada)
        //komórka [3][3] umiera z zatłoczenia (ma 4 sąsiadów)
        boolean[][] boardBegin = {
                {false, false, false, false, false},
                {false, true, true, false, false},
                {false, false, false, false, false},
                {false, false, false, true, true},
                {false, false, true, true, true}
        };
        GameOfLifeBoard golb = new GameOfLifeBoard(boardBegin);
        golb.doStep();
        assert (!golb.getBoard()[1][1]);
        assert (!golb.getBoard()[3][3]);
    }

    @Test
    void test6() {
        //komórka [1][1] umiera z zatłoczenia (ma 5 sąsiadów)
        //komórka [3][3] umiera z zatłoczenia (ma 6 sąsiadów)
        boolean[][] boardBegin = {
                {true, true, true, false, false},
                {true, true, true, false, false},
                {false, false, false, true, false},
                {false, false, true, true, true},
                {false, false, true, true, true}
        };
        GameOfLifeBoard golb = new GameOfLifeBoard(boardBegin);
        golb.doStep();
        assert (!golb.getBoard()[1][1]);
        assert (!golb.getBoard()[3][3]);
    }

    @Test
    void test7() {
        //komórka [1][1] umiera z zatłoczenia (ma 7 sąsiadów)
        //komórka [0][1] umiera z zatłoczenia (ma 8 sąsiadów)
        boolean[][] boardBegin = {
                {true, true, true, false, false},
                {true, true, true, false, false},
                {true, true, false, false, false},
                {false, false, false, false, false},
                {true, true, true, false, false}
        };
        GameOfLifeBoard golb = new GameOfLifeBoard(boardBegin);
        golb.doStep();
        assert (!golb.getBoard()[1][1]);
        assert (!golb.getBoard()[0][1]);
    }
}