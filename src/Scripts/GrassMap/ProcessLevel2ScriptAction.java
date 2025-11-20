package Scripts.GrassMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class ProcessLevel2ScriptAction extends ScriptAction {
    
    @Override
    public ScriptState execute() {
        if (BeeStats.getCurrentLevel() >= 2) {
            map.getFlagManager().setFlag("isLevel2");
            return ScriptState.COMPLETED;
        }
        
        return ScriptState.COMPLETED;
    }
}
