package Scripts.GrassMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.HiveManager;

public class Process50NectarScriptAction extends ScriptAction {
    

    @Override
    public ScriptState execute() {

        if (HiveManager.getNectar() >= 50) {
            map.getFlagManager().setFlag("hasCollected50Nectar");
            return ScriptState.COMPLETED;
        }

        return ScriptState.COMPLETED;
    }
}
