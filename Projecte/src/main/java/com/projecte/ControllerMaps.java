package com.projecte;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerMaps {

    @FXML
    private ImageView map1Preview, map2Preview, map3Preview, map4Preview, map5Preview;

    @FXML
    private Button selectMap1Button, selectMap2Button, selectMap3Button, selectMap4Button, selectMap5Button;

    private static String selectedMapImage = "";

    @FXML
    public void initialize() {
        // Configurar imágenes de los mapas
        map1Preview.setImage(new Image(getClass().getResource("/img/mapVolcano.png").toExternalForm()));
        map2Preview.setImage(new Image(getClass().getResource("/img/mapForest.png").toExternalForm()));
        map3Preview.setImage(new Image(getClass().getResource("/img/mapGlacier.png").toExternalForm()));
        map4Preview.setImage(new Image(getClass().getResource("/img/mapDesert.png").toExternalForm()));
        map5Preview.setImage(new Image(getClass().getResource("/img/mapOcean.png").toExternalForm()));
    }

    @FXML
    private void handleSelectMap1() {
        selectedMapImage = "/img/maps/choiceMaps/choice1.jpg";
        loadBattleView();
    }

    @FXML
    private void handleSelectMap2() {
        selectedMapImage = "/img/maps/choiceMaps/choice2.jpg";
        loadBattleView();
    }

    @FXML
    private void handleSelectMap3() {
        selectedMapImage = "/img/maps/choiceMaps/choice3.jpg";
        loadBattleView();
    }

    @FXML
    private void handleSelectMap4() {
        selectedMapImage = "/img/maps/choiceMaps/choice4.jpg";
        loadBattleView();
    }

    @FXML
    private void handleSelectMap5() {
        selectedMapImage = "/img/maps/choiceMaps/choice5.jpg";
        loadBattleView();
    }

    private void loadBattleView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/battleView.fxml"));
            Scene battleScene = new Scene(loader.load());
            //ControllerBattle controllerBattle = loader.getController();

            // Enviar el mapa seleccionado a la vista de batalla
            //controllerBattle.setBattleBackground(selectedMapImage);

            Stage stage = (Stage) selectMap1Button.getScene().getWindow();
            stage.setScene(battleScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar battleView.fxml");
        }
    }
}
