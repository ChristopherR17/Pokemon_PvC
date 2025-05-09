package com.projecte;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ControllerTrainerSelection implements Initializable {

    @FXML
    private ImageView trainerImage;

    @FXML
    private Label trainerName;

    @FXML
    private TextField customTrainerName;

    @FXML
    private Button prevTrainerButton;

    @FXML
    private Button nextTrainerButton;

    @FXML
    private Button confirmTrainerButton;

    private int currentTrainerIndex = 0;
    private final String[] trainerNames = {"Ash Ketchum", "Misty", "Brock", "Serena", "Red"};
    private final String[] trainerImages = {"/img/trainers/ash.png", "/img/trainers/misty.png", "/img/trainers/brock.png", "/img/trainers/serena.png", "/img/trainers/red.png"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTrainer(currentTrainerIndex);

        prevTrainerButton.setOnAction(_ -> showPreviousTrainer());
        nextTrainerButton.setOnAction(_ -> showNextTrainer());
        confirmTrainerButton.setOnAction(_ -> confirmTrainerSelection());
        // confirmTrainerButton.setOnAction(_ -> handleConfirmTrainer());

    }

    private void updateTrainer(int index) {
        trainerName.setText(trainerNames[index]);
        trainerImage.setImage(new Image(getClass().getResource(trainerImages[index]).toExternalForm()));
    }

    private void showPreviousTrainer() {
        if (currentTrainerIndex > 0) {
            currentTrainerIndex--;
            updateTrainer(currentTrainerIndex);
        }
    }

    private void showNextTrainer() {
        if (currentTrainerIndex < trainerNames.length - 1) {
            currentTrainerIndex++;
            updateTrainer(currentTrainerIndex);
        }
    }

    private void confirmTrainerSelection() {
        String chosenName = customTrainerName.getText().isEmpty() ? trainerName.getText() : customTrainerName.getText();
        String chosenImage = trainerImages[currentTrainerIndex]; // Imagen del entrenador elegido

        if (chosenName == null || chosenName.trim().isEmpty()) {
            System.err.println("Error: chosenName es null o está vacío.");
            return;
        }

        //Guardamos el nombre en la base de datos
        AppData db = AppData.getInstance();
        db.connect("../data/pokemons.sqlite");   
        String sql = String.format(
            "INSERT INTO Player (name) VALUES ('%s');",chosenName
        );
        db.update(sql);

        System.out.println("Trainer selected: " + chosenName);
        
        // Enviar los datos al controlador del menú
        ControllerMenu.setTrainerInfo(chosenName, chosenImage);
    
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/viewMenu.fxml"));
            Scene newScene = new Scene(loader.load());
            Stage stage = (Stage) confirmTrainerButton.getScene().getWindow();
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading viewMenu.fxml");
        }
    }
    

    @FXML
    private void handleConfirmTrainer() {
        System.out.println("El botón de confirmación ha sido presionado.");

        // Si el usuario ha ingresado un nombre, lo usa; de lo contrario, toma el predefinido
        String chosenName = customTrainerName.getText().isEmpty() ? trainerName.getText() : customTrainerName.getText();

        System.out.println("Trainer selected: " + chosenName);

        try {

            // Carga la siguiente escena (ajusta la ruta si es diferente en tu proyecto)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/gameLobby.fxml"));
            Scene newScene = new Scene(loader.load());
            Stage stage = (Stage) confirmTrainerButton.getScene().getWindow();
            stage.setScene(newScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading gameLobby.fxml");
        }
    }

}
