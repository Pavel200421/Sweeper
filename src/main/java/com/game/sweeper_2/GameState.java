package com.game.sweeper_2;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean[][] mineField;
    private String[][] buttonStates;
    private boolean gameOver;
    private int remainingMines;
    private int flagsPlaced;
    private int cellsRevealed;
    private int secondsElapsed;

    // Constructors, getters, and setters
    public GameState(boolean[][] mineField, String[][] buttonStates, boolean gameOver, int remainingMines, int flagsPlaced, int cellsRevealed, int secondsElapsed) {
        this.mineField = mineField;
        this.buttonStates = buttonStates;
        this.gameOver = gameOver;
        this.remainingMines = remainingMines;
        this.flagsPlaced = flagsPlaced;
        this.cellsRevealed = cellsRevealed;
        this.secondsElapsed = secondsElapsed;
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
}
