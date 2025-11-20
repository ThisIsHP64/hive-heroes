package Scripts.HiveMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class RestoreStatsScriptAction extends ScriptAction {
    @Override
    public ScriptState execute() {
        BeeStats.restoreAllStats();
        return ScriptState.COMPLETED;
    }
}
