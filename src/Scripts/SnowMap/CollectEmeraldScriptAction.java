package Scripts.SnowMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class CollectEmeraldScriptAction extends ScriptAction{
    @Override
    public ScriptState execute() {
        BeeStats.setHasEmerald(true);
        return ScriptState.COMPLETED;
    }
}
