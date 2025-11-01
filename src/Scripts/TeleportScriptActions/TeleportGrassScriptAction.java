package Scripts.TeleportScriptActions;

import Game.GameState;
import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.TeleportManager;

public class TeleportGrassScriptAction extends ScriptAction {
    
    @Override
    public ScriptState execute() {
        TeleportManager.setCurrentScreen(GameState.GRASSLEVEL);
        return ScriptState.COMPLETED;
    }
}
