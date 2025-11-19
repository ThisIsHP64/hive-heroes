package Scripts.VolcanoMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class HasBlueEmeraldScriptAction extends ScriptAction {

    @Override
    public ScriptState execute() {
        if (BeeStats.hasRedEmerald()) {
            map.getFlagManager().setFlag("hasRedEmerald");
            return ScriptState.COMPLETED;
        }
        return ScriptState.COMPLETED;
    }

    

}
