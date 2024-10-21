package org.example;

public class Main {
    public static void main(String[] args) {

        GameOfLifeBoard golb = new GameOfLifeBoard(12, 20);
        golb.printBoard();
        System.out.println("==========");
        golb.doStep();
        golb.printBoard();
        System.out.println("==========");
        golb.doStep();
        golb.printBoard();
    }
}