package com.game.sweeper_2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Класс контроллера для настроек игры
public class SettingsController {

    // Аннотация FXML используется для инъекции элементов управления из файла FXML
    @FXML
    private TextField minesField; // Поле для указания количества мин

    @FXML
    private TextField rowsField; // Поле для указания количества строк

    @FXML
    private TextField colsField; // Поле для указания количества столбцов

    @FXML
    private VBox settingsControllerWindow; // Главный контейнер окна настроек

    private Stage stage; // Ссылка на объект окна


    public void setInitialValues(int mines, int rows, int cols) { // Метод для установки начальных значений настроек
        minesField.setText(String.valueOf(mines)); // Установка начального значения для количества мин
        rowsField.setText(String.valueOf(rows)); // Установка начального значения для количества строк
        colsField.setText(String.valueOf(cols)); // Установка начального значения для количества столбцов
    }


    public void setStage(Stage stage) {  // Метод для установки ссылки на объект окна
        this.stage = stage;
        this.stage.setMinWidth(350); // Установка минимальной ширины окна
        this.stage.setMinHeight(450); // Установка минимальной высоты окна
    }


    @FXML
    public void applySettings() {    // Метод, вызываемый при нажатии кнопки "Apply" для применения настроек
        try {
            // Получение значений из текстовых полей и преобразование их в целые числа
            int mines = Integer.parseInt(minesField.getText()); // Количество мин
            int rows = Integer.parseInt(rowsField.getText()); // Количество строк
            int cols = Integer.parseInt(colsField.getText()); // Количество столбцов

            // Проверка, что количество мин меньше общего количества ячеек на поле
            int totalCells = rows * cols;
            if (mines >= totalCells) {
                showAlert("Number of mines must be less than the total number of cells.");
                return;
            }

            // Проверка, что количество строк и столбцов в допустимых пределах
            if (rows <= 0 || rows >= 75 || cols <= 0 || cols >= 75) {
                showAlert("Number of rows and columns must be greater than zero and less than or equal to 75.");
                return;
            }

            // Применение настроек и закрытие окна
            HelloController.setSettings(mines, rows, cols);
            stage.close();
        } catch (NumberFormatException e) {
            // В случае ошибки при парсинге чисел показываем сообщение об ошибке
            showAlert("Please enter valid numbers.");
        }
    }


    public void initialize() {   // Метод для инициализации контроллера
        // Добавление таблицы стилей для окна настроек
        settingsControllerWindow.getStylesheets().add(getClass().getResource("/SettingsController.css").toExternalForm());
    }


    private void showAlert(String message) {   // Метод для показа диалогового окна с сообщением об ошибке
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Settings");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
