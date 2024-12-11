package kea.guessr.Controller;

import kea.guessr.Model.User;
import kea.guessr.Repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Controller
@RequestMapping("/scoreboard")
public class LeaderboardController {

    // Endpoint to serve the scoreboard HTML page
    @GetMapping
    public String scoreboard() {
        return "Scoreboard"; // Sørg for at filen hedder Scoreboard.html i templates
    }

    @RestController
    @RequestMapping("/api/leaderboard")
    public static class MmrApiController {

        private final UserRepository userRepository;

        // Constructor injection
        public MmrApiController(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        // API to return leaderboard data
        @GetMapping
        public List<User> getLeaderboard() {
            return userRepository.findAll(Sort.by(Sort.Direction.DESC, "mmr")); // Antag at MMR er en kolonne
        }
    }
}
