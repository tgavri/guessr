package kea.guessr.Controller;

import kea.guessr.Service.TriviaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class TriviaController {

    @Autowired
    TriviaService triviaService;

    // Serve the Trivia Game HTML Page
    @GetMapping("/trivia")
    public String getTriviaGamePage() {
        // Returns the HTML file located in src/main/resources/templates/triviaGame.html
        return "triviaGame";
    }

    // REST Endpoints for Trivia Game
    @RestController
    @RequestMapping("/api/trivia")
    public static class TriviaApiController {

        private final TriviaService triviaService;

        public TriviaApiController(TriviaService triviaService) {
            this.triviaService = triviaService;
        }

        // Get Trivia Questions Filtered by Difficulty
        @GetMapping("/questions")
        public List<Map<String, Object>> getQuestions(@RequestParam String difficulty) {
            return triviaService.getQuestionsByDifficulty(difficulty);
        }

        // Save the Player's Score
        @PostMapping("/save-score")
        public ResponseEntity<String> saveScore(@RequestParam int score) {
            triviaService.saveScore(score);
            return ResponseEntity.ok("Score saved successfully!");
        }

        // Get the Current Score
        @GetMapping("/current-score")
        public int getCurrentScore() {
            return triviaService.getCurrentScore();
        }

        // Reset the Game State
        @PostMapping("/reset")
        public ResponseEntity<String> resetGame() {
            triviaService.resetGame();
            return ResponseEntity.ok("Game reset successfully!");
        }
    }
}
