package org.example.model;

import org.junit.jupiter.api.Test;

public class FileGameOfLIfeBoardDaoTest {
    @Test
    void test1() {
        //testy dla odczytu z pliku
        boolean[][] board = {
                {false, false, false},
                {false, true, true},
                {true, false, true},
                {true, true, false}
        };
        GameOfLifeBoard golb;
        GameOfLifeBoard golb2 = new GameOfLifeBoard(board);

        //try-with-resources
        try (FileGameOfLifeBoardDao dao = new FileGameOfLifeBoardDao("in.txt")) {
            golb = dao.read();
            assert golb.equals(golb2);
            System.out.println(golb);
            System.out.println(golb.getBoard().size() + " x " + golb.getBoard().getFirst().size());
        } catch (Exception e) {
            assert false;
        }
        assert true;
    }

    @Test
    void test2() {
        //testy dla zapisu do pliku
        boolean[][] board = {{false, false, false}, {false, true, true}, {true, false, true}, {true, true, false}};
        GameOfLifeBoard golb = new GameOfLifeBoard(board);
        GameOfLifeBoard golb2;
        //zapis do pliku
        try (FileGameOfLifeBoardDao dao = new FileGameOfLifeBoardDao("out.txt")) {
            dao.write(golb);
        } catch (Exception e) {
            assert false;
        }

        //sprawdzenie odczytu z zapisanego pliku
        try (FileGameOfLifeBoardDao dao = new FileGameOfLifeBoardDao("out.txt")) {
            golb2 = dao.read();
            assert golb.equals(golb2);
        } catch (Exception e) {
            assert false;
        }
        assert true;
    }
}