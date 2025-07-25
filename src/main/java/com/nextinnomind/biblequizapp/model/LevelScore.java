package com.nextinnomind.biblequizapp.model;

public class LevelScore {

    private int level;
    private double score;
    private int correctAnswers;
    private int totalQuestions;
    private String timestamp;

    public LevelScore() {
    }

    public LevelScore(int level, double score, int correctAnswers, int totalQuestions, String timestamp) {
        this.level = level;
        this.score = score;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.timestamp = timestamp;
    }

    // Getters
    public int getLevel() {
        return level;
    }

    public double getScore() {
        return score;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setLevel(int level) {
        this.level = level;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "LevelScore{" +
                "level=" + level +
                ", score=" + score +
                ", correctAnswers=" + correctAnswers +
                ", totalQuestions=" + totalQuestions +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
