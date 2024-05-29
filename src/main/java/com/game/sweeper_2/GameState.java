package com.game.sweeper_2;

import java.io.Serializable; // Импорт интерфейса для сериализации объектов

public class GameState implements Serializable { // Объявление класса игрового состояния, реализующего интерфейс Serializable

    private boolean[][] mineField; // Массив для хранения информации о минном поле
    private String[][] buttonStates; // Массив для хранения информации о состоянии кнопок
    private boolean gameOver; // Флаг, указывающий на завершение игры
    private int remainingMines; // Количество оставшихся мин
    private int flagsPlaced; // Количество установленных флагов
    private int cellsRevealed; // Количество открытых ячеек
    private int secondsElapsed; // Количество прошедших секунд
    private int rows; // Количество строк в игровом поле
    private int cols; // Количество столбцов в игровом поле

    public GameState(boolean[][] mineField, String[][] buttonStates, boolean gameOver, int remainingMines, int flagsPlaced, int cellsRevealed, int secondsElapsed, int rows, int cols) {
        // Конструктор класса, принимающий параметры для инициализации состояния игры
        this.mineField = mineField; // Присвоение переданного массива mineField
        this.buttonStates = buttonStates; // Присвоение переданного массива buttonStates
        this.gameOver = gameOver; // Присвоение переданного значения gameOver
        this.remainingMines = remainingMines; // Присвоение переданного значения remainingMines
        this.flagsPlaced = flagsPlaced; // Присвоение переданного значения flagsPlaced
        this.cellsRevealed = cellsRevealed; // Присвоение переданного значения cellsRevealed
        this.secondsElapsed = secondsElapsed; // Присвоение переданного значения secondsElapsed
        this.rows = rows; // Присвоение переданного значения rows
        this.cols = cols; // Присвоение переданного значения cols
    }

    public boolean[][] getMineField() { // Метод для получения минного поля
        return mineField; // Возвращение массива mineField
    }

    public String[][] getButtonStates() { // Метод для получения состояния кнопок
        return buttonStates; // Возвращение массива buttonStates
    }

    public boolean isGameOver() { // Метод для проверки завершения игры
        return gameOver; // Возвращение значения gameOver
    }

    public int getRemainingMines() { // Метод для получения количества оставшихся мин
        return remainingMines; // Возвращение значения remainingMines
    }

    public int getFlagsPlaced() { // Метод для получения количества установленных флагов
        return flagsPlaced; // Возвращение значения flagsPlaced
    }

    public int getCellsRevealed() { // Метод для получения количества открытых ячеек
        return cellsRevealed; // Возвращение значения cellsRevealed
    }

    public int getSecondsElapsed() { // Метод для получения количества прошедших секунд
        return secondsElapsed; // Возвращение значения secondsElapsed
    }

    public int getRows() { // Метод для получения количества строк в игровом поле
        return rows; // Возвращение значения rows
    }

    public int getCols() { // Метод для получения количества столбцов в игровом поле
        return cols; // Возвращение значения cols
    }
}
