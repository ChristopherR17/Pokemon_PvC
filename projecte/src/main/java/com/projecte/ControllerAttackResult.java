package com.projecte;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class ControllerAttackResult {

    @FXML private Label winnerLabel;

    @FXML private ImageView playerPokemon1;
    @FXML private ImageView playerPokemon2;
    @FXML private ImageView playerPokemon3;
    @FXML private Label playerName1;
    @FXML private Label playerName2;
    @FXML private Label playerName3;

    @FXML private ImageView enemyPokemon1;
    @FXML private ImageView enemyPokemon2;
    @FXML private ImageView enemyPokemon3;
    @FXML private Label enemyName1;
    @FXML private Label enemyName2;
    @FXML private Label enemyName3;

    /**
     * Cambia el texto del label de ganador a "WIN" y el nombre del jugador o la máquina.
     * @param playerWon true si ganó el jugador, false si ganó la máquina.
     * @param playerName nombre del jugador.
     */
    public void setResult(boolean playerWon, String playerName) {
        if (playerWon) {
            winnerLabel.setText("WIN\n" + playerName);
            winnerLabel.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: #2e7d32; -fx-effect: dropshadow(gaussian, #fff176, 10, 0.5, 0, 2);");
        } else {
            winnerLabel.setText("WIN\nMÁQUINA");
            winnerLabel.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: #d32f2f; -fx-effect: dropshadow(gaussian, #fff176, 10, 0.5, 0, 2);");
        }
    }

    /**
     * Llama esto desde ControllerBattleAttack para pasar los nombres e imágenes de los equipos.
     * Las rutas de imagen deben ser absolutas desde resources (ej: "/img/pokemon/bulbasaur.png").
     */
    public void setTeams(
            String[] playerNames, String[] playerImagePaths,
            String[] enemyNames, String[] enemyImagePaths) {
        // Player
        setPokemon(playerPokemon1, playerName1, playerNames, playerImagePaths, 0);
        setPokemon(playerPokemon2, playerName2, playerNames, playerImagePaths, 1);
        setPokemon(playerPokemon3, playerName3, playerNames, playerImagePaths, 2);
        // Enemy
        setPokemon(enemyPokemon1, enemyName1, enemyNames, enemyImagePaths, 0);
        setPokemon(enemyPokemon2, enemyName2, enemyNames, enemyImagePaths, 1);
        setPokemon(enemyPokemon3, enemyName3, enemyNames, enemyImagePaths, 2);
    }

    private void setPokemon(ImageView imgView, Label nameLabel, String[] names, String[] imgPaths, int idx) {
        if (names != null && names.length > idx && names[idx] != null) {
            nameLabel.setText(names[idx]);
        } else {
            nameLabel.setText("");
        }
        if (imgPaths != null && imgPaths.length > idx && imgPaths[idx] != null && !imgPaths[idx].isEmpty()) {
            try {
                Image img = new Image(getClass().getResourceAsStream(imgPaths[idx]));
                imgView.setImage(img);
            } catch (Exception e) {
                imgView.setImage(null);
                System.err.println("No se pudo cargar la imagen: " + imgPaths[idx]);
            }
        } else {
            imgView.setImage(null);
        }
    }

    @FXML
    private void handleBackToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/viewMaps.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) winnerLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}