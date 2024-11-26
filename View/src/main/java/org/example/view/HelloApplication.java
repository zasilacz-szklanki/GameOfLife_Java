package org.example.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.model.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("2D Array Display");

        // Define the 2D array
        boolean[][] array = {
                {false, false, false},
                {false, true, true},
                {true, false, true},
                {true, true, false}
        };

        GameOfLifeBoard golb = new GameOfLifeBoard(array);

        // Create a GridPane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(0);
        gridPane.setAlignment(Pos.CENTER);

        // Populate the GridPane with Labels for each element in the array
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[row].length; col++) {
                Label label = new Label(golb.get(row, col) ? "X" : ".");
                gridPane.add(label, col, row);
            }
        }

        // Create a Scene and set it to the Stage
        Scene scene = new Scene(gridPane, 300, 200);
        primaryStage.setScene(scene);

        // Show the Stage
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
