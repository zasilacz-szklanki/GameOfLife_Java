package org.example.model;

import java.util.Random;

public enum Density {
    LOW(10),
    MEDIUM(30),
    HIGH(50);

    private final int density;

    Density(int density) {
        this.density = density;
    }

    public int getDensity() {
        return this.density;
    }

    public double getDensityDouble() {
        return (double) this.density / 100;
    }

    public boolean[][] randomFill(int sizeX, int sizeY) {
        boolean[][] array = new boolean[sizeX][sizeY];
        int totalCells = (int) (sizeX * sizeY * getDensity()) / 100;
        Random rand = new Random();

        for (int i = 0; i < totalCells; i++) {
            int x = Math.abs(rand.nextInt() % sizeX);
            int y = Math.abs(rand.nextInt() % sizeY);
            if (array[x][y]) {
                i--;
            } else {
                array[x][y] = true;
            }
        }
        return array;
    }

    @Override
    public String toString() {
        return String.format("%d %%", this.density);
    }
}
