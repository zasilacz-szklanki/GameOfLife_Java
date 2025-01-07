package org.example.model;

public class GameOfLifeBoardDaoFactory {
    public static Dao<GameOfLifeBoard> getFileDao(String fileName) {
        return new FileGameOfLifeBoardDao(fileName);
    }

    public static Dao<GameOfLifeBoard> getDbDao(String boardName) {
        return new JdbcGameOfLifeBoardDao(boardName);
    }
}
