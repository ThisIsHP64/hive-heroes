package Scripts.VolcanoMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class ClaimBlueEmeraldScriptAction extends ScriptAction {
    
    @Override
    public ScriptState execute() {
        BeeStats.setHasBlueEmerald(true);
        map.getFlagManager().setFlag("hasBlueEmerald");
        return ScriptState.COMPLETED;
    }
}
