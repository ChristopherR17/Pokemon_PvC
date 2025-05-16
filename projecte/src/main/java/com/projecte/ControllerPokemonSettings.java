package com.projecte;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ControllerPokemonSettings implements Initializable {

    @FXML
    private ImageView pokemonImageView;

    @FXML
    private Label labelPokemonName;

    @FXML
    private TextField nicknameField;

    @FXML
    private Button backButton;

    // Disponibilidad inicial de ítems
    private int xAttackCount = 0;
    private int xDefenseCount = 0;

    @FXML
    private Label xDefenseAvailable; // Define the xDefenseAvailable label
    @FXML
    private Label bottleCapAvailable; // Define the bottleCapAvailable label
    private int bottleCapCount = 0; // Initialize bottleCapCount

    private String selectedPokemonName; // Almacena el nombre del Pokémon seleccionado
    private String selectedPokemonImage; // Almacena la imagen del Pokémon seleccionado

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Retrasar la ejecución hasta que la escena esté completamente cargada
        javafx.application.Platform.runLater(() -> {
            // Obtener los datos del Stage
            Stage stage = (Stage) pokemonImageView.getScene().getWindow();
            String[] pokemonData = (String[]) stage.getUserData();

            // Configurar el nombre y la imagen del Pokémon
            selectedPokemonName = pokemonData[0];
            selectedPokemonImage = pokemonData[1];

            labelPokemonName.setText(selectedPokemonName);
            pokemonImageView.setImage(new Image(getClass().getResource(selectedPokemonImage).toExternalForm()));

            // Actualizar la disponibilidad inicial de ítems
            updateItemAvailability();
        });
    }

    /**
     * Actualiza la visualización de la cantidad disponible de los ítems X Defense y Bottle Cap.
     */
    private void updateItemAvailability() {
        xDefenseAvailable.setText("Available: " + xDefenseCount);
        bottleCapAvailable.setText("Available: " + bottleCapCount);
    }

    /**
     * Maneja el evento de cambio de apodo del Pokémon.
     * Si el campo está vacío, no realiza ninguna acción.
     * Si hay texto, actualiza el apodo en la base de datos y muestra una alerta de éxito.
     */
    @FXML
    private void handleNicknameChange() {
        String newNickname = nicknameField.getText().trim();

        if (newNickname.isEmpty()) {
            return;
        }

        AppData db = AppData.getInstance();
        db.update(String.format("UPDATE Pokemon SET nickname = '%s' WHERE name = '%s'", newNickname, selectedPokemonName));

        showAlert("Apodo cambiado exitosamente a \"" + newNickname + "\".", Alert.AlertType.INFORMATION);
    }

    /**
     * Muestra una alerta en pantalla con el mensaje y tipo especificados.
     * @param message Mensaje a mostrar.
     * @param type Tipo de alerta (INFORMATION, ERROR, etc.).
     */
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Error" : "Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * Maneja el evento del botón "Back".
     * Cambia la escena a la vista de gestión de Pokémon.
     */
    @FXML
    private void handleBackButton() {
        System.out.println("Botón Back presionado. Navegando atrás...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/viewManagement.fxml")); // Ajusta la ruta según tu proyecto.
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
