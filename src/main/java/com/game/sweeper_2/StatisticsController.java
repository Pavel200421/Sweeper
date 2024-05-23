package com.game.sweeper_2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

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
    private VBox highScoresVBox;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setStatistics(int gamesPlayed, int gamesWon, int gamesLost, int bestTime, List<HighScore> highScores) {
        gamesPlayedLabel.setText("Games Played: " + gamesPlayed);
        gamesWonLabel.setText("Games Won: " + gamesWon);
        gamesLostLabel.setText("Games Lost: " + gamesLost);
        bestTimeLabel.setText("Best Time: " + (bestTime == Integer.MAX_VALUE ? "N/A" : bestTime + "s"));

        highScoresVBox.getChildren().clear();
        for (HighScore score : highScores) {
            Label label = new Label(score.toString());
            highScoresVBox.getChildren().add(label);
        }
    }

    @FXML
    private void handleClose() {
        stage.close();
    }
}
