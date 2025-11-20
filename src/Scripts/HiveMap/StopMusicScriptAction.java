package Scripts.HiveMap;

import Level.ScriptState;
import ScriptActions.ScriptAction;
import Sound.MusicManager;

public class StopMusicScriptAction extends ScriptAction {
    @Override
    public ScriptState execute() {
        MusicManager.stopAll();
        return ScriptState.COMPLETED;
    }
}
