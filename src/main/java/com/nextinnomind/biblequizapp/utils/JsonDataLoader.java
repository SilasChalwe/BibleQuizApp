package com.nextinnomind.biblequizapp.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nextinnomind.biblequizapp.model.LevelScore;
import com.nextinnomind.biblequizapp.model.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;

public class JsonDataLoader {

    private static final Logger logger = LogManager.getLogger(JsonDataLoader.class);


    private static JsonDataLoader instance;
    private final List<Question> questions = new ArrayList<>();
    private List<Question> selectedQuestions = new ArrayList<>();

    private static final Map<Integer, LevelScore> scoresByLevel = new HashMap<>();

    private static final Gson gson = new Gson();
    public static final String ROOT_FILE_PATH = "/com/nextinnomind/biblequizapp/assets/data/";
    public static final String QUESTION_FILE_NAME  = "questions.json";
    private static final Path scoreFilePath = Path.of("src/main/resources/"+ROOT_FILE_PATH+"scores.json");
    // Keep track of questions selected per level to avoid repetition
    private final Map<Integer, Set<Question>> previouslySelectedQuestionsByLevel = new HashMap<>();

    private JsonDataLoader() {
        logger.info("Initializing JsonDataLoader...");
        loadQuestions();
        loadScores();
        logger.info("JsonDataLoader initialized successfully.");
    }

    public static JsonDataLoader getInstance() {
        if (instance == null) {
            instance = new JsonDataLoader();
        }
        return instance;
    }

    public int getStarsForLevel(int level) {
        LevelScore score = scoresByLevel.get(level);
        if (score == null) return 0;
        return calculateStars(score.getScore(), score.getTotalQuestions());
    }

    private void loadQuestions() {
        try (InputStream is = Objects.requireNonNull(getClass().getResourceAsStream((ROOT_FILE_PATH+QUESTION_FILE_NAME)));
             InputStreamReader reader = new InputStreamReader(is)) {

            Type rawListType = new TypeToken<List<Map<String, String>>>() {}.getType();
            List<Map<String, String>> rawList = gson.fromJson(reader, rawListType);

            for (Map<String, String> raw : rawList) {
                Question q = Question.fromFlatJson(
                        raw.get("question"),
                        raw.get("option_a"),
                        raw.get("option_b"),
                        raw.get("option_c"),
                        raw.get("option_d"),
                        raw.get("answer")
                );
                questions.add(q);
            }
            Collections.shuffle(questions);
            Collections.shuffle(questions);
            Collections.shuffle(questions);
            Collections.shuffle(questions);
            Collections.shuffle(questions);
            Collections.shuffle(questions);


            logger.info("Loaded {} questions", questions.size());
        } catch (Exception e) {
            logger.error("Failed to load questions from JSON: {}", e.getMessage(), e);
        }
    }

    /**
     * Selects questions for the specified level avoiding questions
     * used in previous levels (and the current level itself).
     *
     * @param maxCount max number of questions to select
     * @param level    the current level number
     */
    public void setSelectedQuestions(int maxCount, int level) {
        selectedQuestions.clear();
        Random random = new Random();

        // Collect all previously selected questions from levels <= current level
        Set<Question> exclusionSet = new HashSet<>();
        for (Map.Entry<Integer, Set<Question>> entry : previouslySelectedQuestionsByLevel.entrySet()) {
            if (entry.getKey() <= level) {
                exclusionSet.addAll(entry.getValue());
            }
        }

        Set<Question> picked = new HashSet<>();

        int attempts = 0;
        while (picked.size() < maxCount && attempts < 1000) {
            attempts++;

            // Shuffle questions list before each round
            Collections.shuffle(questions, random);

            int addedThisRound = 0;
            for (Question q : questions) {
                if (picked.size() >= maxCount || addedThisRound >= 2) break;
                if (exclusionSet.contains(q) || picked.contains(q)) continue;

                boolean isParagraph = q.getQuestion().trim().split("\\s+").length >= 20;

                if (level == 3 && isParagraph) continue;

                if (level <= 5 || isParagraph) {
                    if (random.nextBoolean()) {
                        picked.add(q);
                        addedThisRound++;
                    }
                }
            }
        }

        // Fallback if not enough
        if (picked.size() < maxCount) {
            logger.warn("Fallback triggered: only {} questions picked, relaxing filters", picked.size());

            for (Question q : questions) {
                if (picked.size() >= maxCount) break;
                if (picked.contains(q)) continue;

                boolean isParagraph = q.getQuestion().trim().split("\\s+").length >= 9;
                if (level == 3 && isParagraph) continue;

                picked.add(q);
            }
        }

        selectedQuestions = new ArrayList<>(picked);
        Collections.shuffle(selectedQuestions);

        previouslySelectedQuestionsByLevel.put(level, new HashSet<>(selectedQuestions));
        logger.info("Selected {} questions for level {}", selectedQuestions.size(), level);
    }

