package org.example.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.model.Density;
import org.example.model.GameOfLifeBoard;
import org.example.model.PlainGameOfLifeSimulator;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Konfiguracja");

        final Label gridSizeXLabel = new Label("Wiersze (4-20):");
        gridSizeXLabel.setFont(Font.font("Courier New", 16));
        TextField gridSizeXField = new TextField();
        gridSizeXField.setPromptText("Wpisz rozmiar");
        gridSizeXField.setFont(Font.font("Courier New", 16));

        final Label gridSizeYLabel = new Label("Kolumny (4-20):");
        gridSizeYLabel.setFont(Font.font("Courier New", 16));
        TextField gridSizeYField = new TextField();
        gridSizeYField.setPromptText("Wpisz rozmiar");
        gridSizeYField.setFont(Font.font("Courier New", 16));

        final Label densityLabel = new Label("Początkowa gęstość (%):");
        densityLabel.setFont(Font.font("Courier New", 16));
        ChoiceBox<Density> densityChoiceBox = new ChoiceBox<>();
        densityChoiceBox.getItems().addAll(Density.LOW, Density.MEDIUM, Density.HIGH);
        densityChoiceBox.setStyle("-fx-font-family: Courier New;-fx-font-size: 16;");

        Button startButton = new Button("START");
        startButton.setFont(Font.font("Courier New", 16));

        startButton.setOnAction(event -> {
            primaryStage.close();
            String sx = gridSizeXField.getText();
            String sy = gridSizeYField.getText();

            if (sx.isBlank() || sy.isBlank()) {
                errorMessageWindow("Blank input");
                return;
            }

            final int gridSizeX;
            final int gridSizeY;
            try {
                gridSizeX = Integer.parseInt(sx);
                gridSizeY = Integer.parseInt(sy);
            } catch (NumberFormatException e) {
                errorMessageWindow("NFE");
                return;
            }

            if (gridSizeX < 4 || gridSizeX > 20 || gridSizeY < 4 || gridSizeY > 20) {
                errorMessageWindow("Wrong size");
                return;
            }

            if (densityChoiceBox.getValue() == null) {
                errorMessageWindow("No density selected");
                return;
            }
            //primaryStage.close();
            openSimulationWindow(gridSizeX, gridSizeY, densityChoiceBox.getValue());
        });

        final VBox layout = new VBox(10);
        VBox.setMargin(gridSizeXLabel, new Insets(20,0,0,20));
        VBox.setMargin(gridSizeXField, new Insets(0,20,0,20));

        VBox.setMargin(gridSizeYLabel, new Insets(0,0,0,20));
        VBox.setMargin(gridSizeYField, new Insets(0,20,0,20));

        VBox.setMargin(densityLabel, new Insets(0,20,0,20));
        VBox.setMargin(densityChoiceBox, new Insets(0,20,0,20));

        VBox center = new VBox(10);
        center.setAlignment(Pos.CENTER);
        VBox.setMargin(startButton, new Insets(20,0,0,20));
        center.getChildren().addAll(startButton);

        layout.getChildren().addAll(gridSizeXLabel, gridSizeXField, gridSizeYLabel,
                gridSizeYField, densityLabel, densityChoiceBox, center);
        Scene scene = new Scene(layout, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openSimulationWindow(int gridSizeX, int gridSizeY, Density density) {
        Stage newWindow = new Stage();
        newWindow.setTitle("New Window");

        final GameOfLifeBoard golb = new GameOfLifeBoard(density.randomFill(gridSizeX, gridSizeY));
        final PlainGameOfLifeSimulator sim = new PlainGameOfLifeSimulator();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: #000057;");

        String lightShade = "░";//"\u2591";
        String fullBlock = "█";//"\u2588";

        Label[][] label = new Label[gridSizeX][gridSizeY];

        for (int row = 0; row < golb.getBoard().size(); row++) {
            for (int col = 0; col < golb.getBoard().get(row).size(); col++) {
                label[row][col] = new Label(golb.get(row, col) ? fullBlock : lightShade);
                label[row][col].setStyle("-fx-text-fill: white;");
                label[row][col].setFont(Font.font("Courier New", 16));
                gridPane.add(label[row][col], col, row);
            }
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            golb.doSimulationStep(sim);
            for (int row = 0; row < golb.getBoard().size(); row++) {
                for (int col = 0; col < golb.getBoard().get(row).size(); col++) {
                    label[row][col].setText(fullBlock);
                    if (golb.get(row, col)) {
                        label[row][col].setStyle("-fx-text-fill: #00ff00;");
                    } else {
                        label[row][col].setStyle("-fx-text-fill: #777777;");
                    }
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        int padding = 30;
        Scene newScene = new Scene(gridPane, gridSizeY * 12 + padding, gridSizeX * 20 + padding);
        newWindow.setScene(newScene);
        newWindow.show();
    }

    private void errorMessageWindow(String errMess) {
        Stage window = new Stage();
        window.setTitle("ERROR");

        Label label = new Label(errMess);
        label.setFont(Font.font("Courier New", 16));
        label.setStyle("-fx-text-fill: #6e0000;-fx-font-weight: bold;");
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(label);

        Scene newScene = new Scene(stackPane, 200, 100);
        window.setScene(newScene);
        window.show();

        window.setOnCloseRequest(event -> start(new Stage()));
    }

    public static void main(String[] args) {
        launch();
    }
}
