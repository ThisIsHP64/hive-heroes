package Scripts.TeleportScriptActions;

import Game.GameState;
import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.TeleportManager;

public class TeleportHiveScriptAction extends ScriptAction {
    
    @Override
    public ScriptState execute() {
        TeleportManager.setCurrentScreen(GameState.HIVELEVEL);
        return ScriptState.COMPLETED;
    }
}
