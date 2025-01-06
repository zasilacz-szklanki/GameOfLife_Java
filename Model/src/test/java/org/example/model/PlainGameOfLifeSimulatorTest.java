package org.example.model;

import org.junit.jupiter.api.Test;

class PlainGameOfLifeSimulatorTest {
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
        GameOfLifeBoard golb = new GameOfLifeBoard(boardBegin);
        GameOfLifeSimulator simulatorGolb = new PlainGameOfLifeSimulator();
        try {
            golb.doSimulationStep(simulatorGolb);
        } catch (DoStepException e) {
            assert(false);
        }
        for (int i = 0; i < boardBegin.length; i++) {
            for (int j = 0; j < boardBegin[i].length; j++) {
                assert (golb.getBoard().get(i).get(j).getCellValue() == boardBegin[i][j]);
            }
        }
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
        GameOfLifeSimulator simulatorGolb = new PlainGameOfLifeSimulator();
        try {
            golb.doSimulationStep(simulatorGolb);
        } catch (DoStepException e) {
            assert(false);
        }
        assert(!golb.getBoard().get(2).get(2).getCellValue());
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
        GameOfLifeSimulator simulatorGolb = new PlainGameOfLifeSimulator();
        try {
            golb.doSimulationStep(simulatorGolb);
        } catch (DoStepException e) {
            assert(false);
        }
        assert (golb.getBoard().get(2).get(2).getCellValue());
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
        GameOfLifeSimulator simulatorGolb = new PlainGameOfLifeSimulator();
        try {
            golb.doSimulationStep(simulatorGolb);
        } catch (DoStepException e) {
            assert(false);
        }
        assert (golb.getBoard().get(2).get(1).getCellValue());
        assert (golb.getBoard().get(3).get(3).getCellValue());
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
        GameOfLifeSimulator simulatorGolb = new PlainGameOfLifeSimulator();
        try {
            golb.doSimulationStep(simulatorGolb);
        } catch (DoStepException e) {
            assert(false);
        }
        assert (!golb.getBoard().get(1).get(1).getCellValue());
        assert (!golb.getBoard().get(3).get(3).getCellValue());
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
        GameOfLifeSimulator simulatorGolb = new PlainGameOfLifeSimulator();
        try {
            golb.doSimulationStep(simulatorGolb);
        } catch (DoStepException e) {
            assert(false);
        }
        assert (!golb.getBoard().get(1).get(1).getCellValue());
        assert (!golb.getBoard().get(3).get(3).getCellValue());
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
        GameOfLifeSimulator simulatorGolb = new PlainGameOfLifeSimulator();
        try {
            golb.doSimulationStep(simulatorGolb);
        } catch (DoStepException e) {
            assert(false);
        }
        assert (!golb.getBoard().get(1).get(1).getCellValue());
        assert (!golb.getBoard().get(0).get(1).getCellValue());

        GameOfLifeSimulator simulator2 = new PlainGameOfLifeSimulator();
        assert (simulatorGolb.equals(simulatorGolb));
        assert (!simulatorGolb.equals(golb));
        assert (!simulatorGolb.equals(null));
        assert (simulatorGolb.equals(simulator2));
    }
}
