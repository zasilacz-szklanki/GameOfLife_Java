package org.example;

import java.util.ArrayList;
import java.util.List;

public class PlainGameOfLifeSimulator implements GameOfLifeSimulator {
    @Override
    public void doStep(GameOfLifeBoard board) {
        List<List<GameOfLifeCell>> current = board.getBoard();
        List<List<GameOfLifeCell>> next = new ArrayList<>();

        for (int i = 0; i < current.size(); i++) {
            List<GameOfLifeCell> newRow = new ArrayList<>();
            for (int j = 0; j < current.get(i).size(); j++) {
                newRow.add(new GameOfLifeCell(current.get(i).get(j)));
            }
            next.add(newRow);
        }

        for (int i = 0; i < next.size(); i++) {
            for (int j = 0; j < next.get(i).size(); j++) {
                next.get(i).get(j).setNeighbours(current.get(i).get(j));
            }
        }

        for (int i = 0; i < current.size(); i++) {
            for (int j = 0; j < current.get(i).size(); j++) {
                current.get(i).get(j).updateState(next.get(i).get(j).nextState());
            }
        }
    }
}
