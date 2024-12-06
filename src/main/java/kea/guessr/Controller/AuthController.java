package kea.guessr.Controller;

import kea.guessr.Repository.UserRepository;
import kea.guessr.Model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        if (BCrypt.checkpw(password, user.getPassword())) {
            session.setAttribute("username", username);
            return ResponseEntity.ok(user.getRole().toString());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
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
            model.addAttribute("isNewUser", false);
            return "profile";
        } else {
            return "redirect:/login";
        }
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        session.setAttribute("tempEmail", email);
        session.setAttribute("tempPassword", password);
        return ResponseEntity.ok("Pre-registration successful");
    }

    @PostMapping("/complete-registration")
    public ResponseEntity<String> completeRegistration(
            @RequestParam String username,
            @RequestParam String name,
            HttpSession session) {
        String email = (String) session.getAttribute("tempEmail");
        String password = (String) session.getAttribute("tempPassword");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("No pre-registration data found");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        newUser.setRole(User.Role.USER);
        newUser.setStatus("Active");
        newUser.setRegistrationDate(LocalDateTime.now());

        userRepository.save(newUser);

        session.removeAttribute("tempEmail");
        session.removeAttribute("tempPassword");
        session.setAttribute("username", username);
        session.setAttribute("role", User.Role.USER.toString());
        return ResponseEntity.ok("Registration completed successfully");
    }
    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }

        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (name != null) user.setName(name);
            if (email != null) user.setEmail(email);
            if (password != null) user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));

            userRepository.save(user);
            return ResponseEntity.ok("Profile updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/user-data")
    public ResponseEntity<?> getUserData(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setPassword(null); // Don't send password to client
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String username,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (BCrypt.checkpw(oldPassword, user.getPassword())) {
            user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            userRepository.save(user);
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect old password");
        }
    }

    @PostMapping("/delete-account")
    public ResponseEntity<String> deleteAccount(@RequestParam String username, HttpSession session) {
        String sessionUsername = (String) session.getAttribute("username");
        if (!username.equals(sessionUsername)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized action");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userRepository.delete(user);
        session.invalidate();
        return ResponseEntity.ok("Account deleted successfully");
    }
}
