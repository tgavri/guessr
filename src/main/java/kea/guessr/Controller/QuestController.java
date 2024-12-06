package kea.guessr.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuestController {
    @GetMapping("/Quest")
    public String showRunningGame() {return "QuestMaker";
    }

}
