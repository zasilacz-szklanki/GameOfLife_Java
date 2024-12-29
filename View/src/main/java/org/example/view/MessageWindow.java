package org.example.view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageWindow {
    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);

    static void errorMessageWindow(String errMess,String title) {
        logger.error(errMess);
        Stage window = new Stage();
        window.setTitle(title);

        Label label = new Label(errMess);
        label.setFont(Font.font("Courier New", 16));
        label.setStyle("-fx-text-fill: #6e0000;-fx-font-weight: bold;");
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(label);

        Scene newScene = new Scene(stackPane, 300, 100);
        window.setScene(newScene);
        window.show();
    }

    static void messageWindow(String mess,String title) {
        Stage window = new Stage();
        window.setTitle(title);

        Label label = new Label(mess);
        label.setFont(Font.font("Courier New", 16));
        label.setStyle("-fx-text-fill: #00cb26;-fx-font-weight: bold;");
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(label);

        Scene newScene = new Scene(stackPane, 300, 100);
        window.setScene(newScene);
        window.show();
    }
}
