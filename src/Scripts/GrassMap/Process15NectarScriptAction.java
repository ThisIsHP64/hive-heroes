package Scripts.GrassMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.HiveManager;

public class Process15NectarScriptAction extends ScriptAction {
    

    @Override
    public ScriptState execute() {

        if (HiveManager.getNectar() >= 15) {
            map.getFlagManager().setFlag("hasCollected15Nectar");
            return ScriptState.COMPLETED;
        }

        return ScriptState.COMPLETED;
    }
}
