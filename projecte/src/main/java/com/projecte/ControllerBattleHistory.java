package com.projecte;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ControllerBattleHistory {

    @FXML private Label titleLabel;
    @FXML private ImageView backgroundImage;
    @FXML private TableView<BattleHistoryEntry> battleHistoryTable;
    @FXML private TableColumn<BattleHistoryEntry, String> trainerColumn;
    @FXML private TableColumn<BattleHistoryEntry, String> mapColumn;
    @FXML private TableColumn<BattleHistoryEntry, String> resultColumn;

    @FXML
    public void initialize() {
        configureTableColumns();
        loadBattleHistory();
    }

    /** Configura correctamente las columnas de la tabla */
    private void configureTableColumns() {
        trainerColumn.setCellValueFactory(cellData -> cellData.getValue().trainerProperty());
        mapColumn.setCellValueFactory(cellData -> cellData.getValue().mapProperty());
        resultColumn.setCellValueFactory(cellData -> cellData.getValue().resultProperty());
    }

    /** Carga el historial de batallas desde la base de datos */
    private void loadBattleHistory() {
        ObservableList<BattleHistoryEntry> battleList = FXCollections.observableArrayList();
        battleList.addAll(BattleHistory.getBattleHistory());
        battleHistoryTable.setItems(battleList);
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
