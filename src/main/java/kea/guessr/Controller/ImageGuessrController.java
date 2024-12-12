package kea.guessr.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kea.guessr.Model.ImageGuessrDTO;
import kea.guessr.Service.ImageGuessrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Controller
public class ImageGuessrController {

    @Autowired
    private ImageGuessrService imageGuessrService;

    @GetMapping("/imageguessr")
    public String getImageGuessrPage(@CookieValue(name = "hasPlayed", defaultValue = "false") String hasPlayed,
                                     HttpServletResponse response,
                                     Model model) {
        /*if (hasPlayed.equals("true")) {
            return "redirect:/countdown";
        }*/

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().atStartOfDay().plusDays(1);
        long secondsUntilMidnight = ChronoUnit.SECONDS.between(now, midnight);

        Cookie cookie = new Cookie("hasPlayed", "true");
        cookie.setMaxAge((int) secondsUntilMidnight);
        response.addCookie(cookie);

        try {
            ImageGuessrDTO imageGuessrDTO = imageGuessrService.getImageGuessrDTO("src/main/resources/static/data/imagejson.json");
            model.addAttribute("imageUrl", imageGuessrDTO.getUrl());
            model.addAttribute("imageYear", imageGuessrDTO.getYear());
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "imageguessr";
    }

    @GetMapping("/countdown")
    public String showCountdownPage() {
        return "countdown";
    }

    @GetMapping("/imageguessr/data")
    @ResponseBody
    public ImageGuessrDTO getImageGuessrData() throws IOException {
        return imageGuessrService.getImageGuessrDTO("src/main/resources/static/data/imagejson.json");
    }

    @PostMapping("/imageguessr")
    @ResponseBody
    public String handleResult(@RequestBody Map<String, Integer> result) {
        int score = result.get("result");
        imageGuessrService.addPoints(score);
        return "Score received: " + score;
    }
}
