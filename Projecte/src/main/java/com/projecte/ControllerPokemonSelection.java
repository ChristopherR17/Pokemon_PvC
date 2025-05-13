package com.projecte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controlador para la selección de Pokémon en la interfaz gráfica.
 * Permite al jugador seleccionar 3 Pokémon de una lista disponible.
 */
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

    // Lista de Pokémon disponibles desde la base de datos
    private ArrayList<HashMap<String, Object>> availablePokemons = new ArrayList<>();
    private int currentIndex = 0;

    /**
     * Método de inicialización que se ejecuta al cargar la vista.
     * Carga la lista de Pokémon disponibles desde la base de datos y actualiza la interfaz.
     */
    @FXML
    public void initialize() {
        // Cargar Pokémon desde la base de datos
        getPokemonList();

        updatePokemonSelection();
        loadUnlockedPokemon();
        if (!availablePokemons.isEmpty()) {
            updatePokemonView(currentIndex);
        } else {
            selectedPokemonName.setText("No hay Pokémon desbloqueados!");
            selectPokemonButton.setDisable(true);
        }
    }

    /**
     * Obtiene la lista de Pokémon disponibles desde la base de datos.
     * Los datos incluyen el nombre y la ruta de la imagen frontal.
     */
    private void getPokemonList() {
        AppData db = AppData.getInstance();
        ArrayList<HashMap<String, Object>> llista = db.query("SELECT name, image_front FROM Pokemon");
        for (HashMap<String, Object> row : llista) {
            String name = (String) row.get("name");
            String imagePath = (String) row.get("image_front");
            this.availablePokemon.add(new Pokemon(name, imagePath));
        }
        
    }

    /**
     * Actualiza la interfaz gráfica para mostrar el Pokémon actualmente seleccionado.
     */
    private void updatePokemonSelection() {
        Pokemon currentPokemon = availablePokemon.get(currentPokemonIndex);
        selectedPokemonImage.setImage(new Image(getClass().getResource(currentPokemon.getImagePath()).toExternalForm()));
        selectedPokemonName.setText(currentPokemon.getName());
    }

    @FXML
    private void handleNextPokemon() {
        if (currentIndex < availablePokemons.size() - 1) {
            currentIndex++;
            updatePokemonView(currentIndex);
        }
    }

    @FXML
    private void handlePrevPokemon() {
        if (currentIndex > 0) {
            currentIndex--;
            updatePokemonView(currentIndex);
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

    private void loadUnlockedPokemon() {
        try {
            AppData db = AppData.getInstance();
            availablePokemons = db.query("SELECT * FROM Pokemon WHERE unlocked = true");

            if (availablePokemons == null || availablePokemons.isEmpty()) {
                System.err.println("No se encontraron Pokémon desbloqueados.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar los Pokémon desbloqueados: " + e.getMessage());
        }
    }


    private void updatePokemonView(int index) {
        if (availablePokemons.isEmpty()) {
            selectedPokemonName.setText("No hay Pokémon desbloqueados!");
            selectPokemonButton.setDisable(true);
            return;
        }

        selectedPokemonName.setText(availablePokemons.get(index).get("name").toString());
        String imagePath = availablePokemons.get(index).get("image_front").toString();

        if (imagePath != null && !imagePath.isEmpty()) {
            selectedPokemonImage.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
        } else {
            System.err.println("Error: La ruta de la imagen está vacía o no existe.");
            selectedPokemonImage.setImage(new Image(getClass().getResource("/assets/gif/pikachu.gif").toExternalForm()));
        }

        prevPokemonButton.setDisable(index == 0);
        nextPokemonButton.setDisable(index == availablePokemons.size() - 1);
    }

}
