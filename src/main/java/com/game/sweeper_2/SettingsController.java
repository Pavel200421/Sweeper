    package com.game.sweeper_2;

    import javafx.fxml.FXML;
    import javafx.scene.Scene;
    import javafx.scene.control.Alert;
    import javafx.scene.control.CheckBox;
    import javafx.scene.control.TextField;
    import javafx.scene.layout.VBox;
    import javafx.stage.Stage;

    public class SettingsController {

        @FXML
        private TextField minesField;

        @FXML
        private TextField rowsField;

        @FXML
        private TextField colsField;

        @FXML
        private VBox settingsControllerWindow;
        private Stage stage;


        // Method to set the initial values of the text fields
        public void setInitialValues(int mines, int rows, int cols) {
            minesField.setText(String.valueOf(mines));
            rowsField.setText(String.valueOf(rows));
            colsField.setText(String.valueOf(cols));
        }

        public void setStage(Stage stage) {
            this.stage = stage;
            this.stage.setMinWidth(350);
            this.stage.setMinHeight(450);

        }
        @FXML
        public void applySettings() {
            try {
                // Получаем значения из текстовых полей
                int mines = Integer.parseInt(minesField.getText());
                int rows = Integer.parseInt(rowsField.getText());
                int cols = Integer.parseInt(colsField.getText());

                // Проверяем, чтобы количество мин было меньше, чем общее количество ячеек на поле
                int totalCells = rows * cols;
                if (mines >= totalCells) {
                    showAlert("Number of mines must be less than the total number of cells.");
                    return;
                }

                // Проверяем, чтобы ширина и высота поля были больше нуля и меньше или равны 75
                if (rows <= 0 || rows >= 75 || cols <= 0 || cols >= 75) {
                    showAlert("Number of rows and columns must be greater than zero and less than or equal to 75.");
                    return;
                }

                // Применяем настройки
                HelloController.setSettings(mines, rows, cols);

                stage.close();
            } catch (NumberFormatException e) {
                showAlert("Please enter valid numbers.");
            }

        }

        public void initialize() {
            settingsControllerWindow.getStylesheets().add(getClass().getResource("/SettingsController.css").toExternalForm());

        }
        private void showAlert(String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Settings");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }



    }
