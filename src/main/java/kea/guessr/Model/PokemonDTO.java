package kea.guessr.Model;
public class PokemonDTO {
    private Integer pokemonId;
    private Boolean name;
    private Boolean id;
    private Boolean type1;
    private Boolean type2;
    private Boolean ability1;
    private Boolean ability2;
    private Boolean ability3;
    private Boolean height;
    private Boolean weight;
    private Boolean habitat;
    private Boolean growth_rate;
    private Boolean debutGeneration;
    private String sprite;

    public String getSprite () {
        return sprite;
    }

    public void setSprite (String sprite) {
        this.sprite = sprite;
    }

    public Integer getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(Integer pokemonId) {
        this.pokemonId = pokemonId;
    }

    public Boolean getName() {
        return name;
    }

    public void setName(Boolean name) {
        this.name = name;
    }

    public Boolean getId() {
        return id;
    }

    public void setId(Boolean id) {
        this.id = id;
    }

    public Boolean getType1() {
        return type1;
    }

    public void setType1(Boolean type1) {
        this.type1 = type1;
    }

    public Boolean getType2() {
        return type2;
    }

    public void setType2(Boolean type2) {
        this.type2 = type2;
    }

    public Boolean getAbility1() {
        return ability1;
    }

    public void setAbility1(Boolean ability1) {
        this.ability1 = ability1;
    }

    public Boolean getAbility2() {
        return ability2;
    }

    public void setAbility2(Boolean ability2) {
        this.ability2 = ability2;
    }

    public Boolean getAbility3() {
        return ability3;
    }

    public void setAbility3(Boolean ability3) {
        this.ability3 = ability3;
    }

    public Boolean getHeight() {
        return height;
    }

    public void setHeight(Boolean height) {
        this.height = height;
    }

    public Boolean getWeight() {
        return weight;
    }

    public void setWeight(Boolean weight) {
        this.weight = weight;
    }

    public Boolean getHabitat() {
        return habitat;
    }

    public void setHabitat(Boolean habitat) {
        this.habitat = habitat;
    }

    public Boolean getGrowth_rate() {
        return growth_rate;
    }

    public void setGrowth_rate(Boolean growth_rate) {
        this.growth_rate = growth_rate;
    }

    public Boolean getDebutGeneration() {
        return debutGeneration;
    }

    public void setDebutGeneration(Boolean debutGeneration) {
        this.debutGeneration = debutGeneration;
    }
}