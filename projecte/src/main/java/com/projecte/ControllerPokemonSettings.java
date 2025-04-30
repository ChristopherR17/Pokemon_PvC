package com.projecte;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPokemonSettings implements Initializable {

    @FXML
    private TextField nicknameField;

    @FXML
    private Button saveNicknameButton;

    @FXML
    private Button xAttackButton;

    @FXML
    private Button xDefenseButton;

    @FXML
    private Button bottleCapButton;

    @FXML
    private Label xAttackAvailable;

    @FXML
    private Label xDefenseAvailable;

    @FXML
    private Label bottleCapAvailable;

    @FXML
    private ImageView pokemonImageView;

    @FXML
    private Label labelPokemonName;

    // Disponibilidad inicial de ítems
    private int xAttackCount = 0;
    private int xDefenseCount = 0;
    private int bottleCapCount = 0;

    // Nombre y sobrenombre del Pokémon
    private String pokemonName = "Name";
    private String nickname = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar el nombre y la imagen del Pokémon
        labelPokemonName.setText(pokemonName);
        pokemonImageView.setImage(new Image(getClass().getResource("/img/pikachu.png").toExternalForm()));

        // Actualizar la disponibilidad inicial de ítems
        updateItemAvailability();

        // Configurar el botón para guardar el sobrenombre
        saveNicknameButton.setOnAction(_ -> saveNickname());

        // Configurar los botones de ítems
        xAttackButton.setOnAction(_ -> useXAttack());
        xDefenseButton.setOnAction(_ -> useXDefense());
        bottleCapButton.setOnAction(_ -> useBottleCap());
    }

    private void saveNickname() {
        nickname = nicknameField.getText();
        if (nickname.isEmpty()) {
            System.out.println("Nickname cannot be empty.");
        } else {
            System.out.println("Nickname saved: " + nickname);
        }
    }

    private void useXAttack() {
        if (xAttackCount > 0) {
            xAttackCount--;
            System.out.println("X Attack used! Remaining: " + xAttackCount);
            updateItemAvailability();
        } else {
            System.out.println("No X Attack available.");
        }
    }

    private void useXDefense() {
        if (xDefenseCount > 0) {
            xDefenseCount--;
            System.out.println("X Defense used! Remaining: " + xDefenseCount);
            updateItemAvailability();
        } else {
            System.out.println("No X Defense available.");
        }
    }

    private void useBottleCap() {
        if (bottleCapCount > 0) {
            bottleCapCount--;
            System.out.println("Bottle Cap used! Remaining: " + bottleCapCount);
            updateItemAvailability();
        } else {
            System.out.println("No Bottle Cap available.");
        }
    }

    private void updateItemAvailability() {
        xAttackAvailable.setText("Available: " + xAttackCount);
        xDefenseAvailable.setText("Available: " + xDefenseCount);
        bottleCapAvailable.setText("Available: " + bottleCapCount);
    }
}
