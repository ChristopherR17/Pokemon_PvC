package com.projecte;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ControllerBattleOptions {

    @FXML private Label mapNameLabel;
    @FXML private Label teamSelectionLabel;
    @FXML private Label activePokemonLabel;
    @FXML private Button confirmOptionsButton;

    @FXML
    public void initialize() {
        loadBattleOptions();
    }

    private void loadBattleOptions() {
        // Simulación de datos desde el juego
        mapNameLabel.setText("Mapa: Bosque Verde");
        teamSelectionLabel.setText("Equipo: Pikachu, Charizard, Blastoise");
        activePokemonLabel.setText("Pokémon Activo: Pikachu");
    }

    @FXML
    private void handleConfirmOptions() {
        System.out.println("Opciones confirmadas para la batalla!");
    }
}
