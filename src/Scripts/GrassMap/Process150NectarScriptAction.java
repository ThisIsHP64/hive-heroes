package Scripts.GrassMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.HiveManager;

public class Process150NectarScriptAction extends ScriptAction {
    

    @Override
    public ScriptState execute() {

        if (HiveManager.getNectar() >= 150) {
            map.getFlagManager().setFlag("hasCollected150Nectar");
            return ScriptState.COMPLETED;
        }

        return ScriptState.COMPLETED;
    }
}
