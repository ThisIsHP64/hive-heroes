package Scripts.TeleportScriptActions;

import Game.GameState;
import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.TeleportManager;

public class TeleportVolcanoScriptAction extends ScriptAction {
    
    @Override
    public ScriptState execute() {
        TeleportManager.setCurrentScreen(GameState.VOLCANOLEVEL);
        return ScriptState.COMPLETED;
    }
}