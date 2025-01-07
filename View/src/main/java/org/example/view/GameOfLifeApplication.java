package org.example.view;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameOfLifeApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        SceneLoader.loadConfigurationScene(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}