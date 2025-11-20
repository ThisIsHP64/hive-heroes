package Scripts.FinalBoss;

import Effects.ScreenFX;
import Enemies.*;
import Engine.WeatherManager;
import Game.GameState;
import Level.MapEntityStatus;
import Level.ScriptState;
import ScriptActions.ScriptAction;
import Sound.Music;
import Sound.MusicManager;
import StaticClasses.EnemySpawner;
import StaticClasses.HordeManager;
import StaticClasses.TeleportManager;

public class TeleportVolcanoBossScriptAction extends ScriptAction {

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

    @Override
    public ScriptState execute() {
        ScreenFX.start(ScreenFX.Effect.DARKEN, Integer.MAX_VALUE, 0.10f);

        TeleportManager.setCurrentGameState(GameState.VOLCANOLEVEL);        

        MusicManager.stopAll();
        MusicManager.playLoop(Music.BOSS);
        
        WeatherManager.GLOBAL.enableOverrideMode();
        WeatherManager.GLOBAL.setRedRain(true);
        return ScriptState.COMPLETED;
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub
        super.cleanup();
    }

}