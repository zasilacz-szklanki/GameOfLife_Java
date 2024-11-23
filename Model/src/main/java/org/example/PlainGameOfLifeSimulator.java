package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(getClass().toString()).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return new EqualsBuilder().isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).toHashCode();
    }
}
