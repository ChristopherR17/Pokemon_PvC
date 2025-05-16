package com.projecte;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Controlador de la pantalla de resultados de la batalla.
 * Muestra el resultado (victoria o derrota) y el equipo del jugador.
 * Permite volver al menú principal o reiniciar la batalla.
 */
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

    /**
     * Establece el resultado de la batalla y muestra el equipo del jugador.
     * @param playerWon true si el jugador ganó, false si perdió.
     * @param playerTeam Array con los Pokémon del equipo del jugador.
     */
    public void setBattleResult(boolean playerWon, Pokemon[] playerTeam) {
        if (playerWon) {
            resultTitle.setText("¡VICTORIA!");
            resultMessage.setText("Has demostrado tu valía como entrenador Pokémon");
        } else {
            resultTitle.setText("¡DERROTA!");
            resultMessage.setText("Los entrenadores fuertes nunca se rinden");
        }

        // Mostrar los Pokémon del equipo del jugador
        displayPlayerTeam(playerTeam);
    }

    /**
     * Muestra las imágenes y nombres de los Pokémon del equipo del jugador.
     * @param team Array con los Pokémon seleccionados.
     */
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

    /**
     * Asigna una imagen a un ImageView desde la ruta proporcionada.
     * @param imageView ImageView donde se mostrará la imagen.
     * @param path Ruta de la imagen.
     */
    private void setImage(ImageView imageView, String path) {
        try {
            imageView.setImage(new Image(getClass().getResourceAsStream(path)));
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
        }
    }

    /**
     * Asigna la imagen de un Pokémon a un ImageView desde la ruta proporcionada.
     * @param imageView ImageView donde se mostrará la imagen del Pokémon.
     * @param path Ruta de la imagen del Pokémon.
     */
    private void setPokemonImage(ImageView imageView, String path) {
        try {
            Image image = new Image(getClass().getResourceAsStream(path));
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading Pokémon image: " + path);
        }
    }

    /**
     * Maneja el evento del botón para volver al menú principal.
     * Cambia la escena a la vista del menú principal.
     */
    @FXML
    private void handleBackToMenu() {
        // Lógica para volver al menú principal
        // Aquí implementa la navegación a la vista anterior
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/viewMenu.fxml")); 
            Scene newScene = new Scene(loader.load());
            Stage stage = (Stage) backToMenuButton.getScene().getWindow();
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista anterior: " + e.getMessage());
        }
    }

    /**
     * Maneja el evento del botón para reiniciar la batalla.
     * Cambia la escena a la vista de selección de Pokémon.
     */
    @FXML
    private void handleRestartBattle() {
        // Lógica para reiniciar la batalla
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/pokemonSelection.fxml"));
            Stage stage = (Stage) restartBattleButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error de navegación. No se pudo cargar pokemonSelection.fxml: " + e.getMessage());
        }
    }
}