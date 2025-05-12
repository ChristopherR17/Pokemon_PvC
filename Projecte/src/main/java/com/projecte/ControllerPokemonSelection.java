package com.projecte;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerPokemonSelection {

    @FXML
    private ImageView selectedPokemonImage, chosenPokemon1, chosenPokemon2, chosenPokemon3;

    @FXML
    private Label selectedPokemonName;

    @FXML
    private Button prevPokemonButton, nextPokemonButton, selectPokemonButton, confirmSelectionButton;

    private final List<Pokemon> availablePokemon = new ArrayList<>();
    private final Pokemon[] selectedPokemon = new Pokemon[3];
    private int currentPokemonIndex = 0;
    private int selectedCount = 0;

    @FXML
    public void initialize() {
        // Lista estática de Pokémon disponibles
        availablePokemon.add(new Pokemon("Charizard", "/img/charizard.png"));
        availablePokemon.add(new Pokemon("Blastoise", "/img/blastoise.png"));
        availablePokemon.add(new Pokemon("Venusaur", "/img/venusaur.png"));
        availablePokemon.add(new Pokemon("Gengar", "/img/gengar.png"));
        availablePokemon.add(new Pokemon("Pikachu", "/img/pikachu.png"));
        availablePokemon.add(new Pokemon("Dragonite", "/img/dragonite.png"));

        updatePokemonSelection();
    }

    private void updatePokemonSelection() {
        Pokemon currentPokemon = availablePokemon.get(currentPokemonIndex);
        selectedPokemonImage.setImage(new Image(getClass().getResource(currentPokemon.getImagePath()).toExternalForm()));
        selectedPokemonName.setText(currentPokemon.getName());
    }

    @FXML
    private void handlePrevPokemon() {
        if (currentPokemonIndex > 0) {
            currentPokemonIndex--;
            updatePokemonSelection();
        }
    }

    @FXML
    private void handleNextPokemon() {
        if (currentPokemonIndex < availablePokemon.size() - 1) {
            currentPokemonIndex++;
            updatePokemonSelection();
        }
    }

    @FXML
    private void handleSelectPokemon() {
        if (selectedCount < 3) {
            selectedPokemon[selectedCount] = availablePokemon.get(currentPokemonIndex);
            updateChosenPokemonDisplay();
            selectedCount++;
        }
    }

    private void updateChosenPokemonDisplay() {
        if (selectedPokemon[0] != null) {
            chosenPokemon1.setImage(new Image(getClass().getResource(selectedPokemon[0].getImagePath()).toExternalForm()));
        }
        if (selectedPokemon[1] != null) {
            chosenPokemon2.setImage(new Image(getClass().getResource(selectedPokemon[1].getImagePath()).toExternalForm()));
        }
        if (selectedPokemon[2] != null) {
            chosenPokemon3.setImage(new Image(getClass().getResource(selectedPokemon[2].getImagePath()).toExternalForm()));
        }
    }

    @FXML
    private void handleConfirmSelection() {
        if (selectedCount == 3) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/battleView.fxml"));
                Scene battleScene = new Scene(loader.load());
                ControllerBattle controllerBattle = loader.getController();

                // Pasar los Pokémon seleccionados a la batalla
                controllerBattle.setPlayerPokemon(selectedPokemon[0], selectedPokemon[1], selectedPokemon[2]);

                Stage stage = (Stage) confirmSelectionButton.getScene().getWindow();
                stage.setScene(battleScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error al cargar battleView.fxml");
            }
        } else {
            System.out.println("Selecciona exactamente 3 Pokémon antes de continuar.");
        }
    }
}
