package org.example.view;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.model.Density;
import org.example.model.FileGameOfLifeBoardDao;
import org.example.model.GameOfLifeBoard;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConfigController {

    @FXML
    private TextField gridSizeXField;

    @FXML
    private TextField gridSizeYField;

    @FXML
    private Button startButton;

    @FXML
    private Button openFileButton;

    @FXML
    private Label gridSizeXLabel;

    @FXML
    private Label gridSizeYLabel;

    @FXML
    private Label densityLabel;

    @FXML
    private ChoiceBox<Density> densityChoiceBox;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu infoMenu;

    @FXML
    private Menu langMenu;

    @FXML
    private MenuItem authorsItem;

    @FXML
    private MenuItem licenseItem;

    @FXML
    private MenuItem polItem;

    @FXML
    private MenuItem deItem;

    private GameOfLifeBoard golb;
    private ResourceBundle resourceBundle;
    private ResourceBundle authorsResourceBundle;
    private String currentLanguage = "pl";

    private void loadResourceBundle(String language) {
        resourceBundle = ResourceBundle.getBundle("org.example.view.lang", new Locale(language));
        authorsResourceBundle = ResourceBundle.getBundle(
                "org.example.view.AuthorsResourceBundle", new Locale(language));
        currentLanguage = language;
    }

    @FXML
    public void initialize() {
        loadResourceBundle(currentLanguage);

        gridSizeXField.setTextFormatter(new TextFormatter<>(
                change -> change.getControlNewText().matches("\\d*") ? change : null
        ));
        gridSizeYField.setTextFormatter(new TextFormatter<>(
                change -> change.getControlNewText().matches("\\d*") ? change : null
        ));

        densityChoiceBox.getItems().clear();
        densityChoiceBox.getItems().addAll(Density.LOW, Density.MEDIUM, Density.HIGH);

        updateUI();

        polItem.setOnAction(event -> {
            loadResourceBundle("pl");
            updateUI();
        });

        deItem.setOnAction(event -> {
            loadResourceBundle("de");
            updateUI();
        });

        authorsItem.setOnAction(event -> {
            String msg = authorsResourceBundle.getString("author1") + "\n" + authorsResourceBundle.getString("author2");
            messageWindow(msg);
        });

        licenseItem.setOnAction(event -> {
            messageWindow("Eclipse Public License - v 2.0");
        });

        menuBar.getMenus().clear();
        menuBar.getMenus().addAll(langMenu, infoMenu);

        startButton.setOnAction(event -> {
            Stage primaryStage = (Stage) startButton.getScene().getWindow();
            primaryStage.close();

            String sx = gridSizeXField.getText();
            String sy = gridSizeYField.getText();

            if (sx.isBlank() || sy.isBlank()) {
                errorMessageWindow(resourceBundle.getString("error.blankInput"));
                return;
            }

            final int gridSizeX;
            final int gridSizeY;
            try {
                gridSizeX = Integer.parseInt(sx);
                gridSizeY = Integer.parseInt(sy);
            } catch (NumberFormatException e) {
                errorMessageWindow(resourceBundle.getString("error.numberFormatException"));
                return;
            }

            if (gridSizeX < 4 || gridSizeX > 20 || gridSizeY < 4 || gridSizeY > 20) {
                errorMessageWindow(resourceBundle.getString("error.wrongSize"));
                return;
            }

            if (densityChoiceBox.getValue() == null) {
                errorMessageWindow(resourceBundle.getString("error.noDensitySelected"));
                return;
            }

            golb = new GameOfLifeBoard(densityChoiceBox.getValue().randomFill(gridSizeX, gridSizeY));
            SimulationController.openSimulationWindow(golb, currentLanguage);
        });

        openFileButton.setOnAction(event -> {
            Stage primaryStage = (Stage) openFileButton.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Game of Life Files", "*.golf"));
            File file = fileChooser.showOpenDialog(primaryStage);

            if (file != null) {
                try (FileGameOfLifeBoardDao dao = new FileGameOfLifeBoardDao(file.getAbsolutePath())) {
                    golb = dao.read();
                    primaryStage.close();
                    SimulationController.openSimulationWindow(golb, currentLanguage);
                } catch (Exception e) {
                    errorMessageWindow(resourceBundle.getString("error.fileRead"));
                }
            }
        });
    }

    private void updateUI() {
        gridSizeXLabel.setText(resourceBundle.getString("label.gridSizeX") + " (4-20):");
        gridSizeXField.setPromptText(resourceBundle.getString("label.enterSize"));
        gridSizeYLabel.setText(resourceBundle.getString("label.gridSizeY") + " (4-20):");
        gridSizeYField.setPromptText(resourceBundle.getString("label.enterSize"));
        densityLabel.setText(resourceBundle.getString("label.density") + " (%):");
        startButton.setText(resourceBundle.getString("button.start"));
        openFileButton.setText(resourceBundle.getString("button.openFile"));

        infoMenu.setText(resourceBundle.getString("menu.info"));
        langMenu.setText(resourceBundle.getString("menu.language"));
        authorsItem.setText(resourceBundle.getString("menu.item.authors"));
        licenseItem.setText(resourceBundle.getString("menu.item.license"));
        polItem.setText(resourceBundle.getString("menu.item.lang.polish"));
        deItem.setText(resourceBundle.getString("menu.item.lang.german"));
    }

    private void errorMessageWindow(String errMess) {
        Stage window = new Stage();
        window.setTitle(resourceBundle.getString("app.errorTitle"));

        Label label = new Label(errMess);
        label.setFont(Font.font("Courier New", 16));
        label.setStyle("-fx-text-fill: #6e0000;-fx-font-weight: bold;");
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(label);

        Scene newScene = new Scene(stackPane, 300, 100);
        window.setScene(newScene);
        window.show();
    }

    private void messageWindow(String mess) {
        Stage window = new Stage();
        window.setTitle(resourceBundle.getString("app.messageTitle"));

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
