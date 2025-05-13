package com.projecte;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.HashMap;

public class ControllerBattleAttack {

    @FXML private Label playerPokemonName, playerPokemonHealth, opponentHealth;
    @FXML private Button attackButton1, attackButton2, attackButton3, attackButton4;

    private HashMap<String, Object> playerPokemon;
    private HashMap<String, Object> opponentPokemon;
    private int playerHealth, opponentHealthPoints;
    private int currentPokemonIndex = 0;
    private ArrayList<HashMap<String, Object>> playerTeam;

    private String battleMap;

    @FXML
    public void initialize() {
        loadBattleData();
    }

    public void setBattleMap(String map) {
        this.battleMap = map;
    }

    private void loadBattleData() {
        try {
            AppData db = AppData.getInstance();

            // Obtener equipo del jugador
            playerTeam = db.query("SELECT * FROM Pokemon WHERE trainer = 'Jugador'");
            if (!playerTeam.isEmpty()) {
                playerPokemon = playerTeam.get(currentPokemonIndex);
                playerHealth = Integer.parseInt(playerPokemon.get("health").toString());
            }

            // Obtener oponente
            opponentPokemon = db.query("SELECT * FROM Pokemon WHERE trainer = 'Oponente'").get(0);
            opponentHealthPoints = Integer.parseInt(opponentPokemon.get("health").toString());

            updateBattleUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateBattleUI() {
        playerPokemonName.setText(playerPokemon.get("name").toString());
        playerPokemonHealth.setText("PS: " + playerHealth);
        opponentHealth.setText("PS: " + opponentHealthPoints);
    }

    @FXML
    private void handleAttack(int attackIndex) {
        try {
            int attackDamage = Integer.parseInt(playerPokemon.get("attack" + attackIndex).toString());
            int opponentSpeed = Integer.parseInt(opponentPokemon.get("speed").toString());
            int playerSpeed = Integer.parseInt(playerPokemon.get("speed").toString());

            // Determinar quién ataca primero
            if (playerSpeed >= opponentSpeed) {
                opponentHealthPoints -= attackDamage;
                if (opponentHealthPoints <= 0) {
                    handleVictory();
                } else {
                    opponentCounterAttack();
                }
            } else {
                opponentCounterAttack();
                if (playerHealth <= 0) {
                    handlePokemonFaint();
                } else {
                    opponentHealthPoints -= attackDamage;
                    if (opponentHealthPoints <= 0) handleVictory();
                }
            }

            updateBattleUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void opponentCounterAttack() {
        int attackDamage = Integer.parseInt(opponentPokemon.get("attack1").toString());
        playerHealth -= attackDamage;
        if (playerHealth <= 0) {
            handlePokemonFaint();
        }
    }

    private void handlePokemonFaint() {
        System.out.println(playerPokemon.get("name") + " ha caído!");

        currentPokemonIndex++;
        if (currentPokemonIndex < playerTeam.size()) {
            playerPokemon = playerTeam.get(currentPokemonIndex);
            playerHealth = Integer.parseInt(playerPokemon.get("health").toString());
            updateBattleUI();
        } else {
            handleDefeat();
        }
    }

    private void handleVictory() {
        System.out.println("¡Has ganado la batalla!");
        AppData db = AppData.getInstance();
        db.update("INSERT INTO BattleHistory (trainer, map, result) VALUES ('Jugador', 'Bosque Verde', 'Ganado')");
    }

    private void handleDefeat() {
        System.out.println("¡Has perdido la batalla!");
        AppData db = AppData.getInstance();
        db.update("INSERT INTO BattleHistory (trainer, map, result) VALUES ('Jugador', 'Bosque Verde', 'Perdido')");
    }
}
