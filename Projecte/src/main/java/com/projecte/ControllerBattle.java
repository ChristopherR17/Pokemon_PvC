package com.projecte;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ControllerBattle {

    private Pokemon[] playerPokemon;

    @FXML
    private Label trainerLabel;
    @FXML
    private Label mapLabel;
    @FXML
    private Label pokemonNameLabel;
    @FXML
    private Label pokemonLevelLabel;
    @FXML
    private Label playerHPLabel;
    @FXML
    private Label opponentHPLabel;
    @FXML
    private ImageView playerPokemonImageView;
    @FXML
    private ImageView opponentPokemonImageView;
    @FXML private Label attack1Name, attack1Damage, attack1Type;
    @FXML private Button attack1Button, attack2Button, attack3Button, attack4Button;
    @FXML private Label attack2Name, attack2Damage, attack2Type;
    @FXML private Label attack3Name, attack3Damage, attack3Type;
    @FXML private Label attack4Name, attack4Damage, attack4Type;



    private String trainer = "Ash";
    private String map = "Bosque Verde";
    private int playerHP = 100;
    private int opponentHP = 100;
    private Random random = new Random();

    @FXML
    public void initialize() {
        attack1Button.setText("Ataque 1");
        attack2Button.setText("Ataque 2");
        attack3Button.setText("Ataque 3");
        attack4Button.setText("Ataque 4");
        loadBattleData();
        updateBattleStatus();
    }

    private void loadBattleData() {
        trainerLabel.setText("Entrenador: " + trainer);
        mapLabel.setText("Mapa: " + map);
        
        pokemonNameLabel.setText("Tangela");
        pokemonLevelLabel.setText("Lv. 14");

        playerHPLabel.setText("PS: " + playerHP + "/100");
        opponentHPLabel.setText("PS: " + opponentHP + "/100");

        playerPokemonImageView.setImage(new Image(getClass().getResource("/img/pokemon/Tangela.png").toExternalForm()));
        opponentPokemonImageView.setImage(new Image(getClass().getResource("/img/pokemon/Vulpix.png").toExternalForm()));
    }

    private void loadAttacksFromDatabase(String pokemonName) {
            try {
                AppData db = AppData.getInstance();
                ArrayList<HashMap<String, Object>> attackData = db.query("SELECT name, damage, type FROM Attacks WHERE pokemon = '" + pokemonName + "'");

                // Cargar datos dinámicamente en los Labels
                attack1Name.setText(attackData.get(0).get("name").toString());
                attack1Damage.setText("Daño: " + attackData.get(0).get("damage").toString());
                attack1Type.setText("Tipo: " + attackData.get(0).get("type").toString());

                attack2Name.setText(attackData.get(1).get("name").toString());
                attack2Damage.setText("Daño: " + attackData.get(1).get("damage").toString());
                attack2Type.setText("Tipo: " + attackData.get(1).get("type").toString());

                attack3Name.setText(attackData.get(2).get("name").toString());
                attack3Damage.setText("Daño: " + attackData.get(2).get("damage").toString());
                attack3Type.setText("Tipo: " + attackData.get(2).get("type").toString());

                attack4Name.setText(attackData.get(3).get("name").toString());
                attack4Damage.setText("Daño: " + attackData.get(3).get("damage").toString());
                attack4Type.setText("Tipo: " + attackData.get(3).get("type").toString());

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error al cargar ataques desde la base de datos: " + e.getMessage());
            }
        }

    public void setPlayerPokemon(Pokemon pokemon1, Pokemon pokemon2, Pokemon pokemon3) {
        // Store the selected Pokémon for the battle
        this.playerPokemon = new Pokemon[] { pokemon1, pokemon2, pokemon3 };
    }

    private void updateBattleStatus() {
        playerHPLabel.setText("PS: " + playerHP + "/100");
        opponentHPLabel.setText("PS: " + opponentHP + "/100");
    }

    @FXML
    private void handleAttack1() {
        processAttack(15, "Ataque rápido!");
    }

    @FXML
    private void handleAttack2() {
        processAttack(10, "Placaje!");
    }

    @FXML
    private void handleAttack3() {
        processAttack(20, "Latigazo!");
    }

    @FXML
    private void handleAttack4() {
        processAttack(25, "Rayo solar!");
    }

    private void processAttack(int damage, String attackName) {
        opponentHP -= damage;

        if (opponentHP <= 0) {
            opponentHPLabel.setText("¡Has ganado la batalla!");
            saveBattleResult("Ganado");
        } else {
            enemyTurn();
        }
    }

    private void enemyTurn() {
        int damage = random.nextInt(15) + 5;
        playerHP -= damage;

        if (playerHP <= 0) {
            playerHPLabel.setText("Has perdido la batalla...");
            saveBattleResult("Perdido");
        }

        updateBattleStatus();
    }

    private void saveBattleResult(String result) {
        try {
            AppData db = AppData.getInstance();
            db.update("INSERT INTO BattleHistory (trainer, map, result) VALUES ('" + trainer + "', '" + map + "', '" + result + "')");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al guardar el resultado de la batalla: " + e.getMessage());
        }
    }
}
