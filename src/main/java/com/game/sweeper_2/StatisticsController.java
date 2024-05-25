package com.game.sweeper_2;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class StatisticsController {

    @FXML
    private Label gamesPlayedLabel;

    @FXML
    private Label gamesWonLabel;

    @FXML
    private Label gamesLostLabel;

    @FXML
    private Label bestTimeLabel;

    @FXML
    private VBox StatsBox;



    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setMinWidth(350);
        this.stage.setMinHeight(450);
        Image ico = new Image("/Stats.png"); stage.getIcons().add(ico);
    }

    public void setStatistics(int gamesPlayed, int gamesWon, int gamesLost, int bestTime /*, List<HighScore> highScores */) {
        StatsBox.getStylesheets().add(getClass().getResource("/statistics.css").toExternalForm());
        gamesPlayedLabel.setText("Games Played: " + gamesPlayed);
        gamesWonLabel.setText("Games Won: " + gamesWon);
        gamesLostLabel.setText("Games Lost: " + gamesLost);
        bestTimeLabel.setText("Best Time: " + (bestTime == Integer.MAX_VALUE ? "N/A" : bestTime + "s"));
    }

    @FXML
    private void handleClose() {
        stage.close();
    }
}
