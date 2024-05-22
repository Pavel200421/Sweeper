package com.game.sweeper_2;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RulesController {
    private Stage stage;
    @FXML
    private Label LabelRule, LabelRules;
    @FXML
    private VBox rulesFxml;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    private void applyRules(){
        stage.close();
    }
    public void initialize() {
        rulesFxml.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

}
