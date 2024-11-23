package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileGameOfLifeBoardDao implements Dao<GameOfLifeBoard> {

    private final String fileName;
    private BufferedReader br;
    private BufferedWriter bw;

    public FileGameOfLifeBoardDao(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public GameOfLifeBoard read() {
        List<int[]> rows = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int[] row = new int[values.length];
                for (int i = 0; i < values.length; i++) {
                    row[i] = Integer.parseInt(values[i]);
                }
                rows.add(row);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GameOfLifeBoard board = new GameOfLifeBoard(rows.size(), rows.getFirst().length);
        for (int i = 0; i < rows.size(); i++) {
            for (int j = 0; j < rows.get(i).length; j++) {
                board.set(i, j, rows.get(i)[j] == 1);
            }
        }
        return board;
    }

    @Override
    public void write(GameOfLifeBoard obj) {
        List<List<GameOfLifeCell>> board = obj.getBoard();
        try {
            bw = new BufferedWriter(new FileWriter(fileName));
            for (List<GameOfLifeCell> gameOfLifeCells : board) {
                for (int i = 0; i < gameOfLifeCells.size(); i++) {
                    bw.write(gameOfLifeCells.get(i).getCellValue() ? '1' : '0');
                    if (i != gameOfLifeCells.size() - 1) {
                        bw.write(",");
                    }
                }
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            if (br != null) {
                br.close();
            }
            if (bw != null) {
                bw.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
