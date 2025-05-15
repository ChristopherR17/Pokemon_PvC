package com.projecte;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ControllerAttackResult {

    @FXML private Label attackOrderLabel;
    @FXML private Label attackResultLabel;
    @FXML private Label pokemonStatsUpdateLabel;
    @FXML private Label opponentStatsUpdateLabel;
    @FXML private Button continueBattleButton;

    @FXML
    public void initialize() {
        loadAttackResult();
    }

    private void loadAttackResult() {
        // Simulación de datos desde el juego
        attackOrderLabel.setText("Pikachu atacó primero!");
        attackResultLabel.setText("Impactrueno golpeó con éxito. Daño: 40");
        pokemonStatsUpdateLabel.setText("Pikachu PS: 80/100");
        opponentStatsUpdateLabel.setText("Oponente PS: 35/100");
    }

    @FXML
    private void handleContinueBattle() {
        System.out.println("Continuando la batalla...");
    }
}
