package Scripts.VolcanoMap;

import Effects.ScreenFX;
import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class ClaimBlueEmeraldScriptAction extends ScriptAction {
    
    @Override
    public ScriptState execute() {
        BeeStats.setHasBlueEmerald(true);
        map.getFlagManager().setFlag("hasBlueEmerald");
        ScreenFX.start(ScreenFX.Effect.DARKEN, Integer.MAX_VALUE, 0.05f);

        return ScriptState.COMPLETED;
    }
}
