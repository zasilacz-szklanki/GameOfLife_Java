package org.example.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public abstract class GameOfLifeSegment implements Observer<GameOfLifeCell>, Cloneable {
    protected List<GameOfLifeCell> segment;
    protected List<List<GameOfLifeCell>> board;
    protected int segmentIndex;
    protected boolean type;

    protected GameOfLifeSegment(List<List<GameOfLifeCell>> board, int index, boolean isRow) {
        this.board = board;
        this.segmentIndex = index;
        this.type = isRow;
        this.segment = new ArrayList<>(isRow ? board.get(0).size() : board.size());

        if (type) {
            for (int i = 0; i < board.get(0).size(); i++) {
                segment.add(board.get(segmentIndex).get(i));
                board.get(segmentIndex).get(i).addObserver(this);
            }
        } else {
            for (int i = 0; i < board.size(); i++) {
                segment.add(board.get(i).get(segmentIndex));
                board.get(i).get(segmentIndex).addObserver(this);
            }
        }
    }

    @Override
    public void whenChanged(GameOfLifeCell cell) {
        updateSegmentState();
    }

    protected void updateSegmentState() {
        for (int i = 0; i < segment.size(); i++) {
            if (type) {
                segment.get(i).updateState(board.get(segmentIndex).get(i).getCellValue());
            } else {
                segment.get(i).updateState(board.get(i).get(segmentIndex).getCellValue());
            }
        }
    }

    public List<GameOfLifeCell> getSegment() {
        List<GameOfLifeCell> segmentCopy = new ArrayList<>(segment.size());
        for (GameOfLifeCell cell : segment) {
            segmentCopy.add(new GameOfLifeCell(cell));
        }
        return segmentCopy;
    }

    public int countAliveCells() {
        int count = 0;
        for (GameOfLifeCell cell : segment) {
            if (cell.getCellValue()) {
                count++;
            }
        }
        return count;
    }

    public int countDeadCells() {
        int count = 0;
        for (GameOfLifeCell cell : segment) {
            if (!cell.getCellValue()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        for (GameOfLifeCell cell : segment) {
            if (type) {
                builder.append(cell.getCellValue() ? "alive" : "dead").append(" ");
            } else {
                builder.append(cell.getCellValue() ? "alive" : "dead").append("\n");
            }
        }
        if (type) {
            builder.append("\n");
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append(builder.toString()).toString();
        }
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(builder.toString()).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GameOfLifeSegment object = (GameOfLifeSegment) obj;
        return new EqualsBuilder()
                .append(segmentIndex, object.segmentIndex)
                .append(type, object.type)
                .append(segment, object.segment)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(segmentIndex)
                .append(type)
                .append(segment)
                .toHashCode();
    }

    @Override
    public GameOfLifeSegment clone() {
        try {
            GameOfLifeSegment cloned = (GameOfLifeSegment) super.clone();
            List<GameOfLifeCell> tmpSegment = new ArrayList<>(this.segment.size());
            for (GameOfLifeCell cell : this.segment) {
                tmpSegment.add(cell.clone());
            }
            cloned.segment = tmpSegment;
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}