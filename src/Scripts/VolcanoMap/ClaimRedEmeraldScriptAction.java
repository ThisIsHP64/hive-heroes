package Scripts.VolcanoMap;

import Effects.ScreenFX;
import Level.ScriptState;
import ScriptActions.ScriptAction;
import StaticClasses.BeeStats;

public class ClaimRedEmeraldScriptAction extends ScriptAction {

    @Override
    public ScriptState execute() {
        BeeStats.setHasRedEmerald(true);
        map.getFlagManager().setFlag("hasRedEmerald");
        ScreenFX.start(ScreenFX.Effect.DARKEN, Integer.MAX_VALUE, 0.05f);

        return ScriptState.COMPLETED;
    }
}
