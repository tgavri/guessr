package kea.guessr.Model;

public class Pokemon {
    private String name;
    private int id;
    private String type1;
    private String type2;
    private String ability1;
    private String ability2;
    private String ability3;
    private int height;
    private int weight;
    private String spriteUrl;
    private int debutGeneration;
    private String habitat;
    private String growth_rate;

    // Getters
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getType1() {
        return type1;
    }

    public String getType2() {
        return type2;
    }
    public String getAbility1() {
        return ability1;
    }
    public String getAbility2() {
        return ability2;
    }
    public String getAbility3() {
        return ability3;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public String getSpriteUrl() {
        return spriteUrl;
    }

    public int getDebutGeneration() {
        return debutGeneration;
    }

    public String getHabitat() {
        return habitat;
    }

    public String getGrowth_rate() {
        return growth_rate;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }
    public void setAbility1(String ability1) {
        this.ability1 = ability1;
    }
    public void setAbility2(String ability2) {
        this.ability2 = ability2;
    }
    public void setAbility3(String ability3) {
        this.ability3 = ability3;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setSpriteUrl(String spriteUrl) {
        this.spriteUrl = spriteUrl;
    }

    public void setDebutGeneration(int debutGeneration) {
        this.debutGeneration = debutGeneration;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public void setGrowth_rate(String growth_rate) {
        this.growth_rate = growth_rate;
    }
}
