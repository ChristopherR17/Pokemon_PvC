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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Controlador del menú principal del juego.
 * Muestra la información del entrenador, estadísticas y permite navegar a otras vistas principales.
 */
public class ControllerMenu implements Initializable {

    // Elementos FXML de la interfaz
    @FXML private ImageView fondoMenu;
    @FXML private Button backButton, teamManagementButton, battleHistoryButton, startBattleButton, exitGameButton;
    @FXML private ImageView playerImage, trainerImage;
    @FXML private Label playerLevel, playerPoints, playerName, battlesWon, consecutiveWins, pokemonCaught;

    // Variables estáticas para almacenar el nombre e imagen del entrenador seleccionado
    private static String selectedTrainerName = "";
    private static String selectedTrainerImage = "";

    /**
     * Inicializa la vista del menú principal.
     * Carga la imagen de fondo, estadísticas y la información del entrenador.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Cargar imagen de fondo
            if (fondoMenu != null) {
                fondoMenu.setImage(new Image(getClass().getResource("/img/bg/fondoMenu.png").toExternalForm()));
            }

            // Cargar estadísticas del jugador
            loadBattleStatistics();

            // Mostrar información del entrenador
            updateTrainerInfo();

        } catch (Exception e) {
            showError("Error inicializando menú", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga y muestra las estadísticas del jugador desde la base de datos.
     * Incluye batallas ganadas, jugadas, racha máxima y Pokémon capturados.
     */
    private void loadBattleStatistics() {
        try {
            AppData db = AppData.getInstance();

            // Verificar que el nombre del entrenador esté establecido
            if (selectedTrainerName == null || selectedTrainerName.isEmpty()) {
                throw new IllegalStateException("Nombre de entrenador no establecido");
            }

            // Escapar comillas simples para SQL
            String escapedName = selectedTrainerName.replace("'", "''");

            // Obtener estadísticas de batallas
            String battlesQuery = "SELECT " +
                                  "SUM(CASE WHEN result = 'Ganado' THEN 1 ELSE 0 END) as battles_won, " +
                                  "COUNT(*) as battles_played " +
                                  "FROM BattleHistory " +
                                  "WHERE trainer = '" + escapedName + "'";

            ArrayList<HashMap<String, Object>> battlesResult = db.query(battlesQuery);

            int won = 0;
            int played = 0;

            if (!battlesResult.isEmpty()) {
                HashMap<String, Object> stats = battlesResult.get(0);
                won = safeGetInt(stats, "battles_won");
                played = safeGetInt(stats, "battles_played");
            }

            battlesWon.setText("Batallas: " + won + "/" + played);

            // Obtener racha máxima de victorias
            String streakQuery =
                "WITH Ganadas AS (" +
                "  SELECT battle_id, trainer, result, battle_date, " +
                "    ROW_NUMBER() OVER (ORDER BY battle_date) - " +
                "    ROW_NUMBER() OVER (PARTITION BY result ORDER BY battle_date) as grp " +
                "  FROM BattleHistory " +
                "  WHERE trainer = '" + escapedName + "' AND result = 'Ganado'" +
                ") " +
                "SELECT COALESCE(MAX(streak), 0) as max_streak FROM (" +
                "  SELECT COUNT(*) as streak FROM Ganadas GROUP BY grp" +
                ")";

            int maxStreak = 0;
            ArrayList<HashMap<String, Object>> streakResult = db.query(streakQuery);
            if (!streakResult.isEmpty()) {
                maxStreak = safeGetInt(streakResult.get(0), "max_streak");
            }
            consecutiveWins.setText("Racha máxima: " + maxStreak);

            // Obtener cantidad de Pokémon capturados
            String pokemonQuery = "SELECT COUNT(*) as caught FROM Pokemon WHERE propietario = '" + escapedName + "'";
            int caught = 3; // Valor por defecto si la consulta falla
            ArrayList<HashMap<String, Object>> pokemonResult = db.query(pokemonQuery);
            if (!pokemonResult.isEmpty()) {
                caught = safeGetInt(pokemonResult.get(0), "caught");
            }
            pokemonCaught.setText("Pokémon: " + caught);

            // Calcular nivel y puntos
            int level = won / 5 + 1;
            int points = won * 100;

            playerLevel.setText("Nivel: " + level);
            playerPoints.setText("Puntos: " + points);

        } catch (Exception e) {
            showError("Error cargando estadísticas", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene de forma segura un valor entero de un HashMap.
     * @param map Mapa con los datos.
     * @param key Clave a buscar.
     * @return Valor entero o 0 si no existe o hay error.
     */
    private int safeGetInt(HashMap<String, Object> map, String key) {
        try {
            Object value = map.get(key);
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Actualiza la información visual del entrenador en el menú.
     */
    private void updateTrainerInfo() {
        try {
            if (selectedTrainerName != null && !selectedTrainerName.isEmpty()) {
                playerName.setText(selectedTrainerName);
            }
            if (selectedTrainerImage != null && !selectedTrainerImage.isEmpty()) {
                trainerImage.setImage(new Image(getClass().getResource(selectedTrainerImage).toExternalForm()));
            }
        } catch (Exception e) {
            showError("Error actualizando info de entrenador", e.getMessage());
        }
    }

    // Métodos de navegación entre vistas principales

    /**
     * Navega a la vista de gestión de equipo.
     */
    @FXML
    private void handleTeamManagement() { navigateTo("viewManagement.fxml"); }

    /**
     * Navega a la vista de historial de batallas.
     */
    @FXML
    private void handleBattleHistory() { navigateTo("viewBattleHistory.fxml"); }

    /**
     * Navega a la vista de selección de Pokémon para batalla.
     */
    @FXML
    private void handleStartBattle() { navigateTo("pokemonSelection.fxml"); }

    /**
     * Cierra la aplicación.
     */
    @FXML
    private void handleExitGame() {
        ((Stage) exitGameButton.getScene().getWindow()).close();
    }

    /**
     * Navega a la vista de selección de entrenador.
     */
    @FXML
    private void handleBackButton() {
        navigateTo("trainerSelection.fxml");
    }

    /**
     * Cambia la escena actual a la especificada por el archivo FXML.
     * @param fxmlFile Nombre del archivo FXML a cargar.
     */
    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/" + fxmlFile));
            Stage stage = (Stage) teamManagementButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            showError("Error de navegación", "No se pudo cargar " + fxmlFile + ": " + e.getMessage());
        }
    }

    /**
     * Muestra una alerta de error en pantalla.
     * @param title Título de la alerta.
     * @param message Mensaje de la alerta.
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Establece el nombre y la imagen del entrenador seleccionado.
     * Se utiliza para transferir estos datos entre escenas.
     * @param name Nombre del entrenador.
     * @param imagePath Ruta de la imagen del entrenador.
     */
    public static void setTrainerInfo(String name, String imagePath) {
        selectedTrainerName = name != null ? name : "";
        selectedTrainerImage = imagePath != null ? imagePath : "";
    }
}