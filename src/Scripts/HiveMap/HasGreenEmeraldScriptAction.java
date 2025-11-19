package Scripts.HiveMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class HasGreenEmeraldScriptAction extends ScriptAction {
    @Override
    public ScriptState execute() {
        if (BeeStats.hasGreenEmerald()) {
            map.getFlagManager().setFlag("hasGreenEmerald");
            return ScriptState.COMPLETED;
        }
        return ScriptState.COMPLETED;
    }
}
