package com.game.sweeper_2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Контроллер для окна статистики игры
public class StatisticsController {

    @FXML
    private Label gamesPlayedLabel; // Эти аннотации помечают поля, связанные с элементами интерфейса из FXML файла

    @FXML
    private Label gamesWonLabel;

    @FXML
    private Label gamesLostLabel;

    @FXML
    private Label bestTimeLabel;

    @FXML
    private VBox StatsBox; // Эта аннотация помечает контейнер, используемый для стилизации

    private Stage stage; // Экземпляр сцены, чтобы закрыть окно


    public void setStage(Stage stage) { // Метод для установки сцены
        this.stage = stage;
        this.stage.setMinWidth(350); // Установка минимальной ширины окна
        this.stage.setMinHeight(450); // Установка минимальной высоты окна
        Image ico = new Image("/Stats.png"); // Загрузка иконки окна
        stage.getIcons().add(ico); // Установка иконки окна
    }


    public void setStatistics(int gamesPlayed, int gamesWon, int gamesLost, int bestTime) { // Метод для установки статистики
        StatsBox.getStylesheets().add(getClass().getResource("/statistics.css").toExternalForm()); // Добавление стилей для контейнера
        gamesPlayedLabel.setText("Games Played: " + gamesPlayed); // Установка текста для метки количества сыгранных игр
        gamesWonLabel.setText("Games Won: " + gamesWon); // Установка текста для метки количества выигранных игр
        gamesLostLabel.setText("Games Lost: " + gamesLost); // Установка текста для метки количества проигранных игр
        bestTimeLabel.setText("Best Time: " + (bestTime == Integer.MAX_VALUE ? "N/A" : bestTime + "s")); // Установка текста для метки лучшего времени
    }


    @FXML
    private void handleClose() { // Метод для закрытия окна
        stage.close(); // Закрытие окна
    }
}
