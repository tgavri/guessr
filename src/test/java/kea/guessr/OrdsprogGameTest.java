package kea.guessr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrdsprogGameTest {

    // Mocked Question class
    static class Question {
        String sentence;
        String answer;
        String explanation;
        String example;

        Question(String sentence, String answer, String explanation, String example) {
            this.sentence = sentence;
            this.answer = answer;
            this.explanation = explanation;
            this.example = example;
        }
    }

    // Mocked Game class
    static class Game {
        List<Question> questions;
        int currentQuestion = 0;
        int score = 0;
        boolean hintUsed = false;
        boolean exampleHintUsed = false;

        public Game(List<Question> questions) {
            this.questions = questions;
        }

        public String getNextQuestion() {
            if (currentQuestion < questions.size()) {
                return questions.get(currentQuestion++).sentence.replace("___", "______");
            }
            return "Game Over!";
        }

        public boolean submitAnswer(String userAnswer) {
            Question current = questions.get(currentQuestion - 1);
            if (current.answer.equalsIgnoreCase(userAnswer.trim())) {
                int points = 10;
                if (hintUsed) points -= 5;
                if (exampleHintUsed) points -= 2.5;
                score += points;
                return true; // Correct
            }
            return false; // Incorrect
        }

        public void useHint() {
            hintUsed = true;
        }

        public void useExampleHint() {
            exampleHintUsed = true;
        }
    }

    // Test setup
    Game mockGame;
    List<Question> mockQuestions;

    @BeforeEach
    void setup() {
        mockQuestions = Mockito.mock(List.class);
        when(mockQuestions.size()).thenReturn(2);
        when(mockQuestions.get(0)).thenReturn(new Question("First question ___", "answer", "Explanation 1", "Example 1"));
        when(mockQuestions.get(1)).thenReturn(new Question("Second question ___", "test", "Explanation 2", "Example 2"));

        mockGame = new Game(mockQuestions);
    }

    @Test
    void testNextQuestion() {
        String question = mockGame.getNextQuestion();
        assertEquals("First question ______", question);

        question = mockGame.getNextQuestion();
        assertEquals("Second question ______", question);

        String gameOver = mockGame.getNextQuestion();
        assertEquals("Game Over!", gameOver);

        // Verify interactions with the mock
        verify(mockQuestions, times(3)).size();
        verify(mockQuestions, times(2)).get(anyInt());
    }

    @Test
    void testSubmitAnswer() {
        // Simulate fetching the next question
        mockGame.getNextQuestion();

        // Simulate submitting an answer
        boolean correct = mockGame.submitAnswer("answer");
        assertTrue(correct);

        // Verify interactions with mock
        verify(mockQuestions, times(2)).get(anyInt()); // Adjust to the actual expected number of calls
    }
}
