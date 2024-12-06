package kea.guessr.Controller;

import kea.guessr.Model.Daily;
import kea.guessr.Model.Pokemon;
import kea.guessr.Model.PokemonDTO;
import kea.guessr.Repository.DailyRepository;
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
    DailyRepository dailyRepository;
    private final List<Pokemon> pokemonList;

    public PokemonController(PokemonService pokemonService, DailyRepository dailyRepository) {
        this.pokemonService = pokemonService;
        this.dailyRepository = dailyRepository;
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
    public String loadGame(@RequestParam(defaultValue = "daily") String mode, Model model) {
        if (mode.equals("daily")) {
            String todayDate = java.time.LocalDate.now().toString();

            Optional<Daily> dailyEntryOptional = dailyRepository.findByGameNameAndDate("Pokemon", todayDate);

            if (dailyEntryOptional.isEmpty() || dailyEntryOptional.get().getPokemonIds().size() < 5) {
                List<Integer> randomPokemonIds = new ArrayList<>();
                while (randomPokemonIds.size() < 5) {
                    Pokemon randomPokemon = selectRandomPokemon();
                    if (!randomPokemonIds.contains(randomPokemon.getId())) {
                        randomPokemonIds.add(randomPokemon.getId());
                    }
                }
                Daily newDailyEntry = new Daily("Pokemon", randomPokemonIds, new ArrayList<>(), todayDate);
                dailyRepository.save(newDailyEntry);

                dailyEntryOptional = Optional.of(newDailyEntry);
            }

            if (dailyEntryOptional.isPresent()) {
                Daily dailyEntry = dailyEntryOptional.get();

                List<Integer> dailyPokemonIds = dailyEntry.getPokemonIds();
                if (dailyPokemonIds.size() == 5) {
                    int nextIndex = pokemonList.size();
                    if (nextIndex < dailyPokemonIds.size()) {
                        int pokemonId = dailyPokemonIds.get(nextIndex);
                        Pokemon selectedPokemon = pokemonService.fetchPokemon(pokemonId);
                        pokemonList.add(selectedPokemon);

                        populateModelWithPokemonData(model, selectedPokemon, mode);
                        return "pokemonGame";
                    }
                }
            }
        }
        Pokemon selectedPokemon = selectRandomPokemon();
        pokemonList.add(selectedPokemon);

        populateModelWithPokemonData(model, selectedPokemon, mode);
        return "pokemonGame";
    }

    private void populateModelWithPokemonData(Model model, Pokemon selectedPokemon, String mode) {
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
        model.addAttribute("gameMode", mode);
    }

    @PostMapping("/savePokemonAnswers")
    @ResponseBody
    public Map<String, Object> savePokemonAnswers(@RequestBody PokemonDTO currentAnswers) {

        pokemonService.savePokemonAnswers(currentAnswers);
        int savedCount = pokemonService.getSavedCount();
        boolean shouldRedirect = savedCount >= 5;

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("shouldRedirect", shouldRedirect);

        if (!shouldRedirect) {
            response.put("gameMode", currentAnswers.getGameMode());
        }

        return response;
    }

    @PostMapping("/resetGame")
    public ResponseEntity<?> resetGame() {
        try {
            pokemonService.resetGameData();
            pokemonList.clear();

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
