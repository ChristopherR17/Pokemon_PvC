package com.projecte;

import javafx.beans.property.SimpleStringProperty;

/** Clase para representar cada entrada en el historial de batalla */
public class BattleHistoryEntry {
    private final SimpleStringProperty trainer;
    private final SimpleStringProperty map;
    private final SimpleStringProperty result;

    public BattleHistoryEntry(String trainer, String map, String result) {
        this.trainer = new SimpleStringProperty(trainer);
        this.map = new SimpleStringProperty(map);
        this.result = new SimpleStringProperty(result);
    }

    public String getTrainer() { return trainer.get(); }
    public String getMap() { return map.get(); }
    public String getResult() { return result.get(); }

    public SimpleStringProperty trainerProperty() { return trainer; }
    public SimpleStringProperty mapProperty() { return map; }
    public SimpleStringProperty resultProperty() { return result; }
}