    public List<Question> getSelectedQuestions() {
        return this.selectedQuestions;
    }

    private void loadScores() {
        if (Files.exists(scoreFilePath)) {
            try (Reader reader = Files.newBufferedReader(scoreFilePath)) {
                Type listType = new TypeToken<List<LevelScore>>() {}.getType();
                List<LevelScore> savedScores = gson.fromJson(reader, listType);
                if (savedScores != null) {
                    for (LevelScore score : savedScores) {
                        scoresByLevel.put(score.getLevel(), score);
                    }
                }
                logger.info("Loaded scores for {} levels", scoresByLevel.size());
            } catch (Exception e) {
                logger.error("Failed to load scores: {}", e.getMessage(), e);
            }
        }
    }

    public int calculateStars(double score, int totalQuestions) {
        if (totalQuestions == 0) return 0;
        double percent = (score / 100) * 100;
        if (percent >= 90) return 3;
        if (percent >= 60) return 2;
        if (percent >= 30) return 1;
        return 0;
    }

    public void saveScore(LevelScore levelScore) {
        scoresByLevel.put(levelScore.getLevel(), levelScore);
        try (Writer writer = Files.newBufferedWriter(scoreFilePath)) {
            List<LevelScore> allScores = new ArrayList<>(scoresByLevel.values());
            gson.toJson(allScores, writer); // This now uses pretty-printing
            logger.info("Saved score for level {}", levelScore.getLevel());
        } catch (IOException e) {
            logger.error("Failed to save scores: {}", e.getMessage(), e);
        }
    }


    // Helper method to save all scores at once
    public void saveAllScores() {
        try (Writer writer = Files.newBufferedWriter(scoreFilePath)) {
            List<LevelScore> allScores = new ArrayList<>(scoresByLevel.values());
            gson.toJson(allScores, writer);
            logger.info("Saved all scores");
        } catch (IOException e) {
            logger.error("Failed to save all scores: {}", e.getMessage(), e);
        }
    }

    public LevelScore getLatestScore(int level) {
        try {
            List<LevelScore> allScores = getAllScores();

            if (allScores.isEmpty()) {
                return null;
            }

            // Filter scores for the specific level and get the most recent one
            return allScores.stream()
                    .filter(score -> score.getLevel() == level)
                    .max(Comparator.comparing(score -> {
                        try {
                            return Instant.parse(score.getTimestamp());
                        } catch (Exception e) {
                            logger.warn("Invalid timestamp format: {}", score.getTimestamp());
                            return Instant.EPOCH; // fallback to a very old time
                        }
                    }))
                    .orElse(null);

        } catch (Exception e) {
            logger.error("Error getting latest score for level {}: {}", level, e.getMessage());
            return null;
        }
    }

    private List<LevelScore> getAllScores() {
        try {
            if (scoresByLevel == null || scoresByLevel.isEmpty()) {
                logger.warn("Scores map is null or empty, returning empty list.");
                return new ArrayList<>();
            }

            // Convert Map<Integer, LevelScore> to List<LevelScore>
            return new ArrayList<>(scoresByLevel.values());

        } catch (Exception e) {
            logger.error("Error loading all scores: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public int getTotalLevelsCount() {
        return  scoresByLevel.size();
    }


}
