package Scripts.GrassMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class ProcessLevel3ScriptAction extends ScriptAction {
    
    @Override
    public ScriptState execute() {
        if (BeeStats.getCurrentLevel() >= 3) {
            map.getFlagManager().setFlag("isLevel3");
            return ScriptState.COMPLETED;
        }
        
        return ScriptState.COMPLETED;
    }
}
