package org.example;

public class PlainGameOfLifeSimulator implements GameOfLifeSimulator {
    @Override
    public void doStep(GameOfLifeBoard board) {
        int neighbour;
        boolean[][] current = board.getBoard();
        boolean[][] next = new boolean[current.length][current[0].length];
        for (int i = 0; i < current.length; i++) {
            for (int j = 0; j < current[i].length; j++) {
                neighbour = countNeighbour(current, i, j);
                next[i][j] = false;
                if (current[i][j] && (neighbour == 2 || neighbour == 3)) {
                    next[i][j] = true;
                } else if (neighbour == 3) {
                    next[i][j] = true;
                }
            }
        }
        for (int i = 0; i < current.length; i++) {
            for (int j = 0; j < current[i].length; j++) {
                board.set(i, j, next[i][j]);
            }
        }
    }

    private int countNeighbour(boolean[][] board, int i, int j) {
        int count = 0;
        final int n = board.length;
        final int m = board[0].length;

        count += board[(i + 1 + n) % n][(j + 1 + m) % m] ? 1 : 0;
        count += board[(i + 1 + n) % n][(j - 1 + m) % m] ? 1 : 0;
        count += board[(i + 1 + n) % n][j % m] ? 1 : 0;

        count += board[i % n][(j - 1 + m) % m] ? 1 : 0;
        count += board[i % n][(j + 1 + m) % m] ? 1 : 0;

        count += board[(i - 1 + n) % n][(j + 1 + m) % m] ? 1 : 0;
        count += board[(i - 1 + n) % n][(j - 1 + m) % m] ? 1 : 0;
        count += board[(i - 1 + n) % n][j % m] ? 1 : 0;
        return count;
    }
}
