package Scripts.GrassMap;

import java.util.ArrayList;

import Flowers.*;
import Level.Script;
import Level.ScriptState;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import StaticClasses.BeeStats;

public class RandomEventScriptAction extends ScriptAction {
    ArrayList<Flower> flowers = new ArrayList<>();


    @Override
    public void setup() {
        // Cosmo cosmo = new Cosmo(4, null);
    }

    @Override
    public ScriptState execute() {
        // map.getNPCs().indexOf(map.getNPCs().getLast());

        return ScriptState.COMPLETED;
    }
}
