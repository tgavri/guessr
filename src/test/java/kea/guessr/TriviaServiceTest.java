package kea.guessr;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import kea.guessr.Service.TriviaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.*;

public class TriviaServiceTest {

    private TriviaService triviaService;

    @BeforeEach
    public void setUp() {
        triviaService = new TriviaService(new ObjectMapper());
    }


    @Test
    public void testGetQuestionsByDifficulty() {
        addTriviaQuestion(createQuestion("easy"));
        addTriviaQuestion(createQuestion("hard"));

        List<Map<String, Object>> filteredQuestions = triviaService.getQuestionsByDifficulty("easy");

        assertEquals(1, filteredQuestions.size());
        assertEquals("easy", filteredQuestions.get(0).get("difficulty"));
    }

    @Test
    public void testSaveScore() {
        triviaService.saveScore(10);
        assertEquals(10, triviaService.getCurrentScore());
    }

    private void addTriviaQuestion(Map<String, Object> question) {
        // Reflection to add questions directly to the private list
        try {
            var field = TriviaService.class.getDeclaredField("triviaQuestions");
            field.setAccessible(true);
            ((List<Map<String, Object>>) field.get(triviaService)).add(question);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Map<String, Object>> getTriviaQuestions() {
        try {
            var field = TriviaService.class.getDeclaredField("triviaQuestions");
            field.setAccessible(true);
            return (List<Map<String, Object>>) field.get(triviaService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> createQuestion(String difficulty) {
        Map<String, Object> question = new HashMap<>();
        question.put("difficulty", difficulty);
        return question;
    }
}
