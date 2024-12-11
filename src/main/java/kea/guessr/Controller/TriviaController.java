package kea.guessr.Controller;

import kea.guessr.Model.User;
import kea.guessr.Repository.UserRepository;
import kea.guessr.Service.TriviaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class TriviaController {

    @Autowired
    TriviaService triviaService;

    @Autowired
    UserRepository userRepository; // Tilføjet UserRepository til håndtering af brugere

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
        private final UserRepository userRepository;

        public TriviaApiController(TriviaService triviaService, UserRepository userRepository) {
            this.triviaService = triviaService;
            this.userRepository = userRepository;
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

        // Endpoint to Update User MMR
        @PostMapping("/update-mmr")
        public ResponseEntity<Integer> updateMmr(@RequestBody Map<String, Object> payload) {
            try {
                // Extract username and score from the payload
                String username = (String) payload.get("username");
                int score = (int) payload.get("score");

                // Log the received data for debugging
                System.out.println("Username received: " + username);
                System.out.println("Score received: " + score);

                // Find user in the database
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                // Update user's MMR
                int newMmr = user.getMmr() + score;
                user.setMmr(newMmr);

                // Save updated user data
                userRepository.save(user);

                // Return the new MMR as the response
                return ResponseEntity.ok(newMmr);
            } catch (Exception e) {
                e.printStackTrace();
                // Return a negative integer as an error indicator
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1);
            }
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

