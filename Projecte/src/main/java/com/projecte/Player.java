package com.projecte;

public class Player {
    private String level;
    private int points;
    private int battlesWon;
    private int consecutiveWins;
    private int pokemonCaught;

    public Player(String level, int points, int battlesWon, int consecutiveWins, int pokemonCaught) {
        this.level = level;
        this.points = points;
        this.battlesWon = battlesWon;
        this.consecutiveWins = consecutiveWins;
        this.pokemonCaught = pokemonCaught;
    }

    // Getters y Setters
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
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
}
