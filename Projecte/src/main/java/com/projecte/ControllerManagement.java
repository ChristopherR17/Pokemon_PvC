package com.projecte;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ControllerManagement implements Initializable {

    @FXML
    private ImageView pokemonImageView;

    @FXML
    private Label pokemonNameLabel;

    @FXML
    private Label pokemonNicknameLabel;

    @FXML
    private Label pokemonStatsLabel;

    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button editPokemonButton;

    @FXML
    private Button backButton;

    // Índice del Pokémon actual
    private int currentIndex = 0;

    // Listas dinámicas para almacenar los datos de la base de datos
    private final List<String> pokemonNames = new ArrayList<>();
    private final List<String> pokemonNicknames = new ArrayList<>();
    private final List<String> pokemonStats = new ArrayList<>();
    private final List<String> pokemonImages = new ArrayList<>();
    private final List<Boolean> pokemonUnlocked = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Cargar datos desde la base de datos
        loadPokemonData();

        showPokemon(currentIndex);

        // Configurar eventos de los botones
        prevButton.setOnAction(_ -> showPreviousPokemon());
        nextButton.setOnAction(_ -> showNextPokemon());
    }

    private void loadPokemonData() {
        try {
            AppData db = AppData.getInstance();
            ArrayList<HashMap<String, Object>> llista = db.query("SELECT name, nickname, max_hp, attack, stamina, image_front, unlocked FROM Pokemon");
            for (HashMap<String, Object> row : llista) {
                pokemonNames.add((String) row.get("name"));
                pokemonNicknames.add((String) row.get("nickname"));
                pokemonStats.add(String.format("HP: %d, Attack: %d, Stamina: %d",
                        (int) row.get("max_hp"), (int) row.get("attack"), (int) row.get("stamina")));
                pokemonImages.add((String) row.get("image_front"));
                pokemonUnlocked.add((int) row.get("unlocked") == 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar datos de la base de datos: " + e.getMessage());
        }
    }

    private void showPokemon(int index) {
        pokemonNameLabel.setText("Name: " + pokemonNames.get(index));
        pokemonNicknameLabel.setText("Nickname: " + pokemonNicknames.get(index));
        pokemonStatsLabel.setText("Stats: " + pokemonStats.get(index));
        pokemonImageView.setImage(new Image(getClass().getResource(pokemonImages.get(index)).toExternalForm()));

        // Cambia el color del texto según si el Pokémon está desbloqueado o bloqueado
        if (pokemonUnlocked.get(index)) {
            pokemonNameLabel.setStyle("-fx-text-fill: #00cc44; -fx-font-weight: bold;"); // Verde si desbloqueado
        } else {
            pokemonNameLabel.setStyle("-fx-text-fill: #cc0000; -fx-font-weight: bold;"); // Rojo si bloqueado
        }
    }

    private void showPreviousPokemon() {
        if (currentIndex > 0) {
            currentIndex--;
            showPokemon(currentIndex);
        }
    }

    private void showNextPokemon() {
        if (currentIndex < pokemonNames.size() - 1) {
            currentIndex++;
            showPokemon(currentIndex);
        }
    }
    
    @FXML
    private void handleEditPokemon() {
        System.out.println("Botón 'Edit Pokémon' presionado. Navegando a ViewPokemonSettings...");
        // Aquí se puede implementar la navegación. Por ejemplo:
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/viewPokemonSettings.fxml"));
            Scene newScene = new Scene(loader.load());
            
            // Guardar los datos en el Stage
            Stage stage = (Stage) editPokemonButton.getScene().getWindow();
            stage.setUserData(new String[]{pokemonNames.get(currentIndex), pokemonImages.get(currentIndex)});

            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar viewPokemonSettings.fxml: " + e.getMessage());
        }
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
