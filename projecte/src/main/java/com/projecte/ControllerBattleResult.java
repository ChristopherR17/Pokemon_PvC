package com.projecte;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class ControllerBattleResult {

    @FXML private BorderPane rootPane;
    @FXML private Label resultTitle;
    @FXML private Label resultMessage;
    @FXML private ImageView resultBackground;
    @FXML private ImageView resultImage;
    @FXML private ImageView playerPokemon1;
    @FXML private ImageView playerPokemon2;
    @FXML private ImageView playerPokemon3;
    @FXML private Label playerName1;
    @FXML private Label playerName2;
    @FXML private Label playerName3;

    public void setBattleResult(boolean playerWon, Pokemon[] playerTeam) {
        // Configurar colores según el resultado
        String backgroundColor = playerWon ? "#4CAF50" : "#F44336"; // Verde para victoria, rojo para derrota
        String darkColor = playerWon ? "#2E7D32" : "#C62828"; // Versión oscura para detalles
        
        rootPane.setStyle("-result-color: " + backgroundColor + "; -result-dark-color: " + darkColor + ";");

        if (playerWon) {
            resultTitle.setText("¡VICTORIA!");
            resultMessage.setText("Has demostrado tu valía como entrenador Pokémon");
            setImage(resultBackground, "/img/battle_results/victory_bg.jpg");
            setImage(resultImage, "/img/battle_results/victory.png");
        } else {
            resultTitle.setText("¡DERROTA!");
            resultMessage.setText("Los entrenadores fuertes nunca se rinden");
            setImage(resultBackground, "/img/battle_results/defeat_bg.jpg");
            setImage(resultImage, "/img/battle_results/defeat.png");
        }

        displayPlayerTeam(playerTeam);
    }

    private void displayPlayerTeam(Pokemon[] team) {
        // ... (mismo código que antes)
    }

    private void setImage(ImageView imageView, String path) {
        // ... (mismo código que antes)
    }
}