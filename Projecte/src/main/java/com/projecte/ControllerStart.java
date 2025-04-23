package com.projecte;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControllerStart implements Initializable {

    @FXML
    private ImageView logoImageView; // Vinculado al ImageView en el FXML

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Cargar la imagen manualmente
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/assets/imgs/pokemonLogo.png"));
            logoImageView.setImage(logoImage);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }
    }
}