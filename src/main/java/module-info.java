module com.game.sweeper_2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.game.sweeper_2 to javafx.fxml;
    exports com.game.sweeper_2;
}