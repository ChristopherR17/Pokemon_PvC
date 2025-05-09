package com.projecte;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
    private Button backButton;

    @FXML
    private Button teamManagementButton;

    @FXML
    private Button battleHistoryButton;

    @FXML
    private Button startBattleButton;

    @FXML
    private Button exitGameButton;

    @FXML
    private ImageView playerImage;

    @FXML
    private Label playerLevel;

    @FXML
    private Label playerPoints;

    @FXML
    private Label playerName;

    @FXML
    private ImageView trainerImage;

    @FXML
    private Label battlesWon;

    @FXML
    private Label consecutiveWins;

    @FXML
    private Label pokemonCaught;

    private Player currentPlayer;

    private static String selectedTrainerName = "";
    private static String selectedTrainerImage = "";



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentPlayer = new Player("2", 2345, 15, 3, 5);

        if (fondoMenu != null) {
            fondoMenu.setImage(new javafx.scene.image.Image(getClass().getResource("/img/fondoMenu.png").toExternalForm()));
        } else {
            System.err.println("fondoMenu no está inicializado correctamente.");
        }

        updatePlayerStats();
        updateTrainerInfo();
    }

    private void updatePlayerStats() {
        playerLevel.setText("Level: " + currentPlayer.getLevel());
        playerPoints.setText("Points: " + currentPlayer.getPoints());
        battlesWon.setText("Battles Won: " + currentPlayer.getBattlesWon());
        consecutiveWins.setText("Max Wins: " + currentPlayer.getConsecutiveWins());
        pokemonCaught.setText("Pokémon: " + currentPlayer.getPokemonCaught());
    }

    @FXML
    private void handleTeamManagement() {
        System.out.println("Boton 'Team Management' presionado. Navegando...");
        navigateTo("viewManagement.fxml");
    }

    @FXML
    private void handleBattleHistory() {
        System.out.println("Boton 'Battle History' presionado. Navegando...");
        navigateTo("BattleHistory.fxml");
    }

    @FXML
    private void handleStartBattle() {
        System.out.println("Boton 'Start New Battle' presionado. Navegando...");
        navigateTo("viewMaps.fxml");
    }

    @FXML
    private void handleExitGame() {
        System.out.println("Boton 'Exit Game' presionado. Cerrando aplicación...");
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

    private void updateTrainerInfo() {
        if (!selectedTrainerName.isEmpty()) {
            playerName.setText("Trainer: " + selectedTrainerName);
        }
        if (!selectedTrainerImage.isEmpty()) {
            trainerImage.setImage(new Image(getClass().getResource(selectedTrainerImage).toExternalForm()));
        }
    }

    // Método para recibir la información del entrenador desde otra vista
    public static void setTrainerInfo(String name, String imagePath) {
        selectedTrainerName = name;
        selectedTrainerImage = imagePath;
    }

    @FXML
    private void handleBackButton() {
    System.out.println("Botón 'Back' presionado. Navegando hacia la vista anterior...");
    // Aquí implementa la navegación a la vista anterior
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/trainerSelection.fxml")); 
        Scene newScene = new Scene(loader.load());
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(newScene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Error al cargar la vista anterior: " + e.getMessage());
    }
    }
}
