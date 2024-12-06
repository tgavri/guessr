package kea.guessr.Controller;

import jakarta.servlet.http.HttpSession;
import kea.guessr.Model.User;
import kea.guessr.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
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
    @Autowired
    private UserRepository userRepository;

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


}
