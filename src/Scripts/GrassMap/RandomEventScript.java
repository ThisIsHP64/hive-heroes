package Scripts.GrassMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.ScriptAction;
import ScriptActions.UnlockPlayerScriptAction;

public class RandomEventScript extends Script {

    // what i want this to do, is to calculate the distance that the bee has traveled, and spawn a random 
    // flower or maybe powerup after they have traveled for a bit

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new RandomEventScriptAction());

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
    
}
