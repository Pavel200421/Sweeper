package com.game.sweeper_2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader; // Импорт класса для загрузки FXML файлов
import javafx.scene.Scene; // Импорт класса для создания сцены
import javafx.scene.image.Image; // Импорт класса для работы с изображениями
import javafx.stage.Stage; // Импорт класса для работы с окном приложения

import java.io.IOException; // Импорт класса для работы с исключениями ввода-вывода

public class HelloApplication extends Application { // Объявление класса приложения

    @Override
    public void start(Stage stage) throws IOException { // Переопределение метода start, который запускает приложение
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml")); // Создание объекта FXMLLoader для загрузки FXML файла
        Scene scene = new Scene(fxmlLoader.load()); // Создание сцены, загруженной из FXML файла
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm()); // Добавление стилей к сцене
        stage.setTitle("Minesweeper"); // Установка заголовка окна
        stage.setScene(scene); // Установка сцены на окно
        Image ico = new Image("/mineBest.png"); // Создание объекта изображения для иконки окна
        stage.getIcons().add(ico); // Установка иконки окна
        stage.setMinWidth(567); // Установка минимальной ширины окна
        stage.setMinHeight(650); // Установка минимальной высоты окна
        stage.show(); // Отображение окна
    }

    public static void main(String[] args) { // Метод main для запуска приложения
        launch(); // Запуск JavaFX приложения
    }
}
