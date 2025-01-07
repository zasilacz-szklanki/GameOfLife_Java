package org.example.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JdbcGameOfLifeBoardDaoTest {

    final String driverManager="jdbc:derby:;shutdown=true";

    final String url = "jdbc:derby:src/test/GameOfLifeDBTest;create=true";
    final String url1 = "jdbc:derby:src/test/GameOfLifeDBTest1;create=true";

    boolean[][] init = {
            {false, false},
            {false, true},
            {true, false},
    };
    final GameOfLifeBoard initBoard = new GameOfLifeBoard(init);

    @Test
    void test1() {
        try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("p1", url)) {
            dao.createTables();
        } catch (DbTableExistException e) {
        } catch (DbException e) {
            assert(false);
        }
        try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("p1", url)) {
            dao.createTables();
        } catch (DbTableExistException e) {
        } catch (DbException e) {
            assert(false);
        }
    }

    @Test
    void test2() {
        try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("p1", url)) {
            GameOfLifeBoard board = new GameOfLifeBoard(init);
            dao.write(board);
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    void test3() {
        try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("p1", url)) {
            GameOfLifeBoard board = dao.read();
            assert(board.equals(initBoard));
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    void test4() {
        try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("p2",url)) {
            GameOfLifeBoard board = new GameOfLifeBoard(2,5);
            dao.write(board);
        } catch (Exception e) {
            assert(false);
        }
    }
    @Test
    void test5() {
        try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("p3",url)) {
            GameOfLifeBoard board = new GameOfLifeBoard(3,3);
            dao.write(board);
        } catch (Exception e) {
            assert(false);
        }
    }

    @Test
    void test6() {
        List<String> names = new ArrayList<>();
        try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("p3",url1)) {
            names = dao.getBoardNames();
        } catch (Exception e) {
            assert(false);
        }
        List<String> expectedNames = new ArrayList<>(Arrays.asList("p1", "p2", "p3"));
        assert(names.equals(expectedNames));
    }

    @Test
    void test7() {
        String name = "";
        try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("p3",url1)) {
            name = dao.getBoardName();
        } catch (Exception e) {
            assert(false);
        }
        assert(name.equals("p3"));
    }
}
