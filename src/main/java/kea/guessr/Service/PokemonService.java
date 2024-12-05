package kea.guessr.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kea.guessr.Model.Pokemon;
import kea.guessr.Model.PokemonDTO;
import kea.guessr.Repository.DailyRepository;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PokemonService {
    private final ObjectMapper objectMapper;
    private List<Map<String, Object>> answersList = new ArrayList<>();
    private int savedCount = 0; // Track saved Pokémon count
    private List<Integer> pokemonScores = new ArrayList<>();

    public int getSavedCount() {
        return savedCount;
    }

    public List<Integer> getPokemonScores() {
        return pokemonScores; // Expose Pokémon scores
    }

    public PokemonService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Pokemon fetchPokemon(int dexNumber) {
        try {
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + dexNumber);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream responseStream = connection.getInputStream();
            JsonNode jsonNode = objectMapper.readTree(responseStream);

            Pokemon pokemon = new Pokemon();
            pokemon.setName(capitalizeFirstLetter(jsonNode.get("name").asText()));
            pokemon.setId(jsonNode.get("id").asInt());

            // Extract Types
            JsonNode types = jsonNode.get("types");
            if (types.isArray()) {
                for (JsonNode typeNode : types) {
                    int slot = typeNode.get("slot").asInt();
                    String typeName = capitalizeFirstLetter(typeNode.get("type").get("name").asText());

                    if (slot == 1) {
                        pokemon.setType1(typeName);
                    } else if (slot == 2) {
                        pokemon.setType2(typeName);
                    }
                }
            }

            // Extract Abilities
            String ability1 = null;
            String ability2 = null;
            String hiddenAbility = null;

            JsonNode abilities = jsonNode.get("abilities");
            if (abilities.isArray()) {
                for (JsonNode abilityNode : abilities) {
                    String abilityName = capitalizeFirstLetter(abilityNode.get("ability").get("name").asText());
                    boolean isHidden = abilityNode.get("is_hidden").asBoolean();

                    if (isHidden) {
                        hiddenAbility = abilityName;
                    } else if (ability1 == null) {
                        ability1 = abilityName;
                    } else if (ability2 == null) {
                        ability2 = abilityName;
                    }
                }
            }

            pokemon.setAbility1(ability1);
            pokemon.setAbility2(ability2);
            pokemon.setAbility3(hiddenAbility);

            pokemon.setHeight(jsonNode.get("height").asInt());
            pokemon.setWeight(jsonNode.get("weight").asInt());

            JsonNode showdownNode = jsonNode.get("sprites").get("other").get("showdown");
            if (showdownNode != null && showdownNode.get("front_default") != null) {
                pokemon.setSpriteUrl(showdownNode.get("front_default").asText());
            } else {
                pokemon.setSpriteUrl(jsonNode.get("sprites").get("front_default").asText());
            }

            JsonNode versions = jsonNode.get("sprites").get("versions");
            int debutGeneration = determineDebutGeneration(versions);
            pokemon.setDebutGeneration(debutGeneration);

            Map<String, String> speciesData = fetchGrowthRateAndHabitat(dexNumber);
            pokemon.setGrowth_rate(speciesData.get("growth_rate"));
            pokemon.setHabitat(speciesData.get("habitat"));

            return pokemon;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Pokémon data", e);
        }
    }

    public void savePokemonAnswers(PokemonDTO currentAnswers) {
        // Save the answers as before
        Map<String, Object> answersMap = new HashMap<>();
        answersMap.put("pokemonId", currentAnswers.getPokemonId());
        answersMap.put("name", currentAnswers.getName());
        answersMap.put("id", currentAnswers.getId());
        answersMap.put("type1", currentAnswers.getType1());
        answersMap.put("type2", currentAnswers.getType2());
        answersMap.put("ability1", currentAnswers.getAbility1());
        answersMap.put("ability2", currentAnswers.getAbility2());
        answersMap.put("ability3", currentAnswers.getAbility3());
        answersMap.put("height", currentAnswers.getHeight());
        answersMap.put("weight", currentAnswers.getWeight());
        answersMap.put("habitat", currentAnswers.getHabitat());
        answersMap.put("growth_rate", currentAnswers.getGrowth_rate());
        answersMap.put("debutGeneration", currentAnswers.getDebutGeneration());
        answersMap.put("spriteUrl", currentAnswers.getSprite());
        answersMap.put("gameMode", currentAnswers.getGameMode());

        int score = 0;
        for (Map.Entry<String, Object> entry : answersMap.entrySet()) {
            if (entry.getValue() instanceof Boolean && (Boolean) entry.getValue()) {
                score += 100;
            }
        }

        pokemonScores.add(score);
        answersList.add(answersMap);

        savedCount++;
    }

    public int calculateTotalScore() {
        return pokemonScores.stream().mapToInt(Integer::intValue).sum();
    }

    public void resetGameData() {
        answersList.clear();
        pokemonScores.clear();
        savedCount = 0;
    }

    private Map<String, String> fetchGrowthRateAndHabitat(int dexNumber) {
        try {
            URL speciesUrl = new URL("https://pokeapi.co/api/v2/pokemon-species/" + dexNumber);
            HttpURLConnection speciesConnection = (HttpURLConnection) speciesUrl.openConnection();
            speciesConnection.setRequestMethod("GET");

            InputStream speciesStream = speciesConnection.getInputStream();
            JsonNode speciesJson = objectMapper.readTree(speciesStream);

            String growth_rate = speciesJson.has("growth_rate") && speciesJson.get("growth_rate") != null
                    ? capitalizeFirstLetter(speciesJson.get("growth_rate").get("name").asText())
                    : "Unknown";
            String habitat = speciesJson.has("habitat") && speciesJson.get("habitat") != null
                    ? capitalizeFirstLetter(speciesJson.get("habitat").get("name").asText())
                    : "Unknown";

            Map<String, String> result = new HashMap<>();
            result.put("growth_rate", growth_rate);
            result.put("habitat", habitat);
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Pokémon species data", e);
        }
    }

    public List<String> getSavedSprites() {
        List<String> sprites = new ArrayList<>();
        for (Map<String, Object> answers : answersList) {
            if (answers.containsKey("spriteUrl")) {
                sprites.add((String) answers.get("spriteUrl"));
            }
        }
        return sprites;
    }

    public List<String> fetchPokemonTypes() {
        try {
            URL url = new URL("https://pokeapi.co/api/v2/type/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream responseStream = connection.getInputStream();
            JsonNode jsonNode = objectMapper.readTree(responseStream);

            List<String> types = new ArrayList<>();
            JsonNode results = jsonNode.get("results");

            if (results != null && results.isArray()) {
                for (JsonNode typeNode : results) {
                    String typeName = typeNode.get("name").asText();
                    // Exclude "stellar" and "unknown"
                    if (!"stellar".equalsIgnoreCase(typeName) && !"unknown".equalsIgnoreCase(typeName)) {
                        types.add(capitalizeFirstLetter(typeName));
                    }
                }
            }
            return types;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch Pokémon types", e);
        }
    }

    public List<String> fetchPokemonHabitats() {
        try {
            URL url = new URL("https://pokeapi.co/api/v2/pokemon-habitat/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream responseStream = connection.getInputStream();
            JsonNode jsonNode = objectMapper.readTree(responseStream);

            List<String> habitats = new ArrayList<>();
            JsonNode results = jsonNode.get("results");

            if (results != null && results.isArray()) {
                for (JsonNode habitatNode : results) {
                    String habitatName = capitalizeFirstLetter(habitatNode.get("name").asText());
                    habitats.add(habitatName);
                }
            }
            return habitats;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch Pokémon habitats", e);
        }
    }

    public List<String> fetchGrowthRates() {
        try {
            URL url = new URL("https://pokeapi.co/api/v2/growth-rate/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream responseStream = connection.getInputStream();
            JsonNode jsonNode = objectMapper.readTree(responseStream);

            List<String> growthRates = new ArrayList<>();
            JsonNode results = jsonNode.get("results");

            if (results != null && results.isArray()) {
                for (JsonNode growthRateNode : results) {
                    String growthRateName = capitalizeFirstLetter(growthRateNode.get("name").asText());
                    growthRates.add(growthRateName);
                }
            }
            return growthRates;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch Pokémon growth rates", e);
        }
    }

    private int determineDebutGeneration(JsonNode versions) {
        if (versions == null) return -1;

        String[] generations = {
                "generation-i", "generation-ii", "generation-iii",
                "generation-iv", "generation-v", "generation-vi",
                "generation-vii", "generation-viii", "generation-ix"
        };

        for (int i = 0; i < generations.length; i++) {
            JsonNode generationNode = versions.get(generations[i]);
            if (generationNode != null && hasNonNullSprite(generationNode)) {
                return i + 1;
            }
        }

        return -1;
    }

    private boolean hasNonNullSprite(JsonNode generationNode) {
        for (JsonNode version : generationNode) {
            for (JsonNode sprite : version) {
                if (sprite != null && !sprite.isNull()) {
                    return true;
                }
            }
        }
        return false;
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
