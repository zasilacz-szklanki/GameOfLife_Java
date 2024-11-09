package org.example;

import java.util.ArrayList;
import java.util.List;

public abstract class GameOfLifeSegment implements Observer<GameOfLifeCell> {
    protected final List<GameOfLifeCell> segment;
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
}