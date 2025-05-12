package com.projecte;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ControllerBattleHistory {

    @FXML
    private TableView<HashMap<String, Object>> battleHistoryTable;
    @FXML
    private TableColumn<HashMap<String, Object>, String> trainerColumn;
    @FXML
    private TableColumn<HashMap<String, Object>, String> mapColumn;
    @FXML
    private TableColumn<HashMap<String, Object>, String> resultColumn;

    @FXML
    public void initialize() {
        loadBattleHistory();
    }

    private void loadBattleHistory() {
        try {
            AppData db = AppData.getInstance();
            ArrayList<HashMap<String, Object>> battles = db.query("SELECT trainer, map, result FROM BattleHistory");
            battleHistoryTable.getItems().setAll(battles);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar el historial de batallas: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/viewMenu.fxml"));
            Scene newScene = new Scene(loader.load());
            Stage stage = (Stage) battleHistoryTable.getScene().getWindow();
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar el menú: " + e.getMessage());
        }
    }
}
