package com.game.sweeper_2;

import javafx.animation.Animation; // Импорт класса для работы с анимацией
import javafx.animation.KeyFrame; // Импорт класса для работы с ключевым кадром анимации
import javafx.animation.Timeline; // Импорт класса для работы с временной шкалой
import javafx.fxml.FXML; // Импорт аннотации для работы с FXML файлами
import javafx.fxml.FXMLLoader; // Импорт класса для загрузки FXML файлов
import javafx.geometry.Pos; // Импорт класса для работы с позиционированием
import javafx.scene.Cursor; // Импорт класса для работы с курсором
import javafx.scene.Parent; // Импорт класса для работы с родительским элементом
import javafx.scene.Scene; // Импорт класса для создания сцены
import javafx.scene.control.*; // Импорт классов для работы с элементами управления
import javafx.scene.control.Alert.AlertType; // Импорт класса для работы с типами сообщений
import javafx.scene.image.Image; // Импорт класса для работы с изображениями
import javafx.scene.input.MouseButton; // Импорт класса для работы с кнопками мыши
import javafx.scene.input.MouseEvent; // Импорт класса для работы с событиями мыши
import javafx.scene.layout.GridPane; // Импорт класса для работы с сеткой
import javafx.scene.media.Media; // Импорт класса для работы с медиафайлами
import javafx.scene.media.MediaPlayer; // Импорт класса для проигрывания медиафайлов
import javafx.stage.Modality; // Импорт класса для работы с модальными окнами
import javafx.stage.Stage; // Импорт класса для работы с окнами приложения
import javafx.util.Duration; // Импорт класса для работы с длительностью времени
import javafx.stage.FileChooser; // Импорт класса для работы с диалогом выбора файла

import java.io.File; // Импорт класса для работы с файлами
import java.io.IOException; // Импорт класса для работы с исключениями ввода-вывода
import java.util.ArrayList; // Импорт класса для работы с массивами
import java.util.Collections; // Импорт класса для работы с коллекциями
import java.util.List; // Импорт класса для работы с списками
import java.util.Random; // Импорт класса для работы с генерацией случайных чисел

public class HelloController { // Объявление класса контроллера

    @FXML // Аннотация указывает, что это поле должно быть загружено из FXML файла
    private GridPane mainPane; // Панель сетки игрового поля

    @FXML
    private Button NewGameBtn; // Кнопка для начала новой игры

    @FXML
    private Button ResetBtn; // Кнопка для сброса игры

    @FXML
    private Label MineCounter, ResetText; // Метки для отображения количества мин и текста "Reset"

    @FXML
    private MenuBar menuBar; // Панель меню

    @FXML
    private MenuItem SaveGame, LoadGame;

    @FXML
    private Button settingsPref; // Кнопка для настройки предпочтений

    @FXML
    private ScrollPane scrollPane; // Панель прокрутки

    @FXML
    private Label timerLabel; // Метка для отображения времени игры

    private static final int ROWS = 10; // Количество строк в игровом поле по умолчанию
    private static final int COLS = 10; // Количество столбцов в игровом поле по умолчанию
    private static final int MINES = 20; // Количество мин по умолчанию

    private Button[][] buttons; // Массив кнопок игрового поля
    private boolean[][] mineField; // Массив для хранения информации о минном поле
    private boolean gameOver; // Флаг, указывающий на завершение игры
    private int remainingMines; // Количество оставшихся мин
    private int flagsPlaced; // Количество установленных флагов
    private int cellsRevealed; // Количество открытых ячеек

    private static int customMines = MINES; // Пользовательские настройки количества мин
    private static int customRows = ROWS; // Пользовательские настройки количества строк
    private static int customCols = COLS; // Пользовательские настройки количества столбцов

    private Timeline timeline; // Временная шкала для отсчета времени игры
    private int secondsElapsed; // Прошедшее время в секундах

