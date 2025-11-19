package Scripts.VolcanoMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class ClaimRedEmeraldScriptAction extends ScriptAction {
    
    @Override
    public ScriptState execute() {
        BeeStats.setHasRedEmerald(true);
        map.getFlagManager().setFlag("hasRedEmerald");
        return ScriptState.COMPLETED;
    }
}
