package Scripts.GrassMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class ProcessLevelUpScriptAction extends ScriptAction {
    

    @Override
    public ScriptState execute() {
        switch (BeeStats.getCurrentLevel()) {
            case 2:
                map.getFlagManager().setFlag("isLevel2");
                return ScriptState.COMPLETED;
            case 3:
                map.getFlagManager().setFlag("isLevel3");
                return ScriptState.COMPLETED;
            case 4:
                map.getFlagManager().setFlag("isLevel4");
                return ScriptState.COMPLETED;
        }
        return ScriptState.COMPLETED;
    }
}
