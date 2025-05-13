package com.projecte;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class ControllerMaps {

    @FXML 
    private ImageView mapImageView;
    @FXML 
    private Label mapNameLabel;
    @FXML 
    private Button prevMapButton, nextMapButton, confirmMapButton;

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
    
    private int currentIndex = 0;

    @FXML
    public void initialize() {
        updateMapView(currentIndex);
    }

    // Actualiza la vista con el nombre y la imagen del mapa seleccionado.
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

    @FXML
    private void handleNextMap() {
        if (currentIndex < mapNames.size() - 1) {
            currentIndex++;
            updateMapView(currentIndex);
        }
    }

    @FXML
    private void handlePrevMap() {
        if (currentIndex > 0) {
            currentIndex--;
            updateMapView(currentIndex);
        }
    }

    @FXML
    private void handleConfirmMap() {
        try {
            String selectedMap = mapNames.get(currentIndex);
            System.out.println("Mapa seleccionado: " + selectedMap);

            // Opcional: Guardar el mapa seleccionado en la base de datos.
            // IMPORTANTE: La columna 'result' solo acepta 'Ganado' o 'Perdido'. 
            // Por ello, es preferible inserción de este registro una vez concluida la batalla.
            // AppData db = AppData.getInstance();
            // db.update("INSERT INTO BattleHistory (trainer, map, result) VALUES ('Jugador', '" + selectedMap + "', 'Ganado')");

            // Cargar la vista de batalla.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/ViewBattleAttack.fxml"));
            Parent battleView = loader.load();

            // Obtener el controlador de la batalla y pasarle el mapa seleccionado.
            ControllerBattleAttack battleController = loader.getController();
            battleController.setBattleMap(selectedMap);

            // Se carga la escena de batalla.
            Stage stage = (Stage) confirmMapButton.getScene().getWindow();
            stage.setScene(new Scene(battleView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista de batalla: " + e.getMessage());
        }
    }
}
