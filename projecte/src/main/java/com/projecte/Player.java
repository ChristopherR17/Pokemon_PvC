package com.projecte;

/**
 * Clase singleton que representa al jugador en el juego.
 * Almacena información relevante como nivel, puntos, victorias, rachas, Pokémon capturados, id y nombre.
 */
public class Player {
    private static Player instance;

    private int level;              // Nivel del jugador
    private int points;             // Puntos acumulados
    private int battlesWon;         // Número de batallas ganadas
    private int consecutiveWins;    // Número de victorias consecutivas
    private int pokemonCaught;      // Número de Pokémon capturados
    private int id;                 // Identificador único del jugador
    private String name;            // Nombre del jugador

    /**
     * Constructor privado para evitar instanciación directa (patrón Singleton).
     */
    private Player(){
        // Constructor vacío
    }

    /**
     * Obtiene la única instancia de la clase Player (patrón Singleton).
     * @return Instancia única de Player.
     */
    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    // Getters y Setters

    /**
     * Obtiene el nivel del jugador.
     * @return Nivel actual.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Establece el nivel del jugador.
     * @param level Nivel a establecer.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Obtiene los puntos acumulados del jugador.
     * @return Puntos actuales.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Establece los puntos acumulados del jugador.
     * @param points Puntos a establecer.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Obtiene el número de batallas ganadas.
     * @return Batallas ganadas.
     */
    public int getBattlesWon() {
        return battlesWon;
    }

    /**
     * Establece el número de batallas ganadas.
     * @param battlesWon Batallas ganadas a establecer.
     */
    public void setBattlesWon(int battlesWon) {
        this.battlesWon = battlesWon;
    }

    /**
     * Obtiene el número de victorias consecutivas.
     * @return Victorias consecutivas.
     */
    public int getConsecutiveWins() {
        return consecutiveWins;
    }

    /**
     * Establece el número de victorias consecutivas.
     * @param consecutiveWins Victorias consecutivas a establecer.
     */
    public void setConsecutiveWins(int consecutiveWins) {
        this.consecutiveWins = consecutiveWins;
    }

    /**
     * Obtiene el número de Pokémon capturados.
     * @return Pokémon capturados.
     */
    public int getPokemonCaught() {
        return pokemonCaught;
    }

    /**
     * Establece el número de Pokémon capturados.
     * @param pokemonCaught Pokémon capturados a establecer.
     */
    public void setPokemonCaught(int pokemonCaught) {
        this.pokemonCaught = pokemonCaught;
    }

    /**
     * Obtiene el identificador único del jugador.
     * @return ID del jugador.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del jugador.
     * @param id ID a establecer.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del jugador.
     * @return Nombre del jugador.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del jugador.
     * @param name Nombre a establecer.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Calcula el nivel del jugador en función de los puntos acumulados.
     * Cada nivel requiere 1000 puntos.
     */
    private void calculateLevel() {
        this.level = this.points / 1000;
    }
}