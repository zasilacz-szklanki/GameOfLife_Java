package org.example;

public abstract class GameOfLifeSegment implements CellObserver {
    protected final GameOfLifeCell[] segment;
    protected GameOfLifeCell[][] board;
    protected int segmentIndex;
    protected boolean type;

    protected GameOfLifeSegment(GameOfLifeCell[][] board, int index, boolean isRow) {
        this.board = board;
        this.segmentIndex = index;
        this.type = isRow;
        segment = new GameOfLifeCell[isRow ? board[0].length : board.length];

        if (type) {
            for (int i = 0; i < board[0].length; i++) {
                segment[i] = board[segmentIndex][i];
                board[segmentIndex][i].addObserver(this);
            }
        } else {
            for (int i = 0; i < board.length; i++) {
                segment[i] = board[i][segmentIndex];
                board[i][segmentIndex].addObserver(this);
            }
        }
    }

    @Override
    public void whenCellChanged(GameOfLifeCell cell) {
        updateSegmentState();
    }

    protected void updateSegmentState() {
        for (int i = 0; i < segment.length; i++) {
            if (type) {
                segment[i].updateState(board[segmentIndex][i].getCellValue());
            } else {
                segment[i].updateState(board[i][segmentIndex].getCellValue());
            }
        }
    }

    public GameOfLifeCell[] getSegment() {
        GameOfLifeCell[] segmentCopy = new GameOfLifeCell[segment.length];
        for (int i = 0; i < segment.length; i++) {
            segmentCopy[i] = new GameOfLifeCell(segment[i]);
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
