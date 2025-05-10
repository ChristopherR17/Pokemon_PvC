package com.projecte;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ControllerManagement implements Initializable {

    @FXML
    private ImageView pokemonImageView;

    @FXML
    private Label pokemonNameLabel;

    @FXML
    private Label pokemonNicknameLabel;

    @FXML
    private Label pokemonStatsLabel;

    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button editPokemonButton;

    @FXML
    private Button backButton;

    // Índice del Pokémon actual
    private int currentIndex = 0;

    // Datos estáticos de ejemplo
    private final String[] pokemonNames = {"Venusaur", "Charizard", "Squirtle"};
    private final String[] pokemonNicknames = {"Leafy", "Flame", "Droplet"};
    private final String[] pokemonStats = {
        "HP: 100, Attack: 50, Stamina: 75",
        "HP: 150, Attack: 70, Stamina: 85",
        "HP: 80, Attack: 40, Stamina: 60"
    };
    private final String[] pokemonImages = {
        "/gif/sprites/front/venusaurFront.gif",
        "/gif/sprites/front/Charizard.gif",
        "/gif/sprites/front/blastoiseFront.gif"
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Mostrar el primer Pokémon al inicializar
        showPokemon(currentIndex);

        // Configurar eventos de los botones
        prevButton.setOnAction(_ -> showPreviousPokemon());
        nextButton.setOnAction(_ -> showNextPokemon());
    }

    private void showPokemon(int index) {
        // Actualizar los datos del Pokémon actual
        pokemonNameLabel.setText("Name: " + pokemonNames[index]);
        pokemonNicknameLabel.setText("Nickname: " + pokemonNicknames[index]);
        pokemonStatsLabel.setText("Stats: " + pokemonStats[index]);
        pokemonImageView.setImage(new Image(getClass().getResource(pokemonImages[index]).toExternalForm()));
    }

    private void showPreviousPokemon() {
        if (currentIndex > 0) {
            currentIndex--;
            showPokemon(currentIndex);
        }
    }

    private void showNextPokemon() {
        if (currentIndex < pokemonNames.length - 1) {
            currentIndex++;
            showPokemon(currentIndex);
        }
    }
    
    @FXML
    private void handleEditPokemon() {
    System.out.println("Botón 'Edit Pokémon' presionado. Navegando a ViewPokemonSettings...");
    // Aquí se puede implementar la navegación. Por ejemplo:
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/viewPokemonSettings.fxml"));
        Scene newScene = new Scene(loader.load());
        Stage stage = (Stage) editPokemonButton.getScene().getWindow();
        stage.setScene(newScene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Error al cargar viewPokemonSettings.fxml: " + e.getMessage());
    }
    }

    @FXML
    private void handleBackButton() {
    System.out.println("Botón 'Back' presionado. Navegando hacia la vista anterior...");
    // Aquí implementa la navegación a la vista anterior
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/viewMenu.fxml")); 
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
