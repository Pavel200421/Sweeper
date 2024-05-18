package com.game.sweeper_2;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.Random;

public class HelloController {

    @FXML
    private GridPane mainPane;

    @FXML
    private Button NewGameBtn;

    @FXML
    private Button ResetBtn;

    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final int MINES = 20;
    private Button[][] buttons;
    private boolean[][] mineField;
    private boolean gameOver;

    @FXML
    public void handleNewGame() {
        startNewGame();
        NewGameBtn.setVisible(false);
    }

    @FXML
    public void handleReset() {
        startNewGame();
    }

    @FXML
    public void handleSettings() {
        // Settings logic here
    }

    private void startNewGame() {
        mainPane.getChildren().clear();
        buttons = new Button[ROWS][COLS];
        mineField = generateMineField(ROWS, COLS, MINES);
        gameOver = false;

        double buttonSize = 50; // Size of each cell

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
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

    private void handlePrimaryClick(Button button, int row, int col) {
        if (gameOver || button.getText().equals("F")) {
            // Do nothing if the game is over or the cell is flagged
            return;
        }

        button.getStyleClass().remove("untouched-cell");
        if (mineField[row][col]) {
            button.getStyleClass().add("mine-cell");
            button.setDisable(true); // Disable the button to make it non-clickable
            gameOver = true;
            showGameOver();
            // Game over logic here
        } else {
            int adjacentMines = countAdjacentMines(mineField, row, col);
            if (adjacentMines == 0) {
                revealEmptyCells(row, col);
            } else {
                button.setText(String.valueOf(adjacentMines));
                button.getStyleClass().add("safe-cell");
                button.setDisable(true);
                button.getStyleClass().add("disabled-cell");
            }
        }
    }

    private void handleSecondaryClick(Button button) {
        if (!button.isDisabled()) { // Check if the cell is already revealed
            if (button.getText().isEmpty()) {
                button.setText("F");
                button.getStyleClass().add("flag-cell");
            } else {
                button.setText("");
                button.getStyleClass().remove("flag-cell");
            }
        }
    }

    private void revealEmptyCells(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS || buttons[row][col].isDisabled()) {
            return;
        }

        int adjacentMines = countAdjacentMines(mineField, row, col);
        buttons[row][col].setDisable(true);
        buttons[row][col].getStyleClass().remove("untouched-cell");
        buttons[row][col].getStyleClass().add("disabled-cell");

        if (adjacentMines == 0) {
            buttons[row][col].setText("");
            buttons[row][col].getStyleClass().add("empty-cell");
            revealEmptyCells(row - 1, col);
            revealEmptyCells(row + 1, col);
            revealEmptyCells(row, col - 1);
            revealEmptyCells(row, col + 1);
            revealEmptyCells(row - 1, col - 1);
            revealEmptyCells(row - 1, col + 1);
            revealEmptyCells(row + 1, col - 1);
            revealEmptyCells(row + 1, col + 1);
        } else {
            buttons[row][col].setText(String.valueOf(adjacentMines));
            buttons[row][col].getStyleClass().add("safe-cell");
        }
    }

    private boolean[][] generateMineField(int rows, int cols, int mines) {
        boolean[][] mineField = new boolean[rows][cols];
        Random random = new Random();

        for (int i = 0; i < mines; i++) {
            int r, c;
            do {
                r = random.nextInt(rows);
                c = random.nextInt(cols);
            } while (mineField[r][c]);

            mineField[r][c] = true;
        }

        return mineField;
    }

    private int countAdjacentMines(boolean[][] mineField, int row, int col) {
        int count = 0;
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newRow = row + dr[i];
            int newCol = col + dc[i];

            if (newRow >= 0 && newRow < mineField.length && newCol >= 0 && newCol < mineField[0].length && mineField[newRow][newCol]) {
                count++;
            }
        }

        return count;
    }

    private void showGameOver() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("You clicked on a mine! Game Over.");
        alert.showAndWait();

        disableAllButtons();
    }

    private void disableAllButtons() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                buttons[row][col].setDisable(true);
            }
        }
    }
}
