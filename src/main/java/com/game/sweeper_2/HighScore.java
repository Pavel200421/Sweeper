package com.game.sweeper_2;

// Класс, представляющий высокий результат
public class HighScore implements Comparable<HighScore> {
    private int time; // Время, за которое был установлен высокий результат


    public HighScore(int time) { // Конструктор, инициализирующий время
        this.time = time;
    }


    public int getTime() {   // Метод для получения времени высокого результата
        return time;
    }


    @Override
    public int compareTo(HighScore other) {  // Метод для сравнения двух высоких результатов по времени
        return Integer.compare(this.time, other.time); // Сравнение времени текущего и другого высокого результата
    }


    @Override
    public String toString() {  // Метод для представления высокого результата в виде строки
        return "Time: " + time + "s"; // Возвращает строку с временем высокого результата
    }
}

