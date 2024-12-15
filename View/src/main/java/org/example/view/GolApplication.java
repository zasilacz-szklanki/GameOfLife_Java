package org.example.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.model.*;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class GolApplication extends Application {

    private GameOfLifeBoard golbLoaded;
    private boolean isFromFile = false;

    private ResourceBundle resourceBundle;
    private ResourceBundle authorsResourceBundle;

    Locale defaultLocale = new Locale("pl");

    private void loadResourceBundle() {
        resourceBundle = ResourceBundle.getBundle("org.example.view.lang", defaultLocale);
        authorsResourceBundle = ResourceBundle.getBundle("org.example.view.AuthorsResourceBundle", defaultLocale);
    }

    @Override
    public void start(Stage primaryStage) {

        loadResourceBundle();

        final TextFormatter<String> intFormatterX = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            }
            return null;
        });
        final TextFormatter<String> intFormatterY = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            }
            return null;
        });

        primaryStage.setTitle(resourceBundle.getString("app.configTitle"));

        final Label gridSizeXLabel = new Label(resourceBundle.getString("label.gridSizeX") + " (4-20):");
        gridSizeXLabel.setFont(Font.font("Courier New", 16));
        TextField gridSizeXField = new TextField();
        gridSizeXField.setPromptText(resourceBundle.getString("label.enterSize"));
        gridSizeXField.setFont(Font.font("Courier New", 16));
        gridSizeXField.setTextFormatter(intFormatterX);

        final Label gridSizeYLabel = new Label(resourceBundle.getString("label.gridSizeY") + " (4-20):");
        gridSizeYLabel.setFont(Font.font("Courier New", 16));
        TextField gridSizeYField = new TextField();
        gridSizeYField.setPromptText(resourceBundle.getString("label.enterSize"));
        gridSizeYField.setFont(Font.font("Courier New", 16));
        gridSizeYField.setTextFormatter(intFormatterY);

        final Label densityLabel = new Label(resourceBundle.getString("label.density") + " (%):");
        densityLabel.setFont(Font.font("Courier New", 16));
        ChoiceBox<Density> densityChoiceBox = new ChoiceBox<>();
        densityChoiceBox.getItems().addAll(Density.LOW, Density.MEDIUM, Density.HIGH);
        densityChoiceBox.setStyle("-fx-font-family: Courier New;-fx-font-size: 16;");

        Button startButton = new Button(resourceBundle.getString("button.start"));
        startButton.setFont(Font.font("Courier New", 16));

        Button openFileButton = new Button(resourceBundle.getString("button.openFile"));
        openFileButton.setFont(Font.font("Courier New", 16));

        final MenuBar menuBar = new MenuBar();

        final Menu languageMenu = new Menu(resourceBundle.getString("menu.language"));

        MenuItem polishMenuItem = new MenuItem(resourceBundle.getString("menu.item.lang.polish"));
        polishMenuItem.setOnAction(event -> {
            defaultLocale = new Locale("pl");
            loadResourceBundle();
            start(primaryStage);
        });

        MenuItem germanMenuItem = new MenuItem(resourceBundle.getString("menu.item.lang.german"));
        germanMenuItem.setOnAction(event -> {
            defaultLocale = new Locale("de");
            loadResourceBundle();
            start(primaryStage);
        });

        Menu infoMenu = new Menu(resourceBundle.getString("menu.info"));

        MenuItem authorsMenuItem = new MenuItem(resourceBundle.getString("menu.item.authors"));
        authorsMenuItem.setOnAction(event -> {
            String msg = authorsResourceBundle.getString("author1") + "\n" + authorsResourceBundle.getString("author2");
            messageWindow(msg);
        });

        MenuItem licenseMenuItem = new MenuItem(resourceBundle.getString("menu.item.license"));
        licenseMenuItem.setOnAction(event -> {
            messageWindow("Eclipse Public License - v 2.0");
        });

        infoMenu.getItems().addAll(authorsMenuItem, licenseMenuItem);
        languageMenu.getItems().addAll(polishMenuItem, germanMenuItem);

        menuBar.getMenus().add(languageMenu);
        menuBar.getMenus().add(infoMenu);
        BorderPane bp = new BorderPane();
        bp.setTop(menuBar);

        startButton.setOnAction(event -> {
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

            openSimulationWindow(gridSizeX, gridSizeY, densityChoiceBox.getValue());
        });

        openFileButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Game of Life Files", "*.golf"));
            File file = fileChooser.showOpenDialog(primaryStage);

            if (file != null) {
                try (FileGameOfLifeBoardDao dao = new FileGameOfLifeBoardDao(file.getAbsolutePath())) {
                    golbLoaded = dao.read();
                    List<List<GameOfLifeCell>> g = golbLoaded.getBoard();
                    isFromFile = true;
                    Density d = Density.LOW;
                    primaryStage.close();
                    openSimulationWindow(g.get(0).size(), g.size(), d);
                } catch (Exception e) {
                    errorMessageWindow(resourceBundle.getString("error.fileRead"));
                }
            }
        });

        final VBox layout = new VBox(10);
        VBox.setMargin(gridSizeXLabel, new Insets(20, 0, 0, 20));
        VBox.setMargin(gridSizeXField, new Insets(0, 20, 0, 20));

        VBox.setMargin(gridSizeYLabel, new Insets(0, 0, 0, 20));
        VBox.setMargin(gridSizeYField, new Insets(0, 20, 0, 20));

        VBox.setMargin(densityLabel, new Insets(0, 20, 0, 20));
        VBox.setMargin(densityChoiceBox, new Insets(0, 20, 0, 20));

        VBox center = new VBox(10);
        center.setAlignment(Pos.CENTER);
        VBox.setMargin(startButton, new Insets(20, 0, 0, 20));
        center.getChildren().addAll(startButton);
        VBox.setMargin(openFileButton, new Insets(20, 0, 0, 20));
        center.getChildren().addAll(openFileButton);

        layout.getChildren().addAll(menuBar, gridSizeXLabel, gridSizeXField, gridSizeYLabel,
                gridSizeYField, densityLabel, densityChoiceBox, center);
        Scene scene = new Scene(layout, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openSimulationWindow(int gridSizeX, int gridSizeY, Density density) {
        Stage newWindow = new Stage();
        newWindow.setTitle(resourceBundle.getString("app.simulationTitle"));

        GameOfLifeBoard golb;

        if (isFromFile) {
            golb = golbLoaded;
        } else {
            golb = new GameOfLifeBoard(density.randomFill(gridSizeX, gridSizeY));
        }

        final PlainGameOfLifeSimulator sim = new PlainGameOfLifeSimulator();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: #000057;");

        String fullBlock = "█";
        Label[][] label = new Label[gridSizeX][gridSizeY];

        CellStateConverter converter = new CellStateConverter();
        final boolean[] isColorMode = {true};

        for (int row = 0; row < golb.getBoard().size(); row++) {
            for (int col = 0; col < golb.getBoard().get(row).size(); col++) {
                label[row][col] = new Label(fullBlock);

                boolean cellValue = golb.get(row, col);
                label[row][col].setStyle("-fx-text-fill: " + converter.toColor(cellValue) + ";");
                label[row][col].setFont(Font.font("Courier New", 16));

                final int finalRow = row;
                final int finalCol = col;

                label[row][col].setOnMouseClicked(event -> {
                    boolean currentState = golb.get(finalRow, finalCol);
                    boolean newState = !currentState;

                    golb.set(finalRow, finalCol, newState);

                    if (isColorMode[0]) {
                        label[finalRow][finalCol].setStyle("-fx-text-fill: " + converter.toColor(newState) + ";");
                    } else {
                        label[finalRow][finalCol].setText(converter.fromBooleanToString(newState));
                        label[finalRow][finalCol].setStyle("-fx-text-fill: " + converter.toColor(newState) + ";");
                    }
                });

                gridPane.add(label[row][col], col, row);
            }
        }

        final MenuBar menuBar = new MenuBar();
        final Menu convertMenu = new Menu(resourceBundle.getString("menu.convert"));
        final Menu fileMenu = new Menu(resourceBundle.getString("menu.file"));

        MenuItem convertToColorsItem = new MenuItem(resourceBundle.getString("menu.item.convertToColors"));
        convertToColorsItem.setOnAction(event -> {
            isColorMode[0] = true;
            for (int row = 0; row < golb.getBoard().size(); row++) {
                for (int col = 0; col < golb.getBoard().get(row).size(); col++) {
                    boolean cellValue = golb.get(row, col);
                    label[row][col].setText(fullBlock);
                    label[row][col].setStyle("-fx-text-fill: " + converter.toColor(cellValue) + ";");
                }
            }
        });

        MenuItem convertToNumbersItem = new MenuItem(resourceBundle.getString("menu.item.convertToNumbers"));
        convertToNumbersItem.setOnAction(event -> {
            isColorMode[0] = false;
            for (int row = 0; row < golb.getBoard().size(); row++) {
                for (int col = 0; col < golb.getBoard().get(row).size(); col++) {
                    boolean cellValue = golb.get(row, col);
                    label[row][col].setText(converter.fromBooleanToString(cellValue));
                    label[row][col].setStyle("-fx-text-fill: " + converter.toColor(cellValue) + ";");
                }
            }
        });

        MenuItem saveToFileItem = new MenuItem(resourceBundle.getString("menu.item.saveToFile"));
        saveToFileItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Game of Life Files", "*.golf"));
            File file = fileChooser.showSaveDialog(newWindow);

            if (file != null) {
                try (Dao<GameOfLifeBoard> dao = GameOfLifeBoardDaoFactory.getFileDao(file.getAbsolutePath())) {
                    dao.write(golb);
                } catch (Exception e) {
                    System.out.println(resourceBundle.getString("error.fileWrite"));
                }
            }
        });

        convertMenu.getItems().addAll(convertToColorsItem, convertToNumbersItem);
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
                        label[row][col].setText(fullBlock);
                        label[row][col].setStyle("-fx-text-fill: " + converter.toColor(cellValue) + ";");
                    } else {
                        label[row][col].setText(converter.fromBooleanToString(cellValue));
                        label[row][col].setStyle("-fx-text-fill: " + converter.toColor(cellValue) + ";");
                    }
                }
            }
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

        window.setOnCloseRequest(event -> start(new Stage()));
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

    public static void main(String[] args) {
        launch();
    }
}
