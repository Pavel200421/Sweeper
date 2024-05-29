package com.game.sweeper_2;

import java.io.*;

// Класс для сохранения и загрузки состояния игры
public class GamePersistence {


    public static void saveGame(GameState gameState, String filePath) throws IOException {  // Метод для сохранения игры в файл
        // Используется try-with-resources для автоматического закрытия потока вывода объектов
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(gameState); // Запись объекта состояния игры в файл
        }
    }


    public static GameState loadGame(String filePath) throws IOException, ClassNotFoundException {  // Метод для загрузки игры из файла
        // Используется try-with-resources для автоматического закрытия потока ввода объектов
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (GameState) ois.readObject(); // Возвращение загруженного объекта состояния игры
        }
    }
}
