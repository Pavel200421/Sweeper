package com.game.sweeper_2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("Minesweeper");
        stage.setScene(scene);
        Image ico = new Image("/mineBest.png"); stage.getIcons().add(ico);
        stage.setMinWidth(567);
        stage.setMinHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
