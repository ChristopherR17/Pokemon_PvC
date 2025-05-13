package com.projecte;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;

public class ControllerMaps {

    @FXML private ImageView mapImageView;
    @FXML private Label mapNameLabel;
    @FXML private Button prevMapButton, nextMapButton, confirmMapButton;

    private ArrayList<String> mapNames = new ArrayList<>(Arrays.asList("Bosque Verde", "Cueva Oscura", "Montaña Roca", "Nieve Azulona", "Gimnasio Elite"));
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

    private void updateMapView(int index) {
        mapNameLabel.setText(mapNames.get(index));

        String imagePath = mapPaths.get(index);
        mapImageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));

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
            System.out.println("Mapa seleccionado para la batalla: " + selectedMap);

            // Guardar el mapa en la base de datos (opcional)
            AppData db = AppData.getInstance();
            db.update("INSERT INTO BattleHistory (trainer, map, result) VALUES ('Jugador', '" + selectedMap + "', 'En progreso')");

            // Cargar la vista de batalla
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/ViewBattleAttack.fxml"));
            Parent battleView = loader.load();

            // Pasar datos al controlador de batalla
            ControllerBattleAttack battleController = loader.getController();
            battleController.setBattleMap(selectedMap);


            Stage stage = (Stage) confirmMapButton.getScene().getWindow();
            stage.setScene(new Scene(battleView));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar la batalla: " + e.getMessage());
        }
    }

}
