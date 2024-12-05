package kea.guessr.Service;




import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class TriviaService {

    private final ObjectMapper objectMapper;

    // Cache for trivia questions
    private final List<Map<String, Object>> triviaQuestions = new ArrayList<>();
    private final List<Integer> triviaScores = new ArrayList<>();
    private int currentScore = 0;

    // Constructor
    public TriviaService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Fetch questions from the API and cache them
    public void fetchAndCacheQuestions() {
        try {
            if (triviaQuestions.isEmpty()) { // Only fetch if the cache is empty
                URL url = new URL("https://opentdb.com/api.php?amount=50&type=multiple");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream responseStream = connection.getInputStream();
                JsonNode jsonNode = objectMapper.readTree(responseStream); // Parse the InputStream directly

                if (jsonNode.has("results") && jsonNode.get("results").isArray()) {
                    for (JsonNode questionNode : jsonNode.get("results")) {
                        Map<String, Object> questionData = new HashMap<>();
                        questionData.put("question", questionNode.get("question").asText());
                        questionData.put("correctAnswer", questionNode.get("correct_answer").asText());

                        List<String> incorrectAnswers = new ArrayList<>();
                        questionNode.get("incorrect_answers").forEach(ans -> incorrectAnswers.add(ans.asText()));
                        questionData.put("incorrectAnswers", incorrectAnswers);

                        questionData.put("difficulty", questionNode.get("difficulty").asText());
                        triviaQuestions.add(questionData);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch trivia questions", e);
        }
    }


    // Get questions filtered by difficulty
    public List<Map<String, Object>> getQuestionsByDifficulty(String difficulty) {
        if (triviaQuestions.isEmpty()) {
            fetchAndCacheQuestions(); // Ensure questions are available
        }

        List<Map<String, Object>> filteredQuestions = new ArrayList<>();
        for (Map<String, Object> question : triviaQuestions) {
            if (difficulty.equalsIgnoreCase((String) question.get("difficulty"))) {
                filteredQuestions.add(question);
            }
        }

        // Shuffle questions to add randomness
        Collections.shuffle(filteredQuestions);
        return filteredQuestions.subList(0, Math.min(10, filteredQuestions.size())); // Limit to 10 questions
    }

    // Save the player's score
    public void saveScore(int score) {
        triviaScores.add(score);
        currentScore = score;
    }

    // Get the current score
    public int getCurrentScore() {
        return currentScore;
    }

    // Get all scores
    public List<Integer> getAllScores() {
        return triviaScores;
    }

    // Reset game state
    public void resetGame() {
        currentScore = 0;
    }
}


