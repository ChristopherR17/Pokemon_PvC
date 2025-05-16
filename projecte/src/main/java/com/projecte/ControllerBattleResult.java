package com.projecte;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class ControllerBattleResult {

    @FXML private BorderPane mainContainer;
    @FXML private Label resultTitle;
    @FXML private Label resultMessage;
    @FXML private ImageView resultImage;
    @FXML private ImageView playerPokemon1;
    @FXML private ImageView playerPokemon2;
    @FXML private ImageView playerPokemon3;
    @FXML private Label playerName1;
    @FXML private Label playerName2;
    @FXML private Label playerName3;
    @FXML private Button backToMenuButton;
    @FXML private Button restartBattleButton;
    

    public void setBattleResult(boolean playerWon, Pokemon[] playerTeam) {
        // Configurar colores según el resultado
        String backgroundColor = playerWon ? "#4CAF50" : "#F44336"; // Verde/Rojo
        String darkColor = playerWon ? "#2E7D32" : "#C62828"; // Verde oscuro/Rojo oscuro
        
        // Aplicar estilos
        mainContainer.setStyle("-result-color: " + backgroundColor + "; -result-dark-color: " + darkColor + ";");

        if (playerWon) {
            resultTitle.setText("¡VICTORIA!");
            resultMessage.setText("Has demostrado tu valía como entrenador Pokémon");
            loadImage(resultImage, "/img/bg/victory.png");
        } else {
            resultTitle.setText("¡DERROTA!");
            resultMessage.setText("Los entrenadores fuertes nunca se rinden");
            loadImage(resultImage, "/img/bg/defeat.png");
        }

        displayPlayerTeam(playerTeam);
    }

    private void displayPlayerTeam(Pokemon[] team) {
        if (team.length > 0 && team[0] != null) {
            loadPokemonImage(playerPokemon1, team[0].getImagePath());
            playerName1.setText(team[0].getName());
        }
        if (team.length > 1 && team[1] != null) {
            loadPokemonImage(playerPokemon2, team[1].getImagePath());
            playerName2.setText(team[1].getName());
        }
        if (team.length > 2 && team[2] != null) {
            loadPokemonImage(playerPokemon3, team[2].getImagePath());
            playerName3.setText(team[2].getName());
        }
    }

    private void loadImage(ImageView imageView, String path) {
        try {
            Image image = new Image(getClass().getResourceAsStream(path));
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
            e.printStackTrace();
        }
    }

    private void loadPokemonImage(ImageView imageView, String path) {
        try {
            Image image = new Image(getClass().getResourceAsStream(path));
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading Pokémon image: " + path);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/viewMenu.fxml"));
            Stage stage = (Stage) backToMenuButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            System.err.println("Error loading menu view: " + e.getMessage());
        }
    }

    @FXML
    private void handleRestartBattle() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/pokemonSelection.fxml"));
            Stage stage = (Stage) restartBattleButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            System.err.println("Error loading battle selection: " + e.getMessage());
        }
    }
}