package com.game.sweeper_2;
import java.io.Serializable;

public class GameState implements Serializable {
    private boolean[][] mineField;
    private String[][] buttonStates;
    private boolean gameOver;
    private int remainingMines;
    private int flagsPlaced;
    private int cellsRevealed;
    private int secondsElapsed;
    private int rows;
    private int cols;

    public GameState(boolean[][] mineField, String[][] buttonStates, boolean gameOver, int remainingMines, int flagsPlaced, int cellsRevealed, int secondsElapsed, int rows, int cols) {
        this.mineField = mineField;
        this.buttonStates = buttonStates;
        this.gameOver = gameOver;
        this.remainingMines = remainingMines;
        this.flagsPlaced = flagsPlaced;
        this.cellsRevealed = cellsRevealed;
        this.secondsElapsed = secondsElapsed;
        this.rows = rows;
        this.cols = cols;
    }

    public boolean[][] getMineField() {
        return mineField;
    }

    public String[][] getButtonStates() {
        return buttonStates;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getRemainingMines() {
        return remainingMines;
    }

    public int getFlagsPlaced() {
        return flagsPlaced;
    }

    public int getCellsRevealed() {
        return cellsRevealed;
    }

    public int getSecondsElapsed() {
        return secondsElapsed;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
