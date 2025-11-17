package Scripts.DungeonMap;

import Level.Script;
import ScriptActions.*;
import Scripts.FinalBoss.LavaRainScriptAction;

import java.util.ArrayList;

public class WalterScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());
        
        scriptActions.add(new TextboxScriptAction() {{
            addText("Walter: Collect nectar.");
            addText("Walter: Ensure survival.");
            addText("Walter: I should've known.");
        }});
        
        scriptActions.add(new UnlockPlayerScriptAction());
        
        return scriptActions;
    }
}