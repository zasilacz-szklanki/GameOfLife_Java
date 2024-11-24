package org.example.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class GameOfLifeCell {
    private boolean value;
    private GameOfLifeCell[] neighbours;
    private ObservatorsManagement<GameOfLifeCell> observers;

    public GameOfLifeCell(boolean value) {
        this.value = value;
        this.neighbours = new GameOfLifeCell[8];
        this.observers = new ObservatorsManagement<>();
    }

    public GameOfLifeCell(GameOfLifeCell cell) {
        this.value = cell.value;
        this.neighbours = new GameOfLifeCell[8];
        this.observers = new ObservatorsManagement<>();
    }

    public void addObserver(Observer<GameOfLifeCell> observer) {
        observers.addObserver(observer);
    }

    public void alarmObservers() {
        observers.alarmObservers(this);
    }

    private int countNeighbour() {
        int count = 0;
        for (GameOfLifeCell neighbour : neighbours) {
            if (neighbour.value) {
                count++;
            }
        }
        return count;
    }

    public void initNeighbours(List<List<GameOfLifeCell>> board, int i, int j) {
        final int n = board.size();
        final int m = board.get(0).size();
        neighbours[0] = board.get((i + 1 + n) % n).get((j + 1 + m) % m);
        neighbours[1] = board.get((i + 1 + n) % n).get((j - 1 + m) % m);
        neighbours[2] = board.get((i + 1 + n) % n).get(j % m);

        neighbours[3] = board.get(i % n).get((j - 1 + m) % m);
        neighbours[4] = board.get(i % n).get((j + 1 + m) % m);

        neighbours[5] = board.get((i - 1 + n) % n).get((j + 1 + m) % m);
        neighbours[6] = board.get((i - 1 + n) % n).get((j - 1 + m) % m);
        neighbours[7] = board.get((i - 1 + n) % n).get(j % m);
    }

    public void setNeighbours(GameOfLifeCell cell) {
        this.neighbours = new GameOfLifeCell[8];
        for (int i = 0; i < cell.neighbours.length; i++) {
            this.neighbours[i] = new GameOfLifeCell(cell.neighbours[i]);
        }
    }

    public boolean getCellValue() {
        return this.value;
    }

    public boolean nextState() {
        int neighbour = countNeighbour();
        if (this.value && (neighbour == 2 || neighbour == 3)) {
            return true;
        } else if (neighbour == 3) {
            return true;
        }
        return false;
    }

    public void updateState(boolean state) {
        if (this.value != state) {
            this.value = state;
            alarmObservers();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("cell state", getCellValue() ? "alive" : "dead")
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GameOfLifeCell object = (GameOfLifeCell) obj;

        EqualsBuilder eb = new EqualsBuilder().append(this.value, object.value);

        List<Boolean> thisN = new ArrayList<>();
        List<Boolean> objectN = new ArrayList<>();

        for (int i = 0; i < this.neighbours.length; i++) {
            thisN.add(this.neighbours[i].value);
            objectN.add(object.neighbours[i].value);
        }

        eb.append(thisN, objectN);
        return eb.isEquals();
    }

    @Override
    public int hashCode() {
        List<Boolean> thisN = new ArrayList<>();
        for (int i = 0; i < this.neighbours.length; i++) {
            thisN.add(this.neighbours[i].value);
        }
        return new HashCodeBuilder(17, 37).append(value).append(thisN).toHashCode();
    }
}