    private MediaPlayer mediaPlayer; // Объект для воспроизведения звуковых эффектов

    private int gamesPlayed = 0; // Количество сыгранных игр
    private int gamesWon = 0; // Количество выигранных игр
    private int gamesLost = 0; // Количество проигранных игр
    private int bestTime = Integer.MAX_VALUE; // Лучшее время прохождения

    private List<HighScore> highScores = new ArrayList<>(); // Список рекордов

    @FXML
    public void handleNewGame() { // Метод для начала новой игры
        startNewGame(); // Вызов метода для начала новой игры
        startTimer(); // Вызов метода для запуска таймера
        SaveGame.setDisable(false);
        LoadGame.setDisable(false);
        NewGameBtn.setVisible(false); // Скрытие кнопки NewGameBtn
        ResetText.setVisible(true); // Отображение метки ResetText
        ResetBtn.setVisible(true); // Отображение кнопки ResetBtn
        MineCounter.setVisible(true); // Отображение метки MineCounter
        ResetBtn.setDisable(false); // Активация кнопки ResetBtn
        menuBar.setVisible(true); // Отображение панели меню
        settingsPref.setVisible(false); // Скрытие кнопки settingsPref
        scrollPane.setVisible(true);// Отображение scrollPane
    }
    private void playSoundEffectBlip() { // Метод для воспроизведения звукового эффекта "blip"
        String path = getClass().getResource("/blip1.mp3").toString(); // Получение пути к звуковому файлу
        Media media = new Media(path); // Создание объекта медиа с указанным путем

        mediaPlayer = new MediaPlayer(media); // Создание объекта проигрывателя медиафайлов
        mediaPlayer.play(); // Воспроизведение звукового эффекта
    }

    @FXML // Аннотация указывает, что это метод, который будет вызван при нажатии на кнопку "Reset"
    public void handleReset() { // Метод для обработки сброса игры
        stopTimer(); // Вызов метода для остановки таймера
        playSoundEffectBlip(); // Вызов метода для воспроизведения звукового эффекта "blip"
        secondsElapsed = 0; // Сброс счетчика секунд
        timerLabel.setText("Time: 0s"); // Обновление метки времени на значение "0s"
        startNewGame(); // Вызов метода для начала новой игры
        startTimer(); // Вызов метода для запуска таймера
    }

