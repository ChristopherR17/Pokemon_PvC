package com.projecte;

import java.io.Serializable;

public class BattleDataDTO implements Serializable {
    private String battleMap;
    private Pokemon[] playerTeam;

    public BattleDataDTO(String battleMap, Pokemon[] playerTeam) {
        this.battleMap = battleMap;
        this.playerTeam = playerTeam;
    }

    public String getBattleMap() {
        return battleMap;
    }

    public Pokemon[] getPlayerTeam() {
        return playerTeam;
    }

    public void setBattleMap(String battleMap) {
        this.battleMap = battleMap;
    }

    public void setPlayerTeam(Pokemon[] playerTeam) {
        this.playerTeam = playerTeam;
    }

}