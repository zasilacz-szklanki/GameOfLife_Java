package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameOfLifeBoard implements Serializable {
    private final List<List<GameOfLifeCell>> board;

    public List<List<GameOfLifeCell>> getBoard() {
        List<List<GameOfLifeCell>> boardCopy = new ArrayList<>();
        for (List<GameOfLifeCell> row : board) {
            List<GameOfLifeCell> rowCopy = new ArrayList<>(row);
            boardCopy.add(rowCopy);
        }
        return boardCopy;
    }

    public GameOfLifeBoard(boolean[][] newBoard) {
        List<List<GameOfLifeCell>> tmpBoard = new ArrayList<>();
        for (boolean[] row : newBoard) {
            List<GameOfLifeCell> newRow = new ArrayList<>();
            for (boolean cell : row) {
                newRow.add(new GameOfLifeCell(cell));
            }
            tmpBoard.add(Collections.unmodifiableList(newRow));
        }
        this.board = Collections.unmodifiableList(tmpBoard);

        final int n = this.board.size();
        final int m = this.board.get(0).size();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                this.board.get(i).get(j).initNeighbours(board, i, j);
            }
        }
    }

    public GameOfLifeBoard(int n, int m) {
        Random rand = new Random();
        List<List<GameOfLifeCell>> tmpBoard = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<GameOfLifeCell> row = new ArrayList<>();
            for (int j = 0; j < m; j++) {
                row.add(new GameOfLifeCell(rand.nextBoolean()));
            }
            tmpBoard.add(Collections.unmodifiableList(row));
        }
        this.board = Collections.unmodifiableList(tmpBoard);

        final int rows = this.board.size();
        final int cols = this.board.get(0).size();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.board.get(i).get(j).initNeighbours(board, i, j);
            }
        }
    }

    public boolean get(int x, int y) {
        return board.get(x).get(y).getCellValue();
    }

    public void set(int x, int y, boolean value) {
        board.get(x).get(y).updateState(value);
    }

    public GameOfLifeColumn getColumn(int index) {
        return new GameOfLifeColumn(board, index);
    }

    public GameOfLifeRow getRow(int index) {
        return new GameOfLifeRow(board, index);
    }

    public void doSimulationStep(GameOfLifeSimulator simulation) {
        simulation.doStep(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        for (List<GameOfLifeCell> row : board) {
            for (GameOfLifeCell cell : row) {
                builder.append(cell.getCellValue() ? "alive" : "dead").append(" ");
            }
            builder.append("\n");
        }
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(builder.toString()).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GameOfLifeBoard object = (GameOfLifeBoard) obj;
        return new EqualsBuilder().append(board, object.board).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(board).toHashCode();
    }
}
