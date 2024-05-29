package com.game.sweeper_2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Класс контроллера для правил игры
public class RulesController {
    private Stage stage; // Ссылка на объект окна

    // Аннотация FXML используется для инъекции элементов управления из файла FXML
    @FXML
    private Label LabelRule; // Элемент для отображения заголовка правил

    @FXML
    private Label LabelRules; // Элемент для отображения текста правил

    @FXML
    private VBox rulesFxml; // Главный контейнер окна с правилами


    public void setStage(Stage stage) {  // Метод для установки ссылки на объект окна
        this.stage = stage;
        Image ico = new Image("/Rules.png"); // Создание объекта изображения для иконки окна
        stage.getIcons().add(ico); // Установка иконки окна
    }


    @FXML
    private void applyRules() { // Метод, вызываемый при нажатии кнопки "Apply" для закрытия окна с правилами
        stage.close(); // Закрытие окна с правилами
    }


    public void initialize() {  // Метод для инициализации контроллера
        rulesFxml.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm()); // Добавление таблицы стилей для окна с правилами
    }

}
