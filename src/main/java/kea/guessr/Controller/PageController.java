package kea.guessr.Controller;

import jakarta.servlet.http.HttpSession;
import kea.guessr.Model.ImageGuessrDTO;
import kea.guessr.Model.User;
import kea.guessr.Repository.UserRepository;
import kea.guessr.Service.ImageGuessrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class PageController {
  
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageGuessrService imageGuessrService;
  
    @GetMapping("/")
    public String showIndexPage() {
        return "index";
    }
    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }

    @GetMapping("/privacy")
    public String showPrivacyPage() {
        return "privacy";
    }
    @GetMapping("/ordsprog")
    public String showOrdsprogPage() {
        return "ordsprog";
    }
    @GetMapping("/fakta")
    public String showFaktaspilPage() {
        return "faktaspillet";
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String tempEmail = (String) session.getAttribute("tempEmail");

        if (tempEmail != null) {
            model.addAttribute("isNewUser", true);
            model.addAttribute("email", tempEmail);
            return "register";
        } else if (username != null) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            model.addAttribute("user", user);
            return "profile";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/register")
    public String registerTempUser(@RequestParam String email, @RequestParam String password, HttpSession session) {
        session.setAttribute("tempEmail", email);
        session.setAttribute("tempPassword", password);
        return "redirect:/profile";
    }

    @GetMapping("/pokemonFront")
    public String getPokemonFront() {return "pokemonFront";}

    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }
    @GetMapping("/fragments/navbar")
    public String getIndexHeader() {
        return "fragments/navbar";
    }

    @GetMapping("/fragments/footer")
    public String getIndexFooter() {
        return "fragments/footer";
    }

    @GetMapping("/fragtments/cookies")
    public String getIndexCookies() { return "fragments/cookies"; }


}
