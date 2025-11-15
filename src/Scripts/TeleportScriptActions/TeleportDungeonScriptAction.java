package Scripts.TeleportScriptActions;

import Game.GameState;
import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.TeleportManager;

public class TeleportDungeonScriptAction extends ScriptAction {
    
    @Override
    public ScriptState execute() {
        TeleportManager.setCurrentGameState(GameState.DUNGEONLEVEL);
        // TeleportManager.setCurrentScreen(GameState.DUNGEONLEVEL);
        return ScriptState.COMPLETED;
    }
}