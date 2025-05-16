package com.projecte;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Controlador para la selección de mapas en el juego.
 * Permite al usuario navegar entre diferentes mapas, ver su imagen y nombre,
 * confirmar la selección del mapa para la batalla o volver a la pantalla anterior.
 */
public class ControllerMaps {

    @FXML 
    private ImageView mapImageView;      // Imagen del mapa actual
    @FXML 
    private Label mapNameLabel;          // Nombre del mapa actual
    @FXML 
    private Button prevMapButton, nextMapButton, confirmMapButton; // Botones de navegación y confirmación
    @FXML
    private Button backButton;           // Botón para volver a la pantalla anterior

    // Lista de nombres y rutas de mapas. Asegúrate de que las rutas (mapPaths) sean correctas
    private ArrayList<String> mapNames = new ArrayList<>(Arrays.asList(
        "Bosque Verde", "Cueva Oscura", "Montaña Roca", "Nieve Azulona", "Gimnasio Elite"
    ));
    private ArrayList<String> mapPaths = new ArrayList<>(Arrays.asList(
        "/img/maps/choiceMaps/choice1.jpg",
        "/img/maps/choiceMaps/choice2.jpg",
        "/img/maps/choiceMaps/choice3.jpg",
        "/img/maps/choiceMaps/choice4.jpg",
        "/img/maps/choiceMaps/choice5.jpg"
    ));
    
    private int currentIndex = 0; // Índice del mapa actualmente seleccionado

    /**
     * Inicializa la vista de selección de mapas mostrando el primer mapa.
     */
    @FXML
    public void initialize() {
        updateMapView(currentIndex);
    }

    /**
     * Actualiza la vista con el nombre y la imagen del mapa seleccionado.
     * También actualiza el estado de los botones de navegación.
     * @param index Índice del mapa a mostrar.
     */
    private void updateMapView(int index) {
        String mapName = mapNames.get(index);
        mapNameLabel.setText(mapName);

        String imagePath = mapPaths.get(index);
        URL imgUrl = getClass().getResource(imagePath);
        if (imgUrl != null) {
            mapImageView.setImage(new Image(imgUrl.toExternalForm()));
        } else {
            System.err.println("No se encontró la imagen: " + imagePath);
        }

        prevMapButton.setDisable(index == 0);
        nextMapButton.setDisable(index == mapNames.size() - 1);
    }

    /**
     * Maneja el evento del botón "Siguiente mapa".
     * Muestra el siguiente mapa en la lista si existe.
     */
    @FXML
    private void handleNextMap() {
        if (currentIndex < mapNames.size() - 1) {
            currentIndex++;
            updateMapView(currentIndex);
        }
    }

    /**
     * Maneja el evento del botón "Mapa anterior".
     * Muestra el mapa anterior en la lista si existe.
     */
    @FXML
    private void handlePrevMap() {
        if (currentIndex > 0) {
            currentIndex--;
            updateMapView(currentIndex);
        }
    }

    /**
     * Maneja el evento del botón "Confirmar mapa".
     * Guarda el mapa seleccionado en el DTO y navega a la vista de batalla.
     */
    @FXML
    private void handleConfirmMap() {
        try {
            String selectedMap = mapNames.get(currentIndex);
            System.out.println("Mapa seleccionado: " + selectedMap);

            // Cargar la vista de batalla.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/ViewBattleAttack.fxml"));
            System.out.println("Cargando archivo FXML desde: " + getClass().getResource("/assets/ViewBattleAttack.fxml"));
            Scene battleView = new Scene(loader.load());

            // Se carga la escena de batalla.
            Stage stage = (Stage) confirmMapButton.getScene().getWindow();
            BattleDataDTO battleData = (BattleDataDTO) stage.getUserData(); // Obtener los datos de batalla
            battleData.setBattleMap(selectedMap); // Establecer el mapa seleccionado
            stage.setUserData(battleData); // Guardar el mapa seleccionado en el Stage

            stage.setScene(battleView); 
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista de batalla: " + e.getMessage());
        }
    }

    /**
     * Maneja el evento del botón "Back".
     * Navega de regreso a la pantalla de selección de Pokémon.
     */
    @FXML
    private void handleBackButton() {
        System.out.println("Botón 'Back' presionado. Navegando hacia la vista anterior...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/pokemonSelection.fxml")); 
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