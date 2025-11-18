package Scripts.HiveMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class HasEmeraldScriptAction extends ScriptAction {
    @Override
    public ScriptState execute() {
        if (BeeStats.hasEmerald()) {
            map.getFlagManager().setFlag("hasEmerald");
            return ScriptState.COMPLETED;
        }
        return ScriptState.COMPLETED;
    }
}
