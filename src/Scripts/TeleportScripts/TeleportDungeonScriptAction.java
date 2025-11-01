package Scripts.TeleportScripts;

import Game.GameState;
import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.TeleportManager;

public class TeleportDungeonScriptAction extends ScriptAction {
    
    @Override
    public ScriptState execute() {
        TeleportManager.setCurrentScreen(GameState.DUNGEONLEVEL);
        return ScriptState.COMPLETED;
    }
}