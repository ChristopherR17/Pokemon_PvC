package com.projecte;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPokemonSettings implements Initializable {

    @FXML
    private ImageView pokemonImageView;

    @FXML
    private Label labelPokemonName;

    @FXML
    private Button backButton;

    // Disponibilidad inicial de ítems
    private int xAttackCount = 0;
    private int xDefenseCount = 0;

    private String pokemonName = "Pikachu"; // Define the Pokémon name
    @FXML
    private Label xDefenseAvailable; // Define the xDefenseAvailable label
    @FXML
    private Label bottleCapAvailable; // Define the bottleCapAvailable label
    private int bottleCapCount = 0; // Initialize bottleCapCount

    public void initialize(URL location, ResourceBundle resources) {
        // Configurar el nombre y la imagen del Pokémon
        labelPokemonName.setText(pokemonName);
        pokemonImageView.setImage(new Image(getClass().getResource("/gif/pikachu.gif").toExternalForm()));

        // Actualizar la disponibilidad inicial de ítems
        updateItemAvailability();
    }

    private void updateItemAvailability() {
        xDefenseAvailable.setText("Available: " + xDefenseCount);
        bottleCapAvailable.setText("Available: " + bottleCapCount);
    }

    @FXML
    private void handleBackButton() {
        System.out.println("Botón Back presionado. Navegando atrás...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/viewManagement.fxml")); // Ajusta la ruta según tu proyecto.
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
