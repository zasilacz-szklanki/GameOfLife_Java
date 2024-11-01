package org.example;

public class PlainGameOfLifeSimulator implements GameOfLifeSimulator {
    @Override
    public void doStep(GameOfLifeBoard board) {
        GameOfLifeCell[][] current = board.getBoard();
        GameOfLifeCell[][] next = new GameOfLifeCell[current.length][current[0].length];
        for (int i = 0; i < next.length; i++) {
            for (int j = 0; j < next[0].length; j++) {
                next[i][j] = new GameOfLifeCell(current[i][j]);
                next[i][j].setNeighbours(current[i][j]);
            }
        }
        for (int i = 0; i < current.length; i++) {
            for (int j = 0; j < current[i].length; j++) {
                current[i][j].updateState(next[i][j].nextState());
            }
        }
    }
}
