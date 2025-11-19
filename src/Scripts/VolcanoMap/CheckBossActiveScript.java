package Scripts.VolcanoMap;

import Level.Script;
import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class CheckBossActiveScript extends ScriptAction {

    @Override
    public ScriptState execute() {
        if (BeeStats.isBossActive()) {
            map.getFlagManager().setFlag("bossActive");
            return ScriptState.COMPLETED;
        }

        return ScriptState.COMPLETED;
    }

}
