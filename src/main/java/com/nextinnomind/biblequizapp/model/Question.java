package com.nextinnomind.biblequizapp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

        String uniqueId = UUID.randomUUID().toString();  // Generate random unique for each question                                  ,vvnv

        return new Question(uniqueId, question, choices, correctAnswer);
    }



}
