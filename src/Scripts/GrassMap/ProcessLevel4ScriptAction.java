package Scripts.GrassMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class ProcessLevel4ScriptAction extends ScriptAction {
    
    @Override
    public ScriptState execute() {
        if (BeeStats.getCurrentLevel() >= 4) {
            map.getFlagManager().setFlag("isLevel4");
            return ScriptState.COMPLETED;
        }
        
        return ScriptState.COMPLETED;
    }
}
