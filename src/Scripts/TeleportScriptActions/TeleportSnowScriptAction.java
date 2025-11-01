package Scripts.TeleportScriptActions;

import Game.GameState;
import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.TeleportManager;

public class TeleportSnowScriptAction extends ScriptAction {
    
    @Override
    public ScriptState execute() {
        TeleportManager.setCurrentScreen(GameState.SNOWLEVEL);
        return ScriptState.COMPLETED;
    }
}