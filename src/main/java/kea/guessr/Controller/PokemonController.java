package kea.guessr.Controller;

import kea.guessr.Model.Pokemon;
import kea.guessr.Service.PokemonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping
public class PokemonController {

    private final PokemonService pokemonService;
    private Pokemon selectedPokemon;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
        selectRandomPokemon();
    }

    private void selectRandomPokemon() {
        int randomDexNumber = new Random().nextInt(386) + 1;
        this.selectedPokemon = pokemonService.fetchPokemon(randomDexNumber);
    }

    @GetMapping("/pokemonGame")
    public String loadGame(Model model) {
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

        return "pokemonGame";
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
