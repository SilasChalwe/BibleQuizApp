package com.nextinnomind.biblequizapp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Question {

    private String id;
    private String question;
    private Map<String, String> choices;
    private String correctAnswer;

    public Question() {
        // needed for Gson deserialization
    }

    public Question(String id, String question, Map<String, String> choices, String correctAnswer) {
        this.id = id;
        this.question = question;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    // Static helper to construct from JSON-style map and generate a unique id
    public static Question fromFlatJson(String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        Map<String, String> choices = new HashMap<>();
        choices.put("option_a", optionA);
        choices.put("option_b", optionB);
        choices.put("option_c", optionC);
        choices.put("option_d", optionD);

        String uniqueId = UUID.randomUUID().toString();  // Generate random unique ID

        return new Question(uniqueId, question, choices, correctAnswer);
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, String> getChoices() {
        return choices;
    }

    public void setChoices(Map<String, String> choices) {
        this.choices = choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }


}
