package com.projecte;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;

public class ControllerBattleResult {

    @FXML private BorderPane mainContainer;
    @FXML private Label resultTitle, resultMessage;
    @FXML private ImageView resultImage;
    @FXML private ImageView playerPokemon1, playerPokemon2, playerPokemon3;
    @FXML private Label playerName1, playerName2, playerName3;
    @FXML private Button backToMenuButton, restartBattleButton;

    public void setBattleResult(boolean playerWon, Pokemon[] playerTeam) {
        try {
            // Configurar colores
            String bgColor = playerWon ? "#4CAF50" : "#F44336";
            String darkColor = playerWon ? "#2E7D32" : "#C62828";
            
            mainContainer.setStyle("-result-color: " + bgColor + "; -result-dark-color: " + darkColor + ";");

            // Configurar contenido
            if (playerWon) {
                setupVictoryUI();
            } else {
                setupDefeatUI();
            }

            displayTeam(playerTeam);

        } catch (Exception e) {
            System.err.println("Error setting battle result: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupVictoryUI() {
        resultTitle.setText("¡VICTORIA!");
        resultMessage.setText("Has demostrado tu valía como entrenador Pokémon");
        loadImageSafe(resultImage, "/img/bg/victory.png");
    }

    private void setupDefeatUI() {
        resultTitle.setText("¡DERROTA!");
        resultMessage.setText("Los entrenadores fuertes nunca se rinden");
        loadImageSafe(resultImage, "/img/bg/defeat.png");
    }

    private void displayTeam(Pokemon[] team) {
        for (int i = 0; i < Math.min(team.length, 3); i++) {
            if (team[i] != null) {
                setPokemonInfo(i, team[i]);
            }
        }
    }

    private void setPokemonInfo(int index, Pokemon pokemon) {
        ImageView[] imageViews = {playerPokemon1, playerPokemon2, playerPokemon3};
        Label[] nameLabels = {playerName1, playerName2, playerName3};
        
        loadImageSafe(imageViews[index], pokemon.getImagePath());
        nameLabels[index].setText(pokemon.getName());
    }

    private void loadImageSafe(ImageView imageView, String path) {
        try {
            Image img = new Image(getClass().getResourceAsStream(path));
            imageView.setImage(img);
        } catch (Exception e) {
            System.err.println("Error loading image from: " + path);
            imageView.setImage(null); // Opcional: establecer imagen por defecto
        }
    }

    @FXML
    private void handleBackToMenu() {
        loadScene("/assets/viewMenu.fxml");
    }

    @FXML
    private void handleRestartBattle() {
        loadScene("/assets/pokemonSelection.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            stage.getScene().setRoot(FXMLLoader.load(getClass().getResource(fxmlPath)));
        } catch (Exception e) {
            System.err.println("Error loading scene: " + fxmlPath);
            e.printStackTrace();
        }
    }
}