package com.game.sweeper_2;

public class HighScore implements Comparable<HighScore> {
    private int time;

    public HighScore(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    @Override
    public int compareTo(HighScore other) {
        return Integer.compare(this.time, other.time);
    }

    @Override
    public String toString() {
        return "Time: " + time + "s";
    }
}
