package org.example.model;

public class GameOfLifeBoardPrototypeFactory {
    public static GameOfLifeBoard createInstance(GameOfLifeBoard board) throws CloneException {
        return board.clone();
    }
}