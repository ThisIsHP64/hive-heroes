package Scripts.VolcanoMap;

import Level.MapEntityStatus;
import Level.ScriptState;
import ScriptActions.ScriptAction;

public class AddRedEmeraldScriptAction extends ScriptAction {
    @Override
    public ScriptState execute() {
        
        // gets the red emerald by its ID and sets it to active
        map.getNPCById(0).setMapEntityStatus(MapEntityStatus.ACTIVE);

        return ScriptState.COMPLETED;
    }
}
