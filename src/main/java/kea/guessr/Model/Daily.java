package kea.guessr.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Daily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String gameName; // Name of the game (e.g., "Pokemon", "QuestionGame")

    @ElementCollection
    @CollectionTable(name = "daily_pokemon_ids", joinColumns = @JoinColumn(name = "daily_id"))
    @Column(name = "pokemon_id")
    private List<Integer> pokemonIds; // For storing 5 Pokémon IDs (or leave empty if not applicable)

    @ElementCollection
    @CollectionTable(name = "daily_question_texts", joinColumns = @JoinColumn(name = "daily_id"))
    @Column(name = "question_text")
    private List<String> questions; // For storing 5 questions (or leave empty if not applicable)

    @Column(nullable = false)
    private String date; // Date for the daily entry (e.g., "2024-12-05")

    // Constructors
    public Daily() {
    }

    public Daily(String gameName, List<Integer> pokemonIds, List<String> questions, String date) {
        this.gameName = gameName;
        this.pokemonIds = pokemonIds;
        this.questions = questions;
        this.date = date;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public List<Integer> getPokemonIds() {
        return pokemonIds;
    }

    public void setPokemonIds(List<Integer> pokemonIds) {
        this.pokemonIds = pokemonIds;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}