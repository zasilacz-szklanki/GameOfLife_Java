package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        stackPane.setPadding(new Insets(20));
        stackPane.getChildren().add(label);

        Scene newScene = new Scene(stackPane);
        window.setScene(newScene);
        window.sizeToScene();
        window.show();
    }

    static void messageWindow(String mess,String title) {
        Stage window = new Stage();
        window.setTitle(title);

        Label label = new Label(mess);
        label.setFont(Font.font("Courier New", 16));
        label.setStyle("-fx-text-fill: #00cb26;-fx-font-weight: bold;");

        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(20));
        stackPane.getChildren().add(label);

        Scene newScene = new Scene(stackPane);
        window.setScene(newScene);
        window.sizeToScene();
        window.show();
    }

    static String boardNameChooseWindow(List<String> boardNames, String mess, String title) {
        Stage window = new Stage();
        window.setTitle(title);

        Label label = new Label(mess);
        label.setFont(Font.font("Courier New", 16));
        ChoiceBox<String> boardNamesChoiceBox = new ChoiceBox<>();
        boardNamesChoiceBox.getItems().addAll(boardNames);

        Button okButton = new Button();
        FontIcon checkIcon = new FontIcon(Material.CHECK);
        okButton.setGraphic(checkIcon);
        okButton.setOnAction(event -> {
            window.close();
        });

        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.getChildren().addAll(label, boardNamesChoiceBox, okButton);
        box.setAlignment(Pos.CENTER);

        Scene newScene = new Scene(box);
        window.setScene(newScene);
        window.sizeToScene();
        window.showAndWait();

        return boardNamesChoiceBox.getSelectionModel().getSelectedItem();
    }

    static String boardSaver(String mess, String title) {
        Stage window = new Stage();
        window.setTitle(title);

        Label label = new Label(mess);
        label.setFont(Font.font("Courier New", 16));

        TextField textField = new TextField();
        textField.setTextFormatter(new TextFormatter<>(
                change -> change.getControlNewText().matches("^[A-Za-z0-9_]*$") ? change : null
        ));
        final String[] name = new String[1];

        Button okButton = new Button();
        FontIcon checkIcon = new FontIcon(Material.CHECK);
        okButton.setGraphic(checkIcon);
        okButton.setOnAction(event -> {
            name[0] = textField.getText();
            window.close();
        });

        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.getChildren().addAll(label, textField, okButton);
        box.setAlignment(Pos.CENTER);

        Scene newScene = new Scene(box);
        window.setScene(newScene);
        window.sizeToScene();
        window.showAndWait();

        return name[0];
    }
}
