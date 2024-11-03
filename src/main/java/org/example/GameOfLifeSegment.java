package org.example;

public abstract class GameOfLifeSegment {
    protected final GameOfLifeCell[] segment;

    protected GameOfLifeSegment(int length) {
        segment = new GameOfLifeCell[length];
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
