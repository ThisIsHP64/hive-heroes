package Scripts.GrassMap;

import Level.Script;
import Level.ScriptState;
import ScriptActions.ScriptAction;

public class RandomEventScriptAction extends ScriptAction {






    @Override
    public void setup() {

    }

    @Override
    public ScriptState execute() {
        map.getNPCs().indexOf(map.getNPCs().getLast());


        return ScriptState.COMPLETED;
    }
}
