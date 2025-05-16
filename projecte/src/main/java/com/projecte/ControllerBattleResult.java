package com.projecte;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

public class ControllerBattleResult {

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

    @FXML private Button backToMenuButton;
    @FXML private Button restartBattleButton;



    public void setBattleResult(boolean playerWon, Pokemon[] playerTeam) {
        if (playerWon) {
            resultTitle.setText("¡VICTORIA!");
            resultMessage.setText("Has demostrado tu valía como entrenador Pokémon");
            setImage(resultImage, "/img/bg/victory_bg.png");
        } else {
            resultTitle.setText("¡DERROTA!");
            resultMessage.setText("Los entrenadores fuertes nunca se rinden");
            setImage(resultImage, "/img/bg/defeat.png");
        }

        // Mostrar los Pokémon del equipo del jugador
        displayPlayerTeam(playerTeam);

    }

    private void displayPlayerTeam(Pokemon[] team) {
        if (team.length > 0) {
            setPokemonImage(playerPokemon1, team[0].getImagePath());
            playerName1.setText(team[0].getName());
        }
        if (team.length > 1) {
            setPokemonImage(playerPokemon2, team[1].getImagePath());
            playerName2.setText(team[1].getName());
        }
        if (team.length > 2) {
            setPokemonImage(playerPokemon3, team[2].getImagePath());
            playerName3.setText(team[2].getName());
        }
    }

    private void setImage(ImageView imageView, String path) {
        try {
            imageView.setImage(new Image(getClass().getResourceAsStream(path)));
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
        }
    }

    private void setPokemonImage(ImageView imageView, String path) {
        try {
            Image image = new Image(getClass().getResourceAsStream(path));
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading Pokémon image: " + path);
        }
    }

    @FXML
    private void handleBackToMenu() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/assets/viewMenu.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            javafx.stage.Stage stage = (javafx.stage.Stage) resultTitle.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            System.err.println("Error loading viewMenu: " + e.getMessage());
        }
    }

    @FXML
    private void handleRestartBattle() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/assets/pokemonSelector.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            javafx.stage.Stage stage = (javafx.stage.Stage) resultTitle.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            System.err.println("Error loading pokemonSelector: " + e.getMessage());
        }
    }
}