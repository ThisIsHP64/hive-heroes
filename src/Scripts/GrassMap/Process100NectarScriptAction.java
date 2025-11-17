package Scripts.GrassMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.HiveManager;

public class Process100NectarScriptAction extends ScriptAction {
    

    @Override
    public ScriptState execute() {

        if (HiveManager.getNectar() >= 100) {
            map.getFlagManager().setFlag("hasCollected100Nectar");
            return ScriptState.COMPLETED;
        }

        return ScriptState.COMPLETED;
    }
}
