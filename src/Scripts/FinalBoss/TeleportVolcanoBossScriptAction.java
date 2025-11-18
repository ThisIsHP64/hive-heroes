package Scripts.FinalBoss;

import Enemies.*;
import Engine.WeatherManager;
import Game.GameState;
import Level.MapEntityStatus;
import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.EnemySpawner;
import StaticClasses.HordeManager;
import StaticClasses.TeleportManager;

public class TeleportVolcanoBossScriptAction extends ScriptAction {

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub
        super.cleanup();
    }

    @Override
    public ScriptState execute() {
        TeleportManager.setCurrentGameState(GameState.VOLCANOLEVEL);        

        WeatherManager.GLOBAL.enableOverrideMode();
        WeatherManager.GLOBAL.setRedRain(true);
        return ScriptState.COMPLETED;
    }

    @Override
    public void setup() {

        HordeManager.setIsRunning(false);
        HordeManager.stopHorde(map);

        EnemySpawner.setEnabled(false);

        for (var npc : map.getNPCs()) {
            if (npc instanceof Spider || npc instanceof Bat || npc instanceof Skull) {
                npc.lock();
            }
        }
    }

}