package com.projecte;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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

/**
 * Controlador para la selección de entrenador.
 * Permite al usuario elegir un entrenador predefinido o personalizar el nombre,
 * y gestiona la persistencia y recuperación de datos del jugador en la base de datos.
 */
public class ControllerTrainerSelection implements Initializable {

    @FXML
    private ImageView trainerImage; // Imagen del entrenador seleccionado

    @FXML
    private Label trainerName; // Nombre del entrenador seleccionado

    @FXML
    private TextField customTrainerName; // Campo para nombre personalizado

    @FXML
    private Button prevTrainerButton; // Botón para entrenador anterior

    @FXML
    private Button nextTrainerButton; // Botón para entrenador siguiente

    @FXML
    private Button confirmTrainerButton; // Botón para confirmar selección

    private int currentTrainerIndex = 0; // Índice del entrenador actual
    private final String[] trainerNames = {"Ash Ketchum", "Misty", "Brock", "Serena", "Red"}; // Nombres de entrenadores
    private final String[] trainerImages = {"/img/trainers/ash.png", "/img/trainers/misty.png", "/img/trainers/brock.png", "/img/trainers/serena.png", "/img/trainers/red.png"}; // Imágenes

    /**
     * Inicializa la vista de selección de entrenador.
     * Configura los botones y muestra el entrenador inicial.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTrainer(currentTrainerIndex);

        prevTrainerButton.setOnAction(_ -> showPreviousTrainer());
        nextTrainerButton.setOnAction(_ -> showNextTrainer());
        confirmTrainerButton.setOnAction(_ -> confirmTrainerSelection());
        // confirmTrainerButton.setOnAction(_ -> handleConfirmTrainer());
    }

    /**
     * Actualiza la información visual del entrenador mostrado.
     * @param index Índice del entrenador a mostrar.
     */
    private void updateTrainer(int index) {
        trainerName.setText(trainerNames[index]);
        trainerImage.setImage(new Image(getClass().getResource(trainerImages[index]).toExternalForm()));
    }

    /**
     * Muestra el entrenador anterior en la lista, si existe.
     */
    private void showPreviousTrainer() {
        if (currentTrainerIndex > 0) {
            currentTrainerIndex--;
            updateTrainer(currentTrainerIndex);
        }
    }

    /**
     * Muestra el siguiente entrenador en la lista, si existe.
     */
    private void showNextTrainer() {
        if (currentTrainerIndex < trainerNames.length - 1) {
            currentTrainerIndex++;
            updateTrainer(currentTrainerIndex);
        }
    }

    /**
     * Confirma la selección del entrenador.
     * Si el nombre ya existe en la base de datos, recupera los datos.
     * Si no existe, crea un nuevo registro en la base de datos.
     * Luego, carga la vista del menú principal.
     */
    private void confirmTrainerSelection() {
        String chosenName = getChosenName();
        String chosenTrainerImage = trainerImages[currentTrainerIndex]; // Imagen del entrenador elegido

        if (chosenName == null || chosenName.trim().isEmpty()) {
            System.err.println("Error: chosenName es null o está vacío.");
            return;
        }

        AppData db = AppData.getInstance();
        db.connect("../data/pokemons.sqlite");   

        Player currentPlayer = Player.getInstance();

        /*
         * Comprobar si el nombre ya existe en la base de datos.
         * Si existe, recuperamos el nombre y los datos de la imagen, sino lo creamos.
         */
        String sql = String.format("SELECT * FROM Player WHERE name = '%s';", chosenName);
        ArrayList<HashMap<String, Object>> llista = db.query(sql);
        if (!llista.isEmpty()) {
            // El nombre ya existe, así que lo recuperamos
            HashMap<String, Object> trainerData = llista.get(0);
            int id = (int) trainerData.get("id");
            currentPlayer.setId(id);
            chosenName = (String) trainerData.get("name");
            currentPlayer.setName(chosenName);
            chosenTrainerImage = (String) trainerData.get("image_path");
        } else {
            // El nombre no existe, así que lo creamos
            sql = String.format(
                "INSERT INTO Player (name, image_path) VALUES ('%s', '%s');", chosenName, chosenTrainerImage
            );
            
            db.update(sql);

            // Después de la inserción, recupera el id generado automáticamente
            sql = "SELECT last_insert_rowid();";  // Devuelve el id generado automáticamente
            ArrayList<HashMap<String, Object>> result = db.query(sql);
            if (!result.isEmpty()) {
                int newId = (int) result.get(0).get("last_insert_rowid()");
                currentPlayer.setId(newId); // Establecer el id generado automáticamente
            }
        }

        System.out.println("Trainer selected: " + chosenName);
        
        // Enviar los datos al controlador del menú
        ControllerMenu.setTrainerInfo(chosenName, chosenTrainerImage);
    
        changeScene("/assets/viewMenu.fxml");
    }

    /**
     * Obtiene el nombre seleccionado por el usuario.
     * Si el campo de nombre personalizado está vacío, devuelve el nombre del entrenador predefinido.
     * @return Nombre elegido por el usuario.
     */
    private String getChosenName() {
        return customTrainerName.getText().isEmpty() ? trainerName.getText() : customTrainerName.getText();
    }
    
    /**
     * Maneja el evento de confirmación del entrenador (no usado actualmente).
     * Carga la vista de inicio.
     */
    @FXML
    private void handleConfirmTrainer() {
        System.out.println("El botón de confirmación ha sido presionado.");

        // Si el usuario ha ingresado un nombre, lo usa; de lo contrario, toma el predefinido
        String chosenName = getChosenName();

        System.out.println("Trainer selected: " + chosenName);

        changeScene("/assets/viewStart.fxml");
    }

    /**
     * Cambia la escena actual a la especificada por el archivo FXML.
     * @param fxmlPath Ruta del archivo FXML de la nueva escena.
     */
    private void changeScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene newScene = new Scene(loader.load());
            Stage stage = (Stage) confirmTrainerButton.getScene().getWindow();
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading " + fxmlPath);
        }
    }

}