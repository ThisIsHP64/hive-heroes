package Scripts.SnowMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class CollectGreenEmeraldScriptAction extends ScriptAction{
    @Override
    public ScriptState execute() {
        BeeStats.setHasGreenEmerald(true);
        return ScriptState.COMPLETED;
    }
}
