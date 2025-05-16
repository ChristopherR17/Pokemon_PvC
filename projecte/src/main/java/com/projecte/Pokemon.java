package com.projecte;

/**
 * Clase que representa un Pokémon en el juego.
 * Contiene información relevante como nombre, imagen, puntos de vida, estamina, tipo y estado de derrota.
 */
public class Pokemon {
    private final String name;         // Nombre del Pokémon
    private final String imagePath;    // Ruta de la imagen asociada al Pokémon
    private int currentHP;             // Puntos de vida actuales
    private int currentStamina;        // Estamina actual
    private String type;               // Tipo del Pokémon (por ejemplo, "Fire", "Water", etc.)
    private boolean defeated = false;  // Indica si el Pokémon ha sido derrotado

    /**
     * Constructor de la clase Pokémon.
     * @param name Nombre del Pokémon.
     * @param imagePath Ruta de la imagen del Pokémon.
     */
    public Pokemon(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    /**
     * Obtiene el nombre del Pokémon.
     * @return Nombre del Pokémon.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la ruta de la imagen del Pokémon.
     * @return Ruta de la imagen.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Obtiene los puntos de vida actuales del Pokémon.
     * @return Puntos de vida actuales.
     */
    public int getCurrentHP() {
        return currentHP;
    }

    /**
     * Establece los puntos de vida actuales del Pokémon.
     * @param currentHP Puntos de vida a establecer.
     */
    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    /**
     * Obtiene la estamina actual del Pokémon.
     * @return Estamina actual.
     */
    public int getCurrentStamina() {
        return currentStamina;
    }

    /**
     * Establece la estamina actual del Pokémon.
     * @param currentStamina Estamina a establecer.
     */
    public void setCurrentStamina(int currentStamina) {
        this.currentStamina = currentStamina;
    }

    /**
     * Indica si el Pokémon ha sido derrotado.
     * @return true si está derrotado, false en caso contrario.
     */
    public boolean isDefeated() { 
        return defeated; 
    }

    /**
     * Establece el estado de derrota del Pokémon.
     * @param defeated true si ha sido derrotado, false en caso contrario.
     */
    public void setDefeated(boolean defeated) { 
        this.defeated = defeated; 
    }

    /**
     * Obtiene el tipo del Pokémon.
     * @return Tipo del Pokémon.
     */
    public String getType() {
        return type;
    }

    /**
     * Establece el tipo del Pokémon.
     * @param type Tipo a establecer.
     */
    public void setType(String type) {
        this.type = type;
    }
}