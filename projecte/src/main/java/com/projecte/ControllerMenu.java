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

public class ControllerMenu implements Initializable {

    // Elementos FXML
    @FXML private ImageView fondoMenu;
    @FXML private Button backButton, teamManagementButton, battleHistoryButton, startBattleButton, exitGameButton;
    @FXML private ImageView playerImage, trainerImage;
    @FXML private Label playerLevel, playerPoints, playerName, battlesWon, consecutiveWins, pokemonCaught;

    private static String selectedTrainerName = "";
    private static String selectedTrainerImage = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Cargar imagen de fondo
            if (fondoMenu != null) {
                fondoMenu.setImage(new Image(getClass().getResource("/img/bg/fondoMenu.png").toExternalForm()));
            }

            // Cargar estadísticas
            loadBattleStatistics();

            // Mostrar información del entrenador
            updateTrainerInfo();

        } catch (Exception e) {
            showError("Error inicializando menú", e.getMessage());
            e.printStackTrace();
        }
    }

    
    private void loadBattleStatistics() {
        try {
            AppData db = AppData.getInstance();

            // 1. Verificar que el nombre del entrenador esté establecido
            if (selectedTrainerName == null || selectedTrainerName.isEmpty()) {
                throw new IllegalStateException("Nombre de entrenador no establecido");
            }

            // Escapar comillas simples para SQL
            String escapedName = selectedTrainerName.replace("'", "''");

            // 2. Obtener estadísticas de batallas (usando campos existentes)
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

            // 3. Obtener racha máxima (usando campos existentes)
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

            // 4. Pokémon capturados (no existe tabla Pokemon en tu esquema actual)
            // Si tienes una tabla Pokemon, ajusta la consulta. Si no, deja el valor en 0.
            int caught = 0;
            pokemonCaught.setText("Pokémon: " + caught);

            // 5. Calcular nivel y puntos
            int level = won / 5 + 1;
            int points = won * 100;

            playerLevel.setText("Nivel: " + level);
            playerPoints.setText("Puntos: " + points);

        } catch (Exception e) {
            showError("Error cargando estadísticas", e.getMessage());
            e.printStackTrace();
        }
    }

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

    private void updateTrainerInfo() {
        try {
            if (selectedTrainerName != null && !selectedTrainerName.isEmpty()) {
                playerName.setText("Entrenador: " + selectedTrainerName);
            }
            if (selectedTrainerImage != null && !selectedTrainerImage.isEmpty()) {
                trainerImage.setImage(new Image(getClass().getResource(selectedTrainerImage).toExternalForm()));
            }
        } catch (Exception e) {
            showError("Error actualizando info de entrenador", e.getMessage());
        }
    }

    // Métodos de navegación
    @FXML
    private void handleTeamManagement() { navigateTo("viewManagement.fxml"); }

    @FXML
    private void handleBattleHistory() { navigateTo("viewBattleHistory.fxml"); }

    @FXML
    private void handleStartBattle() { navigateTo("pokemonSelection.fxml"); }

    @FXML
    private void handleExitGame() {
        ((Stage) exitGameButton.getScene().getWindow()).close();
    }

    @FXML
    private void handleBackButton() {
        navigateTo("trainerSelection.fxml");
    }

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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void setTrainerInfo(String name, String imagePath) {
        selectedTrainerName = name != null ? name : "";
        selectedTrainerImage = imagePath != null ? imagePath : "";
    }
}