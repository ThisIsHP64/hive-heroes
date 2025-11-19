package Scripts.VolcanoMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class SetBossActiveScriptAction extends ScriptAction {

    @Override
    public ScriptState execute() {
        BeeStats.setBossActive(true);
        return ScriptState.COMPLETED;
    }
}
