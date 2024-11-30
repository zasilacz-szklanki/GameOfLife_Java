package org.example.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PlainGameOfLifeSimulator implements GameOfLifeSimulator {
    @Override
    public void doStep(GameOfLifeBoard board) {

        GameOfLifeBoard next = board.clone();

        for (int i = 0; i < next.getBoard().size(); i++) {
            for (int j = 0; j < next.getBoard().get(i).size(); j++) {
                next.getBoard().get(i).get(j).updateState(board.getBoard().get(i).get(j).nextState());
            }
        }

        for (int i = 0; i < next.getBoard().size(); i++) {
            for (int j = 0; j < next.getBoard().get(i).size(); j++) {
                board.set(i, j, next.get(i, j));
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
