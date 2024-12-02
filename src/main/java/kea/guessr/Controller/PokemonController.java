package kea.guessr.Controller;

import kea.guessr.Model.Pokemon;
import kea.guessr.Model.PokemonDTO;
import kea.guessr.Service.PokemonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.*;

@Controller
@RequestMapping
public class PokemonController {

    private final PokemonService pokemonService;
    private final List<Pokemon> pokemonList;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
        selectRandomPokemon();
        pokemonList = new ArrayList<>();
    }

    private Pokemon selectRandomPokemon() {
        int randomDexNumber = new Random().nextInt(386) + 1;
        return pokemonService.fetchPokemon(randomDexNumber);
    }

    @GetMapping("/pokemonScore")
    public String getPokemonScorePage(Model model) {
        int totalScore = pokemonService.calculateTotalScore();
        List<String> sprites = pokemonService.getSavedSprites();
        List<Integer> pokemonScores = pokemonService.getPokemonScores();

        List<Map<String, Object>> combinedList = new ArrayList<>();
        for (int i = 0; i < sprites.size(); i++) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("sprite", sprites.get(i));
            entry.put("score", pokemonScores.get(i));
            combinedList.add(entry);
        }

        model.addAttribute("totalScore", totalScore);
        model.addAttribute("pokemonData", combinedList);
        return "pokemonScore";
    }

    @GetMapping("/pokemonGame")
    public String loadGame(Model model) {
        Pokemon selectedPokemon = selectRandomPokemon();
        pokemonList.add(selectedPokemon);

        for (Pokemon pokemon : pokemonList) {
            System.out.println(pokemon.getName());
        }

        model.addAttribute("spriteUrl", selectedPokemon.getSpriteUrl());
        model.addAttribute("name", selectedPokemon.getName());
        model.addAttribute("id", selectedPokemon.getId());
        model.addAttribute("type1", selectedPokemon.getType1());
        model.addAttribute("type2", selectedPokemon.getType2());
        model.addAttribute("ability1", selectedPokemon.getAbility1());
        model.addAttribute("ability2", selectedPokemon.getAbility2());
        model.addAttribute("ability3", selectedPokemon.getAbility3());
        model.addAttribute("debutGeneration", selectedPokemon.getDebutGeneration());
        model.addAttribute("growth_rate", selectedPokemon.getGrowth_rate());
        model.addAttribute("habitat", selectedPokemon.getHabitat());
        model.addAttribute("weight", selectedPokemon.getWeight());
        model.addAttribute("height", selectedPokemon.getHeight());
        model.addAttribute("pokemonList", pokemonList);

        return "pokemonGame";
    }

    @PostMapping("/savePokemonAnswers")
    public ResponseEntity<?> savePokemonAnswers(@RequestBody PokemonDTO currentAnswers) {
        try {
            System.out.println("Received answers: " + currentAnswers);

            pokemonService.savePokemonAnswers(currentAnswers);

            // Get the current saved count
            int savedCount = pokemonService.getSavedCount();

            // Check if the player has saved 5 Pokémon
            boolean shouldRedirect = savedCount >= 5;

            return ResponseEntity.ok().body(Map.of("success", true, "shouldRedirect", shouldRedirect));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error saving answers"));
        }
    }

    @PostMapping("/resetGame")
    public ResponseEntity<?> resetGame() {
        try {

            pokemonService.resetGameData();

            return ResponseEntity.ok().body(Map.of("success", true, "message", "Game reset successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Failed to reset game"));
        }
    }

    @RestController
    @RequestMapping("/api")
    public static class ApiController {

        private final PokemonService pokemonService;

        public ApiController(PokemonService pokemonService) {
            this.pokemonService = pokemonService;
        }

        @GetMapping("/types")
        public List<String> getPokemonTypes() {
            return pokemonService.fetchPokemonTypes();
        }

        @GetMapping("/habitats")
        public List<String> getPokemonHabitats() {
            return pokemonService.fetchPokemonHabitats();
        }

        @GetMapping("/growth-rates")
        public List<String> getGrowthRates() {
            return pokemonService.fetchGrowthRates();
        }
    }
}
