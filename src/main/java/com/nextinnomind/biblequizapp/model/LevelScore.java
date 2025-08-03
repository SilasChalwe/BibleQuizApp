package com.nextinnomind.biblequizapp.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelScore {

    // Setters
    // Getters
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
