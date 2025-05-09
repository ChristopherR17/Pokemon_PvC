package com.projecte;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControllerBattle {

    @FXML
    private ImageView battleBackground, playerPokemonImage, enemyPokemonImage, backupPokemon1, backupPokemon2;

    @FXML
    private Label playerPokemonName, enemyPokemonName, playerHealth, enemyHealth, attack1, attack2, attack3, attack4, battleStatus;

    private Pokemon playerActivePokemon;
    private Pokemon enemyPokemon;
    private Pokemon backup1, backup2;

    public void setBattleBackground(String mapImagePath) {
        battleBackground.setImage(new Image(getClass().getResource(mapImagePath).toExternalForm()));
    }

    public void setPlayerPokemon(Pokemon active, Pokemon backupOne, Pokemon backupTwo) {
        this.playerActivePokemon = active;
        this.backup1 = backupOne;
        this.backup2 = backupTwo;

        playerPokemonImage.setImage(new Image(getClass().getResource(active.getImagePath()).toExternalForm()));
        playerPokemonName.setText(active.getName() + " (Lv. " + active.getLevel() + ")");
        playerHealth.setText("PS: " + active.getHealth() + "/" + active.getMaxHealth());

        backupPokemon1.setImage(new Image(getClass().getResource(backupOne.getImagePath()).toExternalForm()));
        backupPokemon2.setImage(new Image(getClass().getResource(backupTwo.getImagePath()).toExternalForm()));

        attack1.setText(active.getMove1());
        attack2.setText(active.getMove2());
        attack3.setText(active.getMove3());
        attack4.setText(active.getMove4());
    }

    public void setEnemyPokemon(Pokemon enemy) {
        this.enemyPokemon = enemy;

        enemyPokemonImage.setImage(new Image(getClass().getResource(enemy.getImagePath()).toExternalForm()));
        enemyPokemonName.setText(enemy.getName() + " (Lv. " + enemy.getLevel() + ")");
        enemyHealth.setText("PS: " + enemy.getHealth() + "/" + enemy.getMaxHealth());
    }

    @FXML
    private void handleFight() {
        battleStatus.setText(playerActivePokemon.getName() + " usa " + playerActivePokemon.getMove1() + "!");

        int damage = playerActivePokemon.calculateDamage();
        enemyPokemon.receiveDamage(damage);
        updateHealthBars();

        if (enemyPokemon.getHealth() <= 0) {
            battleStatus.setText(enemyPokemon.getName() + " ha caído!");
        } else {
            handleEnemyTurn();
        }
    }

    private void handleEnemyTurn() {
        battleStatus.setText(enemyPokemon.getName() + " usa " + enemyPokemon.getMove1() + "!");

        int damage = enemyPokemon.calculateDamage();
        playerActivePokemon.receiveDamage(damage);
        updateHealthBars();

        if (playerActivePokemon.getHealth() <= 0) {
            battleStatus.setText(playerActivePokemon.getName() + " ha caído! Cambia de Pokémon.");
            handlePokemon();
        }
    }

    private void updateHealthBars() {
        playerHealth.setText("PS: " + playerActivePokemon.getHealth() + "/" + playerActivePokemon.getMaxHealth());
        enemyHealth.setText("PS: " + enemyPokemon.getHealth() + "/" + enemyPokemon.getMaxHealth());
    }

    @FXML
    private void handlePokemon() {
        if (backup1.getHealth() > 0) {
            setPlayerPokemon(backup1, backup2, playerActivePokemon);
            battleStatus.setText("¡" + backup1.getName() + " entra en combate!");
        } else if (backup2.getHealth() > 0) {
            setPlayerPokemon(backup2, playerActivePokemon, backup1);
            battleStatus.setText("¡" + backup2.getName() + " entra en combate!");
        } else {
            battleStatus.setText("Has perdido la batalla...");
        }
    }
}
