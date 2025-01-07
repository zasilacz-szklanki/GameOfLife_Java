package org.example.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

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
    private Button openDbButton;

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

    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);

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

    private int[] sizeToInteger(String sx, String sy) throws NumberException {
        int[] a = new int[2];
        try {
            a[0] = Integer.parseInt(sx);
            a[1] = Integer.parseInt(sy);
        } catch (NumberFormatException e) {
            throw new NumberException();
        }
        return a;
    }

    private void readFromFile(File file, Stage primaryStage) throws FileException {
        try (FileGameOfLifeBoardDao dao = new FileGameOfLifeBoardDao(file.getAbsolutePath())) {
            golb = dao.read();
            primaryStage.close();
            SimulationController.openSimulationWindow(golb, currentLanguage);
            logger.info(resourceBundle.getString("action.loadedFromFile") + " " + file.getAbsolutePath());
        } catch (Exception e) {
            throw new FileException();
        }
    }

    private void readFromDb(String boardName, Stage primaryStage) throws DbException {
        try (Dao<GameOfLifeBoard> dao = GameOfLifeBoardDaoFactory.getDbDao(boardName)) {
            golb = dao.read();
            primaryStage.close();
            logger.info(resourceBundle.getString("action.loadedFromDb") + ": " + boardName);
        } catch (Exception e) {
            throw new DbReadException();
        }
    }

    @FXML
    public void initialize() {
        loadResourceBundle(currentLanguage);

        logger.info(resourceBundle.getString("action.appStarted"));

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
            logger.info(resourceBundle.getString("action.langChanged"));
        });

        deItem.setOnAction(event -> {
            loadResourceBundle("de");
            updateUI();
            logger.info(resourceBundle.getString("action.langChanged"));
        });

        authorsItem.setOnAction(event -> {
            String msg = authorsResourceBundle.getString("author1") + "\n" + authorsResourceBundle.getString("author2");
            MessageWindow.messageWindow(msg, resourceBundle.getString("app.messageTitle"));
            logger.info(resourceBundle.getString("action.authorsShowed"));
        });

        licenseItem.setOnAction(event -> {
            MessageWindow.messageWindow("Eclipse Public License - v 2.0", resourceBundle.getString("app.messageTitle"));
            logger.info(resourceBundle.getString("action.licenseShowed"));
        });

        menuBar.getMenus().clear();
        menuBar.getMenus().addAll(langMenu, infoMenu);

        startButton.setOnAction(event -> {
            Stage primaryStage = (Stage) startButton.getScene().getWindow();
            primaryStage.close();

            String sx = gridSizeXField.getText();
            String sy = gridSizeYField.getText();

            if (sx.isBlank() || sy.isBlank()) {
                MessageWindow.errorMessageWindow(resourceBundle.getString("error.blankInput"),
                        resourceBundle.getString("app.errorTitle"));
                return;
            }

            final int gridSizeX;
            final int gridSizeY;
            int[] arr;
            try {
                arr = sizeToInteger(sx, sy);
            } catch (NumberException e) {
                MessageWindow.errorMessageWindow(resourceBundle.getString("error.numberFormatException"),
                        resourceBundle.getString("app.errorTitle"));
                return;
            }
            gridSizeX = arr[0];
            gridSizeY = arr[1];

            if (gridSizeX < 4 || gridSizeX > 30 || gridSizeY < 4 || gridSizeY > 80) {
                MessageWindow.errorMessageWindow(resourceBundle.getString("error.wrongSize"),
                        resourceBundle.getString("app.errorTitle"));
                return;
            }

            if (densityChoiceBox.getValue() == null) {
                MessageWindow.errorMessageWindow(resourceBundle.getString("error.noDensitySelected"),
                        resourceBundle.getString("app.errorTitle"));
                return;
            }

            golb = new GameOfLifeBoard(densityChoiceBox.getValue().randomFill(gridSizeX, gridSizeY));
            SimulationController.openSimulationWindow(golb, currentLanguage);
            logger.info(resourceBundle.getString("action.simulationStarted"));
        });

        openFileButton.setOnAction(event -> {
            Stage primaryStage = (Stage) openFileButton.getScene().getWindow();

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Game of Life Files", "*.golf"));
            File file = fileChooser.showOpenDialog(primaryStage);

            try {
                readFromFile(file, primaryStage);
            } catch (FileException e) {
                MessageWindow.errorMessageWindow(resourceBundle.getString("error.fileRead"),
                        resourceBundle.getString("app.errorTitle"));
            }
        });

        openDbButton.setOnAction(event -> {
            Stage primaryStage = (Stage) openDbButton.getScene().getWindow();

            List<String> boardNames = new ArrayList<>();
            try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("jakasplansza");) {
                boardNames = dao.getBoardNames();
            } catch (Exception e) {
                MessageWindow.errorMessageWindow(resourceBundle.getString("error.dbLoad"),
                        resourceBundle.getString("app.errorTitle"));
            }

            String boardName = MessageWindow.boardNameChooseWindow(boardNames,
                    resourceBundle.getString("button.openDb"),
                    resourceBundle.getString("app.dbTitle"));

            if (boardName == null) {
                MessageWindow.errorMessageWindow(resourceBundle.getString("error.boardNotLoaded"),
                        resourceBundle.getString("app.errorTitle"));
            } else {
                try {
                    readFromDb(boardName, primaryStage);
                    SimulationController.openSimulationWindow(golb, currentLanguage);
                } catch (Exception e) {
                    MessageWindow.errorMessageWindow(resourceBundle.getString("error.dbRead"),
                            resourceBundle.getString("app.errorTitle"));
                }
            }
        });
    }

    private void updateUI() {
        gridSizeXLabel.setText(resourceBundle.getString("label.gridSizeX") + " (4-30):");
        gridSizeXField.setPromptText(resourceBundle.getString("label.enterSize"));
        gridSizeYLabel.setText(resourceBundle.getString("label.gridSizeY") + " (4-80):");
        gridSizeYField.setPromptText(resourceBundle.getString("label.enterSize"));
        densityLabel.setText(resourceBundle.getString("label.density") + " (%):");
        startButton.setText(resourceBundle.getString("button.start"));
        openFileButton.setText(resourceBundle.getString("button.openFile"));
        openDbButton.setText(resourceBundle.getString("button.openDb"));

        infoMenu.setText(resourceBundle.getString("menu.info"));
        langMenu.setText(resourceBundle.getString("menu.language"));
        authorsItem.setText(resourceBundle.getString("menu.item.authors"));
        licenseItem.setText(resourceBundle.getString("menu.item.license"));
        polItem.setText(resourceBundle.getString("menu.item.lang.polish"));
        deItem.setText(resourceBundle.getString("menu.item.lang.german"));
    }
}