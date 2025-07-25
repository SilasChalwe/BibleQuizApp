package com.nextinnomind.biblequizapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;

import java.util.*;
import java.util.stream.Collectors;

public class JsonUtils {

    private static final Logger logger = LogManager.getLogger(JsonUtils.class);
    private static final String RESOURCE_PATH = "/com/nextinnomind/biblequizapp/assets/data/questions.json";
    private static final String TEMP_FILE_PATH = "questions_temp.json";

    /**
     * Removes duplicate questions (case-insensitive) from internal JSON file in resources.
     * Writes back to a temp file (writable).
     */
    public static void removeDuplicateQuestions() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();

        // Load JSON from classpath
        URL resourceUrl = JsonUtils.class.getResource(RESOURCE_PATH);
        if (resourceUrl == null) {
            logger.error("File not found in resources: {}", RESOURCE_PATH);
            return;
        }

        List<Map<String, String>> questions;
        try (InputStream inputStream = JsonUtils.class.getResourceAsStream(RESOURCE_PATH);
             InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(inputStream))) {
            questions = gson.fromJson(reader, listType);
        } catch (Exception e) {
            logger.error("Failed to read JSON from resource: {}", e.getMessage());
            return;
        }

        if (questions == null || questions.isEmpty()) {
            logger.warn("No questions loaded from: {}", RESOURCE_PATH);
            return;
        }

        int originalCount = questions.size();

        // Remove duplicates
        Set<String> seen = new HashSet<>();
        List<Map<String, String>> uniqueQuestions = questions.stream()
                .filter(q -> {
                    String question = q.getOrDefault("question", "").trim().toLowerCase();
                    if (seen.contains(question)) return false;
                    seen.add(question);
                    return true;
                })
                .collect(Collectors.toList());

        int removed = originalCount - uniqueQuestions.size();

        // Save to temp file (writeable location)
        try (Writer writer = new FileWriter(TEMP_FILE_PATH)) {
            gson.toJson(uniqueQuestions, writer);
            logger.info("Saved updated questions to temp file: {}", TEMP_FILE_PATH);
        } catch (IOException e) {
            logger.error("Failed to write to temp file: {}", e.getMessage());
        }

        logger.info("Removed {} duplicate questions. Final count: {}", removed, uniqueQuestions.size());
    }
}
