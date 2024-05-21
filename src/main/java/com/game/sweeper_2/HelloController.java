package com.game.sweeper_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
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

    @FXML
    public void handleNewGame() {
        startNewGame();
        NewGameBtn.setVisible(false);
        ResetText.setVisible(true);
        ResetBtn.setVisible(true); // Make Reset button visible
        MineCounter.setVisible(true);
        ResetBtn.setDisable(false); // Enable Reset button
        menuBar.setVisible(true);
        settingsPref.setVisible(false);
        scrollPane.setVisible(true);

    }

    @FXML
    public void handleReset() {
        startNewGame();
    }

    private void startNewGame() {
        mainPane.getChildren().clear();
        buttons = new Button[customRows][customCols];
        mineField = generateMineField(customRows, customCols, customMines);
        gameOver = false;
        remainingMines = customMines;
        flagsPlaced = 0;
        cellsRevealed = 0;
        updateMineCounter();

        double buttonSize = 50;

        for (int row = 0; row < customRows; row++) {
            for (int col = 0; col < customCols; col++) {
                Button button = new Button();
                button.getStyleClass().add("untouched-cell");
                button.setPrefWidth(buttonSize);
                button.setPrefHeight(buttonSize);

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
            // Загрузка FXML-файла с окном настроек
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settingsController.fxml"));
            Parent parent = fxmlLoader.load();

            // Получение контроллера для передачи текущих значений
            SettingsController settingsController = fxmlLoader.getController();
            settingsController.setInitialValues(customMines, customRows, customCols);

            // Создание нового Stage для модального окна
            Stage settingsStage = new Stage();
            Image ico = new Image("/settings.png"); settingsStage.getIcons().add(ico);
            settingsStage.initModality(Modality.APPLICATION_MODAL);
            settingsStage.setTitle("Settings");

            // Установка сцены для нового Stage
            settingsStage.setScene(new Scene(parent));

            // Отображение модального окна и ожидание его закрытия
            settingsStage.showAndWait();
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
            Boolean isFlagSet = (Boolean) button.getUserData(); // Проверяем, установлен ли флажок

            if (isFlagSet == null || !isFlagSet) { // Если флажок не установлен
                if (flagsPlaced < customMines) {
                    button.setText("F");
                    button.getStyleClass().add("flag-cell");
                    button.setUserData(true); // Устанавливаем состояние флажка на кнопке
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
            } else { // Если флажок уже установлен
                button.setText(""); // Убираем текст кнопки
                button.getStyleClass().remove("flag-cell");
                button.setUserData(false); // Устанавливаем состояние флажка на кнопке
                flagsPlaced--;
                remainingMines++;
                updateMineCounter();
            }
        }
    }

    private void handlePrimaryClick(Button button, int row, int col) {
        if (!gameOver && button.getText().isEmpty()) { // Добавляем проверку, что на кнопке нет текста
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

    private void showGameOver() {
        Alert alert = new Alert(AlertType.INFORMATION);
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
            gameOver = true;
            Alert alert = new Alert(AlertType.INFORMATION);
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
        Button button = buttons[row][col];
        button.getStyleClass().remove("untouched-cell");
        button.getStyleClass().add("safe-cell");
        button.setDisable(true);
        cellsRevealed++;

        if (adjacentMines > 0) {
            button.setText(String.valueOf(adjacentMines));
            button.getStyleClass().add("number-" + adjacentMines);
        } else {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    revealEmptyCells(row + i, col + j);
                }
            }
        }

        checkWinCondition();
    }

    @FXML
    public void initialize() {
        mainPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        ResetBtn.getStyleClass().add("ResetButton"); // Добавляем стиль кнопке
        // Устанавливаем курсор "рука" при наведении на кнопку
        ResetBtn.setOnMouseEntered(event -> ResetBtn.setCursor(Cursor.HAND));
        ResetBtn.setOnMouseExited(event -> ResetBtn.setCursor(Cursor.DEFAULT));
        NewGameBtn.setVisible(true);
        ResetBtn.setVisible(false); // Reset button initially hidden
        MineCounter.setVisible(false); // Mine counter initially hidden
        menuBar.setVisible(true);
        settingsPref.setVisible(true);
        scrollPane.setVisible(false);
    }
}

