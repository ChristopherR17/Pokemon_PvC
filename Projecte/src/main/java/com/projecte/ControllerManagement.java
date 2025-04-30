package com.projecte;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

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

    // Índice del Pokémon actual
    private int currentIndex = 0;

    // Datos estáticos de ejemplo
    private final String[] pokemonNames = {"Pikachu", "Charizard", "Bulbasaur"};
    private final String[] pokemonNicknames = {"Sparky", "Flame", "Leafy"};
    private final String[] pokemonStats = {
        "HP: 100, Attack: 50, Stamina: 75",
        "HP: 150, Attack: 70, Stamina: 85",
        "HP: 80, Attack: 40, Stamina: 60"
    };
    private final String[] pokemonImages = {
        "/img/pikachu.png",
        "/img/charizard.png",
        "/img/bulbasaur.png"
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
}
