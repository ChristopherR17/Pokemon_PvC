package com.projecte;

public class Pokemon {
    private final String name;
    private final String imagePath;
    private int currentHP;
    private int currentStamina;
    private boolean defeated = false;

    public Pokemon(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public int getCurrentStamina() {
        return currentStamina;
    }

    public void setCurrentStamina(int currentStamina) {
        this.currentStamina = currentStamina;
    }

    public boolean isDefeated() { 
        return defeated; 
    }

    public void setDefeated(boolean defeated) { 
        this.defeated = defeated; 
    }
}