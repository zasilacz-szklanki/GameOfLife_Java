package org.example.Model;
public class GameOfLifeBoardDaoFactory {
    public static Dao<GameOfLifeBoard> getFileDao(String fileName) {
        return new FileGameOfLifeBoardDao(fileName);
    }
}
