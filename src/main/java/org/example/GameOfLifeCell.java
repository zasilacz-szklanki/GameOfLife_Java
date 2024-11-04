package org.example;

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

    public void initNeighbours(GameOfLifeCell[][] board,int i,int j) {
        final int n = board.length;
        final int m = board[0].length;
        board[i][j].neighbours[0] = board[(i + 1 + n) % n][(j + 1 + m) % m];
        board[i][j].neighbours[1] = board[(i + 1 + n) % n][(j - 1 + m) % m];
        board[i][j].neighbours[2] = board[(i + 1 + n) % n][j % m];

        board[i][j].neighbours[3] = board[i % n][(j - 1 + m) % m];
        board[i][j].neighbours[4] = board[i % n][(j + 1 + m) % m];

        board[i][j].neighbours[5] = board[(i - 1 + n) % n][(j + 1 + m) % m];
        board[i][j].neighbours[6] = board[(i - 1 + n) % n][(j - 1 + m) % m];
        board[i][j].neighbours[7] = board[(i - 1 + n) % n][j % m];
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
}