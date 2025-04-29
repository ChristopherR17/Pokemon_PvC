package com.projecte;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPokemonSettings implements Initializable {

    @FXML
    private ComboBox<String> pokemonSelector;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> abilitySelector;

    @FXML
    private Button saveButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Rellenar el selector de Pokémon
        pokemonSelector.getItems().addAll("Pikachu", "Charmander", "Bulbasaur", "Squirtle");

        // Rellenar el selector de habilidades
        abilitySelector.getItems().addAll("Thunderbolt", "Flamethrower", "Solar Beam", "Hydro Pump");

        // Añadir funcionalidad básica al botón de guardar
        saveButton.setOnAction(event -> {
            String selectedPokemon = pokemonSelector.getValue();
            String newName = nameField.getText();
            String selectedAbility = abilitySelector.getValue();

            if (selectedPokemon != null && newName != null && selectedAbility != null) {
                System.out.println("Settings saved:");
                System.out.println("Pokemon: " + selectedPokemon);
                System.out.println("New Name: " + newName);
                System.out.println("Ability: " + selectedAbility);
            } else {
                System.out.println("Please complete all fields before saving.");
            }
        });
    }
}
