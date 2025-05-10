package com.projecte;

public class Player {
    private static Player instance;

    private int level;
    private int points;
    private int battlesWon;
    private int consecutiveWins;
    private int pokemonCaught;
    private int id;
    private String name;

    private Player(){
        // Constructor vacío
    }

    // Método estático para obtener la única instancia
    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    // Getters y Setters
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }

    public int getBattlesWon() {
        return battlesWon;
    }
    public void setBattlesWon(int battlesWon) {
        this.battlesWon = battlesWon;
    }

    public int getConsecutiveWins() {
        return consecutiveWins;
    }
    public void setConsecutiveWins(int consecutiveWins) {
        this.consecutiveWins = consecutiveWins;
    }

    public int getPokemonCaught() {
        return pokemonCaught;
    }
    public void setPokemonCaught(int pokemonCaught) {
        this.pokemonCaught = pokemonCaught;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
