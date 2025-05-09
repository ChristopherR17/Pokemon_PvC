package com.projecte;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ControllerStart implements Initializable {

    @FXML
    private ImageView fondoStart;

    @FXML
    private ImageView pokemon1;

    @FXML
    private ImageView pokemon2;

    @FXML
    private ImageView pokemon3;

    @FXML
    private Button openGameButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fondoStart.setImage(new Image(getClass().getResource("/img/bg/fondoStart.jpg").toExternalForm()));
        pokemon1.setImage(new Image(getClass().getResource("/gif/charmander.gif").toExternalForm()));
        pokemon2.setImage(new Image(getClass().getResource("/gif/Pikachu2.gif").toExternalForm()));
        pokemon3.setImage(new Image(getClass().getResource("/gif/Mew.gif").toExternalForm()));

        System.out.println("Imagenes y GIFs cargados correctamente al iniciar la vista.");
    }

    @FXML
    public void handleOpenGame() {
        System.out.println("Botón 'Open Game' presionado.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/trainerSelection.fxml"));
            Scene menuScene = new Scene(loader.load());
            
            Stage stage = (Stage) openGameButton.getScene().getWindow();
            stage.setScene(menuScene);
            stage.show();
    
            System.out.println("Vista del menú cargada con éxito.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar trainerSelection.fxml: " + e.getMessage());
        }
    }
    
}
