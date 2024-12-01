/*package kea.guessr.Controller;

import com.example.guessr.model.Game;
import kea.guessr.Repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @GetMapping
    public String listGames(Model model) {
        List<Game> games = gameRepository.findAll();
        model.addAttribute("games", games);
        return "admin/games";
    }

    @GetMapping("/create")
    public String showCreateGameForm(Model model) {
        return "admin/create-game";
    }

    @PostMapping("/create")
    public String createGame(@ModelAttribute Game game, RedirectAttributes redirectAttributes) {
        gameRepository.save(game);
        redirectAttributes.addFlashAttribute("successMessage", "Game created successfully!");
        return "redirect:/admin/games";
    }

    @GetMapping("/edit/{id}")
    public String showEditGameForm(@PathVariable Long id, Model model) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game not found"));
        model.addAttribute("game", game);
        return "admin/edit-game";
    }

    @PostMapping("/edit/{id}")
    public String editGame(@PathVariable Long id, @ModelAttribute Game updatedGame, RedirectAttributes redirectAttributes) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game not found"));
        game.setName(updatedGame.getName());
        game.setImageUrl(updatedGame.getImageUrl());
        game.setCategories(updatedGame.getCategories());
        // Update other fields as needed
        gameRepository.save(game);
        redirectAttributes.addFlashAttribute("successMessage", "Game updated successfully!");
        return "redirect:/admin/games";
    }

    @GetMapping("/delete/{id}")
    public String deleteGame(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        gameRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Game deleted successfully!");
        return "redirect:/admin/games";
    }
}
*/