package com.projecte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BattleHistory {

    /** Obtiene el historial de batallas desde `AppData` */
    public static List<BattleHistoryEntry> getBattleHistory() {
        List<BattleHistoryEntry> battleList = new ArrayList<>();

        try {
            AppData db = AppData.getInstance();
            ArrayList<HashMap<String, Object>> battles = db.query("SELECT trainer, map, result FROM BattleHistory");

            for (HashMap<String, Object> battle : battles) {
                battleList.add(new BattleHistoryEntry(
                    battle.get("trainer").toString(),
                    battle.get("map").toString(),
                    battle.get("result").toString()
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al obtener el historial de batallas: " + e.getMessage());
        }

        return battleList;
    }
}
