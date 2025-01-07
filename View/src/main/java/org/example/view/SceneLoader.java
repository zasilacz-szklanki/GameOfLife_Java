package org.example.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {

    public static void loadConfigurationScene(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/org/example/view/GolbConfig.fxml"));
            stage.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}