    public void startNewGame() { // Метод для начала новой игры
        mainPane.getChildren().clear(); // Очистка дочерних элементов основной панели
        buttons = new Button[customRows][customCols]; // Создание нового массива кнопок
        mineField = generateMineField(customRows, customCols, customMines); // Генерация нового минного поля
        gameOver = false; // Сброс флага завершения игры
        remainingMines = customMines; // Установка начального количества оставшихся мин
        flagsPlaced = 0; // Сброс количества установленных флагов
        cellsRevealed = 0; // Сброс количества открытых ячеек
        updateMineCounter(); // Вызов метода для обновления счетчика мин

        double buttonSize = 50; // Размер кнопки игрового поля

        mainPane.setAlignment(Pos.CENTER); // Установка выравнивания элементов в центр панели

        for (int row = 0; row < customRows; row++) { // Перебор строк игрового поля
            for (int col = 0; col < customCols; col++) { // Перебор столбцов игрового поля
                Button button = new Button(); // Создание новой кнопки
                button.getStyleClass().add("untouched-cell"); // Добавление класса стиля для неактивированной ячейки
                button.setPrefWidth(buttonSize); // Установка предпочтительной ширины кнопки
                button.setPrefHeight(buttonSize); // Установка предпочтительной высоты кнопки
                button.setMaxWidth(buttonSize); // Установка максимальной ширины кнопки
                button.setMaxHeight(buttonSize); // Установка максимальной высоты кнопки
                button.setMinWidth(buttonSize); // Установка минимальной ширины кнопки
                button.setMinHeight(buttonSize); // Установка минимальной высоты кнопки

                int r = row; // Сохранение значения текущей строки
                int c = col; // Сохранение значения текущего столбца

                button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> { // Добавление обработчика событий при клике на кнопку
                    if (event.getButton() == MouseButton.PRIMARY) { // Если клик был левой кнопкой мыши
                        handlePrimaryClick(button, r, c); // Вызов метода для обработки левого клика
                    } else if (event.getButton() == MouseButton.SECONDARY) { // Если клик был правой кнопкой мыши
                        handleSecondaryClick(button); // Вызов метода для обработки правого клика
                    }
                });

                buttons[row][col] = button; // Добавление кнопки в массив кнопок
                mainPane.add(button, col, row); // Добавление кнопки на основную панель по указанным координатам
            }
        }
    }


    @FXML
    public void handleSettingsButtonClick() { // Установка модального окна SettingsController
        try {
            // Создание загрузчика FXML для окна настроек
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settingsController.fxml"));
            // Загрузка родительского элемента из FXML файла
            Parent parent = fxmlLoader.load();

            // Получение контроллера для окна настроек
            SettingsController settingsController = fxmlLoader.getController();
            // Установка начальных значений настроек из текущих значений
            settingsController.setInitialValues(customMines, customRows, customCols);

            // Создание нового окна для настроек
            Stage settingsStage = new Stage();
            // Установка иконки окна
            Image ico = new Image("/settings.png"); settingsStage.getIcons().add(ico);
            // Установка модальности окна
            settingsStage.initModality(Modality.APPLICATION_MODAL);
            // Установка заголовка окна
            settingsStage.setTitle("Settings");

            // Установка сцены окна настроек
            settingsStage.setScene(new Scene(parent));

            // Установка основного окна в качестве родительского для окна настроек
            settingsController.setStage((Stage) settingsStage.getScene().getWindow());

            // Отображение окна настроек и ожидание его закрытия
            settingsStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
        public void handleRulesButtonClick() { // Установка модального окна RulesController
        try {
            // Создание загрузчика FXML для окна правил игры
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("rulesController.fxml"));
            // Загрузка родительского элемента из FXML файла
            Parent parent = fxmlLoader.load();

            // Получение контроллера для окна правил игры
            RulesController rulesController = fxmlLoader.getController();
            // Создание нового окна для правил игры
            Stage rulesStage = new Stage();
            // Установка модальности окна
            rulesStage.initModality(Modality.APPLICATION_MODAL);
            // Установка заголовка окна
            rulesStage.setTitle("Rules");
            // Установка сцены окна правил игры
            rulesStage.setScene(new Scene(parent));
            // Установка основного окна в качестве родительского для окна правил игры
            rulesController.setStage((Stage) rulesStage.getScene().getWindow());
            // Установка минимальных размеров окна
            rulesStage.setMinWidth(400);
            rulesStage.setMinHeight(500);
            // Отображение окна правил игры и ожидание его закрытия
            rulesStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void setSettings(int mines, int rows, int cols) { // Метод для установки пользовательских настроек игры
        // Проверка на допустимость введенных значений настроек
        if (mines > 0 && rows > 0 && cols > 0 && rows <= 75 && cols <= 75) {
            // Установка новых значений настроек
            customMines = mines;
            customRows = rows;
            customCols = cols;
        }
    }


    private void handleSecondaryClick(Button button) { // Метод для обработки клика на ячейку правой кнопкой мыши
        // Проверка, что кнопка не отключена
        if (!button.isDisabled()) {
            // Проверка, установлен ли флаг на ячейке
            Boolean isFlagSet = (Boolean) button.getUserData();

            // Если флаг не установлен
            if (isFlagSet == null || !isFlagSet) {
                // Проверка, что количество установленных флагов не превышает допустимого
                if (flagsPlaced < customMines) {
                    // Установка флага на ячейке
                    button.setText("F");
                    // Добавление класса для стилизации ячейки с флагом
                    button.getStyleClass().add("flag-cell");
                    // Установка флага в качестве пользовательских данных кнопки
                    button.setUserData(true);
                    // Уменьшение количества оставшихся мин
                    flagsPlaced++;
                    remainingMines--;
                    // Обновление счетчика мин
                    updateMineCounter();
                } else {
                    // Вывод предупреждения при попытке установить слишком много флагов
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Too Many Flags");
                    alert.setHeaderText(null);
                    alert.setContentText("You have placed the maximum number of flags.");
                    alert.showAndWait();
                }
            } else {
                // Если флаг уже установлен, его снятие
                button.setText("");
                // Удаление класса стиля для ячейки с флагом
                button.getStyleClass().remove("flag-cell");
                // Установка значения false в качестве пользовательских данных кнопки
                button.setUserData(false);
                // Увеличение количества оставшихся мин
                flagsPlaced--;
                remainingMines++;
                // Обновление счетчика мин
                updateMineCounter();
            }
        }
    }


    private void handlePrimaryClick(Button button, int row, int col) {  // Метод для обработки клика на ячейку левой кнопкой мыши
        // Проверка, что игра не завершена и ячейка не открыта
        if (!gameOver && button.getText().isEmpty()) {
            // Удаление класса стиля для неоткрытой ячейки
            button.getStyleClass().remove("untouched-cell");
            // Если на ячейке мина
            if (mineField[row][col]) {
                // Добавление класса стиля для ячейки с миной
                button.getStyleClass().add("mine-cell");
                // Отключение кнопки
                button.setDisable(true);
                // Установка флага окончания игры
                gameOver = true;
                // Вывод сообщения о проигрыше
                showGameOver();
            } else {
                // Подсчет числа смежных мин
                int adjacentMines = countAdjacentMines(mineField, row, col);
                // Если смежных мин нет
                if (adjacentMines == 0) {
                    // Открытие пустых смежных ячеек
                    revealEmptyCells(row, col);
                } else {
                    // Вывод числа смежных мин на кнопке
                    button.setText(String.valueOf(adjacentMines));
                    // Добавление класса стиля для безопасной ячейки
                    button.getStyleClass().add("safe-cell");
                    // Добавление класса стиля для числа на кнопке
                    button.getStyleClass().add("number-" + adjacentMines);
                    // Отключение кнопки
                    button.setDisable(true);
                    // Увеличение количества открытых ячеек
                    cellsRevealed++;
                    // Проверка условия победы
                    checkWinCondition();
                }
            }
        }
    }


    private void updateMineCounter() {   // Метод для обновления счетчика оставшихся мин
        // Обновление текста счетчика оставшихся мин
        MineCounter.setText("Mines: " + remainingMines);
    }


    private void playSoundEffectWarning() {  // Метод для воспроизведения звукового эффекта предупреждения
        // Указание пути к аудиофайлу в ресурсах
        String path = getClass().getResource("/warning.mp3").toString();
        // Создание объекта Media
        Media media = new Media(path);

        // Создание нового экземпляра MediaPlayer
        mediaPlayer = new MediaPlayer(media);

        // Воспроизведение звука
        mediaPlayer.play();
    }
    private void showGameOver() { // Метод для отображения сообщения о проигрыше
        // Остановка таймера
        stopTimer();
        // Воспроизведение звукового эффекта предупреждения
        playSoundEffectWarning();
        // Увеличение счетчика сыгранных игр и проигранных игр
        gamesPlayed++;
        gamesLost++;
        // Вывод информационного сообщения о проигрыше
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("You clicked on a mine! Game over.");
        alert.showAndWait();
        // Открытие всех мин
        revealAllMines();
    }


    private void revealAllMines() {  // Метод для открытия всех мин после проигрыша
        for (int row = 0; row < customRows; row++) {
            for (int col = 0; col < customCols; col++) {
                if (mineField[row][col]) {
                    Button button = buttons[row][col];
                    // Удаление класса стиля для неоткрытой ячейки
                    button.getStyleClass().remove("untouched-cell");
                    // Добавление класса стиля для ячейки с миной
                    button.getStyleClass().add("mine-cell");
                    // Отключение кнопки
                    button.setDisable(true);
                }
            }
        }
    }


    private void checkWinCondition() { // Метод для проверки условия победы
        // Если все безопасные ячейки открыты
        if (cellsRevealed == customRows * customCols - customMines) {
            // Остановка таймера
            stopTimer();
            // Установка флага завершения игры
            gameOver = true;
            // Увеличение счетчика сыгранных игр и выигранных игр
            gamesPlayed++;
            gamesWon++;
            // Если текущее время лучше лучшего времени
            if (secondsElapsed < bestTime) {
                // Обновление лучшего времени и списка рекордов
                bestTime = secondsElapsed;
                highScores.add(new HighScore(secondsElapsed));
                Collections.sort(highScores);
            }
            // Вывод информационного сообщения о победе
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You Win!");
            alert.setHeaderText(null);
            alert.setContentText("Congratulations! You've cleared all the mines.");
            alert.showAndWait();
        }
    }


    private boolean[][] generateMineField(int rows, int cols, int mines) { // Метод для генерации поля мин
        // Создание двумерного массива для поля мин
        boolean[][] field = new boolean[rows][cols];
        // Создание объекта Random
        Random random = new Random();
        int placedMines = 0;

        // Пока количество размещенных мин меньше необходимого
        while (placedMines < mines) {
            // Генерация случайных координат
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            // Если в данной ячейке нет мины
            if (!field[row][col]) {
                // Размещение мины в ячейке
                field[row][col] = true;
                // Увеличение количества размещенных мин
                placedMines++;
            }
        }

        return field;
    }
    // Метод для подсчета числа смежных мин
    private int countAdjacentMines(boolean[][] field, int row, int col) {   // Метод для подсчета числа смежных мин
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                // Проверка, что новые координаты находятся в пределах поля
                if (newRow >= 0 && newRow < field.length && newCol >= 0 && newCol < field[0].length && field[newRow][newCol]) {
                    count++;
                }
            }
        }
        return count;
    }


    private void revealEmptyCells(int row, int col) {  // Метод для открытия всех смежных пустых ячеек
        // Проверка выхода за пределы поля или отключения ячейки
        if (row < 0 || row >= customRows || col < 0 || col >= customCols || buttons[row][col].isDisabled()) {
            return;
        }

        // Подсчет смежных мин
        int adjacentMines = countAdjacentMines(mineField, row, col);
        // Отключение кнопки
        buttons[row][col].setDisable(true);
        // Удаление класса стиля для неоткрытой ячейки
        buttons[row][col].getStyleClass().remove("untouched-cell");

        // Если в ячейке нет смежных мин
        if (adjacentMines == 0) {
            // Добавление класса стиля для безопасной ячейки
            buttons[row][col].getStyleClass().add("safe-cell");
            // Увеличение количества открытых ячеек
            cellsRevealed++;
            // Проверка условия победы
            checkWinCondition();

            // Перебор смежных ячеек
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        // Рекурсивный вызов для открытия смежных пустых ячеек
                        revealEmptyCells(row + i, col + j);
                    }
                }
            }
        } else {
            // Вывод числа смежных мин на кнопке
            buttons[row][col].setText(String.valueOf(adjacentMines));
            // Добавление класса стиля для безопасной ячейки
            buttons[row][col].getStyleClass().add("safe-cell");
            // Добавление класса стиля для числа на кнопке
            buttons[row][col].getStyleClass().add("number-" + adjacentMines);
            // Увеличение количества открытых ячеек
            cellsRevealed++;
            // Проверка условия победы
            checkWinCondition();
        }
    }

    @FXML
    public void handleShowStatistics() { //Метод для вывода модального окна со статистикой
        try {
            // Создание загрузчика FXML для окна статистики игры
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("statistics.fxml"));
            // Загрузка родительского элемента из FXML файла
            Parent parent = fxmlLoader.load();

            // Получение контроллера для окна статистики игры
            StatisticsController statisticsController = fxmlLoader.getController();
            // Установка статистики в контроллер
            statisticsController.setStatistics(gamesPlayed, gamesWon, gamesLost, bestTime);

            // Создание нового окна для статистики игры
            Stage statisticsStage = new Stage();
            // Установка заголовка окна
            statisticsStage.setTitle("Game Statistics");
            // Установка сцены окна статистики игры
            statisticsStage.setScene(new Scene(parent));
            // Установка основного окна в качестве родительского для окна статистики игры
            statisticsController.setStage((Stage) statisticsStage.getScene().getWindow());
            // Отображение окна статистики игры и ожидание его закрытия
            statisticsStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() { // Метод для подключения стилей и скрытие/открытия элементов интерфейса
        // Добавление стилей из внешнего CSS файла
        mainPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        // Добавление класса стиля для кнопки сброса игры
        ResetBtn.getStyleClass().add("ResetButton");
        // Установка курсора при наведении на кнопку сброса
        ResetBtn.setOnMouseEntered(event -> ResetBtn.setCursor(Cursor.HAND));
        // Возвращение стандартного курсора при уходе мыши с кнопки сброса
        ResetBtn.setOnMouseExited(event -> ResetBtn.setCursor(Cursor.DEFAULT));
        // Изначально скрытие кнопки "Новая игра"
        NewGameBtn.setVisible(true);
        // Изначально скрытие кнопки сброса
        ResetBtn.setVisible(false);
        // Изначально скрытие счетчика мин
        MineCounter.setVisible(false);
        // Отображение меню
        menuBar.setVisible(true);
        // Отображение настроек
        settingsPref.setVisible(true);
        // Скрытие поля игры
        scrollPane.setVisible(false);
        // Скрытие таймера
        timerLabel.setVisible(false);
    }


    private void startTimer() { // Метод для запуска таймера
        // Создание таймлайна с обновлением каждую секунду
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            // Увеличение счетчика секунд
            secondsElapsed++;
            // Обновление текста таймера
            timerLabel.setText("Time: " + secondsElapsed + "s");
        }));
        // Установка бесконечного цикла анимации таймлайна
        timeline.setCycleCount(Animation.INDEFINITE);
        // Запуск таймлайна
        timeline.play();
        // Отображение таймера
        timerLabel.setVisible(true);
    }
        private void stopTimer() { // Метод для остановки таймера
        // Проверка, что таймлайн существует
        if (timeline != null) {
            // Остановка таймлайна
            timeline.stop();
        }
    }


    @FXML
    public void handleSaveGame() {     // Метод для обработки сохранения игры
        // Создание диалога для выбора файла
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        // Установка фильтра для файлов сохранения игры
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Minesweeper Save Files", "*.mswp"));
        // Отображение диалога сохранения и получение выбранного файла
        File file = fileChooser.showSaveDialog(mainPane.getScene().getWindow());

        // Проверка, что файл выбран
        if (file != null) {
            try {
                // Создание объекта состояния игры для сохранения
                GameState gameState = new GameState(mineField, getButtonStates(), gameOver, remainingMines, flagsPlaced, cellsRevealed, secondsElapsed, customRows, customCols);
                // Сохранение игры
                GamePersistence.saveGame(gameState, file.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    public void handleLoadGame() {  // Метод для обработки загрузки игры
        // Создание диалога для выбора файла
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");
        // Установка фильтра для файлов сохранения игры
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Minesweeper Save Files", "*.mswp"));
        // Отображение диалога загрузки и получение выбранного файла
        File file = fileChooser.showOpenDialog(mainPane.getScene().getWindow());

        // Проверка, что файл выбран
        if (file != null) {
            try {
                // Загрузка состояния игры из файла
                GameState gameState = GamePersistence.loadGame(file.getPath());
                // Проверка соответствия размера сохраненной игры текущим настройкам
                if (gameState.getRows() != customRows || gameState.getCols() != customCols) {
                    // Вывод предупреждения, если размер сохраненной игры не соответствует текущим настройкам
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Load Game Error");
                    alert.setHeaderText(null);
                    alert.setContentText("The saved game size does not match the current settings. Please adjust the settings to match the saved game size.");
                    alert.showAndWait();
                } else {
                    // Загрузка состояния игры
                    loadGameState(gameState);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private String[][] getButtonStates() { // Метод для получения состояния кнопок
        // Создание двумерного массива для хранения состояний кнопок
        String[][] buttonStates = new String[customRows][customCols];
        // Перебор всех кнопок
        for (int row = 0; row < customRows; row++) {
            for (int col = 0; col < customCols; col++) {
                Button button = buttons[row][col];
                // Проверка состояния кнопки и добавление соответствующего значения в массив
                if (button.getStyleClass().contains("flag-cell")) {
                    buttonStates[row][col] = "F";
                } else if (button.getStyleClass().contains("mine-cell")) {
                    buttonStates[row][col] = "M";
                } else if (button.getStyleClass().contains("safe-cell")) {
                    buttonStates[row][col] = button.getText();
                } else {
                    buttonStates[row][col] = "";
                }
            }
        }
        return buttonStates;
    }


    private void loadGameState(GameState gameState) { // Метод для загрузки состояния игры
        // Остановка таймера
        stopTimer();
        // Установка поля мин
        mineField = gameState.getMineField();
        // Установка состояния завершения игры
        gameOver = gameState.isGameOver();
        // Установка оставшихся мин
        remainingMines = gameState.getRemainingMines();
        // Установка количества установленных флагов
        flagsPlaced = gameState.getFlagsPlaced();
        // Установка количества открытых ячеек
        cellsRevealed = gameState.getCellsRevealed();
        // Установка прошедшего времени
        secondsElapsed = gameState.getSecondsElapsed();
        // Обновление счетчика оставшихся мин
        updateMineCounter();
        // Установка текста таймера
        timerLabel.setText("Time: " + secondsElapsed + "s");

        // Получение состояний кнопок из сохраненной игры
        String[][] buttonStates = gameState.getButtonStates();
        // Перебор всех кнопок
        for (int row = 0; row < customRows; row++) {
            for (int col = 0; col < customCols; col++) {
                Button button = buttons[row][col];
                String state = buttonStates[row][col];
                // Установка текста кнопки в соответствии с сохраненным состоянием
                button.setText(state.equals("F") ? "" : state);

                // Установка доступности кнопки в зависимости от сохраненного состояния
                boolean isClickable = state.isEmpty() || state.equals("F");
                button.setDisable(!isClickable);

                // Удаление всех классов стилей из кнопки
                button.getStyleClass().removeAll("untouched-cell", "safe-cell", "mine-cell", "flag-cell", "number-1", "number-2", "number-3", "number-4", "number-5", "number-6", "number-7", "number-8");

                // Установка соответствующего класса стиля для кнопки в зависимости от сохраненного состояния
                if (state.equals("F")) {
                    button.getStyleClass().add("flag-cell");
                    button.setUserData(true);
                } else if (state.equals("M")) {
                    button.getStyleClass().add("mine-cell");
                } else if (state.matches("\\d")) {
                    button.getStyleClass().add("safe-cell");
                    button.getStyleClass().add("number-" + state);
                } else {
                    button.getStyleClass().add("untouched-cell");
                }
            }
        }

        // Если игра не завершена, запуск таймера
        if (!gameOver) {
            startTimer();
        }
    }
}
