package Scripts.FinalBoss;

import Game.GameState;
import Level.ScriptState;
import ScriptActions.ScriptAction;
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
        return ScriptState.COMPLETED;
    }

    @Override
    public void setup() {
        // TODO Auto-generated method stub
        super.setup();
    }

}