package com.projecte;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMenu implements Initializable {

    @FXML
    private ImageView fondoMenu;

    @FXML
    private Button teamManagementButton;

    @FXML
    private Button battleHistoryButton;

    @FXML
    private Button startBattleButton;

    @FXML
    private Button exitGameButton;

    @FXML
    private Label playerLevel;

    @FXML
    private Label playerPoints;

    @FXML
    private Label battlesWon;

    @FXML
    private Label consecutiveWins;

    @FXML
    private Label pokemonCaught;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar el fondo del menú
        fondoMenu.setImage(new javafx.scene.image.Image("/img/fondoMenu.png"));

        // Inicializar estadísticas de jugador
        playerLevel.setText("Level: 2");
        playerPoints.setText("Points: 2345");
        battlesWon.setText("Battles Won: 15");
        consecutiveWins.setText("Max Consecutive Wins: 3");
        pokemonCaught.setText("Pokémon Caught: 5");

        System.out.println("Vista del menú inicializada correctamente.");
    }

    @FXML
    private void handleTeamManagement() {
        System.out.println("Botón 'Team Management' presionado. Navegando...");
        navigateTo("TeamManagement.fxml");
    }

    @FXML
    private void handleBattleHistory() {
        System.out.println("Botón 'Battle History' presionado. Navegando...");
        navigateTo("BattleHistory.fxml");
    }

    @FXML
    private void handleStartBattle() {
        System.out.println("Botón 'Start New Battle' presionado. Navegando...");
        navigateTo("StartBattle.fxml");
    }

    @FXML
    private void handleExitGame() {
        System.out.println("Botón 'Exit Game' presionado. Cerrando aplicación...");
        Stage stage = (Stage) exitGameButton.getScene().getWindow();
        stage.close();
    }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/" + fxmlFile));
            Scene newScene = new Scene(loader.load());
            Stage stage = (Stage) teamManagementButton.getScene().getWindow();
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
