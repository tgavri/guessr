package kea.guessr.Controller;

import kea.guessr.Model.Game;
import kea.guessr.Model.User;
import kea.guessr.Service.GameService;
import kea.guessr.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    // Dashboard route
    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("totalGames", gameService.getAllGames().size());
        return "admin/dashboard"; // Return a dashboard view
    }

    @GetMapping("/users")
    public String usersManagement(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute User user) {
        userService.createUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/user/{id}/edit")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/edit-user";
    }

    @PostMapping("/user/{id}/edit")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user) {
        System.out.println("Received user data for update: " + user);

        User updatedUser = userService.updateUser(id, user);
        if (updatedUser != null) {
            System.out.println("Updated user data: " + updatedUser);
            return ResponseEntity.ok("User updated successfully");
        } else {
            System.out.println("User update failed for ID: " + id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update user");
        }
    }


    @GetMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/users-data")
    @ResponseBody
    public List<User> getUsersData() {
        return userService.getAllUsers();
    }
    @GetMapping("/dashboard-data")
    @ResponseBody
    public ResponseEntity<?> getDashboardData() {
        try {
            Map<String, Object> dashboardData = new HashMap<>();
            dashboardData.put("totalUsers", userService.getTotalUserCount());
            dashboardData.put("newSignUps", userService.getNewSignUpsLastWeek());
            dashboardData.put("userGrowth", userService.getUserGrowthData());
            dashboardData.put("activeUsers", userService.getActiveUserCount());
            dashboardData.put("userStatusDistribution", userService.getUserStatusDistribution());
            return ResponseEntity.ok(dashboardData);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("stackTrace", Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }




    // Game management routes
    @GetMapping("/content")
    public String gameManagement(Model model) {
        model.addAttribute("games", gameService.getAllGames());
        return "admin/content";
    }

    @PostMapping("/create-game")
    public String createGame(@ModelAttribute Game game) {
        gameService.createGame(game);
        return "redirect:/admin/content";
    }

    @GetMapping("/game/{id}/edit")
    public String editGame(@PathVariable Long id, Model model) {
        Game game = gameService.getGameById(id);
        model.addAttribute("game", game);
        return "admin/edit-game";
    }

    @PostMapping("/game/{id}/edit")
    public String updateGame(@PathVariable Long id, @ModelAttribute Game game) {
        gameService.updateGame(id, game);
        return "redirect:/admin/content";
    }

    @GetMapping("/game/{id}/delete")
    public String deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return "redirect:/admin/content";
    }

}
