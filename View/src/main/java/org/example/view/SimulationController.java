package org.example.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.model.*;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class SimulationController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);

    private static ResourceBundle resourceBundle;

    public static void loadResourceBundle(String language) {
        resourceBundle = ResourceBundle.getBundle("org.example.view.lang", new Locale(language));
    }

    private static void saveToFile(File file, GameOfLifeBoard golb) throws FileException {
        try (Dao<GameOfLifeBoard> dao = GameOfLifeBoardDaoFactory.getFileDao(file.getAbsolutePath())) {
            dao.write(golb);
            logger.info(resourceBundle.getString("action.savedToFile") + " " + file.getAbsolutePath());
        } catch (Exception e) {
            throw new FileException();
        }
    }

    private static void saveToDb(String boardName, GameOfLifeBoard golb) throws DbWriteException {
        try (Dao<GameOfLifeBoard> dao = GameOfLifeBoardDaoFactory.getDbDao(boardName)) {
            dao.write(golb);
            logger.info(resourceBundle.getString("action.savedInDb") + ": " + boardName);
        } catch (Exception e) {
            throw new DbWriteException();
        }
    }

    private static void setBoardLabels(GameOfLifeBoard golb, PlainGameOfLifeSimulator sim, boolean[] isColorMode,
                                       Label[][] label, CellStateConverter converter) {
        try {
            golb.doSimulationStep(sim);
        } catch (DoStepException e) {
            MessageWindow.errorMessageWindow(resourceBundle.getString("error.cannotDoStep"),
                    resourceBundle.getString("app.errorTitle"));
        }
        for (int row = 0; row < golb.getBoard().size(); row++) {
            for (int col = 0; col < golb.getBoard().get(row).size(); col++) {
                boolean cellValue = golb.get(row, col);
                if (isColorMode[0]) {
                    label[row][col].setText(converter.toBlock(cellValue));
                    label[row][col].setStyle("-fx-text-fill: " + converter.toColor(cellValue) + ";");
                } else {
                    label[row][col].setText(converter.toString(cellValue));
                    label[row][col].setStyle("-fx-text-fill: " + converter.toColor(cellValue) + ";");
                }
            }
        }
    }

    public static void openSimulationWindow(GameOfLifeBoard golb, String language) {

        loadResourceBundle(language);

        Stage newWindow = new Stage();
        newWindow.setTitle(resourceBundle.getString("app.simulationTitle"));

        final PlainGameOfLifeSimulator sim = new PlainGameOfLifeSimulator();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: #000057;");

        int gridSizeX = golb.getBoard().size();
        int gridSizeY = golb.getBoard().get(0).size();

        Label[][] label = new Label[gridSizeX][gridSizeY];

        CellStateConverter converter = new CellStateConverter();
        final boolean[] isColorMode = {true};

        for (int row = 0; row < golb.getBoard().size(); row++) {
            for (int col = 0; col < golb.getBoard().get(row).size(); col++) {
                label[row][col] = new Label(converter.toBlock(golb.get(row, col)));

                boolean cellValue = golb.get(row, col);
                label[row][col].setStyle("-fx-text-fill: " + converter.toColor(cellValue) + ";");
                label[row][col].setFont(Font.font("Courier New", 16));

                final int finalRow = row;
                final int finalCol = col;

                label[row][col].setOnMouseClicked(event -> {
                    boolean currentState = golb.get(finalRow, finalCol);
                    boolean newState = !currentState;

                    golb.set(finalRow, finalCol, newState);
                    logger.info(resourceBundle.getString("action.stateChanged") + " " + newState);

                    if (isColorMode[0]) {
                        label[finalRow][finalCol].setText(converter.toBlock(newState));
                        label[finalRow][finalCol].setStyle("-fx-text-fill: " + converter.toColor(newState) + ";");
                    } else {
                        label[finalRow][finalCol].setText(converter.toString(newState));
                        label[finalRow][finalCol].setStyle("-fx-text-fill: " + converter.toColor(newState) + ";");
                    }
                });

                gridPane.add(label[row][col], col, row);
            }
        }

        final MenuBar menuBar = new MenuBar();
        final Menu convertMenu = new Menu(resourceBundle.getString("menu.convert"));
        final Menu fileMenu = new Menu(resourceBundle.getString("menu.file"));
        final Menu dbMenu = new Menu(resourceBundle.getString("menu.database"));
        final Menu boardMenu = new Menu(resourceBundle.getString("menu.board"));

        MenuItem convertToBlocksItem = new MenuItem(resourceBundle.getString("menu.item.convertToBlocks"));
        MenuItem convertToNumbersItem = new MenuItem(resourceBundle.getString("menu.item.convertToNumbers"));

        convertToBlocksItem.setOnAction(event -> {
            isColorMode[0] = true;
            for (int row = 0; row < golb.getBoard().size(); row++) {
                for (int col = 0; col < golb.getBoard().get(row).size(); col++) {
                    boolean cellValue = golb.get(row, col);
                    label[row][col].setText(converter.toBlock(cellValue));
                    label[row][col].setStyle("-fx-text-fill: " + converter.toColor(cellValue) + ";");
                }
            }
            logger.info(resourceBundle.getString("action.convertedToBlocks"));
        });

        convertToNumbersItem.setOnAction(event -> {
            isColorMode[0] = false;
            for (int row = 0; row < golb.getBoard().size(); row++) {
                for (int col = 0; col < golb.getBoard().get(row).size(); col++) {
                    boolean cellValue = golb.get(row, col);
                    label[row][col].setText(converter.toString(cellValue));
                    label[row][col].setStyle("-fx-text-fill: " + converter.toColor(cellValue) + ";");
                }
            }
            logger.info(resourceBundle.getString("action.convertedTo01"));
        });

        MenuItem saveToFileItem = new MenuItem(resourceBundle.getString("menu.item.saveToFile"));
        saveToFileItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Game of Life Files", "*.golf"));
            File file = fileChooser.showSaveDialog(newWindow);

            try {
                saveToFile(file,golb);
            } catch (FileException e) {
                MessageWindow.errorMessageWindow(resourceBundle.getString("error.fileWrite"),
                        resourceBundle.getString("app.errorTitle"));
            }
        });

        MenuItem saveInDbItem = new MenuItem(resourceBundle.getString("menu.item.saveInDb"));
        saveInDbItem.setOnAction(event -> {

            String boardName = MessageWindow.boardSaver(resourceBundle.getString("menu.item.saveInDb"),
                    resourceBundle.getString("app.dbTitle"));

            if (boardName == null) {
                MessageWindow.errorMessageWindow(resourceBundle.getString("error.boardNotLoaded"),
                        resourceBundle.getString("app.errorTitle"));
            } else {
                List<String> boardNames;
                try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("jakasplansza");) {
                    boardNames = dao.getBoardNames();
                } catch (Exception e) {
                    MessageWindow.errorMessageWindow(resourceBundle.getString("error.dbLoad"),
                            resourceBundle.getString("app.errorTitle"));
                    return;
                }
                if (boardNames.contains(boardName)) {
                    MessageWindow.errorMessageWindow(resourceBundle.getString("error.nameAlreadyExists"),
                            resourceBundle.getString("app.errorTitle"));
                } else {
                    try {
                        saveToDb(boardName, golb);

                    } catch (DbException e) {
                        MessageWindow.errorMessageWindow(resourceBundle.getString("error.dbWrite"),
                                resourceBundle.getString("app.errorTitle"));
                    }
                }
            }
        });

        MenuItem cleanItem = new MenuItem(resourceBundle.getString("menu.item.clean"));
        cleanItem.setOnAction(event -> {
            for (int row = 0; row < golb.getBoard().size(); row++) {
                for (int col = 0; col < golb.getBoard().get(row).size(); col++) {
                    golb.set(row, col, false);
                    if (isColorMode[0]) {
                        label[row][col].setText(converter.toBlock(false));
                        label[row][col].setStyle("-fx-text-fill: " + converter.toColor(false) + ";");
                    } else {
                        label[row][col].setText(converter.toString(false));
                        label[row][col].setStyle("-fx-text-fill: " + converter.toColor(false) + ";");
                    }
                }
            }

            logger.info(resourceBundle.getString("action.boardCleaned"));
        });

        convertMenu.getItems().addAll(convertToBlocksItem, convertToNumbersItem);
        menuBar.getMenus().addAll(convertMenu);
        fileMenu.getItems().addAll(saveToFileItem);
        menuBar.getMenus().addAll(fileMenu);
        dbMenu.getItems().addAll(saveInDbItem);
        menuBar.getMenus().addAll(dbMenu);
        boardMenu.getItems().addAll(cleanItem);
        menuBar.getMenus().addAll(boardMenu);

        Button nextStepButton = new Button(resourceBundle.getString("button.nextStep"));
        nextStepButton.setFont(Font.font("Courier New", 16));
        nextStepButton.setPrefWidth(Region.USE_COMPUTED_SIZE);
        nextStepButton.setOnAction(event -> {
            setBoardLabels(golb, sim, isColorMode, label, converter);
            logger.info(resourceBundle.getString("action.nextStep"));
        });

        Button playButton = new Button();
        FontIcon playIcon = new FontIcon(Material.PLAY_ARROW);
        playButton.setGraphic(playIcon);

        Button stopButton = new Button();
        FontIcon stopIcon = new FontIcon(Material.PAUSE);
        stopButton.setGraphic(stopIcon);

        Timeline simulationTimeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            setBoardLabels(golb, sim, isColorMode, label, converter);
        }));
        simulationTimeline.setCycleCount(Timeline.INDEFINITE);

        playButton.setOnAction(event -> {
            simulationTimeline.play();
            logger.info(resourceBundle.getString("action.autoSimulationStarted"));
        });

        stopButton.setOnAction(event -> {
            simulationTimeline.stop();
            logger.info(resourceBundle.getString("action.autoSimulationStopped"));
        });

        HBox boxPlayStop = new HBox(10);
        boxPlayStop.getChildren().addAll(playButton, stopButton);
        boxPlayStop.setAlignment(Pos.CENTER);

        VBox buttonBox = new VBox(10);
        buttonBox.getChildren().addAll(nextStepButton, boxPlayStop);
        buttonBox.setAlignment(Pos.CENTER);

        HBox bottomBox = new HBox(buttonBox);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20));

        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(gridPane);
        layout.setBottom(bottomBox);
        layout.setStyle("-fx-background-color: #000057;");

        int padding = 30;
        double windowWidth = gridSizeY * 12 + padding;
        double windowHeight = gridSizeX * 20 + padding + 100;
        double minWidth = 300;
        double minHeight = 400;
        Scene newScene = new Scene(layout, windowWidth, windowHeight);
        newWindow.setMinWidth(minWidth);
        newWindow.setMinHeight(minHeight);
        newWindow.setScene(newScene);
        newWindow.show();
    }
}
