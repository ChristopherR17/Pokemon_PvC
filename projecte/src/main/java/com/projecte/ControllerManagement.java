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

/**
 * Controlador para la gestión de los Pokémon del jugador.
 * Permite visualizar los Pokémon, sus apodos, estadísticas y navegar entre ellos.
 * También permite editar un Pokémon seleccionado o volver al menú principal.
 */
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

    /**
     * Inicializa la vista de gestión de Pokémon.
     * Carga los datos desde la base de datos y muestra el primer Pokémon.
     * Configura los eventos de los botones de navegación.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Cargar datos desde la base de datos
        loadPokemonData();

        showPokemon(currentIndex);

        // Configurar eventos de los botones
        prevButton.setOnAction(_ -> showPreviousPokemon());
        nextButton.setOnAction(_ -> showNextPokemon());
    }

    /**
     * Carga los datos de los Pokémon desde la base de datos y los almacena en listas.
     */
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

    /**
     * Muestra la información del Pokémon en la posición indicada.
     * Actualiza los labels, la imagen y el estado de los botones según corresponda.
     * @param index Índice del Pokémon a mostrar.
     */
    private void showPokemon(int index) {
        pokemonNameLabel.setText("Name: " + pokemonNames.get(index));
        pokemonNicknameLabel.setText("Nickname: " + pokemonNicknames.get(index));
        pokemonStatsLabel.setText("Stats: " + pokemonStats.get(index));
        pokemonImageView.setImage(new Image(getClass().getResource(pokemonImages.get(index)).toExternalForm()));

        // Cambia el color del nombre del Pokémon según si está desbloqueado o no
        if (pokemonUnlocked.get(index)) {
            pokemonNameLabel.setStyle("-fx-text-fill: #00cc44; -fx-font-weight: bold;"); // Verde si desbloqueado
            editPokemonButton.setDisable(false); // Activar botón si está desbloqueado
        } else {
            pokemonNameLabel.setStyle("-fx-text-fill: #cc0000; -fx-font-weight: bold;"); // Rojo si bloqueado
            editPokemonButton.setDisable(true); // Desactivar botón si está bloqueado
        }
        // Deshabilitar botones si estamos en el primer o último Pokémon
        prevButton.setDisable(index == 0); // Si es el primer Pokémon, deshabilitar Prev
        nextButton.setDisable(index == pokemonNames.size() - 1); // Si es el último Pokémon, deshabilitar Next
    }
    
    /**
     * Muestra el Pokémon anterior en la lista, si existe.
     */
    private void showPreviousPokemon() {
        if (currentIndex > 0) {
            currentIndex--;
            showPokemon(currentIndex);
        }
    }

    /**
     * Muestra el Pokémon siguiente en la lista, si existe.
     */
    private void showNextPokemon() {
        if (currentIndex < pokemonNames.size() - 1) {
            currentIndex++;
            showPokemon(currentIndex);
        }
    }
    
    /**
     * Maneja el evento del botón "Edit Pokémon".
     * Navega a la vista de configuración del Pokémon seleccionado y pasa los datos necesarios.
     */
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

    /**
     * Maneja el evento del botón "Back".
     * Navega de regreso al menú principal.
     */
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
