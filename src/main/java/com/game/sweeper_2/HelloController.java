package com.game.sweeper_2;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HelloController {

    @FXML
    private GridPane mainPane;

    @FXML
    private Button NewGameBtn;

    @FXML
    private Button ResetBtn;

    @FXML
    private Label MineCounter, ResetText;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Button settingsPref;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label timerLabel;  // Add this line

    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final int MINES = 20;
    private Button[][] buttons;
    private boolean[][] mineField;
    private boolean gameOver;
    private int remainingMines;
    private int flagsPlaced;
    private int cellsRevealed;

    private static int customMines = MINES;
    private static int customRows = ROWS;
    private static int customCols = COLS;

    private Timeline timeline;
    private int secondsElapsed;

    private MediaPlayer mediaPlayer;

    private int gamesPlayed = 0;
    private int gamesWon = 0;
    private int gamesLost = 0;
    private int bestTime = Integer.MAX_VALUE;

    private List<HighScore> highScores = new ArrayList<>();
    @FXML
    public void handleNewGame() {
        startNewGame();
        startTimer();  // Add this line
        NewGameBtn.setVisible(false);
        ResetText.setVisible(true);
        ResetBtn.setVisible(true); // Make Reset button visible
        MineCounter.setVisible(true);
        ResetBtn.setDisable(false); // Enable Reset button
        menuBar.setVisible(true);
        settingsPref.setVisible(false);
        scrollPane.setVisible(true);
    }
    private void playSoundEffectBlip() {

        String path = getClass().getResource("/blip1.mp3").toString();
        Media media = new Media(path);


        mediaPlayer = new MediaPlayer(media);


        mediaPlayer.play();
    }
    @FXML
    public void handleReset() {
        stopTimer();
        playSoundEffectBlip();
        secondsElapsed = 0;
        timerLabel.setText("Time: 0s");
        startNewGame();
        startTimer();
    }

    public void startNewGame() {
        mainPane.getChildren().clear();
        buttons = new Button[customRows][customCols];
        mineField = generateMineField(customRows, customCols, customMines);
        gameOver = false;
        remainingMines = customMines;
        flagsPlaced = 0;
        cellsRevealed = 0;
        updateMineCounter();

        double buttonSize = 50;

        mainPane.setAlignment(Pos.CENTER);

        for (int row = 0; row < customRows; row++) {
            for (int col = 0; col < customCols; col++) {
                Button button = new Button();
                button.getStyleClass().add("untouched-cell");
                button.setPrefWidth(buttonSize);
                button.setPrefHeight(buttonSize);
                button.setMaxWidth(buttonSize);
                button.setMaxHeight(buttonSize);
                button.setMinWidth(buttonSize);
                button.setMinHeight(buttonSize);

                int r = row;
                int c = col;

                button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        handlePrimaryClick(button, r, c);
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        handleSecondaryClick(button);
                    }
                });

                buttons[row][col] = button;
                mainPane.add(button, col, row);
            }
        }
    }


    @FXML
    public void handleSettingsButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settingsController.fxml"));
            Parent parent = fxmlLoader.load();

            SettingsController settingsController = fxmlLoader.getController();
            settingsController.setInitialValues(customMines, customRows, customCols);

            Stage settingsStage = new Stage();
            Image ico = new Image("/settings.png"); settingsStage.getIcons().add(ico);
            settingsStage.initModality(Modality.APPLICATION_MODAL);
            settingsStage.setTitle("Settings");

            settingsStage.setScene(new Scene(parent));

            settingsController.setStage((Stage) settingsStage.getScene().getWindow());

            settingsStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRulesButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("rulesController.fxml"));
            Parent parent = fxmlLoader.load();

            RulesController rulesController = fxmlLoader.getController();
            Stage rulesStage = new Stage();
            rulesStage.initModality(Modality.APPLICATION_MODAL);
            rulesStage.setTitle("Rules");
            rulesStage.setScene(new Scene(parent));
            rulesController.setStage((Stage) rulesStage.getScene().getWindow());
            rulesStage.setMinWidth(400);
            rulesStage.setMinHeight(500);
            rulesStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void setSettings(int mines, int rows, int cols) {
        if (mines > 0 && rows > 0 && cols > 0 && rows <= 75 && cols <= 75) {
            customMines = mines;
            customRows = rows;
            customCols = cols;
        }
    }

    private void handleSecondaryClick(Button button) {
        if (!button.isDisabled()) {
            Boolean isFlagSet = (Boolean) button.getUserData();

            if (isFlagSet == null || !isFlagSet) {
                if (flagsPlaced < customMines) {
                    button.setText("F");
                    button.getStyleClass().add("flag-cell");
                    button.setUserData(true);
                    flagsPlaced++;
                    remainingMines--;
                    updateMineCounter();
                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Too Many Flags");
                    alert.setHeaderText(null);
                    alert.setContentText("You have placed the maximum number of flags.");
                    alert.showAndWait();
                }
            } else {
                button.setText("");
                button.getStyleClass().remove("flag-cell");
                button.setUserData(false);
                flagsPlaced--;
                remainingMines++;
                updateMineCounter();
            }
        }
    }

    private void handlePrimaryClick(Button button, int row, int col) {
        if (!gameOver && button.getText().isEmpty()) {
            button.getStyleClass().remove("untouched-cell");
            if (mineField[row][col]) {
                button.getStyleClass().add("mine-cell");
                button.setDisable(true);
                gameOver = true;
                showGameOver();
            } else {
                int adjacentMines = countAdjacentMines(mineField, row, col);
                if (adjacentMines == 0) {
                    revealEmptyCells(row, col);
                } else {
                    button.setText(String.valueOf(adjacentMines));
                    button.getStyleClass().add("safe-cell");
                    button.getStyleClass().add("number-" + adjacentMines);
                    button.setDisable(true);
                    cellsRevealed++;
                    checkWinCondition();
                }
            }
        }
    }

    private void updateMineCounter() {
        MineCounter.setText("Mines: " + remainingMines);
    }
    private void playSoundEffectWarning() {
        // Указываем путь к аудиофайлу в ресурсах
        String path = getClass().getResource("/warning.mp3").toString();
        Media media = new Media(path);

        // Создаем новый экземпляр MediaPlayer
        mediaPlayer = new MediaPlayer(media);

        // Воспроизводим звук
        mediaPlayer.play();
    }
    private void showGameOver() {
        stopTimer();
        playSoundEffectWarning();
        gamesPlayed++;
        gamesLost++;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("You clicked on a mine! Game over.");
        alert.showAndWait();
        revealAllMines();
    }

    private void revealAllMines() {
        for (int row = 0; row < customRows; row++) {
            for (int col = 0; col < customCols; col++) {
                if (mineField[row][col]) {
                    Button button = buttons[row][col];
                    button.getStyleClass().remove("untouched-cell");
                    button.getStyleClass().add("mine-cell");
                    button.setDisable(true);
                }
            }
        }
    }

    private void checkWinCondition() {
        if (cellsRevealed == customRows * customCols - customMines) {
            stopTimer();
            gameOver = true;
            gamesPlayed++;
            gamesWon++;
            if (secondsElapsed < bestTime) {
                bestTime = secondsElapsed;
                highScores.add(new HighScore(secondsElapsed));
                Collections.sort(highScores);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You Win!");
            alert.setHeaderText(null);
            alert.setContentText("Congratulations! You've cleared all the mines.");
            alert.showAndWait();
        }
    }

    private boolean[][] generateMineField(int rows, int cols, int mines) {
        boolean[][] field = new boolean[rows][cols];
        Random random = new Random();
        int placedMines = 0;

        while (placedMines < mines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            if (!field[row][col]) {
                field[row][col] = true;
                placedMines++;
            }
        }

        return field;
    }

    private int countAdjacentMines(boolean[][] field, int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < field.length && newCol >= 0 && newCol < field[0].length && field[newRow][newCol]) {
                    count++;
                }
            }
        }
        return count;
    }

    private void revealEmptyCells(int row, int col) {
        if (row < 0 || row >= customRows || col < 0 || col >= customCols || buttons[row][col].isDisabled()) {
            return;
        }

        int adjacentMines = countAdjacentMines(mineField, row, col);
        buttons[row][col].setDisable(true);
        buttons[row][col].getStyleClass().remove("untouched-cell");

        if (adjacentMines == 0) {
            buttons[row][col].getStyleClass().add("safe-cell");
            cellsRevealed++;
            checkWinCondition();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        revealEmptyCells(row + i, col + j);
                    }
                }
            }
        } else {
            buttons[row][col].setText(String.valueOf(adjacentMines));
            buttons[row][col].getStyleClass().add("safe-cell");
            buttons[row][col].getStyleClass().add("number-" + adjacentMines);
            cellsRevealed++;
            checkWinCondition();
        }
    }
    @FXML
    public void handleShowStatistics() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("statistics.fxml"));
            Parent parent = fxmlLoader.load();

            StatisticsController statisticsController = fxmlLoader.getController();
            statisticsController.setStatistics(gamesPlayed, gamesWon, gamesLost, bestTime);

            Stage statisticsStage = new Stage();
            statisticsStage.setTitle("Game Statistics");
            statisticsStage.setScene(new Scene(parent));
            statisticsController.setStage((Stage) statisticsStage.getScene().getWindow());
            statisticsStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        mainPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        ResetBtn.getStyleClass().add("ResetButton");
        ResetBtn.setOnMouseEntered(event -> ResetBtn.setCursor(Cursor.HAND));
        ResetBtn.setOnMouseExited(event -> ResetBtn.setCursor(Cursor.DEFAULT));
        NewGameBtn.setVisible(true);
        ResetBtn.setVisible(false);
        MineCounter.setVisible(false);
        menuBar.setVisible(true);
        settingsPref.setVisible(true);
        scrollPane.setVisible(false);
        timerLabel.setVisible(false);
    }

    // Method to start the timer
    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsElapsed++;
            timerLabel.setText("Time: " + secondsElapsed + "s");
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        timerLabel.setVisible(true);
    }

    // Method to stop the timer
    private void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }
    @FXML
    public void handleSaveGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Minesweeper Save Files", "*.mswp"));
        File file = fileChooser.showSaveDialog(mainPane.getScene().getWindow());

        if (file != null) {
            try {
                GameState gameState = new GameState(mineField, getButtonStates(), gameOver, remainingMines, flagsPlaced, cellsRevealed, secondsElapsed, customRows, customCols);
                GamePersistence.saveGame(gameState, file.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleLoadGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Minesweeper Save Files", "*.mswp"));
        File file = fileChooser.showOpenDialog(mainPane.getScene().getWindow());

        if (file != null) {
            try {
                GameState gameState = GamePersistence.loadGame(file.getPath());
                if (gameState.getRows() != customRows || gameState.getCols() != customCols) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Load Game Error");
                    alert.setHeaderText(null);
                    alert.setContentText("The saved game size does not match the current settings. Please adjust the settings to match the saved game size.");
                    alert.showAndWait();
                } else {
                    loadGameState(gameState);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private String[][] getButtonStates() {
        String[][] buttonStates = new String[customRows][customCols];
        for (int row = 0; row < customRows; row++) {
            for (int col = 0; col < customCols; col++) {
                Button button = buttons[row][col];
                if (button.getStyleClass().contains("flag-cell")) {
                    buttonStates[row][col] = "F";
                } else if (button.getStyleClass().contains("mine-cell")) {
                    buttonStates[row][col] = "M";
                } else if (button.getStyleClass().contains("safe-cell")) {
                    buttonStates[row][col] = button.getText();
                } else {
                    buttonStates[row][col] = "";
                }
            }
        }
        return buttonStates;
    }


    private void loadGameState(GameState gameState) {
        stopTimer();
        mineField = gameState.getMineField();
        gameOver = gameState.isGameOver();
        remainingMines = gameState.getRemainingMines();
        flagsPlaced = gameState.getFlagsPlaced();
        cellsRevealed = gameState.getCellsRevealed();
        secondsElapsed = gameState.getSecondsElapsed();
        updateMineCounter();
        timerLabel.setText("Time: " + secondsElapsed + "s");

        String[][] buttonStates = gameState.getButtonStates();
        for (int row = 0; row < customRows; row++) {
            for (int col = 0; col < customCols; col++) {
                Button button = buttons[row][col];
                String state = buttonStates[row][col];
                button.setText(state.equals("F") ? "" : state);

                boolean isClickable = state.isEmpty() || state.equals("F");
                button.setDisable(!isClickable);

                button.getStyleClass().removeAll("untouched-cell", "safe-cell", "mine-cell", "flag-cell", "number-1", "number-2", "number-3", "number-4", "number-5", "number-6", "number-7", "number-8");

                if (state.equals("F")) {
                    button.getStyleClass().add("flag-cell");
                    button.setUserData(true);
                } else if (state.equals("M")) {
                    button.getStyleClass().add("mine-cell");
                } else if (state.matches("\\d")) {
                    button.getStyleClass().add("safe-cell");
                    button.getStyleClass().add("number-" + state);
                } else {
                    button.getStyleClass().add("untouched-cell");
                }
            }
        }

        if (!gameOver) {
            startTimer();
        }
    }

}
