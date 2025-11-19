package Scripts.HiveMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;

public class ResetScreenFX extends ScriptAction {
    @Override
    public ScriptState execute() {
        Effects.ScreenFX.start(Effects.ScreenFX.Effect.NONE, 0, 0f);
        return ScriptState.COMPLETED;
    }
}
