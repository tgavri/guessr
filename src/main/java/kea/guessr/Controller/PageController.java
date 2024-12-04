package kea.guessr.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
