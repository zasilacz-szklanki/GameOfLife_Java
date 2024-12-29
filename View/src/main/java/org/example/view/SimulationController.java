package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;


public class SimulationController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);

    private static ResourceBundle resourceBundle;

    public static void loadResourceBundle(String language) {
        resourceBundle = ResourceBundle.getBundle("org.example.view.lang", new Locale(language));
    }

    private static void saveToFile(File file, GameOfLifeBoard golb) throws MyException {
        try (Dao<GameOfLifeBoard> dao = GameOfLifeBoardDaoFactory.getFileDao(file.getAbsolutePath())) {
            dao.write(golb);
            logger.info(resourceBundle.getString("action.savedToFile") + " " + file.getAbsolutePath());
        } catch (Exception e) {
            throw new MyException();
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
            } catch (MyException e) {
                MessageWindow.errorMessageWindow(resourceBundle.getString("error.fileWrite"),
                        resourceBundle.getString("app.errorTitle"));
            }
        });

        convertMenu.getItems().addAll(convertToBlocksItem, convertToNumbersItem);
        menuBar.getMenus().addAll(convertMenu);
        fileMenu.getItems().addAll(saveToFileItem);
        menuBar.getMenus().addAll(fileMenu);

        Button nextStepButton = new Button(resourceBundle.getString("button.nextStep"));
        nextStepButton.setFont(Font.font("Courier New", 16));
        nextStepButton.setPrefWidth(Region.USE_COMPUTED_SIZE);
        nextStepButton.setOnAction(event -> {
            golb.doSimulationStep(sim);
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
            logger.info(resourceBundle.getString("action.nextStep"));
        });


        HBox bottomBox = new HBox(nextStepButton);
        bottomBox.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(gridPane);
        layout.setBottom(bottomBox);
        layout.setStyle("-fx-background-color: #000057;");

        int padding = 30;
        double windowWidth = gridSizeY * 12 + padding;
        double windowHeight = gridSizeX * 20 + padding + 100;
        Scene newScene = new Scene(layout, windowWidth, windowHeight);
        newWindow.setScene(newScene);
        newWindow.show();
    }
}
