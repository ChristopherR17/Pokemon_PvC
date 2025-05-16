package com.projecte;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Button;

/**
 * Controlador para la selección de Pokémon en la interfaz gráfica.
 * Permite al jugador seleccionar 3 Pokémon de una lista disponible.
 * Gestiona la navegación, la selección y la confirmación del equipo.
 */
public class ControllerPokemonSelection {

    @FXML
    private ImageView selectedPokemonImage, chosenPokemon1, chosenPokemon2, chosenPokemon3; // Vistas de imágenes de Pokémon

    @FXML
    private Label selectedPokemonName; // Nombre del Pokémon actualmente seleccionado

    @FXML
    private Button prevPokemonButton, nextPokemonButton, selectPokemonButton, confirmSelectionButton, backButton; // Botones de navegación y acción

    private final List<Pokemon> availablePokemon = new ArrayList<>(); // Lista de Pokémon disponibles para seleccionar
    private final Pokemon[] selectedPokemon = new Pokemon[3];         // Array de Pokémon seleccionados por el jugador
    private int currentPokemonIndex = 0;                              // Índice del Pokémon actualmente mostrado
    private int selectedCount = 0;                                    // Número de Pokémon seleccionados

    /**
     * Inicializa la vista de selección de Pokémon.
     * Carga la lista de Pokémon disponibles desde la base de datos y actualiza la interfaz.
     */
    @FXML
    public void initialize() {
        getPokemonList();            // Cargar Pokémon desde la base de datos
        updatePokemonSelection();    // Mostrar el primer Pokémon disponible
        updateConfirmButtonState();  // Deshabilitar el botón de confirmación al inicio
    }

    /**
     * Obtiene la lista de Pokémon disponibles desde la base de datos.
     * Solo se añaden los Pokémon desbloqueados.
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

    /**
     * Maneja el evento de selección de un Pokémon.
     * Añade el Pokémon seleccionado al equipo y lo elimina de la lista de disponibles.
     * Actualiza la interfaz y los botones según corresponda.
     */
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

            // Actualizar la interfaz gráfica de los Pokémon elegidos
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

    /**
     * Maneja el evento del botón "Anterior".
     * Muestra el Pokémon anterior en la lista si existe.
     */
    @FXML
    private void handlePrevPokemon() {
        if (currentPokemonIndex > 0) {
            currentPokemonIndex--;
            updatePokemonSelection();
            updateNavigationButtons();
        }
    }

    /**
     * Maneja el evento del botón "Siguiente".
     * Muestra el Pokémon siguiente en la lista si existe.
     */
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
        prevPokemonButton.setDisable(currentPokemonIndex <= 0);
        nextPokemonButton.setDisable(currentPokemonIndex >= availablePokemon.size() - 1);
        selectPokemonButton.setDisable(availablePokemon.isEmpty());
    }

    /**
     * Actualiza la visualización de los Pokémon ya seleccionados por el jugador.
     */
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

    /**
     * Maneja el evento de confirmación de la selección de Pokémon.
     * Si hay 3 Pokémon seleccionados, guarda el equipo y navega a la vista de mapas.
     */
    @FXML
    private void handleConfirmSelection() {
        if (selectedCount == 3) {
            try {
                System.out.println("Intentando cargar la vista de mapas...");

                // Cargar el archivo FXML de mapas
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/viewMaps.fxml"));
                System.out.println("Cargando archivo FXML desde: " + getClass().getResource("/com/projecte/assets/viewMaps.fxml"));
                Scene battleScene = new Scene(loader.load());

                // Guardar los Pokémon seleccionados en el Stage usando un DTO
                Stage stage = (Stage) confirmSelectionButton.getScene().getWindow();
                BattleDataDTO battleData = new BattleDataDTO(null, null);
                battleData.setPlayerTeam(selectedPokemon);
                stage.setUserData(battleData);

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

    /**
     * Maneja el evento del botón "Back".
     * Navega de regreso al menú principal.
     */
    @FXML
    private void handleBackButton() {
        System.out.println("Botón 'Back' presionado. Navegando hacia la vista anterior...");
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