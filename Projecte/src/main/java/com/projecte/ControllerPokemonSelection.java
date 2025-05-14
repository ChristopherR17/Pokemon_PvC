package com.projecte;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Button;


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
    private Button prevPokemonButton, nextPokemonButton, selectPokemonButton, confirmSelectionButton, backButton;

    private final List<Pokemon> availablePokemon = new ArrayList<>();
    private final Pokemon[] selectedPokemon = new Pokemon[3];
    private int currentPokemonIndex = 0;
    private int selectedCount = 0;

    /**
     * Método de inicialización que se ejecuta al cargar la vista.
     * Carga la lista de Pokémon disponibles desde la base de datos y actualiza la interfaz.
     */
    @FXML
    public void initialize() {
        // Cargar Pokémon desde la base de datos
        getPokemonList();

        // Actualizar la selección inicial
        updatePokemonSelection();

        // Deshabilitar el botón de confirmación al inicio
        updateConfirmButtonState();
    }

    /**
     * Obtiene la lista de Pokémon disponibles desde la base de datos.
     * Los datos incluyen el nombre y la ruta de la imagen frontal.
     */
    private void getPokemonList() {
        AppData db = AppData.getInstance();
        ArrayList<HashMap<String, Object>> llista = db.query("SELECT name, image_front FROM Pokemon WHERE unlocked = 1");
        for (HashMap<String, Object> row : llista) {
            String name = (String) row.get("name");
            String imagePath = (String) row.get("image_front");
            this.availablePokemon.add(new Pokemon(name, imagePath));
        }
        
    }

    @FXML
    private void handleSelectPokemon() {
        if (selectedCount < 3) {
            // Añadir el Pokémon seleccionado al array de seleccionados
            selectedPokemon[selectedCount] = availablePokemon.get(currentPokemonIndex);

            // Eliminar el Pokémon seleccionado de la lista de disponibles
            availablePokemon.remove(currentPokemonIndex);

            // Ajustar el índice actual si es necesario
            if (currentPokemonIndex >= availablePokemon.size()) {
                currentPokemonIndex = availablePokemon.size() - 1; // Ajustar al último índice válido
            }

            // Actualizar la interfaz gráfica
            updateChosenPokemonDisplay();

            // Incrementar el contador de seleccionados
            selectedCount++;

            // Actualizar la selección actual si quedan Pokémon disponibles
            if (!availablePokemon.isEmpty()) {
                updatePokemonSelection();
            } else {
                // Si no quedan Pokémon disponibles, deshabilitar el botón de selección
                selectPokemonButton.setDisable(true);
            }

            // Actualizar el estado de los botones de navegación
            updateNavigationButtons();

            // Actualizar el estado del botón de confirmación
            updateConfirmButtonState();
        }
    }

    @FXML
    private void handlePrevPokemon() {
        if (currentPokemonIndex > 0) {
            currentPokemonIndex--;
            updatePokemonSelection();
            updateNavigationButtons();
        }
    }

    @FXML
    private void handleNextPokemon() {
        if (currentPokemonIndex < availablePokemon.size() - 1) {
            currentPokemonIndex++;
            updatePokemonSelection();
            updateNavigationButtons();
        }
    }

    /**
     * Actualiza el estado de los botones de navegación según la posición actual y la lista de Pokémon disponibles.
     */
    private void updateNavigationButtons() {
        // Desactivar el botón "Anterior" si no hay Pokémon a la izquierda
        prevPokemonButton.setDisable(currentPokemonIndex <= 0);

        // Desactivar el botón "Siguiente" si no hay Pokémon a la derecha
        nextPokemonButton.setDisable(currentPokemonIndex >= availablePokemon.size() - 1);

        // Desactivar el botón "Seleccionar" si no hay Pokémon disponibles
        selectPokemonButton.setDisable(availablePokemon.isEmpty());
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
                System.out.println("Intentando cargar la vista de mapas...");

                // Cargar el archivo FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/viewMaps.fxml"));
                System.out.println("Cargando archivo FXML desde: " + getClass().getResource("/com/projecte/assets/viewMaps.fxml"));
                Scene battleScene = new Scene(loader.load());

                // Guardar los Pokémon seleccionados en el Stage
                Stage stage = (Stage) confirmSelectionButton.getScene().getWindow();
                if (stage == null) {
                    System.err.println("No se pudo obtener el Stage.");
                    return;
                }
                stage.setUserData(selectedPokemon);

                // Cambiar a la nueva escena
                stage.setScene(battleScene);
                stage.show();
                System.out.println("Vista de mapas cargada correctamente.");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error al cargar viewMaps.fxml: " + e.getMessage());
            }
        } else {
            System.out.println("Selecciona exactamente 3 Pokémon antes de continuar.");
        }
    }

    /**
     * Actualiza el estado del botón de confirmación según el número de Pokémon seleccionados.
     */
    private void updateConfirmButtonState() {
        confirmSelectionButton.setDisable(selectedCount < 3);
    }

    /**
     * Actualiza la interfaz gráfica para mostrar el Pokémon actualmente seleccionado.
     */
    private void updatePokemonSelection() {
        if (!availablePokemon.isEmpty()) {
            Pokemon currentPokemon = availablePokemon.get(currentPokemonIndex);
            selectedPokemonImage.setImage(new Image(getClass().getResource(currentPokemon.getImagePath()).toExternalForm()));
            selectedPokemonName.setText(currentPokemon.getName());
        } else {
            selectedPokemonImage.setImage(null);
            selectedPokemonName.setText("");
        }
        updateNavigationButtons();
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
