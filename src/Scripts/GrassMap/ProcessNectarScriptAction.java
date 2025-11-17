package Scripts.GrassMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.HiveManager;

public class ProcessNectarScriptAction extends ScriptAction {
    

    @Override
    public ScriptState execute() {
        
        if (HiveManager.getNectar() >= 150) {
            map.getFlagManager().setFlag("hasCollected150Nectar");
            // map.getFlagManager().setFlag("hasCollected100Nectar");
            // map.getFlagManager().setFlag("hasCollected50Nectar");
            // map.getFlagManager().setFlag("hasCollected15Nectar");

            return ScriptState.COMPLETED;
        }

        if (HiveManager.getNectar() >= 100) {
            map.getFlagManager().setFlag("hasCollected100Nectar");
            // map.getFlagManager().setFlag("hasCollected50Nectar");
            // map.getFlagManager().setFlag("hasCollected15Nectar");
            return ScriptState.COMPLETED;
        }
        
        if (HiveManager.getNectar() >= 50) {
            map.getFlagManager().setFlag("hasCollected50Nectar");
            // map.getFlagManager().setFlag("hasCollected15Nectar");
                return ScriptState.COMPLETED;
        }

        if (HiveManager.getNectar() >= 15) {
            map.getFlagManager().setFlag("hasCollected15Nectar");
            return ScriptState.COMPLETED;
        }

        return ScriptState.RUNNING;
    }
}
