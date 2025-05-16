package com.projecte;

import java.io.Serializable;

/**
 * DTO (Data Transfer Object) que encapsula los datos necesarios para una batalla.
 * Permite transferir información relevante entre escenas/controladores de JavaFX.
 */
public class BattleDataDTO implements Serializable {
    private String battleMap;      // Nombre o identificador del mapa de batalla
    private Pokemon[] playerTeam;  // Equipo de Pokémon del jugador

    /**
     * Constructor para inicializar el DTO con el mapa y el equipo del jugador.
     * @param battleMap Nombre o identificador del mapa de batalla.
     * @param playerTeam Array con los Pokémon seleccionados por el jugador.
     */
    public BattleDataDTO(String battleMap, Pokemon[] playerTeam) {
        this.battleMap = battleMap;
        this.playerTeam = playerTeam;
    }

    /**
     * Obtiene el nombre o identificador del mapa de batalla.
     * @return Nombre del mapa.
     */
    public String getBattleMap() {
        return battleMap;
    }

    /**
     * Obtiene el equipo de Pokémon del jugador.
     * @return Array de Pokémon seleccionados.
     */
    public Pokemon[] getPlayerTeam() {
        return playerTeam;
    }

    /**
     * Establece el nombre o identificador del mapa de batalla.
     * @param battleMap Nombre del mapa.
     */
    public void setBattleMap(String battleMap) {
        this.battleMap = battleMap;
    }

    /**
     * Establece el equipo de Pokémon del jugador.
     * @param playerTeam Array de Pokémon seleccionados.
     */
    public void setPlayerTeam(Pokemon[] playerTeam) {
        this.playerTeam = playerTeam;
    }

}