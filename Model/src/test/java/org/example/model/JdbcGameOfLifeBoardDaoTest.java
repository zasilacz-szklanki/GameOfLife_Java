//package org.example.model;
//
//import org.junit.jupiter.api.Test;
//
//public class JdbcGameOfLifeBoardDaoTest {
//
//    final String driverManager="jdbc:derby:;shutdown=true";
//
//    final String url = "jdbc:derby:src/test/GameOfLifeDBTest;create=true";
//
//    boolean[][] init = {
//            {false, false},
//            {false, true},
//            {true, false},
//    };
//    final GameOfLifeBoard initBoard = new GameOfLifeBoard(init);
//
//    @Test
//    void test1() {
//        System.out.println(url);
//        JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("asdf",url,driverManager);
//        try {
//            dao.createTables();
//        } catch (DbException e) {
//            assert(false);
//        }
//        try {
//            dao.createTables();
//        } catch (DbException e) {
//            assert(true);
//        }
//    }
//
//    @Test
//    void test2() {
//        try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("p1", url, driverManager)) {
//            GameOfLifeBoard board = new GameOfLifeBoard(init);
//            dao.write(board);
//        } catch (Exception e) {
//            System.out.println(e);
//            assert (false);
//        }
//    }
//
////    @Test
////    void test3() {
////        try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("p1", url, driverManager)) {
////            GameOfLifeBoard board = dao.read();
////            assert(board.equals(initBoard));
////        } catch (Exception e) {
////            System.out.println(e);
////            assert (false);
////        }
////    }
//
////    @Test
////    void test3() {
////        try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("p2",url,driverManager)) {
////            GameOfLifeBoard board = new GameOfLifeBoard(2,5);
////            dao.write(board);
////        } catch (Exception e) {
////            assert(false);
////        }
////    }
////    @Test
////    void test4() {
////        try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("p3",url,driverManager)) {
////            GameOfLifeBoard board = new GameOfLifeBoard(3,3);
////            dao.write(board);
////        } catch (Exception e) {
////            assert(false);
////        }
////    }
//
//}
