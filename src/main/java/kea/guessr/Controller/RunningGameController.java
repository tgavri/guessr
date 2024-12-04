package kea.guessr.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RunningGameController {
    @GetMapping("/GameSite")
    public String showRunningGame() {return "GameSite";
    }

}
